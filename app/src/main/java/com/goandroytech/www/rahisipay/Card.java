package com.goandroytech.www.rahisipay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.goandroytech.www.rahisipay.apicalls.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.goandroytech.www.rahisipay.apicalls.API.BASE_URL;
import static com.goandroytech.www.rahisipay.apicalls.API.BLOCK_CARD_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PHONE;
import static com.goandroytech.www.rahisipay.apicalls.API.UNBLOCK_CARD_SERVICE_ID;

public class Card extends AppCompatActivity {
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
        setContentView(R.layout.activity_card);

        reason = (EditText)findViewById(R.id.reason);
        submit = (Button)findViewById(R.id.submit);
        sp = getSharedPreferences(MyPref,MODE_PRIVATE);
        queue = Volley.newRequestQueue(Card.this);
        sp = getSharedPreferences(MyPref, MODE_PRIVATE);
        customer_name = sp.getString(NAME, null);
        get_Mobile = sp.getString(MOBILE,null);
        get_card_status = sp.getString(CARD_STATUS,null);

        if (get_card_status.equals("1")){
            getSupportActionBar().setTitle("Block Card");
            LayoutInflater factory = LayoutInflater.from(Card.this);
            final View textEntryView = factory.inflate(R.layout.activity_confirm, null);
            TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
            tv_message.setText("Are you sure you want to block your card?");
            Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button)textEntryView.findViewById(R.id.btn_cancel);
            final AlertDialog.Builder alert = new AlertDialog.Builder(Card.this);
            alert.setCancelable(true);
            final AlertDialog alertDialog = alert.setView(textEntryView).create();

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    finish();
                }
            });

            alertDialog.show();
        } else if (get_card_status.equals("5")){
            getSupportActionBar().setTitle("UnBlock Card");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_pin_number = reason.getText().toString();
                if (user_pin_number.equals("")){
                    reason.setError("Enter PIN");
                } else {
                    if (get_card_status.equals("1")){
                        Block_Card();
                    } else if (get_card_status.equals("5")){
                        UNBlock_Card();
                    }
                }
            }
        });
    }

    private void UNBlock_Card() {
        final ProgressDialog progressDialog = new ProgressDialog(Card.this);
        progressDialog.setMessage("UnBlocking ...");
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
                                saveCardStatus("1");
                                Toast.makeText(Card.this,get_description,Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Card.this,get_description,Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Card.this,error.toString(),Toast.LENGTH_SHORT).show();

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
                params.put(API.SERVICE_ID, UNBLOCK_CARD_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void Block_Card() {
        final ProgressDialog progressDialog = new ProgressDialog(Card.this);
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
                                saveCardStatus("5");
                                Toast.makeText(Card.this,get_description,Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Card.this,get_description,Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Card.this,error.toString(),Toast.LENGTH_SHORT).show();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void saveCardStatus(String get_card_state) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CARD_STATUS, get_card_state);
        editor.apply();
        editor.commit();



    }
}
