package com.goandroytech.www.rahisipay.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public void insert(String service_id, String service_name, String parent, String logo, String subscribed, String subscriptionaccount, String url) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SERVICE_ID, service_id);
        contentValue.put(DatabaseHelper.SERVICE_NAME, service_name);
        contentValue.put(DatabaseHelper.PARENT, parent);
        contentValue.put(DatabaseHelper.LOGO, logo);
        contentValue.put(DatabaseHelper.SUBSCRIBED, subscribed);
        contentValue.put(DatabaseHelper.SUBSCRIPTIONACCOUNT, subscriptionaccount);
        contentValue.put(DatabaseHelper.IMAGE_URL, url);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper.SERVICE_ID, DatabaseHelper.SERVICE_NAME, DatabaseHelper.PARENT,
        DatabaseHelper.LOGO, DatabaseHelper.SUBSCRIBED, DatabaseHelper.SUBSCRIPTIONACCOUNT, DatabaseHelper.IMAGE_URL};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
//
//    public int update(long _id, String name, String desc) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper.SUBJECT, name);
//        contentValues.put(DatabaseHelper.DESC, desc);
//        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
//        return i;
//    }
//
//    public void delete(long _id) {
//        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
//    }

}