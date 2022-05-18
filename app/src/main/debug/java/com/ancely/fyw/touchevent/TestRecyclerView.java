package com.ancely.fyw.touchevent;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.touchevent
 *  @文件名:   TestRecyclerView
 *  @创建者:   admin
 *  @创建时间:  2021/11/1 9:21
 *  @描述：    TODO
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TestRecyclerView extends RecyclerView {

    private int mLastY, mLastX;

    public TestRecyclerView(@NonNull Context context) {
        super(context);
    }

    public TestRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                if (Math.abs(moveX - mLastX) > Math.abs(moveY - mLastY)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}
