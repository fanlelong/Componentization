package com.ancely.fyw.mvptext;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
        Log.e("TextFragment","onCreateView");
        return inflater.inflate(R.layout.fragment_text,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TextFragment","onViewCreated");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("TextFragment","onActivityCreated");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TextFragment","onCreate");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TextFragment","onResume");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("TextFragment","onAttach");

    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.e("TextFragment","onAttachFragment");

    }
}
