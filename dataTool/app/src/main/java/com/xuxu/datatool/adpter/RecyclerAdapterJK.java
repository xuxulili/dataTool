package com.xuxu.datatool.adpter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xuxu.datatool.Activity.WebViewActivity;
import com.xuxu.datatool.R;
import com.xuxu.datatool.data.DataBasetool;
import com.xuxu.datatool.utils.GetData;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/10.
 */
public class RecyclerAdapterJK extends RecyclerView.Adapter<RecyclerAdapterJK.JKViewHolder>{
    private Context context;
    private List<Map<String, Object>> data;
    private View view;
    private String getUrl;
    private LayoutInflater inflater;
    private int mBackground;
    private DataBasetool dbt;

    private MaterialDialog noNetWorkDialog;
    private final TypedValue mTypedValue = new TypedValue();
    public RecyclerAdapterJK(Context context,List<Map<String, Object>> data) {
        this.context = context;
        dbt = new DataBasetool(context);
        this.data = data;
        inflater = LayoutInflater.from(this.context);
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }

    private OnRecyclerLongClickListener mOnRecyclerLongClickListener;
    public interface OnRecyclerLongClickListener{
        void onRecyclerLongClick(View v,String delete);
    }
    public  void setOnRecyclerLongClickListener(OnRecyclerLongClickListener onRecyclerLongClickListener) {
        mOnRecyclerLongClickListener = onRecyclerLongClickListener;
    }
    @Override
    public JKViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_card_view, parent, false);
        view.setBackgroundResource(mBackground);

        return new JKViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final JKViewHolder holder, final int position) {
//        Log.e("holder",holder+"");
//        final int i = position;
        holder.title.setText(data.get(position).get("jk_title").toString());
//        Log.e("adaptertitle", data.get(position).get("jk_title").toString());
        holder.itemView.setTag(data.get(position).get("jk_title").toString());
//        holder.cnews_details_title.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (onRecyclerLongClickListener!=null) {
//                    onRecyclerLongClickListener.onRecyclerLongClick(view,data.get(position).get("cnews_details_title").toString());
//                }
//                return true;
//            }
//        });
        holder.title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick( final View view) {
                Log.e("自定义长按监听","自定义长按监听");
                Log.e("回调接口",mOnRecyclerLongClickListener+"");
                /*
                * 思考如何动态获取position*/
//                if (mOnRecyclerLongClickListener!=null) {
//                    Log.e("回调进行中","回调进行中");
//                    mOnRecyclerLongClickListener.onRecyclerLongClick(view,data.get(position).get("cnews_details_title").toString());
//                }

                Toast.makeText(context, "别按我了",Toast.LENGTH_SHORT).show();
                if (noNetWorkDialog == null) {
                    noNetWorkDialog = new MaterialDialog.Builder(context)
                            .title("删除选项")
                            .content("是否删除?")
                            .positiveText("是")
                            .negativeText("否")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
//                                    Log.e("删除", data.get(position).get("cnews_details_title").toString());
//                                    Log.e("删除", holder.title.getText().toString());
                                    dbt.delete(data.get(position).get("cnews_details_title").toString());
                                    //dbt需要实例，否则报空值异常
                                    data = dbt.selectDBAll();
                                    notifyDataSetChanged();//添加此句实现实现实时删除ITEM
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                }
                            })
                            .cancelable(false)
                            .build();
                }
                if (!noNetWorkDialog.isShowing()) {
                    noNetWorkDialog.show();
                }
                return true;
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("uri", data.get(position).get("url").toString());

                final ProgressDialog pd = ProgressDialog.show(context, "connecting", "正在连接");
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what < 0) {
                            Toast.makeText(context, "数据获取失败", Toast.LENGTH_SHORT).show();
                        } else {
                            pd.dismiss();
                            Intent intent = new Intent(context, WebViewActivity.class);
                            if (getUrl != null) {
//                                Log.e("geturl", getUrl);
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("getUrl", getUrl);
                            bundle.putString("url", data.get(position).get("url").toString());
                            bundle.putString("getTitle", data.get(position).get("jk_title").toString());
                            intent.putExtras(bundle);
                            //获取网址错误或不规范（不是http开头）
                            // 导致无法启动
                            context.startActivity(intent);
                        }
                    }
                };
                new Thread() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        try {

                            getUrl = GetData.getUrl(data.get(position).get("url").toString());
                            msg.what = getUrl.length();
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

    class JKViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView getTitle() {
            return title;
        }

        public JKViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
        }
    }
}
