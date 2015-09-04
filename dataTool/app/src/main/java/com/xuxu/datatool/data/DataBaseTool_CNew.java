package com.xuxu.datatool.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xuxu.datatool.Model.CNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/19.
 */
public class DataBaseTool_CNew {
    private NewsDB newsDB;
    private SQLiteDatabase dbWriter;
    private SQLiteDatabase dbReader;
    private Context context;

    public DataBaseTool_CNew(Context context) {
        this.context = context;
        newsDB = new NewsDB(context);
        dbReader = newsDB.getReadableDatabase();
        dbWriter = newsDB.getWritableDatabase();
    }

    public void addToNews(CNews cNews) {
        ContentValues cv = new ContentValues();
        cv.put(NewsDB.CONTENT, cNews.getcTitle());
        cv.put(NewsDB.MURL, cNews.getcUrl());
        cv.put(NewsDB.TIME, cNews.getcTime());
        dbWriter.insert(NewsDB.TABLE_NAME, null, cv);
//        Log.e("本次插入news表数据", cNews.getcTitle());
    }

    public List<CNews> selectAll() {
        String SQL = "select * from news";
        Cursor cursor_new = dbReader.rawQuery(SQL, null);
        List<CNews> cNewses = null;
        if (cursor_new.moveToFirst()) {
            cNewses = new ArrayList<>();
            for (cursor_new.moveToLast(); !cursor_new.isBeforeFirst(); cursor_new.moveToPrevious()) {
                CNews cNews = new CNews();
                cNews.setcUrl(cursor_new.getString(cursor_new.getColumnIndex("cnews_details_url")));
                cNews.setcTitle(cursor_new.getString(cursor_new.getColumnIndex("cnews_details_title")));
                cNews.setcTime(cursor_new.getString(cursor_new.getColumnIndex("cnews_details_time")));
                cNewses.add(cNews);
            }
        }
        return cNewses;
    }
    public List<CNews> selectEight() {
        String SQL = "select * from news order by _id desc limit 10 offset 0";
        Cursor cursor_new = dbReader.rawQuery(SQL, null);
        List<CNews> cNewses = null;
        if (cursor_new.moveToFirst()) {
            cNewses = new ArrayList<>();
//            for (cursor_new.moveToLast(); !cursor_new.isBeforeFirst(); cursor_new.moveToPrevious()) {
            for (cursor_new.moveToFirst(); !cursor_new.isAfterLast(); cursor_new.moveToNext()) {
                CNews cNews = new CNews();
                cNews.setcUrl(cursor_new.getString(cursor_new.getColumnIndex("cnews_details_url")));
                cNews.setcTitle(cursor_new.getString(cursor_new.getColumnIndex("cnews_details_title")));
                cNews.setcTime(cursor_new.getString(cursor_new.getColumnIndex("cnews_details_time")));
                cNewses.add(cNews);
            }
        }
        return cNewses;
    }

    public void delete(String title) {
        String SQL = "select cnews_details_title from news where cnews_details_title= " + title;
        String SQLDelete = "delete from news where cnews_details_title="+title;
        Cursor cursor = dbReader.rawQuery(SQL, null);
        if (cursor.moveToFirst()) {
            dbWriter.execSQL(SQLDelete);
        }
    }


    public void clear() {
        String SQL = "select * from news";
        String SQLDelete = "delete from news";
        Cursor cursor = dbReader.rawQuery(SQL, null);
        if (cursor.moveToFirst()) {
            dbWriter.execSQL(SQLDelete);
        }
    }
}

