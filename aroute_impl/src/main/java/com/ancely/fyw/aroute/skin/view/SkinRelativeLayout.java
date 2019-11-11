package com.ancely.fyw.aroute.skin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.ancely.fyw.aroute.R;
import com.ancely.fyw.aroute.manager.PluginManager;
import com.ancely.fyw.aroute.skin.bean.AttrsBean;
import com.ancely.fyw.aroute.skin.impl.ViewsMatch;


public class SkinRelativeLayout extends RelativeLayout implements ViewsMatch {

    private AttrsBean attrsBean;

    public SkinRelativeLayout(Context context) {
        this(context, null);
    }

    public SkinRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        attrsBean = new AttrsBean();

        // 根据自定义属性，匹配控件属性的类型集合，如：background
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SkinRelativeLayout,
                defStyleAttr, 0);
        // 存储到临时JavaBean对象
        attrsBean.saveViewResource(typedArray, R.styleable.SkinRelativeLayout);
        // 这一句回收非常重要！obtainStyledAttributes()有语法提示！！
        typedArray.recycle();
    }

    @Override
    public void startChangerSkin() {
        // 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.SkinRelativeLayout[R.styleable.SkinRelativeLayout_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = attrsBean.getViewResource(key);
        if (backgroundResourceId > 0) {
            // 是否默认皮肤
            if (PluginManager.getInstance().isDefaultSkin()) {
                // 兼容包转换
                Drawable drawable = ContextCompat.getDrawable(getContext(), backgroundResourceId);
                // 控件自带api，这里不用setBackgroundColor()因为在9.0测试不通过
                // setBackgroundDrawable在这里是过时了
                setBackground(drawable);
            } else {
                // 获取皮肤包资源
                Object skinResourceId = PluginManager.getInstance().getBackgroundOrSrc(backgroundResourceId);
                // 兼容包转换
                if (skinResourceId instanceof Integer) {
                    int color = (int) skinResourceId;
                    setBackgroundColor(color);
                    // setBackgroundResource(color); // 未做兼容测试
                } else {
                    Drawable drawable = (Drawable) skinResourceId;
                    setBackground(drawable);
                }
            }
        }
    }
}
