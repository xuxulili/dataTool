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
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    private String now_url;
    private ImageView back;
    private ImageView forward;
    private ImageView home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_web);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        floatingActionButton = (FloatingActionButton) findViewById(R.id.webView_share);
        Bundle bundle = getIntent().getExtras();
        getUrl = "";
        url = "";
        if (bundle.getString("getUrl") != null) {
            url = bundle.getString("getUrl");
        } else if (bundle.getString("url") != null) {
            url = bundle.getString("url");
        }
        if (!url.contains("http")) {
            url = "http://www.cqupt.edu.cn" + url;
        }
//        Log.e("weburl", url);
        getTitle = bundle.getString("getTitle");

//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
//                findViewById(R.id.collapsing_web_toolbar);
        if (getTitle != null) {
            getSupportActionBar().setTitle(getTitle);
        } else {
            getSupportActionBar().setTitle("重邮新闻");
        }
//        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
//        nestedScrollView.smoothScrollTo(0,0);
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
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(10240);
        webView.clearCache(true);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new wcc());
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()) {
                    case R.id.webView:
                        webView.requestFocus();
                        break;
                }
                return false;
            }
        });
        back = (ImageView) findViewById(R.id.back);
        forward = (ImageView) findViewById(R.id.forward);
        home = (ImageView) findViewById(R.id.now_job);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goForward();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl(url);
            }
        });
//            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        //去掉按钮
//            webSettings.setUseWideViewPort(true);


//            webSettings.setSupportZoom(true);
// 设置出现缩放工具
        webView.loadUrl(url);
        now_url = getUrl;
    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            pb.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            now_url=url;
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(isFinishing()){
                pb.setVisibility(View.GONE);
            }
        }
    };

    //Web视图
    private class wcc extends WebChromeClient {
//       为webView添加进度条

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            pb.setProgress(newProgress);
            if (newProgress == 100) {
                pb.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
            view.requestFocus();

        }
    }

    //-------------------------------------------

    /**
     * 后退
     */
    private void goBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            Toast.makeText(app.getContext(), "已经是第一页", Toast.LENGTH_SHORT).show();
        }
    }

    //-------------------------------------------

    /**
     * 前进
     */
    private void goForward() {
        if (webView.canGoForward()) {
            webView.goForward();
        } else {
            Toast.makeText(app.getContext(), "已经是最后一页", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_share) {
            String shareText = "";
            if (getTitle != null) {
                if (url != null) {
                    shareText = getTitle + url;
                }
            }

            if (shareText != null) {
                ShareUtil.shareText(this, shareText);
            }
        }
        if (item.getItemId() == R.id.action_look) {
            if (url != null) {
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
}