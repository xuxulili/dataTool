package com.xuxu.datatool.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.xuxu.datatool.R;

/**
 * Created by Administrator on 2015/8/28.
 */
public class JobActivity extends SwipeBackActivity {
    private Toolbar toolbar_job;
    private ListView listView_job;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_aty);
    }
    private void  initView() {
        toolbar_job = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_job);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_job.setTitle("重邮招聘");

        listView_job = (ListView) findViewById(R.id.listView_job);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
