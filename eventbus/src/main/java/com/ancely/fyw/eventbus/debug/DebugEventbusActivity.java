package com.ancely.fyw.eventbus.debug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ancely.fyw.eventbus.EventBus;
import com.ancely.fyw.eventbus.R;
import com.ancely.fyw.eventbus.debug.event.UserInfo;

import com.ancely.fyw.annotation.apt.Subscribe;
import com.ancely.fyw.annotation.apt.bean.ThreadMode;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.eventbus
 *  @文件名:   DebugEventbusActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/8 2:27 PM
 *  @描述：    TODO
 */
public class DebugEventbusActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus);
        mTextView = findViewById(R.id.tv);
        mTextView.setText("debug_eventbus");
//        EventBus.getDefault().addIndex(new EventBusIndex());
        EventBus.getDefault().register(this);

    }


    public void jump(View view) {
        startActivity(new Intent(this, Event2Activity.class));
    }

    @Subscribe
    public void onEvent(UserInfo userInfo) {
        Log.e("ancely>>>", userInfo.toString());
        mTextView.setText(userInfo.getName());
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 10)
    public void onEvent1(UserInfo userInfo) {
        Log.e("ancely2>>>", userInfo.toString());
        mTextView.setText(userInfo.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
//        EventBus.clearCaches();
    }

    public void postSticky(View view) {
        EventBus.getDefault().postSticky(new UserInfo("ancely", 22));
    }

    public void post(View view) {
        EventBus.getDefault().post(new UserInfo("ancely",33));
    }
}
