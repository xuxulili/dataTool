package com.xuxu.datatool.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.xuxu.datatool.Model.GeekNews;
import com.xuxu.datatool.R;
import com.xuxu.datatool.utils.GetData;
import com.xuxu.datatool.utils.NetWorkUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/8/25.
 */
public class DesService extends Service {
    int id = 0;
    int times = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("服务启动", "");
        new DesAsyncTask().execute();
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("服务启动", "");
        super.onStart(intent, startId);
    }

    private void sendNotification(String title, String content,String url) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, WebViewActivity.class);
        Bundle bundle = new Bundle();
        Log.e("",url);
        bundle.putString("getUrl",url);
        bundle.putString("url",url);
        bundle.putString("getTitle",title);
        intent.putExtras(bundle);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);//需要传值的话 第二参数为ID，第四参数需要设置
        Notification notify2 = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.datatool3) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                        // icon)
                .setTicker(title)// 设置在status
                        // bar上显示的提示文字
                .setContentTitle(title)// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText(content)// TextView中显示的详细内容
                .setContentIntent(pendingIntent2) // 关联PendingIntent
                .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify2.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(1, notify2);
    }

    class DesAsyncTask extends AsyncTask<String, Void, List<GeekNews>> {

        @Override
        protected List<GeekNews> doInBackground(String... strings) {
            List<GeekNews> FirstList = null;
            if (NetWorkUtil.isNetWorkConnected(app.getContext())) {
                FirstList = GetData.getGeekFirst();
            }
            return FirstList;
        }

        @Override
        protected void onPostExecute(List<GeekNews> geekNewses) {
            super.onPostExecute(geekNewses);
            if (geekNewses != null) {
                Log.e("桌面获得数据", geekNewses.get(0).getJk_title());
                sendNotification(geekNewses.get(0).getJk_title(), geekNewses.get(0).getJk_label(),geekNewses.get(0).getJk_url());
            } else {
                if (times <= 5) {
                    times++;
                    new DesAsyncTask().execute();
                }
            }
        }
    }
}
