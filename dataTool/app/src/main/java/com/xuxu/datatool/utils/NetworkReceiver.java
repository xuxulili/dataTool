package com.xuxu.datatool.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2015/8/26.
 */
public class NetworkReceiver  extends BroadcastReceiver{
    protected NetworkInfo mobileInfo;
    protected NetworkInfo wifiInfo;
    protected NetworkInfo activeInfo;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        activeInfo = manager.getActiveNetworkInfo();
    }
}
