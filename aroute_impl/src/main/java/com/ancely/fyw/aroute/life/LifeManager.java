package com.ancely.fyw.aroute.life;


import com.ancely.fyw.aroute.manager.NetChangerManager;
import com.ancely.fyw.aroute.model.ModelP;

import java.util.ArrayList;
import java.util.List;

public class LifeManager implements LifecycleListener {

    private ActivityFragmentLifecycle mLifecycle;
    private final List<ModelP> mPresenters = new ArrayList<>();

    LifeManager(ActivityFragmentLifecycle lifecycle, ModelP presenter) {
        mLifecycle = lifecycle;
        mPresenters.add(presenter);
        mLifecycle.addListener(this);
        if (presenter.isOpenNetChanger())
            NetChangerManager.getDefault().registerObserver(presenter);

    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
        mLifecycle.removeListener(this);
        for (ModelP presenter : mPresenters) {
            if (presenter.isOpenNetChanger()) {
                NetChangerManager.getDefault().unRegisterObserver(presenter);
            }
            presenter.unDisposable();
        }
        mPresenters.clear();
    }

    void addPresenter(ModelP presenter) {
        mPresenters.add(presenter);
    }
}
