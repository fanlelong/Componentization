package com.ancely.fyw.common.base;

import com.ancely.fyw.aroute.model.bean.RequestErrBean;
import com.ancely.fyw.aroute.model.bean.ResponseBean;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.common
 *  @文件名:   IBaseView
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/13 11:06 AM
 */
public interface IBaseView<T> {
    void showloading(int flag);

    void hideLoading(int flag);

    void accessError(RequestErrBean errBean);

    void accessSuccess(ResponseBean<T> responseBean);

    void accessMoreSuccess(ResponseBean<T> responseBean);

    boolean isNeedCheckNetWork();//是否需要检查网络状态
}
