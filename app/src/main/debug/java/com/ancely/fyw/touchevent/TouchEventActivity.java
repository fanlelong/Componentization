package com.ancely.fyw.touchevent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ancely.fyw.R;
import com.ancely.fyw.aroute.manager.RouterManager;
import com.ancely.fyw.aroute.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class TouchEventActivity extends AppCompatActivity {
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);
        UIUtils.initApplication(getApplication());
        mViewPager = findViewById(R.id.viewpager);
        List<Fragment> dates = new ArrayList<>();
        Fragment textFragment = (Fragment) RouterManager.getInstance().build("/app/TextFragment")
                .withResultString("name", "app_usercenter")
                .navigation(this, 10);
        dates.add(new HomeFragment());
        dates.add(textFragment);
        mViewPager.setAdapter(new TestPageAdapter(getSupportFragmentManager(), dates));
    }
}