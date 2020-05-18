package com.ancely.fyw.mvptext;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext
 *  @文件名:   GlView
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/10 12:20 AM
 *  @描述：    TODO
 */
public class GlView extends GLSurfaceView {
    public GlView(Context context) {
        super(context);
    }

    public GlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2 );
        setRenderer(new GlRender(this));//这行代码的意思就是渲染交给了GLRrender
        //RENDERMODE_CONTINUOUSLY: 实时渲染
        //RENDERMODE_WHEN_DIRTY: 需要时就渲染 效率高.需要自己调用requestRender
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//        requestRender();//当是按需要渲染时,就需要调用这个
    }
}
