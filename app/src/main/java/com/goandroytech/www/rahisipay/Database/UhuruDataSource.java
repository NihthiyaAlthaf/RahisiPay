package com.goandroytech.www.rahisipay.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.Log;

import com.goandroytech.www.rahisipay.Model.Msg;
import com.goandroytech.www.rahisipay.R;
import com.visa.mvisa.sdk.models.facade.CardDetails;
import com.visa.mvisa.sdk.models.facade.Payee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.database.DatabaseUtils.longForQuery;

/**
 * Created by michael.nkotagu on 8/19/2015.
 */
public class UhuruDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper uhuruHelper;

    private Cursor cursor;

    private String TAG="DBActivity";

    public UhuruDataSource(Context context)
    {
        uhuruHelper =  MySQLiteHelper.getInstance(context);
    }

    public void open() throws SQLException {
        db = uhuruHelper.getWritableDatabase();
    }

    public void close() {

        uhuruHelper.close();

    }

    //Check alias if exist
    public int friendAliasCheck(String phone_number)
    {
        this.open();
        String countQuery = "SELECT  * FROM tbl_alias WHERE phone_number="+phone_number;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.close();
        return cnt;
    }


    //Check alias if exist
    public int friendAliasCheck()
    {
        this.open();
        String countQuery = "SELECT  * FROM tbl_alias";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.close();
        return cnt;
    }



    //Add friend alias
    public long recordFriendsAlias(String fname, String phone){
        this.open();
        long id=-1;

            try
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", fname);
                contentValues.put("phone_number",phone);
                id = db.insert("tbl_alias", null, contentValues);
            }
            catch (IllegalStateException e)
            {
                Log.v(TAG, "Error" + e.getMessage());
            }


        this.close();
        return id;
    }

    //Transaction Details insertion
    public long transactionDetails(String amount, String agent_name, String transaction_time,String status)
    {

        this.open();

        long id=-1;
           try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("amount", amount);
                contentValues.put("agent_name", agent_name);
                contentValues.put("transaction_time", transaction_time);
                contentValues.put("agent_pan", transaction_time);
                contentValues.put("location", transaction_time);
                contentValues.put("approval_code", transaction_time);
                contentValues.put("ret_no", transaction_time);
                contentValues.put("sender_account", transaction_time);
                contentValues.put("status",status);
                id = db.insert("tbl_transaction_history", null, contentValues);

            } catch (IllegalStateException e)
            {
                Log.v(TAG, "Error" + e.getMessage());
            }

        this.close();
        return id;
    }


    //Transaction Details insertion
    public long receipt_details(String amount, String agent_name, String transaction_time,String agent_pan,String location,String approval_code,String ret_no,String sender_account,String payment_type,String status,String foreign_amount,String special_id)
    {

        this.open();

        long id=-1;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("amount", amount);
            contentValues.put("agent_name", agent_name);
            contentValues.put("transaction_time", transaction_time);
            contentValues.put("agent_pan", agent_pan);
            contentValues.put("location", location);
            contentValues.put("approval_code", approval_code);
            contentValues.put("ret_no", ret_no);
            contentValues.put("sender_account", sender_account);
            contentValues.put("payment_type",payment_type);
            contentValues.put("status",status);
            contentValues.put("foreign_amount",foreign_amount);
            contentValues.put("special_id",special_id);
            id = db.insert("tbl_transaction_history", null, contentValues);

        } catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }

    public void clearDatabase(String TABLE_NAME) {
        this.open();
        try {
            db.delete(TABLE_NAME, "1", null);
        }catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        this.close();
    }

    public void clearIndex(String TABLE_NAME) {
        this.open();
        try {
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
        }catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        this.close();
    }

    //InsertAccountDetails
    public long accountDetails(String account, String pan)
    {

        this.open();

        long id=-1;

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("account", account);
            contentValues.put("pan", pan);
            id = db.insert("tbl_account", null, contentValues);
        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }

    public long bankDetails(String name, String bin, String identity)
    {

        this.open();

        long id=-1;

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("bin", bin);
            contentValues.put("identity", identity);
            id = db.insert("tbl_banks", null, contentValues);
        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }
        catch (SQLiteConstraintException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }

    //Insert Payee List
    public long payeeDetails(String payeeName, String payeeCardNumber)
    {

        this.open();

        long id=-1;

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("payeeName", payeeName);
            contentValues.put("payeeCardNumber", payeeCardNumber);
            id = db.insert("tbl_card_details", null, contentValues);
        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }

    //Retrieve payees list
    public ArrayList<Payee> getAllFriends() {
        ArrayList<Payee> payees = new ArrayList<>();
        String[] columns={"payeeName","payeeCardNumber"};

        this.open();
        cursor = db.query("tbl_card_details",columns,null,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                    Payee payee = new Payee();
                    payee.setPayeeName(cursor.getString(0));
                    payee.setPayeeCardNumber(cursor.getString(1).substring(12,16));
                    payees.add(payee);
            }
            while (cursor.moveToNext());
        }

        this.close();

        return payees;
    }

    //Retrieve Payee Card Number



    //Table Count
    public long getProfilesCount(String status) {
        this.open();
        String[] stat = new String[]{status};
        long cnt  = queryNumEntries(db,"tbl_transaction_history","status=?",stat);//DatabaseUtils
        db.close();
        return cnt;
    }



    public static long queryNumEntries(SQLiteDatabase db, String table, String selection,
                                       String[] selectionArgs) {
        String s = (!TextUtils.isEmpty(selection)) ? " where " + selection : "";
        return longForQuery(db, "select count(*) from " + table + s,
                selectionArgs);
    }

    //Get sum
    private int total_transaction;
    public int getSum(){

        this.open();
        String countQuery = "SELECT  SUM(amount) FROM tbl_transaction_history";
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.moveToFirst()) {
            total_transaction = cursor.getInt(0);
        }
        this.close();
        return total_transaction;
    }


    //Get table count
    public int getCount(String pan)
    {
        this.open();
        String countQuery = "SELECT  * FROM tbl_account WHERE pan="+pan;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.close();
        return cnt;
    }

    public int getBankCount(String bin)
    {
        this.open();

        String countQuery = "SELECT * FROM tbl_banks WHERE bin like '%"+bin+"%'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.close();
        return cnt;
    }

    //Get total count
    public int getCountTotal()
    {
        this.open();
        String countQuery = "SELECT  * FROM tbl_account";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.close();
        return cnt;
    }

    //Retrieve all accounts
    public ArrayList<Msg> getAccountDetails() {
        ArrayList<Msg> accounts = new ArrayList<>();
        String[] columns={"account","pan"};

        this.open();
        cursor = db.query("tbl_account",columns,null,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Msg msg = new Msg();
                msg.setAccount_number(cursor.getString(0));
                msg.setPan(cursor.getString(1));
                accounts.add(msg);
            }
            while (cursor.moveToNext());
        }

        this.close();

        return accounts;
    }


    public List<String> getNameSource(String identifier) {

        List<String> banks = new ArrayList<String>();
        String[] columns={"name"};

        this.open();
        cursor = db.query("tbl_banks",columns,"identity="+identifier,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                banks.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }

        this.close();

        return banks;
    }

    public String getBinSource(String name) {

        String banks = "";
        String[] columns={"bin"};

        this.open();
        cursor = db.query("tbl_banks",columns,"name like"+"'%"+name+"%'",null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                banks = cursor.getString(0);
            }
            while (cursor.moveToNext());
        }

        this.close();

        return banks;
    }





    public HashMap<String, String> getNameSourceMap(){
        HashMap<String, String> content =new HashMap<String, String>();

        String[] columns={"name","bin"};

        this.open();
        cursor = db.query("tbl_banks",columns,null,null,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                content.put(cursor.getString(0), cursor.getString(1));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return content;
    }

    private String[] accounts = new String[28];

    private String[] mobile = new String[5];

    //Get Source name of bank or wallet
    public String[] getSourceName() {
        String[] columns={"name"};

        this.open();
        cursor = db.query("tbl_banks",columns,null,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {

                     accounts = new String[]{cursor.getString(0)};


            }
            while (cursor.moveToNext());
        }

        this.close();

        return accounts;
    }

    //Get Source name of bank or wallet
    public String[] getSourceBin(String source_id) {

        int i=0;

        String[] columns={"bin"};

        this.open();
        cursor = db.query("tbl_banks",columns,"identity="+source_id,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                accounts = new String[]{cursor.getString(0).substring(0,4)+"****"+cursor.getString(0).substring(8)};

            }
            while (cursor.moveToNext());
        }

        this.close();

        return accounts;
    }




    //Retrieve all accounts masked
    public List<String> getAccount() {

        int i=0;

        List<String> accounts =  new ArrayList<String>();

        String[] columns={"account","id"};

        this.open();
        cursor = db.query("tbl_account",columns,null,null,null,null,"id");

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                //cursor.getString(0).substring(0,4)+"****"+cursor.getString(0).substring(8)

               accounts.add(cursor.getString(0).substring(0,4)+"****"+cursor.getString(0).substring(8));

            }
            while (cursor.moveToNext());
        }

        this.close();

        return accounts;
    }


    public String getAccountPlain(String id) {

        String account="";

        int i=0;

        List<String> accounts =  new ArrayList<String>();

        String[] columns={"account"};

        this.open();
        cursor = db.query("tbl_account",columns,"id="+Integer.parseInt(id),null,null,null,"id");

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {

                account = cursor.getString(0);

            }
            while (cursor.moveToNext());
        }

        this.close();

        return account;
    }



    //Retrieve all accounts unmasked
    public String[] getUnmaskedAccount() {

        int i=0;

        String[] columns={"account"};

        this.open();
        cursor = db.query("tbl_account",columns,null,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                accounts = new String[]{cursor.getString(0)};
            }
            while (cursor.moveToNext());
        }

        this.close();

        return accounts;
    }


    /**********************mVisa functions*********************************************************/

    //Check if alias available
    public int getCountAlias(String pan)
    {
        this.open();
        String countQuery = "SELECT  * FROM tbl_consumer_alias WHERE card_number="+pan;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.close();
        return cnt;
    }

    public int getTotalCount()
    {
        this.open();
        String countQuery = "SELECT  * FROM tbl_consumer_alias";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.close();
        return cnt;
    }

    public int getLookupcount(String name)
    {
        this.open();
        String countQuery = "SELECT * FROM tbl_look_up WHERE name like '%"+name+"%'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.close();
        return cnt;
    }

    //InsertAllLookups
    public long lookupDetails(String name)
    {

        this.open();

        long id=-1;

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            id = db.insert("tbl_look_up", null, contentValues);
        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }

    public List<String> getLookupNames() {

        List<String> names = new ArrayList<String>();
        String[] columns={"name"};

        this.open();
        cursor = db.query("tbl_look_up",columns,null,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                names.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }

        this.close();

        return names;
    }

    //InsertAllAliases
    public long insertAlias(String phone_number, String card_number, String fname, String mname, String lname)
    {

        this.open();

        long id=-1;

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("phone_number", phone_number);
            contentValues.put("card_number", card_number);
            contentValues.put("f_name",fname);
            contentValues.put("m_name",mname);
            contentValues.put("l_name",lname);
            id = db.insert("tbl_consumer_alias", null, contentValues);
        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }

    //Retrieve all aliases
    public String getAlias(String card_number) {
        String phonenumber="";
        String[] columns={"phone_number"};

        this.open();
        cursor = db.query("tbl_consumer_alias",columns,"card_number="+card_number,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {

                phonenumber = cursor.getString(0);

            }
            while (cursor.moveToNext());
        }

        this.close();

        return phonenumber;
    }

//    public ArrayList<Msg> getAliasFields() {
//        ArrayList<Msg> consumerAlias = new ArrayList<>();
//        String[] columns={"phone_number","card_number","f_name","m_name","l_name"};
//
//        this.open();
//        cursor = db.query("tbl_consumer_alias",columns,null,null,null,null,null);
//
//        //looping through all rows and adding to list
//        if (cursor.moveToFirst())
//        {
//            do
//            {
//                Msg msg = new Msg();
//                msg.setP_number(cursor.getString(0));
//                msg.setPan(cursor.getString(1));
//                msg.setFname(cursor.getString(2));
//                msg.setMname(cursor.getString(3));
//                msg.setLname(cursor.getString(4));
//                consumerAlias.add(msg);
//            }
//            while (cursor.moveToNext());
//        }
//
//        this.close();
//
//        return consumerAlias;
//    }

    //Deleting alias
    public void deleteAlias(String card_number){
        this.open();
        db.delete("tbl_consumer_alias", "card_number = ?", new String[]{card_number});
        this.close();
    }

    //Update alias
    public long updateAlias(String phone_number,String card_number){
        this.open();
        long id=-1;

        try
        {
                ContentValues contentValues = new ContentValues();
                contentValues.put("phone_number", phone_number);
                contentValues.put("card_number",card_number);
                db.update("tbl_consumer_alias", contentValues, "card_number="+card_number, null);


        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }

    public void resetingTableIndex(){
        this.open();


        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("SEQ", "0");
            db.update("SQLITE_SEQUENCE ", contentValues, "NAME="+"tbl_account", null);


        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();

    }

    /**********************************End mVisa functions*****************************************/



    //Get Accounts Using
    public ArrayList<CardDetails> getAcDetails(Context context,String bank) {
        ArrayList<CardDetails> accounts = new ArrayList<>();

        String[] columns={"account","pan"};

        this.open();
        cursor = db.query("tbl_account",columns,null,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {

                CardDetails msg= new CardDetails();
                msg.setCardArtImageLayer(ResourcesCompat.getDrawable(context.getResources(), R.drawable.background_tpb, null));
                msg.setCardNickName(bank);
                msg.setNetworkName("UMOJA");
                msg.setCardNumberLastFour(cursor.getString(1).substring(cursor.getString(1).length()-4));
//                msg.setIssuerLogo(ResourcesCompat.getDrawable(context.getResources(), R.drawable.tpb_logo, null));
//                msg.setNetworkLogo(ResourcesCompat.getDrawable(context.getResources(), R.drawable.mvisa_sdk_network_logo_visa_blue, null));
                /*msg.setIssuerLogo(ResourcesCompat.getDrawable(context.getResources(), R.drawable.uhuru_logo, null));
                msg.setNetworkLogo(d);*/
                accounts.add(msg);
            }
            while (cursor.moveToNext());
        }

        this.close();

        return accounts;
    }




    /** simply resizes a given drawable resource to the given width and height */
    public static Drawable resizeImage(Context ctx, int resId, int iconWidth,
                                       int iconHeight) {

        // load the origial Bitmap
        Bitmap BitmapOrg = BitmapFactory.decodeResource(ctx.getResources(),
                resId);

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = iconWidth;
        int newHeight = iconHeight;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return new BitmapDrawable(resizedBitmap);

    }


    public JSONArray getAllAc() {
        JSONArray accounts = new JSONArray();
        String[] columns={"account","pan"};

        this.open();
        cursor = db.query("tbl_account",columns,null,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                try {
                    JSONObject msg = new JSONObject();
                    msg.accumulate("account",cursor.getString(0));
                    msg.accumulate("pan", cursor.getString(1));
                    accounts.put(msg);
                }catch (JSONException E)
                {
                    E.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        this.close();

        return accounts;
    }

//    //Retrieve Transaction list
//    public ArrayList<Msg> getTransactions(String status) {
//        ArrayList<Msg> transactions = new ArrayList<>();
//        String[] columns={"amount","agent_name","transaction_time","ret_no","id"};
//
//        this.open();
//        cursor = db.query("tbl_transaction_history",columns,"status="+status,null,null,null,"transaction_time DESC");
//
//        //looping through all rows and adding to list
//        if (cursor.moveToFirst())
//        {
//            do
//            {
//                Msg msg = new Msg();
//                msg.setAmount(cursor.getString(0));
//                msg.setAgent_name(cursor.getString(1));
//                msg.setTransaction_time(cursor.getString(2));
//                msg.setApproval_code(cursor.getString(3));
//                msg.setBin(cursor.getString(4));
//                transactions.add(msg);
//            }
//            while (cursor.moveToNext());
//        }
//
//        this.close();
//
//        return transactions;
//    }


    //Retrieve Explicit transaction
    public JSONArray getReceiptData(String approval_code,String datetime) {
        JSONArray transactions = new JSONArray();

        String[] columns=
                {"amount","agent_name","transaction_time","agent_pan","location","approval_code","ret_no","sender_account","payment_type","foreign_amount","special_id"};

        this.open();
        cursor = db.query("tbl_transaction_history",columns,"id like "+"'%"+datetime+"%'",null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                try {
                    JSONObject msg = new JSONObject();
                    msg.put("amount",cursor.getString(0));
                    msg.put("agent_name",cursor.getString(1));
                    msg.put("transaction_time",cursor.getString(2));
                    msg.put("agent_pan",cursor.getString(3));
                    msg.put("location",cursor.getString(4));
                    msg.put("approval_code",cursor.getString(5));
                    msg.put("ret_no",cursor.getString(6));
                    msg.put("sender_account",cursor.getString(7));
                    msg.put("payment_type",cursor.getString(8));
                    msg.put("foreign_amount",cursor.getString(9));
                    msg.put("special_id",cursor.getString(10));
                    transactions.put(msg);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        this.close();

        return transactions;
    }

    /******************************Friends list***************************************************/
    //Retrieve Friends
    public ArrayList<Msg> getFriendsFromAlias() {
        ArrayList<Msg> transactions = new ArrayList<>();
        String[] columns={"name","phone_number","id"};

        this.open();
        cursor = db.query("tbl_alias",columns,null,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Msg msg = new Msg();
                msg.setName(cursor.getString(0));
                msg.setP_number(cursor.getString(1));
                msg.setRef_no(cursor.getString(2));
                transactions.add(msg);
            }
            while (cursor.moveToNext());
        }

        this.close();

        return transactions;
    }


    //Retrieve Friends
    public ArrayList<Msg> getFriendsAlias(String name) {
        ArrayList<Msg> transactions = new ArrayList<>();
        String[] columns={"name","phone_number"};

        this.open();
        cursor = db.query("tbl_alias",columns,"name like "+"'%"+name+"%'",null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Msg msg = new Msg();
                msg.setName(cursor.getString(0));
                msg.setP_number(cursor.getString(1));
                transactions.add(msg);
            }
            while (cursor.moveToNext());
        }

        this.close();

        return transactions;
    }

    public boolean updateFriendAlias(String name,String phone_number, String phone){
        Log.v("STATEMENT",name+" "+phone_number+" "+phone);
        this.open();
        boolean id=true;
        String[] args = new String[]{phone};
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("phone_number",phone_number);
            db.update("tbl_alias", contentValues, "id=?",args);
        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
            id=false;
        }

        this.close();
        return id;
    }

    //Deleting alias
    public void deleteFriendAlias(String phone_number){
        this.open();
        db.delete("tbl_alias", "phone_number = ?", new String[]{phone_number});
        this.close();
    }

    /******************************End Friends list***************************************************/


    //Retrieve payees list
    public ArrayList<Payee> getFriends() {
        ArrayList<Payee> payees = new ArrayList<>();
        String[] columns={"payeeName","payeeCardNumber"};

        this.open();
        cursor = db.query("tbl_card_details",columns,null,null,null,null,null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Payee payee = new Payee();
                payee.setPayeeName(cursor.getString(0));
                payee.setPayeeCardNumber(cursor.getString(1));
                payees.add(payee);
            }
            while (cursor.moveToNext());
        }

        this.close();

        return payees;
    }

    //Deleting friend account
    public void deleteFriend(String payeeCardNumber){
        this.open();
        db.delete("tbl_card_details", "payeeCardNumber = ?", new String[]{payeeCardNumber});
        this.close();
    }

    //Update Friend
    public long updateFriend(String trxID,String status,String receipt){
        this.open();
        long id=-1;

        String selection =  "_id=?" ;
        String []selectionArgs={""};
        String []selectionTrxID=trxID.split("~",-1);

        try
        {
            for(int i=0;i<selectionTrxID.length;i++) {
                selectionArgs[0]=selectionTrxID[i];
                ContentValues contentValues = new ContentValues();
                contentValues.put("payeeName", receipt);
                contentValues.put("payeeCardNumber", status);

                db.update("tbl_card_details", contentValues, selection, selectionArgs);
                Log.v(TAG, "Updated to status: " + status + " ID:" + selectionTrxID[i]);
            }

        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }




    public void cleanAllowedtrxns(){
        this.open();
        db.delete("tbl_municipal_trx_types", "1", null);
        this.close();
    }
    public void ForceDeleteTransaction(){
        this.open();
        final String sql =     "DELETE FROM tbl_municipal_trx_logs " ;

        db.rawQuery(sql, null);

        this.close();
    }

    public List<String> getTemptrx(){
        List<String> labels = new ArrayList<String>();
        String[] columns={"name","pay_code"};

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"pay_type='00'", null,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                labels.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return labels;
    }

    public String getRouteDetailss(String stop_name){
        String[] columns={"stop_id"};
        String [] parameters = {stop_name};
        String details="";

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"stop_name=?",parameters,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                details=cursor.getString(0);
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return details;
    }

    public List<String> getTemporaryTrans(int x,String order){
        List<String> labels = new ArrayList<String>();
        String[] columns={"stop_name","stop_id","longitude","latitude"};


        if(order==""){
            this.open();
            cursor = db.query("tbl_municipal_trx_types",columns,"stop_id >"+x,null,null,null,"stop_id "+order);
        }else{
            this.open();
            cursor = db.query("tbl_municipal_trx_types",columns,"stop_id <="+x,null,null,null,"stop_id "+order);
        }


        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                labels.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return labels;
    }


    public List<String> getTemporaryTransaction(int x){
        List<String> labels = new ArrayList<String>();
        String[] columns={"stop_name","stop_id","longitude","latitude"};

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"stop_id <="+x,null,null,null,"stop_id DESC");

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                labels.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return labels;
    }



    public HashMap<String, String> getTemporarytrxMap(){
        HashMap<String, String> content =new HashMap<String, String>();

        String[] columns={"stop_name","stop_id"};

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,null,null,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                content.put(cursor.getString(1), cursor.getString(0));
                // System.out.println("PPPPPP_________" + cursor.getString(0) + "=" + cursor.getString(1));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return content;
    }


    public HashMap<String, String> getTemptrxMap(){
        HashMap<String, String> content =new HashMap<String, String>();

        String[] columns={"name","pay_code"};

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"pay_type='00'", null,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                content.put(cursor.getString(0), cursor.getString(1));
               // System.out.println("PPPPPP_________" + cursor.getString(0) + "=" + cursor.getString(1));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return content;
    }
    public List<String> getPermtrx(){
        List<String> labels = new ArrayList<String>();
        String[] columns={"name","pay_code"};

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"pay_type='01'", null,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                labels.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return labels;
    }
//*********************************************************************************
    public void listTrxs(){
       this.open();//tbl_municipal_trx_logs
       cursor = db.query("tbl_municipal_trx_logs",null,null, null,null,null,"10");
        if (cursor.moveToFirst())
        {
            do
            {
               // System.out.println("listTrx MMMMMMMMMMMMMMMMXXXYYZZZZZZ " + cursor.getColumnNames().toString());
                System.out.println("listTrx MMMMMMMMMMMMMMMMXXXYYYYYYYY " + cursor.getString(0)+ "_f1_" + cursor.getString(1) + "_f2_" + cursor.getString(2) + "__f3_" + cursor.getString(3) + "__f4_" + cursor.getString(4) + "_"
                        + cursor.getString(5) + "__f6_" + cursor.getString(6) + "_f7_" + cursor.getString(7) + "_f8_" + cursor.getString(8) + "_" + cursor.getString(9) + "_f10_" + cursor.getString(10)
                        +  "__f11_" + cursor.getString(11) + "__f12_" + cursor.getString(12) + "__f13_" + cursor.getString(13)  );

            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();


    }
    public List<String> mainList(String category){
        List<String> labels = new ArrayList<String>();
        String[] columns={"name","pay_code"};
        String selectArgs="pay_type='00' and pay_code  like '%"+category+"'";

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,selectArgs, null,null,null,"pay_code");

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                if (cursor.getString(0).contains(":")) {
                    labels.add(cursor.getString(0).substring(0, cursor.getString(0).indexOf(":")) + "~" + cursor.getString(1));
                }
                else {
                    labels.add(cursor.getString(0) + "~" + cursor.getString(1));
                }
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return labels;
    }
    public List<String> getVipimoListPerCategory(String category){
    List<String> labels = new ArrayList<String>();
    String[] columns={"name","pay_code","amount"};
    String selectArgs="pay_type='00' and pay_code  like '"+category+"%'";

    this.open();
    cursor = db.query("tbl_municipal_trx_types",columns,selectArgs, null,null,null,null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst())
    {
        do
        {
            if (cursor.getString(0).contains(":")) {
                labels.add(cursor.getString(0).substring(0, cursor.getString(0).indexOf(":"))+"-"+cursor.getString(2) + "~" + cursor.getString(1));
            }
            else {
                labels.add(cursor.getString(0) + "~" + cursor.getString(1)+"-"+cursor.getString(2) );
            }
        }
        while (cursor.moveToNext());
    }

    // closing connection
    cursor.close();
    db.close();

    return labels;
}
    public HashMap<String, String> getVipimoMapPerCategory(String category){
        HashMap<String, String> content =new HashMap<String, String>();

        //String[] columns={"name","pay_code"};
        //String[] columns={"pay_code","name","pay_type","amount","amount_alt"};
        String[] columns={"pay_code","name",};
        String selectArgs="pay_type='00' and pay_code  like '%"+category+"%'";

        this.open();
        cursor = db.query("tbl_municipal_trx_types", columns, selectArgs, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do {
                content.put(cursor.getString(0), cursor.getString(1));
               // System.out.println("XXXXXXXXX" + cursor.getString(0) + " = " + cursor.getString(1) + " = " + cursor.getString(2) + " = " + cursor.getString(3) + " = " + cursor.getString(4));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return content;
    }
    /***
     * Get Payment details for Hopsital Transactions
     * @author Peter Paul
     * @param paycode
     * @param paytype
     * @return
     */
    public String getHospitalPaymentDetails(String paycode,String paytype){
        String[] columns={"name","amount","amount_alt","pay_code"};
        String [] parameters = {paycode,paytype };
        String details="";
         this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"pay_code =? AND pay_type=?", parameters,null,null,null);
       // String sqlQuery="SELECT name,amount,amount_alt FROM  tbl_municipal_trx_types WHERE pay_type="+paytype+" AND pay_code IN("+makePlaceHolders(placeHolderCount)+")";
       // String sqlQuery="SELECT name,amount,amount_alt FROM  tbl_municipal_trx_types WHERE  pay_code IN('0003','0030','0033')";
       // cursor =db.rawQuery(sqlQuery,null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                details=cursor.getString(0)+"~"+cursor.getString(1)+"~"+cursor.getString(2)+"~"+cursor.getString(3);
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return details;
    }
    /***
     * Calculate how many placeholder to be placed
     * @param len
     * @return
     */
    private String makePlaceHolders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }


    public long updateMunicipalHospitalTrxDetails(String trxID,String status,String receipt){
        this.open();
        long id=-1;

        String selection =  "_id=?" ;
        String []selectionArgs={""};
        String []selectionTrxID=trxID.split("~",-1);

        try
        {
            for(int i=0;i<selectionTrxID.length;i++) {
                selectionArgs[0]=selectionTrxID[i];
                ContentValues contentValues = new ContentValues();
                contentValues.put("field_37", receipt);
                contentValues.put("field_39", status);

                db.update("tbl_municipal_trx_logs", contentValues, selection, selectionArgs);
                Log.v(TAG, "Updated to status: " + status + " ID:" + selectionTrxID[i]);
            }

        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }
//*********************************************************************************
    public HashMap<String, String> getPermtrxMap(){
        HashMap<String, String> content =new HashMap<String, String>();

        String[] columns={"name","pay_code"};

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"pay_type='01'", null,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                content.put(cursor.getString(0), cursor.getString(1));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return content;
    }
    public String getPaymentDetails(String paycode,String paytype){
        String[] columns={"name","amount","amount_alt"};
        String [] parameters = {paycode,paytype};
        String details="";

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"pay_code=? AND pay_type=?", parameters,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                details=cursor.getString(0)+"~"+cursor.getString(1)+"~"+cursor.getString(2);
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return details;
    }

    public String getRouteDetails(String stop_name){
        String[] columns={"stop_id"};
        String [] parameters = {stop_name};
        String details="";

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"stop_name=?",parameters,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                details=cursor.getString(0);
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return details;
    }

    public String getRouteDetailsTwo(String stop_name){
        String[] columns={"stop_id"};
        String [] parameters = {stop_name};
        String details="";

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"stop_name=?",parameters,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                details=cursor.getString(0);
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return details;
    }

    public long updateMunicipalTrxDetails(long trxID,String status,String receipt){
        this.open();
        long id=-1;

        String selection =  "_id=?" ;
        String []selectionArgs={ new String(trxID+"")};

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("field_37", receipt);
            contentValues.put("field_39", status);

            db.update("tbl_municipal_trx_logs",contentValues,selection,selectionArgs);
            Log.v(TAG, "Updated to status: "+status +" ID:"+trxID);

        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }
    public long clearMunicipalTrxDetails(long trxID){
        this.open();
        long id=-1;

        String selection =  "_id=?" ;
        String []selectionArgs={trxID+""};

        try
        {
            db.delete("tbl_municipal_trx_logs", selection, selectionArgs);
            Log.v(TAG, "Deleted ID:"+trxID);
        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }



    public long clearSelectedSeats(){
        this.open();
        long id=-1;

        try
        {
            db.delete("tbl_municipal_seats", null, null);
        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }

    public JSONArray getSeatsSelected(String status){
        JSONArray trxList=new JSONArray();
        String[] columns={"_id","seat","recon_status"};

        this.open();
        String selection =  "recon_status=?" ;
        String []selectionArgs={status};
        cursor = db.query("tbl_municipal_seats",columns,selection ,selectionArgs,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                JSONObject trx=new JSONObject();
                try
                {
                    trx.put("seat",  cursor.getString(1));
                    trxList.put(trx);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return trxList;
    }


    public JSONArray getDailyTrxBySummary(){
           JSONArray trxList=new JSONArray();


         String sql =  "SELECT count(b.stop_id) as name ," +
                            "SUM(a.field_4)," +
                            "count(a.field_7),"+
                            "field_102 "+
                            "FROM tbl_municipal_trx_logs a INNER JOIN tbl_municipal_trx_types b " +
                            "ON a.field_102=SUBSTR(b.stop_id,2) " +
                            "WHERE a.recon_status=? AND (field_39=? OR field_39=?)" +
                            "GROUP BY a.field_102";
        this.open();
        cursor=db.rawQuery(sql,new String[]{"0", "00", "91"});
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                JSONObject trx=new JSONObject();
                try
                {

                    trx.put("Description",cursor.getString(0)+"("+cursor.getString(2)+")");
                    trx.put("Amount",  cursor.getString(1));
                    trxList.put(trx);

                 }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
            while (cursor.moveToNext());
        }else
            Log.v(TAG, "No Summary Records" );

        // closing connection
        cursor.close();
        db.close();

        return trxList;
    }
    public JSONArray getDailyTrxBySummaryPP(){
        JSONArray trxList=new JSONArray();

        final String sql =  "SELECT b.name," +
                "SUM(a.field_4)," +
                "count(a.field_7),"+
                "field_102 "+
                "FROM tbl_municipal_trx_logs a INNER JOIN tbl_municipal_trx_types b " +
                "ON a.field_102=b.pay_code " +
                "WHERE a.recon_status=? AND (field_39=? OR field_39=?)" +
                "GROUP BY a.field_102";
        this.open();
        cursor=db.rawQuery(sql, new String[]{"0", "00", "91"});


        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                JSONObject trx=new JSONObject();
                try
                {
                    trx.put("Description",cursor.getString(0));
                    trx.put("counter",cursor.getString(2));
                    trx.put("Amount", cursor.getString(1));
                    trxList.put(trx);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return trxList;
    }
    public String [] getReportSummary(){

        final String sql = "SELECT  strftime('%d/%m/%Y %H:%M:%S',MIN(datetime(record_time,'localtime')))," +
                                   "strftime('%d/%m/%Y %H:%M:%S',MAX(datetime(record_time,'localtime'))), " +
                                   "SUM(seat) " +
                           "FROM tbl_municipal_trx_logs " +
                           "WHERE recon_status=? AND (field_39=? OR field_39=?)";
        this.open();
        cursor=db.rawQuery(sql,new String[]{"0","00", "91"});
        String [] values=new String[3];

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                values[0]=cursor.getString(0);
                values[1]=cursor.getString(1);
                values[2]=cursor.getString(2);
                System.out.println("MMMMMMMMMMMMM"+values[0]+"="+values[1]+"="+values[2]);
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return values;
    }
    public long updateReconStatus(){
        this.open();
        long id=-1;

        String selection =  "recon_status=?" ;
        String []selectionArgs={"0"};

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("recon_status", "1");

            db.update("tbl_municipal_trx_logs",contentValues,selection,selectionArgs);
            Log.v(TAG, "Closed business day");

        }
        catch (IllegalStateException e)
        {
            Log.v(TAG, "Error" + e.getMessage());
        }

        this.close();
        return id;
    }
    public String GetTicketDetails(String paycode,String paytype)
    {
        String[] columns={"name","amount","pay_code"};
        String [] parameters = {paycode,paytype};
        String details="";

        this.open();
        cursor = db.query("tbl_municipal_trx_types",columns,"pay_code like ? AND pay_type=?", parameters,null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                details=cursor.getString(0)+"~"+cursor.getString(1)+"~"+cursor.getString(2);

            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return details;
    }
    public int GetSTAN(){

        final String sql = "SELECT   MAX(CAST(field_11 AS INTEGER)) FROM tbl_municipal_trx_logs";
        this.open();
        cursor=db.rawQuery(sql,null);
        int  values=0;

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                values=cursor.getInt(0);

            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return values;
    }

}







