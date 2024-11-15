package org.example;

public class ShadyChoice {
    private volatile boolean getWhite = false;
    private volatile int last = 0;

    public String choose() {
        int me = ThreadId.get();
        last = me;
        if (getWhite)
            return "white";
        getWhite = true;
        if (last == me)
            return "red";
        else
            return "black";
    }
}
