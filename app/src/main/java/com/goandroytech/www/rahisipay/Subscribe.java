package com.goandroytech.www.rahisipay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goandroytech.www.rahisipay.Adapter.RecyclerTouchListener;
import com.goandroytech.www.rahisipay.Adapter.Service_Adapter;
import com.goandroytech.www.rahisipay.Adapter.Subscribe_Adapter;
import com.goandroytech.www.rahisipay.Dialog.CustomDialog;
import com.goandroytech.www.rahisipay.Model.Service_Model;

import java.util.ArrayList;
import java.util.List;

public class Subscribe extends AppCompatActivity {
    RecyclerView recyclerView;
    Subscribe_Adapter mAdapter;
  //  private List<Service_Model> serviceList = new ArrayList<>();
    static SharedPreferences preferences;
    public String SHARED_PREF_NAME = "mysharedpref";
    String SERVICE="service";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Intent intent = getIntent();
        String title = intent.getStringExtra("type");
        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        try{
            if (title.equals("link")){
                this.getSupportActionBar().setTitle("Subscribe Service");
            } else if (title.equals("linked")){
                this.getSupportActionBar().setTitle("Unsubscribe Service");
            }

        }
        catch (Exception e){}

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new Subscribe_Adapter(Login.serviceList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

       // loadServiceData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Service_Model service = Login.serviceList.get(position);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SERVICE, service.getTitle());
                editor.commit();
                CustomDialog cdd=new CustomDialog(Subscribe.this);
                cdd.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

   /* private void loadServiceData() {

        Service_Model service = new Service_Model(R.drawable.topup, "TOP UP");
        serviceList.add(service);

        service = new Service_Model(R.drawable.passport, "PASSPORT");
        serviceList.add(service);

        service = new Service_Model(R.drawable.zbc, "ZBC");
        serviceList.add(service);

        service = new Service_Model(R.drawable.comnet, "COMNET");
        serviceList.add(service);

        service = new Service_Model(R.drawable.dstv, "DSTV");
        serviceList.add(service);

        service = new Service_Model(R.drawable.island, "ISLAND TV");
        serviceList.add(service);

        service = new Service_Model(R.drawable.zeco, "ZECO");
        serviceList.add(service);

        service = new Service_Model(R.drawable.zctv, "ZCTV");
        serviceList.add(service);

        mAdapter.notifyDataSetChanged();

    } */

}
