package com.xuxu.datatool.adpter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.xuxu.datatool.Activity.CQUPTNews;
import com.xuxu.datatool.Activity.WebViewActivity;
import com.xuxu.datatool.Model.CNews;
import com.xuxu.datatool.Model.NewsPicture;
import com.xuxu.datatool.R;
import com.xuxu.datatool.utils.GetData;

import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Administrator on 2015/8/6.
 */
public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.CViewHolder> {

    private List<CNews> cData;
    private Context context;
    private LayoutInflater inflater;
    private View view;
    private int mBackground;
    private final TypedValue mTypedValue = new TypedValue();
    private String getUrl;
    int position = 0;
    List<NewsPicture> newsPictures;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    public RecyclerAdapter2(Context context, List<CNews> cData) {
        this.cData = cData;
        this.context = context;
//        this.newsPictures = newsPictures;
        inflater = LayoutInflater.from(this.context);
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }
    private int lastPosition=-1;
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewAttachedToWindow(CViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.c_card.clearAnimation();
    }

    @Override
    public RecyclerAdapter2.CViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType==TYPE_HEADER) {
//            view = inflater.inflate(R.layout.header, parent, false);
//            view.setBackgroundResource(mBackground);
//            return new hViewHolder(view);
//        }else {
        view = inflater.inflate(R.layout.cnews_card_view, parent, false);
        view.setBackgroundResource(mBackground);
        return new CViewHolder(view);
//        }

    }

    @Override
    public void onBindViewHolder(RecyclerAdapter2.CViewHolder holder, final int position) {

//        if (holder instanceof hViewHolder) {
//
////            holder.viewPager.setAdapter(new CQUPTNews.BannerAdapter(context,newsPictures));
//        }else
        if (holder instanceof CViewHolder) {
            setAnimation(holder.c_card,position);
            holder.time_c.setText(cData.get(position).getcTime().toString());
            holder.textView_c.setText(cData.get(position).getcTitle().toString());
            holder.textView_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.e("uri", cData.get(position).getcUrl().toString());
                    //                Intent intent = new Intent(context,NewsContentActivity.class);
                    //                Bundle bundle = new Bundle();
                    //
                    //                bundle.putString("contentUrl", data.get(position).get("url").toString());
                    ////                intent.putExtra("content", data.get(position).get("url"));
                    //                intent.putExtras(bundle);
                    //                context.startActivity(intent);

                    //                final ProgressDialog pd = ProgressDialog.show(context,"connecting","正在连接" );
                    //                pd.dismiss();

                    Intent intent = new Intent(context, WebViewActivity.class);
                    if (getUrl != null) {
//                        Log.e("geturl", getUrl);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("getUrl", getUrl);
                    bundle.putString("url", cData.get(position).getcUrl().toString());
                    bundle.putString("getTitle", cData.get(position).getcTitle().toString());
                    intent.putExtras(bundle);

                    //获取网址错误或不规范（不是http开头）
                    // 导致无法启动

                    context.startActivity(intent);

                    //                new Thread(){
                    //                    @Override
                    //                    public void run() {
                    //                        Message msg=new Message();
                    //                        try {
                    //
                    //                            getUrl = GetData.getUrl(data.get(position).get("url").toString());
                    //                            msg.what=getUrl.length();
                    //                        } catch (Exception e) {
                    //                            e.printStackTrace();
                    //                        }
                    //                        handler.sendMessage(msg);
                    //
                    //                    }
                    //                }.start();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == cData ? 0 : cData.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (isPositionHeader(position))
//            return TYPE_HEADER;
//
//        return TYPE_ITEM;
//    }

//    private boolean isPositionHeader(int position) {
//        return position == 0;
//    }


    class CViewHolder extends ViewHolder {
        public TextView textView_c;
        public TextView time_c;
        public CardView c_card;

        //        public ViewPager viewPager;
        public CViewHolder(View itemView) {
            super(itemView);
            c_card = (CardView) itemView.findViewById(R.id.cnews_card);
            textView_c = (TextView) itemView.findViewById(R.id.c_news_title);
            time_c = (TextView) itemView.findViewById(R.id.c_news_time);
//            viewPager = (ViewPager) itemView.findViewById(R.id.viewpager);
        }
    }

    class hViewHolder extends ViewHolder {
        public TextView textView_c;
        public TextView time_c;

        //        public ViewPager viewPager;
        public hViewHolder(View itemView) {
            super(itemView);
            textView_c = (TextView) itemView.findViewById(R.id.c_news_title);
            time_c = (TextView) itemView.findViewById(R.id.c_news_time);
//            viewPager = (ViewPager) itemView.findViewById(R.id.viewpager);
        }
    }

}
