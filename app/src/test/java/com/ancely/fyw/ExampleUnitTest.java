package com.ancely.fyw;

import com.ancely.fyw.interceptertest.ManagerTask;
import com.ancely.fyw.interceptertest.Task1;
import com.ancely.fyw.interceptertest.Task2;
import com.ancely.fyw.interceptertest.Task3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void math() {
        String s = int2chineseNum(900001001);
        System.out.println("金额是: "+s);
    }

    public String int2chineseNum(int src) {
        final String num[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final String unit[] = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String dst = "";
        int count = 0;
        while (src > 0) {
            dst = (num[src % 10] + unit[count]) + dst;
            src = src / 10;
            count++;
        }
        System.out.println("金额是: "+src);
        return dst.replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");
    }

    @Test
    public void taskTest(){
        Task1 task1 = new Task1();
        Task2 task2 = new Task2();
        Task3 task3 = new Task3();
        ManagerTask managerTask = new ManagerTask();
        managerTask.addTask(task1);
        managerTask.addTask(task2);
        managerTask.addTask(task3);

        String actionReturn = managerTask.doAction("ok2",managerTask);
        System.out.println(actionReturn);
    }
}