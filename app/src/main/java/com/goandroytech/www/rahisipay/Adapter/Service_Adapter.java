package com.goandroytech.www.rahisipay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goandroytech.www.rahisipay.ChildService;
import com.goandroytech.www.rahisipay.Login;
import com.goandroytech.www.rahisipay.Model.Service_Model;
import com.goandroytech.www.rahisipay.R;
import com.goandroytech.www.rahisipay.SelectService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Service_Adapter extends RecyclerView.Adapter<Service_Adapter.MyViewHolder> {
    Context context;

    public static List<Service_Model> serviceList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        ImageView star;
        LinearLayout lin;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView)view.findViewById(R.id.image);
            star = (ImageView)view.findViewById(R.id.star);
            lin = (LinearLayout)view.findViewById(R.id.lin);
        }
    }


    public Service_Adapter(List<Service_Model> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_service__adapter, parent, false);
        context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Service_Model service = serviceList.get(position);

        if(service.getTitle().length()>13){
            holder.title.setTextSize(10);
        }
        holder.title.setText(service.getTitle());
        Picasso.with(context).load(service.getUrl()+service.getImage()).into(holder.image);

        if (service.getSubscribed().equals("0")){
            holder.star.setBackgroundResource(R.drawable.star);
        } else if (service.getSubscribed().equals("1")){
            holder.star.setBackgroundResource(R.drawable.yellow_star);
        }
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Login.array_parent.contains(service.getService_id())) {
                    Intent intent = new Intent(context,ChildService.class);
                    intent.putExtra("service_id",service.getService_id());
                    intent.putExtra("service_name",service.getTitle());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, SelectService.class);
                    intent.putExtra("logo_url",service.getUrl()+service.getImage());
                    intent.putExtra("service_name",service.getTitle());
                    intent.putExtra("service_id",service.getService_id());
                    intent.putExtra("subscribed",service.getSubscribed());
                    intent.putExtra("subscriptionAccount",service.getSubscriptionAccount());
                    intent.putExtra("from","single");
                    context.startActivity(intent);
                }

            }
        });

    /*    Glide.with(context).load(service.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);*/
        //   holder.image.setImageResource(service.getImage());

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }
}
