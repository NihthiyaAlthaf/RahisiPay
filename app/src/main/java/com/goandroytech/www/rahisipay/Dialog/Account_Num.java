package com.goandroytech.www.rahisipay.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goandroytech.www.rahisipay.Forgot;
import com.goandroytech.www.rahisipay.Home;
import com.goandroytech.www.rahisipay.R;

import static android.content.Context.MODE_PRIVATE;

public class Account_Num extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button submit;
    EditText account_no;
    Context context;
    SharedPreferences preferences;
    public  String SHARED_PREF_NAME = "mysharedpref";
    String ACCOUNT_NUMBER ="account_number";
    String SERVICE_ID = "service_id";
    String get_ServiceID;
    String SUBSCRIBED = "subscribed";
    String get_subscribed;
    public Account_Num(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context=a;
    }

    public Account_Num(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_account__num);
        preferences = context.getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        get_ServiceID = preferences.getString(SERVICE_ID,null);
        get_subscribed = preferences.getString(SUBSCRIBED,null);
        account_no = (EditText)findViewById(R.id.account_no);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (account_no.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Enter Account Number",Toast.LENGTH_SHORT).show();
                } else {
                    saveData();
                    dismiss();
                    PIN pin=new PIN(getContext());
                    pin.show();
                }

                break;
            default:
                break;
        }
    }

    private void saveData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ACCOUNT_NUMBER, account_no.getText().toString());
        editor.putString(SERVICE_ID, get_ServiceID);
        editor.putString(SUBSCRIBED, get_subscribed);
        editor.apply();
        editor.commit();
    }
}