package com.xuxu.datatool.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.xuxu.datatool.Activity.DesActivity;
import com.xuxu.datatool.Activity.DesService;

/**
 * Created by Administrator on 2015/8/24.
 */
public class MyReceiver extends BroadcastReceiver {
    protected NetworkInfo mobileInfo;
    protected NetworkInfo wifiInfo;
    protected NetworkInfo activeInfo;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        activeInfo = manager.getActiveNetworkInfo();
        Time time=new Time();
        time.setToNow();
        int hour = time.hour;
        int minute = time.minute;
        int second = time.second;
        String t =String.valueOf(time.hour) + String.valueOf(time.minute)+"";
        Intent intent1 = new Intent(context,DesService.class);
        context.startService(intent1);
    }
}