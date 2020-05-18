package com.ancely.fyw.mvptext.shape;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.mvptext.shape
 *  @文件名:   Trianger
 *  @创建者:   fanlelong
 *  @创建时间:  2020/5/10 12:33 AM
 *  @描述：    TODO
 */
public class Trianger {
    //openGl二个步骤

    //1: 初始化
    //2:渲染
    static float traiangerCoords[] = {
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
    };
    private FloatBuffer mFloatBuffer;
    private String vertextShaderCode = " attribute vec4 vPosition;\n" +
            "void main(){" +
            "gl_Position=vPosition;" +
            "}";

    private final String fragmentShaderCode = " precision mediump float;\n" +
            "uniform  vec4 vColor;\n" +
            "void main() {\n" +
            "gl_FragColor=vColor;\t\n" +
            "}";
    private int mProgram;


    public Trianger() {
        //初始化
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(traiangerCoords.length * 4); //申明内存大小, 因为float是4个字节
        byteBuffer.order(ByteOrder.nativeOrder());//GPU的内存排列顺序
        mFloatBuffer = byteBuffer.asFloatBuffer();//相当于获取一个管道
        mFloatBuffer.put(traiangerCoords);//把这门语言推送给GPU

        mFloatBuffer.position(0);
        //现在写GL语言

        //1:创建顶点着色器程序
        int shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        //编译程序 shader程序id
        GLES20.glShaderSource(shader, vertextShaderCode);
        //在GPU中编译
        GLES20.glCompileShader(shader);


        //2: 创建片元着色器
        int frangmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        //编译程序 frangmentShader
        GLES20.glShaderSource(frangmentShader, fragmentShaderCode);
        //在GPU中编译
        GLES20.glCompileShader(frangmentShader);


        mProgram = GLES20.glCreateProgram();//创建一个程序管理器

        //将片元着色器和顶点着色器放到mProgram中来管理
        GLES20.glAttachShader(mProgram, shader);
        GLES20.glAttachShader(mProgram, frangmentShader);

        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);
    }

    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};

    public void onDrawFrame(GL10 gl) {
        //开始渲染
        GLES20.glUseProgram(mProgram);
        //获取一个指针 指向GPU某个内存区域vPosition
        int positionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //找开允许对变量读写
        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, mFloatBuffer);
        int colorHandler = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandler, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);
        //关闭
        GLES20.glDisableVertexAttribArray(positionHandler);


    }
}
