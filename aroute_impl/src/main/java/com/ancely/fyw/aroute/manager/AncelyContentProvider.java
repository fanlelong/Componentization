package com.ancely.fyw.aroute.manager;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ancely.fyw.aroute.base.BaseActivity;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext
 *  @文件名:   AncelyContentProvider
 *  @创建者:   fanlelong
 *  @创建时间:  2019/12/9 3:26 PM
 *  @描述：    TODO
 */
public class AncelyContentProvider extends ContentProvider {
    @SuppressLint("StaticFieldLeak")
    static Context context;

    @Override
    public boolean onCreate() {
        context = getContext();
        Log.e("ancely1","AncelyContentProvider time end  "+ (System.currentTimeMillis()- BaseActivity.time));
        SystemClock.sleep(3000);
        Log.e("ancely1","AncelyContentProvider time end  "+ (System.currentTimeMillis()- BaseActivity.time));
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
