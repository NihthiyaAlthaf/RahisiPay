package com.goandroytech.www.rahisipay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.goandroytech.www.rahisipay.Pay_Visa.AllContacts;

public class PayScan extends AppCompatActivity {
    Button p_p,scan_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_scan);
        p_p = (Button)findViewById(R.id.p_p);
        scan_pay = (Button)findViewById(R.id.scan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        p_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SharedPreferences sp_visa;
                sp_visa = getSharedPreferences("UhuruPayPref",0);
                SharedPreferences.Editor e = sp_visa.edit();
                e.putString("CurrentPage","1");
                e.commit();
                Intent intent = new Intent(PayScan.this,AllContacts.class);
                startActivity(intent);
            }
        });

        scan_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PayScan.this,Visa.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
