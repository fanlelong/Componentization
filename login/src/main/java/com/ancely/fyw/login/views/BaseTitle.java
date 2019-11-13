package com.ancely.fyw.login.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ancely.fyw.aroute.utils.SizeUtils;
import com.ancely.fyw.login.R;


public class BaseTitle extends LinearLayout {
    private RelativeLayout mActMapBack;
    private TextView mBaseTitle;
    private TextView mBaseRight;
    private FrameLayout mBase;
    private View view;


    public BaseTitle(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseTitle(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        view = new View(context);
        addView(view);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.px2dp(context,25));
        view.setLayoutParams(layoutParams);
        LayoutInflater.from(context).inflate(R.layout.base_title, this, true);
        mActMapBack = findViewById(R.id.act_map_back);
        FrameLayout mBackLeft = findViewById(R.id.back_left);
        mBaseTitle = findViewById(R.id.base_title);
        mBaseRight = findViewById(R.id.base_right);
        mBase = findViewById(R.id.base);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseTitle);
        String mTitle = a.getString(R.styleable.BaseTitle_title);
        boolean mVisbility = a.getBoolean(R.styleable.BaseTitle_right_title_gone, false);
        String mRightTitle = a.getString(R.styleable.BaseTitle_right_title);
        mBaseTitle.setText(mTitle);
        mBaseRight.setText(mRightTitle);
        mBaseRight.setEnabled(false);
        if (mVisbility) {
            mBaseRight.setVisibility(VISIBLE);
        } else {
            mBaseRight.setVisibility(GONE);
        }
        a.recycle();

        mBaseRight.setOnClickListener(v -> {
            if (listener != null) {
                listener.save();
            }
        });

        mActMapBack.setOnClickListener(v -> {
            if (leftListener != null) {
                leftListener.leftBack();

            } else if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        });

        if (Build.VERSION.SDK_INT >= 19) {
            view.setVisibility(GONE);
        }

    }


    public BaseTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(String title) {
        mBaseTitle.setText(title);
    }

    public void setTitle(int resId) {
        mBaseTitle.setText(resId);
    }

    public void setOnLeftListerner(OnBackListener l) {
        backListener = l;
    }

    public void setOnBackListerner(OnRightListener l) {
        listener = l;
    }

    private OnRightListener listener;
    private OnBackListener backListener;

    public interface OnRightListener {
        void save();
    }

    public interface OnBackListener {
        void onBack();
    }

    //设置保存可见
    public void setEnable() {
        mBaseRight.setEnabled(true);
    }


    public void setViewVisbility(boolean visbility) {
        if (visbility) {
            mBase.setVisibility(GONE);
        } else {
            mBase.setVisibility(VISIBLE);
        }
    }

    public void setBaseVisbility() {
        setVisibility(GONE);
    }

    public void setViewVisbility() {
        view.setVisibility(GONE);
    }


    private OnXClickListener xListener;

    public void setOnXClickListener(OnXClickListener l) {
        xListener = l;
    }

    public interface OnXClickListener {
        void xFinish();
    }

    private OnLeftBackListener leftListener;

    public interface OnLeftBackListener {
        void leftBack();
    }

    public void setOnLeftBackListener(OnLeftBackListener l) {
        this.leftListener = l;
    }

    public void removeAllListener() {
        if (leftListener != null) {
            leftListener = null;
        }
        if (xListener != null) {
            xListener = null;
        }
    }

    public void setBackVisbility(boolean visbility) {
        mActMapBack.setVisibility(visbility ? VISIBLE : GONE);
    }
}