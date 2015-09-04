package com.xuxu.datatool.adpter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andexert.library.RippleView;
import com.nostra13.universalimageloader.core.*;
import com.xuxu.datatool.Activity.WebViewActivity;
import com.xuxu.datatool.Model.GeekNews;
import com.xuxu.datatool.R;
import com.xuxu.datatool.data.DataBasetool;
import com.xuxu.datatool.utils.GetData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/20.
 */
public class RecyclerAdapterGeek extends RecyclerView.Adapter<RecyclerAdapterGeek.JKViewHolder> {
    private Context context;
    private List<GeekNews> data;
    private View view;
    private String getUrl;
    private LayoutInflater inflater;
    private int mBackground;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private DisplayImageOptions options;

    private MaterialDialog noNetWorkDialog;
    private final TypedValue mTypedValue = new TypedValue();
    private OnTapListener onTapListener;
    private int lastPosition=-1;
    public void setOnTapListener(OnTapListener onTapListener)
    {
        this.onTapListener = onTapListener;
    }
    public RecyclerAdapterGeek(Context context, List<GeekNews> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(this.context);
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
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
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(JKViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.rippleView!=null){
          holder.rippleView.clearAnimation();
        }
    }

    @Override
    public JKViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            view = inflater.inflate(R.layout.geek_item_title, parent, false);
        } else {
            view = inflater.inflate(R.layout.geek_new_item, parent, false);
        }
        view.setBackgroundResource(mBackground);
        return new JKViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JKViewHolder holder, final int position) {
        if(holder.rippleView!=null){
            setAnimation(holder.rippleView,position);
        }
//        Log.e("holder", holder + "");
//        Log.e("绑定view更新UI", data.get(position).getJk_title());
        holder.geek_title.setText(data.get(position).getJk_title());
        if (data.get(position).getJk_time() != null) {
            holder.geek_time.setText(data.get(position).getJk_time());
            holder.geek_label.setText(data.get(position).getJk_label());
            if(!data.get(position).getJk_userName().equals("")){
                holder.user.setText(data.get(position).getJk_userName());
            }

            imageLoader.displayImage(data.get(position).getJk_userNameImg(), holder.user_img, options);
        }
        holder.itemView.setTag(data.get(position).getJk_title());
//        holder.cnews_details_title.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (onRecyclerLongClickListener!=null) {
//                    onRecyclerLongClickListener.onRecyclerLongClick(view,data.get(position).get("cnews_details_title").toString());
//                }
//                return true;
//            }
//        });
//        holder.geek_title.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(final View view) {
//                Log.e("自定义长按监听","自定义长按监听");
//                Log.e("回调接口",mOnRecyclerLongClickListener+"");
//                /*
//                * 思考如何动态获取position*/
////                if (mOnRecyclerLongClickListener!=null) {
////                    Log.e("回调进行中","回调进行中");
////                    mOnRecyclerLongClickListener.onRecyclerLongClick(view,data.get(position).get("cnews_details_title").toString());
////                }
//
//                Toast.makeText(context, "别按我了", Toast.LENGTH_SHORT).show();
//                if (noNetWorkDialog == null) {
//                    noNetWorkDialog = new MaterialDialog.Builder(context)
//                            .title("删除选项")
//                            .content("是否删除?")
//                            .positiveText("是")
//                            .negativeText("否")
//                            .callback(new MaterialDialog.ButtonCallback() {
//                                @Override
//                                public void onPositive(MaterialDialog dialog) {
//                                    Log.e("删除", data.get(position).get("cnews_details_title").toString());
//                                    Log.e("删除", holder.title.getText().toString());
//                                    dbt.delete(data.get(position).get("cnews_details_title").toString());
//                                    //dbt需要实例，否则报空值异常
//                                    data = dbt.selectDBAll();
//                                    notifyDataSetChanged();//添加此句实现实现实时删除ITEM
//                                }
//
//                                @Override
//                                public void onNegative(MaterialDialog dialog) {
//                                }
//                            })
//                            .cancelable(false)
//                            .build();
//                }
//                if (!noNetWorkDialog.isShowing()) {
//                    noNetWorkDialog.show();
//                }
//                return true;
//            }
//        });
//        holder.geek_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, WebViewActivity.class);
//
//                Bundle bundle = new Bundle();
//                bundle.putString("url", data.get(position).getJk_url());
//                bundle.putString("getTitle", data.get(position).getJk_title());
//                intent.putExtras(bundle);
//                //获取网址错误或不规范（不是http开头）
//                // 导致无法启动
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getJk_time() != null) {
            return 0;
        } else {
            return 1;
        }
    }

    class JKViewHolder extends RecyclerView.ViewHolder {
        public TextView geek_title;
        public TextView geek_label;
        public TextView geek_time;
        public TextView user;
        public ImageView user_img;

        public RippleView rippleView;

        public JKViewHolder(View itemView) {
            super(itemView);
            rippleView = (RippleView) itemView.findViewById(R.id.more);
            geek_title = (TextView) itemView.findViewById(R.id.geek_title);
            geek_label = (TextView) itemView.findViewById(R.id.geek_label);
            geek_time = (TextView) itemView.findViewById(R.id.geek_time);
            user = (TextView) itemView.findViewById(R.id.userName);
            user_img = (ImageView) itemView.findViewById(R.id.user_img);
//            rippleView = (RippleView) itemView.findViewById(R.id.more);
//            rippleView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onTapListener.onTapView(getPosition());
//                    Log.e("所点击位置", getPosition() + String.valueOf(getOldPosition()));
//                }
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    onTapListener.onTapView(getPosition());
//                    Log.e("所点击位置", getPosition() + String.valueOf(getOldPosition()));
                    Message message = new Message();
                    message.what = getPosition();
                    handler.sendMessageDelayed(message, 300);
                }
            });
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what>-1) {
                Intent intent = new Intent(context, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", data.get(msg.what).getJk_url());
                bundle.putString("getTitle", data.get(msg.what).getJk_title());
                intent.putExtras(bundle);
                //获取网址错误或不规范（不是http开头）
                // 导致无法启动
                context.startActivity(intent);
            }
        }
    };
}
