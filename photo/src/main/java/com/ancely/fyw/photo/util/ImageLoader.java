package com.ancely.fyw.photo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/*
 *  @项目名：  PhotoFile
 *  @包名：    com.pick.photo.util
 *  @文件名:   ImageLoader
 *  @创建者:   fanlelong
 *  @创建时间:  2018/12/13 上午10:28
 *  @描述：    加载图片类
 */
public class ImageLoader {
    private static ImageLoader mInstance;

    private LruCache<String, Bitmap> mLruCache;//图片缓存核心对象
    private ExecutorService mThreadPools; //线程池
    public static final int DEFAULT_THREAD_POOL = 1; //默认线程数
    private Type mType = Type.LIFO;//队列的调度方式
    private LinkedList<Runnable> mTaskQueue;
    private Thread mPoolThread;//后台轮询线程
    private Handler mPoolThreadHandler;//线程中的handler
    private Handler mUiHandler; // 主线程的Handler

    private Semaphore mPoolThreadHandlerSemaphore = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;
    private boolean isScroll;

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public enum Type {
        FIFO, LIFO
    }

    private ImageLoader(int threadCound, Type type) {
        init(threadCound, type);
    }

    /**
     * 初始化
     */
    private void init(int threadCound, Type type) {
        //后台的一个轮询线程
        HandlerThread handlerThread = new HandlerThread("photo-list: ") {
            @Override
            protected void onLooperPrepared() {
                mPoolThreadHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        //线程池去取出一个任务执行
                        mThreadPools.execute(getTask());
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });

                mPoolThreadHandlerSemaphore.release();
            }
        };
        handlerThread.start();
//        mPoolThread = new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                mPoolThreadHandler = new Handler(new Handler.Callback() {
//                    @Override
//                    public boolean handleMessage(Message message) {
//                        //线程池去取出一个任务执行
//                        mThreadPools.execute(getTask());
//                        try {
//                            mSemaphoreThreadPool.acquire();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        return false;
//                    }
//                });
//                mPoolThreadHandlerSemaphore.release();// 证明 mPoolThreadHandler初始化完毕  释放一个信号量
//                Looper.loop();
//            }
//        };
//        mPoolThread.start();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight(); //每一行的字节数* 图片高度
            }
        };
        mThreadPools = Executors.newFixedThreadPool(threadCound);
        mTaskQueue = new LinkedList<>();
        mType = type;
        mSemaphoreThreadPool = new Semaphore(threadCound);
    }

    /**
     * 从任务队列取方法
     */
    private synchronized Runnable getTask() {
        if (mType == Type.FIFO) {
            return mTaskQueue.removeFirst();
        } else if (mType == Type.LIFO) {
            return mTaskQueue.removeLast();
        }
        return null;
    }

    public void clearTaskQueue() {
        mTaskQueue.clear();
    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(DEFAULT_THREAD_POOL, Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    public static ImageLoader getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }


    public ImageLoader loadImage(final String filePath, final ImageView imageView, final boolean isBigImg) {
        imageView.setTag(filePath);
        if (mUiHandler == null) {
            mUiHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    //获取得到的图片,为imageView设置bitmap
                    ImageBeanHolder holder = (ImageBeanHolder) message.obj;
                    Bitmap bitmap = holder.bitmap;
                    String filePath = holder.filePath;
                    ImageView imageView = holder.imageView;
                    if (imageView.getTag().toString().equals(filePath)) {
                        imageView.setImageBitmap(bitmap);
                    }
                    return false;
                }
            });
        }
        if (!new File(filePath).exists()) {
            refreashBitmap(null, "-1", imageView);
            return this;
        }
        Bitmap bm = getBitmapFromLrucache(isBigImg ? (filePath + "BIG") : filePath);
        if (bm != null) {
            refreashBitmap(bm, filePath, imageView);
        } else {
            addTask(new Runnable() {
                @Override
                public void run() {
                    //获取图片要显示的大小
                    ImageSize imageViewSize = getImageViewSize(imageView);
                    //通过宽高压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(filePath, imageViewSize.width, imageViewSize.height);
                    addBitmapToLrucache(isBigImg ? (filePath + "BIG") : filePath, bm);
                    //加载图片
                    refreashBitmap(bm, filePath, imageView);
                    mSemaphoreThreadPool.release();
                }
            });
        }
        return this;
    }

    public ImageLoader loadImage(String filePath, ImageView imageView) {
        return this.loadImage(filePath, imageView, false);
    }

    private void refreashBitmap(Bitmap bm, String filePath, ImageView imageView) {
        Message message = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.bitmap = bm;
        holder.filePath = filePath;
        holder.imageView = imageView;
        message.obj = holder;
        mUiHandler.sendMessage(message);
    }

    /**
     * 将压缩的bitmap加入到缓存
     *
     * @param filePath 路径
     * @param bm       图片
     */
    private void addBitmapToLrucache(String filePath, Bitmap bm) {
        if (getBitmapFromLrucache(filePath) == null) {
            if (bm != null) {
                mLruCache.put(filePath, bm);
            }
        }
    }

    private Bitmap decodeSampledBitmapFromPath(String filePath, int width, int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//获取图片的宽和高,并不让其加载到内存中
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = caculateInSampleSize(options, width, height);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }


    /**
     * 通过imageview获取到其的宽高
     */
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        int screenWidth = imageView.getContext().getResources().getDisplayMetrics().widthPixels;
        int screenHeight = imageView.getContext().getResources().getDisplayMetrics().heightPixels;
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        int width = imageView.getWidth();
        if (width <= 0) {
            width = lp.width;
        }

        if (width <= 0) {
            width = imageView.getMaxWidth();
        }
        if (width <= 0) {
            width = screenWidth;
        }


        int height = imageView.getHeight();
        if (height <= 0) {
            height = lp.height;
        }

        if (height <= 0) {
            height = imageView.getMaxHeight();
        }
        if (width <= 0) {
            height = screenHeight;
        }
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    private boolean requestBitmapFlag = true;

    public void requestBitmapFlag(boolean requestFlag) {
        requestBitmapFlag = requestFlag;
    }

    /**
     * 需要同步 避免多次进行了acquire() 导致死锁
     */
    private synchronized void addTask(Runnable runnable) {
        if (!requestBitmapFlag) {
            return;
        }
        mTaskQueue.add(runnable);
        try {
            if (mPoolThreadHandler == null) {
                mPoolThreadHandlerSemaphore.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    /**
     * 根据filePaht从缓存中获取bitmap
     */
    private Bitmap getBitmapFromLrucache(String filePath) {
        return mLruCache.get(filePath);
    }

    private class ImageBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String filePath;
    }

    private class ImageSize {
        int width;
        int height;
    }
}
