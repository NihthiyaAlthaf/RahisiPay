package com.goandroytech.www.rahisipay.Pay_Visa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.goandroytech.www.rahisipay.Dialog.ChangePIN_Dialog;
import com.goandroytech.www.rahisipay.Home;
import com.goandroytech.www.rahisipay.Login;
import com.goandroytech.www.rahisipay.Model.Msg;
import com.goandroytech.www.rahisipay.Model.menuData;
import com.goandroytech.www.rahisipay.R;
import com.goandroytech.www.rahisipay.Visa;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class TransactionList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private Toolbar toolbar;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<menuData> items;
    private static ArrayList<Msg> transactions;
    private static final int SETTINGS_RESULT = 1;
    public static Locale myLocale;
    private SharedPreferences pref;
    private UhuruDataSource db;
    private String customerName,identifier,guid;
    private ProgressDialog mProgressDialog;
    private String pan, phonenumber;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);
                    SharedPreferences.Editor value = pref.edit();
                    value.putString("CurrentPage","1");
                    value.commit();
                    Intent intent = new Intent(TransactionList.this, AllContacts.class);//PayFriend
                    startActivity(intent);
                    return true;

                case R.id.navigation_notifications:
              //      if(db.getCount()>0) {
                      SharedPreferences.Editor editor = pref.edit();
                      editor.putString("FromLogin", "0");
                      editor.commit();
                        Intent intent_3 = new Intent(TransactionList.this, Visa.class);
                        startActivity(intent_3);
                       ;
//                    }else{
//                        Toast.makeText(TransactionList.this,"Please add Account",Toast.LENGTH_LONG).show();
//                    }
                    return true;
            }
            return false;
        }

    };
    private  BroadcastReceiver receiver;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

//            hideSoftKeyboard(TransactionList.this);
            /*IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.package.ACTION_LOGOUT");
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("onReceive", "Logout in progress");
                    //At this point you should start the login activity and finish this one
                    intent = new Intent(context, FormLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            };
            registerReceiver(receiver, intentFilter);*/


            setContentView(R.layout.activity_transaction_list);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);

//            hideSoftKeyboard(this);

            pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);

            guid = pref.getString("Guid","0");

            customerName = pref.getString("CustomerName", null);

            identifier = pref.getString("identifier", null);

            phonenumber = pref.getString("PhoneNumber", null);

            pan = pref.getString("Pan"+String.valueOf(pref.getInt("defaultIndex", 0)), null);

            String bank = pref.getString("BankName", null);

            setSupportActionBar(toolbar);

            TextView trans = (TextView) findViewById(R.id.transaction_listed);


//         Log.e("RETURNED",);
            trans.setText(getString(R.string.welcome) + " " + customerName + System.lineSeparator() + bank + "...." + pan.substring(12) + System.lineSeparator() + getString(R.string.phone_number) + ":" + phonenumber.substring(0, 6) + "****" + phonenumber.substring(9));


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            View headerView = navigationView.getHeaderView(0);
            TextView consumerName = (TextView) headerView.findViewById(R.id.consumer);
            consumerName.setText(customerName);

            navigationView.setNavigationItemSelectedListener(this);


            db = new UhuruDataSource(this);
            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            items = new ArrayList<>();
            transactions = db.getTransactions("0");
            /*JSONArray data = db.getReceiptData("077680");
            Log.v("CODE",""+data);*/
            menuBuilder myMenu = new menuBuilder("TransactionList", getApplicationContext());
            
            Log.v("ProfileCount", String.valueOf(db.getProfilesCount("0") + " SUM=" + String.valueOf(db.getSum())));

            adapter = new transactionlistMyAdapter(transactions, this, db);
            recyclerView.setAdapter(adapter);
            mProgressDialog = new ProgressDialog(this);


            recyclerView.addOnItemTouchListener(
                    new menuRecyclerItemClickListener(this.getApplicationContext(), new menuRecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                        }
                    })
            );

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setSelected(false);
            navigation.setBackgroundColor(getResources().getColor(R.color.mvisa_sdk_orange));
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }catch (NullPointerException e)
        {
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setSelected(false);
            navigation.setBackgroundColor(getResources().getColor(R.color.mvisa_sdk_orange));
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            e.printStackTrace();
        }
    }

    /***********************
     *Hiding keyboard
     ***********************/
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings)
        {
            Intent intent = new Intent(TransactionList.this, Home.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
//            super.onBackPressed();

            if(identifier.equalsIgnoreCase("0")) {
                Intent mainIntent = new Intent(TransactionList.this, Home.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(mainIntent);
                TransactionList.this.finish();
            }else
            {
                Intent mainIntent = new Intent(TransactionList.this, Home.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(mainIntent);
                TransactionList.this.finish();
            }
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(TransactionList.this, Home.class);
            intent.putExtra("status","0");
            startActivity(intent);
        }
//        else if (id == R.id.nav_slideshow) {
//            Log.v("ACCOUNT-DATA",""+db.getTotalCount());
////            if(db.getTotalCount()<=1){
//                mProgressDialog.setIndeterminate(true);
//                mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
//                mProgressDialog.setCancelable(false);
//                mProgressDialog.show();
//                RequestQueue queue = Action.getInstance(getApplicationContext()).getRequestQueue();
//                Msg msg = new Msg();
//                msg.set_MTI("0904");
//                msg.setGuid(guid(pan,phonenumber));
//                String ENDPOINT = Action.getApiEndpoint("sandboxVisa.php");
//                JSONObject reqMsg = Action.getJSONObject(msg);
//                JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, ENDPOINT, reqMsg, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        //Returned values from manage ALias
//                        Log.e("ALIAS-MANAGEMENT",""+jsonObject);
//
//                        //Capture JSON response
//                        String error = Action.getFieldValue(jsonObject,"reason");
//
//                        String errorMessage = Action.getFieldValue(jsonObject,"errorMessage");
//
//                        if(error.equalsIgnoreCase("not-found") || errorMessage.length()>0)
//                        {
//                            mProgressDialog.dismiss();
//                            SharedPreferences.Editor edit = pref.edit();
//                            edit.putString("title", "SETUP");
//                            edit.commit();
////                            Intent createAlias =  new Intent(TransactionList.this, SelectAccount.class);
////                            createAlias.putExtra("createAlias","createAlias");
////                            startActivity(createAlias);
//                        }
//                        else
//                        {
//                            mProgressDialog.dismiss();
//                            String phone_number = Action.getFieldValue(jsonObject,"alias");
//                            String recipientPrimaryAccountNumber = Action.getFieldValue(jsonObject,"recipientPrimaryAccountNumber");
//                            String recipientFirstName = Action.getFieldValue(jsonObject,"recipientFirstName");
//                            String recipientLastName = Action.getFieldValue(jsonObject,"recipientLastName");
//                            String recipientMiddleName = Action.getFieldValue(jsonObject,"recipientMiddleName");
//
//                            if(recipientFirstName==null)
//                            {
//                                recipientFirstName="";
//                            }
//
//                            if(recipientMiddleName==null)
//                            {
//                                recipientMiddleName="";
//                            }
//
//                            if(recipientLastName==null)
//                            {
//                                recipientLastName="";
//                            }
//
//
//                            db.insertAlias(phone_number,recipientPrimaryAccountNumber,recipientFirstName,recipientMiddleName,recipientLastName);
//                            Intent intent3 = new Intent(TransactionList.this, ManageAccount.class);
//                            startActivity(intent3);
//                        }
//
//                        //Display on dialog information returned
//
//                        //Perform action of either deleting or updating in case get returns with user information
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
//                        mProgressDialog.dismiss();
//                        Intent createAlias =  new Intent(TransactionList.this, SelectAccount.class);
//                        createAlias.putExtra("createAlias","createAlias");
//                        startActivity(createAlias);
//
//                    }
//                });
//
//                myReq.setRetryPolicy(new DefaultRetryPolicy(
//                        0,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                queue.add(myReq);
//
//
//           /* }else {
//                Intent intent3 = new Intent(TransactionList.this, ManageAccount.class);
//                startActivity(intent3);
//            }*/
//        }

        else if (id == R.id.nav_manage) {
            SharedPreferences.Editor preferences = pref.edit();
            preferences.putString("AfterPin","TransactionList");
            preferences.commit();
            ChangePIN_Dialog cdd=new ChangePIN_Dialog(TransactionList.this);
            cdd.show();
        }
//        else if(id == R.id.visa_number){
//            SharedPreferences.Editor edit = pref.edit();
//            edit.putString("title", "UPDATE MOBILE");
//            edit.commit();
//            Intent intent4 = new Intent(TransactionList.this, SelectAccount.class);
//            intent4.putExtra("visaNumber","visaNumber");
//            startActivity(intent4);
//        }

        else if (id == R.id.logout){

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
            sendBroadcast(broadcastIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Added 2018-09-03
    public  String guid(String pan, String mob_nr){

        String in=pan+mob_nr;

        String out;

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");

            md.update(in.getBytes());

            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }


            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            out=hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return out;

    }
    //End of update 2018-09-03

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
//        TransactionList.this.finish();
    }

    private Timer timer;
    /***********************
     *On stop
     ***********************/
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Main","Stopped");
        timer.cancel();
        TransactionList.this.finish();
    }

    /***********************
     *Activity paused
     ***********************/
    @Override
    protected void onPause() {
        super.onPause();

        timer = new Timer();
        Log.i("Main", "Invoking logout timer");
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
        timer.schedule(logoutTimeTask, 30000); //auto logout in 5 minutes
    }

    /***********************
     *Activity resumed
     ***********************/
    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            Log.i("Main", "cancel timer");
            timer = null;
        }
    }

    /***********************
     *Action if timeout
     ***********************/
    private class LogOutTimerTask extends TimerTask {

        @Override
        public void run() {

            //redirect user to login screen
            Intent i = new Intent(TransactionList.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }


}
