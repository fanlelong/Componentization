# Componentization

**组件化/Componentization**


## 不同模块跳转示例

*比如想从MainActivity跳转到Login_MainActivity。,代码如下*

```
@ARouter(path = "/login/Login_MainActivity")//login一定是模块名,不是则报错,/一定是二个
public class Login_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ParameterManager.getInstance().loadParameter(this);
        Log.e("componentization", name);
    }


    public void jumpToApp(View view) {
        //.build是跳转,.finish是有带ResponseCode的回调就是setResult
        //如果是.finish则withString("call","login_app")是传第二个界面返回第一个界面传的参数,则navigation(this,20)这个20则是ResponseCode
        //如果是.build,里面传的是要跳转Activity的path路径,就是@ARouter里面的path,withString("call","login_app")则是把参数带给第二个界面, navigation(this,20)则是RequestCode
        RouterManager.getInstance().finish(this).withString("call","login_app").navigation(this,20);
    }
}
```

## 插件化

*比如想跳转到sd卡里的apk,或都其它文件里的Activity或者Service*

*1.本app跳转到第三方apk*

```

    public void startActivitys(View view) {
    //这个是加载第三方apk的路径
        boolean isSuccess = PluginManager.getInstance().loadPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk");
        if (!isSuccess) {
            return;
        }
        Intent intent = new Intent(this, ProxyActivity.class);
        String serviceName = PluginManager.getInstance().getPackageInfo().activities[0].name;
        intent.putExtra("className", serviceName);
        startActivity(intent);
    }


```

*1.本第三方apkActivity启动第三方service*

```
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startService(new Intent(mActivity, TextService.class));
        }
    });

```