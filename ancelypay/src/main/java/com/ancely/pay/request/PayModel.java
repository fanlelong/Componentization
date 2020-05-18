package com.ancely.pay.request;

import android.support.v4.app.FragmentActivity;

import com.ancely.fyw.aroute.manager.NetWorkManager;
import com.ancely.fyw.aroute.model.BaseViewModel;
import com.ancely.fyw.aroute.model.ModelP;
import com.ancely.fyw.aroute.networks.StringViewModel;
import com.ancely.pay.PayApi;
import com.ancely.pay.PayApplication;
import com.ancely.pay.utils.SharePreferenceHelper;

import java.util.Map;

import io.reactivex.Observable;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.pay.request
 *  @文件名:   PayModel
 *  @创建者:   fanlelong
 *  @创建时间:  2020/2/15 3:41 PM
 *  @描述：    TODO
 */
public class PayModel extends ModelP<String> {
    public PayModel(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public Class<? extends BaseViewModel<String>> getVMClass() {
        return StringViewModel.class;
    }

    @Override
    protected Observable<String> getObservable(Map<String, Object> map, int flag) {
//        "api/personal/notifyPolymerization"
        String api = SharePreferenceHelper.getString(PayApplication.mApplication
                , "POST_API");

        api = api + "api/personal/notify";
        return NetWorkManager.getInstance().getRetrofit().create(PayApi.class).postPay(api, map);
    }

    @Override
    public void showProgress(int flag) {

    }

    @Override
    public void hideProgress(int flag) {

    }
}
