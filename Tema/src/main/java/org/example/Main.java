package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        //exercise4A();
        //exercise4B();
        //exercise4C();
        exercise5();
    }
    public static void exercise4A() {
        ShadyLock lock = new ShadyLock();
        int numThreads = 3;
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                System.out.println("Thread " + ThreadId.get() + " tries acquiring the lock");
                lock.lock();
                System.out.println("Thread " + ThreadId.get() + " acquired the lock");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
                System.out.println("Thread " + ThreadId.get() + " released the lock");
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void exercise4B() {
        VeryShadyLock lock = new VeryShadyLock();
        int numThreads = 3;
        int limit = 6;
        AtomicInteger counter = new AtomicInteger(0);
        AtomicInteger[] localCounters = new AtomicInteger[numThreads];
        for (int i = 0; i < numThreads; i++) {
            localCounters[i] = new AtomicInteger(0);
        }
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                while(counter.get() < limit) {
                    //System.out.println("Thread " + ThreadId.get() + " tries acquiring the lock");
                    lock.lock();
                    System.out.println("Thread " + ThreadId.get() + " acquired the lock");
                    if (counter.get() < limit) {
                        counter.incrementAndGet();
                        localCounters[index].incrementAndGet();
                    }
                    //System.out.println("Counter: " + counter.get() + " Thread: " + ThreadId.get());
                    lock.unlock();
                    System.out.println("Thread " + ThreadId.get() + " released the lock");
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < numThreads; i++) {
            System.out.println("Thread " + i + " counter: " + localCounters[i].get());
        }
    }
    public static void exercise4C() {
        int numThreads = 3;
        ShadyChoice shadyChoice = new ShadyChoice();

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++)
        {
            threads[i] = new Thread(() -> {
                System.out.println("Thread " + ThreadId.get() + " tries choosing");
                String choice = shadyChoice.choose();
                System.out.println("Thread " + ThreadId.get() + " chose " + choice);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void exercise5() {
        int numThreads = 3;
        int limit = 150000;
        PetersonFairLock lock = new PetersonFairLock(numThreads, limit);
        lock.startCounter();
    }
}