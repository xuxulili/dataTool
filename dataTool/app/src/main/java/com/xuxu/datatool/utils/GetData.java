package com.xuxu.datatool.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.xuxu.datatool.Model.CNews;
import com.xuxu.datatool.Model.CNewsDetails;
import com.xuxu.datatool.Model.GeekNews;
import com.xuxu.datatool.Model.News;
import com.xuxu.datatool.Model.NewsDto;
import com.xuxu.datatool.Model.NewsPicture;
import com.xuxu.datatool.Model.province;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuxu on 2015/7/23.
 */
public class GetData {
    /*Socket与Http区别

    * 需要服务器端主动向客户端推送数据，保持客户端与服务器数据的实时与同步。此时若双方建立的是Socket连接，
    * 服务器就可以直接将数据传送给客户端；
    * 若双方建立的是HTTP连接，则服务器需要等到客户端发送一次请求后才能
    * 将数据传回给客户端，因此，客户端定时向服务器端发送连接请求，不仅可以保持在线，同时也是在“询问”服务器是
    * 否有新的数据，如果有就将数据传给客户端。*/


/*jsoup总结
* doc.select("div.news");  select("td[style=line-height:30px;]")  getElementById("news_content")
* select("article.hentry") getElementsByTag("h3")  getElementsByClass("link_detail")
        element.attr("href")
        Pattern p = Pattern.compile("_banner2 = (.*?);");*/
    public List<Map<String, Object>> data;

    private String url = "http://www.nmc.cn/";//天气查询

    final String CSDNURL = "http://www.csdn.net/";
    final String CONTENTURL = "http://www.superqq.com/blog/2015/07/28/four-create-uiimage-method/";
    //    final String CQUPTURL = "http://xwzx.cqupt.edu.cn/xwzx/news_type.php?id=1&page=";
    static final String CQUPTURL = "http://www.cqupt.edu.cn/cqupt/index.shtml";
    final static String CNEEWSDETAILS = "http://xwzx.cqupt.edu.cn/xwzx/news.php?id=28377";

    final static String Geek = "http://geek.csdn.net/newest";
    public static List<GeekNews> getGeekFirst() {
        //新建一个geekList
        List<GeekNews> geekNewsList = null;
        try {
            Document document = Jsoup.connect(Geek).timeout(3000).get();
            geekNewsList = new ArrayList<>();
            Element element_div = document.getElementsByAttributeValue("id", "geek_list").get(0);
            Elements elements_title = document.getElementsByAttributeValue("class", "title");
            Elements elements_type = document.getElementsByAttributeValue("class", "news_vote");
            Elements elements_img = element_div.getElementsByAttributeValue("class", "avatar");
            Elements elements_time = document.getElementsByAttributeValue("class", "list-inline");
            if (elements_title.size() == elements_time.size()) {
                //新建一个几个新闻对象news_vote

                for (int i = 0; i < elements_title.size(); i++) {
                    GeekNews geekNews = new GeekNews();
                    Element element_title = elements_title.get(i);
                    String geek_title = element_title.text();
                    String geek_url = element_title.attr("href");
                    geekNews.setJk_title(geek_title);
                    geekNews.setJk_url(geek_url);
//                    Log.e("第一次刷新获取title", geek_title + geek_url);

                    if (elements_title != null) {
                        Element element_time = elements_time.get(i);
                        Elements elements_li = element_time.select("li");
                        if (elements_li != null) {
                            String geek_time = elements_li.get(1).text();
                            String geek_label = elements_li.get(2).text();
                            geekNews.setJk_label(geek_label);
                            geekNews.setJk_time(geek_time);
//                            Log.e("第一次获取的时间", geek_time + geek_label);
                        }
                    }
                    if (elements_img != null) {
                        Element element_img = elements_img.get(i);
                        String geek_icon = element_img.attr("src");
//                        Log.e("第一次获取的icon地址", geek_icon);
                        geekNews.setJk_userNameImg(geek_icon);
                    }
                    if (elements_type != null) {
                        Element element_type = elements_type.get(i);
                        int geek_type = Integer.parseInt(element_type.attr("voteid"));
                        geekNews.setJk_type(geek_type);
//                        Log.e("第一次获取的type", geek_type+"");
                    }
                    geekNews.setJk_userName("");
//                    Log.e("第一次list添加前geeknews",geekNews.toString());
                    geekNewsList.add(geekNews);
                }

            }
        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        }
        return geekNewsList;
    }
    public static int getGeekNum(){
        int number=0;
        try {
            Document document = Jsoup.connect(Geek).timeout(5000).get();
            Element element = document.select("dt[class=left]").get(0);
//            Log.e("dt[class=left]",element.toString());
            Element element_num = element.getElementsByAttributeValue("class", "news_vote").get(0);
//            Log.e("极客头条数目", element_num.attr("voteid"));
            number = Integer.parseInt(element_num.attr("voteid"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    public static List<GeekNews> getGeekList(int begin,int page) {
        int sum = getGeekNum();
        List<GeekNews> geekNewsList = new ArrayList<>();
        for (int p = begin; p > begin-page;p--) {
            String url = "http://geek.csdn.net/news/detail/" + p;
            try {
                GeekNews geekNews = new GeekNews();
//                Log.e("得到一篇文章网址,并且开始解析", url);
                Document document = Jsoup.connect(url).timeout(3000).get();
                Element element_header = document.getElementsByTag("h2").get(0);
                geekNews.setJk_type(p);
                if (!element_header.getElementsByTag("a").toString().equals("")) {
                    Element element_details = element_header.getElementsByTag("a").get(0);
//                    Log.e("得到文章头部信息", element_details.text()+element_details.attr("href"));
                    geekNews.setJk_title(element_details.text());
                    geekNews.setJk_url(element_details.attr("href"));

                    String label = "";
                    Elements element_labels = document.select("a[class=label label-default]");
                    for(Element element_label:element_labels) {
                        label += element_label.text()+"  ";
                    }
                    geekNews.setJk_label(label);
//                    Log.e("得到文章标签信息",label);

                    Element element_user = document.getElementsByAttributeValue("class", "footer_bar clearfix").get(0);
//                    Log.e("文章作者信息",element_user.toString());
                    Element element_userImg = element_user.select("img[class=avatar]").get(0);
                    Element element_userName = element_user.select("a[class=user_name]").get(0);
                    Element element_time = element_user.select("span[class=time]").get(0);
//                    Log.e("得到文章作者信息",element_userName.text()+element_userImg.attr("src")+element_time.text());
                    geekNews.setJk_userName(element_userName.text());
                    geekNews.setJk_userNameImg(element_userImg.attr("src"));
                    geekNews.setJk_time(element_time.text());
                }
                else {
                    geekNews.setJk_title(element_header.text());
                    geekNews.setJk_url(url);
                }
                geekNewsList.add(geekNews);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return geekNewsList;
    }

    public static String getWeatherUrl(String cityName,Context context) {
        String weatherUrl = null;
        String locationName = ChineseUtil.getCityName(context);
//        String city = "重庆市";
        //很有意思的一点重庆的拼音是zhongqing
//        String city = cityName;//此处出现，双引号的string可以进行contains判断，
// 而且获取的string始终通不过判断,还有州 区没有做特么处理
//        Log.e("等待处理的城市名",ChineseUtil.getPingYin(locationName));

        String url1="";
        String url2="";
        String url = "";
        List<province> section = CityData.getList();
        for(province pro:section) {
            //查询省份
            //str1.indexOf(str2) != -1 || str2.indexOf(str1) != -1
            if ((pro.getPlace()).indexOf(cityName)!=-1||cityName.indexOf(pro.getPlace())!=-1) {
                url1 = "A"+pro.getTag();
//                Log.e("省份城市列表",pro.getPlace());
                url2 = ChineseUtil.getPingYin(cityName);
                if(cityName.contains("市")) {
                    url2=url2.substring(0,url2.length()-3);//把“市”去掉
                }else if(cityName.contains("区")){
                    url2=url2.substring(0,url2.length()-2);
                }else if(cityName.toCharArray().length>2&&cityName.contains("州")){
                    url2=url2.substring(0,url2.length()-4);
                }
                break;
            }


        }
        //http://www.nmc.cn/publish/forecast/AXJ/wulumuqi.html
        if (!url1.equals("")) {
            if(!url2.equals("")) {
                url = "http://www.nmc.cn/publish/forecast/" + url1 + "/" + url2 + ".html";
            }
        } else {

            url = "http://www.nmc.cn/publish/forecast/ACQ/zhongqing.html";
        }
//        Log.e("获取的天气地址",url);
        return  url;
    }
    public static  List<province> getAllPlace() {
        try {
            Document document = Jsoup.connect("http://www.maps7.com/china_province.php").get();
            Element element = document.select
                    ("div[style=width:940px; text-align:left; line-height:2]").get(0);
            String html = element.toString();
//            Log.e("div",html);
            Pattern p = Pattern.compile("h4(.*?)h4");
//        Pattern p = Pattern.compile("class=\"link_title\"><a href=\"(.*?)\">");
            Matcher m = p.matcher(html);

            while (m.find()) {
//                Log.e("正则表达式有输出", "");
                MatchResult mr = m.toMatchResult();
                for(int i=0;i<=mr.groupCount();i++) {
//                    Log.e("正则表达式输出", mr.group(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public  static List<CNewsDetails> getCNewsDetailsList(String url) {
        List<CNewsDetails> cNewsDetailses = new ArrayList<>();
        String htmlStr = null;
        try {
//            htmlStr = doGet(url);
//            Log.e("htmlStr",htmlStr+"");
//            Document doc = Jsoup.connect("http://www.open-open.com").get();
            Document document = Jsoup.connect(url).timeout(4000)
                    .get();
            //使用此种方法不会导致解析出来的文字乱码，乱码原因有两个：1、使用doget方式获取的string乱码2、jsoup解析string乱码
            Element element_title = document.select("td[style=padding-top:10px; height:30px; " +
                    "font-size:16px; font-family:'黑体'; line-height:30px;]").get(0);
//            Log.e("cnews_details_title",element_title.text());
            CNewsDetails cNewsDetails1 = new CNewsDetails();
            cNewsDetails1.setCNewsDetails_title(element_title.text());
            cNewsDetails1.setCNewsDetails_type(CNewsDetails.CNewsDetailsType.TITLE);
            cNewsDetailses.add(cNewsDetails1);

            Element element_details = document.select("td[style=line-height:30px;]").get(0);//此处忘了加style=导致无法往下执行
//            Log.e("details",element_details.text());
            CNewsDetails cNewsDetails4 = new CNewsDetails();
            cNewsDetails4.setCNewsDetails_details(element_details.text());
            cNewsDetails4.setCNewsDetails_type(CNewsDetails.CNewsDetailsType.DETAILS);
            cNewsDetailses.add(cNewsDetails4);


            Element element = document.getElementById("news_content");
            Elements elements = element.getElementsByTag("P");
//            Element element1 = elements.get(0);
            for(Element element1:elements){
                if(element1.select("A").size()==0) {//此处判断需要用size()方法
//                    Log.e("content", element1.text().toString());
                    CNewsDetails cNewsDetails2 = new CNewsDetails();
                    cNewsDetails2.setCNewsDetails_content("    "+element1.text());
                    cNewsDetails2.setCNewsDetails_type(CNewsDetails.CNewsDetailsType.CONTENT);
                    cNewsDetailses.add(cNewsDetails2);
                }else{
                    CNewsDetails cNewsDetails3 = new CNewsDetails();
                    /*处理图片不完全问题*/
                    if((element1.select("A").attr("href")).contains("http")) {
                        cNewsDetails3.setCNewsDetails_imageUrl(element1.select("A").attr("href"));
//                        Log.e("imageUrl",element1.select("A").attr("href"));
                    }else{
                        cNewsDetails3.setCNewsDetails_imageUrl("http://xwzx.cqupt.edu.cn"+
                                element1.select("A").attr("href"));
//                        Log.e("imageUrl1","http://xwzx.cqupt.edu.cn"+element1.select("A").attr("href"));
                    }
                    cNewsDetails3.setCNewsDetails_type(CNewsDetails.CNewsDetailsType.IMG_URL);
                    cNewsDetailses.add(cNewsDetails3);
                }
            }
//            Element element = elements.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cNewsDetailses;
    }

    /*
   * 抓取极客头条文章内容*/
    public static NewsDto getNews() {
        NewsDto newsDto = new NewsDto();
        List<News> newses = new ArrayList<News>();
//        String htmlStr = null;
//        try {
//            htmlStr = doGet(urlStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Document doc = Jsoup.parse("http://www.superqq.com/blog/2015/07/28/four-create-uiimage-method/");

        // 获得文章中的第一个detail
        Element detailEle = doc.select("article.hentry").get(0);
        // 标题
        Element titleEle = detailEle.select("h1.entry-cnews_details_title").get(0);
        News news = new News();
        news.setTitle(titleEle.text());
        news.setType(News.NewsType.TITLE);
        newses.add(news);
        // 摘要
//        Element summaryEle = detailEle.select("div.entry_content").get(0);
//        news = new News();
//        news.setSummary(summaryEle.text());
//        newses.add(news);
        // 内容
        Element contentEle = detailEle.select("div.entry-content").get(0);
        Elements childrenEle = contentEle.children();

        for (Element child : childrenEle) {
            Elements little = child.getElementsByTag("h3");

            if (little.size() > 0) {
                if ((little.text()) == null) continue;
                news = new News();
                news.setlittleTitle(little.text());
                newses.add(news);
            }

            little.remove();


            Elements imgEles = child.getElementsByTag("img");
            // 图片
            if (imgEles.size() > 0) {
                for (Element imgEle : imgEles) {
                    if (imgEle.attr("src").equals(""))
                        continue;
                    news = new News();
                    news.setImageLink(imgEle.attr("src"));
                    newses.add(news);
                }
            }
            // 移除图片
            imgEles.remove();
            Elements content = child.getElementsByTag("p");

            if (content.size() > 0) {
                if ((content.text()) == null) continue;
                news = new News();
                news.setContent(content.text());
                newses.add(news);
            }

            content.remove();
            Elements pre = child.getElementsByTag("pre");


            if (pre.size() > 0) {
                if ((pre.text()) == null) continue;
                news = new News();
                news.setContent("\n" + pre.text() + "\n");
                newses.add(news);
            }

            pre.remove();
//            if (child.text().equals(""))
//                continue;
//
//            news = new News();
//            news.setType(News.NewsType.CONTENT);
//
//            try {
//                if (child.children().size() == 1) {
//                    Element cc = child.child(0);
//                    if (cc.tagName().equals("h3")) {
//                        news.setType(News.NewsType.BOLD_TITLE);
//                    }
//                }
//
//            } catch (IndexOutOfBoundsException e) {
//                e.printStackTrace();
//            }
//            news.setContent(child.text());
//            newses.add(news);
        }
        newsDto.setNewses(newses);
        return newsDto;
    }

    private static String jsonUrl = "http://www.cqupt.edu.cn/cqupt/dynamic/imgs.js";

    public static String getUrl(String url) throws Exception {
        String csdnString = doGet(url);
//        Log.e("csdnString", csdnString);//其实不能完全打印
        String result = "";

        Document document = Jsoup.parse(csdnString);
        Elements elements = document.getElementsByClass("link_detail");
        Element element = elements.get(0);
        result = element.attr("href");
        return result;

    }

    /*
        * 抓取viewpager*/
    public static List<NewsPicture> getNewsPicture() {
        List<NewsPicture> list = null;
        NewsPicture newsPicture;
        String htmlStr = "";
        JSONObject jsonObject;
        try {

            htmlStr = doGet(jsonUrl);
//            Log.e("htmlstr", htmlStr);//其实不能完全打印
            if (htmlStr != null) {
                list = new ArrayList<NewsPicture>();
//                Pattern p = Pattern.compile("cnews_details_title=\"(.*?)\" href=\"(.*?)\".*?363");
                Pattern p = Pattern.compile("_banner2 = (.*?);");
//        Pattern p = Pattern.compile("class=\"link_title\"><a href=\"(.*?)\">");
                Matcher m = p.matcher(htmlStr);

                while (m.find()) {
                    MatchResult mr = m.toMatchResult();
//                    Log.e("正则表达式输出", mr.group(1));
                    JSONArray jsonArray = new JSONArray(mr.group(1));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        newsPicture = new NewsPicture();
//                        Log.e("imgaddr", "http://www.cqupt.edu.cn" + jsonObject.get("imgaddr").toString());
                        newsPicture.setImageUrl("http://www.cqupt.edu.cn" + jsonObject.get("imgaddr").toString());
//                        Log.e("imgaddr", jsonObject.get("linkaddr").toString());
                        newsPicture.setNewsUrl(jsonObject.get("linkaddr").toString());
                        list.add(newsPicture);
                    }
                }
//                jsonObject = new JSONObject(htmlStr);
//                JSONArray jsonArray = jsonObject.getJSONArray("imgaddr");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    jsonObject = jsonArray.getJSONObject(i);
//                    newsPicture = new NewsPicture();
//                    newsPicture.setImageUrl(jsonObject.get("imgaddr").toString());
//                    Log.e("newsPicture", jsonObject.get("imgaddr").toString());
//                }
            }
//            Document document = Jsoup.parse(htmlStr);
//            Elements elements = document.getElementsByAttributeValue("id", "ads_slider");
//            Element element = elements.get(0);
//            Log.e("抓取到的新闻网址",element+"");
//            Elements elements_image = element.select("a");
//            Log.e("抓取到的新闻网址",elements_image+"");
//            Element element_image = elements_image.get(0);
//            Log.e("抓取到的新闻网址",element_image+"");
//            Log.e("抓取到的新闻网址",element_image.attr("href"));
//            newsPicture.setNewsUrl(element_image.attr("href"));
//            newsPicture.setImageUrl(element_image.attr("src"));

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static final String TOPCNEWSURL = "http://xwzx.cqupt.edu.cn/xwzx/news_type.php?id=1&page=";
    public static List<CNews> getNewCquptData(int page) {
        List<CNews> list = null;
        try {
//            Log.e("当前请求数据页数",page+"");
//            Log.e("当前请求数据网址",TOPCNEWSURL + page);
            Document document = Jsoup.connect(TOPCNEWSURL + page).timeout(6000).get();
            Elements elements_title = document.getElementsByAttributeValue("onmouseout", "this.style.background='#FFFFFF';");
            list = new ArrayList<>();
            for (Element element_title : elements_title) {
                CNews cNews = new CNews();
                Element element_detail = element_title.getElementsByTag("a").get(0);
                cNews.setcTitle(element_detail.attr("title"));
                cNews.setcUrl(element_detail.attr("href"));
                Element element_time = element_title.getElementsByTag("td").get(1);
                cNews.setcTime(element_time.text());
//                Log.e("新增一条重邮头条新闻", cNews.toString());
                list.add(cNews);
            }
//            Log.e("重邮头条新闻", elements_title.get(0).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 联网获得数据
     *
     * @return 数据
     * @author xuxu
     * @created 2015-7-24
     */
    //利用Jsoup爬虫
    public static List<CNews> getCquptData() {
//        String url = CQUPTURL + page;
        String code = null;
//        try {
//            code = doGet(CQUPTURL);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e("CQUPTCODE", code);
        List<CNews> list = new ArrayList<CNews>();
        Document doc = null;
        try {
            doc = Jsoup.connect(CQUPTURL).get();
            Elements units = doc.select("div.news");
            for (int i = 0; i < units.size(); i++) {
//            newsItem = new NewsItem();
//            newsItem.setNewsType(newsType);
                CNews cNews1 = new CNews();

                org.jsoup.nodes.Element unit_ele = units.get(i);

                Elements a_ele = unit_ele.getElementsByTag("a");
                for (Element a_a_ele : a_ele) {
                    Log.e("a_title", a_a_ele.attr("title"));
                    cNews1.setcTitle(a_a_ele.attr("title"));
                    cNews1.setcUrl(a_a_ele.attr("href"));
                }

                Element span_ele = unit_ele.getElementsByAttributeValue("class", "time").get(0);

                cNews1.setcTime(span_ele.text());
//                Log.e("time", span_ele.text());
                list.add(cNews1);
//                String[] arr = list.toArray(new String[list.size()]);//list与数组之间转化
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;
    }
    //通过URL获取源码标准代码

    public static String doGet(String urlStr) throws Exception {
        if (urlStr == "") {
            return null;
        }
        URL url;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
            }
            conn.disconnect();
        }

        return null;
    }

    /**
     * 清除APP数据
     */
    public static void clearData(Context context) {
//      final String path = "/data/data/" + getPackageName().toString();
        final String path = context.getFilesDir().getParent();

        //清空配置文件目录shared_prefs；
        File file_xml = new File(path + "/shared_prefs");
        if (file_xml.exists()) {
            File[] files = file_xml.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
        //清空myShare截屏
        File file_shot = new File(context.getFilesDir().getPath()+"/myShare");
        if (file_shot.exists()) {
            File[] files = file_shot.listFiles();
            for (int i = 0; i < files.length; i++) {
                Log.e("filesDir目录",files[i]+"");
                files[i].delete();
            }
        }
        //清空缓存目录；
        File file_cache = context.getCacheDir();
        if (file_cache.exists()) {
            File[] files = file_cache.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }

        //清空file目录；
        File file_file = new File(path + "/files");;
        if (file_file.exists()) {
            File[] files = file_file.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }

        //清空数据库目录；
        File file_db = new File(path + "/databases");
        if (file_db.exists()) {
            File[] files = file_db.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
//        //这里可以重启你的应用程序，我的app中有service，所以我只要杀死进程就自动重启了。
//        android.os.Process.killProcess(android.os.Process.myPid());
    }
    //通过URL获取源码
    public static String getCodeString(String url) {
        StringBuffer urlString = null;//只有StringBuffer可以使用append
        InputStream inputStream = null;
        String line = "";
        try {
            URL url1 = new URL(url);
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                Log.e("bufferedReader", bufferedReader + "");
                while ((line = bufferedReader.readLine()) != null) {
                    urlString.append(line);
                }
//                Log.e("urlString", urlString + "");
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return urlString.toString();
    }

    public static List<Map<String, Object>> getCsdnNetDate(String url) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String csdnString = null;
        try {
            csdnString = doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.e("csdnString", csdnString);//其实不能完全打印
        //<li><a cnews_details_title="(.*?)" href="(.*?)" target="_blank" onclick="LogClickCountthis,363;">\1</a></li>
        //cnews_details_title="(.*?)" href="(.*?)".*?,363\)
        if (csdnString != null) {
            Pattern p = Pattern.compile("title=\"(.*?)\" href=\"(.*?)\".*?363");
//        Pattern p = Pattern.compile("class=\"link_title\"><a href=\"(.*?)\">");
            Matcher m = p.matcher(csdnString);

            while (m.find()) {
                MatchResult mr = m.toMatchResult();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("jk_title", mr.group(1));
                map.put("url", mr.group(2));
                result.add(map);
            }
            return result;
        }
        return null;
    }

    /**
     * get请求URL，失败时尝试三次
     *
     * @param url 请求网址
     * @return 网页内容的字符串
     * @author Lai Huan
     * @created 2013-6-20
     */
//    public static String http_get(String url) {
//        final int RETRY_TIME = 3;
//        HttpClient httpClient = null;
//        HttpGet httpGet = null;
//
//        String responseBody = "";
//        int time = 0;
//        do {
//            try {
//                httpClient = getHttpClient();
//                httpGet = new HttpGet(url);
//                HttpResponse response = null;
//                try {
//                    response = httpClient.execute(httpGet);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (response.getStatusLine().getStatusCode() == 200) {
//                    //用utf-8编码转化为字符串
//                    byte[] bResult = EntityUtils.toByteArray(response.getEntity());
//                    if (bResult != null) {
//                        responseBody = new String(bResult, "utf-8");
//                    }
//                }
//                break;
//            } catch (IOException e) {
//                time++;
//                if (time < RETRY_TIME) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e1) {
//                    }
//                    continue;
//                }
//                e.printStackTrace();
//            } finally {
//                httpClient = null;
//            }
//        } while (time < RETRY_TIME);
//
//        return responseBody;
//    }
//
//    public static HttpClient getHttpClient() {
//        HttpParams httpParams = new BasicHttpParams();
//        //设定连接超时和读取超时时间
//        HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
//        HttpConnectionParams.setSoTimeout(httpParams, 30000);
//        return new DefaultHttpClient(httpParams);
//    }
    //    public static void testUserHttpUnit() throws FailingHttpStatusCodeException,
//            MalformedURLException, IOException {
//
//        /** HtmlUnit请求web页面 */
//        WebClient wc = new WebClient(BrowserVersion.CHROME);
//        wc.getOptions().setUseInsecureSSL(true);
//        wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
//        wc.getOptions().setCssEnabled(false); // 禁用css支持
//        wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
//        wc.getOptions().setTimeout(100000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
//        wc.getOptions().setDoNotTrackEnabled(false);
//        HtmlPage page = wc
//                .getPage("http://www.cqupt.edu.cn/cqupt/news_detail.shtml?id=7745");
//
//        DomNodeList<DomElement> links = page.getElementsByTagName("p");
//
//        for (DomElement link : links) {
//            System.out
//                    .println(link.asText() + "  " + link.getAttribute("href"));
//        }
//    }
}
