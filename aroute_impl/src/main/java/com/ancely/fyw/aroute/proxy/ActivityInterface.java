package com.ancely.fyw.aroute.proxy;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public interface ActivityInterface {

    void attach(Activity activity);

    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
