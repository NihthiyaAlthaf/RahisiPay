package com.goandroytech.www.rahisipay.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "SERVICES";

    // Table columns
    public static final String _ID = "_id";
    public static final String SERVICE_ID = "service_id";
    public static final String SERVICE_NAME = "service_name";
    public static final String PARENT = "parent";
    public static final String LOGO = "logo";
    public static final String SUBSCRIBED = "subscribed";
    public static final String SUBSCRIPTIONACCOUNT = "subscriptionAccount";
    public static final String IMAGE_URL = "url";

    // Database Information
    static final String DB_NAME = "RAHISI.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SERVICE_ID + " TEXT, "+ SERVICE_NAME + " TEXT, " +
            PARENT + "TEXT, " + LOGO + " TEXT, " + SUBSCRIBED + " TEXT, " + SUBSCRIPTIONACCOUNT + " TEXT, " + IMAGE_URL
            + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}