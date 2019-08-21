package com.ancely.fyw.aroute.proxy;

import android.os.Bundle;
import android.support.annotation.Nullable;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.proxy
 *  @文件名:   ActivityInterface
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/21 1:56 PM
 *  @描述：    TODO
 */
public interface ActivityInterface {

    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
