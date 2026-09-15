package h.Hchat.hooks.api.message;

import android.content.ContentValues;
import android.text.TextUtils;
import de.robv.android.xposed.XC_MethodHook;
import h.Hchat.dexkit.DexFinder;
import h.Hchat.hooks.core.HookRegistry;
import h.Hchat.utils.KavaReflector;
import java.lang.reflect.Method;

import h.Hchat.hooks.api.contact.WeChatAccountApi;
import h.Hchat.hooks.api.model.DatabaseChange;
import h.Hchat.hooks.api.model.WeChatMessage;
import h.Hchat.hooks.api.runtime.WeChatDatabaseListenerApi;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * message 表变更监听 API。
 */
public final class WeChatMessageChangeApi {
    public interface Logger {
        void log(String message);
    }

    public interface Listener {
        void onMessageChanged(MessageChange change);
    }

    public static final class MessageChange {
        public final DatabaseChange databaseChange;
        public final WeChatMessage message;

        private MessageChange(DatabaseChange databaseChange, WeChatMessage message) {
            this.databaseChange = databaseChange;
            this.message = message;
        }

        public String operation() {
            return databaseChange != null ? databaseChange.operation : "";
        }

        public boolean isInsert() {
            return databaseChange != null && databaseChange.isInsert();
        }

        public boolean isUpdate() {
            return databaseChange != null && databaseChange.isUpdate();
        }

        public boolean isDelete() {
            return databaseChange != null && databaseChange.isDelete();
        }
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

    private final WeChatDatabaseListenerApi databaseListenerApi;
    private final WeChatMessageStoreApi messageStoreApi;
    private final WeChatAccountApi accountApi;
    private final Logger logger;
    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();
    private volatile boolean installed;
    private final DexFinder dexFinder;
    private volatile boolean databaseSubscribed;
    private volatile boolean storageHookInstalled;
    private final ThreadLocal<Integer> storageDepth = ThreadLocal.withInitial(() -> 0);

    public WeChatMessageChangeApi(WeChatDatabaseListenerApi databaseListenerApi,
                                  WeChatMessageStoreApi messageStoreApi,
                                  Logger logger) {
        this(databaseListenerApi, messageStoreApi, null, logger);
    }

    public WeChatMessageChangeApi(WeChatDatabaseListenerApi databaseListenerApi,
                                  WeChatMessageStoreApi messageStoreApi,
                                  WeChatAccountApi accountApi,
                                  Logger logger) {
        this(databaseListenerApi, messageStoreApi, accountApi, null, logger);
    }

    public WeChatMessageChangeApi(WeChatDatabaseListenerApi databaseListenerApi,
                                  WeChatMessageStoreApi messageStoreApi,
                                  WeChatAccountApi accountApi,
                                  DexFinder dexFinder,
                                  Logger logger) {
        this.databaseListenerApi = databaseListenerApi;
        this.messageStoreApi = messageStoreApi;
        this.accountApi = accountApi;
        this.logger = logger;
        this.dexFinder = dexFinder;
    }

    public boolean isAvailable() {
        return installed;
    }

    public boolean isInstalled() {
        return installed;
    }

    public Subscription subscribe(Listener listener) {
        if (listener == null) return null;
        listeners.addIfAbsent(listener);
        return new Subscription(listener);
    }

    public void unsubscribe(Listener listener) {
        if (listener != null) listeners.remove(listener);
    }

    public synchronized void install() {
        try {
            installStorageHook();
        } catch (Throwable error) {
            log("消息存储监听安装失败: " + error.getMessage());
        }
        if (databaseListenerApi == null || databaseSubscribed) return;
        databaseListenerApi.install();
        if (!databaseListenerApi.isOperational()) {
            log("消息变更监听未就绪: databaseHooks="
                    + databaseListenerApi.hookedMethodCount()
                    + " wrapperHooks="
                    + databaseListenerApi.hookedWrapperMutationMethodCount()
                    + " wrapperInserts="
                    + databaseListenerApi.hookedWrapperInsertMethodCount());
            return;
        }
        databaseListenerApi.subscribe(change -> {
            if (storageDepth.get() == 0) onDatabaseChanged(change);
        });
        databaseSubscribed = true;
        installed = true;
        log("消息变更监听已安装");
    }

    private void installStorageHook() {
        if (storageHookInstalled || dexFinder == null) return;
        dexFinder.resolveMessageStorageInsert();
        Method insert = dexFinder.messageStorageInsertMethod;
        if (insert == null) return;
        Method convert = KavaReflector.findMethodRecursive(insert.getParameterTypes()[0], "convertTo");
        if (convert == null || convert.getReturnType() != ContentValues.class) return;
        HookRegistry.get().hook(insert, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                storageDepth.set(storageDepth.get() + 1);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                int depth = storageDepth.get() - 1;
                if (depth > 0) {
                    storageDepth.set(depth);
                    return;
                }
                storageDepth.remove();
                if (param.hasThrowable() || listeners.isEmpty()) return;
                Object result = param.getResult();
                if (!(result instanceof Number) || ((Number) result).longValue() <= 0L) return;
                Object snapshot = KavaReflector.invoke(convert, param.args[0]);
                if (!(snapshot instanceof ContentValues)) return;
                ContentValues values = new ContentValues((ContentValues) snapshot);
                values.put("msgId", ((Number) result).longValue());
                onDatabaseChanged(new DatabaseChange(DatabaseChange.INSERT, "message", null,
                        values, null, null, ((Number) result).longValue(), "storage:" + insert.getName()));
            }
        });
        storageHookInstalled = true;
        installed = true;
        log("消息存储监听已安装: " + insert);
    }

    private void onDatabaseChanged(DatabaseChange change) {
        if (change == null || !isMessageTable(change.table)) return;
        WeChatMessage message = resolveMessage(change);
        MessageChange event = new MessageChange(change, message);
        for (Listener listener : listeners) {
            try {
                listener.onMessageChanged(event);
            } catch (Throwable e) {
                log("消息变更回调失败: " + e.getMessage());
            }
        }
    }

    private boolean isMessageTable(String table) {
        if (TextUtils.isEmpty(table)) return false;
        String lower = table.toLowerCase(java.util.Locale.US);
        return "message".equals(lower)
                || lower.startsWith("message_")
                || lower.endsWith("_message");
    }

    private WeChatMessage resolveMessage(DatabaseChange change) {
        long msgId = resolveMsgId(change);
        boolean complete = change.values != null && change.values.containsKey("talker")
                && change.values.containsKey("content") && change.values.containsKey("type")
                && change.values.containsKey("isSend") && change.values.containsKey("createTime");
        if (!complete && msgId > 0 && messageStoreApi != null) {
            WeChatMessage stored = messageStoreApi.getMessageById(msgId);
            if (stored != null) return stored;
        }
        if (change.values == null) return null;
        return new WeChatMessage(
                msgId,
                longValue(change.values, "msgSvrId"),
                intValue(change.values, "type"),
                intValue(change.values, "status"),
                intValue(change.values, "isSend"),
                longValue(change.values, "createTime"),
                str(change.values, "talker"),
                str(change.values, "content"),
                str(change.values, "imgPath"),
                str(change.values, "reserved"),
                str(change.values, "transContent"),
                intValue(change.values, "flag"),
                str(change.values, "msgSource"),
                selfWxId());
    }

    private String selfWxId() {
        return accountApi != null ? accountApi.selfWxId() : "";
    }

    private long resolveMsgId(DatabaseChange change) {
        if (change == null) return 0L;
        long msgId = longValue(change.values, "msgId");
        if (msgId > 0) return msgId;
        msgId = longValue(change.values, "msgid");
        if (msgId > 0) return msgId;
        msgId = longValue(change.values, "_id");
        if (msgId > 0) return msgId;
        msgId = longValue(change.values, "rowid");
        if (msgId > 0) return msgId;
        if (change.isInsert() && change.result > 0) return change.result;
        return msgIdFromWhere(change.whereClause, change.whereArgs);
    }

    private long msgIdFromWhere(String whereClause, String[] whereArgs) {
        if (TextUtils.isEmpty(whereClause) || whereArgs == null || whereArgs.length == 0) return 0L;
        String where = whereClause.toLowerCase(java.util.Locale.US);
        if (!where.contains("msgid")) return 0L;
        for (String arg : whereArgs) {
            if (TextUtils.isEmpty(arg)) continue;
            try {
                long value = Long.parseLong(arg);
                if (value > 0) return value;
            } catch (Throwable ignored) {}
        }
        return 0L;
    }

    private static String str(ContentValues values, String key) {
        if (values == null || TextUtils.isEmpty(key) || !values.containsKey(key)) return "";
        Object value = values.get(key);
        return value != null ? String.valueOf(value) : "";
    }

    private static int intValue(ContentValues values, String key) {
        Long value = longBox(values, key);
        return value != null ? value.intValue() : 0;
    }

    private static long longValue(ContentValues values, String key) {
        Long value = longBox(values, key);
        return value != null ? value : 0L;
    }

    private static Long longBox(ContentValues values, String key) {
        if (values == null || TextUtils.isEmpty(key) || !values.containsKey(key)) return null;
        try {
            return values.getAsLong(key);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private void log(String message) {
        if (logger != null) logger.log("[WeChatMessageChangeApi] " + message);
    }
}
