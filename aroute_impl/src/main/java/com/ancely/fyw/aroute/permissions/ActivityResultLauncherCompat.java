package com.ancely.fyw.aroute.permissions;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.aroute.permissions
 *  @文件名:   ActivityResultLauncherCompat
 *  @创建者:   admin
 *  @创建时间:  2022/5/16 10:15
 *  @描述：    TODO
 */

public class ActivityResultLauncherCompat<I, O>{}
//implements LifecycleObserver, ActivityResultCallback<O> {
//
//    private ActivityResultCaller caller;
//    private ActivityResultContract<I, O> contract;
//    private ActivityResultRegistry registry;
//    private LifecycleOwner lifecycleOwner;
//
//    private ActivityResultLauncher<I> activityResultLauncher = null;
//    private ActivityResultCallback<O> activityResultCallback = null;
//
//    /**
//     * Called when result is available
//     *
//     * @param result
//     */
//    @Override
//    public void onActivityResult(O result) {
//        activityResultCallback.onActivityResult(result);
//    }
//
//    public ActivityResultLauncherCompat(ActivityResultCaller caller, ActivityResultContract<I, O> contract, ActivityResultRegistry registry, LifecycleOwner lifecycleOwner) {
//        this.caller = caller;
//        this.contract = contract;
//        this.registry = registry;
//        this.lifecycleOwner = lifecycleOwner;
//        lifecycleOwner.getLifecycle().addObserver(this);
//    }
//
//    public ActivityResultLauncherCompat(FragmentActivity activity, ActivityResultContract<I, O> contract) {
//        this(activity, contract, activity);
//    }
//
//    public ActivityResultLauncherCompat(ActivityResultCaller caller, ActivityResultContract<I, O> contract, LifecycleOwner lifecycleOwner) {
//        this(caller, contract, null, lifecycleOwner);
//    }
//
//    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
//    public void onCreate(LifecycleOwner owner) {
//        if (registry == null) {
//            activityResultLauncher = caller.registerForActivityResult(contract, this);
//        } else {
//            activityResultLauncher = caller.registerForActivityResult(contract, registry, this);
//        }
//    }
//
//
//    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
//    public void onStart(LifecycleOwner owner) {
//        if (activityResultLauncher == null) {
//            throw new IllegalStateException("ActivityResultLauncherCompat must initialize before they are STARTED.");
//        }
//    }
//
//    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
//    public void onDestroy(LifecycleOwner owner) {
//        lifecycleOwner.getLifecycle().removeObserver(this);
//    }
//
//    public final void launch(I input, ActivityResultCallback<O> callback) {
//        this.launch(input, null, callback);
//    }
//
//    public final void launch(I input, @Nullable ActivityOptionsCompat options, ActivityResultCallback<O> callback) {
//        this.activityResultCallback = callback;
//        activityResultLauncher.launch(input, options);
//    }
//}
