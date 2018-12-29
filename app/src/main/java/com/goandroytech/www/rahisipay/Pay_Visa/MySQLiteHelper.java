package com.goandroytech.www.rahisipay.Pay_Visa;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by michael.nkotagu on 8/3/2015.
 */


    public class MySQLiteHelper extends SQLiteOpenHelper {

       private static final String DATABASE_NAME = "tpb_live.db";

       private static final int DATABASE_VERSION = 1;

       private static final String CREATE_LOOK_UP="CREATE TABLE tbl_look_up (id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(60));";

       private static final String CREATE_BANKS="CREATE TABLE tbl_banks (id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(60), bin VARCHAR(30) not null unique,identity VARCHAR(5) not null);";

       private static final String CREATE_ACCOUNT="CREATE TABLE tbl_account (id INTEGER PRIMARY KEY AUTOINCREMENT,account VARCHAR(30) not null unique,pan VARCHAR(30));";

       private static final String CREATE_ALIAS="CREATE TABLE tbl_alias (id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(30),phone_number VARCHAR(60));";

       private static final String CREATE_LINKED_ACCOUNT="CREATE TABLE tbl_consumer_alias (id INTEGER PRIMARY KEY AUTOINCREMENT,phone_number VARCHAR(30) not null unique,card_number VARCHAR(30) not null unique,f_name VARCHAR(100),m_name VARCHAR(100),l_name VARCHAR(100));";

        private static final String CREATE_TRANSACTION_HISTORY="CREATE TABLE tbl_transaction_history (id INTEGER PRIMARY KEY AUTOINCREMENT, amount VARCHAR(30), agent_name VARCHAR(60), transaction_time VARCHAR(60),agent_pan VARCHAR(60),location VARCHAR(60),approval_code VARCHAR(60),ret_no VARCHAR(60),sender_account VARCHAR(60),payment_type VARCHAR(60), status VARCHAR(4),foreign_amount VARCHAR(30),special_id VARCHAR(30));";

        private static final String CREATE_TABLE_CARD_DETAILS="CREATE TABLE tbl_card_details (id INTEGER PRIMARY KEY AUTOINCREMENT,payeeName VARCHAR(255), payeeCardNumber VARCHAR(30))";

        private static final String CREATE_TABLE_TRX      ="CREATE TABLE tbl_municipal_trx_types (_id INTEGER PRIMARY KEY AUTOINCREMENT,pay_code VARCHAR(10),name VARCHAR(255),pay_type VARCHAR(2),amount INTEGER,amount_alt INTEGER);";

        private static final String CREATE_TABLE_TRX_LOGS ="CREATE TABLE tbl_municipal_trx_logs  (_id INTEGER PRIMARY KEY AUTOINCREMENT,field_3 VARCHAR(6),field_4 VARCHAR(12),field_7 VARCHAR(10),field_11 VARCHAR(4),field_37 VARCHAR(100),field_39 VARCHAR(2),field_43 VARCHAR(50),field_48 VARCHAR(255),field_58 varchar(20),field_61 VARCHAR(20),field_62 VARCHAR(4),field_102 VARCHAR(20),record_time DATETIME DEFAULT CURRENT_TIMESTAMP,recon_status CHAR(1),field_103 VARCHAR(20), field_nfc VARCHAR(15));";

        private Context context;

        private static MySQLiteHelper sInstance;

        public MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        public static synchronized MySQLiteHelper getInstance(Context context) {

            // Use the application context, which will ensure that you
            if (sInstance == null)
            {
                if(context!=null)
                {
                    sInstance = new MySQLiteHelper(context.getApplicationContext());
                }
            }
            return sInstance;
        }

        public void onCreate(SQLiteDatabase db) {
            //Create Database
            try
            {
                db.execSQL(CREATE_BANKS);
                db.execSQL(CREATE_TABLE_TRX);
                db.execSQL(CREATE_TABLE_TRX_LOGS);
                db.execSQL(CREATE_TABLE_CARD_DETAILS);
                db.execSQL(CREATE_TRANSACTION_HISTORY);
                db.execSQL(CREATE_ALIAS);
                db.execSQL(CREATE_ACCOUNT);
                db.execSQL(CREATE_LINKED_ACCOUNT);
                db.execSQL(CREATE_LOOK_UP);
//                Toast.makeText(context, "DB Created", Toast.LENGTH_LONG).show();
            }
            catch (SQLException e)
            {
//                Toast.makeText(context, "DB Create Failed"+e.getMessage(), Toast.LENGTH_LONG).show();
                Log.v("DBActivity",e.getMessage());
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Create Database
            try {


                //db=this.getWritableDatabase();
//                db.execSQL(CREATE_BANKS);
                //db.execSQL(DROP_TABLE_TRX_LOGS);
                //onCreate(db);
                //String upgradeQuery = "ALTER TABLE tbl_municipal_trx_types ADD COLUMN amount_alt INTEGER";
//                db.execSQL(CREATE_LOOK_UP);
                //upgradeQuery = "ALTER TABLE tbl_municipal_trx_types ADD COLUMN use_count INTEGER";


//                Toast.makeText(context, "DB Upgraded", Toast.LENGTH_LONG).show();

            } catch (SQLException e) {
//                Toast.makeText(context, "DB Upgrade Failed", Toast.LENGTH_LONG).show();
            }
        }



    }


