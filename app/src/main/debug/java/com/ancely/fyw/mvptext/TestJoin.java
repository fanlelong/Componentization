package com.ancely.fyw.mvptext;

import android.util.Log;

import java.util.concurrent.CountDownLatch;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext
 *  @文件名:   TestJoin
 *  @创建者:   fanlelong
 *  @创建时间:  2019/12/11 10:36 AM
 *  @描述：    TODO
 */
public class TestJoin {
    public static void join() {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
//                Log.e("ancely1>>> ", "thread1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread1");

            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

//                Log.e("ancely1>>> ", "thread2");
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread2");
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Log.e("ancely1>>> ", "thread3");
                System.out.println("thread3");
            }
        });

//

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread3.start();

    }

    public static void join1() {
        CountDownLatch count = new CountDownLatch(3);
        CountDownLatch count1 = new CountDownLatch(2);
        CountDownLatch count2 = new CountDownLatch(1);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.e("ancely1>>> ", "thread1");
                    count.countDown();
                    count1.countDown();
                    count2.countDown();
                    Log.e("ancely1>>> ", "thread1--end");
                } catch (Exception ignored) {

                }

            }
        },"A");

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    count2.await();
                    Log.e("ancely1>>> ", "thread2--start");
                    count.countDown();
                    Log.e("ancely1>>> ", "thread2--end");
                    count1.countDown();
                } catch (Exception e) {

                }finally {
                }
            }
        },"B");

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    count1.await();
                    Log.e("ancely1>>> ", "thread3--start");
                    count.countDown();
                    Log.e("ancely1>>> ", "thread3--end");
                } catch (Exception e) {

                }finally {
                }
            }
        },"C");
        thread1.start();
        thread3.start();
        thread2.start();
        try {
            count.await();
        }catch (Exception e){

        }
        Log.e("ancely1>>> ", "执行完....");
    }
    public static void main(String art []){
        join();
    }
}
