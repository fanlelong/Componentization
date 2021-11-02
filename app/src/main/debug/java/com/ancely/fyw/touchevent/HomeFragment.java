package com.ancely.fyw.touchevent;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.touchevent
 *  @文件名:   HomeFragment
 *  @创建者:   admin
 *  @创建时间:  2021/11/1 9:20
 *  @描述：    TODO
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ancely.fyw.R;
import com.ancely.fyw.common.base.BaseFragment;
import com.ancely.fyw.view.FloatLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private FloatLayout mFloatLayout;

    @Override
    protected void loadData() {

    }

    @Override
    protected void init() {
        mRecyclerView = mContentView.findViewById(R.id.test_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new TestAdapter(getContext()));

        mFloatLayout = mContentView.findViewById(R.id.float_layout);
        List<String> list = new ArrayList<>();
        list.add("杯子");
        list.add("一个台灯");
        list.add("防毒口罩");
        list.add("蓝牙音响");
        list.add("移动固态硬盘");
        list.add("鼠标");
        list.add("100抽纸巾");
        list.add("小音响");
        list.add("音响");
        list.add("电风扇");
        for (String s : list) {
            TextView textView = new TextView(getContext());
            textView.setTextColor(0xff000000);
            textView.setTextSize(24);
            textView.setPadding(10, 10, 10, 10);
            textView.setBackgroundColor(0xffffffff);
            textView.setText(s);
            mFloatLayout.addView(textView);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }
}
