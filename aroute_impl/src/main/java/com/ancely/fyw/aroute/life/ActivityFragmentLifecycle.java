package com.ancely.fyw.aroute.life;

/*
 *  @项目名：  BaseMvp
 *  @包名：    com.ancely.rxjava.mvp
 *  @文件名:   ActivityFragmentLifecycle
 *  @创建者:   fanlelong
 *  @创建时间:  2018/8/28 上午10:03
 */

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class ActivityFragmentLifecycle implements Lifecycle {

    private final Set<LifecycleListener> lifecycleListeners = Collections.newSetFromMap(new WeakHashMap<>());
    private boolean isStarted;
    private boolean isDestroyed;

    @Override
    public void addListener(@NonNull LifecycleListener listener) {
        lifecycleListeners.add(listener);

        if (isDestroyed) {
            listener.onDestroy();
        } else if (isStarted) {
            listener.onStart();
        } else {
            listener.onStop();
        }
    }

    @Override
    public void removeListener(@NonNull LifecycleListener listener) {

    }

    void onStart() {
        isStarted = true;
        for (LifecycleListener lifecycleListener : getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStart();
        }
    }

    void onStop() {
        isStarted = false;
        for (LifecycleListener lifecycleListener : getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStop();
        }
    }

    void onDestroy() {
        isDestroyed = true;
        for (LifecycleListener lifecycleListener : getSnapshot(lifecycleListeners)) {
            lifecycleListener.onDestroy();
        }
    }

    @NonNull
    @SuppressWarnings("UseBulkOperation")
    private static <T> List<T> getSnapshot(@NonNull Collection<T> other) {

        List<T> result = new ArrayList<>(other.size());
        for (T item : other) {
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }
}
