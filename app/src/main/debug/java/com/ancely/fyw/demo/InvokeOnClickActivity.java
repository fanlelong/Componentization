package com.ancely.fyw.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ancely.fyw.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.demo
 *  @文件名:   InvokeOnClickActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2020/4/4 9:19 PM
 *  @描述：    TODO
 */
public class InvokeOnClickActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoke);

        TextView textView = findViewById(R.id.act_invoke_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InvokeOnClickActivity.this, ((TextView)v).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        try {
            hook(textView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * hook分析: 我们主要hook点是hook  OnClickListener的onClick方法 而OnClickListener怎么获取到呢
     * 1: textView.setOnClickListener 将View.OnclickListener 这样传的 getListenerInfo().mOnClickListene
     * 所以首先要获取的是View里面的getListenerInfo这个方法, 通过执行这个方法可以获取到ListenerInfo这个
     *
     * 2: 因为我们setOnClickListener的时候就将listener附给了ListenerInfo 所以要获取到OnClickListener的话就得通过这个类的成员变量来
     * 也就是ListenerInfo里的mOnClickListener这个成员变量
     * @param view
     * @throws Exception
     */
    private void hook(View view) throws Exception {
        //第一步
        Class<?> viewName = Class.forName("android.view.View");
        Method getListenerInfo = viewName.getDeclaredMethod("getListenerInfo");
        getListenerInfo.setAccessible(true);
        Object listenerInfo = getListenerInfo.invoke(view);//这个listenerInfo就是ListenerInfo
        //第二步 获取成员变量是用get  设置是用set
        Class<?> listenerInfoClass = Class.forName("android.view.View$ListenerInfo");
        Field onClickListenerField = listenerInfoClass.getDeclaredField("mOnClickListener");
        Object mOnClickListener = onClickListenerField.get(listenerInfo);//这就获取到了,

        Object proxyInstance = Proxy.newProxyInstance(getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                TextView textView = new TextView(InvokeOnClickActivity.this);
                textView.setText("fsdfsdfsdfsdf");
                Toast.makeText(InvokeOnClickActivity.this, "已经hokd到方法了", Toast.LENGTH_SHORT).show();
                return method.invoke(mOnClickListener, textView);
            }
        });
        //这里是将我们自己的替换掉原来的
        onClickListenerField.set(listenerInfo, proxyInstance);
    }
}
