package com.goandroytech.www.rahisipay.Connection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.goandroytech.www.rahisipay.Card;
import com.goandroytech.www.rahisipay.Home;
import com.goandroytech.www.rahisipay.MyAccount;
import com.goandroytech.www.rahisipay.R;

/**
 * Created by AGM TAZIM on 12/31/2015.
 */
public class ConnectivityDetector {
    Context ctx;

    //Contsructor
    public ConnectivityDetector(Context ctx){
        this.ctx = ctx;
    }

    //Check internet status
    public boolean checkConnectivityStatus(){
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        //boolean wifi=connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        //boolean internet=connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        NetworkInfo internetInfo = connectivity.getActiveNetworkInfo();

        if ( internetInfo != null) {
            return true;
        }
        return false;
    }

    //Show alert
    public void showAlertDialog(Context context, String title, String message) {
        ctx=context;
        LayoutInflater factory = LayoutInflater.from(ctx);
        View textEntryView = factory.inflate(R.layout.activity_error, null);
        final TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
        final TextView tv_success = (TextView)textEntryView.findViewById(R.id.tv_success);
        ImageView img =(ImageView)textEntryView.findViewById(R.id.img);
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
    //    AlertDialog alertDialog = new AlertDialog.Builder(context).setView(textEntryView).create();
        final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);

        if (title.equals("Warning")){
            img.setBackgroundResource(R.drawable.warning);
            tv_success.setText("Warning");
        }
        tv_message.setText(message);
        final AlertDialog alertDialog = alert.setView(textEntryView).create();

        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 6000);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // Setting Dialog Title
      //  alertDialog.setTitle(title);

        // Setting Dialog Message
       // alertDialog.setMessage(message);

        // Setting OK Button
     /*   alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }); */

        // Showing Alert Message
        alertDialog.show();
    }

    public void openSuccessDialog(Context context, String title, String message) {
        ctx=context;
        LayoutInflater factory = LayoutInflater.from(ctx);
        View textEntryView = factory.inflate(R.layout.activity_success, null);
        final TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        //    AlertDialog alertDialog = new AlertDialog.Builder(context).setView(textEntryView).create();
        final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);

        tv_message.setText(message);
        final AlertDialog alertDialog = alert.setView(textEntryView).create();

        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 6000);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }


    private void openErrorDialog(String get_description) {
        LayoutInflater factory = LayoutInflater.from(ctx);
        View textEntryView = factory.inflate(R.layout.activity_error, null);
        final TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        tv_message.setText(get_description);
        alert.setCancelable(true);

        final AlertDialog alertDialog = alert.setView(textEntryView).create();
        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 10000);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void openSuccessDialog(String get_description) {
        LayoutInflater factory = LayoutInflater.from(ctx);
        View textEntryView = factory.inflate(R.layout.activity_success, null);
        final TextView tv_message = (TextView)textEntryView.findViewById(R.id.tv_message);
        Button btn_ok = (Button)textEntryView.findViewById(R.id.btn_ok);
        final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        tv_message.setText(get_description);
        alert.setCancelable(true);

        final AlertDialog alertDialog = alert.setView(textEntryView).create();
// Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 10000);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
