package com.goandroytech.www.rahisipay.Pay_Visa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.goandroytech.www.rahisipay.Login;
import com.goandroytech.www.rahisipay.Model.Msg;
import com.goandroytech.www.rahisipay.R;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.goandroytech.www.rahisipay.apicalls.API;

import org.json.JSONException;
import org.json.JSONObject;


public class CreatemPin extends AppCompatActivity {
    Button Create_mpin;
    com.alimuzaffar.lib.pin.PinEntryEditText pin;
    private ProgressDialog mProgressDialog;
    private String deviceID,login;
    private String newString,success,failure,fromlogin,forex;
    private String amount, payeeName;
    private UhuruDataSource db;
    private int retries=0;
    private SharedPreferences pref;
    private JSONObject forexData;
    private String currency="834";
    private String destination_amount="0";
    private boolean status=false;

    /*
     *First method to be called
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_createm_pin);

        db = new UhuruDataSource(CreatemPin.this);

        pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);

        login = pref.getString("FromLogin", "2");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
                success = null;
                failure = null;

            } else {
                try {
                    newString = extras.getString("jsonObject");
                    success = extras.getString("success");
                    failure = extras.getString("failure");
                    forex = extras.getString("Forex");

                    try{
                        forexData = new JSONObject(forex);

                    }catch (JSONException e)
                    {

                    }



                    Log.v("STATEMANNT", "JUU---->"+newString);

                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }


            }
        } else {
            newString= (String) savedInstanceState.getSerializable("jsonObject");
        }




        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        deviceID = tm.getDeviceId(); */
       deviceID="359966087525738";
        Log.v("STATEMANNT", "JUU2---->"+newString);

        mProgressDialog = new ProgressDialog(this);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        pin = (com.alimuzaffar.lib.pin.PinEntryEditText) findViewById(R.id.txt_pin_entry);

        Log.v("STATEMANNT", "JUU3---->"+newString);

        Create_mpin = (Button) findViewById(R.id.submit);
        Create_mpin.setVisibility(View.GONE);
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                // TODO Auto-generated method stub
                if(s.length()==4)
                {
                    try{
                        JSONObject json = new JSONObject(newString);

                        String currency = Action.getFieldValue(json,"currencyCode");

                        if(currency.equalsIgnoreCase("834"))
                        {
                            Create_mpin.performClick();
                        }else {

                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage(getResources().getString(R.string.Checking));
                            mProgressDialog.setCancelable(false);
                            mProgressDialog.show();
                            //If currency not TZ
                            RequestQueue queue = Action.getInstance(CreatemPin.this).getRequestQueue();

                            String ENDPOINT = Action.getEndpoint("tpbLive.php");//municipalTransactions.php

                            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, ENDPOINT, forexData, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {

                                    Log.v("RESPONSE", "" + jsonObject);

                                    mProgressDialog.dismiss();

                                    String conversionRate = Action.getFieldValue(jsonObject, "conversionRate");
                                    destination_amount = Action.getFieldValue(jsonObject, "destinationAmount");

                                    new AlertDialog.Builder(CreatemPin.this)
                                            .setTitle(R.string.exchange_rate)
                                            .setMessage(
                                                    getString(R.string.kiasi)+" "+destination_amount+"\n"
                                            )
                                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
//                                                   CreatemPin.this.finish();

                                                }

                                            })
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Create_mpin.performClick();

                                                }

                                            }).show();

                                }
                            }, createMyReqErrorListener());

                            myReq.setRetryPolicy(new DefaultRetryPolicy(0,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            queue.add(myReq);
                        }
                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        Create_mpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("STATEMANNT",pin.getText().toString());

                postRequest(pin.getText().toString());

                if(login.equalsIgnoreCase("0"))
                {
                    postWithoutPin();
                }
                else if(login.equalsIgnoreCase("1"))
                {
                    postRequest(pin.getText().toString());
                }


            }
        });

        //Click

        Log.v("STATEMANTE",login);

        if(login.equalsIgnoreCase("0"))
        {
            try{
                JSONObject json = new JSONObject(newString);

                String currency = Action.getFieldValue(json,"currencyCode");

                if(currency.equalsIgnoreCase("834"))
                {
                    Create_mpin.performClick();
                }else {

                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage(getResources().getString(R.string.Checking));
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    //If currency not TZ
                    RequestQueue queue = Action.getInstance(CreatemPin.this).getRequestQueue();

                    String ENDPOINT = Action.getEndpoint("tpbLive.php");//municipalTransactions.php

                    JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, ENDPOINT, forexData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            Log.v("RESPONSE", "" + jsonObject);

                            mProgressDialog.dismiss();

                            String conversionRate = Action.getFieldValue(jsonObject, "conversionRate");
                            destination_amount = Action.getFieldValue(jsonObject, "destinationAmount");
                            //"Exchange Rate: TZS "+conversionRate+"\n"+

                            new AlertDialog.Builder(CreatemPin.this)
                                    .setTitle(R.string.exchange_rate)
                                    .setMessage(getString(R.string.kiasi)+" "+destination_amount+"\n")
                                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
//                                                   CreatemPin.this.finish();

                                        }

                                    })
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Create_mpin.performClick();

                                        }

                                    }).show();

                        }
                    }, createMyReqErrorListener());

                    myReq.setRetryPolicy(new DefaultRetryPolicy(0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(myReq);
                }
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        //End Click



    }

    /*
     *Hide Soft Key Board , call it after submission of data on a form to hide key board
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    /*
     *Post pin to server if response success send trans details else fails
     */
    private void postRequest(String pin){
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        RequestQueue queue =  Action.getInstance(this).getRequestQueue();
        Msg msg=new Msg();
        msg.set_MTI("0610");//0620
        msg.set_processing_code("910000");
        msg.set_transmission_datetime();
        msg.set_stan();
        msg.set_extended_tran_type("9003");
        msg.set_PIN_data(pin);
        msg.set_field_68(deviceID);
        msg.set_field_69(deviceID);
        msg.setBIN("503697");

        String ENDPOINT=API.VISA_URL;//municipalTransactions.php

        JSONObject reqMsg=Action.getJSONObject(msg);
        Log.v("STATEMANNT",""+reqMsg);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,ENDPOINT,reqMsg, createMyReqSuccessListener(), createMyReqErrorListener());

        myReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(myReq);

    }

    /**
     *Response Listener for successful pin enrty
     */
    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                String responseCode = Action.getFieldValue(response,"Status");
                Log.v("RESPONSE_CODE-First",String.valueOf(response));
//                String errorMsg = Action.getFieldValue()
                if(responseCode.equalsIgnoreCase("00")) {

                    postWithoutPin();
                }
                else if(responseCode.equalsIgnoreCase("01") && retries<3)
                {
                    mProgressDialog.dismiss();
                    Action.resolveRspCode(responseCode);
                    new AlertDialog.Builder(CreatemPin.this)
                            .setTitle(R.string.h_login_failure)
                            .setMessage(Action.resolveRspCode(responseCode))
                            .setPositiveButton(android.R.string.yes, null).show();
                    retries=retries+1;

                }
                else if(responseCode.equalsIgnoreCase("01") && retries>2)
                {
                    mProgressDialog.dismiss();
                    new AlertDialog.Builder(CreatemPin.this)
                            .setTitle(R.string.h_login_failure)
                            .setMessage(getString(R.string.max_retries))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(CreatemPin.this, Login.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    CreatemPin.this.finish();
                                }
                            }).show();

                }
                else {
                    Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                    String strName = failure;
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("STRING_I_NEED", strName);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    CreatemPin.this.finish();
                }

            }
        };
    }

    private void postWithoutPin()
    {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        JSONObject reqMsg = null;
        try {

            RequestQueue queue = Action.getInstance(getApplicationContext()).getRequestQueue();
            reqMsg = new JSONObject(newString);
            if(destination_amount.equalsIgnoreCase("0"))
            {
                reqMsg.put("OriginalAmount",Action.getFieldValue(reqMsg, "transactionAmount"));
            }else {
                reqMsg.put("OriginalAmount", destination_amount);
            }
            SharedPreferences.Editor editorOne = pref.edit();
            editorOne.putString("FinalResponse",String.valueOf(reqMsg));
            editorOne.commit();
            Log.v("STATEMANNT", "JJB--->"+String.valueOf(reqMsg));
            amount = Action.getFieldValue(reqMsg, "transactionAmount");
            payeeName=Action.getFieldValue(reqMsg, "recipientName");
            String ENDPOINT = API.VISA_URL;

            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, ENDPOINT, reqMsg, createFinalMyReqSuccessListener(), createMyReqErrorListener());
            myReq.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myReq);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *Response Listener for requested transaction
     */
    private Response.Listener<JSONObject> createFinalMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                Log.v("RESPONSE_CODE-Second",String.valueOf(response));



                mProgressDialog.dismiss();

                hideSoftKeyboard(CreatemPin.this);

                SharedPreferences.Editor editorOne = pref.edit();
                editorOne.putString("VisaResponse",String.valueOf(response));
                editorOne.commit();




                String errorMessage = Action.getFieldValue(response,"errorMessage");
                if(errorMessage.trim().length()!=0){
                    /*if(errorMessage.contains("3001")) {
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = failure;
                        i.putExtra("STRING_I_NEED", "Invalid PAN");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }else{*/
                    Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                    String strName = failure;
                    i.putExtra("STRING_I_NEED", strName);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    CreatemPin.this.finish();
//                    }
                }
                else {

                    String responseCode = Action.getFieldValue(response, "actionCode");
                    if (responseCode.equalsIgnoreCase("00")) {
                        /****
                         *Added on the Digital Receipt
                         ****/
//                        db.transactionDetails(amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = success;
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                        destination_amount="0";
                    }
                    else if (responseCode.equalsIgnoreCase("02") || responseCode.equalsIgnoreCase("01") ) {
//                        db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "REFER CARD ISSUER";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("03")) {
//                        db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Invalid Merchant ID";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("06")) {
//                        db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Error occured";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("07")) {
//                        db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Fraud Account";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("10")) {
//                        db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Partial Approval";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("11")) {
//                         db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Approved (V.I.P)";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("12")) {
//                         db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Invalid Transaction";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("61")) {
//                         db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Exceed Transaction Limit";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("59")) {
//                         db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Suspected Fraud";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("62")) {
                        //db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Restricted Card";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                    else if (responseCode.equalsIgnoreCase("63")) {
//                         db.transactionDetails("Dr "+amount,payeeName, Action.set_transmission_datetime(),"0");

                        Log.v("STATEMANNT", response.toString());
                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = "Security Violation";
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("fromlogin", fromlogin);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }

                    else {

                        Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                        String strName = failure;
                        i.putExtra("STRING_I_NEED", strName);
                        i.putExtra("FinalResponse",newString);
                        i.putExtra("ReturnValue",String.valueOf(response));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        CreatemPin.this.finish();
                    }
                }

            }
        };
    }

    /**
     *Error Listener
     */
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError ex) {

                Log.v("STATEMANNT",ex.toString());
                Log.v("RESPONSE_CODE-Third",String.valueOf(ex));
                Intent i = new Intent(CreatemPin.this, NotificationHome.class);
                String strName = failure;
                i.putExtra("STRING_I_NEED", strName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                CreatemPin.this.finish();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftKeyboard(CreatemPin.this);
        finish();
    }

    /**
     *Back pressed
     */
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(CreatemPin.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

