package com.goandroytech.www.rahisipay.Database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;


import com.goandroytech.www.rahisipay.Login;
import com.goandroytech.www.rahisipay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.transition.Fade.IN;

public class DigitalReceiptTwo extends AppCompatActivity implements View.OnClickListener {

    private static String tarehe_iliyopo;
    private TextView total, mVisaID, mVisaHeader, location, payment_type_value, approvalCode,senderAccount, referenceID;
    private Button close;
    private String finalString,visaString;
    private String merchant_name,merchant_id,total_amount, payment_amount,foreign_amount, payment_type,loc,billnumber,approval_code,retrievalReferenceNumber;
    private JSONObject reqMsg2 = null;
    private SharedPreferences pref;
    private JSONObject reqMsg = null;
    private TextView payment_amount_edt;
    private String sender_Acc;
    private String customer_Name,ret_ref_no,special_id;
    private TextView datetime,foreign_tv,foreign_tv2;
    private UhuruDataSource db;
    private String amount_value, agent_name, transaction_time_value, agent_pan,location_value, ret_no, sender_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_receipt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new UhuruDataSource(this);
        foreign_tv= (TextView) findViewById(R.id.foreign_amount);
        foreign_tv2 = (TextView) findViewById(R.id.foreign_amount_value);
        foreign_tv.setVisibility(View.GONE);
        foreign_tv2.setVisibility(View.GONE);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);
        visaString = pref.getString("ReceiptData", null);
        customer_Name = pref.getString("CustomerName",null);


        try {


            reqMsg = new JSONObject(visaString);

            Log.v("DIGITAL-RECEIPT",""+reqMsg);

            foreign_amount =  Action.getFieldValue(reqMsg,"foreign_amount");

            Log.v("DIGITAL-RECEIPT",""+foreign_amount);

            if(foreign_amount.equalsIgnoreCase("0"))
            {
                foreign_tv.setVisibility(View.GONE);

                foreign_tv2.setVisibility(View.GONE);
            }
            else{

                foreign_amount =Action.getFieldValue(reqMsg,"foreign_amount").substring(0,3)+  Action.getFormatedAmountVisa(Action.getFieldValue(reqMsg,"foreign_amount").substring(3));


                foreign_tv.setVisibility(View.VISIBLE);

                foreign_tv2.setVisibility(View.VISIBLE);

                foreign_tv2.setText(foreign_amount);
            }

            amount_value =Action.getFieldValue(reqMsg,"amount").substring(0,3)+ Action.getFormatedAmountVisa(Action.getFieldValue(reqMsg,"amount").substring(3));//amount
            Log.v("DIGITAL-RECEIPT",""+amount_value);
            special_id = Action.getFieldValue(reqMsg,"special_id");
            agent_name = Action.getFieldValue(reqMsg,"agent_name");
            transaction_time_value = Action.getFieldValue(reqMsg,"transaction_time");
            agent_pan = Action.getFieldValue(reqMsg,"agent_pan");
            location_value = Action.getFieldValue(reqMsg,"location");
            approval_code = Action.getFieldValue(reqMsg,"approval_code");
            ret_no = Action.getFieldValue(reqMsg,"ret_no");
            sender_account = Action.getFieldValue(reqMsg,"sender_account");
            payment_type = Action.getFieldValue(reqMsg,"payment_type");

            Log.v("SPECIAL_ID",special_id);

            datetime = (TextView) findViewById(R.id.datetime);
            datetime.setText(transaction_time_value);
            total = (TextView) findViewById(R.id.total_amount_value);
            total.setText(amount_value);
            mVisaID = (TextView) findViewById(R.id.mVisaID);
            if(special_id.length()>2)
            {
                mVisaID.setText("Visa ..."+agent_pan+"\n"+special_id);
            }else{
                mVisaID.setText("Visa ..."+agent_pan);
            }

            mVisaHeader = (TextView) findViewById(R.id.mVisaHeader);
            mVisaHeader.setText("Paid To: "+agent_name);
            TextView mVisaHeader2 = (TextView) findViewById(R.id.mVisaHeader2);
            mVisaHeader2.setText("Sent From: "+customer_Name);
            location = (TextView) findViewById(R.id.location_value);
            location.setText(location_value);
            payment_type_value = (TextView) findViewById(R.id.bill_number_value);
            payment_type_value.setText(payment_type);
            approvalCode = (TextView) findViewById(R.id.approval_code_value);
            approvalCode.setText(approval_code);
            referenceID = (TextView) findViewById(R.id.ret_ref_value);
            referenceID.setText(ret_no);
            payment_amount_edt = (TextView) findViewById(R.id.payment_amount_value);
            payment_amount_edt.setText(amount_value);
            senderAccount = (TextView) findViewById(R.id.senderAccount);
            senderAccount.setText("UMOJA ..."+sender_account);

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
        Intent intent = new Intent(DigitalReceiptTwo.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        DigitalReceiptTwo.this.finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.closing:
                Intent intent = new Intent(DigitalReceiptTwo.this,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                DigitalReceiptTwo.this.finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.finish();
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
}
