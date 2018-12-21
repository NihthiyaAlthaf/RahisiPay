package com.goandroytech.www.rahisipay.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.goandroytech.www.rahisipay.Forgot;
import com.goandroytech.www.rahisipay.Home;
import com.goandroytech.www.rahisipay.R;

import static android.content.Context.MODE_PRIVATE;

public class BlockCode extends Dialog implements
        View.OnClickListener {

    public Dialog d;
    public Button submit;
    EditText code;
    Context context;
    static SharedPreferences preferences;



    public BlockCode(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_block_code);
        code = (EditText)findViewById(R.id.code);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (code.getText().toString().equals("")){
                    Message msg = new Message(getContext());
                    msg.show();
                    dismiss();
                } else {
                    code.setError("Enter Code");
                }

                break;
            default:
                break;
        }
        dismiss();
    }
}