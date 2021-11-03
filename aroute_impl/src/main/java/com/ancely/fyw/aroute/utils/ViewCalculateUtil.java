package com.ancely.fyw.aroute.utils;


import android.content.res.Configuration;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class ViewCalculateUtil {

    /**
     * 根据屏幕的大小设置view的高度，间距
     *
     * @param view
     * @param width
     * @param height
     * @param topMargin
     * @param bottomMargin
     * @param lefMargin
     * @param rightMargin
     */
    public static void setViewLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin, int rightMargin) {

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();

        if (layoutParams != null) {
            if (width != ConstraintLayout.LayoutParams.MATCH_PARENT && width != ConstraintLayout.LayoutParams.WRAP_CONTENT) {
                layoutParams.width = UIUtils.getUIUtils().getWidth(width);
            } else {
                layoutParams.width = width;
            }
            if (height != ConstraintLayout.LayoutParams.MATCH_PARENT && height != ConstraintLayout.LayoutParams.WRAP_CONTENT) {
                layoutParams.height = UIUtils.getUIUtils().getHeight(height);
            } else {
                layoutParams.height = height;
            }


            layoutParams.topMargin = UIUtils.getUIUtils().getHeight(topMargin);
            layoutParams.bottomMargin = UIUtils.getUIUtils().getHeight(bottomMargin);
            layoutParams.leftMargin = UIUtils.getUIUtils().getWidth(lefMargin);
            layoutParams.rightMargin = UIUtils.getUIUtils().getWidth(rightMargin);

            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMarginStart(layoutParams.leftMargin);
                layoutParams.setMarginEnd(layoutParams.rightMargin);
            }
            view.setLayoutParams(layoutParams);
        }
    }

    /**
     * 横屏时的宽高适配
     */
    public static void setViewLayoutParamPortrait(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin, int rightMargin) {

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();

        if (layoutParams != null) {
            if (width != ConstraintLayout.LayoutParams.MATCH_PARENT && width != ConstraintLayout.LayoutParams.WRAP_CONTENT) {
                layoutParams.width = UIUtils.getUIUtils().getHeight(width);
            } else {
                layoutParams.width = width;
            }
            if (height != ConstraintLayout.LayoutParams.MATCH_PARENT && height != ConstraintLayout.LayoutParams.WRAP_CONTENT) {
                layoutParams.height = UIUtils.getUIUtils().getWidth(height);
            } else {
                layoutParams.height = height;
            }

            layoutParams.topMargin = UIUtils.getUIUtils().getWidth(topMargin);
            layoutParams.bottomMargin = UIUtils.getUIUtils().getWidth(bottomMargin);
            layoutParams.leftMargin = UIUtils.getUIUtils().getHeight(lefMargin);
            layoutParams.rightMargin = UIUtils.getUIUtils().getHeight(rightMargin);

            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMarginStart(layoutParams.leftMargin);
                layoutParams.setMarginEnd(layoutParams.rightMargin);
            }
            view.setLayoutParams(layoutParams);
        }
    }

    public static void setViewLayoutParam(View view, int width, int topMargin, int bottomMargin, int lefMargin, int rightMargin) {

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();

        if (layoutParams != null) {
            if (width != ConstraintLayout.LayoutParams.MATCH_PARENT && width != ConstraintLayout.LayoutParams.WRAP_CONTENT) {
                layoutParams.width = UIUtils.getUIUtils().getWidth(width);
                layoutParams.height = UIUtils.getUIUtils().getWidth(width);
            } else {
                layoutParams.width = width;
            }

            layoutParams.topMargin = UIUtils.getUIUtils().getHeight(topMargin);
            layoutParams.bottomMargin = UIUtils.getUIUtils().getHeight(bottomMargin);
            layoutParams.leftMargin = UIUtils.getUIUtils().getWidth(lefMargin);
            layoutParams.rightMargin = UIUtils.getUIUtils().getWidth(rightMargin);

            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMarginStart(layoutParams.leftMargin);
                layoutParams.setMarginEnd(layoutParams.rightMargin);
            }
            view.setLayoutParams(layoutParams);
        }
    }


    /**
     * 宽高一样
     */
    public static void setViewLayoutParam(View view, int width) {

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();

        if (layoutParams != null) {
            if (width != ConstraintLayout.LayoutParams.MATCH_PARENT && width != ConstraintLayout.LayoutParams.WRAP_CONTENT) {
                layoutParams.height = layoutParams.width = UIUtils.getUIUtils().getWidth(width);
            }
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setViewLayoutParam(View view, int topMargin, int bottomMargin, int lefMargin, int rightMargin) {

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();

        if (layoutParams != null) {
            layoutParams.topMargin = UIUtils.getUIUtils().getHeight(topMargin);
            layoutParams.bottomMargin = UIUtils.getUIUtils().getHeight(bottomMargin);
            layoutParams.leftMargin = UIUtils.getUIUtils().getWidth(lefMargin);
            layoutParams.rightMargin = UIUtils.getUIUtils().getWidth(rightMargin);
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMarginStart(layoutParams.leftMargin);
                layoutParams.setMarginEnd(layoutParams.rightMargin);
            }
            view.setLayoutParams(layoutParams);
        }
    }

    /**
     * @param view
     * @param width
     * @param height
     * @param topMargin
     * @param bottomMargin
     * @param lefMargin
     * @param rightMargin
     */
//    public static void setViewFrameLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin,
//                                               int rightMargin) {
//
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
//        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
//            layoutParams.width = UIUtils.getUIUtils().getWidth(width);
//        } else {
//            layoutParams.width = width;
//        }
//        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
//            layoutParams.height = UIUtils.getUIUtils().getHeight(height);
//        } else {
//            layoutParams.height = height;
//        }
//
//        layoutParams.topMargin = UIUtils.getUIUtils().getHeight(topMargin);
//        layoutParams.bottomMargin = UIUtils.getUIUtils().getHeight(bottomMargin);
//        layoutParams.leftMargin = UIUtils.getUIUtils().getWidth(lefMargin);
//        layoutParams.rightMargin = UIUtils.getUIUtils().getWidth(rightMargin);
//        view.setLayoutParams(layoutParams);
//    }

    /**
     * 设置view的内边距
     *
     * @param view
     * @param topPadding
     * @param bottomPadding
     * @param leftpadding
     * @param rightPadding
     */
    public static void setViewPadding(View view, int topPadding, int bottomPadding, int leftpadding, int rightPadding) {
        view.setPadding(UIUtils.getUIUtils().getWidth(leftpadding),
                UIUtils.getUIUtils().getHeight(topPadding),
                UIUtils.getUIUtils().getWidth(rightPadding),
                UIUtils.getUIUtils().getHeight(bottomPadding));
    }

    public static void setViewPadding(View view, int padding) {
        view.setPadding(UIUtils.getUIUtils().getWidth(padding),
                UIUtils.getUIUtils().getHeight(padding),
                UIUtils.getUIUtils().getWidth(padding),
                UIUtils.getUIUtils().getHeight(padding));
    }


    /**
     * 设置字号
     *
     * @param view
     * @param size
     */
    public static void setTextSize(TextView view, int size) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, UIUtils.getUIUtils().getWidth(size));
    }

    /**
     * 设置LinearLayout中 view的高度宽度
     *
     * @param view
     * @param width
     * @param height
     */
//    public static void setViewLinearLayoutParam(View view, int width, int height) {
//
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
//        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT) {
//            layoutParams.width = UIUtils.getUIUtils().getWidth(width);
//        } else {
//            layoutParams.width = width;
//        }
//        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT) {
//            layoutParams.height = UIUtils.getUIUtils().getHeight(height);
//        } else {
//            layoutParams.height = height;
//        }
//
//        view.setLayoutParams(layoutParams);
//    }
    public static void setViewGroupLayoutParamPx(View view, int width, int height) {

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void setViewGroupLayoutParamPortrait(View view, int width, int height) {

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT) {
            layoutParams.width = UIUtils.getUIUtils().getHeight(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT) {
            layoutParams.height = UIUtils.getUIUtils().getWidth(height);
        } else {
            layoutParams.height = height;
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setViewGroupLayoutParam(View view, int width, int height) {

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT) {
            layoutParams.width = UIUtils.getUIUtils().getWidth(width);

        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT) {
            layoutParams.height = UIUtils.getUIUtils().getHeight(height);
        } else {
            layoutParams.height = height;
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setViewGroupLayoutParam(View view, int orientation) {

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        layoutParams.width = (int) ((orientation == Configuration.ORIENTATION_LANDSCAPE)?UIUtils.sDisplayMetricsHeight:UIUtils.sDisplayMetricsWidth);
        layoutParams.height = (int) ((orientation == Configuration.ORIENTATION_LANDSCAPE)?UIUtils.sDisplayMetricsWidth:UIUtils.sDisplayMetricsHeight);
        view.setLayoutParams(layoutParams);
    }

    public static void setViewGroupLayoutParam(View view, int width, int height, int orientation) {

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutParams.width = UIUtils.getUIUtils().getHeight(width);
            } else {
                layoutParams.width = UIUtils.getUIUtils().getWidth(width);
            }
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutParams.height = UIUtils.getUIUtils().getWidth(height);
            } else {
                layoutParams.height = UIUtils.getUIUtils().getHeight(height);
            }
        } else {
            layoutParams.height = height;
        }
        view.setLayoutParams(layoutParams);
    }

//    public static void setViewGroupLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin,
//                                               int rightMargin) {
//
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
//            layoutParams.width = UIUtils.getUIUtils().getWidth(width);
//        } else {
//            layoutParams.width = width;
//        }
//        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
//            layoutParams.height = UIUtils.getUIUtils().getHeight(height);
//        } else {
//            layoutParams.height = height;
//        }
//
//        view.setLayoutParams(layoutParams);
//    }

    /**
     * 设置LinearLayout中 view的高度宽度
     *
     * @param view
     * @param width
     * @param height
     */
    public static void setViewLinearLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin,
                                                int rightMargin) {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.width = UIUtils.getUIUtils().getWidth(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.height = UIUtils.getUIUtils().getHeight(height);
        } else {
            layoutParams.height = height;
        }

        layoutParams.topMargin = UIUtils.getUIUtils().getHeight(topMargin);
        layoutParams.bottomMargin = UIUtils.getUIUtils().getHeight(bottomMargin);
        layoutParams.leftMargin = UIUtils.getUIUtils().getWidth(lefMargin);
        layoutParams.rightMargin = UIUtils.getUIUtils().getWidth(rightMargin);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(layoutParams.leftMargin);
            layoutParams.setMarginEnd(layoutParams.rightMargin);
        }
        view.setLayoutParams(layoutParams);
    }
}
