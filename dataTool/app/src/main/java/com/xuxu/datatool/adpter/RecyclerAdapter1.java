package com.xuxu.datatool.adpter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.xuxu.datatool.Activity.WebViewActivity;
import com.xuxu.datatool.R;
import com.xuxu.datatool.utils.GetData;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/24.
 */
public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.TwoViewHolder> {
    private List<Map<String, Object>> data;
    private Context context;
    private LayoutInflater inflater;
    private View view;
    private int mBackground;
    private final TypedValue mTypedValue = new TypedValue();
    private String getUrl;

    public RecyclerAdapter1(Context context, List<Map<String, Object>> datas) {
        this.data = datas;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
//        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//        mBackground = mTypedValue.resourceId;
        Log.e("设置adpter","设置adpter1");

    }
    @Override
    public TwoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_card_view, parent, false);
//        view.setBackgroundResource(mBackground);
        return new TwoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TwoViewHolder holder, final int position) {
        Log.e("设置adpter","设置adpter2");
        holder.title.setText(data.get(position).get("cnews_details_title").toString());
//        Log.e("adaptertitle",data.get(position).get("cnews_details_title").toString());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e("uri", data.get(position).get("url").toString());

                final ProgressDialog pd = ProgressDialog.show(context,"connecting","正在连接" );
                final Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what<0){
                            Toast.makeText(context, "数据获取失败", Toast.LENGTH_SHORT).show();
                        }else{
                            pd.dismiss();
                            Intent intent = new Intent(context, WebViewActivity.class);
                            if(getUrl!=null){
                            Log.e("geturl", getUrl);}
                            Bundle bundle = new Bundle();
                            bundle.putString("getUrl",getUrl);
                            bundle.putString("url",data.get(position).get("url").toString());
                            bundle.putString("getTitle",data.get(position).get("cnews_details_title").toString());
                            intent.putExtras(bundle);
                            //获取网址错误或不规范（不是http开头）
                            // 导致无法启动
                            context.startActivity(intent);
                        }
                    }
                };
                new Thread(){
                    @Override
                    public void run() {
                        Message msg=new Message();
                        try {

                            getUrl = GetData.getUrl(data.get(position).get("url").toString());
                            msg.what=getUrl.length();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });

    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    public class TwoViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public TwoViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

}

