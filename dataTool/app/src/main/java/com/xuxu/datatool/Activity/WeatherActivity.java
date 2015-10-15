package com.xuxu.datatool.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.xuxu.datatool.Model.AppStoreWeather;
import com.xuxu.datatool.Model.Weather;
import com.xuxu.datatool.R;
import com.xuxu.datatool.utils.ChineseUtil;
import com.xuxu.datatool.utils.NetWorkUtil;
import com.xuxu.datatool.utils.ShareUtil;
import com.xuxu.datatool.utils.WeatherData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/8/15.
 */
public class WeatherActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView cityName;
    private TextView wedec;
    private TextView temp;
    private TextView date;

    private ImageView imageView_next;
    private TextView cityName_next;
    private TextView wedec_next;
    private TextView temp_next;
    private TextView date_next;

    private ImageView imageView_over;
    private TextView cityName_over;
    private TextView wedec_over;
    private TextView temp_over;
    private TextView date_over;

    private TextView searchText;
    private EditText editText;
    private TextView loading;
    private ImageLoader imageLoader;
    private String located = "重庆";
    private String editPlace = "";
    private Weather today;
    private Weather tomorrow;
    private Weather tomorrow_over;
    private DisplayImageOptions options;
    private String cityName_last;

    private TextView cityName_store;
    private TextView weather_store;
    private TextView temperature_store;
    private TextView time_store;
    private TextView wind_store;
    private TextView height_store;
    private TextView sun_store;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appstore_weather);

        linearLayout = (LinearLayout) findViewById(R.id.main_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("天气");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintResource(R.color.red);
            tintManager.setStatusBarTintEnabled(true);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            linearLayout.setPadding(0, config.getStatusBarHeight(), 0, config.getPixelInsetBottom());
        }
        initViewTwo();
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPlace = "";
                //排除找不到城市，程序死掉BUG，如何检索不到新网址，网址不变，并且告知用户网址没变，没有找到所要城市
                //关闭输入法
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                editPlace = editText.getText().toString();
                char[] length = editPlace.toCharArray();
                if (length.length == 0) {
                    Toast.makeText(app.getContext(), "请输入城市名", Toast.LENGTH_SHORT).show();
                    cityName_last = ChineseUtil.getCityName(app.getContext());
                    new AppStoreWeatherAsyncTask().execute();
                } else if (length.length == 1) {
                    Toast.makeText(app.getContext(), "没有城市名为一个字", Toast.LENGTH_SHORT).show();
                }else {
                    cityName_last = editPlace;
                    loading.setVisibility(View.VISIBLE);

                    new AppStoreWeatherAsyncTask().execute();
                }
            }
        });
        //调用enter键
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editPlace = "";
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                editPlace = editText.getText().toString();
                char[] length = editPlace.toCharArray();
                if (length.length == 0) {
                    Toast.makeText(app.getContext(), "请输入城市名", Toast.LENGTH_SHORT).show();
                    if(ChineseUtil.getCityName(app.getContext()).equals("")) {
                        cityName_last = "重庆";
                    }else{
                        cityName_last = ChineseUtil.getCityName(app.getContext());
                    }
                    new AppStoreWeatherAsyncTask().execute();
                } else if (length.length == 1) {
                    Toast.makeText(app.getContext(), "没有城市名为一个字", Toast.LENGTH_SHORT).show();
                } else {

                    loading.setVisibility(View.VISIBLE);
                    cityName_last = editPlace;

                    new AppStoreWeatherAsyncTask().execute();
                }
                return false;
            }
        });
        if(ChineseUtil.getCityName(app.getContext()).equals("")){
            Toast.makeText(app.getContext(), "定位失败", Toast.LENGTH_SHORT).show();
            cityName_last = "重庆";
        }else{
            cityName_last = ChineseUtil.getCityName(app.getContext());
            Toast.makeText(app.getContext(), "定位中", Toast.LENGTH_SHORT).show();
        }
        new AppStoreWeatherAsyncTask().execute();
    }
    private void initViewTwo() {
        searchText = (TextView) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.edit_query);
        loading = (TextView) findViewById(R.id.weather_loading);
        loading.setVisibility(View.VISIBLE);
        cityName_store = (TextView) findViewById(R.id.city_name);
        weather_store = (TextView) findViewById(R.id.weather_name);
        temperature_store = (TextView) findViewById(R.id.temperature);
        time_store = (TextView) findViewById(R.id.time);
        wind_store = (TextView) findViewById(R.id.wind);
        height_store = (TextView) findViewById(R.id.altitude);
        sun_store = (TextView) findViewById(R.id.sun);
    }
    private void initDataTwo(AppStoreWeather appStoreWeather){
        Time time=new Time();
        time.setToNow();
        int hour = time.hour;
        int minute = time.minute;
        int second = time.second;
        SimpleDateFormat format=new SimpleDateFormat("MM月dd日 HH:mm");
        Date curDate=new Date();
        String str=format.format(curDate);
//        String t =(time.month+1)+"-"+time.monthDay+" "+time.hour+":"+time.minute+"更新~";
        String t =str+"更新~";
        cityName_store.setText(appStoreWeather.getCityName());
        weather_store.setText(appStoreWeather.getWeather());
        temperature_store.setText(appStoreWeather.getTemperature());
        time_store.setText(t);
        wind_store.setText(appStoreWeather.getWind());
        height_store.setText(appStoreWeather.getLongitude_latitude()+" "+appStoreWeather.getAltitude());
        sun_store.setText(appStoreWeather.getSun());
        loading.setVisibility(View.GONE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_share) {
            try {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String imgPath = ShareUtil.saveToSD(this);
//                Log.e("最后要分享的图片路径",imgPath);
                ShareUtil.sharePicture(this, imgPath, "天气");
                return true;//注意此处需要返回TRUE,还有因为没有添加读写SD卡权限导致无法分享
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    class AppStoreWeatherAsyncTask extends AsyncTask<String, Void, AppStoreWeather> {

        @Override
        protected AppStoreWeather doInBackground(String... strings) {
            AppStoreWeather appStoreWeather = null;
            if (NetWorkUtil.isNetWorkConnected(WeatherActivity.this)) {
//                String city = "武汉";
                appStoreWeather = WeatherData.getOneWeather(cityName_last);
            }
            return appStoreWeather;
        }

        @Override
        protected void onPostExecute(AppStoreWeather appStoreWeather) {
            if(appStoreWeather!=null){
                initDataTwo(appStoreWeather);
                editText.setText("");
            }
            else {
                editText.setText("");
                Toast.makeText(app.getContext(), "对不起，你的城市没有数据！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class WeatherAsyncTask extends AsyncTask<String, Void, List<Weather>> {

        @Override
        protected List<Weather> doInBackground(String... strings) {
            //不能再其他线程使用toast
            List<Weather> weatherList = null;
            if (NetWorkUtil.isNetWorkConnected(WeatherActivity.this)) {
                String city = "武汉";

                WeatherData.getOneWeather(city);
//                weatherList = new ArrayList<>();
//                //只是城市名没变，天气已经改变
//                weatherList.clear();
//                weatherList = WeatherData.getWeatherData(editPlace, WeatherActivity.this);
                //最终获取的数据会动态更新，到了傍晚就没有白天数据，只有晚上的数据
                //缺少温度数据，由于解析数据时获取图片数据方法不对，应该直接获取数组第二个，温度是数组第三个
//                    Log.e("最终获取的天气数据", weather.toString());

            }
            return weatherList;
        }

        @Override
        protected void onPostExecute(List<Weather> weathers) {
            super.onPostExecute(weathers);
            if (weathers != null && weathers.size() > 0) {
                initData(weathers);
            } else {
                Toast.makeText(app.getContext(), "对不起，你的城市没有数据！", Toast.LENGTH_SHORT).show();
            }
            char[] length = editPlace.toCharArray();
            if (length.length == 0) {

            } else if (length.length == 1) {
                Toast.makeText(app.getContext(), "没有城市名为一个字", Toast.LENGTH_SHORT).show();
            } else if (today.getWeatherCityName().equals("重庆")) {
                Toast.makeText(app.getContext(), "对不起，你的城市没有数据！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        int loadingResource = R.drawable.w1;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(loadingResource)
                .build();

        imageView = (ImageView) findViewById(R.id.weather_icon);
        cityName = (TextView) findViewById(R.id.city_name);
        wedec = (TextView) findViewById(R.id.weather_name);
        temp = (TextView) findViewById(R.id.temperature);
        date = (TextView) findViewById(R.id.time);

        imageView_next = (ImageView) findViewById(R.id.next_weather_icon);
//        cityName_next = (TextView) findViewById(R.id.next_city_name);
        wedec_next = (TextView) findViewById(R.id.next_weather_name);
        temp_next = (TextView) findViewById(R.id.next_temperature);
        date_next = (TextView) findViewById(R.id.next_time);

        imageView_over = (ImageView) findViewById(R.id.over_weather_icon);
//        cityName = (TextView) findViewById(R.id.city_name);
        wedec_over = (TextView) findViewById(R.id.over_weather_name);
        temp_over = (TextView) findViewById(R.id.over_temperature);
        date_over = (TextView) findViewById(R.id.over_time);

        searchText = (TextView) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.edit_query);
        loading = (TextView) findViewById(R.id.weather_loading);
        loading.setVisibility(View.VISIBLE);
    }

    private void initData(List<Weather> list) {

        today = list.get(0);
        cityName.setText(today.getWeatherCityName());
        temp.setText(today.getTemp());
        wedec.setText(today.getWdesc());
        date.setText(today.getDate());
        imageLoader.displayImage(today.getWeatherIcon(), imageView, options);
        Log.e("缓存", imageLoader.getMemoryCache() + "");

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("imgUrl", today.getWeatherIcon());
        setResult(RESULT_OK, intent);

        tomorrow = list.get(1);
//        cityName.setText(tomorrow.getWeatherCityName());
        temp_next.setText(tomorrow.getTemp());
        wedec_next.setText(tomorrow.getWdesc());
        date_next.setText(tomorrow.getDate());
        imageLoader.displayImage(tomorrow.getWeatherIcon(), imageView_next, options);

        tomorrow_over = list.get(2);
//        cityName_over.setText(today.getWeatherCityName());
        temp_over.setText(tomorrow_over.getTemp());
        wedec_over.setText(tomorrow_over.getWdesc());
        date_over.setText(tomorrow_over.getDate());
        imageLoader.displayImage(tomorrow_over.getWeatherIcon(), imageView_over, options);

        imageLoader.displayImage(today.getWeatherIcon(), imageView, options);
        loading.setVisibility(View.INVISIBLE);
    }

}
