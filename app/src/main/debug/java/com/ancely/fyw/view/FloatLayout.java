package com.ancely.fyw.view;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.view
 *  @文件名:   FloatLayout
 *  @创建者:   fanlelong
 *  @创建时间:  2021/11/2 7:59 PM
 *  @描述：    TODO
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FloatLayout extends ViewGroup {
    private List<List<View>> mAllLine = new ArrayList<>();
    private List<Integer> mAllLineHeight = new ArrayList<>();
    private List<Integer> mAllLineSurplusWidth = new ArrayList<>();
    private int mHonizontalWidthSpace = 50;
    private int mVerticalSpace = 40;

    public FloatLayout(Context context) {
        super(context);
    }

    public FloatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void clearViewsLineView() {
        mAllLine.clear();
        mAllLineHeight.clear();
        mAllLineSurplusWidth.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        clearViewsLineView();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int childCount = getChildCount();

        @SuppressLint("DrawAllocation")
        List<View> lineViews = new ArrayList<>(); // 每一行的View集合
        int lineWidthUsed = 0; // 每一行已经使用了的宽
        int lineHeight = 0;// 总行高

        int selfWidth = MeasureSpec.getSize(widthMeasureSpec); // 安全宽度
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec); // 安全高度

        int parentWidth = 0;
        int parentHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            LayoutParams layoutParams = childView.getLayoutParams();
            // 将layoutParams 转为measureSpec
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, layoutParams.width);
            int childHeightMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingTop + paddingBottom, layoutParams.height);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            // 获取子View度量的宽高
            int measuredWidth = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();
            if (measuredWidth + lineWidthUsed + mHonizontalWidthSpace > selfWidth) { // 需要换行
                mAllLineSurplusWidth.add(selfWidth - lineWidthUsed - mHonizontalWidthSpace);
                parentWidth = Math.max(lineWidthUsed, parentWidth);
                parentHeight = lineHeight + parentHeight + mVerticalSpace;
                mAllLineHeight.add(lineHeight);
                mAllLine.add(lineViews);
                lineViews = new ArrayList<>();
                lineWidthUsed = 0;
                lineHeight = 0;
            }


            lineViews.add(childView);
            lineWidthUsed = lineWidthUsed + measuredWidth + mHonizontalWidthSpace;
            lineHeight = Math.max(lineHeight, measuredHeight);
            if (i == childCount - 1) {
                mAllLine.add(lineViews);
                mAllLineHeight.add(lineHeight);
                mAllLineSurplusWidth.add(selfWidth - lineWidthUsed);
                parentHeight = parentHeight + lineHeight + mVerticalSpace;
            }

        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int realWidth = widthMode == MeasureSpec.EXACTLY ? selfWidth : parentWidth;
        int realHeight = heightMode == MeasureSpec.EXACTLY ? selfHeight : parentHeight;
        setMeasuredDimension(realWidth, realHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int curL = getPaddingLeft();
        int curT = getPaddingTop();
        for (int i = 0; i < mAllLine.size(); i++) { // 有多少行
            List<View> views = mAllLine.get(i); // 每行多少View
            int averageWidth = (mAllLineSurplusWidth.get(i) + mHonizontalWidthSpace * (views.size() - 1)) / (views.size() + 1);
            int widthTotal = 0;
            for (View view : views) {
                widthTotal = widthTotal + view.getMeasuredWidth();
            }
            averageWidth = (getMeasuredWidth()- widthTotal) / (views.size()+1);
            for (View view : views) {
                int left = curL + averageWidth;
                int top = curT;
                int right = left + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();
                view.layout(left, top, right, bottom);
                curL = right;
            }
            curL = getPaddingLeft();
            curT = curT + mAllLineHeight.get(i) + mVerticalSpace;
        }
    }
}
