package com.xuxu.datatool.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/6/19.
 */



public class NotesDB extends SQLiteOpenHelper {
    public  static final String  TABLE_NAME="notes";
    public  static final String  CONTENT="jk_title";
    public  static final String  MURL="murl";
    public  static final String  VIDEO="video";
    public  static final String  ID="_id";
    public  static final String  TIME="time";
    public NotesDB(Context context) {
        super(context, "notes", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +CONTENT+" TEXT NOT NULL,"+MURL+" TEXT NOT NULL)");//每个语句前面必须有空格！！！！！
    }//插入的数据开始和末尾必须有数据
//+PATH+" TEXT NOT NULL,"+VIDEO+" TEXT NOT NULL,"
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
