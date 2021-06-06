package com.ancely.fyw;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FoodDemo {

    private String mFoodName;//物品名
    private int mFoodId;//物品ID
    private boolean isProducted;

    public synchronized void putFood(String foodName) {
        if (!isProducted) {
            //开始生产
            mFoodId += 1;
            System.out.println(Thread.currentThread().getName() + " 生产者: 生产了: " + mFoodId);


            //已经生产完了
            isProducted = true;

            notifyAll();//唤醒被等待的 如果不用synchronized修饰就会报 Exception in thread "Thread-1" java.lang.IllegalMonitorStateException


            try {
                wait();//它需要获取锁,然后再把这把锁给释放掉,当前线程进入等待状态,这个时候CPU就会去执行其它线程.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void outFood() {

        if (isProducted) {
            //可以开始消费了
            System.out.println(Thread.currentThread().getName() + " >>>>>>>>>>>>>消费者: 消费了: " + mFoodId);
            isProducted = false;

            //消费完成
            notifyAll();

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ProductThread extends Thread {
        private FoodDemo mFoodDemo;

        public ProductThread(FoodDemo foodDemo) {
            mFoodDemo = foodDemo;
        }

        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 20; i++) {
                mFoodDemo.putFood("面包 ");
            }
        }
    }

    public static class ConsumeThread extends Thread {
        private FoodDemo mFoodDemo;

        public ConsumeThread(FoodDemo foodDemo) {
            mFoodDemo = foodDemo;
        }

        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 20; i++) {
                mFoodDemo.outFood();
            }
        }
    }

    public static void main(String[] args) {

//        FoodDemo foodDemo = new FoodDemo();
//        new ProductThread(foodDemo).start();
//        new ConsumeThread(foodDemo).start();
        unDiedLocd();
    }

    public static void diedLocd() {
        Object one = new Object();
        Object two = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (one) {
                    System.out.println("get one");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (two) {
                        System.out.println("get two");
                    }
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (two) {
                    System.out.println("get two");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (one) {
                        System.out.println("get onw");
                    }
                }
            }
        }).start();
    }

    public static void unDiedLocd() {
        Lock one = new ReentrantLock();
        Lock two = new ReentrantLock();
        Random random = new Random();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (one.tryLock()) {
                        System.out.println("get one");

                        try {
                            if (two.tryLock()) {
                                try {
                                    System.out.println("get two");
                                    break;
                                } finally {
                                    two.unlock();
                                }
                            }
                        } finally {
                            one.unlock();
                        }
                    }
                    try {
                        Thread.sleep(random.nextInt(3));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (two.tryLock()) {
                        System.out.println("get one");

                        try {
                            if (one.tryLock()) {
                                try {
                                    System.out.println("get two");
                                    break;
                                } finally {
                                    one.unlock();
                                }
                            }
                        } finally {
                            two.unlock();
                        }
                    }
                    try {
                        Thread.sleep(random.nextInt(3));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }
}
