package com.xuxu.datatool.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuxu.datatool.Model.CNewsDetails;
import com.xuxu.datatool.R;
import com.xuxu.datatool.adpter.CNewsAdapter;
import com.xuxu.datatool.utils.GetData;
import com.xuxu.datatool.utils.NetWorkUtil;
import com.xuxu.datatool.utils.ShareUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/13.
 */
public class CNewsDetailsActivity extends SwipeBackActivity {
    private RecyclerView recyclerView_CNewsDetails;
    private String url;
    private TextView textView;
    private int reTry=0;
    //    private GestureFrameLayout gestureFrameLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cnews_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//增加返回图标
        getSupportActionBar().setTitle("重邮头条");

//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
//                findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle("新闻详情");

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        if(!url.contains("http")) {
            url = "http://xwzx.cqupt.edu.cn/xwzx/" + url;
        }
//        Log.e("获取最终详细新闻网址","");
        iniImageLoader();
        initView();
        new CNewsDetailsAsyncTask().execute();
    }

    private void iniImageLoader() {
    }

    private void initView() {

        textView = (TextView) findViewById(R.id.cNews_details_loading);
        recyclerView_CNewsDetails = (RecyclerView) findViewById(R.id.recyclerView_CNewsDetails);
        recyclerView_CNewsDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_CNewsDetails.setItemAnimator(new DefaultItemAnimator());

        // touchView要设置到ListView上面

//        gestureFrameLayout = (GestureFrameLayout) findViewById(R.id.news_content_gesture_layout);
//        gestureFrameLayout.attachToActivity(this);


    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取navigation bar的高度
     *
     * @return
     */
    protected int getNavigationBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_web,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
//        if (item.getItemId() == R.id.action_look) {
//            ShareUtil.ScreenShot(recyclerView_CNewsDetails);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    class CNewsDetailsAsyncTask extends AsyncTask<String, Void, List<CNewsDetails>> {

        @Override
        protected List<CNewsDetails> doInBackground(String... strings) {
            List<CNewsDetails> list = null;
            if (NetWorkUtil.isNetWorkConnected(CNewsDetailsActivity.this)) {
//                try {
////                    GetData.testUserHttpUnit();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                list = GetData.getCNewsDetailsList(url);
                for (CNewsDetails cNewsDetails : list) {
//                    Log.e("最终获取的新闻详细数据", cNewsDetails.toString());
                }
            } else {

            }

            return list;
        }

        @Override
        protected void onPostExecute(List<CNewsDetails> cNewsDetailses) {
            super.onPostExecute(cNewsDetailses);

            if(cNewsDetailses!=null) {
                if(cNewsDetailses.size()!=0) {
                    textView.setVisibility(View.INVISIBLE);
                    CNewsAdapter cNewAdapter = new CNewsAdapter(CNewsDetailsActivity.this, cNewsDetailses);
                    recyclerView_CNewsDetails.setAdapter(cNewAdapter);
                }else{
//                    Toast.makeText(app.getContext(),"重邮主页网络不好，正在努力加载！",Toast.LENGTH_SHORT).show();
                    if(reTry<6){
                        if(reTry==2||reTry==5){
                            Toast.makeText(app.getContext(),"重邮主页网络不好，正在努力加载！",Toast.LENGTH_SHORT).show();
                        }
                        reTry++;
                        handler.sendEmptyMessageDelayed(0, 100);
                    }else {
                        Toast.makeText(app.getContext(), "重连失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else{
                if(reTry<=5){
                    if(reTry==2||reTry==5){
                        Toast.makeText(app.getContext(),"重邮主页网络不好，正在努力加载！",Toast.LENGTH_SHORT).show();
                    }
                    reTry++;
                    handler.sendEmptyMessageDelayed(0, 100);
                }else {
                    Toast.makeText(app.getContext(), "重连失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                }
            }

//            cNewAdapter.setOnItemClickListener(new CNewsAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    startActivity(new Intent(CNewsDetailsActivity.this, MainActivity.class));
//
//                }
//            });

        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            new CNewsDetailsAsyncTask().execute();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_right_out);
    }
}
