package com.xuxu.datatool.adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuxu.datatool.Activity.CNewsDetailsActivity;
import com.xuxu.datatool.Activity.WebViewActivity;
import com.xuxu.datatool.Model.CNews;
import com.xuxu.datatool.R;

import java.util.List;

/**
 * Created by Administrator on 2015/8/12.
 */
public class ListAdpter extends BaseAdapter {
    private List<CNews> cData;
    private Context context;
    private String getUrl;
    private LayoutInflater inflater;

    public ListAdpter(Context context, List<CNews> cData) {
        this.cData = cData;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return cData.size();
    }

    @Override
    public Object getItem(int i) {
        return cData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public View getView( int position, View view, ViewGroup viewGroup) {
        final int i=position;
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.cnews_card_view, null);
            holder.cardView = (CardView) view.findViewById(R.id.cnews_card);
            holder.textView_content = (TextView) view.findViewById(R.id.c_news_time);
            holder.textView_title = (TextView) view.findViewById(R.id.c_news_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView_content.setText(cData.get(i).getcTime().toString());
        holder.textView_title.setText(cData.get(i).getcTitle().toString());
        holder.textView_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Log.e("uri", cData.get(i).getcUrl().toString());
                //                Intent intent = new Intent(context,NewsContentActivity.class);
                //                Bundle bundle = new Bundle();
                //
                //                bundle.putString("contentUrl", data.get(position).get("url").toString());
                ////                intent.putExtra("content", data.get(position).get("url"));
                //                intent.putExtras(bundle);
                //                context.startActivity(intent);

                //                final ProgressDialog pd = ProgressDialog.show(context,"connecting","正在连接" );
                //                pd.dismiss();

                Intent intent = null;
//                    if ((cData.get(i).getcUrl().toString()).contains("/xwzx/")) {
                intent = new Intent(context, CNewsDetailsActivity.class);
//                    }else{
//                        intent = new Intent(context, WebViewActivity.class);
//                    }
                if (getUrl != null) {
//                        Log.e("geturl", getUrl);
                }
                Bundle bundle = new Bundle();
                bundle.putString("getUrl", getUrl);
                bundle.putString("url", cData.get(i).getcUrl().toString());
                bundle.putString("getTitle", cData.get(i).getcTitle().toString());
                intent.putExtras(bundle);
                //获取网址错误或不规范（不是http开头）
                // 导致无法启动
                context.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        public CardView cardView;
        public TextView textView_title;
        public TextView textView_content;

    }
}
