package com.goandroytech.www.rahisipay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.goandroytech.www.rahisipay.apicalls.API;
import com.squareup.picasso.Picasso;

public class Receipt extends AppCompatActivity {
    TextView service_name,service_provider,amount_paid,paid_to,service_no,transaction_no,receipt_no,date_time,service_charge,vat;
    String get_url,get_transaction_id,getProviderId,get_date,get_time,get_receipt_no,get_amount,get_service_charge,get_vat_amount,get_account_no,get_service_name,get_service_provider;
    ImageView logo_url;
    Button close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        logo_url = (ImageView)findViewById(R.id.logo_url);
        close = (Button)findViewById(R.id.close);
        service_name = (TextView)findViewById(R.id.service_name);
        service_provider = (TextView)findViewById(R.id.service_provider);
        amount_paid = (TextView)findViewById(R.id.amount_paid);
        paid_to = (TextView)findViewById(R.id.paid_to);
        service_no = (TextView)findViewById(R.id.service_no);
        transaction_no = (TextView)findViewById(R.id.transaction_no);
        receipt_no = (TextView)findViewById(R.id.receipt_no);
        date_time = (TextView)findViewById(R.id.date_time);
        service_charge = (TextView)findViewById(R.id.service_charge);
        vat = (TextView)findViewById(R.id.vat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        get_url = intent.getStringExtra(SelectService.URL);
        get_transaction_id = intent.getStringExtra(SelectService.TRANSACTION_ID);
        getProviderId = intent.getStringExtra(SelectService.PROVIDER_ID);
        get_date = intent.getStringExtra(SelectService.DATE);
        get_time = intent.getStringExtra(SelectService.TIME);
        get_receipt_no = intent.getStringExtra(SelectService.RECEIPT_NO);
        get_amount = intent.getStringExtra(SelectService.AMOUNT);
        get_service_charge = intent.getStringExtra(SelectService.SERVICE_CHARGE);
        get_vat_amount = intent.getStringExtra(SelectService.VAT);
        get_account_no = intent.getStringExtra(SelectService.SERVICE_ACCOUNT_NO);
        get_service_name = intent.getStringExtra(SelectService.SERVICE_NAME);
        get_service_provider = intent.getStringExtra(SelectService.SERVICE_PROVIDER);

        Picasso.with(Receipt.this).load(get_url).into(logo_url);

        service_name.setText(get_service_name);
        service_provider.setText(get_service_provider);
        amount_paid.setText(API.formatCurrency(get_amount));
        paid_to.setText(get_account_no);
        transaction_no.setText(get_transaction_id);
        receipt_no.setText(get_receipt_no);
        date_time.setText(API.DateFormat(get_date + " " + get_time));
        service_charge.setText(API.formatCurrency(get_service_charge));
        vat.setText(API.formatCurrency(get_vat_amount));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Receipt.this,Home.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        Intent intent = new Intent(Receipt.this,Home.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        Intent intent = new Intent(Receipt.this,Home.class);
        startActivity(intent);
    }
}
