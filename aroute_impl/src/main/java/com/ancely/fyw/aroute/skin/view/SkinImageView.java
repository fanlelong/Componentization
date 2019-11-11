package com.ancely.fyw.aroute.skin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.ancely.fyw.aroute.R;
import com.ancely.fyw.aroute.manager.PluginManager;
import com.ancely.fyw.aroute.skin.bean.AttrsBean;
import com.ancely.fyw.aroute.skin.impl.ViewsMatch;


/**
 * 继承TextView兼容包，9.0源码中也是如此
 * 参考：AppCompatViewInflater.java
 * 86行 + 138行 + 206行
 */
public class SkinImageView extends AppCompatImageView implements ViewsMatch {

    private AttrsBean attrsBean;

    public SkinImageView(Context context) {
        this(context, null);
    }

    public SkinImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        attrsBean = new AttrsBean();

        // 根据自定义属性，匹配控件属性的类型集合，如：src
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SkinImageView,
                defStyleAttr, 0);
        // 存储到临时JavaBean对象
        attrsBean.saveViewResource(typedArray, R.styleable.SkinImageView);
        // 这一句回收非常重要！obtainStyledAttributes()有语法提示！！
        typedArray.recycle();
    }

    @Override
    public void startChangerSkin() {
        // 根据自定义属性，获取styleable中的src属性
        int key = R.styleable.SkinImageView[R.styleable.SkinImageView_android_src];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = attrsBean.getViewResource(key);
        if (backgroundResourceId > 0) {
            // 是否默认皮肤
            if (PluginManager.getInstance().isDefaultSkin()) {
                // 兼容包转换
                Drawable drawable = ContextCompat.getDrawable(getContext(), backgroundResourceId);
                setImageDrawable(drawable);
            } else {
                // 获取皮肤包资源
                Object skinResourceId = PluginManager.getInstance().getBackgroundOrSrc(backgroundResourceId);
                // 兼容包转换
                if (skinResourceId instanceof Integer) {
                    int color = (int) skinResourceId;
                    setImageResource(color);
                    // setImageBitmap(); // Bitmap未添加
                } else {
                    Drawable drawable = (Drawable) skinResourceId;
                    setImageDrawable(drawable);
                }
            }
        }
    }
}
