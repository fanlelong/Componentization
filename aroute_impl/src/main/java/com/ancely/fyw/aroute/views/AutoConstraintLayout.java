package com.ancely.fyw.aroute.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.ancely.fyw.aroute.utils.AutoLayoutHelp;

public class AutoConstraintLayout extends ConstraintLayout {

    private final AutoLayoutHelp mAutoLayoutHelp = new AutoLayoutHelp(this);

    public AutoConstraintLayout(@NonNull Context context) {
        this(context, null);
    }

    public AutoConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) mAutoLayoutHelp.adjustChildren();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public ConstraintLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ConstraintLayout.LayoutParams implements AutoLayoutHelp.AutoLayoutParams {
        private final AutoLayoutHelp.LayoutInfo mLayoutInfo;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            mLayoutInfo = AutoLayoutHelp.getLayoutInfo(context, attrs);
        }

        @Override
        public AutoLayoutHelp.LayoutInfo getAutoLayoutInfo() {
            return mLayoutInfo;
        }
    }


}