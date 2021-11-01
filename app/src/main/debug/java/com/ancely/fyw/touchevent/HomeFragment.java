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

import com.ancely.fyw.R;
import com.ancely.fyw.common.base.BaseFragment;

public class HomeFragment extends BaseFragment {
    private RecyclerView mRecyclerView;

    @Override
    protected void loadData() {

    }

    @Override
    protected void init() {
        mRecyclerView = mContentView.findViewById(R.id.test_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new TestAdapter(getContext()));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }
}
