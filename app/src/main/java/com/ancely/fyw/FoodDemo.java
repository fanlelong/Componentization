package com.ancely.fyw;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw
 *  @文件名:   LockDemo
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/31 9:00 PM
 *  @描述：    TODO
 */
public class LockDemo {
    private int mCount = 0;
    private Lock mLock = new ReentrantLock();

    public void add() {
        mLock.lock();
        mCount++;
        mLock.unlock();
    }

    private static class MyThread extends Thread {
        LockDemo mLockDemo;

        public MyThread(LockDemo lockDemo) {
            mLockDemo = lockDemo;
        }

        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 10000; i++) {
                mLockDemo.add();
            }
        }
    }

    public static void main(String[] args) {

        LockDemo lockDemo = new LockDemo();
        new MyThread(lockDemo).start();
        new MyThread(lockDemo).start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("count: " + lockDemo.mCount);
    }

}
