package com.ancely.fyw.aroute.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.ancely.fyw.aroute.core.IApplication;
import com.ancely.fyw.aroute.life.LifeManagerRetriever;
import com.ancely.fyw.aroute.networks.NetChangeImpl;
import com.ancely.fyw.aroute.networks.NetworkCallbackImpl;
import com.ancely.fyw.aroute.networks.cookie.CookieManger;
import com.ancely.fyw.aroute.networks.okhttp.HttpLoggingInterceptor;
import com.ancely.fyw.aroute.networks.okhttp.HttpsSSL;
import com.ancely.fyw.aroute.networks.okhttp.ProgressInterceptor;
import com.ancely.fyw.aroute.networks.receiver.NetWorkConnectReceiver;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Zaifeng on 2018/2/28.
 * API初始化类
 */

public class NetWorkManager {

    @SuppressLint("StaticFieldLeak")
    private static volatile NetWorkManager mInstance;
    private Retrofit mRetrofit;
    private OkHttpClient mClient;
    private Headers mHeaders;
    private LinkedHashMap<String, String> heardsMap;
    private Context mApplication;
    private HttpsSSL.SSLParams sslParams;
    private final LifeManagerRetriever lifeManagerRetriever;
    private Map<String, Retrofit> mRetrofitMap = new HashMap<>();

    private final NetWorkConnectReceiver mReceiver;
    private final NetChangeImpl mNetChange;

    private NetWorkManager(LifeManagerRetriever retriever) {
        this.lifeManagerRetriever = retriever;
        mNetChange = new NetChangeImpl();
        mReceiver = new NetWorkConnectReceiver(mNetChange);
    }

    public static NetWorkManager getInstance() { //DCL单例,优化懒汉式单例

//        return SinglerNetWorkManager.sInstance;//静态内部类单例
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    initializeNetWorkManager();
                }
            }
        }
        return mInstance;

    }

//    //静态内部类单例
//    private static class SinglerNetWorkManager {
//        public static final NetWorkManager sInstance = initializeNetWorkManager();
//    }

    private static void initializeNetWorkManager() {
        LifeManagerRetriever retriever = new LifeManagerRetriever();

        //JVM虚拟机: 首先:会为 mInstance分配内存,第二:会调用NetWorkManager的构造方法来初始化变量,第三:将mInstance这个对象指向JVM分配的内存空间
        mInstance = new NetWorkManager(retriever);//这么代码不会以产面三步执行 解决方法mInstance用volatile修饰 禁止JVM指令重排序优化
    }

    /**
     * 初始化必要对象和参数
     *
     * @param host 域名
     */
    public void init(String host, Context context) {
        init(host, null, context);
        initEvent(context);
    }

    public void initEvent(Context context){
        List<String> applicationList = Applications.getApplicationPackageName();
        for (String applicationAbsolutePath : applicationList) {
            if (applicationAbsolutePath.equals(context.getClass().getName())) {
                if (context instanceof IApplication) {
                    ((IApplication) context).addEventBean();
                }
                continue;
            }
            try {
                Class<?> aClass = Class.forName(applicationAbsolutePath);
                Object application = aClass.newInstance();
                if (application instanceof IApplication) {
                    ((IApplication) application).attach(context);
                    ((IApplication) application).addEventBean();
                    Applications.addApplication((IApplication) application);
                }
            }catch (Exception ignored){
                Log.e("ancely>>>", "applicationAbsolutePath not register, Please Applications register");
            }
        }
        applicationList.clear();
    }

    public Context getContext() {
        return mApplication;
    }

    public void init(@NonNull String host, List<Interceptor> interceptors, Context context) {
//        if (AncelyContentProvider.context == null) {
//            throw new IllegalArgumentException("Context must not be null.");
//        }
//        mApplication = AncelyContentProvider.context.getApplicationContext();
        mApplication = context;
        //初始化网络监听
        initNetChangerListener(mApplication);
        PluginManager.init(mApplication);

        initOkhttpClient(interceptors);
        // 初始化Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(mClient)
                .baseUrl(host)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitMap.put(host, mRetrofit);
    }

    private void initOkhttpClient(List<Interceptor> interceptors) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor("ancelyOkhttp:");
        httpLoggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        httpLoggingInterceptor.setColorLevel(Level.INFO);
        // 初始化okhttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor())
                .addInterceptor(chain -> {
                    //默认添加 Accept-Language
                    //默认添加 User-Agent
                    Request.Builder builder1 = chain.request().newBuilder();
                    addHeards(builder1);
                    Request request = builder1
                            .addHeader("Content-type", "application/json; charset=utf-8")
                            .addHeader("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .connectTimeout(60000, TimeUnit.MILLISECONDS);
        if (interceptors != null && interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                if (interceptor != null) builder.addInterceptor(interceptor);
            }
        }
        builder.cookieJar(new CookieManger(getContext()))
                .retryOnConnectionFailure(true);
        if (sslParams != null)
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);

        mClient = builder.build();
    }

    /**
     * 初始化网络改变监听
     */
    private void initNetChangerListener(Context application) {
        //通过广播方式来操作
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        //不通过广播来监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl(mNetChange);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                cm.registerNetworkCallback(request, networkCallback);
            }
        } else {
            mApplication.registerReceiver(mReceiver, filter);
        }
    }


    /**
     * 注册网络监听
     */
    public void registerObserver(Object object) {
        mNetChange.registerObserver(object);
    }

    /**
     * 反注册网络监听
     */
    public void unRegisterObserver(Object object) {
        mNetChange.unRegisterObserver(object);
    }

    /**
     * 取消所有
     */
    public void unRegisterAllObserver() {
        mNetChange.unRegisterAllObserver();

    }

    public OkHttpClient getClient() {
        return mClient;
    }

    public NetWorkManager sslSocketFactory(String sslSocket) {
        if (!TextUtils.isEmpty(sslSocket)) {
            sslParams = HttpsSSL.getSslSocketFactory
                    (new InputStream[]{new Buffer().writeUtf8(sslSocket).inputStream()}, null, null);
        }
        return this;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * 处理多host
     */
    public Retrofit getRetrofit(String host) {
        Retrofit retrofit = mRetrofitMap.get(host);
        if (retrofit == null) {
            Retrofit r = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(host)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mRetrofitMap.put(host, r);
            return r;
        }
        return retrofit;
    }


    /**
     * 添加全局请求头
     */
    public NetWorkManager addHeards(Headers headers) {
        mHeaders = headers;
        return this;
    }

    /**
     * 添加全局请求头
     */
    public NetWorkManager addheard(String key, String value) {
        if (heardsMap == null) {
            heardsMap = new LinkedHashMap<>();
        }
        heardsMap.put(key, value);
        return this;
    }

    private void addHeards(Request.Builder builder) {
        if (mHeaders != null) {
            builder.headers(mHeaders);
        }
        if (heardsMap != null) {
            for (String s : heardsMap.keySet()) {
                String value = heardsMap.get(s);
                if (value != null) {
                    builder.addHeader(s, value);
                }
            }
        }
    }

    @NonNull
    public LifeManagerRetriever getRequestManagerRetriever() {
        return lifeManagerRetriever;
    }

    private boolean mIsLogin;

    public void setIsLogin(boolean isLogin) {
        mIsLogin = isLogin;
    }

    public boolean isLogin() {
        return mIsLogin;
    }
}