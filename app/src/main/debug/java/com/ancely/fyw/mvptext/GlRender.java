package com.ancely.fyw.mvptext;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ancely.fyw.mvptext.shape.Trianger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext
 *  @文件名:   GlRender
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/10 12:21 AM
 *  @描述：    TODO
 */
public class GlRender implements GLSurfaceView.Renderer {
    GlView mGlView;
    Trianger mTrianger;
    public GlRender(GlView glView) {
        mGlView = glView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 0);
        mTrianger = new Trianger();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    //这个方法会不断被调用
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mTrianger.onDrawFrame(gl);
    }
}
