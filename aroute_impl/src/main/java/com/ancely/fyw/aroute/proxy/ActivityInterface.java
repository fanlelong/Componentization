package com.ancely.fyw.aroute.proxy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public interface ActivityInterface {

    void attach(Activity activity);

    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    View onCreateView(View parent, String name, Context context, AttributeSet attrs);

}
