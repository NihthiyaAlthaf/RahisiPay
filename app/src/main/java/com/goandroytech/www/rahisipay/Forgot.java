package com.goandroytech.www.rahisipay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.goandroytech.www.rahisipay.Dialog.CustomDialog;
import com.goandroytech.www.rahisipay.Dialog.Reset_PIN;

public class Forgot extends AppCompatActivity {

    EditText account_no;
   // Button reset_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        account_no = (EditText)findViewById(R.id.account_no);
      //  reset_pin = (Button)findViewById(R.id.reset_pin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        reset_pin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (account_no.getText().toString().equals("")){
//                    account_no.setError("Enter Account Number!..");
//                } else {
//                    Reset_PIN reset=new Reset_PIN(Forgot.this);
//                    reset.show();
//                }
//            }
//        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
      finish();
        return true;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
