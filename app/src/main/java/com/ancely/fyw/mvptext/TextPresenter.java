package com.ancely.fyw.mvptext;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.ancely.fyw.aroute.base.BasePresenter;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext
 *  @文件名:   TextPresenter
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/23 10:58 AM
 *  @描述：    TODO
 */
public class TextPresenter extends BasePresenter<TextModel, ITextView, TestContract.Persenter> {

    public TextPresenter(@NonNull Fragment fragment, ITextView baseView) {
        super(fragment, baseView);
    }

    public TextPresenter(@NonNull FragmentActivity fragment, ITextView baseView) {
        super(fragment, baseView);
    }

    @Override
    protected void initObserable(TextModel baseModel) {

        baseModel.getResultLiveData().observe(getView(), new Observer<TestInfo>() {
            @Override
            public void onChanged(@Nullable TestInfo testInfo) {
                mBaseView.getContract().handlerResult(testInfo);
            }
        });

        baseModel.getResultLiveData1().observe(getView(), new Observer<TestOneInfo>() {
            @Override
            public void onChanged(@Nullable TestOneInfo testOneInfo) {
                mBaseView.getContract().handlerResult1(testOneInfo);
            }
        });
    }

    @Override
    protected Class<TextModel> getModelClass() {
        return TextModel.class;
    }

    @Override
    protected TestContract.Persenter getContract() {
        return new TestContract.Persenter() {
            @Override
            public void requestTest(String accound, String psw) {

            }

            @Override
            public void requestTest1(String accound, String psw) {
                try {
                    getBaseModel().getContract().execudeTest(accound, psw);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestSuccess(TestInfo testInfo) {
                mBaseView.getContract().handlerResult(testInfo);
            }
        };
    }
}
