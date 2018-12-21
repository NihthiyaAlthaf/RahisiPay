package com.goandroytech.www.rahisipay.Database;


/**
 * Created by jamesb on 2018/03/09.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.goandroytech.www.rahisipay.Login;
import com.goandroytech.www.rahisipay.Model.Msg;
import com.goandroytech.www.rahisipay.R;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.rilixtech.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllContacts extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private  RecyclerView rvContacts;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private UhuruDataSource db;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<Msg> transactions;
    private SharedPreferences pref;
    private EditText phone_number,receipientName,first_name, last_name;
    private String mobile_number;
    private Button submit;
    private  ArrayList<Msg> contactVOList;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.favourite:
                    pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);
                    SharedPreferences.Editor value3 = pref.edit();
                    value3.putString("CurrentPage","1");
                    value3.commit();
                    Intent intent = new Intent(getApplicationContext(), AllContacts.class);
                    startActivity(intent);
                    return true;
                case R.id.all_contacts:
                    pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);
                    SharedPreferences.Editor value = pref.edit();
                    value.putString("CurrentPage","2");
                    value.commit();
                    Intent intent_2 = new Intent(getApplicationContext(), AllContacts.class);
                    startActivity(intent_2);
                    return true;
                case R.id.enter_mobile:
                    pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);
                    SharedPreferences.Editor value2 = pref.edit();
                    value2.putString("CurrentPage","3");
                    value2.commit();
                    Intent intent_3 = new Intent(getApplicationContext(), AllContacts.class);
                    startActivity(intent_3);
                    return true;

            }

            updateNavigationBarState(item.getItemId());

            return true;
        }

    };
    private String page;
    private ProgressDialog mProgressDialog;
    private AllContactsAdapter contactAdapter;
    private TextView update, delete;
    private BottomNavigationView navigation;
    private CountryCodePicker ccp;
    private Switch switch1;
    private TextView no_favourites;
    private ImageView imv;
    private Boolean checked_status=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);
        page = pref.getString("CurrentPage","1");
        Log.e("TEST","dhgdhg");
        mProgressDialog = new ProgressDialog(this);

        if (!checkPermission()) {
            Log.e("TEST","aa");

            ActivityCompat.requestPermissions(AllContacts.this,
                    new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS},
                    PERMISSION_REQUEST_CODE);

        }
        else{
            Log.e("TEST","bb");





            if(page.equalsIgnoreCase("1")){
                Log.e("TEST",page);

                setContentView(R.layout.activity_all_contacts);

                //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
               // setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.title_activity_all_contacts));

                imv = (ImageView) findViewById(R.id.imageView);
                no_favourites = (TextView) findViewById(R.id.no_favourites);
                phone_number = (EditText) findViewById(R.id.p_number);
                receipientName = (EditText) findViewById(R.id.receipientName);
                first_name = (EditText) findViewById(R.id.first_name);
                last_name = (EditText) findViewById(R.id.last_name);
                receipientName.setVisibility(View.GONE);
                first_name.setVisibility(View.GONE);
                last_name.setVisibility(View.GONE);
                submit = (Button) findViewById(R.id.submit);
                switch1 = (Switch) findViewById(R.id.switch1);
                switch1.setVisibility(View.GONE);
                ccp = (CountryCodePicker) findViewById(R.id.ccp);
                ccp.setDefaultCountryUsingNameCode("TZ");
                ccp.setClickable(true);
                ccp.setVisibility(View.GONE);
                /*update = (TextView) findViewById(R.id.update);
                delete = (TextView) findViewById(R.id.delete);*/
                phone_number.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);

                recyclerView = (RecyclerView) findViewById(R.id.rvContacts);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                db = new UhuruDataSource(this);
                transactions = db.getFriendsFromAlias();
                if(db.friendAliasCheck()==0)
                {
                    imv.setVisibility(View.VISIBLE);
                    no_favourites.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    imv.setVisibility(View.GONE);
                    no_favourites.setVisibility(View.GONE);
                    adapter = new AllContactsAdapter(transactions,this,db,"1");
                    recyclerView.setAdapter(adapter);
                }


                navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setSelectedItemId(R.id.favourite);
                navigation.setBackgroundColor(getResources().getColor(R.color.mvisa_sdk_orange));
                navigation.setVerticalScrollbarPosition(0);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            }
            else if(page.equalsIgnoreCase("2")){
                Log.e("TEST",page);

                setContentView(R.layout.activity_all_contacts);
             //   Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
             //   setSupportActionBar(toolbar2);
              //  setSupportActionBar(toolbar2);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.all_contacts));

                phone_number = (EditText) findViewById(R.id.p_number);
                receipientName = (EditText) findViewById(R.id.receipientName);
                first_name = (EditText) findViewById(R.id.first_name);
                last_name = (EditText) findViewById(R.id.last_name);
                receipientName.setVisibility(View.GONE);
                first_name.setVisibility(View.GONE);
                last_name.setVisibility(View.GONE);
                ccp = (CountryCodePicker) findViewById(R.id.ccp);
                ccp.setDefaultCountryUsingNameCode("TZ");
                ccp.setClickable(true);
                ccp.setVisibility(View.GONE);

                submit = (Button) findViewById(R.id.submit);
                phone_number.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                switch1 = (Switch) findViewById(R.id.switch1);
                switch1.setVisibility(View.GONE);
                getAllContacts();
                navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setSelectedItemId(R.id.all_contacts);
                navigation.setVerticalScrollbarPosition(1);
                navigation.setBackgroundColor(getResources().getColor(R.color.mvisa_sdk_orange));
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            }
            else if(page.equalsIgnoreCase("3")){
                Log.e("TEST",page);
                setContentView(R.layout.activity_all_contacts);

           //     Toolbar toolbar3 = (Toolbar) findViewById(R.id.toolbar);
            //    setSupportActionBar(toolbar3);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.enter_mobile));
                phone_number = (EditText) findViewById(R.id.p_number);
                setLength(phone_number,13);

                receipientName = (EditText) findViewById(R.id.receipientName);
                receipientName = (EditText) findViewById(R.id.receipientName);
                first_name = (EditText) findViewById(R.id.first_name);
                last_name = (EditText) findViewById(R.id.last_name);
                switch1 = (Switch) findViewById(R.id.switch1);

                ccp = (CountryCodePicker) findViewById(R.id.ccp);
                ccp.setDefaultCountryUsingNameCode("TZ");
                ccp.setClickable(true);
                ccp.setVisibility(View.VISIBLE);
                first_name.setVisibility(View.GONE);
                last_name.setVisibility(View.GONE);

                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // The toggle is enabled
                            checked_status=isChecked;
                            phone_number.setHint(R.string.mvisa_sdk_card_number);
                            switch1.setText(R.string.switch_phone);
                            setLength(phone_number,16);
                            ccp.setVisibility(View.GONE);
                            receipientName.setVisibility(View.GONE);
                            first_name.setVisibility(View.VISIBLE);
                            last_name.setVisibility(View.VISIBLE);

                        } else {
                            // The toggle is disabled
                            checked_status=isChecked;
                            receipientName.setVisibility(View.VISIBLE);
                            first_name.setVisibility(View.GONE);
                            last_name.setVisibility(View.GONE);
                            phone_number.setHint(R.string.phone_number);
                            switch1.setText(R.string.switch_card);
                            setLength(phone_number,13);
                            ccp.setVisibility(View.VISIBLE);
                        }
                    }
                });


                submit = (Button) findViewById(R.id.submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //Phone number condition
                        if(checked_status==false){
                            receipientName.setVisibility(View.VISIBLE);
                            first_name.setVisibility(View.GONE);
                            last_name.setVisibility(View.GONE);
                            Log.v("CHECK-STATUS","Phonenumber");
                            if(receipientName.getText().toString().length()==0)
                            {
                                ErrorMessage(receipientName.getText().toString(),receipientName,getString(R.string.please_enter_fullname));
                                ClearError(receipientName,getString(R.string.please_enter_fullname));
                            }
                            if(phone_number.getText().toString().length()==0)
                            {
                                ErrorMessage(phone_number.getText().toString(),phone_number,getString(R.string.please_enter_phone));
                                ClearError(phone_number,getString(R.string.please_enter_phone));
                            }

                            else if(receipientName.getText().toString().length()>0 && phone_number.getText().toString().length()>0)
                            {
                                Log.v("CHECK-STATUS","CHECK-1");

                                if(phone_number.getText().toString().substring(0,1).equalsIgnoreCase("0"))
                                {
                                    mobile_number = ccp.getSelectedCountryCode()+phone_number.getText().toString().substring(1);
                                }else{
                                    mobile_number = ccp.getSelectedCountryCode()+phone_number.getText().toString();
                                }

                                //This will only happen if validation is passed
                                if(ccp.getSelectedCountryCode().equalsIgnoreCase("255") || ccp.getSelectedCountryCode().equalsIgnoreCase("254"))
                                {
                                    Intent intent = new Intent(AllContacts.this,PayFriend.class);
                                    intent.putExtra("phone",mobile_number);
                                    intent.putExtra("person_name",receipientName.getText().toString().trim());
                                    intent.putExtra("status",false);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(AllContacts.this, getString(R.string.not_allow), Toast.LENGTH_SHORT).show();
                                }

                            }


                        }
                        //full card number condition
                        else if(checked_status==true) {
                            receipientName.setVisibility(View.GONE);
                            first_name.setVisibility(View.VISIBLE);
                            last_name.setVisibility(View.VISIBLE);
                            Log.v("CHECK-STATUS","Cardnumber");
                            if(first_name.getText().toString().length()==0)
                            {
                                ErrorMessage(first_name.getText().toString(),first_name,getString(R.string.please_fill_field));
                                ClearError(first_name,getString(R.string.please_enter_fullname));
                            }
                            if( last_name.getText().toString().length()==0)
                            {
                                ErrorMessage(last_name.getText().toString(),last_name,getString(R.string.please_fill_field));
                                ClearError(last_name,getString(R.string.please_enter_fullname));
                            }
                            if(phone_number.getText().toString().length()==0 || phone_number.getText().toString().length()<16)
                            {
                                ErrorMessage(phone_number.getText().toString(),phone_number,getString(R.string.please_enter_phone));
                                ClearError(phone_number,getString(R.string.please_enter_cardnumber));
                            }

                            if (phone_number.getText().toString().length() == 16 && first_name.getText().toString().length()>0 && last_name.getText().toString().length()>0) {
                                Cards cards = new Cards();
                                Boolean status = cards.luhnCheck(phone_number.getText().toString());
                                Log.v("CHECK-STATUS",""+status);
                                if (status == true) {
                                    try {
                                        Log.v("CHECK-STATUS", "CHECK-2");
                                        mobile_number = phone_number.getText().toString().trim();
                                        RequestQueue queue = Action.getInstance(getApplicationContext()).getRequestQueue();

                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.accumulate("MTI", "0680");
                                        jsonObject.accumulate("recipientPrimaryAccountNumber", mobile_number);

                                        String ENDPOINT = API.VISA_URL;

                                        JSONObject reqMsg = jsonObject;
                                        Log.v("STATEMANNT", String.valueOf(reqMsg));
                                        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, ENDPOINT, jsonObject, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject jsonObject) {
                                                try {
                                                    Log.v("STATEMANNT",""+jsonObject);
                                                    String visaInfo = Action.getFieldValue(jsonObject, "visaNetworkInfo");

                                                    Log.v("STATEMANNT",visaInfo);

                                                    JSONObject checkCurrencyCode = new JSONObject(visaInfo);

                                                    String billingCurrencyCode = Action.getFieldValue(checkCurrencyCode,"billingCurrencyCode");

                                                    Log.v("STATEMANNT",billingCurrencyCode);

                                                    //This will only happen if validation is passed
                                                    if(billingCurrencyCode.equalsIgnoreCase("834") || billingCurrencyCode.equalsIgnoreCase("404"))
                                                    {
                                                        Intent intent = new Intent(AllContacts.this, PayFriend.class);
                                                        intent.putExtra("phone", mobile_number);
                                                        intent.putExtra("person_name", first_name.getText().toString().trim()+" "+last_name.getText().toString().trim());
                                                        intent.putExtra("status",true);
                                                        startActivity(intent);
                                                    }else{
                                                        Toast.makeText(AllContacts.this, getString(R.string.not_allowed_code), Toast.LENGTH_SHORT).show();
                                                    }

                                                }catch (JSONException e)
                                                {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {

                                            }
                                        });
                                        myReq.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        queue.add(myReq);
                                    }catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                } else {

                                }
                            }
                            else if (phone_number.getText().toString().length() < 16 && first_name.getText().toString().length()>0 && last_name.getText().toString().length()>0) {
                                    Toast.makeText(AllContacts.this, getString(R.string.invalid_card), Toast.LENGTH_SHORT).show();
                            }else{
                                    Toast.makeText(AllContacts.this, getString(R.string.invalid_card), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

                navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setSelectedItemId(R.id.enter_mobile);
                navigation.setBackgroundColor(getResources().getColor(R.color.mvisa_sdk_orange));
                navigation.setFocusable(true);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            }



        }
    }


    /***********************
     *Displays error message
     ***********************/
    public static void ErrorMessage(String j, final EditText k, String l){
        if(j.length() == 0){
            k.setError(l);
        }
    }

    /***********************
     *Clears error
     ***********************/
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

    private void setLength(EditText editText, int len) {
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(len) });
    }


    /**Permissions **/
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(AllContacts.this, Manifest.permission.READ_CONTACTS);
        int result2 = ContextCompat.checkSelfPermission(AllContacts.this, Manifest.permission.WRITE_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED  ){

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AllContacts.this.startActivity(new Intent(AllContacts.this.getApplicationContext(), AllContacts.class));
                } else {
                    finish();
                }
                break;

        }
    }
    /*****End of permission request ****/

    private void getAllContacts() {
        ArrayList<Msg> contactVOList = new ArrayList();
        Msg contactVO;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" COLLATE LOCALIZED ASC LIMIT 30");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new Msg();
                    contactVO.setName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        contactVO.setPhone_number(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    contactVOList.add(contactVO);
                }
            }

            contactAdapter = new AllContactsAdapter(contactVOList,AllContacts.this,db,"2");
            rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
            rvContacts.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvContacts.setLayoutManager(layoutManager);
            rvContacts.setItemAnimator(new DefaultItemAnimator());
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
            rvContacts.setAdapter(contactAdapter);
        }
    }

    private void getContacts() {
        ArrayList<Msg> contactVOList = new ArrayList();
        Msg contactVO;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" COLLATE LOCALIZED ASC LIMIT 30");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new Msg();
                    contactVO.setName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        contactVO.setPhone_number(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    contactVOList.add(contactVO);
                }
            }

            contactAdapter = new AllContactsAdapter(contactVOList,AllContacts.this,db,"2");
            rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
            rvContacts.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvContacts.setLayoutManager(layoutManager);
            rvContacts.setItemAnimator(new DefaultItemAnimator());
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
            rvContacts.setAdapter(contactAdapter);


        }

//        return contactVOList;
    }

    private void getAllContacts(String phone_number) {
        ArrayList<Msg> contactVOList = new ArrayList();
        Msg contactVO;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,  "display_name like "+"'%"+phone_number+"%'", null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" COLLATE LOCALIZED ASC LIMIT 30");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new Msg();
                    contactVO.setName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        contactVO.setPhone_number(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    contactVOList.add(contactVO);
                }
            }

            contactAdapter = new AllContactsAdapter(contactVOList,AllContacts.this,db,"2");
            rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
            rvContacts.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvContacts.setLayoutManager(layoutManager);
            rvContacts.setItemAnimator(new DefaultItemAnimator());
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
            rvContacts.setAdapter(contactAdapter);


        }

//        return contactVOList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       /* if(id == R.id.action_settings)
        {
            Intent intent = new Intent(AllContacts.this, AddNewAlias.class);
            startActivity(intent);
            AllContacts.this.finish();
        }*  else*/
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(AllContacts.this,Login.class);
            startActivity(intent);
            AllContacts.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String query) {

        if(page.equalsIgnoreCase("1"))
        {
            try {
                if (query.trim().length() > 0) {
                    transactions = db.getFriendsAlias(query);
                    adapter = new AllContactsAdapter(transactions,getApplicationContext(),db,"1");
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                }else{
                    transactions = db.getFriendsFromAlias();
                    adapter = new AllContactsAdapter(transactions,getApplicationContext(),db,"1");
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                }

            }catch (NullPointerException e)
            {
                transactions = db.getFriendsFromAlias();
                adapter = new AllContactsAdapter(transactions,getApplicationContext(),db,"1");
                recyclerView.setAdapter(adapter);
            }


        }
        else if(page.equalsIgnoreCase("2")) {
            try {
                if (query.trim().length() > 0) {
                    getAllContacts(query);
                }else{
                    getContacts();
                }
            }catch (NullPointerException e)
            {
                    getContacts();
            }

        }


        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.v("STATEMANNT",query);
        if(page.equalsIgnoreCase("1"))
        {
            try {
                if (query.trim().length() > 0) {
                    transactions = db.getFriendsAlias(query);
                    adapter = new AllContactsAdapter(transactions,getApplicationContext(),db,"1");
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                }else{
                    transactions = db.getFriendsFromAlias();
                    adapter = new AllContactsAdapter(transactions,getApplicationContext(),db,"1");
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                }

            }catch (NullPointerException e)
            {
                transactions = db.getFriendsFromAlias();
                adapter = new AllContactsAdapter(transactions,getApplicationContext(),db,"1");
                recyclerView.setAdapter(adapter);
            }


        }
        else if(page.equalsIgnoreCase("2")) {
            try {
                if (query.trim().length() > 0) {
                    getAllContacts(query);
                }else{
                    getContacts();
                }
            }catch (NullPointerException e)
            {
                getContacts();
            }

        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        AllContacts.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AllContacts.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    private void updateNavigationBarState(int actionId){
        Menu menu = navigation.getMenu();

        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == actionId);
        }
    }

}
