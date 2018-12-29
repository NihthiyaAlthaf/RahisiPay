package com.goandroytech.www.rahisipay.Pay_Visa;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.view.View;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

//import com.visa.mcc.controls.button.VPrimaryButton;
import com.goandroytech.www.rahisipay.Login;
import com.goandroytech.www.rahisipay.R;
import com.goandroytech.www.rahisipay.Visa;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.visa.mvisa.sdk.MVisaSDK;
import com.visa.mvisa.sdk.MVisaSDKImpl;
//import com.visa.mvisa.sdk.*;
import com.visa.mvisa.sdk.activities.BasePayFriendActivity;
import com.visa.mvisa.sdk.callback.ResponseCallback;
import com.visa.mvisa.sdk.exceptions.InputInvalidException;
import com.visa.mvisa.sdk.models.facade.CardDetails;
import com.visa.mvisa.sdk.models.facade.MVisaConfig;
import com.visa.mvisa.sdk.models.facade.MVisaErrorObject;
import com.visa.mvisa.sdk.models.facade.PayFriendRequest;
import com.visa.mvisa.sdk.models.facade.PayFriendResponse;
import com.visa.mvisa.sdk.models.facade.Payee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;


public class PayFriend extends BasePayFriendActivity implements View.OnClickListener {
    private BroadcastReceiver receiver;
    private PayFriendRequest payFriendRequest;
    private String RESPONSE_CALLBACK_TAG="RESPONSE TAG";
    private String TAG="TAG";
    private boolean newPayee;
    private boolean shouldAddNewPayeeToPayeeList;
    private ArrayList<CardDetails> cardDetailsArrayList;
    private Payee payee;
    private UhuruDataSource db;
    private String phone, name;
    private com.visa.mvisa.controls.views.MVisaListField payeeName, payeeCard;
    private String receipient_name,recipientPrimaryAccountNumber,issuerName,postalCode,country;
    private String city_name,transactionCurrencyCode="834";
    private SharedPreferences pref;
    private boolean status=false;
    private MVisaConfig mVisaConfig;
    private MVisaSDK mVisaSDK;
    private ProgressDialog mProgressDialog;
    private String account, pan,bank="RahisiPay";
    private int primary_Account;
    private String person_name;
    private BasePayFriendActivity end;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            //Start setting up SDK
            mVisaConfig = new MVisaConfig.Builder(getApplicationContext())
                    .backgroundColor("#1A1F71").isNfcOptionOn(false)//#009fe3 for AZANIA
                    .actionButtonActiveColor("#fa9a26")
                    .actionButtonColor("#fa9a26")
//                .payFriendTimeOutInSeconds(30)
                    .build();

            bank = "RahisiPay";

            mVisaSDK = MVisaSDKImpl.getInstance(getApplicationContext(), mVisaConfig);

            mProgressDialog = new ProgressDialog(this);

            pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);
            primary_Account = pref.getInt("defaultIndex", 0);
            account = pref.getString("Account" + String.valueOf(primary_Account), null);
            pan = pref.getString("Pan" + String.valueOf(primary_Account), null);
            bank = pref.getString("BankName", null);


            db = new UhuruDataSource(PayFriend.this);

            db.accountDetails("101508000017","4515280000000024");


            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    phone = null;
                    name = null;
                    status = false;

                } else {
                    try {
                        phone = extras.getString("phone");
                        person_name = extras.getString("person_name");
                        status = extras.getBoolean("status");

                        RequestQueue queue = Action.getInstance(getApplicationContext()).getRequestQueue();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.accumulate("MTI", "0810");
                        jsonObject.accumulate("alias", phone.trim());
                        jsonObject.accumulate("app_id", "PP");

                        String ENDPOINT = API.VISA_URL;

                        JSONObject reqMsg = jsonObject;
                        Log.v("STATEMANNT", String.valueOf(reqMsg));
                        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, ENDPOINT, jsonObject, new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onResponse(final JSONObject response) {

                                Log.v("STATEMANNT", String.valueOf(response));

                                if (Action.getFieldValue(response, "StatusCode").trim().length() > 0 && Action.getFieldValue(response, "visaNetworkInfo").trim().length() == 0) {
                                    Log.v("STATEMANNT=1", "Fail0");
                                    if (name == null) {
                                        Log.v("STATEMANNT=1", "Fail1");
                                        payFriendRequest = setUpCardFallBackRequestObject(phone, person_name);
                                    } else {
                                        try {
                                            Log.v("STATEMANNT=1", "Fail2");
                                            payFriendRequest = setUpCardFallBackRequestObject(phone, person_name);
                                        } catch (NullPointerException e) {

                                        }
                                    }
                                } else if (Action.getFieldValue(response, "reason").trim().length() == 0 && Action.getFieldValue(response, "StatusCode").trim().length() == 0 && Action.getFieldValue(response, "errorMessage").trim().length() == 0 && Action.getFieldValue(response, "visaNetworkInfo").trim().length() == 0) {
                                    Log.v("STATEMANNT=1", "Success");
                                    try {
                                        city_name = Action.getFieldValue(response, "city");
                                        receipient_name = Action.getFieldValue(response, "recipientName");
                                        recipientPrimaryAccountNumber = Action.getFieldValue(response, "recipientPrimaryAccountNumber");
                                        country = Action.getFieldValue(response, "country");
                                        issuerName = Action.getFieldValue(response, "issuerName");
                                        postalCode = Action.getFieldValue(response, "postalCode");
                                        transactionCurrencyCode = Action.getFieldValue(response, "accountLookUpInfo");
                                        JSONObject history = Action.getInnerJsonObject(transactionCurrencyCode);
                                        transactionCurrencyCode = Action.getFieldValue(history, "visaNetworkInfo");
                                        JSONObject history2 = Action.getInnerJsonObject(transactionCurrencyCode);
                                        transactionCurrencyCode = Action.getFieldValue(history2, "billingCurrencyCode");

                                        Log.v("TRANSACTION_CURRENCY", transactionCurrencyCode);
                                        payFriendRequest = setUpAliasRequestObject(recipientPrimaryAccountNumber, receipient_name, phone, issuerName, transactionCurrencyCode);//setUpAliasRequestObject()//payFriend()
                                    } catch (InputInvalidException e) {
                                        payFriendRequest = setUpCardFallBackRequestObject(phone, person_name);
                                    }

                                } else if (Action.getFieldValue(response, "visaNetworkInfo").trim().length() > 0) {
                                    String visaInfo = Action.getFieldValue(response, "visaNetworkInfo");
                                    JSONObject friend = Action.getInnerJsonObject(visaInfo);
                                    transactionCurrencyCode = Action.getFieldValue(friend, "billingCurrencyCode");
                                    String issuer = Action.getFieldValue(friend, "issuerName");
                                    recipientPrimaryAccountNumber = Action.getFieldValue(response, "recipientPrimaryAccountNumber");
                                    payFriendRequest = setUpPanRequestObject(recipientPrimaryAccountNumber, person_name);
                                } else {

                                    if (name == null) {
                                        Log.v("STATEMANNT=1", "Fail1");
                                        payFriendRequest = setUpCardFallBackRequestObject(phone, person_name);

                                    } else {
                                        try {
                                            Log.v("STATEMANNT=1", "Fail2");
                                            payFriendRequest = setUpCardFallBackRequestObject(phone, person_name);
                                        } catch (NullPointerException e) {
                                            payFriendRequest = setUpCardFallBackRequestObject(phone, person_name);
                                        }
                                    }

                                }





                                //Call payMerchant API using MVisaSDK instance
                                mVisaSDK.payFriend(payFriendRequest, new ResponseCallback<PayFriendResponse, MVisaErrorObject, BasePayFriendActivity>() {

                                    @Override
                                    public void onSuccess(final BasePayFriendActivity activity, PayFriendResponse response) {

                                        context = activity;


                                        if (phone.length() == 16) {
                                            recipientPrimaryAccountNumber = phone;
                                        }

                                        if (receipient_name == null) {
                                            receipient_name = person_name;
                                        }


                                        if (response.getPayeeName() == null) {
                                            Toast.makeText(activity, "Error Occured", Toast.LENGTH_SHORT).show();
                                        } else {

                                            //Handle success callback
                                            Log.v("TransactionTime", Action.set_transmission_datetime());
                                            JSONObject jsonObject = new JSONObject();

                                            try {

                                                if (city_name == null || country == null) {
                                                    country = "TZ";
                                                    city_name = "Unknown";
                                                }

                                                JSONArray jsonArray = db.getAllAc();
                                                JSONObject pan = jsonArray.getJSONObject(response.getSenderCardIndex());
                                                JSONObject account = jsonArray.getJSONObject(response.getSenderCardIndex());
                                                jsonObject.accumulate("MTI", "0650");
                                                jsonObject.accumulate("recipientName", receipient_name);
                                                jsonObject.accumulate("payeeIndex", response.getPayeeIndex());
                                                jsonObject.accumulate("senderCardIndex", response.getSenderCardIndex());
                                                jsonObject.accumulate("transactionAmount", response.getTransactionAmount());
                                                jsonObject.accumulate("transactionTime", Action.set_transmission_datetime());
                                                jsonObject.accumulate("senderAccountNumber", Action.getFieldValue(pan, "pan"));
                                                jsonObject.accumulate("accountNumber", Action.getFieldValue(account, "account"));
                                                jsonObject.accumulate("bin", "004255");
                                                jsonObject.accumulate("recipientCardNumber", response.getPayeeCardNumber());
                                                jsonObject.accumulate("country", country);
                                                jsonObject.accumulate("city", city_name);
                                                jsonObject.accumulate("cityName", city_name);
                                                jsonObject.accumulate("sender_name", pref.getString("CustomerName", ""));
                                                jsonObject.accumulate("retrievalReferenceNumber", Action.getRrn(Action.stan()));
                                                jsonObject.accumulate("recipientPrimaryAccountNumber", recipientPrimaryAccountNumber);
                                                jsonObject.accumulate("new", response.isShouldAddNewUserToPayeeList());
                                                jsonObject.accumulate("from", "Fund Transfer");


                                                if (response.isShouldAddNewUserToPayeeList() == true && phone != null) {
                                                    if (db.friendAliasCheck(phone) > 0) {
                                                        Toast.makeText(activity, getString(R.string.user_available), Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        db.recordFriendsAlias(response.getPayeeName(), phone);
                                                    }
                                                } else {

                                                }


                                                final JSONObject json = new JSONObject();
                                                json.accumulate("MTI", "0690");
                                                json.accumulate("recipientName", response.getPayeeName());
                                                json.accumulate("city", city_name);
                                                json.accumulate("currencyCode", transactionCurrencyCode);
                                                json.accumulate("amount", response.getTransactionAmount());
                                                jsonObject.put("currencyCode", transactionCurrencyCode);
                                                JSONObject reqMsg = jsonObject;

                                                SharedPreferences.Editor editor = pref.edit();
                                                editor.putString("FromLogin", "0");
                                                editor.commit();

                                                Log.v("STATEMANNT", String.valueOf(reqMsg));
                                                Intent intent = new Intent(PayFriend.this, CreatemPin.class);
                                                intent.putExtra("jsonObject", String.valueOf(reqMsg));
                                                intent.putExtra("Forex", String.valueOf(json));
                                                intent.putExtra("success", getString(R.string.t_m_s));
                                                intent.putExtra("failure", getString(R.string.t_d));
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                activity.finish();


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (NullPointerException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }

                                    @Override
                                    public void onFlowTimeOut(final BasePayFriendActivity activity, final MVisaErrorObject failureObject) {
                                        //handle flow timeout

                                        Log.v(RESPONSE_CALLBACK_TAG, "Called");
//                                        payFriendRequest = setUpPanRequestObject("Enter Name");
                                        Toast.makeText(getApplicationContext(), failureObject.getErrorMessage(), Toast.LENGTH_LONG).show();
                                        //Exit SDK if needed , if not we can remove this line
                                        Intent intent = new Intent(activity, AllContacts.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                        activity.finish();

                                    }
                                });

                            }
                        }, createMyReqErrorListener());
                        myReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        queue.add(myReq);
                    } catch (JSONException E) {
                        E.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                }
            } else {
                phone = (String) savedInstanceState.getSerializable("phone");
                name = (String) savedInstanceState.getSerializable("name");
            }

            super.onCreate(savedInstanceState);
        }catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }

    }

    public BasePayFriendActivity end(BasePayFriendActivity activity)
    {
        return activity;
    }


    /***
     * Producing Rrn
     */
    public String getRrn(String stan){
        String rrn="";

        rrn=getJulianDate()+stan;


        return rrn;
    }


    /**
     *Returning the Julian date format in the form of ydddhh.
     */
    private  String getJulianDate(){

        String julianDate="";

        Date dt = new Date();

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(dt);

        int curr_year = cal.get(Calendar.YEAR);
        String curr_day_of_yr=String.format("%03d", cal.get(Calendar.DAY_OF_YEAR));
        String curr_hr_of_day=String.format("%02d",cal.get(Calendar.HOUR_OF_DAY));

        //Getting last digit of the year.
        int last_digit=curr_year%10;

        //Getting a number of day of the year
        julianDate=last_digit+curr_day_of_yr+curr_hr_of_day;


        return julianDate;

    }

    /**
     *Error Listener
     */
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError ex) {
                Log.v("STATEMANNT",ex.toString());
                recipientPrimaryAccountNumber="";
                receipient_name="";

                payFriendRequest =setUpPanRequestObject(phone,receipient_name);//setUpAliasRequestObject()//payFriend()

                mVisaConfig = new MVisaConfig.Builder(getApplicationContext())
                        .backgroundColor("#1A1F71").isNfcOptionOn(false)
//                        .payFriendTimeOutInSeconds(30)
                        .build();

                mVisaSDK =  MVisaSDKImpl.getInstance(getApplicationContext(),mVisaConfig);

                db = new UhuruDataSource(PayFriend.this);

                //Call payMerchant API using MVisaSDK instance
                mVisaSDK.payFriend(payFriendRequest, new ResponseCallback<PayFriendResponse, MVisaErrorObject, BasePayFriendActivity>() {

                    @Override
                    public void onSuccess(final BasePayFriendActivity activity, PayFriendResponse response) {


                        //Handle success callback
                        Log.v("TransactionTime",Action.set_transmission_datetime());
                        JSONObject jsonObject = new JSONObject();

                        try{

                            JSONArray jsonArray = db.getAllAc();
                            JSONObject pan = jsonArray.getJSONObject(response.getSenderCardIndex());
                            JSONObject account = jsonArray.getJSONObject(response.getSenderCardIndex());

                            jsonObject.accumulate("MTI","0650");
                            jsonObject.accumulate("payeeCardNumber",response.getPayeeCardNumber());
                            jsonObject.accumulate("payeeName",response.getPayeeName());
                            jsonObject.accumulate("payeeIndex",response.getPayeeIndex());
                            jsonObject.accumulate("senderCardIndex",response.getSenderCardIndex());
                            jsonObject.accumulate("transactionAmount",response.getTransactionAmount());
                            jsonObject.accumulate("transactionTime",Action.set_transmission_datetime());
                            jsonObject.accumulate("senderAccountNumber",Action.getFieldValue(pan,"pan"));
                            jsonObject.accumulate("accountNumber",Action.getFieldValue(account,"account"));
                            jsonObject.accumulate("bin","503697");
                            jsonObject.accumulate("recipientCardNumber",recipientPrimaryAccountNumber);
                            jsonObject.accumulate("recipientPrimaryAccountNumber",recipientPrimaryAccountNumber);
                            jsonObject.accumulate("phone",phone);
                            jsonObject.accumulate("country",country);
                            jsonObject.accumulate("cityName",city_name);
                            jsonObject.accumulate("city",city_name);
                            jsonObject.accumulate("transactionCurrencyCode","834");

                            JSONObject reqMsg = jsonObject;
                            Log.v("STATEMANNT", String.valueOf(reqMsg));
                            Intent intent = new Intent(PayFriend.this, CreatemPin.class);
                            intent.putExtra("jsonObject",String.valueOf(reqMsg));
                            intent.putExtra("success",getString(R.string.t_m_s));
                            intent.putExtra("failure",getString(R.string.t_d));
                            startActivity(intent);
                            activity.finish();


                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        //JSON OBJECT END



                    }
                    @Override
                    public void onFlowTimeOut(BasePayFriendActivity activity, final MVisaErrorObject failureObject) {
                        //handle flow timeout
                        Log.v(RESPONSE_CALLBACK_TAG, "Called");
                        payFriendRequest = setUpPanRequestObject(phone,person_name);
                        Toast.makeText(getApplicationContext(), "session time out", Toast.LENGTH_LONG).show();
                        //Exit SDK if needed , if not we can remove this line
                        Intent intent = new Intent(activity,Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        activity.finish();
                    } });


            }
        };


    }

    private boolean shouldAddNewPayeeToPayeeList(boolean response,String payeeName,String payCardNumber){
        if(response==false){

            return false;
        }
        else{

            //Add to the database
            db.payeeDetails(payeeName,payCardNumber);
            return shouldAddNewPayeeToPayeeList;
        }
    }

    private PayFriendRequest payFriend(){

        try {
            cardDetailsArrayList = new ArrayList<CardDetails>();

            cardDetailsArrayList  = db.getAcDetails(PayFriend.this,bank);
            String currency_code = "834";
            int default_index = 0;
            payFriendRequest = new PayFriendRequest();
            payFriendRequest.setCardDetails(cardDetailsArrayList);
            payFriendRequest.setCurrencyCode(currency_code);
            payFriendRequest.setDefaultSenderCardIndex(default_index);

        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return  payFriendRequest;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btAddNewPayeePanel:
                com.visa.mvisa.controls.views.MVisaListField payeeName = (com.visa.mvisa.controls.views.MVisaListField) findViewById(R.id.card_number_mvlf);
                com.visa.mvisa.controls.views.MVisaListField payeeCard = (com.visa.mvisa.controls.views.MVisaListField) findViewById(R.id.payee_name_mvlf);
                String payeeCard_no= payeeCard.getText().toString().trim();

                String payeeNames = payeeName.getText().toString();

                Payee payee = new Payee();
                payee.setPayeeName(payeeNames);
                payee.setPayeeCardNumber(payeeCard_no);
                ArrayList<Payee> payees = new ArrayList<>();
                payees.add(payee);
                String currency_code ="834";
                int default_index=0;

                payFriendRequest.setCardDetails(cardDetailsArrayList);
                payFriendRequest.setCurrencyCode(currency_code);
                payFriendRequest.setDefaultSenderCardIndex(default_index);
                payFriendRequest.setPayees(payees);


                break;

            case R.id.btAddNewPayee:

                Payee payee1 = new Payee();
                payee1.setPayeeName("James");
                payee1.setPayeeCardNumber("0991");
                ArrayList<Payee> payees2 = new ArrayList<>();
                payees2.add(payee1);
                String currency_code1 ="834";
                int default_index1=1;
                payFriendRequest.setCardDetails(cardDetailsArrayList);
                payFriendRequest.setCurrencyCode(currency_code1);
                payFriendRequest.setDefaultSenderCardIndex(default_index1);
                payFriendRequest.setPayees(payees2);

                break;


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(PayFriend.this,AllContacts.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        SharedPreferences.Editor value3 = pref.edit();
        value3.putString("CurrentPage","1");
        value3.commit();
        Intent intent = new Intent(context, AllContacts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        PayFriend.this.finish();
    }


    @Override
    protected void onRestart () {
        super.onRestart();

        mProgressDialog.dismiss();
        Intent intent = new Intent(PayFriend.this, Login.class); //Main menu
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        PayFriend.this.finish();

    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        PayFriend.this.finish();
    }

    /***********************
     *
     * @param pan
     * @param name
     * @param phone
     * @param issuerName
     * @return
     */
    private PayFriendRequest setUpAliasRequestObject(String pan, String name,String phone,String issuerName,String transCurrencyCode) {
        //Change made 22/12/2018
        cardDetailsArrayList = new ArrayList<CardDetails>();

        cardDetailsArrayList  = db.getAcDetails(PayFriend.this,"Rahisi Pay");

        int default_index = 0;

        if(phone.substring(0,3).equalsIgnoreCase("255"))
        {
            phone="0"+phone.substring(3);
            Log.v("STATEMANNTE", phone);
        }
        else if(phone.substring(0,4).equalsIgnoreCase("+255"))
        {
            phone="0"+phone.substring(4);
            Log.v("STATEMANNTE", phone);
        }



        Log.v("STATEMANNTE","NUMBER");
        payFriendRequest = new PayFriendRequest();
        payFriendRequest.setCardDetails(cardDetailsArrayList);
        payFriendRequest.setCurrencyCode(transCurrencyCode);
        payFriendRequest.setDefaultSenderCardIndex(default_index);
        payFriendRequest.setRecipientMobileNo(phone);
        payFriendRequest.setRecipientName(name);
        payFriendRequest.setRecipientPan(pan);
        payFriendRequest.setNameOnContactList(name);
        payFriendRequest.setPaymentType(PayFriendRequest.AliasPayType.Alias);//Alias
        payFriendRequest.setAliasFlow(true);
        payFriendRequest.setNameOnContactList(name);
        payFriendRequest.setRecipientBank(issuerName);
        payFriendRequest.setNewUser(true);

        return payFriendRequest;
    }

    /***********************
     *
     * @param pan
     * @param receipient_name
     * @return
     */
    private PayFriendRequest setUpPanRequestObject(String pan,String receipient_name) {
        //String pan, String name
        ArrayList<CardDetails> cardDetailsArrayList = new ArrayList<CardDetails>();

        cardDetailsArrayList  = db.getAcDetails(PayFriend.this,"RahisiPay");

        String currencyCode = transactionCurrencyCode;

        int default_index = primary_Account;


        payFriendRequest = new PayFriendRequest();
        payFriendRequest.setCardDetails(cardDetailsArrayList);
        payFriendRequest.setCurrencyCode(currencyCode);
        payFriendRequest.setDefaultSenderCardIndex(default_index);
        payFriendRequest.setRecipientPan(pan.substring(12));
        payFriendRequest.setRecipientName(receipient_name);
        payFriendRequest.setPaymentType(PayFriendRequest.AliasPayType.None);
        payFriendRequest.setAliasFlow(true);
        payFriendRequest.setNameOnContactList(receipient_name);
        payFriendRequest.setRecipientBank(issuerName);
        payFriendRequest.setNewUser(false);
        payFriendRequest.setRecipientMobileNo(pan);

        return payFriendRequest;
    }


    /***********************
     *
     * @param pan
     * @param receipient_name
     * @return
     */
    private PayFriendRequest setUpCardFallBackRequestObject(String pan,String receipient_name) {
        //String pan, String name
        ArrayList<CardDetails> cardDetailsArrayList = new ArrayList<CardDetails>();

        cardDetailsArrayList  = db.getAcDetails(PayFriend.this,bank);

        String currencyCode = "834";

        int default_index = primary_Account;


        payFriendRequest = new PayFriendRequest();
        payFriendRequest.setCardDetails(cardDetailsArrayList);
        payFriendRequest.setCurrencyCode(currencyCode);
        payFriendRequest.setDefaultSenderCardIndex(default_index);
        payFriendRequest.setRecipientPan(pan);
        payFriendRequest.setRecipientName(receipient_name);
        payFriendRequest.setPaymentType(PayFriendRequest.AliasPayType.CardFallback);
        payFriendRequest.setAliasFlow(true);
        payFriendRequest.setNameOnContactList(receipient_name);
        payFriendRequest.setRecipientBank(issuerName);
        payFriendRequest.setNewUser(false);

        return payFriendRequest;
    }


    /***********************
     *On stop
     ***********************/
    @Override
    protected void onStop() {
        super.onStop();
    }

    /***********************
     *Activity paused
     ***********************/
    @Override
    protected void onPause() {
        super.onPause();
    }

    /***********************
     *Activity resumed
     ***********************/
    @Override
    protected void onResume() {
        super.onResume();

        getApplicationContext();

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

    }

    /***********************
     *Action if timeout
     ***********************/
    private class LogOutTimerTask extends TimerTask {

        @Override
        public void run() {

            //redirect user to login screen
            Intent i = new Intent(PayFriend.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }



}

