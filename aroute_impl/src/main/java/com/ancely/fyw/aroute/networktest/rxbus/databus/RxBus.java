package com.ancely.fyw.aroute.networktest.rxbus.databus;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.network.rxbus.databus
 *  @文件名:   RxBus
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/12 10:58 AM
 *  @描述：    数据分发
 */
public class RxBus {
    private final Set<Object> subscribers;
    private final Map<Class<?>, CompositeDisposable> disposeMaps;

    public synchronized void register(Object subscriber) {
        subscribers.add(subscriber);
        disposeMaps.put(subscriber.getClass(), new CompositeDisposable());
    }

    public synchronized void unRegister(Object subscriber) {
        subscribers.remove(subscriber);
        CompositeDisposable disposable = disposeMaps.remove(subscriber.getClass());
        if (disposable != null && disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private static volatile RxBus sInstance;

    private RxBus() {
        subscribers = new CopyOnWriteArraySet<>();//稳定,线程安全
        disposeMaps = new HashMap<>();
    }

    public static RxBus getInstance() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }


    public <T> void doProcessInvoke(Observable<T> observable, Class<?> clazz) {
        CompositeDisposable compositeDisposable = disposeMaps.get(clazz);
        Disposable disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        if (t != null) {
                            sendDataAction(t);
                        }
                    }
                });
        if (compositeDisposable != null) {
            compositeDisposable.add(disposable);
        }
    }

    private void sendDataAction(Object data) {
        //扫描注册进来的
        for (Object subscriber : subscribers) {
            checkAnnotation(subscriber, data);
        }
    }

    private void checkAnnotation(Object targetObject, Object data) {
        Method[] declaredMethods = targetObject.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.setAccessible(true);

            RigisterRxBus annotation = declaredMethod.getAnnotation(RigisterRxBus.class);
            if (annotation != null) {
                Class<?> parameterClass = declaredMethod.getParameterTypes()[0];
                if (data.getClass().getName().equals(parameterClass.getName())) {
                    try {
                        declaredMethod.invoke(targetObject, data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
