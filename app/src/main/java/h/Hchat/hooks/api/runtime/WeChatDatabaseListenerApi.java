package h.Hchat.hooks.api.runtime;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import h.Hchat.dexkit.DexFinder;
import h.Hchat.hooks.api.model.DatabaseChange;
import h.Hchat.hooks.core.HookRegistry;
import h.Hchat.utils.KavaReflector;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import de.robv.android.xposed.XC_MethodHook;

/**
 * 微信 SqliteDB wrapper 变更与查询监听 API。
 */
public final class WeChatDatabaseListenerApi {
    public interface Logger {
        void log(String message);
    }

    public interface Listener {
        void onDatabaseChanged(DatabaseChange change);
    }

    public interface QueryInterceptor {
        String onQuery(String sql);
    }

    public final class Subscription {
        private final Listener listener;
        private volatile boolean active = true;

        private Subscription(Listener listener) {
            this.listener = listener;
        }

        public void unsubscribe() {
            if (!active) return;
            active = false;
            listeners.remove(listener);
        }

        public boolean isActive() {
            return active;
        }
    }

    public final class QuerySubscription {
        private final QueryInterceptor interceptor;
        private volatile boolean active = true;

        private QuerySubscription(QueryInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        public void unsubscribe() {
            if (!active) return;
            active = false;
            queryInterceptors.remove(interceptor);
        }

        public boolean isActive() {
            return active;
        }
    }

    private final DexFinder dexFinder;
    private final ClassLoader classLoader;
    private final Logger logger;
    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<QueryInterceptor> queryInterceptors = new CopyOnWriteArrayList<>();
    private final Set<Method> hookedMethods = ConcurrentHashMap.newKeySet();
    private final Set<Method> hookedWrapperMutationMethods = ConcurrentHashMap.newKeySet();
    private final Set<Method> hookedWrapperInsertMethods = ConcurrentHashMap.newKeySet();
    // One logical operation can traverse wrapper, WCDB and Android SQLite hooks.
    private final ThreadLocal<Integer> mutationDepth = ThreadLocal.withInitial(() -> 0);
    private final ThreadLocal<Integer> queryDepth = ThreadLocal.withInitial(() -> 0);
    private volatile boolean installed;
    private volatile int hookedMethodCount;
    private volatile Class<?> hookedWrapperClass;

    public WeChatDatabaseListenerApi(DexFinder dexFinder, Logger logger) {
        this(dexFinder, null, logger);
    }

    public WeChatDatabaseListenerApi(DexFinder dexFinder, ClassLoader classLoader, Logger logger) {
        this.dexFinder = dexFinder;
        this.classLoader = classLoader;
        this.logger = logger;
    }

    public boolean isAvailable() {
        return (dexFinder != null && dexFinder.sqliteDbWrapperClass != null) || classLoader != null;
    }

    public boolean isInstalled() {
        return installed;
    }

    public int hookedMethodCount() {
        return hookedMethodCount;
    }

    public int hookedWrapperMutationMethodCount() {
        return hookedWrapperMutationMethods.size();
    }

    public int hookedWrapperInsertMethodCount() {
        return hookedWrapperInsertMethods.size();
    }

    public boolean isOperational() {
        Class<?> wrapperClass = dexFinder != null ? dexFinder.sqliteDbWrapperClass : null;
        // Standard WCDB/Android SQLite hooks are a valid fallback when the
        // obfuscated wrapper insert signature moved (seen in 8.0.78+).
        // Requiring a wrapper insert would incorrectly disable all database
        // observers even though hookedMethodCount contains working insert hooks.
        boolean wrapperReady = wrapperClass != null && wrapperClass == hookedWrapperClass
                && !hookedWrapperInsertMethods.isEmpty();
        boolean standardReady = hookedMethodCount > 0;
        return wrapperReady || standardReady;
    }

    public Subscription subscribe(Listener listener) {
        if (listener == null) return null;
        listeners.addIfAbsent(listener);
        return new Subscription(listener);
    }

    public void unsubscribe(Listener listener) {
        if (listener != null) listeners.remove(listener);
    }

    public QuerySubscription subscribeQuery(QueryInterceptor interceptor) {
        if (interceptor == null) return null;
        queryInterceptors.addIfAbsent(interceptor);
        return new QuerySubscription(interceptor);
    }

    public void unsubscribeQuery(QueryInterceptor interceptor) {
        if (interceptor != null) queryInterceptors.remove(interceptor);
    }

    public synchronized void install() {
        if (!isAvailable()) return;
        Class<?> wrapperClass = dexFinder != null ? dexFinder.sqliteDbWrapperClass : null;
        if (isOperational()) return;
        if (wrapperClass != hookedWrapperClass) {
            hookedWrapperMutationMethods.clear();
            hookedWrapperInsertMethods.clear();
        }
        int count = 0;
        java.util.List<Class<?>> wrappers = new java.util.ArrayList<>();
        if (dexFinder != null) wrappers.addAll(dexFinder.sqliteDbWrapperCandidates);
        if (wrapperClass != null && !wrappers.contains(wrapperClass)) wrappers.add(wrapperClass);
        for (Class<?> wrapper : wrappers) {
            Class<?> cur = wrapper;
            while (cur != null && cur != Object.class) {
                count += hookDatabaseClass(cur, true);
                cur = cur.getSuperclass();
            }
        }
        count += hookNamedDatabaseClass("com.tencent.wcdb.database.SQLiteDatabase");
        count += hookNamedDatabaseClass("com.tencent.wcdb.compat.SQLiteDatabase");
        count += hookNamedDatabaseClass("android.database.sqlite.SQLiteDatabase");
        if (wrapperClass != null) hookedWrapperClass = wrapperClass;
        hookedMethodCount = hookedMethods.size();
        installed = isOperational();
        log("数据库变更监听Hook: wrapper=" + (wrapperClass != null ? wrapperClass.getName() : "null")
                + " added=" + count + " total=" + hookedMethodCount
                + " wrapperMutations=" + hookedWrapperMutationMethods.size()
                + " wrapperInserts=" + hookedWrapperInsertMethods.size()
                + " operational=" + installed);
    }

    private String operationOf(Method method, boolean allowObfuscatedWrapperMethod) {
        if (method == null) return null;
        Class<?>[] params = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        String name = method.getName();
        if (returnType == long.class && hasContentValuesArg(params)
                && ("insert".equals(name)
                || "insertWithOnConflict".equals(name)
                || "replace".equals(name)
                || "replaceOrThrow".equals(name))) {
            return DatabaseChange.INSERT;
        }
        if (returnType == int.class && hasContentValuesArg(params)
                && ("update".equals(name) || "updateWithOnConflict".equals(name))) {
            return DatabaseChange.UPDATE;
        }
        if ("delete".equals(method.getName())
                && returnType == int.class
                && params.length == 3
                && params[0] == String.class
                && params[1] == String.class
                && params[2] == String[].class) {
            return DatabaseChange.DELETE;
        }
        if (!allowObfuscatedWrapperMethod) return null;
        if ((returnType == long.class || returnType == int.class) && isWrapperInsertSignature(params)) {
            return DatabaseChange.INSERT;
        }
        if (returnType == int.class && isWrapperUpdateSignature(params)) {
            return DatabaseChange.UPDATE;
        }
        if (returnType == int.class && isWrapperDeleteSignature(params)) {
            return DatabaseChange.DELETE;
        }
        return null;
    }

    private boolean isWrapperInsertSignature(Class<?>[] params) {
        if (params == null || params.length < 2 || params.length > 5) return false;
        int strings = 0;
        boolean values = false;
        for (Class<?> param : params) {
            if (param == String.class) strings++;
            if (ContentValues.class.isAssignableFrom(param)) values = true;
        }
        return values && strings >= 1;
    }

    private boolean isWrapperUpdateSignature(Class<?>[] params) {
        if (params == null || params.length < 3 || params.length > 6) return false;
        boolean values = false;
        boolean selection = false;
        for (Class<?> param : params) {
            if (ContentValues.class.isAssignableFrom(param)) values = true;
            if (param == String[].class) selection = true;
        }
        return values && selection;
    }

    private boolean isWrapperDeleteSignature(Class<?>[] params) {
        return params != null
                && params.length == 3
                && params[0] == String.class
                && params[1] == String.class
                && params[2] == String[].class;
    }

    private int hookNamedDatabaseClass(String className) {
        if (classLoader == null) return 0;
        try {
            return hookDatabaseClass(KavaReflector.loadClass(className, classLoader), false);
        } catch (Throwable ignored) {
            return 0;
        }
    }

    private int hookDatabaseClass(Class<?> dbClass, boolean allowObfuscatedWrapperMethod) {
        if (dbClass == null) return 0;
        int count = 0;
        for (Method method : KavaReflector.declaredMethods(dbClass)) {
            String operation = operationOf(method, allowObfuscatedWrapperMethod);
            boolean query = isRawQueryMethod(method);
            boolean potentialMutation = allowObfuscatedWrapperMethod && isPotentialWrapperMutationMethod(method);
            if (operation == null && !query && !potentialMutation) continue;
            if (hookedMethods.contains(method)) {
                if (allowObfuscatedWrapperMethod && (operation != null || potentialMutation)) {
                    hookedWrapperMutationMethods.add(method);
                    if (DatabaseChange.INSERT.equals(operation) || potentialMutation) {
                        hookedWrapperInsertMethods.add(method);
                    }
                }
                continue;
            }
            try {
                HookRegistry.get().hook(method, new XC_MethodHook(de.robv.android.xposed.callbacks.XCallback.PRIORITY_LOWEST) {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        if (query) {
                            if (enterQuery()) transformQuery(param.args);
                        } else {
                            enterMutation();
                            normalizeLegacyMsgSource(operation, param.thisObject, param.args);
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        if (query) {
                            leaveQuery();
                        } else if (leaveMutation()) {
                            dispatchIfSuccessful(operation, method, param.args, param.getResult());
                        }
                    }
                });
                hookedMethods.add(method);
                if (allowObfuscatedWrapperMethod && (operation != null || potentialMutation)) {
                    hookedWrapperMutationMethods.add(method);
                    if (DatabaseChange.INSERT.equals(operation) || potentialMutation) {
                        hookedWrapperInsertMethods.add(method);
                    }
                }
                count++;
            } catch (Throwable error) {
                log("数据库方法Hook失败: " + method + " " + error.getMessage());
            }
        }
        return count;
    }

    private boolean isRawQueryMethod(Method method) {
        if (method == null || !Cursor.class.isAssignableFrom(method.getReturnType())) return false;
        String name = method.getName();
        if (!"rawQuery".equals(name) && !"rawQueryWithFactory".equals(name)) return false;
        for (Class<?> type : method.getParameterTypes()) {
            if (type == String.class) return true;
        }
        return false;
    }

    private boolean enterQuery() {
        int depth = queryDepth.get();
        queryDepth.set(depth + 1);
        return depth == 0;
    }

    private void leaveQuery() {
        int depth = queryDepth.get();
        if (depth <= 1) {
            queryDepth.remove();
        } else {
            queryDepth.set(depth - 1);
        }
    }

    private void transformQuery(Object[] args) {
        if (args == null || queryInterceptors.isEmpty()) return;
        int sqlIndex = -1;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                sqlIndex = i;
                break;
            }
        }
        if (sqlIndex < 0) return;
        String sql = (String) args[sqlIndex];
        if (TextUtils.isEmpty(sql)) return;
        String rewritten = sql;
        for (QueryInterceptor interceptor : queryInterceptors) {
            try {
                String next = interceptor.onQuery(rewritten);
                if (next != null) rewritten = next;
            } catch (Throwable error) {
                log("数据库查询拦截器异常: " + error.getMessage());
            }
        }
        if (!rewritten.equals(sql)) args[sqlIndex] = rewritten;
    }

    private void enterMutation() {
        mutationDepth.set(mutationDepth.get() + 1);
    }

    private boolean leaveMutation() {
        int depth = mutationDepth.get();
        if (depth <= 1) {
            mutationDepth.remove();
            return true;
        }
        mutationDepth.set(depth - 1);
        return false;
    }

    private void normalizeLegacyMsgSource(String operation, Object database, Object[] args) {
        if (args == null) return;
        if (!DatabaseChange.INSERT.equals(operation) && !DatabaseChange.UPDATE.equals(operation)) return;
        ContentValues values = contentValuesArg(args);
        if (values == null) return;
        String table = tableArg(args);
        if (!isMessageTable(table, values)) return;
        if (!values.containsKey("msgSource")) return;
        String current = contentValueString(values, "msgSource");
        if (!TextUtils.isEmpty(current)) return;
        String source = msgSourceFromLvBuffer(values);
        if (TextUtils.isEmpty(source)) return;
        values.put("msgSource", source);
    }

    private boolean isMessageTable(String table, ContentValues values) {
        if (TextUtils.isEmpty(table)) return false;
        String lower = table.toLowerCase();
        boolean tableLooksLikeMessage = "message".equals(lower)
                || lower.startsWith("message_")
                || lower.endsWith("_message");
        if (!tableLooksLikeMessage) return false;
        return values != null
                && (values.containsKey("msgSource")
                || values.containsKey("lvbuffer")
                || values.containsKey("msgId")
                || values.containsKey("msgSvrId"));
    }

    private String msgSourceFromLvBuffer(ContentValues values) {
        if (values == null || !values.containsKey("lvbuffer")) return "";
        try {
            byte[] bytes = values.getAsByteArray("lvbuffer");
            if (bytes != null && bytes.length > 0) {
                String source = extractMsgSource(new String(bytes, StandardCharsets.UTF_8));
                if (!TextUtils.isEmpty(source)) return source;
            }
        } catch (Throwable ignored) {
        }
        try {
            Object raw = values.get("lvbuffer");
            return raw != null ? extractMsgSource(String.valueOf(raw)) : "";
        } catch (Throwable ignored) {
            return "";
        }
    }

    private String contentValueString(ContentValues values, String key) {
        if (values == null || TextUtils.isEmpty(key) || !values.containsKey(key)) return "";
        try {
            String value = values.getAsString(key);
            return value != null ? value : "";
        } catch (Throwable ignored) {
            Object raw = values.get(key);
            return raw != null ? String.valueOf(raw) : "";
        }
    }

    private String extractMsgSource(String value) {
        if (TextUtils.isEmpty(value)) return "";
        String lower = value.toLowerCase();
        int start = lower.indexOf("<msgsource");
        if (start < 0) return "";
        int end = lower.indexOf("</msgsource>", start);
        if (end < 0) return "";
        end += "</msgsource>".length();
        return value.substring(start, Math.min(end, value.length())).trim();
    }

    private boolean hasContentValuesArg(Class<?>[] params) {
        if (params == null) return false;
        for (Class<?> param : params) {
            if (ContentValues.class.isAssignableFrom(param)) return true;
        }
        return false;
    }

    private boolean isPotentialWrapperMutationMethod(Method method) {
        if (method == null || method.getReturnType() == void.class) return false;
        Class<?> returnType = method.getReturnType();
        if (returnType != long.class && returnType != int.class && returnType != boolean.class
                && returnType != Boolean.class && returnType != Long.class && returnType != Integer.class) return false;
        Class<?>[] params = method.getParameterTypes();
        if (params.length < 2 || params.length > 8) return false;
        boolean string = false;
        boolean object = false;
        for (Class<?> type : params) {
            if (type == String.class) string = true;
            if (!type.isPrimitive() && type != String.class && type != String[].class
                    && type != Object[].class && type != Class.class) object = true;
        }
        return string && object;
    }

    private void dispatchIfSuccessful(String operation, Method method, Object[] args, Object result) {
        if (listeners.isEmpty() || args == null) return;
        if (operation == null) operation = inferOperation(method, args);
        if (operation == null) return;
        long resultValue = resultToLong(result);
        if (DatabaseChange.INSERT.equals(operation)) {
            if (resultValue < 0) return;
            dispatch(new DatabaseChange(
                    operation,
                    tableArg(args),
                    nullColumnHackArg(args),
                    contentValuesArg(args),
                    null,
                    null,
                    resultValue,
                    method.getName()));
            return;
        }
        if (DatabaseChange.UPDATE.equals(operation)) {
            if (resultValue <= 0) return;
            dispatch(new DatabaseChange(
                    operation,
                    tableArg(args),
                    null,
                    contentValuesArg(args),
                    whereClauseArg(args),
                    stringArrayArg(args),
                    resultValue,
                    method.getName()));
            return;
        }
        if (DatabaseChange.DELETE.equals(operation)) {
            if (resultValue <= 0 || args.length < 3) return;
            dispatch(new DatabaseChange(
                    operation,
                    tableArg(args),
                    null,
                    null,
                    whereClauseArgForDelete(args),
                    stringArrayArg(args),
                    resultValue,
                    method.getName()));
        }
    }

    private void dispatch(DatabaseChange change) {
        for (Listener listener : listeners) {
            try {
                listener.onDatabaseChanged(change);
            } catch (Throwable e) {
                log("数据库变更监听回调失败: " + e.getMessage());
            }
        }
    }

    private long resultToLong(Object result) {
        return result instanceof Number ? ((Number) result).longValue() : 0;
    }

    private String tableArg(Object[] args) {
        if (args == null || args.length == 0) return "";
        if (args[0] instanceof String) return (String) args[0];
        for (Object arg : args) {
            if (!(arg instanceof String)) continue;
            String value = (String) arg;
            if ("message".equalsIgnoreCase(value) || value.toLowerCase().contains("message")) return value;
        }
        return "";
    }

    private String nullColumnHackArg(Object[] args) {
        if (args == null) return "";
        for (int i = 1; i < args.length; i++) {
            Object value = args[i];
            if (value instanceof String) return (String) value;
            if (value instanceof ContentValues) return "";
        }
        return "";
    }

    private ContentValues contentValuesArg(Object[] args) {
        if (args == null) return null;
        for (Object value : args) {
            ContentValues converted = coerceContentValues(value, 0,
                    java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<Object, Boolean>()));
            if (converted != null) return converted;
        }
        return null;
    }

    private ContentValues coerceContentValues(Object value, int depth, Set<Object> visited) {
        if (value == null || depth > 3 || !visited.add(value)) return null;
        if (value instanceof ContentValues) return (ContentValues) value;
        if (value instanceof Map) {
            ContentValues out = new ContentValues();
            for (Object raw : ((Map<?, ?>) value).entrySet()) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) raw;
                if (entry.getKey() != null && entry.getValue() != null) {
                    Object item = entry.getValue();
                    if (item instanceof byte[]) out.put(String.valueOf(entry.getKey()), (byte[]) item);
                    else if (item instanceof Integer) out.put(String.valueOf(entry.getKey()), (Integer) item);
                    else if (item instanceof Long) out.put(String.valueOf(entry.getKey()), (Long) item);
                    else if (item instanceof Float) out.put(String.valueOf(entry.getKey()), (Float) item);
                    else if (item instanceof Double) out.put(String.valueOf(entry.getKey()), (Double) item);
                    else out.put(String.valueOf(entry.getKey()), String.valueOf(item));
                }
            }
            return out;
        }
        Class<?> type = value.getClass();
        if (type.getName().startsWith("java.") || type.getName().startsWith("android.")) return null;
        for (Field field : KavaReflector.declaredFields(type)) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) || field.getType().isPrimitive()) continue;
            ContentValues nested = coerceContentValues(KavaReflector.readField(field, value), depth + 1, visited);
            if (nested != null) return nested;
        }
        for (String name : new String[]{"getValues", "contentValues", "toContentValues", "getContentValues"}) {
            ContentValues nested = coerceContentValues(KavaReflector.invokeMethod(value, name), depth + 1, visited);
            if (nested != null) return nested;
        }
        return null;
    }

    private String inferOperation(Method method, Object[] args) {
        if (method == null) return null;
        String name = method.getName().toLowerCase();
        if (name.contains("delete") || name.equals("d")) return DatabaseChange.DELETE;
        if (name.contains("update") || name.equals("updatewithonconflict") || name.equals("u")) return DatabaseChange.UPDATE;
        if (name.contains("insert") || name.contains("replace") || name.equals("i")) return DatabaseChange.INSERT;
        if (contentValuesArg(args) == null) return null;
        if (stringArrayArg(args) != null) return DatabaseChange.UPDATE;
        for (Object arg : args) if (arg instanceof String) return DatabaseChange.INSERT;
        return null;
    }

    private String whereClauseArg(Object[] args) {
        if (args == null) return "";
        boolean afterValues = false;
        for (Object value : args) {
            if (value instanceof ContentValues) {
                afterValues = true;
                continue;
            }
            if (afterValues && value instanceof String) return (String) value;
        }
        return "";
    }

    private String whereClauseArgForDelete(Object[] args) {
        if (args == null) return "";
        for (int i = 1; i < args.length; i++) {
            Object value = args[i];
            if (value instanceof String) return (String) value;
        }
        return "";
    }

    private String[] stringArrayArg(Object[] args) {
        if (args == null) return null;
        Object value = null;
        for (Object arg : args) {
            if (arg instanceof String[]) {
                value = arg;
                break;
            }
        }
        return value instanceof String[] ? (String[]) value : null;
    }

    private void log(String message) {
        if (logger != null) logger.log("[WeChatDatabaseListenerApi] " + message);
    }
}
