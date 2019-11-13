package com.ancely.fyw.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ancely.fyw.aroute.model.ModelP;
import com.ancely.fyw.aroute.model.bean.RequestErrBean;
import com.ancely.fyw.aroute.model.bean.ResponseBean;

import java.util.HashMap;
import java.util.Map;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.common
 *  @文件名:   BaseModelFragment
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/13 11:09 AM
 */
public abstract class BaseModelFragment<P extends ModelP<T>, T> extends Fragment implements View.OnClickListener, IBaseView<T> {
    protected P mModelP;
    protected View mContentView;
    protected Context mContext;
    protected final Map<String, Object> mParams = new HashMap<>();//请求参数
    protected boolean mIsFirstInto;//是否是第一次进入视图
    private boolean isLazyLoad;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(getContentView(), container, false);
        mModelP = getModelP();
        if (mModelP != null) {
            initObserveDatas();
        }
        return mContentView;

    }

    public abstract P getModelP();

    private void initObserveDatas() {
        mModelP.getBaseViewModel().getErrorLiveData().observe(this, this::accessError);
        mModelP.getBaseViewModel().getResultLiveData().observe(this, responseBean -> {
            mIsFirstInto = true;
            accessSuccess(responseBean);
        });
        mModelP.getBaseViewModel().getMoreLiveData().observe(this, this::accessMoreSuccess);
        mModelP.getBaseViewModel().getShowLoadingLiveData().observe(this, this::showloading);
        mModelP.getBaseViewModel().getShowLoadingLiveData().observe(this, this::hideLoading);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initEvent();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (!isLazyLoad) {
            loadData();
        }
        super.onActivityCreated(savedInstanceState);
    }

    protected abstract void loadData();

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract int getContentView();


    @Override
    public void onClick(View v) {

    }

    @Override
    public void showloading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }


    /**
     * 请求错误
     *
     * @param errBean 错误信息bean类
     */
    @Override
    public void accessError(RequestErrBean errBean) {
        Toast.makeText(mContext, errBean.msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 请求数据成功
     *
     * @param responseBean 请求成功信息bean类
     */
    @Override
    public void accessSuccess(ResponseBean<T> responseBean) {
    }

    /**
     * 请求加载更多数据居功
     *
     * @param responseBean 请求加载更多成功信息bean类
     */
    @Override
    public void accessMoreSuccess(ResponseBean<T> responseBean) {
    }

    @Override
    public boolean isNeedCheckNetWork() {
        return false;
    }
}
