package org.example;

public class ShadyLock {
    private volatile int turn;
    private volatile boolean used = false;

    public void lock() {
        int me = ThreadId.get();
        do {
            do {
                turn = me;
            } while (used);
            used = true;
        } while (turn != me);
    }

    public void unlock () {
        used = false;
    }
}
