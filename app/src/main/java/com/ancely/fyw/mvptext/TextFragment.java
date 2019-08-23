package com.ancely.fyw.mvptext;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ancely.fyw.R;

import con.ancely.fyw.annotation.apt.ARouter;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext
 *  @文件名:   TextFragment
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/23 5:46 PM
 *  @描述：    TODO
 */
@ARouter(path = "/app/TextFragment")
public class TextFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main,container,false);
    }
}
