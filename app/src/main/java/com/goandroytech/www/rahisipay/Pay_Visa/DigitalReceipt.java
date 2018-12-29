package com.goandroytech.www.rahisipay.Pay_Visa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;


import com.goandroytech.www.rahisipay.Login;
import com.goandroytech.www.rahisipay.MyAccount;
import com.goandroytech.www.rahisipay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DigitalReceipt extends AppCompatActivity implements View.OnClickListener {

    private static String tarehe_iliyopo;
    private TextView total, mVisaID, mVisaHeader, location, payment_type_value, approvalCode,senderAccount, referenceID;
    private Button close;
    private String finalString,visaString;
    private String merchant_name,merchant_id,total_amount, payment_amount, payment_type,loc,billnumber,approval_code,retrievalReferenceNumber;
    private JSONObject reqMsg2 = null;
    private SharedPreferences pref;
    private JSONObject reqMsg = null;
    private TextView payment_amount_edt,foreign_amount_value,foreign_amount_tv;
    private String sender_Acc;
    private String customer_Name,ret_ref_no;
    private TextView datetime;
    private UhuruDataSource db;
    private String foreign_amount="0";
    private String ID="",FromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_receipt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new UhuruDataSource(this);


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);
        visaString = pref.getString("VisaResponse", null);
        finalString = pref.getString("FinalResponse",null);
        FromLogin = pref.getString("FromLogin","1");

        foreign_amount_tv = (TextView) findViewById(R.id.foreign_amount);
        foreign_amount_value = (TextView) findViewById(R.id.foreign_amount_value);

        foreign_amount_tv.setVisibility(View.GONE);
        foreign_amount_value.setVisibility(View.GONE);

       /* if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                finalString=null;

            } else {
                try {
                    finalString = extras.getString("FinalResponse");


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


            }
        } else {
            finalString = (String) savedInstanceState.getSerializable("FinalResponse");
        }*/

        try {


            reqMsg = new JSONObject(finalString);

            Log.v("DIGITAL_RECEIPTS_ONE",String.valueOf(reqMsg));

            reqMsg2 = new JSONObject(visaString);

            Log.v("DIGITAL_RECEIPTS_TWO",String.valueOf(reqMsg2)+datetime(Action.getFieldValue(reqMsg2,"transmissionDateTime")));


            merchant_name = Action.getFieldValue(reqMsg,"recipientName");
            if(Action.getFieldValue(reqMsg,"recipientPrimaryAccountNumber")!=null) {
                Log.v("STATEMANNT-LENGTH",""+Action.getFieldValue(reqMsg,"recipientPrimaryAccountNumber").length());
                if(Action.getFieldValue(reqMsg,"recipientPrimaryAccountNumber").length()>=12) {
                    merchant_id = Action.getFieldValue(reqMsg, "recipientPrimaryAccountNumber").substring(12);
                }else{
                    Log.v("STATEMANNT-LENGTH2",""+Action.getFieldValue(reqMsg,"mVisaMerchantPan").length());
                    if(Action.getFieldValue(reqMsg,"mVisaMerchantPan").length()>=12) {
                        merchant_id = Action.getFieldValue(reqMsg, "mVisaMerchantPan").substring(12);

                    }else{
                        merchant_id ="01234567890123456".substring(12);
                    }

                    Log.v("STATEMANNT-LENGTH2",""+merchant_id);
                }
            }
            else if(Action.getFieldValue(reqMsg, "mVisaMerchantPan")!=null){
                Log.v("STATEMANNT-LENGTH2",""+Action.getFieldValue(reqMsg,"mVisaMerchantPan").length());
                if(Action.getFieldValue(reqMsg,"mVisaMerchantPan").length()>=12) {
                    merchant_id = Action.getFieldValue(reqMsg, "mVisaMerchantPan").substring(12);
                }else{
                    if(Action.getFieldValue(reqMsg,"recipientPrimaryAccountNumber").length()>=12) {
                        merchant_id = Action.getFieldValue(reqMsg, "recipientPrimaryAccountNumber").substring(12);
                    }else{
                        merchant_id ="01234567890123456".substring(12);
                    }
                }
            }
            String currency="TZS ";
            if(Action.getFieldValue(reqMsg,"currencyCode").equalsIgnoreCase("834"))
            {
                 currency="TZS ";
            }
            else if(Action.getFieldValue(reqMsg,"currencyCode").equalsIgnoreCase("404")){
                foreign_amount_tv.setVisibility(View.VISIBLE);
                foreign_amount_value.setVisibility(View.VISIBLE);
                currency="KES ";
                foreign_amount = currency+Action.getFieldValue(reqMsg,"transactionAmount");
                foreign_amount_value.setText(foreign_amount);
            }
            sender_Acc = Action.getFieldValue(reqMsg, "senderAccountNumber").substring(12);
            approval_code = Action.getFieldValue(reqMsg2,"approvalCode");
            retrievalReferenceNumber = Action.getFieldValue(reqMsg2,"retrievalReferenceNumber");
            total_amount = "TZS "+Action.getFieldValue(reqMsg,"OriginalAmount");//transactionAmount
            payment_type = Action.getFieldValue(reqMsg,"from");
            loc = Action.getFieldValue(reqMsg,"cityName");

            customer_Name = pref.getString("CustomerName",null);

            datetime = (TextView) findViewById(R.id.datetime);
            datetime.setText(datetime(Action.getFieldValue(reqMsg,"transactionTime")));
            total = (TextView) findViewById(R.id.total_amount_value);
            total.setText(total_amount);
            mVisaID = (TextView) findViewById(R.id.mVisaID);

            if(Action.getFieldValue(reqMsg,"MTI").equalsIgnoreCase("0650"))
            {
                 ID = " ";

            }else {
                 ID = "ID :" + Action.getFieldValue(reqMsg, "recipientPrimaryAccountNumber").substring(0, 6) + Action.getFieldValue(reqMsg, "recipientPrimaryAccountNumber").substring(14);
            }
            mVisaID.setText("Visa ..."+merchant_id+"\n"+ID);
            mVisaHeader = (TextView) findViewById(R.id.mVisaHeader);
            mVisaHeader.setText("Paid To: "+merchant_name);
            TextView mVisaHeader2 = (TextView) findViewById(R.id.mVisaHeader2);
            mVisaHeader2.setText("Sent From: "+customer_Name);
            location = (TextView) findViewById(R.id.location_value);
            location.setText(loc);
            payment_type_value = (TextView) findViewById(R.id.bill_number_value);
            payment_type_value.setText(payment_type);
            approvalCode = (TextView) findViewById(R.id.approval_code_value);
            approvalCode.setText(approval_code);
            referenceID = (TextView) findViewById(R.id.ret_ref_value);
            referenceID.setText(retrievalReferenceNumber);
            payment_amount_edt = (TextView) findViewById(R.id.payment_amount_value);
            payment_amount_edt.setText(total_amount);
            senderAccount = (TextView) findViewById(R.id.senderAccount);
            senderAccount.setText("TPB ..."+sender_Acc);

            if(approval_code.length()>0)
            {
                db.receipt_details(total_amount,merchant_name, Action.set_transmission_datetime(),merchant_id,loc,approval_code,retrievalReferenceNumber,sender_Acc,payment_type,"0",foreign_amount,ID);
            }

            close = (Button) findViewById(R.id.closing);
            close.setOnClickListener(this);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {

            e.printStackTrace();
        }

    }


    public static String datetime(String tarehe)
    {

        String string = tarehe.replace("T","");
//        String string2 = string.replace(".000Z","");


        String originalStringFormat = "yyyy-MM-ddHH:mm:ss";
        String desiredStringFormat = "yyyy-MM-dd HH:mm:ss a";

        SimpleDateFormat readingFormat = new SimpleDateFormat(originalStringFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(desiredStringFormat);

        try {
            Date date = readingFormat.parse(string);
            tarehe_iliyopo = outputFormat.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }

        return tarehe_iliyopo;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DigitalReceipt.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        DigitalReceipt.this.finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.closing:
                if(FromLogin.equalsIgnoreCase("1"))
                {
                    Intent intent = new Intent(DigitalReceipt.this,Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    DigitalReceipt.this.finish();
                }
                else if(FromLogin.equalsIgnoreCase("0"))
                {
                    Intent intent = new Intent(DigitalReceipt.this,MyAccount.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    DigitalReceipt.this.finish();
                }

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.finish();
    }

    /****
     *Hide Soft Key Board , call it after submission of data on a form to hide key board
     ****/
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
