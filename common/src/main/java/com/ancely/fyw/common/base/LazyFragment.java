package com.ancely.fyw.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.common.base
 *  @文件名:   LazyFragment
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/26 3:09 PM
 *  @描述：    TODO
 */
public abstract class LazyFragment extends BaseFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLazyLoad(true);
        isPrepare = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        onUserVIsibleHint();
    }

    private void onUserVIsibleHint() {
        // 如果已经初始化完成，并且显示给用户
        if (isPrepare && getUserVisibleHint()) {
            isPrepare = false;
            loadData();
        }
    }
}
