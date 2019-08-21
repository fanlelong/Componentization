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

    public void jumpToUsercenter(View view) {
        RouterManager.getInstance().build("/usercenter/UserCenter_MainActivity")
                .withResultString("name", "app_usercenter")
                .navigation(this, 10);
    }

}
```

##组件与组件之间如果传差

* 首先在公用基础module里面定义一个接口继承Call  *
```

public interface LoginCall extends Call {
    int getDrawable();
}


```
*  比如:如果想获取login模块里面的一个资源文件,就在login模块里面定义实现LoginCall的类,格式一定不能错,/model名/xxx *

```
@ARouter(path = "/login/getDrawable")
public class LoginDrawableImpl implements LoginCall {
    @Override
    public int getDrawable() {
        return R.drawable.login_ic_mouse_black_24dp;
    }
}

```

*  在app模块中如果获取login模块里的res 示例如下 *

```
    //这是一个成员变量 name = "/login/getDrawable  这玩意要跟上面的path = "/login/getDrawable"对应
    @Parameter(name = "/login/getDrawable")
    LoginCall mLoginCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //先要懒加载出来
        ParameterManager.getInstance().loadParameter(this);
        ImageView imageView = findViewById(R.id.act_main_iv);
        imageView.setImageResource(mLoginCall.getDrawable());
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

*2.第三方apk的Activity启动自己的service*

```
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startService(new Intent(mActivity, TextService.class));
        }
    });

```