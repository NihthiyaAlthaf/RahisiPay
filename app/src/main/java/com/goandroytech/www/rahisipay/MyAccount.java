package com.goandroytech.www.rahisipay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goandroytech.www.rahisipay.Connection.ConnectivityDetector;
import com.goandroytech.www.rahisipay.Dialog.ChangePIN_Dialog;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyAccount extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static SharedPreferences sp;
    public   String SHARED_PREF_NAME = "mysharedpref";
    public static String PHONE ="phone";
    static String PIN ="pin";
    static String get_phone;
    static String get_pin;
    ConnectivityDetector connectivityDetector;
    RequestQueue queue;
    TextView name,account_no,card_no,phone_no,email,balance,date,nature,amount;

    String get_date;
    String get_transaction_number;
    String get_description;
    String get_amount;
    String get_charge;
    String get_nature;

    String get_name;
    String get_id;
    public static String NAME ="name";
    public static String ID ="id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        get_name = sp.getString(NAME, null);
        get_id = sp.getString(ID, null);
        get_phone = sp.getString(PHONE, null);
        get_pin = sp.getString(PIN, null);
        connectivityDetector = new ConnectivityDetector(MyAccount.this);
        queue = Volley.newRequestQueue(MyAccount.this);
        name = (TextView)findViewById(R.id.name);
        account_no = (TextView)findViewById(R.id.account_no);
        card_no = (TextView)findViewById(R.id.card_no);
        phone_no = (TextView)findViewById(R.id.phone_no);
        email = (TextView)findViewById(R.id.email);
        balance = (TextView)findViewById(R.id.balance);
        date = (TextView)findViewById(R.id.date);
        nature = (TextView)findViewById(R.id.nature);
        amount = (TextView)findViewById(R.id.amount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (connectivityDetector.checkConnectivityStatus()) {
            myAccountDetails();
        } else {
            connectivityDetector.showAlertDialog(MyAccount.this, "Connection Error!", "No internet connection");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView customer_name = (TextView)hView.findViewById(R.id.customer_name);
        TextView customer_account_no = (TextView)hView.findViewById(R.id.customer_account_no);
        customer_name.setText("Hello ,"+get_name);
        customer_account_no.setText(get_id);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_services) {
            // Handle the camera action
            finish();
            Intent intent = new Intent(MyAccount.this,Home.class);
            startActivity(intent);
        }  else if (id == R.id.nav_account_details) {


        } else if (id == R.id.nav_view_transaction){
            finish();
            Intent intent = new Intent(MyAccount.this,ViewTransaction.class);
            startActivity(intent);

        } else if (id == R.id.nav_pay_qrcode) {

        } else if (id == R.id.nav_block_card) {
            finish();
            Intent intent = new Intent(MyAccount.this,BlockCard.class);
            startActivity(intent);
        } else if (id == R.id.nav_change_pin) {
            ChangePIN_Dialog cdd=new ChangePIN_Dialog(MyAccount.this);
            cdd.show();
        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(MyAccount.this);
            builder1.setMessage("Do You Want to Logout?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                            Intent intent = new Intent(MyAccount.this,Login.class);
                            startActivity(intent);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void myAccountDetails() {

        final ProgressDialog progressDialog = new ProgressDialog(MyAccount.this);
        progressDialog.setMessage("Fetching Details ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, API.BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);
                            JSONObject jsonObject = json_res.getJSONObject("response");
                            String get_account_number = jsonObject.getString("account_number");
                            String get_account_name = jsonObject.getString("account_name");
                            String get_phone_number = jsonObject.getString("phone_number");
                            String get_email = jsonObject.getString("email");
                            String get_balance = jsonObject.getString("balance");

                            JSONObject jsonObject1 = jsonObject.getJSONObject("last_transaction");

                            if (jsonObject1.isNull("last_transaction")){
                                get_date = jsonObject1.getString("date");
                                get_transaction_number = jsonObject1.getString("transaction_number");
                                get_description = jsonObject1.getString("description");
                                get_amount = jsonObject1.getString("amount");
                                get_charge = jsonObject1.getString("charge");
                                get_nature = jsonObject1.getString("nature");
                            }


                            if (get_account_number.equals("")){
                                progressDialog.dismiss();

                                MDToast mdToast = MDToast.makeText(MyAccount.this,"Unable to Load Account Details!",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);
                                mdToast.show();

                            } else {
                                progressDialog.dismiss();
                                name.setText(get_account_name);
                                account_no.setText(get_account_number);
                                phone_no.setText(get_phone_number);
                                email.setText(get_email);
                                balance.setText(get_balance+" TZS");
                                date.setText(get_date);
                                nature.setText(get_nature);
                                amount.setText(get_amount);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        MDToast mdToast = MDToast.makeText(MyAccount.this,error.toString(),Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);
                        mdToast.show();
                    }
                }
        )
        {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("phone", get_phone);
                params.put("pin", get_pin);
                params.put("service_id", API.ACCOUNT_SERVICE_ID);

                return params;
            }
        };
        queue.add(postRequest);
    }

}
