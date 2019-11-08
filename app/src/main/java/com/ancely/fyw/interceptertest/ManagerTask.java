package com.ancely.fyw.interceptertest;

import java.util.ArrayList;
import java.util.List;

/*
 *  @项目名：  ShareAndroid
 *  @包名：    com.ancely.share.intecepter
 *  @文件名:   ManagerTask
 *  @创建者:   fanlelong
 *  @创建时间:  2019/10/16 9:53 AM
 *  @描述：    TODO
 */
public class ManagerTask implements IBaseTask {
    public int index;
    private List<IBaseTask> mTaskList = new ArrayList<>();

    public void addTask(IBaseTask baseTask) {
        mTaskList.add(baseTask);
    }

    @Override
    public void doAction(String action, IBaseTask baseTask) {
        if (mTaskList != null && mTaskList.size() > 0 && index <= mTaskList.size()) {
            IBaseTask iBaseTask = mTaskList.get(index);
            index++;
            iBaseTask.doAction(action, baseTask);
        }
    }
}
