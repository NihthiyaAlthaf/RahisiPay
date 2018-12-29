package com.goandroytech.www.rahisipay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.goandroytech.www.rahisipay.Adapter.Service_Adapter;
import com.goandroytech.www.rahisipay.Pay_Visa.AllContacts;
import com.goandroytech.www.rahisipay.Dialog.ChangePIN_Dialog;
import com.goandroytech.www.rahisipay.Model.Service_Model;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {
    RecyclerView recyclerView;
    Service_Adapter mAdapter;
    public static String PHONE ="phone";
    String SERVICE="service";
    String PIN="pin";
    String get_name;
    String get_id;
    public static String NAME ="name";
    public static String ID ="id";
    public   String SHARED_PREF_NAME = "mysharedpref";
    static SharedPreferences sp;
    Spinner filter;
    LinearLayout visa,qr_scan;
    String[] arr_filter = {"All","Linked","Unlinked"};
    static public List<Service_Model> serviceList = new ArrayList<>();
    String CARD_STATUS ="card_state";
    String get_card_state;
    TextView alert_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        visa =(LinearLayout)findViewById(R.id.visa);
        qr_scan =(LinearLayout)findViewById(R.id.qr_scan);
        filter = (Spinner) findViewById(R.id.filter);
        alert_msg = (TextView)findViewById(R.id.alert_msg);
        filter.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spin,arr_filter);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(aa);

        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        get_name = sp.getString(NAME, null);
        get_id = sp.getString(ID, null);
        get_card_state = sp.getString(CARD_STATUS,null);

        if (get_card_state.equals("1")){
            alert_msg.setVisibility(View.GONE);
        } else if (get_card_state.equals("5")){
            alert_msg.setText(getString(R.string.blocked_msg));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        if (get_card_state.equals("1")){
            menu.findItem(R.id.nav_block_card).setTitle("Block Card");
        } else if (get_card_state.equals("5")){
            menu.findItem(R.id.nav_block_card).setTitle("UnBlock");
        }
        View hView =  navigationView.getHeaderView(0);
        TextView customer_name = (TextView)hView.findViewById(R.id.customer_name);
        TextView customer_account_no = (TextView)hView.findViewById(R.id.customer_account_no);
        customer_name.setText(get_name);
        customer_account_no.setText(get_id);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
     //   forgot =(TextView)findViewById(R.id.forgot);
        mAdapter = new Service_Adapter(Login.serviceList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences sp_visa;
//                sp_visa = getSharedPreferences("UhuruPayPref",0);
//                SharedPreferences.Editor e = sp_visa.edit();
//                e.putString("CurrentPage","1");
//                e.commit();
                Intent intent = new Intent(Home.this,PayScan.class);
                startActivity(intent);
            }
        });

        qr_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(Home.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

      /*  recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Service_Model service = Login.serviceList.get(position);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(SERVICE, service.getTitle());
                editor.commit();
                CustomDialog cdd=new CustomDialog(Home.this);
                cdd.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        })); */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
          //  super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_services) {
            // Handle the camera action
        } else if (id == R.id.nav_account_details) {
            Intent intent = new Intent(Home.this,MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_view_transaction){
            Intent intent = new Intent(Home.this,ViewTransaction.class);
            startActivity(intent);

        }  else if (id == R.id.nav_pay_qrcode) {
            IntentIntegrator integrator = new IntentIntegrator(Home.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        } else if (id == R.id.nav_block_card) {
            Intent intent = new Intent(Home.this,Card.class);
            startActivity(intent);

        } else if (id == R.id.nav_change_pin) {
            ChangePIN_Dialog cdd=new ChangePIN_Dialog(Home.this);
            cdd.show();
        } else if (id == R.id.nav_logout) {
            openLogoutConfirmation();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openLogoutConfirmation() {
        LayoutInflater factory = LayoutInflater.from(Home.this);
        final View textEntryView = factory.inflate(R.layout.activity_confirm, null);
        TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
        tv_message.setText("Are you sure you want to Logout?");
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button)textEntryView.findViewById(R.id.btn_cancel);
        btn_ok.setText("Yes");
        btn_cancel.setText("No");
        final AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
        alert.setCancelable(true);
        final AlertDialog alertDialog = alert.setView(textEntryView).create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
                updateLoginPrefrence();
                Intent intent = new Intent(Home.this,Login.class);
                startActivity(intent);            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new Service_Adapter(Login.serviceList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        get_card_state = sp.getString(CARD_STATUS, null);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        if (get_card_state.equals("1")){
            menu.findItem(R.id.nav_block_card).setTitle("Block Card");
            alert_msg.setVisibility(View.GONE);
        } else if (get_card_state.equals("5")){
            alert_msg.setVisibility(View.VISIBLE);
            menu.findItem(R.id.nav_block_card).setTitle("UnBlock");
            alert_msg.setText(getString(R.string.blocked_msg));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position==0){

            mAdapter = new Service_Adapter(Login.serviceList);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        } else if (position==1){
            serviceList.clear();
            for (int i = 0;i<Login.array_subscribe.size();i++){
                if (Login.array_subscribe.get(i).equals("1")){
                    Service_Model service = new Service_Model(Login.array_service_id.get(i),Login.array_image.get(i), Login.array_service_name.get(i),Login.array_logo_url.get(i),Login.array_parent.get(i),
                            Login.array_subscribe.get(i),Login.array_subscriptionAccounte.get(i));
                    serviceList.add(service);
                }
                mAdapter = new Service_Adapter(serviceList);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }

        } else if (position==2){
            serviceList.clear();
            for (int i = 0;i<Login.array_subscribe.size();i++){
                if (Login.array_subscribe.get(i).equals("0")){
                    Service_Model service = new Service_Model(Login.array_service_id.get(i),Login.array_image.get(i), Login.array_service_name.get(i),Login.array_logo_url.get(i),Login.array_parent.get(i),
                            Login.array_subscribe.get(i),Login.array_subscriptionAccounte.get(i));
                    serviceList.add(service);


                }
                mAdapter = new Service_Adapter(serviceList);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateLoginPrefrence() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PHONE, "");
        editor.apply();
        editor.commit();
    }
}
