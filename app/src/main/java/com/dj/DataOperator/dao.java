package com.dj.DataOperator;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Administrator on 2018/5/3.
 */

public class dao {
    SQLiteDatabase db = null;

    public dao(Activity activity, String dbpath){
        MyDatabaseHelper dbhelper = new MyDatabaseHelper(activity, dbpath, null, 1);
        this.db = dbhelper.getWritableDatabase();
    }
    /**
     * 插入数据
     */
    public void insertData(){
        try{
            ContentValues values = new ContentValues();

            values.put("author", "吴承恩");
            values.put("price", 45);
            values.put("pages", 562);
            values.put("name", "西游记");
            db.insert("book", null, values);
        }catch (Exception e){
            Log.i("dj", e.toString());
        }

    }

    /**
     * 查询数据
     */
    public void queryData(){
        Cursor cursor = db.query("book", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getInt(cursor.getColumnIndex("price"));
                Log.i("dj", name + ",,," + author + ",,," +  pages + ",,," + price);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

}
