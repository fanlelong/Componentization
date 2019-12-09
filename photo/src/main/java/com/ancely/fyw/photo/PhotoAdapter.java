package com.ancely.fyw.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/*
 *  @项目名：  PhotoFile
 *  @包名：    com.pick.photo
 *  @文件名:   PhotoAdapter
 *  @创建者:   fanlelong
 *  @创建时间:  2018/12/13 下午1:57
 *  @描述：    TODO
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mDatas;
    private String mDirPath;
    private LayoutInflater mLayoutInflater;

    public void setDirPath(String dirPath) {
        mDirPath = dirPath;
    }

    public PhotoAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
        mDirPath = "";
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<String> datas) {
        mDatas = datas;
    }

    @NonNull
    @Override
    public PhotoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mLayoutInflater.inflate(R.layout.item_photo, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.MyViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        viewHolder.mImageView.setImageResource(R.drawable.bg_photo);


//        Object tag = viewHolder.mImageView.getTag(R.id.item_iv);
//        if (tag != null && (int) tag != position) {
//            Glide.with(mContext).clear(viewHolder.mImageView);
//        }
//        viewHolder.mImageView.setTag(R.id.item_iv, position);
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClickListener(v, mDirPath + "/" + mDatas.get(position), position);
                }
            }
        });
        int threadCount = Runtime.getRuntime().availableProcessors();
        if (TextUtils.isEmpty(mDatas.get(position))) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bg_photo).centerCrop();
        Glide.with(mContext).load(mDirPath + "/" + mDatas.get(position)).apply(requestOptions).into(viewHolder.mImageView);
//        ImageLoader.getInstance(threadCount, ImageLoader.Type.LIFO).loadImage(mDirPath + "/" + mDatas.get(position), viewHolder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_iv);
        }
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }

    interface OnItemClickListener {
        void onItemClickListener(View view, String path, int position);
    }
}
