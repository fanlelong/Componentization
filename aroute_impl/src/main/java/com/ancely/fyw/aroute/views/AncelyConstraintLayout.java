package com.ancely.fyw.aroute.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ancely.fyw.aroute.R;
import com.ancely.fyw.aroute.utils.UIUtils;

public class AncelyConstraintLayout extends ConstraintLayout {

    public static final int DEFAULT_VALUE = 0;
    private static final int[] LL = new int[]
            {
                    android.R.attr.textSize,
                    android.R.attr.padding,
                    android.R.attr.paddingLeft,
                    android.R.attr.paddingTop,
                    android.R.attr.paddingRight,
                    android.R.attr.paddingBottom,
                    android.R.attr.layout_width,
                    android.R.attr.layout_height,
                    android.R.attr.layout_margin,
                    android.R.attr.layout_marginLeft,
                    android.R.attr.layout_marginTop,
                    android.R.attr.layout_marginRight,
                    android.R.attr.layout_marginBottom,
                    android.R.attr.maxWidth,
                    android.R.attr.maxHeight,
                    android.R.attr.minWidth,
                    android.R.attr.minHeight,
            };

    private static final int INDEX_TEXT_SIZE = 0;
    private static final int INDEX_PADDING = 1;
    private static final int INDEX_PADDING_LEFT = 2;
    private static final int INDEX_PADDING_TOP = 3;
    private static final int INDEX_PADDING_RIGHT = 4;
    private static final int INDEX_PADDING_BOTTOM = 5;
    private static final int INDEX_WIDTH = 6;
    private static final int INDEX_HEIGHT = 7;
    private static final int INDEX_MARGIN = 8;
    private static final int INDEX_MARGIN_LEFT = 9;
    private static final int INDEX_MARGIN_TOP = 10;
    private static final int INDEX_MARGIN_RIGHT = 11;
    private static final int INDEX_MARGIN_BOTTOM = 12;
    private static final int INDEX_MAX_WIDTH = 13;
    private static final int INDEX_MAX_HEIGHT = 14;
    private static final int INDEX_MIN_WIDTH = 15;
    private static final int INDEX_MIN_HEIGHT = 16;

    public AncelyConstraintLayout(@NonNull Context context) {
        this(context, null);
    }

    public AncelyConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AncelyConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) adjustChildren();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void adjustChildren() {
        for (int i = 0, n = getChildCount(); i < n; i++) {
            View view = getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof AutoLayoutParams) {
                LayoutInfo autoLayoutInfo = ((AutoLayoutParams) params).getAutoLayoutInfo();
                if (autoLayoutInfo != null) {
                    fillAttrs(view, autoLayoutInfo);
                }
            }
        }
    }

    public static final int BASE_WIDTH = 1;
    public static final int BASE_HEIGHT = 2;

    private int getSize(int px, int baseCalculation, int widthOrHeight) {
        if (px == 0) {
            return 0;
        }
        int calculationSize;
        if (baseCalculation == 1) {// 横屏
            if (widthOrHeight == BASE_WIDTH) { //宽
                calculationSize = UIUtils.getUIUtils().getHeight(px);
            } else {
                calculationSize = UIUtils.getUIUtils().getWidth(px);
            }
        } else if (baseCalculation == 2) {//以宽来计算值
            calculationSize = UIUtils.getUIUtils().getWidth(px);
        } else if (baseCalculation == 3) {//以高来计算值
            calculationSize = UIUtils.getUIUtils().getHeight(px);
        } else {
            if (widthOrHeight == BASE_WIDTH) { //宽
                calculationSize = UIUtils.getUIUtils().getWidth(px);
            } else {
                calculationSize = UIUtils.getUIUtils().getHeight(px);
            }
        }
        return calculationSize;
    }


    private void fillAttrs(View view, LayoutInfo autoLayoutInfo) {
        int width = autoLayoutInfo.width;
        int height = autoLayoutInfo.height;
        AncelyConstraintLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (width != ConstraintLayout.LayoutParams.MATCH_PARENT && width != ConstraintLayout.LayoutParams.WRAP_CONTENT && width != DEFAULT_VALUE) {
            layoutParams.width = getSize(width, autoLayoutInfo.base_calculation, BASE_WIDTH);
        }
        if (height != ConstraintLayout.LayoutParams.MATCH_PARENT && height != ConstraintLayout.LayoutParams.WRAP_CONTENT && height != DEFAULT_VALUE) {
            layoutParams.height = getSize(height, autoLayoutInfo.base_calculation, BASE_HEIGHT);
        }
        boolean margin = (autoLayoutInfo.leftMargin == DEFAULT_VALUE) && (autoLayoutInfo.topMargin == DEFAULT_VALUE) && (autoLayoutInfo.rightMargin == DEFAULT_VALUE) && (autoLayoutInfo.bottomMargin == DEFAULT_VALUE);
        if (!margin) {
            layoutParams.leftMargin = getSize(autoLayoutInfo.leftMargin, autoLayoutInfo.base_calculation, BASE_WIDTH);
            layoutParams.topMargin = getSize(autoLayoutInfo.topMargin, autoLayoutInfo.base_calculation, BASE_HEIGHT);
            layoutParams.rightMargin = getSize(autoLayoutInfo.rightMargin, autoLayoutInfo.base_calculation, BASE_WIDTH);
            layoutParams.bottomMargin = getSize(autoLayoutInfo.bottomMargin, autoLayoutInfo.base_calculation, BASE_HEIGHT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMarginStart(layoutParams.leftMargin);
                layoutParams.setMarginEnd(layoutParams.rightMargin);
            }
        }
        view.setLayoutParams(layoutParams);
        boolean padding = (autoLayoutInfo.paddingLeft == DEFAULT_VALUE) && (autoLayoutInfo.paddingTop == DEFAULT_VALUE) && (autoLayoutInfo.paddingRight == DEFAULT_VALUE) && (autoLayoutInfo.paddingBottom == DEFAULT_VALUE);

        if (!padding) {
            view.setPadding(
                    getSize(autoLayoutInfo.paddingLeft, autoLayoutInfo.base_calculation, BASE_WIDTH),
                    getSize(autoLayoutInfo.paddingTop, autoLayoutInfo.base_calculation, BASE_HEIGHT),
                    getSize(autoLayoutInfo.paddingRight, autoLayoutInfo.base_calculation, BASE_WIDTH),
                    getSize(autoLayoutInfo.paddingBottom, autoLayoutInfo.base_calculation, BASE_HEIGHT));
        }
        if (autoLayoutInfo.textSize != DEFAULT_VALUE) {
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, UIUtils.getUIUtils().getWidth(autoLayoutInfo.textSize));
            }
        }


        if (autoLayoutInfo.minWidth != DEFAULT_VALUE) {
            if (view instanceof TextView) {
                ((TextView) view).setMinWidth(getSize(autoLayoutInfo.minWidth, autoLayoutInfo.base_calculation, BASE_WIDTH));
            }
        }

        if (autoLayoutInfo.minHeight != DEFAULT_VALUE) {
            if (view instanceof TextView) {
                ((TextView) view).setMinHeight(getSize(autoLayoutInfo.minHeight, autoLayoutInfo.base_calculation, BASE_HEIGHT));
            }
        }

        if (autoLayoutInfo.maxWidth != DEFAULT_VALUE) {
            if (view instanceof TextView) {
                ((TextView) view).setMaxWidth(getSize(autoLayoutInfo.maxWidth, autoLayoutInfo.base_calculation, BASE_WIDTH));
            }
        }

        if (autoLayoutInfo.maxHeight != DEFAULT_VALUE) {
            if (view instanceof TextView) {
                ((TextView) view).setMaxHeight(getSize(autoLayoutInfo.maxHeight, autoLayoutInfo.base_calculation, BASE_HEIGHT));
            }
        }
    }

    @Override
    public ConstraintLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AncelyConstraintLayout.LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ConstraintLayout.LayoutParams implements AutoLayoutParams {
        private final LayoutInfo mLayoutInfo;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            boolean padding = false;
            boolean margin = false;
            mLayoutInfo = new LayoutInfo();
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AncelyConstraintLayout_Layout);
            int baseCalculation = a.getInteger(R.styleable.AncelyConstraintLayout_Layout_base_orientation, -1);
            a.recycle();
            mLayoutInfo.base_calculation = baseCalculation;
            TypedArray array = context.obtainStyledAttributes(attrs, LL);
            int n = array.getIndexCount();
            for (int i = 0; i < n; i++) {
                int index = array.getIndex(i);
                int pxVal;
                try {
                    pxVal = array.getDimensionPixelOffset(index, 0);
                } catch (Exception ignore) {
                    continue;
                }
                switch (index) {
                    case INDEX_TEXT_SIZE:
                        mLayoutInfo.textSize = pxVal;
                        break;
                    case INDEX_PADDING:
                        mLayoutInfo.paddingLeft = mLayoutInfo.paddingTop = mLayoutInfo.paddingRight = mLayoutInfo.paddingBottom = pxVal;
                        padding = true;
                        break;
                    case INDEX_PADDING_LEFT:
                        if (padding) break;
                        mLayoutInfo.paddingLeft = pxVal;
                        break;
                    case INDEX_PADDING_TOP:
                        if (padding) break;
                        mLayoutInfo.paddingTop = pxVal;
                        break;
                    case INDEX_PADDING_RIGHT:
                        if (padding) break;
                        mLayoutInfo.paddingRight = pxVal;
                        break;
                    case INDEX_PADDING_BOTTOM:
                        if (padding) break;
                        mLayoutInfo.paddingBottom = pxVal;
                        break;
                    case INDEX_WIDTH:
                        mLayoutInfo.width = pxVal;
                        break;
                    case INDEX_HEIGHT:
                        mLayoutInfo.height = pxVal;
                        break;
                    case INDEX_MARGIN:
                        mLayoutInfo.leftMargin = mLayoutInfo.topMargin = mLayoutInfo.rightMargin = mLayoutInfo.bottomMargin = pxVal;
                        margin = true;
                        break;
                    case INDEX_MARGIN_LEFT:
                        if (margin) break;
                        mLayoutInfo.leftMargin = pxVal;
                        break;
                    case INDEX_MARGIN_TOP:
                        if (margin) break;
                        mLayoutInfo.topMargin = pxVal;
                        break;
                    case INDEX_MARGIN_RIGHT:
                        if (margin) break;
                        mLayoutInfo.rightMargin = pxVal;
                        break;
                    case INDEX_MARGIN_BOTTOM:
                        if (margin) break;
                        mLayoutInfo.bottomMargin = pxVal;
                        break;
                    case INDEX_MAX_WIDTH:
                        mLayoutInfo.maxWidth = pxVal;
                        break;
                    case INDEX_MAX_HEIGHT:
                        mLayoutInfo.maxHeight = pxVal;
                        break;
                    case INDEX_MIN_WIDTH:
                        mLayoutInfo.minWidth = pxVal;
                        break;
                    case INDEX_MIN_HEIGHT:
                        mLayoutInfo.minHeight = pxVal;
                        break;
                }
            }
            array.recycle();
        }

        @Override
        public LayoutInfo getAutoLayoutInfo() {
            return mLayoutInfo;
        }
    }

    public interface AutoLayoutParams {
        LayoutInfo getAutoLayoutInfo();
    }

    private static class LayoutInfo {
        protected int width = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int height = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int leftMargin = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int topMargin = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int rightMargin = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int bottomMargin = AncelyConstraintLayout.DEFAULT_VALUE;

        protected int paddingLeft = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int paddingTop = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int paddingRight = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int paddingBottom = AncelyConstraintLayout.DEFAULT_VALUE;

        protected int maxWidth = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int maxHeight = AncelyConstraintLayout.DEFAULT_VALUE;

        protected int minWidth = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int minHeight = AncelyConstraintLayout.DEFAULT_VALUE;

        protected int textSize = AncelyConstraintLayout.DEFAULT_VALUE;
        protected int base_calculation;
    }
}
