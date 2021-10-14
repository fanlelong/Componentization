package com.ancely.fyw.mvptext;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;

import com.ancely.fyw.R;
import com.ancely.fyw.annotation.apt.Subscribe;
import com.ancely.fyw.aroute.base.BaseActivity;
import com.ancely.fyw.aroute.skin.utils.PreferencesUtils;

import java.io.File;

public class SkinTestActivity extends BaseActivity {

    private String skinPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_test);

        AppCompatDelegate.setDefaultNightMode(Configuration.UI_MODE_NIGHT_NO);
        boolean isNight = PreferencesUtils.putBoolean(this, "isNight", false);
        setDayNightModel(AppCompatDelegate.MODE_NIGHT_NO);


        skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "ancely.skin";

        if (("ancely_fyw").equals(PreferencesUtils.getString(this, "currentSkin"))) {
            skinDynamic(skinPath, R.color.skin_item_color);
        } else {
            defaultSkin(R.color.colorPrimary);
        }
    }

    public void skinDynamic(View view) {
        // 真实项目中：需要先判断当前皮肤，避免重复操作！
        if (!("ancely_fyw").equals(PreferencesUtils.getString(this, "currentSkin"))) {
            Log.e("netease >>> ", "-------------start-------------");
            long start = System.currentTimeMillis();

            skinDynamic(skinPath, R.color.skin_item_color);
            PreferencesUtils.putString(this, "currentSkin", "ancely_fyw");

            long end = System.currentTimeMillis() - start;
            Log.e("netease >>> ", "换肤耗时（毫秒）：" + end);
            Log.e("netease >>> ", "-------------end---------------");
        }

    }

    public void skinDefault(View view) {
        if (!("default").equals(PreferencesUtils.getString(this, "currentSkin"))) {
            Log.e("netease >>> ", "-------------start-------------");
            long start = System.currentTimeMillis();

            defaultSkin(R.color.colorPrimary);
            PreferencesUtils.putString(this, "currentSkin", "default");

            long end = System.currentTimeMillis() - start;
            Log.e("netease >>> ", "还原耗时（毫秒）：" + end);
            Log.e("netease >>> ", "-------------end---------------");
        }
    }

    @Override
    public boolean openChangerSkin() {
        return true;
    }

    @Subscribe
    public void textEvent(PathBean pathBean){

    }
}
