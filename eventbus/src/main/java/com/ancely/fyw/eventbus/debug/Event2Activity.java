package com.ancely.fyw.eventbus.debug;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ancely.fyw.eventbus.EventBus;
import com.ancely.fyw.eventbus.R;
import com.ancely.fyw.eventbus.debug.event.UserInfo;

import com.ancely.fyw.annotation.apt.Subscribe;

public class Event2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event2);
    }

    public void post(View view) {

        EventBus.getDefault().post(new UserInfo("ancely2", 23));
        finish();
    }

    public void sticky(View view) {
        EventBus.getDefault().register(this);
        EventBus.getDefault().removeStickyEvent(UserInfo.class);
    }

    @Subscribe(sticky = true)
    public void sticky(UserInfo userInfo) {
        Log.e("ancely_sticky", userInfo.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInfo userInfo = EventBus.getDefault().getStickyEvent(UserInfo.class);
        if (userInfo != null) {
            UserInfo info = EventBus.getDefault().removeStickyEvent(UserInfo.class);
            if (info != null) {
                EventBus.getDefault().removeAllStickyEvents();
            }
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
