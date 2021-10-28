package com.ancely.fyw.lock;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.lock
 *  @文件名:   ReenTrantLockTest
 *  @创建者:   admin
 *  @创建时间:  2021/10/28 17:06
 *  @描述：    TODO
 */

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ReenTrantLockTest implements Lock {
    final Sync sync;

    public ReenTrantLockTest(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

    @Override
    public void lock() {
        sync.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
