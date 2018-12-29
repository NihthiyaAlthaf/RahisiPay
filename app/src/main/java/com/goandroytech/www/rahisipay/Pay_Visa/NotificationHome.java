package com.goandroytech.www.rahisipay.Pay_Visa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.goandroytech.www.rahisipay.Login;
import com.goandroytech.www.rahisipay.MyAccount;
import com.goandroytech.www.rahisipay.R;

public class NotificationHome extends AppCompatActivity {
    TextView notification;
    private String newString,fromlogin, finalString;
    private SharedPreferences pref;
    private String login,identifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_notification_home);

            pref = getApplicationContext().getSharedPreferences("UhuruPayPref", 0);
            fromlogin = pref.getString("FromLogin", "1");
            identifier = pref.getString("identifier",null);


            notification = (TextView) findViewById(R.id.textView2);

            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    newString = null;
                    login =null;
                    finalString=null;
                } else {
                    try {
                        newString = extras.getString("STRING_I_NEED");
                        login =  extras.getString("FromLogin");
                        finalString = extras.getString("FinalResponse");
                        notification.setText(newString);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                }
            } else {
                newString = (String) savedInstanceState.getSerializable("STRING_I_NEED");
                login = (String) savedInstanceState.getSerializable("FromLogin");
            }

            ImageView imageView = (ImageView) findViewById(R.id.notification_home);
            Log.v("VALUE_REGISTERED",newString);
            if(newString.equalsIgnoreCase(getString(R.string.t_d)))
            {
                Log.v("VALUE_DECLINED",newString+1);
                imageView.setBackgroundResource(R.drawable.declined);//setting_logo

                try{

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (fromlogin.equalsIgnoreCase("1") && login!=null) {
                                    Log.v("PASS1","1");
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("FromLogin", "0");
                                    editor.commit();
                                    final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                                else if(newString.equalsIgnoreCase("MOBILE NUMBER LINKED")){
                                    if(identifier.equalsIgnoreCase("0")) {
                                        Log.v("PASS1", "2");
                                        final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }else{
                                        Log.v("PASS1", "2.5");

                                        final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }
                                }
                                else {
                                    Log.v("PASS1","3");
                                    final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }catch (NullPointerException e)
                            {
                                if(identifier.equalsIgnoreCase("0")) {
                                    Log.v("PASS1", "4");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }else{
                                    Log.v("PASS1", "4.5");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }
                        }
                    }, 1000);
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
            }

            else if(newString.equalsIgnoreCase(getString(R.string.p_d)))
            {
                Log.v("VALUE_DECLINED",newString+1);
                imageView.setBackgroundResource(R.drawable.declined);//setting_logo
                try{

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (fromlogin.equalsIgnoreCase("1") && login!=null) {
                                    Log.v("PASS1","1");
                                   /* SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("FromLogin", "0");
                                    editor.commit();*/
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                                if (fromlogin.equalsIgnoreCase("0") && login!=null) {
                                    Log.v("PASS1","1");
                                   /* SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("FromLogin", "0");
                                    editor.commit();*/
                                    final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                                else if(newString.equalsIgnoreCase("MOBILE NUMBER LINKED")){
                                    if(identifier.equalsIgnoreCase("0")) {
                                        Log.v("PASS1", "2");
                                        final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }else{
                                        Log.v("PASS1", "2.5");

                                        final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }
                                }
                                else {
                                    Log.v("PASS1","3");
                                    final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }catch (NullPointerException e)
                            {
                                if(identifier.equalsIgnoreCase("0")) {
                                    Log.v("PASS1", "4");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }else{
                                    Log.v("PASS1", "4.5");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, MyAccount.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }
                        }
                    }, 1000);
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
            }

            else if(newString.equalsIgnoreCase(getString(R.string.c_w_d)))
            {
                Log.v("VALUE_DECLINED",newString+1);
                imageView.setBackgroundResource(R.drawable.declined);//setting_logo
            try{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (fromlogin.equalsIgnoreCase("1") && login!=null) {
                                Log.v("PASS1","1");
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("FromLogin", "0");
                                editor.commit();
                                final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                NotificationHome.this.startActivity(mainIntent);
                                NotificationHome.this.finish();
                            }
                            else if(newString.equalsIgnoreCase("MOBILE NUMBER LINKED")){
                                if(identifier.equalsIgnoreCase("0")) {
                                    Log.v("PASS1", "2");
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }else{
                                    Log.v("PASS1", "2.5");

                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }
                            else {
                                Log.v("PASS1","3");
                                final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                NotificationHome.this.startActivity(mainIntent);
                                NotificationHome.this.finish();
                            }
                        }catch (NullPointerException e)
                        {
                            if(identifier.equalsIgnoreCase("0")) {
                                Log.v("PASS1", "4");
                                e.printStackTrace();
                                final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                NotificationHome.this.startActivity(mainIntent);
                                NotificationHome.this.finish();
                            }else{
                                Log.v("PASS1", "4.5");
                                e.printStackTrace();
                                final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                NotificationHome.this.startActivity(mainIntent);
                                NotificationHome.this.finish();
                            }
                        }
                    }
                }, 1000);
            }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
            }
            else if(newString.equalsIgnoreCase(getString(R.string.reason)))
            {
                Log.v("VALUE_DECLINED",newString+1);
                imageView.setBackgroundResource(R.drawable.declined);//setting_logo

                try{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (fromlogin.equalsIgnoreCase("1") && login!=null) {
                                    Log.v("PASS1","1");
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("FromLogin", "0");
                                    editor.commit();
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                                else if(newString.equalsIgnoreCase("MOBILE NUMBER LINKED")){
                                    if(identifier.equalsIgnoreCase("0")) {
                                        Log.v("PASS1", "2");
                                        final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }else{
                                        Log.v("PASS1", "2.5");

                                        final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }
                                }
                                else {
                                    Log.v("PASS1","3");
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }catch (NullPointerException e)
                            {
                                if(identifier.equalsIgnoreCase("0")) {
                                    Log.v("PASS1", "4");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }else{
                                    Log.v("PASS1", "4.5");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }
                        }
                    }, 1000);
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
            }
            else if(newString.equalsIgnoreCase(getString(R.string.reason_merchant)))
            {
                Log.v("VALUE_DECLINED",newString+1);
                imageView.setBackgroundResource(R.drawable.declined);//setting_logo

                try{

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (fromlogin.equalsIgnoreCase("1") && login!=null) {
                                    Log.v("PASS1","1");
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("FromLogin", "0");
                                    editor.commit();
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                                else if(newString.equalsIgnoreCase("MOBILE NUMBER LINKED")){
                                    if(identifier.equalsIgnoreCase("0")) {
                                        Log.v("PASS1", "2");
                                        final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }else{
                                        Log.v("PASS1", "2.5");

                                        final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }
                                }
                                else {
                                    Log.v("PASS1","3");
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }catch (NullPointerException e)
                            {
                                if(identifier.equalsIgnoreCase("0")) {
                                    Log.v("PASS1", "4");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }else{
                                    Log.v("PASS1", "4.5");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }
                        }
                    }, 1000);
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
            }

            else{
                Log.v("VALUE_REGISTERED",newString+ 2);
                imageView.setBackgroundResource(R.drawable.rahisi_img);

                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (fromlogin.equalsIgnoreCase("1") && login!=null) {
                                    Log.v("PASS1","1");
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("FromLogin", "0");
                                    editor.commit();
                                    final Intent mainIntent = new Intent(NotificationHome.this, DigitalReceipt.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mainIntent.putExtra("FinalResponse",finalString);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                                else if(newString.equalsIgnoreCase("MOBILE NUMBER LINKED")){
                                    if(identifier.equalsIgnoreCase("0")) {
                                        Log.v("PASS1", "2");
                                        final Intent mainIntent = new Intent(NotificationHome.this, DigitalReceipt.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mainIntent.putExtra("FinalResponse",finalString);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }else{
                                        Log.v("PASS1", "2.5");

                                        final Intent mainIntent = new Intent(NotificationHome.this, DigitalReceipt.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mainIntent.putExtra("FinalResponse",finalString);
                                        NotificationHome.this.startActivity(mainIntent);
                                        NotificationHome.this.finish();
                                    }
                                }
                                else {
                                    Log.v("PASS1","3");
                                    final Intent mainIntent = new Intent(NotificationHome.this, DigitalReceipt.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mainIntent.putExtra("FinalResponse",finalString);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }catch (NullPointerException e)
                            {
                                if(identifier.equalsIgnoreCase("0")) {
                                    Log.v("PASS1", "4");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, DigitalReceipt.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mainIntent.putExtra("FinalResponse",finalString);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }else{
                                    Log.v("PASS1", "4.5");
                                    e.printStackTrace();
                                    final Intent mainIntent = new Intent(NotificationHome.this, DigitalReceipt.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mainIntent.putExtra("FinalResponse",finalString);
                                    NotificationHome.this.startActivity(mainIntent);
                                    NotificationHome.this.finish();
                                }
                            }
                        }
                    }, 1000);
                }catch (NullPointerException e)
                {
                  try{

                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  if (fromlogin.equalsIgnoreCase("1") && login!=null) {
                                      Log.v("PASS1","1");
                                      SharedPreferences.Editor editor = pref.edit();
                                      editor.putString("FromLogin", "0");
                                      editor.commit();
                                      final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                      mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                      mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                      NotificationHome.this.startActivity(mainIntent);
                                      NotificationHome.this.finish();
                                  }
                                  else if(newString.equalsIgnoreCase("MOBILE NUMBER LINKED")){
                                      if(identifier.equalsIgnoreCase("0")) {
                                          Log.v("PASS1", "2");
                                          final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                          mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                          mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                          NotificationHome.this.startActivity(mainIntent);
                                          NotificationHome.this.finish();
                                      }else{
                                          Log.v("PASS1", "2.5");

                                          final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                          mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                          mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                          NotificationHome.this.startActivity(mainIntent);
                                          NotificationHome.this.finish();
                                      }
                                  }
                                  else {
                                      Log.v("PASS1","3");
                                      final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                      mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                      mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                      NotificationHome.this.startActivity(mainIntent);
                                      NotificationHome.this.finish();
                                  }
                              }catch (NullPointerException e)
                              {
                                  if(identifier.equalsIgnoreCase("0")) {
                                      Log.v("PASS1", "4");
                                      e.printStackTrace();
                                      final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                      mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                      mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                      NotificationHome.this.startActivity(mainIntent);
                                      NotificationHome.this.finish();
                                  }else{
                                      Log.v("PASS1", "4.5");
                                      e.printStackTrace();
                                      final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                      mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                      mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                      NotificationHome.this.startActivity(mainIntent);
                                      NotificationHome.this.finish();
                                  }
                              }
                          }
                      }, 1000);
                  }catch (NullPointerException e1)
                  {
                      e1.printStackTrace();
                  }
                }
            }



           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (fromlogin.equalsIgnoreCase("1") && login!=null) {
                            Log.v("PASS1","1");
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("FromLogin", "0");
                            editor.commit();
                            final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            NotificationHome.this.startActivity(mainIntent);
                            NotificationHome.this.finish();
                        }
                        else if(newString.equalsIgnoreCase("MOBILE NUMBER LINKED")){
                            if(identifier.equalsIgnoreCase("0")) {
                                Log.v("PASS1", "2");
                                final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                NotificationHome.this.startActivity(mainIntent);
                                NotificationHome.this.finish();
                            }else{
                                Log.v("PASS1", "2.5");

                                final Intent mainIntent = new Intent(NotificationHome.this, MainAgencyBankingTwo.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                NotificationHome.this.startActivity(mainIntent);
                                NotificationHome.this.finish();
                            }
                        }
                        else {
                            Log.v("PASS1","3");
                            final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            NotificationHome.this.startActivity(mainIntent);
                            NotificationHome.this.finish();
                        }
                    }catch (NullPointerException e)
                    {
                        if(identifier.equalsIgnoreCase("0")) {
                            Log.v("PASS1", "4");
                            e.printStackTrace();
                            final Intent mainIntent = new Intent(NotificationHome.this, Login.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            NotificationHome.this.startActivity(mainIntent);
                            NotificationHome.this.finish();
                        }else{
                            Log.v("PASS1", "4.5");
                            e.printStackTrace();
                            final Intent mainIntent = new Intent(NotificationHome.this, MainAgencyBankingTwo.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            NotificationHome.this.startActivity(mainIntent);
                            NotificationHome.this.finish();
                        }
                    }
                }
            }, 1000);
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }*/

    }



}
