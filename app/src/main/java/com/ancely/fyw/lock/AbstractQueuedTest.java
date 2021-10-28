package com.ancely.fyw.lock;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.lock
 *  @文件名:   AbstractQueuedTest
 *  @创建者:   admin
 *  @创建时间:  2021/10/28 16:48
 *  @描述：    TODO
 */

import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.LockSupport;

public class AbstractQueuedTest extends AbstractOwnableSynchronizer implements java.io.Serializable {
    private transient volatile Node head;
    private transient volatile Node tail;
    private volatile int state;

    private static final long STATE;
    private static final long HEAD;
    private static final long TAIL;

    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    protected final void setState(int newState) {
        state = newState;
    }

    protected final int getState() {
        return state;
    }

    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    protected final boolean compareAndSetState(int expect, int update) {
        return ReflectUtils.compareAndSwapInt(this, STATE, expect, update);
    }

    public final void acquireInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }

    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }

    private void doAcquireInterruptibly(int arg)
            throws InterruptedException {
        final Node node = addWaiter(Node.EXCLUSIVE);
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    return;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } catch (Throwable t) {
            cancelAcquire(node);
            throw t;
        }
    }

    public final boolean hasQueuedPredecessors() {
        Node t = tail;
        Node h = head;
        Node s;
        return h != t && ((s = h.next) == null || s.thread != Thread.currentThread());
    }

    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }


    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    public final void acquire(int arg) {
        boolean isAcquire = tryAcquire(arg);
        if (!isAcquire){
            Node node = addWaiter(Node.EXCLUSIVE);
            boolean b = acquireQueued(node, arg);
            if (b){
                selfInterrupt();
            }
        }
    }

    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    final boolean acquireQueued(final Node node, int arg) {
        try {
            boolean interrupted = false;
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } catch (Throwable t) {
            cancelAcquire(node);
            throw t;
        }
    }

    private void cancelAcquire(Node node) {
        if (node == null)
            return;

        node.thread = null;

        Node pred = node.prev;
        while (pred.waitStatus > 0)
            node.prev = pred = pred.prev;

        Node predNext = pred.next;
        node.waitStatus = Node.CANCELLED;

        if (node == tail && compareAndSetTail(node, pred)) {
            pred.compareAndSetNext(predNext, null);
        } else {
            int ws;
            if (pred != head &&
                    ((ws = pred.waitStatus) == Node.SIGNAL ||
                            (ws <= 0 && pred.compareAndSetWaitStatus(ws, Node.SIGNAL))) &&
                    pred.thread != null) {
                Node next = node.next;
                if (next != null && next.waitStatus <= 0)
                    pred.compareAndSetNext(predNext, next);
            } else {
                unparkSuccessor(node);
            }

            node.next = node; // help GC
        }
    }

    private void unparkSuccessor(Node node) {

        int ws = node.waitStatus;
        if (ws < 0)
            node.compareAndSetWaitStatus(ws, 0);

        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node p = tail; p != node && p != null; p = p.prev)
                if (p.waitStatus <= 0)
                    s = p;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }

    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL)
            return true;
        if (ws > 0) {
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            pred.compareAndSetWaitStatus(ws, Node.SIGNAL);
        }
        return false;
    }

    private Node addWaiter(Node mode) {
        Node node = new Node(mode);

        for (; ; ) {
            Node oldTail = tail;
            if (oldTail != null) {
                ReflectUtils.putObject(node, Node.PREV, oldTail);
                if (compareAndSetTail(oldTail, node)) {
                    oldTail.next = node;
                    return node;
                }
            } else {
                initializeSyncQueue();
            }
        }
    }

    private void initializeSyncQueue() {
        Node h;
        if (ReflectUtils.compareAndSwapObject(this, HEAD, null, (h = new Node())))
            tail = h;
    }

    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    private boolean compareAndSetTail(Node expect, Node update) {
        return ReflectUtils.compareAndSwapObject(this, TAIL, expect, update);
    }


    static {
        try {
            STATE = ReflectUtils.objectFieldOffset
                    (AbstractQueuedTest.class.getDeclaredField("state"));
            HEAD = ReflectUtils.objectFieldOffset
                    (AbstractQueuedTest.class.getDeclaredField("head"));
            TAIL = ReflectUtils.objectFieldOffset
                    (AbstractQueuedTest.class.getDeclaredField("tail"));
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }

        Class<?> ensureLoaded = LockSupport.class;
    }

    static final class Node {
        static final Node SHARED = new Node();
        static final Node EXCLUSIVE = null;
        static final int CANCELLED = 1;
        static final int SIGNAL = -1;
        static final int CONDITION = -2;

        static final int PROPAGATE = -3;

        volatile int waitStatus;

        volatile Node prev;

        volatile Node next;

        volatile Thread thread;

        Node nextWaiter;

        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {
        }

        Node(Node nextWaiter) {
            this.nextWaiter = nextWaiter;
            ReflectUtils.putObject(this, THREAD, Thread.currentThread());
        }

        private static final long NEXT;
        static final long PREV;
        private static final long THREAD;
        private static final long WAITSTATUS;

        static {
            try {
                NEXT = ReflectUtils.objectFieldOffset
                        (Node.class.getDeclaredField("next"));
                PREV = ReflectUtils.objectFieldOffset
                        (Node.class.getDeclaredField("prev"));
                THREAD = ReflectUtils.objectFieldOffset
                        (Node.class.getDeclaredField("thread"));
                WAITSTATUS = ReflectUtils.objectFieldOffset
                        (Node.class.getDeclaredField("waitStatus"));
            } catch (ReflectiveOperationException e) {
                throw new Error(e);
            }
        }

        final boolean compareAndSetWaitStatus(int expect, int update) {
            return ReflectUtils.compareAndSwapInt(this, Node.WAITSTATUS, expect, update);
        }

        /**
         * CASes next field.
         */
        final boolean compareAndSetNext(Node expect, Node update) {
            return ReflectUtils.compareAndSwapObject(this, NEXT, expect, update);
        }
    }
}
