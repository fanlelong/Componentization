package com.ancely.fyw.photo;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.photo
 *  @文件名:   PhotoImageView
 *  @创建者:   fanlelong
 *  @创建时间:  2019/12/8 12:52 AM
 *  @描述：    TODO
 */
public class PhotoImageView extends AppCompatImageView {
    private Context mContext;
    private int screenWidth;

    public PhotoImageView(Context context) {
        super(context);
        mContext = context;
        screenWidth = getScreenWidth();
    }

    public PhotoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        screenWidth = getScreenWidth();
    }

    public PhotoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        screenWidth = getScreenWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = screenWidth / 4-dp2px(1);

        super.onMeasure( MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),  MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY));

    }

    /**
     * dp转px
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, mContext.getResources().getDisplayMetrics());
    }

    public int getScreenWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }
}
