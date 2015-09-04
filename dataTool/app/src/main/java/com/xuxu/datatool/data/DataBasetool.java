package com.xuxu.datatool.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.xuxu.datatool.Activity.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/24.
 */
public class DataBasetool {
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
//    private String cnews_details_title;
//    private String url;
    private Context context;
    private SQLiteDatabase dbReader;

    //    notesDB = new NotesDB(this);
//    Log.e("notesDB", "数据库建立成功");
//    dbWriter = notesDB.getWritableDatabase();
    public DataBasetool(Context context) {
        this.context = context;
//        this.cnews_details_title = cnews_details_title;
//        this.url = url;
        notesDB = new NotesDB(this.context);
        dbWriter = notesDB.getWritableDatabase();
        dbReader = notesDB.getReadableDatabase();
    }

    public void addDB(String title,String url) {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, title);
//        Log.e("输入", title + url);
        cv.put(NotesDB.MURL, url);//此处为notesDB还是NotesDB???
//        cv.put(NotesDB.PATH, );
//        cv.put(NotesDB.VIDEO, );
//        cv.put(NotesDB.PATH,imageFile+"");
//        Log.e("图片途径",imageFile+"");
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    public List<Map<String, Object>> selectDBAll() {
        Cursor cursor = dbReader.query(NotesDB.TABLE_NAME, null, null,
                null, null, null, null);

//        Log.e("cursor", cursor + "");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {//不能读取第0,1行
//            Log.e("count", cursor.getColumnCount() + "");
            String title = cursor.getString(cursor.getColumnIndex("jk_title"));
//            Log.e("cnews_details_title", cnews_details_title);
            String url = cursor.getString(cursor.getColumnIndex("murl"));
//            Log.e("murl", url);
//            list = 此句一定不能在此处，不然只会读取到最后一条数据
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("jk_title", title);
            map.put("url", url);
            list.add(map);
        }
//        Log.e("cursorList", list + "");
        return list;
    }

    public List<Map<String, Object>> selectDB10() {
        Cursor cursor = dbReader.rawQuery("select * from notes order by _id desc limit 11 ",null);
//        Log.e("cursor", cursor + "");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (cursor.getCount()!=0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {//不能读取第0,1行
    //            Log.e("count", cursor.getColumnCount() + "");
                String title = cursor.getString(cursor.getColumnIndex("jk_title"));
    //            Log.e("cnews_details_title", cnews_details_title);
                String url = cursor.getString(cursor.getColumnIndex("murl"));
    //            Log.e("murl", url);
    //            list = 此句一定不能在此处，不然只会读取到最后一条数据
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("jk_title", title);
                map.put("url", url);
                list.add(map);
            }
//            Log.e("cursorListd888888", list + "");
        }
        return list;
    }
    public List<Map<String, Object>> selectDB9(int i) {
        Cursor cursor = dbReader.rawQuery("select * from notes order by _id desc limit 9 offset "+i,null);
        Log.e("cursor", cursor + "");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {//不能读取第0,1行
//            Log.e("count", cursor.getColumnCount() + "");
            String title = cursor.getString(cursor.getColumnIndex("jk_title"));
//            Log.e("cnews_details_title", cnews_details_title);
            String url = cursor.getString(cursor.getColumnIndex("murl"));
//            Log.e("murl", url);
//            list = 此句一定不能在此处，不然只会读取到最后一条数据
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("jk_title", title);
            map.put("url", url);
            list.add(map);
        }
        Log.e("cursorListd888888", list + "");
        return list;
    }
    public void deleteAll() {
        Cursor myCursor = dbReader.rawQuery("select * from notes", null);
        if(myCursor.moveToFirst()){
            String SQL = "delete from notes ";
            dbReader.execSQL(SQL);
        } else {
            Toast.makeText(app.getContext(), "没有什么可以删了啦", Toast.LENGTH_SHORT).show();
        }

    }
    public  void delete(String title) {
        String SQL = "delete from notes where jk_title ='" + title + "'";
        Log.e("SQL", SQL);
        dbReader.execSQL(SQL);
    }
}
