package com.goandroytech.www.rahisipay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.goandroytech.www.rahisipay.Connection.ConnectivityDetector;
import com.goandroytech.www.rahisipay.Model.Child;
import com.goandroytech.www.rahisipay.Model.Service_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.goandroytech.www.rahisipay.apicalls.API.BASE_URL;
import static com.goandroytech.www.rahisipay.apicalls.API.LOGIN_SERVICE_ID;

public class Login extends Activity {
    EditText input_number,input_pin;
    Button btn_login;
    ConnectivityDetector connectivityDetector;
    RequestQueue queue;
    static public List<Service_Model> serviceList = new ArrayList<>();
    static public List<Child> childList = new ArrayList<>();
    static public ArrayList<String> array_service_id = new ArrayList<>();
    static public ArrayList<String> array_image = new ArrayList<>();
    static public ArrayList<String> array_service_name = new ArrayList<>();
    static public ArrayList<String> array_logo_url = new ArrayList<>();
    static public ArrayList<String> array_parent = new ArrayList<>();
    static public ArrayList<String> array_subscribe = new ArrayList<>();
    static public ArrayList<String> array_subscriptionAccounte = new ArrayList<>();
    String get_status,get_customer_name,get_mobile_number,get_service_id,get_service_name,get_image,get_parent,get_subscribed,get_subscriptionAccount;
    TextView forgot;
    SharedPreferences sp;
    public  String SHARED_PREF_NAME = "mysharedpref";
    String PHONE ="phone";
    String NAME ="name";
    String MOBILE ="mobile";
    String ID ="id";
    String ACCOUNT ="account";
    String PIN ="pin";
    String get_id;
    String get_name;
    String get_mobile;
    String get_email;
    String get_account;
    String get_balance;
    String get_card;
    String get_date;
    String get_transaction_number;
    String get_description;
    String get_amount;
    String get_logo_url;
    Typeface type;
    Button pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_number =(EditText)findViewById(R.id.customer_number);
        input_pin =(EditText)findViewById(R.id.customer_pin);
        btn_login =(Button)findViewById(R.id.login);
        forgot = (TextView)findViewById(R.id.forgot);
        pay = (Button)findViewById(R.id.pay);
        connectivityDetector = new ConnectivityDetector(getApplication());
        queue = Volley.newRequestQueue(this);
        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        type = Typeface.createFromAsset(getAssets(),"arial.ttf");
        input_number.setTypeface(type);
        input_number.setTypeface(type);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Visa.class);
                startActivity(intent);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Login.this,Forgot.class);
                startActivity(intent);

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_number.getText().toString().equals("")){
                    input_number.setError("Enter Mobile Number");
                } else if (input_pin.getText().toString().equals("")){
                    input_pin.setError("Enter PIN");
                } else   if (connectivityDetector.checkConnectivityStatus()) {
                    serviceList.clear();
                    array_service_id.clear();
                    array_image.clear();
                    array_service_name.clear();
                    array_logo_url.clear();
                    array_parent.clear();
                    array_subscribe.clear();
                    array_subscriptionAccounte.clear();
                    userLogin();
                } else {
                    connectivityDetector.showAlertDialog(Login.this, "Connection Error!", "No internet connection");

                }
            }
        });
    }

    private void userLogin() {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Signing ...");
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
                            String get_logo_url = json_res.getString("logo_url");

                            JSONObject jsonObject = json_res.getJSONObject("response");
                            String get_code = jsonObject.getString("code");

                            if (get_code.equals("200")){
                                JSONObject jsonObject1 = json_res.getJSONObject("customer");

                                JSONObject jsonObject2 = jsonObject1.getJSONObject("basic_detail");
                                get_id = jsonObject2.getString("id");
                                get_name = jsonObject2.getString("name");
                                get_mobile = jsonObject2.getString("mobile");
                                get_email = jsonObject2.getString("email");
                                get_account = jsonObject2.getString("account");
                                get_balance = jsonObject2.getString("balance");
                                get_card = jsonObject2.getString("card");

                                JSONObject jsonObject3 = jsonObject1.getJSONObject("last_transaction");
                                get_date = jsonObject3.getString("date");
                                get_transaction_number = jsonObject3.getString("transaction_number");
                                get_description = jsonObject3.getString("description");
                                get_amount = jsonObject3.getString("amount");

                                JSONArray jsonArray = json_res.getJSONArray("services");

                                for (int i =0; i<jsonArray.length();i++){
                                    JSONObject jsonObject4 = jsonArray.getJSONObject(i);
                                    get_service_id = jsonObject4.getString("id");
                                    get_service_name = jsonObject4.getString("name");
                                    get_parent = jsonObject4.getString("parent");
                                    get_image = jsonObject4.getString("logo");
                                    get_subscribed = jsonObject4.getString("subscribed");
                                    get_subscriptionAccount = jsonObject4.getString("subscriptionAccount");

                                    if (get_parent.equals("0")){
                                        Service_Model service = new Service_Model(get_service_id,get_image, get_service_name,get_logo_url,get_parent,get_subscribed,get_subscriptionAccount);
                                        serviceList.add(service);
                                    } else {
                                        array_service_id.add(get_service_id);
                                        array_image.add(get_image);
                                        array_service_name.add(get_service_name);
                                        array_logo_url.add(get_logo_url);
                                        array_parent.add(get_parent);
                                        array_subscribe.add(get_subscribed);
                                        array_subscriptionAccounte.add(get_subscriptionAccount);

                                        //   Child child = new Child(get_service_id,get_image,get_service_name,get_logo_url,get_parent);
                                        //  childList.add(child);
                                    }


                                }
                                Toast.makeText(Login.this,"Login Success!",Toast.LENGTH_SHORT).show();


                                //  MDToast mdToast = MDToast.makeText(Login.this,"Login Success!",Toast.LENGTH_SHORT,MDToast.TYPE_SUCCESS);
                                // mdToast.show();
                                progressDialog.dismiss();
                                saveLoginPrefrence();

                                finish();
                                Intent intent = new Intent(Login.this, MyAccount.class);
                                startActivity(intent);

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this,"Login Failed!",Toast.LENGTH_SHORT).show();
                                //  MDToast mdToast = MDToast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                                //  mdToast.show();
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
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_SHORT).show();
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
                params.put("phone", input_number.getText().toString());
                params.put("pin", input_pin.getText().toString());
                params.put("service_id", LOGIN_SERVICE_ID);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void saveLoginPrefrence() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PHONE, input_number.getText().toString());
        editor.putString(MOBILE, get_mobile);
        editor.putString(NAME, get_name);
        editor.putString(ACCOUNT, get_id);
        editor.putString(ID, get_account);
        editor.putString(PIN,input_pin.getText().toString() );
        editor.apply();
        editor.commit();
    }
}
