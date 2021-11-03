package com.ancely.fyw.aroute.floatball;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.ancely.fyw.aroute.R;
import com.ancely.fyw.aroute.utils.UIUtils;
import com.ancely.fyw.aroute.utils.ViewCalculateUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class FloatPopup extends PopupWindow {

    Handler mHandler = new Handler(Looper.getMainLooper());
    //设置悬浮按钮尺寸
    private final int size = UIUtils.getUIUtils().getWidth(86 + 43);
    private final int imageSize = UIUtils.getUIUtils().getWidth(86);

    private int screenWidth;
    private int screenHeight;
    //记录当前手指实时位置
    private float x, y;
    //记录手指按下时的位置
    private float downX, downY;
    private float showY;
    private boolean isClickState;
    private final int clickLimitValue = UIUtils.getUIUtils().getWidth(18) / 2;
    @SuppressLint("StaticFieldLeak")
    private static FloatPopup floatPopup;
    private final ImageView pointView;
    private float lastX = 0;
    private float lastY = UIUtils.getUIUtils().getWidth(18) * 20;
    private final int statusHeight;
    private final AtomicBoolean animationing = new AtomicBoolean(false);
    private final View floatView;
    private AnimatorSet animatorSet;
    private ValueAnimator animator;//第一次显示悬浮球动画

    private Rect leftSafeArea;//左边安全区域
    private Rect rightSafeArea;//右边安全区域
    private Rect safeArea;
    private int unSafeAreaWidth = 0; // 不安全区域的宽度
    private Activity mActivity;
    public synchronized static FloatPopup getInstance(Activity activity) {
        if (floatPopup == null) {
            floatPopup = new FloatPopup(activity);
        }
        return floatPopup;
    }

    public void show() {

        try {
            if (floatPopup.isShowing()) {
                floatPopup.dismiss();
            }
        } catch (Exception e) {
        }
        float animatorEnd;
        View decorView = mActivity.getWindow().getDecorView();
        int width = decorView.getWidth();
        int widthPixels = mActivity.getResources().getDisplayMetrics().widthPixels;
        screenWidth = width = Math.max(width, widthPixels);
        getSafeArea(mActivity);
        Rect safeRect;
        if (lastX > width / 2.f) {
            lastX = width - size;
            animatorEnd = width - size / 2.f-unSafeAreaWidth;
            endX = width - size + UIUtils.getUIUtils().getWidth(43) / 2;
            safeRect = rightSafeArea;
        } else {
            lastX = (-size / 2.f);
            endX = -size / 2 + UIUtils.getUIUtils().getWidth(86) / 2;
            animatorEnd = lastX + unSafeAreaWidth;
            safeRect = leftSafeArea;
        }

        //留海只做横屏游戏适配，竖屏忽略
        if ((mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) && (safeRect != null) && ((lastY + imageSize) >= safeRect.top) && (lastY <= safeRect.bottom)) {
            //证明已经在留海里面
            if (this.x < screenWidth / 2.f) {
                endX = -size / 2 + UIUtils.getUIUtils().getWidth(86) / 2 + safeRect.right;
            } else {
                endX = width - size + UIUtils.getUIUtils().getWidth(43) / 2 - (safeRect.right-safeRect.left);
            }
        }else{
            unSafeAreaWidth = 0;
        }
        showY = lastY;
        if (!false) {
            mHandler.post(() -> {
                if (mActivity == null || mActivity.isDestroyed()) {
                    floatPopup = null;
                    getInstance(mActivity).show();
                    return;
                }
                animationing.set(true);
                floatPopup.showAtLocation(mActivity.getWindow().getDecorView(),
                        Gravity.NO_GRAVITY, -size / 2 +unSafeAreaWidth + UIUtils.getUIUtils().getWidth(86) / 2, (int) lastY);
                mHandler.postDelayed(alphaAnimatorRunable, 500);
            });
            return;
        }

        screenHeight = mActivity.getResources().getDisplayMetrics().heightPixels;
        mHandler.post(() -> {
            if (mActivity == null || mActivity.isDestroyed()) {
                floatPopup = null;
                getInstance(mActivity).show();
                return;
            }
            floatPopup.showAtLocation(mActivity.getWindow().getDecorView(),
                    Gravity.NO_GRAVITY, (int) animatorEnd, (int) lastY);
        });
    }


    private void getSafeArea(Activity activity) {
        if (Build.VERSION.SDK_INT >= 28) {
            DisplayCutout displayCutout = activity.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
            if (displayCutout != null) {
                List<Rect> boundingRectList = displayCutout.getBoundingRects();
                for (Rect boundingRect : boundingRectList) {
                    if ((boundingRect.right >= imageSize / 3) && (boundingRect.left < imageSize / 3)) {
                        leftSafeArea = boundingRect;
                        safeArea = boundingRect;
                        rightSafeArea = null;
                    } else if ((boundingRect.left < (screenWidth - imageSize / 3)) && (boundingRect.right >= (screenWidth - imageSize / 3))) {
                        rightSafeArea = boundingRect;
                        safeArea = boundingRect;
                        leftSafeArea = null;
                    }
                }
            }
            if (safeArea != null) {
                unSafeAreaWidth = Math.abs(safeArea.left - safeArea.right);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    public FloatPopup(Activity activity) {
        //记录当前悬浮按钮显示位置
        mActivity = activity;
        statusHeight = UIUtils.getUIUtils().getSystemBarHeight(activity);
        floatView = View.inflate(activity, R.layout.view_float_ball, null);
        pointView = floatView.findViewById(R.id.float_ball_point);
        pointView.setVisibility(View.GONE);
        ImageView floatBallIcon = floatView.findViewById(R.id.float_ball_icon);
        ViewCalculateUtil.setViewLayoutParam(pointView, 16, 8, 0, 0, 8);
        ViewCalculateUtil.setViewLayoutParam(floatBallIcon, 86);
        setContentView(floatView);
        setWidth(size);
        setHeight(size);
        setFocusable(false);
        setClippingEnabled(false);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(false);
        getContentView().setOnClickListener(v -> {
        });
//        setTouchInterceptor(new PopStackViewTouchListener(iv,UIUtils.getUIUtils().getWidth(18) / 4, true,this));
        setTouchInterceptor((v, event) -> {
            x = event.getRawX();
            y = event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getRawX();
                    downY = event.getRawY();
                    isClickState = true;
                    break;
                case MotionEvent.ACTION_MOVE:


                    if (animatorSet != null && animatorSet.isRunning()) {
                        animatorSet.cancel();
                    } else {
                        v.removeCallbacks(alphaAnimatorRunable);
                    }

                    if (Math.abs(this.x - downX) < clickLimitValue && Math.abs(y - downY) < clickLimitValue && isClickState) {
                        isClickState = true;
                    } else {
                        isClickState = false;
                        float offX = event.getRawX() - downX;
                        float offY = event.getRawY() - downY;

                        if (!animationing.get()) {
                            update((int) (lastX + offX), (int) (offY + lastY));
                            if (floatView.getAlpha() != 1) {
                                floatView.setAlpha(1);
                            }
                        }
                        startX = (int) (lastX + offX);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (this.x - downX < clickLimitValue && isClickState) {
                        getContentView().performClick();
                        break;
                    }
                    if (animationing.get()) {
                        return true;
                    }

                    getSafeArea(activity);

                    showY = lastY + event.getRawY() - downY;
                    if (showY <= 0) {
                        showY = statusHeight;
                    } else if (showY >= (screenHeight - size)) {
                        showY = screenHeight - size;
                    }

                    Rect safeRect;
                    if (this.x < screenWidth / 2.f) {
                        endX = -size / 2 + UIUtils.getUIUtils().getWidth(86) / 2;
                        safeRect = leftSafeArea;
                    } else {
                        endX = screenWidth - size + UIUtils.getUIUtils().getWidth(43) / 2;
                        safeRect = rightSafeArea;
                    }
                    //留海只做横屏游戏适配，竖屏忽略
                    if ((activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) && (safeRect != null) && ((showY + imageSize) >= safeRect.top) && (showY <= safeRect.bottom)) {
                        if (this.x < screenWidth / 2.f) {
                            endX = -size / 2 + UIUtils.getUIUtils().getWidth(86) / 2 + safeRect.right;
                        } else {
                            endX = screenWidth - size + UIUtils.getUIUtils().getWidth(43) / 2 - (safeRect.right-safeRect.left);
                        }
                    } else {
                        unSafeAreaWidth = 0;
                        endY = 0;
                    }

                    animationing.set(true);
                    lastY = showY;
                    v.removeCallbacks(actionUpAnimatorRunnable);
                    v.removeCallbacks(alphaAnimatorRunable);
                    v.post(actionUpAnimatorRunnable);
                    v.postDelayed(alphaAnimatorRunable, 1300);
                    break;
            }
            return true;
        });
    }

    private int startX;
    private int endX;
    private float endY;

    private int xDisparity;
    private float yDisparity;
    /**
     * 悬浮球移动结束后的动画事件
     */
    Runnable actionUpAnimatorRunnable = new Runnable() {


        @Override
        public void run() {

            xDisparity = endX - startX;
            yDisparity = endY - showY;
            animator = ValueAnimator.ofFloat(0, 1).setDuration(300);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(animation -> {
                lastX = startX + (float) (animation.getAnimatedValue()) * xDisparity;
                if (endY > 0) {
                    lastY = showY + (float) (animation.getAnimatedValue()) * yDisparity;
                }
                update((int) (lastX), (int) lastY);

            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animationing.set(true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animationing.set(false);
                    showY = lastY;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    animationing.set(false);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }
    };

    /**
     * 悬浮球移动结束后的动画结束后，执行透明+隐藏一半悬浮球动画
     */
    Runnable alphaAnimatorRunable = new Runnable() {

        @Override
        public void run() {
            int alphaEndX;
            if (lastX > screenWidth / 2.f) {
                lastX = mActivity.getResources().getDisplayMetrics().widthPixels - size;
                alphaEndX = (int) (screenWidth - size / 2.f - unSafeAreaWidth);
            } else {
                alphaEndX = (int) (-size / 2.f + unSafeAreaWidth);
            }
            animatorSet = new AnimatorSet();
            ValueAnimator animator1 = ValueAnimator.ofInt(endX, alphaEndX).setDuration(300);
            animator1.setInterpolator(new LinearInterpolator());
            animator1.addUpdateListener(animation -> {
                update((int) (animation.getAnimatedValue()), (int) showY);
                lastX = (int) (animation.getAnimatedValue());
            });

            ValueAnimator animator2 = ValueAnimator.ofFloat(1, 0.5f).setDuration(300);
            animator2.setInterpolator(new LinearInterpolator());
            animator2.addUpdateListener(animation -> floatView.setAlpha((Float) animation.getAnimatedValue()));
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animationing.set(true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animationing.set(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    animationing.set(false);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorSet.playTogether(animator1, animator2);
            animatorSet.start();
        }
    };

    @Override
    public void update(int x, int y) {
        if (mActivity==null || mActivity.isDestroyed()) {
            return;
        }
        this.update(x, y, size, size);
    }

    @Override
    public void update(int x, int y, int width, int height) {
        super.update(x, y, width, height);
    }

    public void release() {
        dismiss();
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (animator != null) {
            animator.cancel();
        }
        floatPopup = null;
    }

    public void setFloatBallRedPointVisibility(boolean visibility) {
        if (pointView != null) {
            pointView.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }
}