package com.xuxu.datatool.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuxu.datatool.Activity.MainActivity;
import com.xuxu.datatool.Model.CNewsDetails;
import com.xuxu.datatool.R;

import java.util.List;

/**
 * Created by Administrator on 2015/8/13.
 */
public class CNewsAdapter extends RecyclerView.Adapter<CNewsAdapter.CNViewHolder> {
    private Context context;
    private List<CNewsDetails> cNewsDetailsList;
    private LayoutInflater inflater;
    private View view;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private DisplayImageOptions options;

    public CNewsAdapter(Context context, List<CNewsDetails> cNewsDetailsList) {
        this.context = context;
        this.cNewsDetailsList = cNewsDetailsList;
        inflater = LayoutInflater.from(this.context);
        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        int loadingResource = R.drawable.img1;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(loadingResource)
                .build();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CNViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType >= 0) {
            switch (viewType) {
                case 0:
                    view = inflater.inflate(R.layout.cnews_details_title, parent, false);
                    return new CNViewHolder(view);
                case 1:
                    view = inflater.inflate(R.layout.cnews_details_details, parent, false);
                    return new CNViewHolder(view);
                case 2:
                    view = inflater.inflate(R.layout.cnews_details_content, parent, false);
                    return new CNViewHolder(view);
                case 3:
                    view = inflater.inflate(R.layout.cnews_details_image, parent, false);
                    return new CNViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(CNViewHolder holder, final int position) {

        CNewsDetails cNewsDetails = cNewsDetailsList.get(position);
        if (cNewsDetails != null) {
            switch (getItemViewType(position)) {
                case 0:
                    holder.textView_CNewsDetails.setText(cNewsDetails.getCNewsDetails_title());
//                    Log.e("设置title", cNewsDetails.getCNewsDetails_title());
                    break;
                case 1:
                    holder.textView_CNewsDetails.setText(cNewsDetails.getCNewsDetails_details());
//                    Log.e("设置details", cNewsDetails.getCNewsDetails_details());
                    break;
                case 2:
                    holder.textView_CNewsDetails.setText(cNewsDetails.getCNewsDetails_content());
//                    Log.e("设置content", cNewsDetails.getCNewsDetails_content());
                    break;
                case 3:
                    imageLoader.displayImage(cNewsDetails.getCNewsDetails_imageUrl(),
                            holder.imageView_CNewsDetails, options);
//                    Log.e("设置image", cNewsDetails.getCNewsDetails_imageUrl());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        CNewsDetails cNewsDetails = cNewsDetailsList.get(position);
        if (cNewsDetails != null) {
            switch (cNewsDetails.getCNewsDetails_type()) {
                case CNewsDetails.CNewsDetailsType.TITLE:
                    return 0;
                case CNewsDetails.CNewsDetailsType.DETAILS:
                    return 1;
                case CNewsDetails.CNewsDetailsType.CONTENT:
                    return 2;
                case CNewsDetails.CNewsDetailsType.IMG_URL:
                    return 3;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return cNewsDetailsList.size();
    }

    class CNViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_CNewsDetails;
        private TextView textView_CNewsDetails;

        public CNViewHolder(View itemView) {
            super(itemView);
            imageView_CNewsDetails = (ImageView) itemView.findViewById(R.id.image);
            textView_CNewsDetails = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
