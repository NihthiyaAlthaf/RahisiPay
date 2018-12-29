package com.goandroytech.www.rahisipay.Pay_Visa;


/**
 * Created by jamesb on 2018/03/09.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.goandroytech.www.rahisipay.Model.ContactVO;
import com.goandroytech.www.rahisipay.Model.Msg;
import com.goandroytech.www.rahisipay.R;

import java.util.ArrayList;
import java.util.List;

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder>  {

    private List<ContactVO> contactVOList;
    private ArrayList<Msg> payeeDataSet;
    private Context context;
    private RecyclerView recyclerView;
    private UhuruDataSource db;
    private String signature;
    private String friend_identity=" ";

    public AllContactsAdapter(ArrayList<Msg> items, Context context, UhuruDataSource db,String signature){
        this.payeeDataSet = items;
        this.context = context;
        this.db = db;
        this.signature = signature;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.visa_favorite_friends, null);//mvisa_sdk_payee_list_item
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    /*****End of permission request ****/
    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
//        final ContactVO contactVO = contactVOList.get(position);

        holder.tvContactName.setText(payeeDataSet.get(position).getName());
        holder.tvPhoneNumber.setText(payeeDataSet.get(position).getPhone_number().trim());
        try {
            friend_identity = payeeDataSet.get(position).getRef_no();
            Log.v("IDENTITY", friend_identity);
        }catch (NullPointerException e)
        {
            friend_identity = "0";
            Log.v("IDENTITY", friend_identity);
            e.printStackTrace();
        }
        if(signature.equalsIgnoreCase("2")) {
            try {
                //Taken care
                holder.update_account.setVisibility(View.GONE);
                holder.delete_account.setVisibility(View.GONE);

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PayFriend.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("phone", holder.tvPhoneNumber.getText().toString().replace(" ", ""));
                        intent.putExtra("person_name", holder.tvContactName.getText().toString());
                        intent.putExtra("identity",friend_identity);
                        intent.putExtra("status",false);
                        context.startActivity(intent);
                    }
                });
            }catch(NullPointerException e)
            {
                e.printStackTrace();
            }

        }
        else if(signature.equalsIgnoreCase("1"))
        {
            holder.update_account.setVisibility(View.VISIBLE);
            holder.update_account.setText(R.string.update);
            holder.update_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,AddNewAlias.class);
                    intent.putExtra("phone",holder.tvPhoneNumber.getText().toString().replace(" ",""));
                    intent.putExtra("person_name",holder.tvContactName.getText().toString());
                    intent.putExtra("status",false);
                    intent.putExtra("identity",friend_identity);
                    intent.putExtra("update_account","UPDATE");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);
                }
            });
            holder.delete_account.setVisibility(View.VISIBLE);
            holder.delete_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.confirm_deletion)
                            .setMessage("Friend will no longer exist in the favourite list")
                            .setIcon(R.drawable.ic_delete_black_24dp)
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }

                            })
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.deleteFriendAlias(payeeDataSet.get(position).getPhone_number());

                                    Toast.makeText(context, "Friend Deleted Successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context,AllContacts.class);

                                    context.startActivity(intent);

                                }

                            }).show();

                }
            });
        }

        holder.select_payee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PayFriend.class);
                intent.putExtra("phone",holder.tvPhoneNumber.getText().toString().trim());
                intent.putExtra("person_name",holder.tvContactName.getText().toString());
                intent.putExtra("status",false);
                context.startActivity(intent);
            }
        });



    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        TextView delete_account, update_account;
        RelativeLayout payeeInitial;
        RelativeLayout cardView;
        TextView initials;
        ImageView select_payee;


        public ContactViewHolder(View itemView) {
            super(itemView);
//            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.payeeName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.payeeLastFour);
            delete_account = (TextView) itemView.findViewById(R.id.delete_account);
            update_account = (TextView) itemView.findViewById(R.id.update_account);
            payeeInitial = (RelativeLayout) itemView.findViewById(R.id.initialsView);
            payeeInitial.setVisibility(View.GONE);
            cardView = (RelativeLayout) itemView.findViewById(R.id.card_view);
            select_payee = (ImageView) itemView.findViewById(R.id.select_payee);

        }
    }

    @Override
    public int getItemCount() {
        return payeeDataSet.size();
//        if(payeeDataSet == null){return 0;}
//
//        if(!payeeDataSet.isEmpty()&&payeeDataSet!=null) {
//            return payeeDataSet.size();
//        } else{
//
//            return 0;
//        }
    }




}
