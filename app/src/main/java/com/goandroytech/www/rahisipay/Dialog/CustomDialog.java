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

public class CustomDialog extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button ok;
    TextView title;
    EditText amount;
    static SharedPreferences preferences;
    public String SHARED_PREF_NAME = "mysharedpref";
    String SERVICE="service";
    String service_title;

    public CustomDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_dialog);
        title = (TextView)findViewById(R.id.title);
        amount = (EditText)findViewById(R.id.amount);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
        preferences = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        service_title = preferences.getString(SERVICE, null);
        title.setText(service_title);

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