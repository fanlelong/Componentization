package com.ancely.fyw.mvptext;

import com.ancely.fyw.aroute.base.BaseModelActivity;


import con.ancely.fyw.annotation.apt.ARouter;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw
 *  @文件名:   TextModelActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/23 10:56 AM
 *  @描述：    TODO
 */
@ARouter(path = "/app/TextModelActivity")
public class TextModelActivity extends BaseModelActivity<TextPresenter> implements ITextView {


    @Override
    protected TextPresenter getPresetner() {
        return new TextPresenter(this, this);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initListener() {

    }


    @Override
    public TestContract.View getContract() {
        return new TestContract.View() {


            @Override
            public void handlerResult(TestInfo t) {

            }

            @Override
            public void handlerResult1(TestOneInfo oneInfo) {

            }
        };
    }
}
