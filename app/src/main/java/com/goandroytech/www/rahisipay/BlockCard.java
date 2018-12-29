package com.goandroytech.www.rahisipay;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goandroytech.www.rahisipay.Dialog.ChangePIN_Dialog;
import com.goandroytech.www.rahisipay.Dialog.PIN;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.goandroytech.www.rahisipay.apicalls.API.BASE_URL;
import static com.goandroytech.www.rahisipay.apicalls.API.BLOCK_CARD_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PHONE;

public class BlockCard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText reason;
    Button submit;
    SharedPreferences sp;
    String MyPref = "mysharedpref";
    String VALIDATE = "val";
    String customer_name;
    String NAME ="name";
    String MOBILE = "mobile";
    String get_Mobile;
    public static String ID ="id";
    String user_pin_number;
    RequestQueue queue;
    String CARD_STATUS ="card_state";
    String get_card_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_card);
        reason = (EditText)findViewById(R.id.reason);
        submit = (Button)findViewById(R.id.submit);
        sp = getSharedPreferences(MyPref,MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(BlockCard.this);
        sp = getSharedPreferences(MyPref, MODE_PRIVATE);

        customer_name = sp.getString(NAME, null);
        get_Mobile = sp.getString(MOBILE,null);
        get_card_status = sp.getString(CARD_STATUS,null);
        LayoutInflater factory = LayoutInflater.from(BlockCard.this);

        final View textEntryView = factory.inflate(R.layout.activity_confirm, null);

        //final TextView msg = (TextView)textEntryView.findViewById(R.id.msg);

     //   msg.setText("Do you want to block the card?");
        final AlertDialog.Builder alert = new AlertDialog.Builder(BlockCard.this);
        alert.setCancelable(false);
        alert.setView(textEntryView).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alert.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_pin_number = reason.getText().toString();
                if (user_pin_number.equals("")){
                    reason.setError("Enter PIN");
                } else {
                   Block_Card();
                }
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


    private void saveLoginPrefrence() {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(VALIDATE, "block");
            editor.apply();
            editor.commit();
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
            Intent intent = new Intent(BlockCard.this,MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_view_transaction){
            Intent intent = new Intent(BlockCard.this,ViewTransaction.class);
            startActivity(intent);

        }  else if (id == R.id.nav_pay_qrcode) {
            IntentIntegrator integrator = new IntentIntegrator(BlockCard.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        } else if (id == R.id.nav_block_card) {

        } else if (id == R.id.nav_change_pin) {
            ChangePIN_Dialog cdd=new ChangePIN_Dialog(BlockCard.this);
            cdd.show();
        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(BlockCard.this);
            builder1.setMessage("Do You Want to Logout?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                            Intent intent = new Intent(BlockCard.this,Login.class);
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

    private void Block_Card() {
        final ProgressDialog progressDialog = new ProgressDialog(BlockCard.this);
        progressDialog.setMessage("Blocking ...");
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

                            if (get_code.equals("200")) {
                                progressDialog.dismiss();
                                Toast.makeText(BlockCard.this,get_description,Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(BlockCard.this,get_description,Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BlockCard.this,error.toString(),Toast.LENGTH_SHORT).show();

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
                params.put(API.SERVICE_ID, BLOCK_CARD_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }

}
