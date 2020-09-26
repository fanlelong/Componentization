package com.ancely.fyw.aroute.floatball;

import android.animation.ValueAnimator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

public class StackViewTouchListener implements View.OnTouchListener {
    private View stackView;
    private float dX, dY = 0f;
    private float downX, downY = 0f;
    private boolean isClickState;
    private int clickLimitValue;
    private int screenWidth;
    private int screenHeight;
    private int finalMoveX;

    public StackViewTouchListener(View stackView, int clickLimitValue) {
        this.stackView = stackView;
        this.clickLimitValue = clickLimitValue;
        DisplayMetrics displayMetrics = stackView.getContext().getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float X = event.getRawX();
        float Y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClickState = true;
                downX = X;
                downY = Y;
                dX = stackView.getX() - event.getRawX();
                dY = stackView.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(X - downX) < clickLimitValue && Math.abs(Y - downY) < clickLimitValue && isClickState) {
                    isClickState = true;
                } else {
                    isClickState = false;

                    stackView.setX(event.getRawX() + dX);
                    if (event.getRawY() + dY < 0) {
                        stackView.setY(0);
                    } else if ((event.getRawY() + dY + v.getMeasuredHeight()) >= screenHeight) {
                        stackView.setY(screenHeight - v.getMeasuredHeight());
                    } else {
                        stackView.setY(event.getRawY() + dY);
                    }
                    Log.e("ancely>>>", "stackView.getX:" + stackView.getX() + "");
                    Log.e("ancely>>>", "stackView.getY:" + stackView.getY() + "");
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (X - downX < clickLimitValue && isClickState) {
                    stackView.performClick();
                }

                if (event.getRawX() >= screenWidth / 2) {
                    finalMoveX = screenWidth - v.getMeasuredWidth();
                } else {
                    finalMoveX = 0;
                }
                stickToSide();
                break;
            default:
                return false;
        }
        return true;
    }


    private void stickToSide() {
        ValueAnimator animator = ValueAnimator.ofFloat(stackView.getX(), finalMoveX).setDuration((long) Math.abs(stackView.getX() - finalMoveX));
        animator.setInterpolator(new BounceInterpolator());
        animator.addUpdateListener(animation -> {
            stackView.setX((Float) animation.getAnimatedValue());
        });
        animator.start();
    }
}