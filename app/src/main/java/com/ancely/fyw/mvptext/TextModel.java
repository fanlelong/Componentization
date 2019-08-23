package com.ancely.fyw.mvptext;


import android.arch.lifecycle.MediatorLiveData;

import com.ancely.fyw.aroute.base.BaseModel;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext
 *  @文件名:   TextModel
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/23 10:58 AM
 *  @描述：    TODO
 */
public class TextModel extends BaseModel<TextPresenter, TestContract.Model> {

    private MediatorLiveData<TestInfo> resultLiveData = new MediatorLiveData<>();
    private MediatorLiveData<TestOneInfo> resultLiveData1 = new MediatorLiveData<>();
    @Override
    public TestContract.Model getContract() {
        return new TestContract.Model() {
            @Override
            public void execudeTest(String accound, String psw) throws Exception {

            }

            @Override
            public void execudeTest1(String accound, String psw) throws Exception {

                mPresenter.getContract().requestSuccess(new TestInfo());
            }
        };
    }

    public MediatorLiveData<TestInfo> getResultLiveData() {
        return resultLiveData;
    }

    public MediatorLiveData<TestOneInfo> getResultLiveData1() {
        return resultLiveData1;
    }
}
