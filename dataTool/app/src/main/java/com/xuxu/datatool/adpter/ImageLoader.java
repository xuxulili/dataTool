package com.xuxu.datatool.adpter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.xuxu.datatool.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2015/6/9.
 */
public class ImageLoader {
    private ImageView mImageView;//传入的 所要操作的图片视图
    private String mUrl;//与之对应的网址标示
    private LruCache<String, Bitmap> mCache;//缓存队列
    private Set<NewsAsyncTask> mTask;

    //初始化传入的全局变量
    public ImageLoader(String  url,ImageView imageView) {
        this.mUrl=url;
        this.mImageView=imageView;
        mTask = new HashSet<>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        new NewsAsyncTask().execute(mUrl);
        int cashSize = maxMemory / 4;
        mCache = new LruCache<String, Bitmap>(cashSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();//每次存入缓存调用，返回BitMap大小
            }
        };
    }

    //把bitmap传入缓存
    public void addBitmapToCash(String url, Bitmap bitmap) {
        if (getBitmapFromCash(url) == null) {
            mCache.put(url, bitmap);
        }
    }

    //从缓存中取出bitmap
    public Bitmap getBitmapFromCash(String url) {
        return mCache.get(url);
    }

    //通过urlString回去图片Bitmap教程
    public Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(inputStream);
            httpURLConnection.disconnect();
            return bitmap;//少了此句，导致默认图标不显示，显示空白；
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
//    加载start到end数据
    public void loadImages(int start,int end){
        for(int i=start;i<end;i++) {
            String url = mUrl;
            Bitmap bitmap = getBitmapFromCash(url);
            if (bitmap == null) {
                NewsAsyncTask task = new NewsAsyncTask();//不再需要传递imageview
                task.execute(url);
                mTask.add(task);
            } else {

                mImageView.setImageBitmap(bitmap);
            }
        }
    }

    /*
    AsyncTask方式更新ImageView
    * */
    public void showImageByAsyncTask() {
        Bitmap bitmap =null ;
        if (bitmap == null) {
            mImageView.setImageResource(R.mipmap.ic_launcher);//调用excute方法更新ListView,只是设置默认图标，显示图标控制权移动到滚动事件
        } else {
            mImageView.setImageBitmap(bitmap);
        }
    }
//取消mTask中的所有工作
    public void cancelAllTask() {
        if (mTask!=null) {
            for(NewsAsyncTask task:mTask) {
                task.cancel(false);
            }
        }
    }

    //新建NewAsyncTask类
    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
//        private ImageView mImageView;


        @Override//
//        获取数据并进行存入缓存操作
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = getBitmapFromURL(strings[0]);
            if (bitmap != null) {
                addBitmapToCash(url, bitmap);
            }
            return bitmap;
        }

        @Override
//        把获得bitmap更新至mImageView
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.e("bitmap",bitmap+"");
            mImageView.setImageBitmap(bitmap);
            mTask.remove(this);
//            if (mImageView.getTag().equals(mUrl)) {
//                mImageView.setImageBitmap(bitmap);
//            }
        }
    }

    /*
* Thread方式
* */
    //Handler使用教程
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl)) {//判断匹配
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    //通过多线程方式更新imageView
    public void showImageByThread(ImageView imageView, final String url) {
        //保存数据
        mUrl = url;
        mImageView = imageView;
        //新建线程
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = getBitmapFromCash(url);
                System.out.println(bitmap);
                if (bitmap == null) {
                    bitmap = getBitmapFromURL(url);
                    addBitmapToCash(url, bitmap);
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    mHandler.sendMessage(message);
                } else {
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    mHandler.sendMessage(message);
                }
                //由于其他线程不能操作UI，所以只有利用Handler来操作UI，绑定到Message
                //Message属性设置
            }
        }.start();
    }
}

