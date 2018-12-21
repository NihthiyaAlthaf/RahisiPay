package com.goandroytech.www.rahisipay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.goandroytech.www.rahisipay.Adapter.RecyclerTouchListener;
import com.goandroytech.www.rahisipay.Adapter.Service_Adapter;
import com.goandroytech.www.rahisipay.Database.AllContacts;
import com.goandroytech.www.rahisipay.Database.PayFriend;
import com.goandroytech.www.rahisipay.Dialog.ChangePIN_Dialog;
import com.goandroytech.www.rahisipay.Dialog.CustomDialog;
import com.goandroytech.www.rahisipay.Model.Service_Model;

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
    Button visa;
    String[] arr_filter = {"All","Linked","Unlinked"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        visa =(Button)findViewById(R.id.visa);
        filter = (Spinner) findViewById(R.id.filter);
        filter.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spin,arr_filter);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(aa);

        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        get_name = sp.getString(NAME, null);
        get_id = sp.getString(ID, null);

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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
     //   forgot =(TextView)findViewById(R.id.forgot);
        mAdapter = new Service_Adapter(Login.serviceList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp_visa;
                sp_visa = getSharedPreferences("UhuruPayPref",0);
                SharedPreferences.Editor e = sp_visa.edit();
                e.putString("CurrentPage","1");
                e.commit();
                Intent intent = new Intent(Home.this,AllContacts.class);
                startActivity(intent);
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
        } else if (id == R.id.nav_account_details) {
            Intent intent = new Intent(Home.this,MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_view_transaction){
            Intent intent = new Intent(Home.this,ViewTransaction.class);
            startActivity(intent);

        }  else if (id == R.id.nav_pay_qrcode) {

        } else if (id == R.id.nav_block_card) {
            finish();
            Intent intent = new Intent(Home.this,BlockCard.class);
            startActivity(intent);

        } else if (id == R.id.nav_change_pin) {
            ChangePIN_Dialog cdd=new ChangePIN_Dialog(Home.this);
            cdd.show();
        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
            builder1.setMessage("Do You Want to Logout?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                            Intent intent = new Intent(Home.this,Login.class);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
