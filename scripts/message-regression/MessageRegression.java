import h.Hchat.event.Events;
import h.Hchat.hooks.api.message.WeChatMessageChangeApi;
import h.Hchat.hooks.api.message.WeChatMessageObserveApi;
import h.Hchat.hooks.api.model.DatabaseChange;
import h.Hchat.hooks.api.model.WeChatMessage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public final class MessageRegression {
    private static final long NOW = System.currentTimeMillis();
    private static final Method PB = method("onMessageReceived", Events.MessageReceived.class);
    private static final Method DB = method("onMessageChanged", WeChatMessageChangeApi.MessageChange.class);
    private static final Method CLAIM = method("claimIncomingMessage", WeChatMessage.class);
    private static int checks;

    private static Method method(String name, Class<?> type) {
        try {
            Method method = WeChatMessageObserveApi.class.getDeclaredMethod(name, type);
            method.setAccessible(true);
            return method;
        } catch (Exception e) { throw new AssertionError(e); }
    }

    private static WeChatMessage message(long svr, long local, long time) {
        return new WeChatMessage(local, svr, 1, 3, 0, time,
                "friend", "hello", "", "", "", 0);
    }

    private static WeChatMessageObserveApi observer(List<WeChatMessageObserveApi.ObservedMessage> out) throws Exception {
        WeChatMessageObserveApi api = new WeChatMessageObserveApi(null, null, null, null, null);
        Field start = WeChatMessageObserveApi.class.getDeclaredField("databaseIncomingStartedAt");
        start.setAccessible(true);
        start.setLong(api, NOW);
        Field pb = WeChatMessageObserveApi.class.getDeclaredField("pbLayerActive");
        pb.setAccessible(true);
        pb.setBoolean(api, true); // Database input must survive an active PB layer.
        api.subscribe(out::add);
        return api;
    }

    private static void pb(WeChatMessageObserveApi api, long svr) throws Exception {
        PB.invoke(api, new Events.MessageReceived("hello", "friend", "friend", "hello", "1", NOW / 1000, svr));
    }

    private static void db(WeChatMessageObserveApi api, long svr, long local, long time, String operation) throws Exception {
        Constructor<WeChatMessageChangeApi.MessageChange> ctor =
                WeChatMessageChangeApi.MessageChange.class.getDeclaredConstructor(DatabaseChange.class, WeChatMessage.class);
        ctor.setAccessible(true);
        DB.invoke(api, ctor.newInstance(new DatabaseChange(operation, "message", null, null, null, null, local, "storage:Bb"),
                message(svr, local, time)));
    }

    private static void check(boolean value, String label) {
        if (!value) throw new AssertionError(label);
        checks++;
        System.out.println("PASS " + label);
    }

    public static void main(String[] args) throws Exception {
        List<WeChatMessageObserveApi.ObservedMessage> out = new ArrayList<>();
        WeChatMessageObserveApi api = observer(out);
        pb(api, 10);
        db(api, 10, 1, NOW, DatabaseChange.INSERT);
        check(out.size() == 1, "PB then database delivers once");
        db(api, 11, 2, NOW, DatabaseChange.INSERT);
        pb(api, 11);
        check(out.size() == 2, "database then PB delivers once");
        db(api, 12, 3, NOW, DatabaseChange.INSERT);
        check(out.size() == 3, "database-only input survives active PB hook");
        check(out.get(2).sender.equals("friend"), "database sender preserved");
        db(api, 12, 3, NOW, DatabaseChange.INSERT);
        check(out.size() == 3, "repeated insert suppressed");
        db(api, 13, 4, NOW, DatabaseChange.UPDATE);
        db(api, 14, 5, NOW - 600_000, DatabaseChange.INSERT);
        check(out.size() == 3, "updates and historical rows do not auto-reply");
        pb(api, 15);
        pb(api, 16);
        check(out.size() == 5, "same text with distinct server IDs is not lost");

        AtomicInteger accepted = new AtomicInteger();
        CountDownLatch gate = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            Thread t = new Thread(() -> {
                try {
                    gate.await();
                    if ((Boolean) CLAIM.invoke(api, message(100, 10, NOW))) accepted.incrementAndGet();
                } catch (Exception e) { throw new AssertionError(e); }
            });
            threads.add(t);
            t.start();
        }
        gate.countDown();
        for (Thread t : threads) t.join();
        check(accepted.get() == 1, "concurrent PB and database claims are atomic");
        Field cache = WeChatMessageObserveApi.class.getDeclaredField("recentIncomingDatabaseMessages");
        cache.setAccessible(true);
        @SuppressWarnings("unchecked") Map<String, Long> entries = (Map<String, Long>) cache.get(api);
        entries.put("svr:friend:100", NOW - 7_200_000);
        check((Boolean) CLAIM.invoke(api, message(100, 10, NOW)), "expired identity can be claimed again");
        for (int i = 200; i < 4500; i++) CLAIM.invoke(api, message(i, i, NOW));
        check(entries.size() <= 4096, "dedup cache is bounded");
        System.out.println("Message regression: " + checks + " checks passed");
    }
}
