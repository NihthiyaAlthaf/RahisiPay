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

import static android.content.Context.MODE_PRIVATE;

public class TransactionDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button ok;
    TextView title_nature,desc,charges,date,amount;
    SharedPreferences sp;
    public String SHARED_PREF_NAME = "mysharedpref";
    String get_Nature,get_Date,get_Desc,get_Charges,get_Amount;

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

        title_nature.setText(get_Nature);
        date.setText(get_Date);
        desc.setText(get_Desc);
        charges.setText(get_Charges);
        amount.setText("TSh "+get_Amount);
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