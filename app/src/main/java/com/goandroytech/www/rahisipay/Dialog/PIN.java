package com.goandroytech.www.rahisipay.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goandroytech.www.rahisipay.Home;
import com.goandroytech.www.rahisipay.MyAccount;
import com.goandroytech.www.rahisipay.R;
import com.goandroytech.www.rahisipay.SelectService;
import com.goandroytech.www.rahisipay.apicalls.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.goandroytech.www.rahisipay.apicalls.API.BASE_URL;
import static com.goandroytech.www.rahisipay.apicalls.API.LINK_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.LOGIN_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PHONE;
import static com.goandroytech.www.rahisipay.apicalls.API.UNLINK_SERVICE_ID;

public class PIN extends Dialog implements
        View.OnClickListener {

    public Context c;
    public Dialog d;
    public Button submit;
    EditText pin;
    TextView txt_customer_name;
    static SharedPreferences sp;
    String MyPref = "mysharedpref";
    String VALIDATE = "val";
    String get_data;
    TextView txt;
    String ACCOUNT_NUMBER ="account_number";
    String MOBILE = "mobile";
    String get_AccountNumber,get_Mobile,get_ServiceID;
    String SERVICE_ID = "service_id";
    String customer_name;
    String NAME ="name";
    RequestQueue queue;
    String SUBSCRIBED = "subscribed";
    String get_subscribed;
    public PIN(Context context) {
        super(context);
        this.c=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pin);
        pin = (EditText)findViewById(R.id.pin);
        submit = (Button) findViewById(R.id.submit);
        txt_customer_name = (TextView)findViewById(R.id.customer_name);
        txt = (TextView)findViewById(R.id.txt);
        submit.setOnClickListener(this);
        queue = Volley.newRequestQueue(c);
        sp = c.getSharedPreferences(MyPref,MODE_PRIVATE);
        get_data = sp.getString(VALIDATE, null);
        get_AccountNumber = sp.getString(ACCOUNT_NUMBER,null);
        get_Mobile = sp.getString(MOBILE,null);
        get_ServiceID = sp.getString(SERVICE_ID,null);
        get_subscribed = sp.getString(SUBSCRIBED,null);
        customer_name = sp.getString(NAME,null);
        txt_customer_name.setText(customer_name);
        if (get_data.equals("pay")) {
            txt.setText("Enter Amount");
            pin.setHint("Amount");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (get_data.equals("pay")){
                    dismiss();
                    Confirmation confirmation = new Confirmation(getContext());
                    confirmation.show();
                } else {
                    if (get_subscribed.equals("0")){
                        Link_Service();
                    } else {
                        UnLink_Service();
                    }
                }

                //ResetCode cdd=new ResetCode(PIN.this);
                // cdd.show();
                break;
            default:
                break;
        }
    }

    private void UnLink_Service() {
        final ProgressDialog progressDialog = new ProgressDialog(c);
        progressDialog.setMessage("Linking ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);
                            JSONObject jsonObject = json_res.getJSONObject("response");
                            String get_code = jsonObject.getString("code");
                            String get_message = jsonObject.getString("message");
                            String get_description = jsonObject.getString("description");

                            if (get_code.equals("200")){
                                Toast.makeText(c,get_description,Toast.LENGTH_SHORT).show();
                                dismiss();
                                progressDialog.dismiss();
                                ((Activity) c).finish();

                            } else {
                                Toast.makeText(c,get_description,Toast.LENGTH_SHORT).show();
                                dismiss();
                                progressDialog.dismiss();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        Toast.makeText(c,error.toString(),Toast.LENGTH_SHORT).show();
                        //   MDToast mdToast = MDToast.makeText(Login.this,error.toString(),Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);
                        //   mdToast.show();
                    }
                }
        )
        {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(PHONE, get_Mobile);
                params.put(API.PIN, pin.getText().toString());
                params.put(API.LINK_SERVICE,get_ServiceID);
                params.put(API.SERVICE_ID, UNLINK_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }


    private void Link_Service() {
        final ProgressDialog progressDialog = new ProgressDialog(c);
        progressDialog.setMessage("Linking ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);
                            JSONObject jsonObject = json_res.getJSONObject("response");
                            String get_code = jsonObject.getString("code");
                            String get_message = jsonObject.getString("message");
                            String get_description = jsonObject.getString("description");

                            if (get_code.equals("200")){
                                Toast.makeText(c,get_description,Toast.LENGTH_SHORT).show();
                                dismiss();
                                progressDialog.dismiss();

                            } else {
                                Toast.makeText(c,get_description,Toast.LENGTH_SHORT).show();
                                dismiss();
                                progressDialog.dismiss();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        Toast.makeText(c,error.toString(),Toast.LENGTH_SHORT).show();
                        //   MDToast mdToast = MDToast.makeText(Login.this,error.toString(),Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);
                        //   mdToast.show();
                    }
                }
        )
        {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(PHONE, get_Mobile);
                params.put(API.PIN, pin.getText().toString());
                params.put(API.SERVICE_ACCOUNT, get_AccountNumber);
                params.put(API.LINK_SERVICE,get_ServiceID);
                params.put(API.SERVICE_ID, LINK_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }
}