package com.goandroytech.www.rahisipay.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.goandroytech.www.rahisipay.Model.Service_Model;
import com.goandroytech.www.rahisipay.Model.Transaction;
import com.goandroytech.www.rahisipay.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<Transaction> transactionList;

    Context context;
    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  nature, amount,desc;
        public ImageButton view_eye;

        public MyViewHolder(View view) {
            super(view);
         //   date = (TextView) view.findViewById(R.id.date);
            nature = (TextView) view.findViewById(R.id.nature);
            desc = (TextView) view.findViewById(R.id.desc);
         //   service = (TextView) view.findViewById(R.id.service);
            amount = (TextView) view.findViewById(R.id.amount);
            view_eye = (ImageButton) view.findViewById(R.id.view);
          //  charges = (TextView) view.findViewById(R.id.charges);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_transaction_adapter, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.nature.setText(transaction.getNature());
        holder.amount.setText("TSh "+transaction.getAmount());
        holder.desc.setText(transaction.getDescription());
        holder.view_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //  holder.date.setText(transaction.getDate());
        //  holder.service.setText(transaction.getDescription());
        //  holder.charges.setText(transaction.getCharge());

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
