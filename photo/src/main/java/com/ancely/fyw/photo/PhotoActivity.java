package com.ancely.fyw.photo;

import android.Manifest;
import android.app.SharedElementCallback;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ancely.fyw.aroute.eventbus.EventBus;
import com.ancely.fyw.aroute.model.ModelP;
import com.ancely.fyw.aroute.model.bean.ResponseBean;
import com.ancely.fyw.common.base.BaseModelActivity;
import com.ancely.fyw.photo.bean.FloderBean;
import com.ancely.fyw.photo.bean.UpdataViewEvent;
import com.ancely.fyw.photo.event.EventBusIndex;
import com.ancely.fyw.photo.util.GridSpacingItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.Subscribe;

@ARouter(path = "/photo/PhotoActivity")
public class PhotoActivity extends BaseModelActivity implements PhotoAdapter.OnItemClickListener {

    private RecyclerView mRv;
    private RelativeLayout mBottomRl;
    private TextView mDirName;
    private TextView mDirCount;

    public static List<String> mImgs = new ArrayList<>(100);
    private List<FloderBean> mFloderBeans = new ArrayList<>();
    private PhotoAdapter mPhotoAdapter;
    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED,       //图片被添加的时间，long型  1450518608
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,       //图片被添加的时间，long型  1450518608d
            MediaStore.Images.Media.BUCKET_ID,       //图片被添加的时间，long型  1450518608d


    };
    private Handler mHandler = new Handler(new Handler.Callback() {


        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 0x100) {
                mImgs = (ArrayList<String>) message.obj;
                if (mImgs == null || mImgs.size() == 0) {
                    Toast.makeText(PhotoActivity.this, "未扫描到任务图片", Toast.LENGTH_SHORT).show();
                    return false;
                }
                mDirPath = "";

                mPhotoAdapter = new PhotoAdapter(PhotoActivity.this, mImgs);
                mPhotoAdapter.setOnItemClickListener(PhotoActivity.this);
                mRv.setAdapter(mPhotoAdapter);
                mDirCount.setText(String.valueOf(getString(R.string.ip_folder_image_count, mImgs.size())));
            }
            return false;
        }
    });
    private ImageFolderAdapter mImageFolderAdapter;
    private FolderPopUpWindow mFolderPopupWindow;
    private GridLayoutManager mGridLayoutManager;
    //    private ImageLoader mInstance;
    private String mDirPath;

    @Override
    public ModelP getModelP() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().addIndex(new EventBusIndex());
        super.onCreate(savedInstanceState);
        mImgs = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        mRv = findViewById(R.id.rv);
        mBottomRl = findViewById(R.id.bottom_rl);
        mDirName = findViewById(R.id.dir_name);
        mDirCount = findViewById(R.id.dir_count);
        mGridLayoutManager = new GridLayoutManager(this, 4);
        mRv.setLayoutManager(mGridLayoutManager);
        mRv.addItemDecoration(new GridSpacingItemDecoration(4, dp2px(this, 1), false));

        mRv.setHasFixedSize(true);
        mImageFolderAdapter = new ImageFolderAdapter(this, null);
        mBottomRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRv.stopScroll();
                createPopupFolderList();
                mImageFolderAdapter.refreshData(mFloderBeans);  //刷新数据
                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.showAtLocation(mBottomRl, Gravity.NO_GRAVITY, 0, 0);
                    //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
                    int index = mImageFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.setSelection(index);
                }
            }
        });


        //扫描手机的所有图片
//        initDatas();


    }

    private String storaged = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public void initDatas() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用! ", Toast.LENGTH_SHORT).show();
            return;
        }
//        new ImageDataSource(this, this);
        new Thread() {
            @Override
            public void run() {
                List<String> mImgs = new ArrayList<>();
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = PhotoActivity.this.getContentResolver();
//                Cursor cursor = contentResolver.query(mImgUri, null,
//                        MediaStore.Images.Media.MIME_TYPE + " =? or " + MediaStore.Images.Media.MIME_TYPE + " =? ",
//                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
                assert cursor != null;
                Map<String, FloderBean> parentFiles = new HashMap<>();
                long start=System.currentTimeMillis();
                while (cursor.moveToNext()) {

                    String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long time = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                    if (!path.contains(storaged)) {
                        continue;
                    }
                    boolean exists = fileExists(path);
                    if (!exists) {
                        continue;
                    }
                    File parentPath = new File(path).getParentFile();
                    if (parentPath == null) {
                        continue;
                    }
                    if (mImgs.contains(path)) {
                        continue;
                    }
//                    sPhotoBeans.add(new PhotoBean(path, time));
                    mImgs.add(path);
                    String dirPath = parentPath.getAbsolutePath();


                    if (parentFiles.containsKey(dirPath)) {
                        FloderBean bean = parentFiles.get(dirPath);
                        bean.imags.add(name);
                    } else {
                        FloderBean bean = new FloderBean();
                        bean.imags = new ArrayList<>();
                        bean.firstImg = name;
                        bean.imags.add(name);
                        bean.setDir(dirPath);
                        parentFiles.put(dirPath, bean);
                        mFloderBeans.add(bean);
                        bean.parentName = parentPath.getName();
                    }
                }
                cursor.close();
//                mImgs.clear();
//                for (PhotoBean photoBean : sPhotoBeans) {
//                    mImgs.add(photoBean.path);
//                }
                Log.e("ancely>>> ",String.valueOf(System.currentTimeMillis()-start));
                Message message = mHandler.obtainMessage();
                message.obj = mImgs;
                message.what = 0x100;
                mHandler.sendMessage(message);//通知更新ui
            }
        }.start();

        if (Build.VERSION.SDK_INT >= 22) {
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (bundle != null) {
                        int i = bundle.getInt("index", 0);
                        sharedElements.clear();
                        names.clear();
                        View itemView = mGridLayoutManager.findViewByPosition(i);
                        if (itemView != null) {
                            ImageView imageView = itemView.findViewById(R.id.item_iv);
                            //注意这里第二个参数，如果放置的是条目的item则动画不自然。放置对应的imageView则完美
                            sharedElements.put(String.valueOf(i), imageView);
                        } else {

                            sharedElements.put(String.valueOf(i), (View) findViewById(R.id.item_view));
                        }
                        bundle = null;

                    }
                }

                @Override
                public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
                    //sharedElement 本页面指定共享元素动画的view
                    Log.d("test exit a", "onCaptureSharedElementSnapshot");
                    //以下代码已经没必要设置，因为demo中的动画效果已经全部设置在了rv_item_fake_iv上
                    //解决执行共享元素动画的时候，一开始显示空白的问题
                    sharedElement.setAlpha(1f);
                    return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
                }
            });
        }
    }

    private boolean fileExists(String path) {

        return new File(path).exists();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_photo;
    }

    private Bundle bundle;

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        bundle = new Bundle(data.getExtras());
    }


    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        mFolderPopupWindow = new FolderPopUpWindow(this, mImageFolderAdapter);
        mFolderPopupWindow.setOnItemClickListener(new FolderPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mImageFolderAdapter.setSelectIndex(position);
                mFolderPopupWindow.dismiss();
                mImgs.clear();
                mImgs.addAll(mFloderBeans.get(position).imags);
                String name = mFloderBeans.get(position).getName();
                mDirName.setText(mFloderBeans.get(position).parentName);
                mDirCount.setText(String.valueOf(getString(R.string.ip_folder_image_count, mImgs.size())));
                mPhotoAdapter.setDirPath(name);
                mDirPath = name;
                mPhotoAdapter.setDatas(mImgs);
                mRv.scrollToPosition(0);
                mPhotoAdapter.notifyDataSetChanged();
            }
        });
        mFolderPopupWindow.setMargin(mBottomRl.getHeight());
    }

    private View view;

    @Override
    public void onItemClickListener(View view, String path, int position) {

        Intent intent = new Intent(this, PhotoItemActivity.class);
        intent.putExtra("photoPath", path);
        intent.putExtra("position", position);
        intent.putExtra("dirPath", mDirPath);
        intent.putExtra("index", position);
        this.view = view;
        if (Build.VERSION.SDK_INT >= 22) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, view, String.valueOf(position));// mAdapter.get(position).getUrl()
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImgs.clear();
        mImgs = null;
    }

    @Subscribe
    public void updataView(UpdataViewEvent event) {
        if (view != null) {
            view.setAlpha(1.f);
        }
    }

    @Override
    public boolean openChangerSkin() {
        return super.openChangerSkin();
    }

    @Override
    public void accessSuccess(ResponseBean responseBean) {

    }

    @Override
    public boolean isNeedCheckNetWork() {
        return false;
    }

    @Override
    public boolean openEventBus() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

}






















































