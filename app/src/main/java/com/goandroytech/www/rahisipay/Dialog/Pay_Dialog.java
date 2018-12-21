package com.goandroytech.www.rahisipay.Dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;

import com.goandroytech.www.rahisipay.R;

public class Pay_Dialog extends AppCompatActivity {
    RadioButton self,other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay__dialog);
        self = (RadioButton)findViewById(R.id.self);
        other = (RadioButton)findViewById(R.id.other);
    }
}
