package com.xuxu.datatool.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import com.xuxu.datatool.Model.GeekNews;
import com.xuxu.datatool.R;
import com.xuxu.datatool.utils.GetData;
import com.xuxu.datatool.utils.NetWorkUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/8/25.
 */
public class DesActivity extends Activity {
    private TextView textView;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.des_aty);
//        if(NetWorkUtil.isNetWorkConnected(this)) {
//            new DesAsyncTask().execute();
//        }
    }
    class DesAsyncTask extends AsyncTask<String,Void,List<GeekNews>>{

        @Override
        protected List<GeekNews> doInBackground(String... strings) {
            List<GeekNews> FirstList = null;
            if (NetWorkUtil.isNetWorkConnected(DesActivity.this)) {
                FirstList = GetData.getGeekFirst();
            }
            return FirstList;
        }

        @Override
        protected void onPostExecute(List<GeekNews> geekNewses) {
            super.onPostExecute(geekNewses);
            if (geekNewses!=null) {
                Log.e("桌面获得数据",geekNewses.get(0).getJk_title());
                textView.setText(geekNewses.get(0).getJk_title());
            }
        }
    }
}
