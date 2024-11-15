package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadId {
    private static final AtomicInteger nextId = new AtomicInteger(1);
    private static final ThreadLocal<Integer> threadId = ThreadLocal.withInitial(nextId::getAndIncrement);

    public static int get() {
        return threadId.get();
    }
}
