package com.goandroytech.www.rahisipay;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goandroytech.www.rahisipay.Adapter.RecyclerTouchListener;
import com.goandroytech.www.rahisipay.Adapter.Service_Adapter;
import com.goandroytech.www.rahisipay.Adapter.TransactionAdapter;
import com.goandroytech.www.rahisipay.Connection.ConnectivityDetector;
import com.goandroytech.www.rahisipay.Connection.MyDividerItemDecoration;
import com.goandroytech.www.rahisipay.Dialog.ChangePIN_Dialog;
import com.goandroytech.www.rahisipay.Dialog.CustomDialog;
import com.goandroytech.www.rahisipay.Dialog.TransactionDialog;
import com.goandroytech.www.rahisipay.Model.Service_Model;
import com.goandroytech.www.rahisipay.Model.Transaction;
import com.goandroytech.www.rahisipay.apicalls.API;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewTransaction extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {
    TextView from_date,to_date;
    DatePickerDialog datePickerDialog;
    String select_date_from ="",select_date_to = "";
    RequestQueue queue;
    SharedPreferences sp;
    public String SHARED_PREF_NAME = "mysharedpref";
    String PHONE ="phone";
    String PIN ="pin";
    String get_phone,get_pin;
    static public List<Transaction> transactionList = new ArrayList<>();
    RecyclerView recyclerView;
    TransactionAdapter mAdapter;
    Spinner nature_all,service_all;
    String get_name;
    String get_id;
    public static String NAME ="name";
    public static String ID ="id";
    ConnectivityDetector connectivityDetector;
    public static String NATURE = "nature";
    public static String DATE_TIME = "date_time";
    public static String DESCRIPTION = "desc";
    public static String AMOUNT = "amount";
    public static String CHARGES = "charges";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(ViewTransaction.this);
        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        get_phone = sp.getString(PHONE, null);
        get_pin = sp.getString(PIN, null);

        get_name = sp.getString(NAME, null);
        get_id = sp.getString(ID, null);
        transactionList.clear();
        connectivityDetector = new ConnectivityDetector(ViewTransaction.this);

        if (connectivityDetector.checkConnectivityStatus()) {
                myTransaction();
        } else {
            connectivityDetector.showAlertDialog(ViewTransaction.this, "Connection Error!", "No internet connection");

        }

        from_date = (TextView)findViewById(R.id.from_date);
        to_date = (TextView)findViewById(R.id.to_date);
        nature_all = (Spinner)findViewById(R.id.nature_all);
        service_all = (Spinner)findViewById(R.id.service_all);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(NATURE, transactionList.get(position).getNature());
                editor.putString(DATE_TIME, transactionList.get(position).getDate());
                editor.putString(DESCRIPTION, transactionList.get(position).getDescription());
                editor.putString(CHARGES, transactionList.get(position).getCharge());
                editor.putString(AMOUNT, transactionList.get(position).getAmount());
                editor.apply();
                editor.commit();
                TransactionDialog transactionDialog =new TransactionDialog(ViewTransaction.this);
                transactionDialog.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Spinner click listener
        nature_all.setOnItemSelectedListener(this);
        service_all.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> list_nature = new ArrayList<String>();
        list_nature.add("Nature All");


        // Spinner Drop down elements
        List<String> list_service = new ArrayList<String>();
        list_service.add("Service All");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_nature);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nature_all.setAdapter(dataAdapter);

        // Creating adapter for spinner
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_service);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service_all.setAdapter(serviceAdapter);

        //   forgot =(TextView)findViewById(R.id.forgot);

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(ViewTransaction.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");


                                select_date_from = (year+ "-" + (monthOfYear+1) + "-" + dayOfMonth);
                                from_date.setText(select_date_from);
                                if (!(select_date_from.equals(""))&&!(select_date_to.equals(""))){
                                    transactionList.clear();
                                    filterTransaction();
                                }

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(ViewTransaction.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");


                               // select_date_to = (dayOfMonth+ "-" + (monthOfYear+1) + "-" + year);
                                select_date_to = (year+ "-" + (monthOfYear+1) + "-" + dayOfMonth);
                                to_date.setText(select_date_to);
                                //date.setText(select_date);
                                if (!select_date_from.equals("")&!select_date_to.equals("")){
                                    filterTransaction();
                                }

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView customer_name = (TextView)hView.findViewById(R.id.customer_name);
        TextView customer_account_no = (TextView)hView.findViewById(R.id.customer_account_no);
        customer_name.setText("Hello ,"+get_name);
        customer_account_no.setText(get_id);
    }

    private void filterTransaction() {

        final ProgressDialog progressDialog = new ProgressDialog(ViewTransaction.this);
        progressDialog.setMessage("Fetching Details ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, API.BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);
                            JSONObject jsonObject = json_res.getJSONObject("response");

                            JSONArray jsonArray = jsonObject.getJSONArray("transactions");

                            if (jsonArray.length()>0){
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String get_date = jsonObject1.getString("date");
                                    String get_description = jsonObject1.getString("description");
                                    String get_amount = jsonObject1.getString("amount");
                                    String get_charge = jsonObject1.getString("charge");
                                    String get_nature = jsonObject1.getString("nature");

                                    Transaction transaction = new Transaction(get_date,get_description,get_amount,get_charge,get_nature);
                                    transactionList.add(transaction);

                                }
                                progressDialog.dismiss();
                                mAdapter = new TransactionAdapter(transactionList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                transactionList.clear();
                                mAdapter = new TransactionAdapter(transactionList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);
                                progressDialog.dismiss();
                                MDToast mdToast = MDToast.makeText(ViewTransaction.this,"No Data Found!",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);
                                mdToast.show();
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
                        MDToast mdToast = MDToast.makeText(ViewTransaction.this,error.toString(),Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);
                        mdToast.show();
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
                params.put("phone", get_phone);
                params.put("pin", get_pin);
                params.put("from_date", select_date_from);
                params.put("to_date", select_date_to);
                params.put("service_id", API.TRANSACTION_FILTER_SERVICE_ID);

                return params;
            }
        };
        queue.add(postRequest);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_services) {
            // Handle the camera action
            finish();
            Intent intent = new Intent(ViewTransaction.this,Home.class);
            startActivity(intent);
        }  else if (id == R.id.nav_account_details) {
            finish();
            Intent intent = new Intent(ViewTransaction.this,MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_view_transaction){


        }  else if (id == R.id.nav_pay_qrcode) {

        } else if (id == R.id.nav_block_card) {
            finish();
            Intent intent = new Intent(ViewTransaction.this,BlockCard.class);
            startActivity(intent);
        } else if (id == R.id.nav_change_pin) {
            ChangePIN_Dialog cdd=new ChangePIN_Dialog(ViewTransaction.this);
            cdd.show();
        }  else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewTransaction.this);
            builder1.setMessage("Do You Want to Logout?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                            Intent intent = new Intent(ViewTransaction.this,Login.class);
                            startActivity(intent);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void myTransaction() {

        final ProgressDialog progressDialog = new ProgressDialog(ViewTransaction.this);
        progressDialog.setMessage("Fetching Details ...");
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, API.BASE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject json_res = new JSONObject(response);
                            JSONObject jsonObject = json_res.getJSONObject("response");

                            JSONArray jsonArray = jsonObject.getJSONArray("transactions");

                            if (jsonArray.length()>0){
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String get_date = jsonObject1.getString("date");
                                    String get_description = jsonObject1.getString("description");
                                    String get_amount = jsonObject1.getString("amount");
                                    String get_charge = jsonObject1.getString("charge");
                                    String get_nature = jsonObject1.getString("nature");

                                    Transaction transaction = new Transaction(get_date,get_description,get_amount,get_charge,get_nature);
                                    transactionList.add(transaction);

                                }
                                progressDialog.dismiss();
                                mAdapter = new TransactionAdapter(transactionList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.addItemDecoration(new MyDividerItemDecoration(ViewTransaction.this, LinearLayoutManager.VERTICAL, 16));
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                progressDialog.dismiss();
                                MDToast mdToast = MDToast.makeText(ViewTransaction.this,"Unable to Load Data!",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);
                                mdToast.show();
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
                        MDToast mdToast = MDToast.makeText(ViewTransaction.this,error.toString(),Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);
                        mdToast.show();
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
                params.put("phone", get_phone);
                params.put("pin", get_pin);
                params.put("service_id", API.TRANSACTION_SERVICE_ID);

                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (id== R.id.nature_all) {

        } else if (id == R.id.service_all);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
