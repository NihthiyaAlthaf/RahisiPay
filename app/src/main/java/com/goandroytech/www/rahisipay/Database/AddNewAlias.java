package com.goandroytech.www.rahisipay.Database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.goandroytech.www.rahisipay.R;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.rilixtech.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;


public class AddNewAlias extends AppCompatActivity {
    private EditText f_name, p_number,phon_numberr;
    private Button submit;
    private UhuruDataSource db;
    private String full_name, phone_number,old_phone;
    private ProgressDialog mProgressDialog;
    private String phone, name, status;
    private SharedPreferences pref;
    private String phonenumber;
    private CountryCodePicker ccp;
    private String mobile_number;
    private String person_name,friend_identity;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_alias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getApplicationContext().getSharedPreferences("TokenBundle", 0);


        phonenumber = pref.getString("phone_number",null);




        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                phone = null;
                name = null;
                status = null;
                person_name = null;
                friend_identity = null;
            } else {
                try {
                    phone = extras.getString("phone");
                    name = extras.getString("name");
                    status = extras.getString("update");
                    person_name = extras.getString("person_name");
                    friend_identity = extras.getString("identity");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


            }
        } else {
            phone= (String) savedInstanceState.getSerializable("phone");
            name= (String) savedInstanceState.getSerializable("name");
            status= (String) savedInstanceState.getSerializable("update");
            person_name= (String) savedInstanceState.getSerializable("person_name");
        }

        Log.v("NAME",phone+"  "+person_name);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt(phone.substring(0,3)));
        ccp.setClickable(true);
        ccp.setVisibility(View.VISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            if (phone.trim().length() > 0) {
                getSupportActionBar().setTitle(R.string.header_update_alias);

            }
        }catch (NullPointerException e)
        {
            getSupportActionBar().setTitle(R.string.header_add_alias);
        }
        getSupportActionBar().setElevation(6);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);

        phon_numberr = (EditText) findViewById(R.id.phon_number);


        phon_numberr.setText(phonenumber);

        f_name = (EditText) findViewById(R.id.full_name);
        f_name.setEnabled(false);
        f_name.setText(person_name);

        p_number = (EditText) findViewById(R.id.p_number);
        setLength(p_number,15);

        p_number.setText(phone.substring(3));

        if(status!=null){
            phon_numberr.setVisibility(View.VISIBLE);
            p_number.setVisibility(View.GONE);
        }else{
            phon_numberr.setVisibility(View.GONE);
            p_number.setVisibility(View.VISIBLE);
        }



        submit = (Button) findViewById(R.id.submit);

        db = new UhuruDataSource(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f_name.getText().toString().length()==0)
                {
                    ErrorMessage(f_name.getText().toString(),f_name,getString(R.string.please_enter_fullname));
                    ClearError(f_name,getString(R.string.please_enter_fullname));
                }

                if(p_number.getText().toString().length()==0){
                    ErrorMessage(p_number.getText().toString(),p_number,getString(R.string.please_enter_phone));
                    ClearError(p_number,getString(R.string.please_enter_phone));
                }

                if(f_name.getText().toString().length()>0 && p_number.getText().toString().length()>0) {
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage(getResources().getString(R.string.loader_text));
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    try {
                        full_name = f_name.getText().toString();
                        phone_number = ccp.getSelectedCountryCode() + p_number.getText().toString();
                        old_phone = phon_numberr.getText().toString();


                        RequestQueue queue = Action.getInstance(getApplicationContext()).getRequestQueue();
                        //Json object start

                        JSONObject jsonObject = new JSONObject();

                        if(phone_number.length()==9){
                             mobile_number = ccp.getSelectedCountryCode() + phone_number;
                        }
                        else if(phone_number.length()==10)
                        {
                             mobile_number = phone_number.substring(1);
                        }
                        else if(phone_number.length()==12)
                        {
                             mobile_number = phone_number;
                        }
                        else {
                            Toast.makeText(AddNewAlias.this,getString(R.string.incorrect_mobile_number),Toast.LENGTH_LONG).show();
                        }

                        jsonObject.accumulate("MTI", "0810");
                        jsonObject.accumulate("alias", mobile_number);
                        jsonObject.accumulate("app_id", "PP");

                        String ENDPOINT = API.VISA_URL;

                        JSONObject reqMsg = jsonObject;
                        Log.v("STATEMANNT", String.valueOf(reqMsg));
                        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, ENDPOINT, jsonObject, new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onResponse(final JSONObject response) {

                                if (db.friendAliasCheck(phone_number) > 0) {

                                    mProgressDialog.dismiss();
                                    ErrorMessage(p_number.getText().toString(),p_number,getString(R.string.please_enter_phone_exist));
                                    ClearError(p_number,getString(R.string.please_enter_phone_exist));
                                } else if (status != null) {

                                    if (db.updateFriendAlias(full_name, phone_number, friend_identity) == true) {
                                        f_name.setText("");
                                        p_number.setText("");
                                        Toast.makeText(AddNewAlias.this, "Friend Updated Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), AllContacts.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                    }


                                } else {
                                    mProgressDialog.dismiss();

//                                    db.recordFriendsAlias(full_name, phone_number);
                                    db.updateFriendAlias(full_name, phone_number, friend_identity);

                                    f_name.setText("");
                                    p_number.setText("");
                                    Toast.makeText(AddNewAlias.this, "Friend Updated Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), AllContacts.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }


                            }
                        }, createMyReqErrorListener());
                        myReq.setRetryPolicy(new DefaultRetryPolicy(
                                0,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        queue.add(myReq);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }
        });


    }

    /**
     * When pressed back
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Pusher(AddNewAlias.this,AllContacts.class);
    }

    /***********************
     *Action when top left arrow clicked and menu on right if available
     ***********************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Pusher(AddNewAlias.this,AllContacts.class);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *Setting error if field is empty
     */
    public static void ErrorMessage(String j, final EditText k, String l){
        if(j.length() == 0){
            k.setError(l);
        }
    }

    /**
     *Clear error when edit text is filled
     */
    public static void ClearError(final EditText z, final String error) {
        z.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable za) {
                if (za.toString().trim().length() > 0) {
                    z.setError(null);
                } else {
                    z.setError(error);
                }
            }
        });
    }

    private void setLength(EditText editText, int len)
    {
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(len) });
    }

    /**
     *Response Listener print receipt
     */
    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(final JSONObject response) {
                Log.v("STATEMANNT",response.toString());
                Intent i = new Intent(AddNewAlias.this, NotificationHome.class);
                String strName = "FRIEND SUCCESSFULLY ADDED";
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("STRING_I_NEED", strName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                AddNewAlias.this.finish();


            }
        };
    }

    /**
     *Error Listener
     */
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError ex) {
                Log.v("STATEMANNT",ex.toString());

                Intent i = new Intent(AddNewAlias.this, NotificationHome.class);
                String strName = "INVALID ALIAS";
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("STRING_I_NEED", strName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                AddNewAlias.this.finish();
            }
        };
    }

    /*
    *Intent pusher method
    */
    private void Pusher(Context context, Class x)
    {
        Intent i = new Intent(context,x);
        startActivity(i);
    }

}
