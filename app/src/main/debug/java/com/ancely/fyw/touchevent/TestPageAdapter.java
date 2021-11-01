package com.ancely.fyw.touchevent;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.touchevent
 *  @文件名:   TestPageAdapter
 *  @创建者:   admin
 *  @创建时间:  2021/11/1 9:16
 *  @描述：    TODO
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TestPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> dates;

    public TestPageAdapter(FragmentManager fm, List<Fragment> dates) {
        super(fm);
        this.dates = dates;
    }

    @Override
    public Fragment getItem(int i) {
        return dates.get(i);
    }

    @Override
    public int getCount() {
        return dates.size();
    }
}
