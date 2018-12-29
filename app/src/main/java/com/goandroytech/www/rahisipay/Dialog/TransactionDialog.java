package com.goandroytech.www.rahisipay.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.goandroytech.www.rahisipay.R;
import com.goandroytech.www.rahisipay.ViewTransaction;
import com.goandroytech.www.rahisipay.apicalls.API;

import static android.content.Context.MODE_PRIVATE;

public class TransactionDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button ok;
    TextView title_nature,desc,charges,date,amount,vat;
    SharedPreferences sp;
    public String SHARED_PREF_NAME = "mysharedpref";
    String get_Nature,get_Date,get_Desc,get_Charges,get_Amount;
    double get_vat;
    int charge;
    public TransactionDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transaction_dialog);
        title_nature = (TextView)findViewById(R.id.title_nature);
        vat = (TextView)findViewById(R.id.vat);
        desc = (TextView)findViewById(R.id.desc);
        amount = (TextView)findViewById(R.id.amount);
        date = (TextView)findViewById(R.id.date);
        charges = (TextView)findViewById(R.id.charges);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);

        sp = c.getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        get_Nature = sp.getString(ViewTransaction.NATURE, null);
        get_Date = sp.getString(ViewTransaction.DATE_TIME, null);
        get_Desc = sp.getString(ViewTransaction.DESCRIPTION, null);
        get_Charges = sp.getString(ViewTransaction.CHARGES, null);
        get_Amount = sp.getString(ViewTransaction.AMOUNT,null);

        if (!get_Charges.equals("0") && !get_Charges.equals(null)){
            get_vat = Integer.parseInt(get_Charges)*API.VAT;
            charge = Integer.parseInt(get_Charges) - ((int) get_vat);
            vat.setText(API.formatCurrency(String.format("%.2f",get_vat)));
            charges.setText(API.formatCurrency(Integer.toString(charge)));
        } else {
            charges.setText(API.formatCurrency(get_Charges));
        }
        title_nature.setText(get_Nature);
        date.setText(API.DateFormat(get_Date));
        desc.setText(get_Desc);
        amount.setText(API.formatCurrency(get_Amount));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}