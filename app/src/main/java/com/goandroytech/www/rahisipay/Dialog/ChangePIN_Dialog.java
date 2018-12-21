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

import static android.content.Context.MODE_PRIVATE;

public class ChangePIN_Dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button ok;
    TextView title;
    EditText amount;
    static SharedPreferences preferences;


    public ChangePIN_Dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change_pin__dialog);
        title = (TextView)findViewById(R.id.title);
        amount = (EditText)findViewById(R.id.amount);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);

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