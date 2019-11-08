package com.ancely.fyw.interceptertest;

/*
 *  @项目名：  ShareAndroid
 *  @包名：    com.ancely.share.intecepter
 *  @文件名:   Task3
 *  @创建者:   fanlelong
 *  @创建时间:  2019/10/16 9:50 AM
 *  @描述：    TODO
 */
public class Task3  implements IBaseTask {
    @Override
    public void doAction(String action, IBaseTask baseTask) {
        if (action.equals("ok2")) {
            System.out.println("执行任务成功了");
        } else {
            baseTask.doAction(action, baseTask);
        }
    }
}
