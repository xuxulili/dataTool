package com.xuxu.datatool.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.xuxu.datatool.Model.Weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/15.
 */
public class WeatherData {
    String url = "http://www.nmc.cn/publish/forecast/ACQ/zhongqing.html";

    public static List<Weather> getWeatherData(String editPlace, Context context) {

        String url = "http://www.nmc.cn/publish/forecast/ACQ/zhongqing.html";
        String cityName = ChineseUtil.getCityName(context);
        if(cityName==null) {
            cityName = "重庆";
        }
        char[] length = editPlace.toCharArray();
        if (!editPlace.equals(cityName)) {
            if (!editPlace.equals("")) {
                if (editPlace.equals("成都")) {
                    url = "http://www.nmc.cn/publish/forecast/ASC/chengdu.html";
                } else if (editPlace.equals("长沙")) {
                    url = "http://www.nmc.cn/publish/forecast/AHN/changsha.html";
                } else if (editPlace.equals("长春")) {
                    url = "http://www.nmc.cn/publish/forecast/AHN/changchun.html";
                } else if (length.length <= 1) {
                    url = "http://www.nmc.cn/publish/forecast/ACQ/zhongqing.html";
                } else {
                    url = GetData.getWeatherUrl(editPlace, context);
                }
            }
            else {
                url = GetData.getWeatherUrl(cityName, context);
//                Log.e("初始化定位城市网址",url);
            }
        }

        if (!url.equals(GetData.getWeatherUrl(cityName, context))) {
            cityName = editPlace;
        }

        List<Weather> weatherList = new ArrayList<>();
        try {
            Document document = null;
            document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("today");
            for (int i = 0; i <= 2; i++) {
                Element element = elements.get(i);
                Weather weather = new Weather();
//                Log.e("获取的一天天气", element.toString());
                Elements elements_tr = element.select("tr");
                String date = elements_tr.get(0).text();
                String wdesc = elements_tr.get(2).text();
                String temp = elements_tr.get(3).text();
//                Log.e("temp", temp);
                weather.setWeatherCityName(cityName);//注意此处需要设置传入的城市名
                weather.setDate(date);
                weather.setWdesc(wdesc);
                weather.setTemp(temp);
                if (elements_tr.get(1).select("img").size() != 0) {
                    Element element_im = elements_tr.get(1).select("img").get(0);
//                    Log.e("其他解析方式获取的img", element_im.attr("src"));
                    String img = "http://www.nmc.cn" + element_im.attr("src");
//                    Log.e("获取的图片资源", img);
                    weather.setWeatherIcon(img);
                }

//                for(Element element_td:elements_tr) {
//                    Log.e("获取的详细数据",element_td.toString());
//                    if (element_td.getElementsByTag("img").size() != 0) {//注意此句多次嵌套出现打印错误，
//                        //判断获取的elements大小知道是否获取到资源
//
//                        String img = "http://www.nmc.cn" + element_td.select("img").get(0).attr("src");
//                        Log.e("获取的图片资源",img);
//                        weather.setWeatherIcon(img);
////                        String img = element_td.getElementsByTag("img").get(0).attr("src");
////                        Log.e("获取的图片资源",img);
//                    }
//                }
                weatherList.add(weather);

            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return weatherList;
    }
}
