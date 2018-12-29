package com.goandroytech.www.rahisipay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goandroytech.www.rahisipay.Adapter.Child_Adapter;
import com.goandroytech.www.rahisipay.Connection.ConnectivityDetector;
import com.goandroytech.www.rahisipay.Dialog.ChangePIN_Dialog;
import com.goandroytech.www.rahisipay.Model.Child;
import com.goandroytech.www.rahisipay.Model.Service_Model;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.goandroytech.www.rahisipay.Adapter.Service_Adapter.serviceList;
import static com.goandroytech.www.rahisipay.apicalls.API.BASE_URL;
import static com.goandroytech.www.rahisipay.apicalls.API.BLOCK_CARD_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.LINK_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PHONE;
import static com.goandroytech.www.rahisipay.apicalls.API.formatCurrency;

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
    String get_card_state;
    public static String NAME ="name";
    public static String ID ="id";
    String CARD_STATUS ="card_state";

    String user_pin_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        get_name = sp.getString(NAME, null);
        get_card_state = sp.getString(CARD_STATUS, null);
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
            connectivityDetector.showAlertDialog( MyAccount.this,"Connection Error","No internet connection");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        if (get_card_state.equals("1")){
            menu.findItem(R.id.nav_block_card).setTitle("Block Card");
        } else if (get_card_state.equals("5")){
            menu.findItem(R.id.nav_block_card).setTitle("UnBlock");
        }

        View hView =  navigationView.getHeaderView(0);
        TextView customer_name = (TextView)hView.findViewById(R.id.customer_name);
        TextView customer_account_no = (TextView)hView.findViewById(R.id.customer_account_no);
        customer_name.setText(get_name);
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

    @Override
    protected void onResume() {
        super.onResume();
        get_card_state = sp.getString(CARD_STATUS, null);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        if (get_card_state.equals("1")){
            menu.findItem(R.id.nav_block_card).setTitle("Block Card");
        } else if (get_card_state.equals("5")){
            menu.findItem(R.id.nav_block_card).setTitle("UnBlock");
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
            IntentIntegrator integrator = new IntentIntegrator(MyAccount.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        } else if (id == R.id.nav_block_card) {
            Intent intent = new Intent(MyAccount.this,Card.class);
            startActivity(intent);

        } else if (id == R.id.nav_change_pin) {
            ChangePIN_Dialog cdd=new ChangePIN_Dialog(MyAccount.this);
            cdd.show();
        } else if (id == R.id.nav_logout) {
            openLogoutConfirmation();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void openLogoutConfirmation() {
        LayoutInflater factory = LayoutInflater.from(MyAccount.this);
        final View textEntryView = factory.inflate(R.layout.activity_confirm, null);
        TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
        tv_message.setText("Are you sure you want to Logout?");
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button)textEntryView.findViewById(R.id.btn_cancel);
        btn_ok.setText("Yes");
        btn_cancel.setText("No");
        final AlertDialog.Builder alert = new AlertDialog.Builder(MyAccount.this);
        alert.setCancelable(true);
        final AlertDialog alertDialog = alert.setView(textEntryView).create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
                updateLoginPrefrence();
                Intent intent = new Intent(MyAccount.this,Login.class);
                startActivity(intent);            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                connectivityDetector.showAlertDialog(MyAccount.this,"Error", "You Cancelled Scanning!");
            }
            else {
                connectivityDetector.openSuccessDialog(MyAccount.this,"Success", result.getContents());
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
                            String get_card_number = jsonObject.getString("card_number");
                            String get_balance = jsonObject.getString("balance");
                            try {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("last_transaction");
                                if (jsonObject1.isNull("last_transaction")){
                                    get_date = jsonObject1.getString("date");
                                    get_transaction_number = jsonObject1.getString("transaction_number");
                                    get_description = jsonObject1.getString("description");
                                    get_amount = jsonObject1.getString("amount");
                                    get_charge = jsonObject1.getString("charge");
                                    get_nature = jsonObject1.getString("nature");
                                }

                            } catch (Exception e){

                            }

                            if (get_account_number.equals("")){
                                progressDialog.dismiss();

                                connectivityDetector.showAlertDialog(MyAccount.this,"Error", "Unable to load data!");


                            } else {
                                try{
                                    progressDialog.dismiss();
                                    name.setText(get_account_name);
                                    account_no.setText(get_account_number);
                                    phone_no.setText(get_phone_number);
                                    email.setText(get_email);
                                    card_no.setText(get_card_number);
                                    balance.setText(API.formatCurrency(get_balance));
                                    date.setText(API.DateFormat(get_date));
                                    nature.setText(get_description);
                                    amount.setText(formatCurrency(get_amount));
                                } catch (Exception e){
                                    e.printStackTrace();
                                }


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
                        connectivityDetector.showAlertDialog(MyAccount.this,"Error", error.toString());

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

    private void updateLoginPrefrence() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PHONE, "");
        editor.apply();
        editor.commit();
    }
}
