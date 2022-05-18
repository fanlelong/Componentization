package com.ancely.fyw.aroute.utils;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.view
 *  @文件名:   AutoLayoutHelp
 *  @创建者:   admin
 *  @创建时间:  2021/11/3 9:19
 *  @描述：    TODO
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ancely.fyw.aroute.R;
import com.ancely.fyw.aroute.views.AutoConstraintLayout;


public class AutoLayoutHelp {
    public static final int[] LL = new int[]
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


    public static final int DEFAULT_VALUE = 0;

    public final ViewGroup mHost;

    public AutoLayoutHelp(ViewGroup host) {
        mHost = host;
    }

    public interface AutoLayoutParams {
        LayoutInfo getAutoLayoutInfo();

        void setMarginStart(int start);

        void setMarginEnd(int end);
    }

    public static class LayoutInfo {
        public int width = DEFAULT_VALUE;
        public int height = DEFAULT_VALUE;
        protected boolean haveMargin = false;
        protected boolean havePadding = false;
        protected int leftMargin = DEFAULT_VALUE;
        protected int topMargin = DEFAULT_VALUE;
        protected int rightMargin = DEFAULT_VALUE;
        protected int bottomMargin = DEFAULT_VALUE;

        protected int paddingLeft = DEFAULT_VALUE;
        protected int paddingTop = DEFAULT_VALUE;
        protected int paddingRight = DEFAULT_VALUE;
        protected int paddingBottom = DEFAULT_VALUE;

        protected int maxWidth = DEFAULT_VALUE;
        protected int maxHeight = DEFAULT_VALUE;

        protected int minWidth = DEFAULT_VALUE;
        protected int minHeight = DEFAULT_VALUE;

        protected int textSize = DEFAULT_VALUE;
        protected int base_calculation;
    }

    public static LayoutInfo getLayoutInfo(Context context, AttributeSet attrs) {
        boolean padding = false;
        boolean margin = false;
        LayoutInfo layoutInfo = new LayoutInfo();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLayout_Layout);
        int baseCalculation = a.getInteger(R.styleable.AutoLayout_Layout_base_orientation, -1);
        a.recycle();
        layoutInfo.base_calculation = baseCalculation;
        TypedArray array = context.obtainStyledAttributes(attrs, AutoLayoutHelp.LL);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = array.getIndex(i);
            int pxVal;
            try {
                if (!isPxVal(array.peekValue(index))) continue;
                pxVal = array.getDimensionPixelOffset(index, 0);
            } catch (Exception ignore) {
                continue;
            }
            switch (index) {
                case INDEX_TEXT_SIZE:
                    layoutInfo.textSize = pxVal;
                    break;
                case INDEX_PADDING:
                    layoutInfo.paddingLeft = layoutInfo.paddingTop = layoutInfo.paddingRight = layoutInfo.paddingBottom = pxVal;
                    layoutInfo.havePadding = padding = true;
                    break;
                case INDEX_PADDING_LEFT:
                    if (padding) break;
                    layoutInfo.paddingLeft = pxVal;
                    layoutInfo.havePadding = true;
                    break;
                case INDEX_PADDING_TOP:
                    if (padding) break;
                    layoutInfo.paddingTop = pxVal;
                    layoutInfo.havePadding = true;
                    break;
                case INDEX_PADDING_RIGHT:
                    if (padding) break;
                    layoutInfo.paddingRight = pxVal;
                    layoutInfo.havePadding = true;
                    break;
                case INDEX_PADDING_BOTTOM:
                    if (padding) break;
                    layoutInfo.paddingBottom = pxVal;
                    layoutInfo.havePadding = true;
                    break;
                case INDEX_WIDTH:
                    layoutInfo.width = pxVal;
                    break;
                case INDEX_HEIGHT:
                    layoutInfo.height = pxVal;
                    break;
                case INDEX_MARGIN:
                    layoutInfo.leftMargin = layoutInfo.topMargin = layoutInfo.rightMargin = layoutInfo.bottomMargin = pxVal;
                    layoutInfo.haveMargin = margin = true;
                    break;
                case INDEX_MARGIN_LEFT:
                    if (margin) break;
                    layoutInfo.leftMargin = pxVal;
                    layoutInfo.haveMargin = true;
                    break;
                case INDEX_MARGIN_TOP:
                    if (margin) break;
                    layoutInfo.topMargin = pxVal;
                    layoutInfo.haveMargin = true;
                    break;
                case INDEX_MARGIN_RIGHT:
                    if (margin) break;
                    layoutInfo.rightMargin = pxVal;
                    layoutInfo.haveMargin = true;
                    break;
                case INDEX_MARGIN_BOTTOM:
                    if (margin) break;
                    layoutInfo.bottomMargin = pxVal;
                    layoutInfo.haveMargin = true;
                    break;
                case INDEX_MAX_WIDTH:
                    layoutInfo.maxWidth = pxVal;
                    break;
                case INDEX_MAX_HEIGHT:
                    layoutInfo.maxHeight = pxVal;
                    break;
                case INDEX_MIN_WIDTH:
                    layoutInfo.minWidth = pxVal;
                    break;
                case INDEX_MIN_HEIGHT:
                    layoutInfo.minHeight = pxVal;
                    break;
            }
        }
        array.recycle();
        return layoutInfo;
    }

    public void adjustChildren() {
        for (int i = 0, n = mHost.getChildCount(); i < n; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();

            AutoLayoutParams alp;
            if (!(params instanceof AutoLayoutParams)) {
                return;
            }
            alp = (AutoLayoutParams) params;
            LayoutInfo autoLayoutInfo = alp.getAutoLayoutInfo();
            if (autoLayoutInfo == null) {
                return;
            }
            fillAttrs(view, autoLayoutInfo);
            if (autoLayoutInfo.haveMargin) {
                ((AutoLayoutParams) params).setMarginStart(autoLayoutInfo.leftMargin);
                ((AutoLayoutParams) params).setMarginEnd(autoLayoutInfo.rightMargin);
            }
        }
    }

    private void fillAttrs(View view, LayoutInfo autoLayoutInfo) {
        int width = autoLayoutInfo.width;
        int height = autoLayoutInfo.height;
        if (width != AutoConstraintLayout.LayoutParams.MATCH_PARENT && width != AutoConstraintLayout.LayoutParams.WRAP_CONTENT && width != DEFAULT_VALUE) {
            autoLayoutInfo.width = getSize(width, autoLayoutInfo.base_calculation, BASE_WIDTH);
        }
        if (height != AutoConstraintLayout.LayoutParams.MATCH_PARENT && height != AutoConstraintLayout.LayoutParams.WRAP_CONTENT && height != DEFAULT_VALUE) {
            autoLayoutInfo.height = getSize(height, autoLayoutInfo.base_calculation, BASE_HEIGHT);
        }
        if (autoLayoutInfo.haveMargin) {
            autoLayoutInfo.leftMargin = getSize(autoLayoutInfo.leftMargin, autoLayoutInfo.base_calculation, BASE_WIDTH);
            autoLayoutInfo.topMargin = getSize(autoLayoutInfo.topMargin, autoLayoutInfo.base_calculation, BASE_HEIGHT);
            autoLayoutInfo.rightMargin = getSize(autoLayoutInfo.rightMargin, autoLayoutInfo.base_calculation, BASE_WIDTH);
            autoLayoutInfo.bottomMargin = getSize(autoLayoutInfo.bottomMargin, autoLayoutInfo.base_calculation, BASE_HEIGHT);
        }
        if (autoLayoutInfo.havePadding) {
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

    private static int getComplexUnit(int data) {
        return TypedValue.COMPLEX_UNIT_MASK & (data >> TypedValue.COMPLEX_UNIT_SHIFT);
    }

    public static boolean isPxVal(TypedValue val) {
        return val != null && val.type == TypedValue.TYPE_DIMENSION && getComplexUnit(val.data) == TypedValue.COMPLEX_UNIT_PX;
    }
}
