package com.goandroytech.www.rahisipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.goandroytech.www.rahisipay.Connection.ConnectivityDetector;
import com.goandroytech.www.rahisipay.Pay_Visa.CreatemPin;
import com.goandroytech.www.rahisipay.Pay_Visa.UhuruDataSource;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.visa.mvisa.sdk.MVisaSDK;
import com.visa.mvisa.sdk.MVisaSDKImpl;
import com.visa.mvisa.sdk.activities.BasePayMerchantActivity;
import com.visa.mvisa.sdk.callback.AliasValidationHandler;
import com.visa.mvisa.sdk.callback.PayMerchantAliasResponseHandler;
import com.visa.mvisa.sdk.callback.ResponseCallback;
import com.visa.mvisa.sdk.exceptions.InputInvalidException;
import com.visa.mvisa.sdk.models.facade.AliasResponse;
import com.visa.mvisa.sdk.models.facade.CardDetails;
import com.visa.mvisa.sdk.models.facade.MVisaConfig;
import com.visa.mvisa.sdk.models.facade.MVisaErrorObject;
import com.visa.mvisa.sdk.models.facade.PayFriendRequest;
import com.visa.mvisa.sdk.models.facade.PayMerchantAliasData;
import com.visa.mvisa.sdk.models.facade.PayMerchantRequest;
import com.visa.mvisa.sdk.models.facade.PayMerchantResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import static com.goandroytech.www.rahisipay.Pay_Visa.Action.getJulianDate;

public class Visa extends Activity {
    MVisaConfig mVisaConfig;
    PayMerchantRequest payMerchantRequest;
    MVisaSDK mVisaSDK;
    AliasValidationHandler aliasValidationHandler;
    AliasResponse aliasResponse;
    private SharedPreferences pref;
    ArrayList<CardDetails> cardDetailsArrayList;
    String merchantCategoryCode;
    String alias_data;
    String city_name;
    String receipient_name;
    String country;
    String transactionCurrencyCode="834";
    String issuerName;
    String recipientPrimaryAccountNumber;
    PayFriendRequest payFriendRequest;
    UhuruDataSource db;
    int primary_Account =1;
    String MyPref = "mysharedpref";
    SharedPreferences sp;
    String get_Mobile;
    String MOBILE = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_service);
        sp = getSharedPreferences(MyPref, MODE_PRIVATE);
        get_Mobile = sp.getString(MOBILE,null);
        db = new UhuruDataSource(Visa.this);
        db.accountDetails("101508000017","4515280000000024");
        Drawable image = this.getResources().getDrawable(R.drawable.mvisa_sdk_network_logo_visa_blue);
        ArrayList<Drawable> arrayList = new ArrayList<Drawable>();
        arrayList.add(image);


        MVisaConfig mVisaConfig = new MVisaConfig.Builder(Visa.this)
                .backgroundColor("#1A1F71").isNfcOptionOn(false)//#009fe3 for AZANIA
                .actionButtonActiveColor("#fa9a26")
                .actionButtonColor("#fa9a26")
//                    .payMerchantTimeOutInSeconds(30)
                .networkLogoList(arrayList)
                .build();

        mVisaSDK = MVisaSDKImpl.getInstance(Visa.this, mVisaConfig);

        aliasValidationHandler = new AliasValidationHandler<PayMerchantAliasData, BasePayMerchantActivity>() {
            @Override
            public void doAliasValidation(PayMerchantAliasData aliasData, final BasePayMerchantActivity activity, final PayMerchantAliasResponseHandler<AliasResponse> aliasCallback) {
                try {

                    //alias data input on UI
                    String data = aliasData.getAliasData();

                    alias_data = aliasData.getAliasData();

                    RequestQueue queue = Volley.newRequestQueue(Visa.this);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("MTI","0810");
                    jsonObject.accumulate("alias",data.trim());
                    jsonObject.accumulate("app_id","MP");

                    String ENDPOINT = API.VISA_URL;

                   // reqMsg = jsonObject;
                    Log.v("STATEMANNT", ""+jsonObject);
                    JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, ENDPOINT, jsonObject, new Response.Listener<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(final JSONObject response) {

                            String reason = "";

                            String errorMessage = getFieldValue(response, "errorMessage");

                            reason = getFieldValue(response, "reason");

                            final String visaInfo = getFieldValue(response, "visaNetworkInfo");

                            if (reason.trim().length() == 0 && errorMessage.length() == 0 && visaInfo.length() == 0) {

                                Log.v("TRACER-1", "PASS");

                                city_name = getFieldValue(response, "city");
                                receipient_name = getFieldValue(response, "recipientName");
                                recipientPrimaryAccountNumber = getFieldValue(response, "recipientPrimaryAccountNumber");
                                country = getFieldValue(response, "country");
                                merchantCategoryCode = getFieldValue(response, "merchantCategoryCode");
                                transactionCurrencyCode = getFieldValue(response, "transactionCurrencyCode");
                           //     SharedPreferences.Editor editor = pref.edit();
                          //      editor.putString("CurrencyCode", transactionCurrencyCode);
                           //     editor.commit();


                                aliasResponse = new AliasResponse();
                                aliasResponse.setMerchantCity(city_name);
                                aliasResponse.setMerchantName(receipient_name);
                                aliasResponse.setTransactionCurrencyCode(transactionCurrencyCode);
                                aliasResponse.setCountry(country);
                                aliasResponse.setMerchantCity(city_name);
                                aliasResponse.setRecipientPrimaryAccountNumber(recipientPrimaryAccountNumber);
                                aliasResponse.setMerchantCategoryCode(merchantCategoryCode);
                                aliasCallback.onSuccess(aliasResponse);


                            } else if (visaInfo.length() > 0) {
                                Log.v("TRACER-2", "PASS");

                                city_name = "mVisa payment";
                                receipient_name = "mVisa merchant";
                                country = "TZ";
                                JSONObject merchant_id = getInnerJsonObject(visaInfo);
                                transactionCurrencyCode = getFieldValue(merchant_id, "billingCurrencyCode");
                                issuerName = getFieldValue(merchant_id, "issuerName");
                                recipientPrimaryAccountNumber = getFieldValue(response, "recipientPrimaryAccountNumber");
                                payFriendRequest = setPanPaymentObject("mVisa", "cash-out", recipientPrimaryAccountNumber);

                                aliasResponse = new AliasResponse();
                                aliasResponse.setMerchantCity(city_name);
                                aliasResponse.setMerchantName(receipient_name);
                                aliasResponse.setTransactionCurrencyCode(transactionCurrencyCode);
                                aliasResponse.setCountry(country);
                                aliasResponse.setMerchantCity(city_name);
                                aliasResponse.setRecipientPrimaryAccountNumber(recipientPrimaryAccountNumber);
                                aliasResponse.setMerchantCategoryCode("6012");
                                aliasCallback.onSuccess(aliasResponse);
                            } else {

                                Log.v("TRACER-3", "PASS");

                                Toast.makeText(getApplicationContext(),"Cannot Retrive Information",Toast.LENGTH_SHORT).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    myReq.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(myReq);



                }
                catch (InputInvalidException inputInvalidException)
                {
                    Toast.makeText(getApplicationContext(), "InputInvalidException", Toast.LENGTH_LONG).show();
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        payMerchantRequest = setUpPayMerchantRequestObject(transactionCurrencyCode);


        mVisaSDK.payMerchantUsingAlias(payMerchantRequest, new ResponseCallback<PayMerchantResponse, MVisaErrorObject, BasePayMerchantActivity>() {


            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(BasePayMerchantActivity activity, PayMerchantResponse response) {

                Log.v("CODE_RETURNED",transactionCurrencyCode);

                Log.v("STATEMANNTE", String.valueOf(response));

                JSONObject jsonObject = new JSONObject();

                try {
                    //Json object start
                    if (response.getCurrencyCode() == null) {
                        transactionCurrencyCode = "834";
                    } else {
                        transactionCurrencyCode = response.getCurrencyCode();
                    }

                    if(merchantCategoryCode==null){
                        merchantCategoryCode= response.getMerchantCategoryCode();
                    }





                    JSONArray jsonArray = db.getAllAc();
                    JSONObject pan = jsonArray.getJSONObject(response.getSelectedCardIndex());
                    JSONObject account = jsonArray.getJSONObject(response.getSelectedCardIndex());

                    if (recipientPrimaryAccountNumber == null) {
                        recipientPrimaryAccountNumber = response.getmVisaMerchantPan();
                    }


                    jsonObject.accumulate("MTI", "0640");
                    jsonObject.accumulate("payLoadFormatIndicator", response.getPayloadFormatIndicator());
                    jsonObject.accumulate("pointOfInitiation", response.getPointOfInitiation());
                    jsonObject.accumulate("mVisaMerchantId", response.getmVisaMerchantPan());
                    jsonObject.accumulate("mVisaMerchantPan", response.getmVisaMerchantPan());
                    jsonObject.accumulate("recipientName", response.getMerchantName());
                    jsonObject.accumulate("transactionAmount", response.getTransactionAmount());
                    jsonObject.accumulate("additionalAmount", response.getAdditionalAmount());
                    jsonObject.accumulate("selectedCardIndex", response.getSelectedCardIndex());
                    jsonObject.accumulate("currencyCode", response.getCurrencyCode());
                    jsonObject.accumulate("countryCode", response.getCountryCode());
                    jsonObject.accumulate("cityName", response.getCityName());
                    jsonObject.accumulate("postalCode", response.getPostalCode());
                    jsonObject.accumulate("billId", response.getBillId());
                    jsonObject.accumulate("mobileNumber", response.getMobileNumber());
                    jsonObject.accumulate("storeId", response.getStoreId());
                    jsonObject.accumulate("loyaltyNumber", response.getLoyaltyNumber());
                    jsonObject.accumulate("referenceNumber", response.getReferenceId());
                    jsonObject.accumulate("consumerId", response.getConsumerId());
                    jsonObject.accumulate("terminalId", response.getTerminalId());
                    jsonObject.accumulate("purpose", response.getPurpose());
                    jsonObject.accumulate("additionalConsumerDataRequest", response.getAdditionalConsumerDataRequest());
                    jsonObject.accumulate("transactionTime", set_transmission_datetime());
                    jsonObject.accumulate("recipientPrimaryAccountNumber", recipientPrimaryAccountNumber);
                    jsonObject.accumulate("bin", "004255");
                    jsonObject.accumulate("merchantCategoryCode",merchantCategoryCode);
                    jsonObject.accumulate("transactionFeeAmt",response.getAdditionalAmount());
                    jsonObject.accumulate("senderAccountNumber", getFieldValue(pan, "pan"));
                    jsonObject.accumulate("accountNumber", getFieldValue(account, "account"));
                    jsonObject.accumulate("transactionCurrencyCode", transactionCurrencyCode);
                    jsonObject.accumulate("retrievalReferenceNumber",getRrn(stan()));
                    jsonObject.accumulate("from", "Merchant Payment");
                    JSONObject reqMsg = jsonObject;
                    final JSONObject json = new JSONObject();
                    json.accumulate("MTI", "0690");
                    json.accumulate("recipientName", response.getMerchantName());
                    json.accumulate("city", response.getCityName());
                    json.accumulate("currencyCode", transactionCurrencyCode);
                    json.accumulate("amount", response.getTransactionAmount());
                    Log.v("DATABACK", "" + reqMsg);
                    Intent intent = new Intent(Visa.this, CreatemPin.class);
                    intent.putExtra("jsonObject", String.valueOf(reqMsg));
                    intent.putExtra("Forex", String.valueOf(json));
                    intent.putExtra("success", getString(R.string.p_s));
                    intent.putExtra("failure", getString(R.string.p_d));
                    startActivity(intent);
                    activity.finish();
                   // card_status = false;
                    Visa.this.finish();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFlowTimeOut(BasePayMerchantActivity activity, final MVisaErrorObject failureObject) {
                //handle flow timeout
                //Change 2018-09-04

                //End change 2018-09-04

            }
        }, aliasValidationHandler);
    }




//        mVisaSDK = MVisaSDKImpl.getInstance(Visa.this);
//        mVisaConfig = new MVisaConfig.Builder(getApplicationContext()).backgroundColor("#FF4500").actionButtonColor("#B1D9F4")
//                .actionButtonActiveColor("#BBD5E5")
//                .build();
//        mVisaSDK.setConfiguration(mVisaConfig); //Step 3. Make Pay Merchant API call.
//
//
//        mVisaSDK.payMerchant(payMerchantRequest, new ResponseCallback<PayMerchantResponse, MVisaErrorObject, BasePayMerchantActivity>() {
//            @Override
//            public void onSuccess(final BasePayMerchantActivity activity, PayMerchantResponse response) { //handle response
//                Toast.makeText(getApplicationContext(), "finished payment", Toast.LENGTH_LONG).show();
//                Log.d(TAG, response.toString());
//            }
//
//            @Override
//            public void onFlowTimeOut(BasePayMerchantActivity activity, final MVisaErrorObject failureObject) {
////Dont use the method. Will be deprecated
//            }
//        });


    private PayFriendRequest setPanPaymentObject(String fname, String lname, String card_number)
    {
        cardDetailsArrayList = new ArrayList<CardDetails>();

        cardDetailsArrayList  = db.getAcDetails(Visa.this,"Rahisi Pay");

        String currency_code = "834";

        int default_index = primary_Account;

        receipient_name=fname+" "+lname;


        payFriendRequest = new PayFriendRequest();
        payFriendRequest.setCardDetails(cardDetailsArrayList);
        payFriendRequest.setCurrencyCode(currency_code);
        payFriendRequest.setDefaultSenderCardIndex(default_index);
        payFriendRequest.setRecipientPan(card_number.substring(12));
        payFriendRequest.setRecipientName(receipient_name);
        payFriendRequest.setPaymentType(PayFriendRequest.AliasPayType.None);
        payFriendRequest.setAliasFlow(true);
        payFriendRequest.setNameOnContactList(receipient_name);
        payFriendRequest.setRecipientBank(issuerName);
        payFriendRequest.setNewUser(false);

        return payFriendRequest;
    }

    //Inner json
    public static synchronized JSONObject getInnerJsonObject(String innerJson){

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(innerJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static synchronized String getFieldValue(JSONObject response,String field){
        String responseCode="";
        try {

            responseCode=response.getString(field);
        }
        catch(JSONException ex){

        }
        return responseCode;

    }

    /**
     *Pay merchant request object
     */
    private PayMerchantRequest setUpPayMerchantRequestObject(String transactionCurrencyCode)
    {


        cardDetailsArrayList = new ArrayList<CardDetails>();

        cardDetailsArrayList  = db.getAcDetails(Visa.this,"Rahisi Pay");

        int default_index = 0;

        PayMerchantRequest payMerchantRequest =  new PayMerchantRequest();
        payMerchantRequest.setCurrencyCode(transactionCurrencyCode);
        payMerchantRequest.setDefaultCardIndex(default_index);
        payMerchantRequest.setCardDetails(cardDetailsArrayList);

        return payMerchantRequest;
    }
    @SuppressLint("SimpleDateFormat")
    public static String set_transmission_datetime() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.getTime();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
        String _datetime = sdf.format(cal.getTime()).toString();
        return _datetime;
    }

    public static String guid(String pan, String mob_nr){

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

    public static String stan()
    {
        Random random = new Random();
        String generatedStan = String.format("%04d", random.nextInt(10000));
        return generatedStan;
    }

    public static String getRrn(String stan){//Stan you can use any 6digits value.
        String rrn="";

        rrn=getJulianDate()+stan;
        return rrn;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (get_Mobile.equals("")){
            Visa.this.finish();
            Intent intent = new Intent(Visa.this,Login.class);
            startActivity(intent);
        } else {
            Visa.this.finish();
            Intent intent = new Intent(Visa.this,Home.class);
            startActivity(intent);
        }

    }
}
