package com.ancely.fyw.aroute.networks.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {
    private final int maxRetries;
    private int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) {
        return observable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {

            if (++retryCount <= maxRetries) {
//                    Log.e("Presenter","重启了 "+ retryCount+"次");
                retryDelayMillis += 1000;
                return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
            }
            return Observable.error(throwable);
        });
    }
}
