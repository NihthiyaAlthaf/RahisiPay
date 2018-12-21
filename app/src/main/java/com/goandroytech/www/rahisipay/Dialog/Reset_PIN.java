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

import com.goandroytech.www.rahisipay.Home;
import com.goandroytech.www.rahisipay.R;

import static android.content.Context.MODE_PRIVATE;

public class Reset_PIN extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button submit;
    EditText account_no;
    static SharedPreferences preferences;


    public Reset_PIN(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reset__pin);
        account_no = (EditText)findViewById(R.id.account_no);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                //ResetCode cdd=new ResetCode(Reset_PIN.this);
               // cdd.show();
                break;
            default:
                break;
        }
        dismiss();
    }
}