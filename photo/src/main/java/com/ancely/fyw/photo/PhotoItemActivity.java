package com.ancely.fyw.photo;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ancely.fyw.aroute.base.BaseActivity;
import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.fyw.photo.bean.UpdataViewEvent;
import com.ancely.fyw.photo.photoview.PhotoView;
import com.ancely.fyw.photo.util.DragCloseHelper;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 *  @项目名：  PhotoFile
 *  @包名：    com.pick.photo
 *  @文件名:   PhotoActivity
 *  @创建者:   fanlelong
 *  @创建时间:  2018/12/14 下午5:02
 */
public class PhotoItemActivity extends BaseActivity {
    private RecyclerView mPhotoRv;
    private ConstraintLayout mActPhoneCll;


    private int index;
    private LinearLayoutManager mLinearLayoutManager;
    private DragCloseHelper mDragCloseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phont_item);

        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        mPhotoRv = findViewById(R.id.act_photo_rv);
        mActPhoneCll = findViewById(R.id.act_phone_cll);

        int position = Objects.requireNonNull(getIntent().getExtras()).getInt("position");
        index = position;
        final String dirPath = Objects.requireNonNull(getIntent().getExtras()).getString("dirPath");
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPhotoRv.setLayoutManager(mLinearLayoutManager);
//        final ImageLoader instance = ImageLoader.getInstance();
        new PagerSnapHelper().attachToRecyclerView(mPhotoRv);

        initDrawView();

        mPhotoRv.setAdapter(new RecyclerView.Adapter<PhotoHolder>() {
            @NonNull
            @Override
            public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(PhotoItemActivity.this).inflate(R.layout.item_rv_photo, parent, false);
                return new PhotoHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
//                instance.loadImage(dirPath + "/" + PhotoActivity.mImgs.get(position), holder.mImageView, true);
                Glide.with(holder.mImageView).load(dirPath + "/" + PhotoActivity.mImgs.get(position)).into(holder.mImageView);
            }

            @Override
            public int getItemCount() {
                return PhotoActivity.mImgs.size();
            }
        });
        mPhotoRv.scrollToPosition(position);
        mPhotoRv.addOnScrollListener(mScrollListener);
        if (Build.VERSION.SDK_INT >= 22) {
            //这个可以看做个管道  每次进入和退出的时候都会进行调用  进入的时候获取到前面传来的共享元素的信息
            //退出的时候 把这些信息传递给前面的activity
            //同时向sharedElements里面put view,跟对view添加transitionname作用一样
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    sharedElements.clear();
                    index = mLinearLayoutManager.findFirstVisibleItemPosition();
                    View viewByPosition = mLinearLayoutManager.findViewByPosition(index);
                    if (viewByPosition instanceof ConstraintLayout) {
                        View childAt = ((ConstraintLayout) viewByPosition).getChildAt(0);
                        sharedElements.put(String.valueOf(index), childAt);
                    }
                    Log.e("PhotoItemActivity", "onMapSharedElements");
                }
                @Override
                public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                    //sharedElements 是本页面共享元素的view   sharedElementSnapshots是本页面真正执行动画的view
                    Log.e("PhotoItemActivity", "onSharedElementStart");
                }

                @Override
                public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                    //sharedElements 是本页面共享元素的view   sharedElementSnapshots是本页面真正执行动画的view
                    Log.e("PhotoItemActivity", "onSharedElementEnd");
                }

                @Override
                public void onRejectSharedElements(List<View> rejectedSharedElements) {
                    super.onRejectSharedElements(rejectedSharedElements);
                    //屏蔽的view
                    Log.e("PhotoItemActivity", "onRejectSharedElements");
                }
                

//            @Override
//            public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
//                Log.e("test enter b", "onCaptureSharedElementSnapshot");
//                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
//            }

                @Override
                public View onCreateSnapshotView(Context context, Parcelable snapshot) {
                    //新的iv执行动画的真正iv
                    Log.e("PhotoItemActivity", "onCreateSnapshotView");
                    return super.onCreateSnapshotView(context, snapshot);
                }

                @Override
                public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
                    //sharedElements 是本页面共享元素的view
                    Log.e("PhotoItemActivity", "onSharedElementsArrived");
                    super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
                }
            });

        }
    }

    boolean scrolling;
    RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            scrolling = newState != 0;
        }
    };

    private void initDrawView() {
        //初始化拖拽返回
        mDragCloseHelper = new DragCloseHelper(this);
        mDragCloseHelper.setShareElementMode(true);
        mDragCloseHelper.setDragCloseViews(mActPhoneCll, mPhotoRv);
        mDragCloseHelper.setDragCloseListener(new DragCloseHelper.DragCloseListener() {
            @Override
            public boolean intercept() {
                //默认false 不拦截 如果图片是放大状态，或者处于滑动返回状态，需要拦截
                int index = mLinearLayoutManager.findFirstVisibleItemPosition();
                View viewByPosition = mLinearLayoutManager.findViewByPosition(index);
                if (viewByPosition instanceof ConstraintLayout) {
                    PhotoView photoView = (PhotoView) ((ConstraintLayout) viewByPosition).getChildAt(0);
                    return scrolling || photoView.getScale() != 1;
                }
                return false;
            }

            @Override
            public void dragStart() {
                EventBus.getDefault().post(new UpdataViewEvent());
            }

            @Override
            public void dragging(float percent) {
                //拖拽中。percent当前的进度，取值0-1，可以在此额外处理一些逻辑
            }

            @Override
            public void dragCancel() {
                //拖拽取消，会自动复原。可以在此额外处理一些逻辑
            }

            @Override
            public void dragClose(boolean isShareElementMode) {
                //拖拽关闭，如果是共享元素的页面，需要执行activity的onBackPressed方法，注意如果使用finish方法，则返回的时候没有共享元素的返回动画
                if (isShareElementMode) {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        index = mLinearLayoutManager.findFirstVisibleItemPosition();
        data.putExtra("index", index);
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }

    @TargetApi(22)
    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra("index", 0);
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        PhotoHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_iv);

        }
    }

    @Override

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mDragCloseHelper.handleEvent(event)) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }


//    private static void removeActivityFromTransitionManager(Activity activity) {
//        if (Build.VERSION.SDK_INT < 21) {
//            return;
//        }
//        Class transitionManagerClass = TransitionManager.class;
//        try {
//            Field runningTransitionsField = transitionManagerClass.getDeclaredField("sRunningTransitions");
//            runningTransitionsField.setAccessible(true);
//            //noinspection unchecked
//            ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> runningTransitions
//                    = (ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>>)
//                    runningTransitionsField.get(transitionManagerClass);
//            if (runningTransitions.get() == null || runningTransitions.get().get() == null) {
//                return;
//            }
//            ArrayMap map = runningTransitions.get().get();
//            View decorView = activity.getWindow().getDecorView();
//            map.remove(decorView);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDragCloseHelper.removeCallback();
        mPhotoRv.removeOnScrollListener(mScrollListener);

    }
}
