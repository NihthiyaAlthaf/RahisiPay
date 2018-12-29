package com.goandroytech.www.rahisipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goandroytech.www.rahisipay.Adapter.Child_Adapter;
import com.goandroytech.www.rahisipay.Adapter.Service_Adapter;
import com.goandroytech.www.rahisipay.Connection.ConnectivityDetector;
import com.goandroytech.www.rahisipay.Connection.MoneyTextWatcher;
import com.goandroytech.www.rahisipay.Dialog.Account_Num;
import com.goandroytech.www.rahisipay.Dialog.ChangePIN_Dialog;
import com.goandroytech.www.rahisipay.Dialog.PIN;
import com.goandroytech.www.rahisipay.Dialog.Pay;
import com.goandroytech.www.rahisipay.Model.Child;
import com.goandroytech.www.rahisipay.Model.Service_Model;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.goandroytech.www.rahisipay.Adapter.Service_Adapter.serviceList;
import static com.goandroytech.www.rahisipay.apicalls.API.BASE_URL;
import static com.goandroytech.www.rahisipay.apicalls.API.LAST_TRANSACTION_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.LINK_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.LOGIN_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PAY_LINK_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PAY_OTHER_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PAY_UNLINK_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PHONE;
import static com.goandroytech.www.rahisipay.apicalls.API.UNLINK_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.VALUE_LINKED_PAYMENT;
import static com.goandroytech.www.rahisipay.apicalls.API.VALUE_OTHER_PAYMENT;
import static com.goandroytech.www.rahisipay.apicalls.API.VALUE_UNLINKED_PAYMENT;

public class SelectService extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView service_name,service_account,service_title,txt_service_account;
    Button link,unlink,pay;
    String url,ser_name,subscribed,subscriptionAccount,service_id;
    ImageView logo_url;
    String MyPref = "mysharedpref";
    SharedPreferences sp;
    String VALIDATE = "val";
    String SERVICE_ID = "service_id";
    String SUBSCRIBED = "subscribed";
    String customer_name;
    String NAME ="name";
    String MOBILE = "mobile";
    String PIN ="pin";
    String get_Mobile,get_PIN;
    String user_amount,user_ac_number,user_pin_number;
    RequestQueue queue;
    String get_Status,get_message,get_transaction_id,get_provider_id,get_date,get_time,get_receipt_no,get_amount,get_service_charge,get_vat_amount,
            get_balance,get_card_no,get_account_no,get_service_id,get_service_name,get_service_provider;
    String PAY_FOR="pay_for";
    public static String ID ="id";
    String get_name,get_id,from;
    String CARD_STATUS ="card_state";
    String get_card_state;
    TextView date,nature,amount;
    ConnectivityDetector connectivityDetector;

    static String URL = "url";
    static String TRANSACTION_ID = "transaction_id";
    static String PROVIDER_ID = "provider_id";
    static String DATE = "date";
    static String TIME = "time";
    static String RECEIPT_NO = "receipt_no";
    static String AMOUNT = "amount";
    static String SERVICE_CHARGE = "service_charge";
    static String VAT = "vat";
    static String SERVICE_ACCOUNT_NO = "service_no";
    static String SERVICE_NAME = "service_name";
    static String SERVICE_PROVIDER = "service_provider";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(SelectService.this);
        service_name = (TextView) findViewById(R.id.service_name);
        service_title = (TextView) findViewById(R.id.service_title);
        date = (TextView) findViewById(R.id.date);
        nature = (TextView) findViewById(R.id.nature);
        amount = (TextView) findViewById(R.id.amount);
        txt_service_account = (TextView) findViewById(R.id.txt_service_account);
        service_account = (TextView) findViewById(R.id.service_account);
        link = (Button) findViewById(R.id.link);
        unlink = (Button) findViewById(R.id.unlink);
        logo_url = (ImageView) findViewById(R.id.logo_url);
        pay = (Button) findViewById(R.id.pay);
        sp = getSharedPreferences(MyPref, MODE_PRIVATE);
        connectivityDetector = new ConnectivityDetector(getApplication());



        get_name = sp.getString(NAME, null);
        get_id = sp.getString(ID, null);
        get_card_state = sp.getString(CARD_STATUS,null);

        customer_name = sp.getString(NAME, null);
        get_Mobile = sp.getString(MOBILE,null);
        get_PIN = sp.getString(PIN,null);
        Intent intent = getIntent();
        url = intent.getStringExtra("logo_url");
        ser_name = intent.getStringExtra("service_name");
        subscribed = intent.getStringExtra("subscribed");
        service_id = intent.getStringExtra("service_id");
        from = intent.getStringExtra("from");
        if (!subscribed.equals("0")) {
            link.setVisibility(View.GONE);
            subscriptionAccount = intent.getStringExtra("subscriptionAccount");
        } else {
            unlink.setVisibility(View.INVISIBLE);
        }

        service_account.setText(subscriptionAccount);
        Picasso.with(SelectService.this).load(url).into(logo_url);
        service_name.setText(ser_name);
        service_title.setText(ser_name);
        if (ser_name.equals("ZECO")){
            txt_service_account.setText("Your Meter No:");
        }
        if (connectivityDetector.checkConnectivityStatus()) {
            getLastTransaction();
        } else {
            connectivityDetector.showAlertDialog(SelectService.this, "Connection Error!", "No internet connection");
        }

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     saveLoginPrefrence("link");
                     LayoutInflater factory = LayoutInflater.from(SelectService.this);
                     View textEntryView = factory.inflate(R.layout.activity_account__num, null);
                     final EditText account_no = (EditText)textEntryView.findViewById(R.id.account_no);
                     Button submit = (Button)textEntryView.findViewById(R.id.submit);
                     final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
                     alert.setCancelable(true);

                      final AlertDialog alertDialog = alert.setView(textEntryView).create();

                      submit.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              user_ac_number = account_no.getText().toString();
                                    if(user_ac_number.equals("")){
                                      //  alert.setCancelable(true);
                                        Toast.makeText(SelectService.this,"Enter Account Number to Continue",Toast.LENGTH_SHORT).show();
                                    } else {
                                        alertDialog.dismiss();
                                        openPINDialog();
                                    }
                          }
                      });


                alertDialog.show();

        }

        });

        unlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPINDialog();

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLoginPrefrence("pay");
                //Pay pay = new Pay(SelectService.this);
                //pay.show();
                LayoutInflater factory = LayoutInflater.from(SelectService.this);
                final View textEntryView = factory.inflate(R.layout.activity_pay, null);
                final RadioButton self =(RadioButton)textEntryView.findViewById(R.id.self);
                final RadioButton other = (RadioButton)textEntryView.findViewById(R.id.other);
                Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
                final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
                alert.setCancelable(true);
                final AlertDialog alertDialog = alert.setView(textEntryView).create();

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (self.isChecked()){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(PAY_FOR, "self");
                            editor.apply();
                            editor.commit();
                            if (subscribed.equals("0")){
                                alertDialog.dismiss();
                                openPayAccount();
                            } else if (subscribed.equals("1")){
                                alertDialog.dismiss();
                                openPayAmount();
                            }
                        } else if (other.isChecked()){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(PAY_FOR, "other");
                            editor.apply();
                            editor.commit();
                            alertDialog.dismiss();
                            openPayAccount();
                        } else {
                            alertDialog.dismiss();
                            connectivityDetector.showAlertDialog(SelectService.this,"Error","Select Type to Continue");
                        }
                    }
                });
                self.setChecked(true);
                self.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        other.setChecked(false);
                    }
                });
                other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        self.setChecked(false);
                    }
                });

               alertDialog.show();
            }
        });

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

    private void getLastTransaction() {
        final ProgressDialog progressDialog = new ProgressDialog(SelectService.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);

                            try{
                                JSONObject jsonObject = json_res.getJSONObject("response");
                                String get_date = jsonObject.getString("date");
                                String get_transaction_number = jsonObject.getString("transaction_number");
                                String get_description = jsonObject.getString("description");
                                String get_amount = jsonObject.getString("amount");
                                String get_charge = jsonObject.getString("charge");
                                String get_nature = jsonObject.getString("nature");

                                progressDialog.dismiss();

                                date.setText(API.DateFormat(get_date));
                                nature.setText(get_description);
                                amount.setText(API.formatCurrency(get_amount));
                            } catch (Exception e){
                                progressDialog.dismiss();

                                e.printStackTrace();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(SelectService.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        Toast.makeText(SelectService.this,error.toString(),Toast.LENGTH_SHORT).show();
                        //   MDToast mdToast = MDToast.makeText(Login.this,error.toString(),Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);
                        //   mdToast.show();
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
                params.put(PHONE, get_Mobile);
                params.put(API.PIN, get_PIN);
                params.put(API.SERVICE_LAST_TRANSACTION_SERVICE_ID, service_id);
                params.put(API.SERVICE_ID, LAST_TRANSACTION_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
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
    private void openPayAccount() {
        LayoutInflater factory = LayoutInflater.from(SelectService.this);
        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.activity_account__num, null);
        final EditText account_no = (EditText)textEntryView.findViewById(R.id.account_no);
        Button submit = (Button)textEntryView.findViewById(R.id.submit);

        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        alert.setCancelable(true);
        final AlertDialog alertDialog = alert.setView(textEntryView).create();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_ac_number = account_no.getText().toString();
                if (user_ac_number.equals("")){
                    Toast.makeText(SelectService.this,"Enter Account Number",Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    openPayAmount();
                }
            }
        });
        alertDialog.show();
    }

    private void openPayAmount() {
        LayoutInflater factory = LayoutInflater.from(SelectService.this);
        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.activity_amount, null);
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        final EditText amount = (EditText)textEntryView.findViewById(R.id.account_no);
        amount.addTextChangedListener(new MoneyTextWatcher(amount));
        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        alert.setCancelable(true);
        final AlertDialog alertDialog =alert.setView(textEntryView).create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_amount = amount.getText().toString();
                if (user_amount.equals("")){
                    Toast.makeText(SelectService.this,"Enter Amount",Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    openPayConfirmation();
                }

            }
        });
        alertDialog.show();
    }

    private void openPayConfirmation() {
        LayoutInflater factory = LayoutInflater.from(SelectService.this);
        View textEntryView = factory.inflate(R.layout.activity_confirmation, null);
        final TextView service_name = (TextView) textEntryView.findViewById(R.id.service_name);
        TextView amount = (TextView) textEntryView.findViewById(R.id.amount);
        service_name.setText(ser_name);
        String a= user_amount.replace(",","");
        try {
            amount.setText(API.formatCurrency(a));
        } catch (Exception e){
            e.printStackTrace();
        }
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button)textEntryView.findViewById(R.id.btn_cancel);
        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        alert.setCancelable(true);

        final AlertDialog alertDialog = alert.setView(textEntryView).create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                openPayPINDialog();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void openPayPINDialog() {
        LayoutInflater factory = LayoutInflater.from(SelectService.this);
        final View textEntryView = factory.inflate(R.layout.activity_pin, null);
        final EditText pin = (EditText)textEntryView.findViewById(R.id.pin);
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        alert.setCancelable(true);
        final AlertDialog alertDialog = alert.setView(textEntryView).create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_pin_number = pin.getText().toString();
                if (user_pin_number.equals("")){
                    Toast.makeText(SelectService.this,"Enter PIN Number",Toast.LENGTH_SHORT).show();
                } else {
                    String get_Pay_For = sp.getString(PAY_FOR,"");
                    if (get_Pay_For.equals("self")){
                        if (subscribed.equals("0")){
                            alertDialog.dismiss();
                            Pay_UnLink_Service();
                        } else {
                            alertDialog.dismiss();
                            Pay_Link_Service();
                        }
                    } else if (get_Pay_For.equals("other")){
                        alertDialog.dismiss();
                        Pay_Other();
                    }

                }
            }
        });
        alertDialog.show();
        }

    private void Pay_Other() {
        final ProgressDialog progressDialog = new ProgressDialog(SelectService.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);

                            JSONArray jsonArray = json_res.getJSONArray("response");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                get_Status = jsonObject.getString("status");
                                get_transaction_id = jsonObject.getString("transaction_id");
                                get_provider_id = jsonObject.getString("provider_id");
                                get_date = jsonObject.getString("date");
                                get_time = jsonObject.getString("time");
                                get_receipt_no = jsonObject.getString("receipt_no");
                                get_amount = jsonObject.getString("amount");
                                get_service_charge = jsonObject.getString("service_charge");
                                get_vat_amount = jsonObject.getString("vat_amount");
                                get_balance = jsonObject.getString("balance");
                                get_card_no = jsonObject.getString("card_no");
                                get_account_no = jsonObject.getString("account_no");
                                get_service_id = jsonObject.getString("service_id");
                                get_service_name = jsonObject.getString("service_name");
                                get_service_provider = jsonObject.getString("service_provider");
                                get_message = jsonObject.getString("message");
                            }
                            if (get_Status.equals("PASS")){
                             //   finish();
                                progressDialog.dismiss();
                                openSuccessDialog(get_transaction_id,get_provider_id,get_date,get_time,get_receipt_no,get_amount,get_service_charge,get_vat_amount,
                                        get_balance,get_card_no,get_account_no,get_service_id,get_service_name,get_provider_id,get_message);                            } else if (get_Status.equals("FAIL")){
                                progressDialog.dismiss();
                                connectivityDetector.showAlertDialog(SelectService.this,"Error", get_message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            connectivityDetector.showAlertDialog(SelectService.this,"Error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        connectivityDetector.showAlertDialog(SelectService.this,"Error", error.toString());
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
                params.put(PHONE, get_Mobile);
                params.put(API.PIN, user_pin_number);
                params.put(API.PAYMENT_SERVICE_ID, service_id);
                params.put(API.AMOUNT,user_amount.replace(",",""));
                params.put(API.LINKED_PAYMENT, VALUE_OTHER_PAYMENT);
                params.put(API.SERVICE_ACCOUNT, user_ac_number);
                params.put(API.SERVICE_ID, PAY_OTHER_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void Pay_UnLink_Service() {
        final ProgressDialog progressDialog = new ProgressDialog(SelectService.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);

                            JSONArray jsonArray = json_res.getJSONArray("response");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                get_Status = jsonObject.getString("status");
                                get_transaction_id = jsonObject.getString("transaction_id");
                                get_provider_id = jsonObject.getString("provider_id");
                                get_date = jsonObject.getString("date");
                                get_time = jsonObject.getString("time");
                                get_receipt_no = jsonObject.getString("receipt_no");
                                get_amount = jsonObject.getString("amount");
                                get_service_charge = jsonObject.getString("service_charge");
                                get_vat_amount = jsonObject.getString("vat_amount");
                                get_balance = jsonObject.getString("balance");
                                get_card_no = jsonObject.getString("card_no");
                                get_account_no = jsonObject.getString("account_no");
                                get_service_id = jsonObject.getString("service_id");
                                get_service_name = jsonObject.getString("service_name");
                                get_service_provider = jsonObject.getString("service_provider");
                                get_message = jsonObject.getString("message");
                            }
                            if (get_Status.equals("PASS")){
                                progressDialog.dismiss();
                                openSuccessDialog(get_transaction_id,get_provider_id,get_date,get_time,get_receipt_no,get_amount,get_service_charge,get_vat_amount,
                                        get_balance,get_card_no,get_account_no,get_service_id,get_service_name,get_provider_id,get_message);

                            } else if (get_Status.equals("FAIL")){
                                progressDialog.dismiss();
                                connectivityDetector.showAlertDialog(SelectService.this,"Error", get_message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            connectivityDetector.showAlertDialog(SelectService.this,"Error",e.toString());

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        connectivityDetector.showAlertDialog(SelectService.this,"Error", error.toString());
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
                params.put(PHONE, get_Mobile);
                params.put(API.PIN, user_pin_number);
                params.put(API.PAYMENT_SERVICE_ID, service_id);
                params.put(API.AMOUNT,user_amount.replace(",",""));
                params.put(API.LINKED_PAYMENT, VALUE_UNLINKED_PAYMENT);
                params.put(API.SERVICE_ACCOUNT, user_ac_number);
                params.put(API.SERVICE_ID, PAY_UNLINK_SERVICE_ID);
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
    private void Pay_Link_Service() {
        final ProgressDialog progressDialog = new ProgressDialog(SelectService.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);

                            JSONArray jsonArray = json_res.getJSONArray("response");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                get_Status = jsonObject.getString("status");
                                get_transaction_id = jsonObject.getString("transaction_id");
                                get_provider_id = jsonObject.getString("provider_id");
                                get_date = jsonObject.getString("date");
                                get_time = jsonObject.getString("time");
                                get_receipt_no = jsonObject.getString("receipt_no");
                                get_amount = jsonObject.getString("amount");
                                get_service_charge = jsonObject.getString("service_charge");
                                get_vat_amount = jsonObject.getString("vat_amount");
                                get_balance = jsonObject.getString("balance");
                                get_card_no = jsonObject.getString("card_no");
                                get_account_no = jsonObject.getString("account_no");
                                get_service_id = jsonObject.getString("service_id");
                                get_service_name = jsonObject.getString("service_name");
                                get_service_provider = jsonObject.getString("service_provider");
                                get_message = jsonObject.getString("message");


                            }
                            if (get_Status.equals("PASS")){
                              //  finish();
                                progressDialog.dismiss();
                                openSuccessDialog(get_transaction_id,get_provider_id,get_date,get_time,get_receipt_no,get_amount,get_service_charge,get_vat_amount,
                                        get_balance,get_card_no,get_account_no,get_service_id,get_service_name,get_provider_id,get_message);
                            } else if (get_Status.equals("FAIL")){
                                progressDialog.dismiss();
                                connectivityDetector.showAlertDialog(SelectService.this,"Error", get_message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            connectivityDetector.showAlertDialog(SelectService.this,"Error", e.toString());

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        connectivityDetector.showAlertDialog(SelectService.this,"Error", error.toString());
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
                params.put(PHONE, get_Mobile);
                params.put(API.PIN, user_pin_number);
                params.put(API.PAYMENT_SERVICE_ID, service_id);
                params.put(API.AMOUNT,user_amount.replace(",",""));
                params.put(API.LINKED_PAYMENT, VALUE_LINKED_PAYMENT);
                params.put(API.SERVICE_ID, PAY_LINK_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void openPINDialog() {
        LayoutInflater factory = LayoutInflater.from(SelectService.this);
        final View textEntryView = factory.inflate(R.layout.activity_pin, null);
        final EditText pin = (EditText)textEntryView.findViewById(R.id.pin);
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        alert.setCancelable(true);
        final AlertDialog alertDialog = alert.setView(textEntryView).create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_pin_number = pin.getText().toString();
                if (user_pin_number.equals("")){
                    connectivityDetector.showAlertDialog(SelectService.this,"Error","Enter PIN Number");
                } else {
                    if (subscribed.equals("0")){
                        alertDialog.dismiss();
                        Link_Service();
                    } else {
                        alertDialog.dismiss();
                        UnLink_Service();
                    }

                }
            }
        });
        alertDialog.show();
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
        } else if (id == R.id.nav_account_details) {
            Intent intent = new Intent(SelectService.this,MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_view_transaction){
            Intent intent = new Intent(SelectService.this,ViewTransaction.class);
            startActivity(intent);

        }  else if (id == R.id.nav_pay_qrcode) {
            IntentIntegrator integrator = new IntentIntegrator(SelectService.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        } else if (id == R.id.nav_block_card) {

        } else if (id == R.id.nav_change_pin) {
            ChangePIN_Dialog cdd=new ChangePIN_Dialog(SelectService.this);
            cdd.show();
        } else if (id == R.id.nav_logout) {
            openLogoutConfirmation();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void openLogoutConfirmation() {
        LayoutInflater factory = LayoutInflater.from(SelectService.this);
        final View textEntryView = factory.inflate(R.layout.activity_confirm, null);
        TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
        tv_message.setText("Are you sure you want to Logout?");
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button)textEntryView.findViewById(R.id.btn_cancel);
        btn_ok.setText("Yes");
        btn_cancel.setText("No");
        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        alert.setCancelable(true);
        final AlertDialog alertDialog = alert.setView(textEntryView).create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
                updateLoginPrefrence();
                Intent intent = new Intent(SelectService.this,Login.class);
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
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void saveLoginPrefrence(String check) {
        if (check.equals("link")){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(VALIDATE, "link_unlink");
            editor.putString(SUBSCRIBED,subscribed);
            editor.putString(SERVICE_ID,service_id);
            editor.putString(NAME,customer_name);
            editor.apply();
            editor.commit();
        } else if (check.equals("pay")){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(VALIDATE, "pay");
            editor.apply();
            editor.commit();
        }

    }


    private void Link_Service() {
        final ProgressDialog progressDialog = new ProgressDialog(SelectService.this);
        progressDialog.setMessage("Linking ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);
                            JSONObject jsonObject = json_res.getJSONObject("response");
                            String get_code = jsonObject.getString("code");
                            String get_message = jsonObject.getString("message");
                            String get_description = jsonObject.getString("description");

                            if (get_code.equals("200")){

                                if (from.equals("single")){
                                    for (int i=0;i<serviceList.size();i++){
                                        Service_Model service = serviceList.get(i);
                                        if (service_id.equals(service.getService_id())){
                                            service.setSubscribed("1");
                                            service_account.setText(user_ac_number);
                                            }
                                        }
                                    link.setVisibility(View.GONE);
                                    unlink.setVisibility(View.VISIBLE);
                                } else if (from.equals("group")){
                                    for(int i=0;i<Login.array_subscribe.size();i++){
                                        if (service_id.equals(Login.array_service_id.get(i))){
                                           Login.array_subscribe.set(i,"1");
                                           String get = Login.array_subscribe.get(i);
                                           service_account.setText(user_ac_number);
                                        }
                                    }
                                    link.setVisibility(View.GONE);
                                    unlink.setVisibility(View.VISIBLE);

                                }


                                connectivityDetector.openSuccessDialog(SelectService.this,"Success", get_description);
                                progressDialog.dismiss();

                            } else {

                                connectivityDetector.showAlertDialog(SelectService.this,"Error", get_description);
                                progressDialog.dismiss();

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
                        connectivityDetector.showAlertDialog(SelectService.this,"Error", error.toString());

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
                params.put(PHONE, get_Mobile);
                params.put(API.PIN, user_pin_number);
                params.put(API.SERVICE_ACCOUNT, user_ac_number);
                params.put(API.LINK_SERVICE,service_id);
                params.put(API.SERVICE_ID, LINK_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void openErrorDialog(String get_description) {
        LayoutInflater factory = LayoutInflater.from(SelectService.this);
        View textEntryView = factory.inflate(R.layout.activity_error, null);
        final TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        tv_message.setText(get_description);
        alert.setCancelable(true);

        final AlertDialog alertDialog = alert.setView(textEntryView).create();
        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 10000);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void openSuccessDialog(final String get_transaction_id, final String getProviderId, final String get_date, final String get_time, final String get_receipt_no, final String get_amount, final String get_service_charge, final String get_vat_amount, String get_balance, String get_card_no, final String get_account_no, String get_service_id, final String get_service_name, final String get_provider_id, String get_description) {
        LayoutInflater factory = LayoutInflater.from(SelectService.this);
        View textEntryView = factory.inflate(R.layout.activity_success, null);
        final TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        tv_message.setText(get_description);
        alert.setCancelable(true);

        final AlertDialog alertDialog = alert.setView(textEntryView).create();
        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    finish();
                    Intent intent = new Intent(SelectService.this,Receipt.class);
                    intent.putExtra(URL,url);
                    intent.putExtra(TRANSACTION_ID,get_transaction_id);
                    intent.putExtra(PROVIDER_ID,getProviderId);
                    intent.putExtra(DATE,get_date);
                    intent.putExtra(TIME,get_time);
                    intent.putExtra(RECEIPT_NO,get_receipt_no);
                    intent.putExtra(AMOUNT,get_amount);
                    intent.putExtra(SERVICE_CHARGE,get_service_charge);
                    intent.putExtra(VAT,get_vat_amount);
                    intent.putExtra(SERVICE_ACCOUNT_NO,get_account_no);
                    intent.putExtra(SERVICE_NAME,get_service_name);
                    intent.putExtra(SERVICE_PROVIDER,get_service_provider);
                    startActivity(intent);
                }
            }
        };
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 6000);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertDialog.dismiss();
                finish();
                Intent intent = new Intent(SelectService.this,Receipt.class);
                intent.putExtra(URL,url);
                intent.putExtra(TRANSACTION_ID,get_transaction_id);
                intent.putExtra(PROVIDER_ID,getProviderId);
                intent.putExtra(DATE,get_date);
                intent.putExtra(TIME,get_time);
                intent.putExtra(RECEIPT_NO,get_receipt_no);
                intent.putExtra(AMOUNT,get_amount);
                intent.putExtra(SERVICE_CHARGE,get_service_charge);
                intent.putExtra(VAT,get_vat_amount);
                intent.putExtra(SERVICE_ACCOUNT_NO,get_account_no);
                intent.putExtra(SERVICE_NAME,get_service_name);
                intent.putExtra(SERVICE_PROVIDER,get_service_provider);
                startActivity(intent);
            }
        });

        alertDialog.show();

    }

    private void UnLink_Service() {
        final ProgressDialog progressDialog = new ProgressDialog(SelectService.this);
        progressDialog.setMessage("Linking ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);
                            JSONObject jsonObject = json_res.getJSONObject("response");
                            String get_code = jsonObject.getString("code");
                            String get_message = jsonObject.getString("message");
                            String get_description = jsonObject.getString("description");

                            if (get_code.equals("200")){
                                if (from.equals("single")){
                                    for (int i=0;i<serviceList.size();i++){
                                        Service_Model service = serviceList.get(i);
                                        if (service_id.equals(service.getService_id())){
                                            service.setSubscribed("0");
                                        }

                                    }
                                    unlink.setVisibility(View.INVISIBLE);
                                    link.setVisibility(View.VISIBLE);

                                } else if (from.equals("group")){
                                    for(int i=0;i<Login.array_subscribe.size();i++){
                                        if (service_id.equals(Login.array_service_id.get(i))){
                                            Login.array_subscribe.set(i,"0");
                                            String get = Login.array_subscribe.get(i);
                                            service_account.setText(user_ac_number);
                                        }
                                    }
                                    unlink.setVisibility(View.INVISIBLE);
                                    link.setVisibility(View.VISIBLE);
                                }

                                connectivityDetector.openSuccessDialog(SelectService.this,"Success", get_description);
                                progressDialog.dismiss();

                            } else {
                                connectivityDetector.showAlertDialog(SelectService.this,"Error", get_description);
                                progressDialog.dismiss();

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
                        connectivityDetector.showAlertDialog(SelectService.this,"Error", error.toString());
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
                params.put(PHONE, get_Mobile);
                params.put(API.PIN, user_pin_number);
                params.put(API.LINK_SERVICE,service_id);
                params.put(API.SERVICE_ID, UNLINK_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }


}
