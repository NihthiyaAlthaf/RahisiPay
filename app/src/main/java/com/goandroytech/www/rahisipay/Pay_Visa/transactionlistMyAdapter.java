package com.goandroytech.www.rahisipay.Pay_Visa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import com.goandroytech.www.rahisipay.Model.Msg;
import com.goandroytech.www.rahisipay.R;
import com.visa.mvisa.sdk.models.facade.Payee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by michael.nkotagu on 6/18/2015.
 */

public class transactionlistMyAdapter extends RecyclerView.Adapter<transactionlistMyAdapter.MyViewHolder> {

    private ArrayList<Msg> payeeDataSet;
    private Context context;
    private UhuruDataSource db;
    private String code;
    private SharedPreferences pref;
    private String datetime;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView origination, transaction_time, amount;
        private CardView receipt;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.origination = (TextView) itemView.findViewById(R.id.origination);
            this.transaction_time = (TextView) itemView.findViewById(R.id.transaction_time);
            this.amount = (TextView) itemView.findViewById(R.id.amount);
            this.receipt = (CardView) itemView.findViewById(R.id.payeeListCard);
        }
    }

    public transactionlistMyAdapter(ArrayList<Msg> items, Context context, UhuruDataSource db) {
        this.payeeDataSet = items;
        this.context = context;
        this.db = db;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list, parent, false);

       // view.setOnClickListener(MainUtilities.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    private String amount_value, agent_name, transaction_time_value, agent_pan, location , approval_code, ret_no, sender_account;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {



        TextView origination = holder.origination;
        final TextView amount = holder.amount;
        TextView transaction_time = holder.transaction_time;
        CardView receipt_details = holder.receipt;
        origination.setText(payeeDataSet.get(listPosition).getAgent_name());
        amount.setText("TZS " +Action.getFormatedAmountVisa(payeeDataSet.get(listPosition).getAmount().substring(3))+" Dr");
        transaction_time.setText(datetime(payeeDataSet.get(listPosition).getTransaction_time()));//
        code = payeeDataSet.get(listPosition).getApproval_code();
        datetime = payeeDataSet.get(listPosition).getBin();
        Log.v("CODE",payeeDataSet.get(listPosition).getAmount());
        final JSONArray data = db.getReceiptData(code,datetime);

        receipt_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {

                     pref = context.getSharedPreferences("UhuruPayPref", 0);
                     SharedPreferences.Editor editorOne = pref.edit();
                     editorOne.putString("ReceiptData",String.valueOf(data.getJSONObject(0)));
                     editorOne.commit();
                     Intent intent = new Intent(context, DigitalReceiptTwo.class);
                     context.startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v("CODE-DATA",""+data);

                //Dailog
                /*new AlertDialog.Builder(context)
                        .setTitle(R.string.title_activity_digital_receipt)
                        .setMessage(
                           "AMOUNT :"+amount_value+"\n"+
                           "AGENT NAME :"+agent_name+"\n"+
                           "TRANSACTION TIME :"+transaction_time_value+"\n"+
                           "AGENT ACCOUNT No. :Visa..."+agent_pan+"\n"+
                           "LOCATION :"+location+"\n"+
                           "APPROVAL CODE :"+approval_code+"\n"+
                           "RETRIEVAL REFERENCE NUMBER :"+ret_no+"\n"+
                           "SENDER ACCOUNT No. :Umoja..."+sender_account+"\n"
                        )
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }

                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }

                        }).show();*/

            }
        });


    }

    private static String tarehe_iliyopo;
    public static String datetime(String tarehe)
    {

        String string = tarehe.replace("T","");


        String originalStringFormat = "yyyy-MM-ddHH:mm:ss";
        String desiredStringFormat = "yyyy-MM-dd HH:mm:ss a";

        SimpleDateFormat readingFormat = new SimpleDateFormat(originalStringFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(desiredStringFormat);

        try {
            Date date = readingFormat.parse(string);
            tarehe_iliyopo = outputFormat.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }

        return tarehe_iliyopo;
    }

    @Override
    public int getItemCount() {
        return payeeDataSet.size();
    }
}
