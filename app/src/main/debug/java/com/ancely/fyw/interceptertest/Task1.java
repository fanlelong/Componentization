package com.ancely.fyw.interceptertest;

/*
 *  @项目名：  ShareAndroid
 *  @包名：    com.ancely.share.intecepter
 *  @文件名:   Task1
 *  @创建者:   fanlelong
 *  @创建时间:  2019/10/16 9:50 AM
 *  @描述：    TODO
 */
public class Task1 implements IBaseTask {
    @Override
    public String doAction(String action, IBaseTask baseTask) {
        System.out.println("Task1执行任务完成");
        if (action.equals("no")) {
            System.out.println("执行任务成功了");
            return "Task1:执行任务成功了";
        } else {
           return baseTask.doAction(action, baseTask);
        }
    }
}
