package com.ancely.fyw.lock;

public class FairSync extends Sync {

    final void lock() {
        acquire(1);
    }

    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            boolean hasQueuedPredecessors = hasQueuedPredecessors();
            if (!hasQueuedPredecessors) {
                boolean b = compareAndSetState(0, acquires);
                if (b) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
        } else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}