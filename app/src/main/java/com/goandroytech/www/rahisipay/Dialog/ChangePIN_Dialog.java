package com.goandroytech.www.rahisipay.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.goandroytech.www.rahisipay.Card;
import com.goandroytech.www.rahisipay.Connection.ConnectivityDetector;
import com.goandroytech.www.rahisipay.R;
import com.goandroytech.www.rahisipay.apicalls.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.goandroytech.www.rahisipay.apicalls.API.BASE_URL;
import static com.goandroytech.www.rahisipay.apicalls.API.CHANGE_PIN_SERVICE_ID;
import static com.goandroytech.www.rahisipay.apicalls.API.PHONE;
import static com.goandroytech.www.rahisipay.apicalls.API.UNBLOCK_CARD_SERVICE_ID;

public class ChangePIN_Dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button ok;
    TextView title;
    EditText amount;
    public   String SHARED_PREF_NAME = "mysharedpref";
    static SharedPreferences sp;    String get_name;
    String get_id;
    public static String NAME ="name";
    public static String ID ="id";
    String CARD_STATUS ="card_state";
    String get_card_state;
    EditText current_pin,new_pin,con_new_pin;
    RequestQueue queue;
    String MOBILE = "mobile";
    String get_Mobile;
    ConnectivityDetector connectivityDetector;
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
        current_pin = (EditText)findViewById(R.id.current_pin);
        new_pin = (EditText)findViewById(R.id.new_pin);
        con_new_pin = (EditText)findViewById(R.id.con_new_pin);
        amount = (EditText)findViewById(R.id.amount);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
        queue = Volley.newRequestQueue(c);
        sp = c.getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        get_name = sp.getString(NAME, null);
        get_id = sp.getString(ID, null);
        get_card_state = sp.getString(CARD_STATUS,null);
        get_Mobile = sp.getString(MOBILE,null);
        connectivityDetector = new ConnectivityDetector(c);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (current_pin.getText().toString().equals("")){
                    Toast.makeText(c,"Enter Current PIN",Toast.LENGTH_SHORT).show();
                } else if (new_pin.getText().toString().equals("")){
                    Toast.makeText(c,"Enter New PIN",Toast.LENGTH_SHORT).show();
                } else if (con_new_pin.getText().toString().equals("")){
                    Toast.makeText(c,"Enter Confirm PIN",Toast.LENGTH_SHORT).show();
                } else if (new_pin.getText().toString().equals(con_new_pin.getText().toString())){
                    Toast.makeText(c,"New PIN and Confirm PIN does not match",Toast.LENGTH_SHORT).show();
                } else if (new_pin.getText().toString().length()>4){
                    Toast.makeText(c,"Enter 4 digit PIN Number",Toast.LENGTH_SHORT).show();
                } else {
                    ChangePIN();
                }
              //  dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void ChangePIN() {
        final ProgressDialog progressDialog = new ProgressDialog(c);
        progressDialog.setMessage("UnBlocking ...");
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

                            if (get_code.equals("200")) {
                                progressDialog.dismiss();
                                connectivityDetector.openSuccessDialog(c,"Success",get_description);

                            } else {
                                progressDialog.dismiss();
                                connectivityDetector.showAlertDialog(c,"Success",get_description);
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
                        connectivityDetector.showAlertDialog(c,"Success",error.toString());

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
                params.put(API.PIN, current_pin.getText().toString());
                params.put(API.NEW_PIN, new_pin.getText().toString());
                params.put(API.SERVICE_ID, CHANGE_PIN_SERVICE_ID);
                return params;
            }
        };
        queue.add(postRequest);
    }

}