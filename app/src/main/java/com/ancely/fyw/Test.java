package com.ancely.fyw;

//import org.openjdk.jol.info.ClassLayout;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw
 *  @文件名:   Test
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/26 1:33 PM
 *  @描述：    TODO
 */
public class Test {


    public static void main(String[] args) {
        int[] arr = new int[100];
        for (int i = 0; i < 100; i++) {
            arr[i] = i;
        }
        test(arr);

//        printlnObject();
    }

    private static void test(int[] array) {
        int q = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = i; j < array.length; j++) {
                StringBuilder buffer = new StringBuilder();
                for (int k = 0; k <= j - i; k++) {
                    q++;
                    buffer.append(array[k + i]).append(",");
                }
                System.out.println(buffer.toString());
            }
        }
        System.out.println(q);


    }

//    public static void printlnObject() {
//        Object o = new Object();
//        System.out.println(ClassLayout.parseInstance(o).toPrintable());
//        /**
//         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
//         *       0     4        (object header) (对象头) Mark Word         01 00 00 00 (00000001 00000000 00000000 00000000) (1)   从第0个开始往后数4个字节
//         *       4     4        (object header) Klass Word                00 00 00 00 (00000000 00000000 00000000 00000000) (0)
//         *       8     4        (object header)                           d5 01 00 f8 (11010101 00000001 00000000 11111000) (-134217259)  d5 01 00 f8指向哪个class上面指的是Object.class
//         *      12     4        (loss due to the next object alignment)  上面是4+4+4=12 不能被8除, 补4个字节  字节对齐
//         *      上面就是new出来的一个Object的布局
//         */
//
//
//        synchronized (o) {//给这个对象上锁 当我执有这个锁的时候才能执行下面操作所以是给对象上锁  synchronized主要是修改markword的信息
//            System.out.println(ClassLayout.parseInstance(o).toPrintable());
//            /**
//             *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
//             *       0     4        (object header)                           b0 91 45 0e (10110000 10010001 01000101 00001110) (239440304)  mark word 记录了锁信息 gc信息 hashcode信息
//             *       4     4        (object header)                           00 70 00 00 (00000000 01110000 00000000 00000000) (28672)
//             *       8     4        (object header)                           d5 01 00 f8 (11010101 00000001 00000000 11111000) (-134217259)
//             *      12     4        (loss due to the next object alignment)
//             */
//        }
//
//        //synchronized升级过程
//        //        普通对象-->偏向锁->轻度竞争变成->轻量级锁->转为重量级锁
//        //                        ->重度竞争变成->重量级锁
//    }
}
