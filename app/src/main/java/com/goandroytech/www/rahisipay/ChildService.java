package com.goandroytech.www.rahisipay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.goandroytech.www.rahisipay.Adapter.Child_Adapter;
import com.goandroytech.www.rahisipay.Adapter.Service_Adapter;
import com.goandroytech.www.rahisipay.Model.Child;

import java.util.ArrayList;
import java.util.List;

import static com.goandroytech.www.rahisipay.Login.array_image;
import static com.goandroytech.www.rahisipay.Login.array_logo_url;
import static com.goandroytech.www.rahisipay.Login.array_parent;
import static com.goandroytech.www.rahisipay.Login.array_service_id;
import static com.goandroytech.www.rahisipay.Login.array_service_name;
import static com.goandroytech.www.rahisipay.Login.array_subscribe;
import static com.goandroytech.www.rahisipay.Login.array_subscriptionAccounte;

public class ChildService extends AppCompatActivity {
    Intent intent;
    public static String get_parent_service_id,get_parent_name;
    RecyclerView recyclerView;
    Child_Adapter childAdapter;
    List<Child> childList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        intent = getIntent();
        get_parent_service_id = intent.getStringExtra("service_id");
        get_parent_name = intent.getStringExtra("service_name");
        getSupportActionBar().setTitle(get_parent_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        childList.clear();
        for (int i =0;i<Login.array_parent.size();i++){
            Log.e("print",array_parent.get(i));
            if (get_parent_service_id.equals(Login.array_parent.get(i))){
                Child child = new Child(array_service_id.get(i),array_image.get(i),array_service_name.get(i),array_logo_url.get(i),array_parent.get(i),array_subscribe.get(i),array_subscriptionAccounte.get(i));
                childList.add(child);

            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        childAdapter = new Child_Adapter(childList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(childAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        childList.clear();
        for (int i =0;i<Login.array_parent.size();i++) {
            Log.e("print", array_parent.get(i));
            if (get_parent_service_id.equals(Login.array_parent.get(i))) {
                Child child = new Child(array_service_id.get(i), array_image.get(i), array_service_name.get(i), array_logo_url.get(i), array_parent.get(i), array_subscribe.get(i), array_subscriptionAccounte.get(i));
                childList.add(child);

            }
        }
        childAdapter = new Child_Adapter(childList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(childAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


}