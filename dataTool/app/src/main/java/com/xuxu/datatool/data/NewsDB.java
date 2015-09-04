package com.xuxu.datatool.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/8/19.
 */
public class NewsDB extends SQLiteOpenHelper{
    public  static final String  TABLE_NAME="news";
    public  static final String  CONTENT="cnews_details_title";
    public  static final String  MURL="cnews_details_url";
    public  static final String  VIDEO="video";
    public  static final String  ID="_id";
    public  static final String  TIME="cnews_details_time";
    public NewsDB(Context context) {
        super(context, "news", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE news(_id INTEGER PRIMARY KEY AUTOINCREMENT,cnews_details_title text not null," +
                "cnews_details_url text not null,cnews_details_time text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
