package com.ancely.pay;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ancely.fyw.annotation.apt.ARouter;
import com.ancely.fyw.annotation.apt.Subscribe;
import com.ancely.fyw.aroute.model.bean.ResponseBean;
import com.ancely.fyw.aroute.utils.LogUtils;
import com.ancely.fyw.common.base.BaseModelActivity;
import com.ancely.pay.constans.Constances;
import com.ancely.pay.event.PayBean;
import com.ancely.pay.request.PayModel;
import com.ancely.pay.service.FloatingService;
import com.ancely.pay.service.NotificationCollectorService;
import com.ancely.pay.utils.SharePreferenceHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ARouter(path="/ancelypay/PlayActivity")
public class PlayActivity extends BaseModelActivity<PayModel, String> {


    private EditText mPayApiEt;
    private Button mPayPostSetting;
    private RecyclerView mPayRv;
    private Map<String, Object> mParams;
    private EditText mPayAppidEt;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        if (PayApplication.mApplication==null) {
            new PayApplication().initValized(newBase.getApplicationContext());

        }
    }

    @Override
    public PayModel getModelP() {
        return new PayModel(this);
    }

    @Override
    protected void initDatas() {
        mParams = new HashMap<>();
    }

    @Override
    protected void initEvent() {
        mPayPostSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceHelper.putString(PlayActivity.this, "POST_API", mPayApiEt.getText().toString());
                Constances.API = mPayApiEt.getText().toString();
                modelP.startRequestService(mParams);
            }
        });
        mPayAppidEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Constances.APPID = s.toString();
                SharePreferenceHelper.putString(PlayActivity.this, "APPID", Constances.APPID);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initView() {

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
//            startFloatingButtonService();
        }
        mPayApiEt = findViewById(R.id.pay_api_et);
        mPayPostSetting = findViewById(R.id.pay_post_setting);
        mPayRv = findViewById(R.id.pay_rv);
        mPayAppidEt = findViewById(R.id.pay_appid_et);
        mPayApiEt.setText(Constances.API);

        boolean enable = isNotificationListenerEnabled();

        String host = SharePreferenceHelper.getString(PlayActivity.this, "POST_API");
        if (!host.isEmpty()) {
            mPayApiEt.setText(host);
        }

        String appid = SharePreferenceHelper.getString(PlayActivity.this, "APPID");
        if (!appid.isEmpty()) {
            mPayAppidEt.setText(appid);
        }

        if (!enable) {
            openNotificationListenSettings();
        }
        toggleNotificationListenerService();
    }

    private void openNotificationListenSettings() {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        } else {
            intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        }
        startActivity(intent);

    }

    private boolean isNotificationListenerEnabled() {
        Set enabledListenerPackages = NotificationManagerCompat.getEnabledListenerPackages(mContext);
        return enabledListenerPackages.contains(mContext.getPackageName());
    }

    private void toggleNotificationListenerService() {
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(mContext, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(mContext, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected int getContentView() {
        return R.layout.act_pay_main;
    }

    @SuppressLint("ShowToast")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startFloatingButtonService() {

        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 0);
        } else {
            startService(new Intent(this, FloatingService.class));
        }
    }

    @Override
    public void accessSuccess(ResponseBean responseBean) {
        LogUtils.e("ancely_pay", responseBean.body.toString());
        Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean isNeedCheckNetWork() {
        return false;
    }

    @Subscribe
    public void notifacations(PayBean payBean) {
        LogUtils.e("ancely_pay>>>", payBean.payContent);
        mParams.put("title", payBean.payContent);
        mParams.put("appid", Constances.APPID);
        mParams.put("app", payBean.title);
        getModelP().startRequestService(mParams);

    }

    private long mBackPressedTime = 0L;

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public boolean openEventBus() {
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        long curTime = SystemClock.uptimeMillis();
//        if (curTime - mBackPressedTime < 2 * 1000) {
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
//        } else {
//            mBackPressedTime = curTime;
//            Toast.makeText(mContext, "双击回桌面", Toast.LENGTH_SHORT).show();
//        }
//        getModelP().startRequestService(mParams);
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
