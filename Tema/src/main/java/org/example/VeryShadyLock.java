package org.example;


public class VeryShadyLock {
    private DummyLock lock = new DummyLock();
    private volatile int x, y = 0;

    public void lock() {
        int me = ThreadId.get();
        x = me;
        while (y != 0) {};
        y = me;
        if (x != me) {
            lock.lock();
        }
    }

    public void unlock() {
        y = 0;
        lock.unlock();
    }
}
