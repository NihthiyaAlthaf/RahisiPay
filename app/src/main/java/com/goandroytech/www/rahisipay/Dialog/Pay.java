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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.goandroytech.www.rahisipay.Home;
import com.goandroytech.www.rahisipay.R;

import static android.content.Context.MODE_PRIVATE;

public class Pay extends Dialog implements
        View.OnClickListener {

    public Context c;
    public Dialog d;
    public Button ok;
    RadioButton pay,other;
    static SharedPreferences preferences;


    public Pay(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    public Pay(Context context) {
        super(context);
        this.c=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay);
        pay =(RadioButton)findViewById(R.id.self);
        other = (RadioButton)findViewById(R.id.other);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (pay.isChecked()){
                    PIN pin =new PIN(getContext());
                    pin.show();
                    dismiss();
                } else if (other.isChecked()){
                    Account_Num acc=new Account_Num(getContext());
                    acc.show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(),"Select one type",Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }
}