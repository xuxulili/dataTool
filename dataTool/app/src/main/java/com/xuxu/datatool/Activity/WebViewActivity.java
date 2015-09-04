package com.xuxu.datatool.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xuxu.datatool.R;
import com.xuxu.datatool.utils.GetData;
import com.xuxu.datatool.utils.ShareUtil;

/**
 * Created by Administrator on 2015/7/30.
 */
public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private String getUrl;
    private String url;
    private String getTitle;
    private ProgressBar pb;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_web);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.webView_share);
        Bundle bundle = getIntent().getExtras();
        getUrl = "";
        url = "";
        getUrl = bundle.getString("getUrl");
        url = bundle.getString("url");
        if(!url.contains("http")) {
            url = "http://www.cqupt.edu.cn"+url;
        }
//        Log.e("weburl", url);
        getTitle = bundle.getString("getTitle");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_web_toolbar);
        if (getTitle!=null) {
            collapsingToolbarLayout.setTitle(getTitle);
        } else {
            collapsingToolbarLayout.setTitle("重邮新闻");
        }
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        nestedScrollView.smoothScrollTo(0,0);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setMax(100);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        //初始显示模式



        webView.clearCache(true);

        if(getUrl!=null) {
//            Log.e("getUrl", getUrl);
            //加载需要显示的网页
//            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setDisplayZoomControls(false);
//            webSettings.setSupportZoom(true);
// 设置出现缩放工具
            webSettings.setBuiltInZoomControls(true);
//扩大比例的缩放
//            webSettings.setUseWideViewPort(true);
//自适应屏幕
//            webSettings.setLoadWithOverviewMode(true);
//            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            webView.loadUrl(getUrl);
        }else{
//            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            //去掉按钮
//            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setDisplayZoomControls(false);
//            webSettings.setSupportZoom(true);
// 设置出现缩放工具
            webSettings.setBuiltInZoomControls(true);
            webView.loadUrl(url);
        }
        //设置Web视图
        webView.setWebChromeClient(new webViewClient());
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareText = "";
                if (getTitle != null) {
                    if (getUrl != null) {
                        shareText = getTitle + getUrl;
                    } else {
                        shareText = getTitle + url;
                    }
                }

                if (shareText!=null) {
                    ShareUtil.shareText(WebViewActivity.this, shareText);
                }
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
//        if(item.getItemId() == R.id.action_share) {
//            String shareText = "";
//            if (getTitle != null) {
//                if (getUrl != null) {
//                    shareText = getTitle + getUrl;
//                } else {
//                    shareText = getTitle + url;
//                }
//            }
//
//            if (shareText!=null) {
//                ShareUtil.shareText(this, shareText);
//            }
//        }
        if(item.getItemId()==R.id.action_look){
            if (getUrl != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl));
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }


    //Web视图
    private class webViewClient extends WebChromeClient {
//       为webView添加进度条

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            pb.setProgress(newProgress);
            if(newProgress==100){
                pb.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_fade_in);
    }
}