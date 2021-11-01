package com.ancely.fyw.mvptext;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext
 *  @文件名:   JavaStack
 *  @创建者:   fanlelong
 *  @创建时间:  2019/12/11 2:43 PM
 *  @描述：    TODO
 */
public class JavaStack {

    private static String sLs = "一个静态的成员变量";
    public static final String SINT = "我是一个常量";

    private void test() {
        Object obj = new Object();

        int oneKing = 100;
        int twoKing = 200;
        if ((oneKing + twoKing) > 40) {
            obj.hashCode();
            oneKing -= 20;
            twoKing -= 20;
        }
    }

    public static void main(String[] args) {
        JavaStack stack = new JavaStack();
        stack.test();
    }

}
