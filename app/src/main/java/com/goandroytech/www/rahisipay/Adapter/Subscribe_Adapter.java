package com.goandroytech.www.rahisipay.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goandroytech.www.rahisipay.Model.Service_Model;
import com.goandroytech.www.rahisipay.R;

import java.util.List;

public class Subscribe_Adapter extends RecyclerView.Adapter<Subscribe_Adapter.MyViewHolder> {
    Context context;

    private List<Service_Model> serviceList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView)view.findViewById(R.id.image);
        }
    }


    public Subscribe_Adapter(List<Service_Model> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_subscribe__adapter, parent, false);
        context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Service_Model service = serviceList.get(position);
        holder.title.setText(service.getTitle());
        Glide.with(context).load(service.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
      //  holder.image.setImageResource(service.getImage());

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }
}
