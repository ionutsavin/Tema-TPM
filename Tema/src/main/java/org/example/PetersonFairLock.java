package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class PetersonFairLock {
    private final int n;
    private final AtomicInteger[] level;
    private final AtomicInteger[] victim;
    private final AtomicInteger[] turns;
    private final AtomicInteger sharedCounter = new AtomicInteger(0);
    private final int limit;

    public PetersonFairLock(int n, int limit) {
        this.n = n;
        this.limit = limit;
        level = new AtomicInteger[n];
        victim = new AtomicInteger[n];
        turns = new AtomicInteger[n];
        for (int i = 0; i < n; i++) {
            level[i] = new AtomicInteger(0);
            victim[i] = new AtomicInteger(-1);
            turns[i] = new AtomicInteger(0);
        }
    }

    public void lock(int i) {
        int myTurn = turns[i].incrementAndGet();
        for (int L = 1; L < n; L++) {
            level[i].set(L);
            victim[L].set(i);

            while (existsHigherOrEqualLevel(i, L, myTurn) && victim[L].get() == i) {
                Thread.yield();
            }
        }
    }

    public void unlock(int i) {
        level[i].set(0);
    }

    private boolean existsHigherOrEqualLevel(int i, int L, int myTurn) {
        for (int k = 0; k < n; k++) {
            if (k != i && level[k].get() >= L) {
                if(turns[k].get() < myTurn)
                    return true;
            }
        }
        return false;
    }

    class CounterThread extends Thread {
        private final int id;
        private int localCount = 0;

        public CounterThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (sharedCounter.get() < limit) {
                lock(id);

                if (sharedCounter.get() < limit) {
                    sharedCounter.incrementAndGet();
                    localCount++;
                    //System.out.println("Thread " + id + " a incrementat contorul");
                }

                unlock(id);
            }

            System.out.println("Thread " + id + " a incrementat contorul de " + localCount + " ori.");
        }
    }

    public void startCounter() {
        long startTime = System.currentTimeMillis();

        CounterThread[] threads = new CounterThread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = new CounterThread(i);
            threads[i].start();
        }

        for (int i = 0; i < n; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Timpul total de executie: " + (endTime - startTime) + " ms");
        System.out.println("Valoarea finala a contorului partajat: " + sharedCounter.get());
    }
}