package com.goandroytech.www.rahisipay.apicalls;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class API {

    public static String BASE_URL ="https://sandbox.rahisi.co.tz/api/customer/";
    public static String VISA_URL ="https://bcx.ercis.co.tz/api/transactions/processUSSDRequest.php";

    //service_ID

    public static String LOGIN_SERVICE_ID ="1";
    public static String ACCOUNT_SERVICE_ID ="3";
    public static String TRANSACTION_SERVICE_ID ="4";
    public static String TRANSACTION_FILTER_SERVICE_ID ="5";
    public static String LINK_SERVICE_ID ="6";
    public static String UNLINK_SERVICE_ID ="7";
    public static String PAY_LINK_SERVICE_ID ="8";
    public static String PAY_UNLINK_SERVICE_ID ="8";
    public static String PAY_OTHER_SERVICE_ID ="8";
    public static String BLOCK_CARD_SERVICE_ID ="10";
    public static String UNBLOCK_CARD_SERVICE_ID ="11";
    public static String CHANGE_PIN_SERVICE_ID ="12";
    public static String LAST_TRANSACTION_SERVICE_ID ="13";


    public static double VAT =0.18;




    public static String VALUE_LINKED_PAYMENT ="1";
    public static String VALUE_UNLINKED_PAYMENT ="4";
    public static String VALUE_OTHER_PAYMENT ="4";


    //variables

    public static String PHONE = "phone";
    public static String PIN = "pin";
    public static String NEW_PIN = "new_pin";
    public static String SERVICE_ACCOUNT = "service_account";
    public static String LINK_SERVICE = "link_service_id";
    public static String SERVICE_ID = "service_id";
    public static String PAYMENT_SERVICE_ID = "payment_service_id";
    public static String AMOUNT = "amount";
    public static String LINKED_PAYMENT = "linked_payment";
    public static String SERVICE_LAST_TRANSACTION_SERVICE_ID = "transaction_service_id";

    public static String formatCurrency(String balance) {
        //set locale to tanzania for currency format
        if (balance.equals(null) || balance.equals("")) {
            return balance;
        } else {
            Locale mozambique = new Locale("pt_MZ", "MZ");
            DecimalFormat format = new DecimalFormat("#,##0.00 TZS;-#,##0.00 TZS");
            //NumberFormat format = NumberFormat.getCurrencyInstance(tanzania);
            BigDecimal bBalance = new BigDecimal(balance.toString());
            String formattedBalance = format.format(bBalance);
            return formattedBalance;
        }
    }

    public static String DateFormat(String date) {
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd-MM-yyyy hh:mm");
        date = spf.format(newDate);
            return date;
        }

}
