package com.xuxu.datatool.Activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuxu.datatool.Model.News;
import com.xuxu.datatool.Model.NewsDto;
import com.xuxu.datatool.R;
import com.xuxu.datatool.adpter.ImageLoader;
import com.xuxu.datatool.utils.GetData;
import com.xuxu.datatool.widget.AutoScrollTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/28.
 */
public class NewsContentActivity extends AppCompatActivity {
    public static final String CONTENT = "cheese_name";
    String urlString = "";
    private NewsDto newsDto;
    private List<News> newses;
    final String CONTENTURL = "http://www.superqq.com/blog/2015/07/28/four-create-uiimage-method/";
    TextView title;
    TextView content;
    private ImageLoader imageLoader;
    private ImageView imageView;
    String uri = "";
    private AutoScrollTextView autoScrollTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_content);
//        Intent intent = getIntent();
//        urlString = intent.getStringExtra(CONTENT);
//        Log.e("content",urlString);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("新闻");
        autoScrollTextView = (AutoScrollTextView)findViewById(R.id.TextViewNotice);
        autoScrollTextView.init(getWindowManager());
        autoScrollTextView.startScroll();
        ImageView imageView1 = (ImageView) findViewById(R.id.imageTool);
        imageView1.setImageResource(R.drawable.bg3);
        imageView= (ImageView) findViewById(R.id.image);
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        nestedScrollView.smoothScrollTo(0,0);
//        imageView.setImageResource(R.drawable.bg3);
//        newsDto = new NewsDto();
//        newsDto= GetData.getNews(CONTENTURL);
//        newses = newsDto.getNewses();
//        StringBuffer text= new StringBuffer();
//        for(News news:newses) {
//            Log.e("news", news + "");
//            text.append("/n").append(news.toString());
//        }

        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
//        Bundle bundle = getIntent().getExtras();
//        uri = bundle.getString("contentUrl");
//        Log.e("传递网址",uri);


        new ContentAsyncTask().execute();

//        textView.setText(text);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class ContentAsyncTask extends AsyncTask<String, Void, NewsDto> {


        @Override
        protected NewsDto doInBackground(String... strings) {
//            dbt = new DataBasetool(getActivity());
//            List<Map<String, Object>> list = GetData.getCsdnNetDate(strings[0]);
            List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
            List<NewsDto> newsDtos = new ArrayList<NewsDto>();

            newsDto = new NewsDto();
//            String url = GetData.getUrl(uri);
            newsDto=GetData.getNews();



//            for (int i = list.size() - 1; i >= 0; i--) {//注意此处范围，容易报数组溢出异常
//                /*倒序存入解决新存入数据在列表下方问题，如数据库有4,3,2,1，有新数据存储时，直接存入1后面
//                * 注意提取数据时必须从moveToLast开始倒序取至moveToFirst(moveToPrevious)*/
//                String cnews_details_title = list.get(i).get("cnews_details_title").toString();
//                String url = list.get(i).get("url").toString();
//                Boolean isHased = false;
//                list1 = dbt.selectDB8();//list.size()次数据库查询，保证每个数据在数据库不存在
//                Log.e("selectDB8", list1 + "");
//                /*此处很好的处理了重复添加数据的问题*/
//                for (int n = 0; n < list1.size(); n++) {
//                    if (cnews_details_title.equals(
//                            (list1.get(n).get("cnews_details_title").toString()))) {
//                        isHased = true;
//                    }
//                }
//                if (!isHased) {
//                    dbt.addDB(cnews_details_title, url);
//                }
//            }
//
//
//            Log.e("listDataBase", dbt.selectDBAll() + "");
            return newsDto;
        }

        @Override
        protected void onPostExecute(NewsDto newsDto1) {
            super.onPostExecute(newsDto1);
            StringBuffer text= new StringBuffer();
            newses = newsDto1.getNewses();
            List<String> images = new ArrayList<String>();

            for(News news:newses) {

//                Log.e("news", news + "");
                if (news.getTitle() != null) {
                    title.setText(news.getTitle());
                } else if(news.getlittleTitle()!=null) {
//                    Log.e("fromHtml", Html.fromHtml(news.getContent().toString())+"");
                    text.append("\n").append("\n").append(news.getlittleTitle()).append("\n");
                }else  {
                    text.append("\n").append(news.getContent());
                }
                String imageLink=news.getImageLink();
                images.add(imageLink);
            }

            content.setText(text.toString());
        }
    }
}

