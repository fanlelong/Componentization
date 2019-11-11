package com.ancely.fyw.aroute.skin.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatViewInflater;
import android.util.AttributeSet;
import android.view.View;


/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.skin.view
 *  @文件名:   SelfAppCompatViewInflater
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/11 11:05 AM
 */
public class SelfAppCompatViewInflater extends AppCompatViewInflater {

    private String name; // 控件名
    private Context context; // 上下文
    private AttributeSet attrs; // 某控件对应所有属性

    public SelfAppCompatViewInflater(@NonNull Context context) {
        this.context = context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttrs(AttributeSet attrs) {
        this.attrs = attrs;
    }

    /**
     * @return 自动匹配控件名，并初始化控件对象
     */
    public View autoMatch() {
        View view = null;
        switch (name) {
            case "LinearLayout":
                // view = super.createTextView(context, attrs); // 源码写法
                view = new SkinLinearLayout(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "RelativeLayout":
                view = new SkinRelativeLayout(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "TextView":
                view = new SkinTextView(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "ImageView":
                view = new SkinImageView(context, attrs);
                this.verifyNotNull(view, name);
                break;
            case "Button":
                view = new SkinButton(context, attrs);
                this.verifyNotNull(view, name);
                break;
        }

        return view;
    }

    /**
     * 校验控件不为空（源码方法，由于private修饰，只能复制过来了。为了代码健壮，可有可无）
     *
     * @param view 被校验控件，如：AppCompatTextView extends TextView（v7兼容包，兼容是重点！！！）
     * @param name 控件名，如："ImageView"
     */
    private void verifyNotNull(View view, String name) {
        if (view == null) {
            throw new IllegalStateException(this.getClass().getName() + " asked to inflate view for <" + name + ">, but returned null");
        }
    }
}
