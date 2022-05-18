package com.ancely.fyw.usercenter.debug;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ancely.fyw.usercenter.R;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public class Usercenter_Debug_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter_debug);
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

            }
        }, BackpressureStrategy.BUFFER).
                onBackpressureBuffer().
                subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        fold(new ArrayList(), new Object(), new Function2() {
            @Override
            public Object invoke(Object o, Object o2) {
                return null;
            }
        });
    }

    public final Object fold(@NotNull Collection $this$fold, Object initial, @NotNull Function2 combine) {
        Intrinsics.checkNotNullParameter($this$fold, "$this$fold");
        Intrinsics.checkNotNullParameter(combine, "combine");
        Object accumulator = initial;

        Object element;
        for (Iterator var6 = $this$fold.iterator(); var6.hasNext(); accumulator = combine.invoke(accumulator, element)) {
            element = var6.next();
        }

        String ddd = fold1(new ArrayList<>(), "11", (Function2<String, String, String>) (s, s2) -> s + s2);
        return accumulator;

    }


    public final <T, R1> R1 fold1(Collection<T> collection, R1 initial, Function2<R1, T, R1> combine) {
        R1 accumulator = initial;
        for (T element : collection) {
            accumulator = combine.invoke(accumulator, element);
        }
        return accumulator;
    }
}
