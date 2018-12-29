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
import com.goandroytech.www.rahisipay.Model.Child;
import com.goandroytech.www.rahisipay.Model.Service_Model;
import com.goandroytech.www.rahisipay.R;
import com.goandroytech.www.rahisipay.SelectService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Child_Adapter extends RecyclerView.Adapter<Child_Adapter.MyViewHolder> {
    Context context;

    public static List<Child> childList;

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


    public Child_Adapter(List<Child> childList) {
        this.childList = childList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_child_adapter, parent, false);
        context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Child child = childList.get(position);

            holder.title.setText(child.getTitle());
            Picasso.with(context).load(child.getUrl() + child.getImage()).into(holder.image);


        if (child.getSubscribe().equals("0")){
            holder.star.setBackgroundResource(R.drawable.star);
        } else if (child.getSubscribe().equals("1")){
            holder.star.setBackgroundResource(R.drawable.yellow_star);
        }

            holder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SelectService.class);
                    intent.putExtra("logo_url",child.getUrl()+child.getImage());
                    intent.putExtra("service_name",child.getTitle());
                    intent.putExtra("service_id",child.getService_id());
                    intent.putExtra("subscribed",child.getSubscribe());
                    intent.putExtra("subscriptionAccount",child.getSubscriptionAccount());
                    intent.putExtra("from","group");
                    context.startActivity(intent);
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
        return childList.size();
    }
}
