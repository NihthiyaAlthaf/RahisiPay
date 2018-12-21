package com.goandroytech.www.rahisipay.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.goandroytech.www.rahisipay.R;

import static android.content.Context.MODE_PRIVATE;

public class Confirmation extends Dialog implements
        View.OnClickListener {

    public Dialog d;
    public Button yes,no;
    TextView service_name,amount;
    static SharedPreferences sp;
    public String SHARED_PREF_NAME = "mysharedpref";
    String VALIDATE = "val";
    Context context;


    public Confirmation(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_confirmation);
        service_name = (TextView)findViewById(R.id.service_name);
        amount = (TextView)findViewById(R.id.amount);
        sp = context.getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                saveLoginPrefrence();
                PIN pin = new PIN(getContext());
                pin.show();
                dismiss();
                break;
            case R.id.no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void saveLoginPrefrence() {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(VALIDATE, "link_unlink");
            editor.apply();
            editor.commit();
    }
}