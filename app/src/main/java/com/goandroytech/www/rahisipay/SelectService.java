package com.goandroytech.www.rahisipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.goandroytech.www.rahisipay.Dialog.Account_Num;
import com.goandroytech.www.rahisipay.Dialog.ChangePIN_Dialog;
import com.goandroytech.www.rahisipay.Dialog.PIN;
import com.goandroytech.www.rahisipay.Dialog.Pay;
import com.goandroytech.www.rahisipay.Model.Service_Model;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.goandroytech.www.rahisipay.apicalls.API.BASE_URL;
import static com.goandroytech.www.rahisipay.apicalls.API.LINK_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.LOGIN_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PHONE;
import static com.goandroytech.www.rahisipay.apicalls.API.UNLINK_SERVICE_ID;

public class SelectService extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView service_name,service_account,service_title;
    Button link_unlink,pay;
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
    String get_Mobile;
    String user_ac_number,user_pin_number;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(SelectService.this);
        service_name = (TextView) findViewById(R.id.service_name);
        service_title = (TextView) findViewById(R.id.service_title);
        service_account = (TextView) findViewById(R.id.service_account);
        link_unlink = (Button) findViewById(R.id.link_unlink);
        logo_url = (ImageView) findViewById(R.id.logo_url);
        pay = (Button) findViewById(R.id.pay);
        sp = getSharedPreferences(MyPref, MODE_PRIVATE);
        customer_name = sp.getString(NAME, null);
        get_Mobile = sp.getString(MOBILE,null);
        Intent intent = getIntent();
        url = intent.getStringExtra("logo_url");
        ser_name = intent.getStringExtra("service_name");
        subscribed = intent.getStringExtra("subscribed");
        service_id = intent.getStringExtra("service_id");
        link_unlink.setText("Link");
        if (!subscribed.equals("0")) {
            link_unlink.setText("Unlink");
            subscriptionAccount = intent.getStringExtra("subscriptionAccount");
        }

        Picasso.with(SelectService.this).load(url).into(logo_url);
        service_name.setText(ser_name);
        service_title.setText(ser_name);


        link_unlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLoginPrefrence("link");
                if (subscribed.equals("0")) {
                    // Account_Num acc = new Account_Num(SelectService.this);
                    // acc.show();
                    LayoutInflater factory = LayoutInflater.from(SelectService.this);

                    final View textEntryView = factory.inflate(R.layout.activity_account__num, null);

                    final EditText account_no = (EditText)textEntryView.findViewById(R.id.account_no);

                    final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
                    alert.setIcon(R.drawable.rahisi_logo)
                            .setTitle("Rahisi Pay").setView(textEntryView).setPositiveButton("Submit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {dialog.cancel();
                                    user_ac_number = account_no.getText().toString();
                                    if(user_ac_number.equals("")){
                                        Toast.makeText(SelectService.this,"Enter Account Number to Continue",Toast.LENGTH_SHORT).show();
                                    } else {
                                        openPINDialog();
                                    }
                                }
                            });/*.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                }
                            }); */

                   /* submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user_ac_number = account_no.getText().toString();
                            if(user_ac_number.equals("")){
                                Toast.makeText(SelectService.this,"Enter Account Number to Continue",Toast.LENGTH_SHORT).show();
                            } else {
                                openPINDialog();
                            }
                        }
                    }); */
                    alert.show();


            } else {
               // PIN pin = new PIN(SelectService.this);
               // pin.show();
                    openPINDialog();
            }


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
                RadioButton other = (RadioButton)textEntryView.findViewById(R.id.other);
                final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
                alert.setIcon(R.drawable.rahisi_logo)
                        .setTitle("Rahisi Pay").setView(textEntryView).setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {dialog.cancel();
                               if (self.isChecked()){
                                   openPayAccount();
                               }
                            }
                        });/*.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                }
                            }); */

                   /* submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user_ac_number = account_no.getText().toString();
                            if(user_ac_number.equals("")){
                                Toast.makeText(SelectService.this,"Enter Account Number to Continue",Toast.LENGTH_SHORT).show();
                            } else {
                                openPINDialog();
                            }
                        }
                    }); */
                alert.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void openPayAccount() {
        LayoutInflater factory = LayoutInflater.from(SelectService.this);
        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.activity_account__num, null);
        final EditText account_no = (EditText)textEntryView.findViewById(R.id.account_no);



        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        alert.setIcon(R.drawable.rahisi_logo)
                .setTitle("Rahisi Pay").setView(textEntryView).setPositiveButton("Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        user_ac_number = account_no.getText().toString();
                        if (user_ac_number.equals("")){
                            Toast.makeText(SelectService.this,"Enter Account Number",Toast.LENGTH_SHORT).show();
                        } else {
                            if (subscribed.equals("0")){
                             //   Pay_UnLink_Service();
                            } else {
                                //Pay_UnLink_Service();
                            }
                        }

                    }
                });/*.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                }
                            }); */
        alert.show();
    }

    private void openPINDialog() {

        LayoutInflater factory = LayoutInflater.from(SelectService.this);

        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.activity_pin, null);

        final EditText pin = (EditText)textEntryView.findViewById(R.id.pin);
        TextView txt_customer_name = (TextView)textEntryView.findViewById(R.id.customer_name);
        TextView txt = (TextView)textEntryView.findViewById(R.id.txt);


        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectService.this);
        alert.setIcon(R.drawable.rahisi_logo)
                .setTitle("Rahisi Pay").setView(textEntryView).setPositiveButton("Submit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    user_pin_number = pin.getText().toString();
                                    if (user_pin_number.equals("")){
                                        Toast.makeText(SelectService.this,"Enter PIN Number",Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (subscribed.equals("0")){
                                            Link_Service();
                                        } else {

                                        }
                                    }

                                }
                            });/*.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                }
                            }); */
        alert.show();
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

        } else if (id == R.id.nav_block_card) {

        } else if (id == R.id.nav_change_pin) {
            ChangePIN_Dialog cdd=new ChangePIN_Dialog(SelectService.this);
            cdd.show();
        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(SelectService.this);
            builder1.setMessage("Do You Want to Logout?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                            Intent intent = new Intent(SelectService.this,Login.class);
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
                                finish();
                                Toast.makeText(SelectService.this,get_description,Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            } else {
                                Toast.makeText(SelectService.this,get_description,Toast.LENGTH_SHORT).show();
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
                params.put(API.PIN, user_pin_number);
                params.put(API.SERVICE_ACCOUNT, user_ac_number);
                params.put(API.LINK_SERVICE,service_id);
                params.put(API.SERVICE_ID, LINK_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
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
                                Toast.makeText(SelectService.this,get_description,Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            } else {
                                Toast.makeText(SelectService.this,get_description,Toast.LENGTH_SHORT).show();
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
                params.put(API.PIN, user_pin_number);
                params.put(API.LINK_SERVICE,service_id);
                params.put(API.SERVICE_ID, UNLINK_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }


}
