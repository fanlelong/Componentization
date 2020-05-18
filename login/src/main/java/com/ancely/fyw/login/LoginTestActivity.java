package com.ancely.fyw.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ancely.fyw.aroute.base.BaseActivity;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.login
 *  @文件名:   LoginTestActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2020/1/9 12:38 AM
 *  @描述：    TODO
 */
public class LoginTestActivity extends BaseActivity {
    private FrameLayout mFrameLayout1;
    private FrameLayout mFrameLayout2;
    private TextView mTextview;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_test);

        mFrameLayout1 = findViewById(R.id.FrameLayout1);
        mFrameLayout2 = findViewById(R.id.FrameLayout2);
        mTextview = findViewById(R.id.textview);

        mFrameLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ancely>>>","mFrameLayout1.onClick()");
            }
        });
        mFrameLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ancely>>>","mFrameLayout2.onClick()");
            }
        });
        mTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ancely>>>","mTextview.onClick()");
            }
        });

    }
}
