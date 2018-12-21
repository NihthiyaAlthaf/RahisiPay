package com.goandroytech.www.rahisipay.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.goandroytech.www.rahisipay.Model.Msg;
import com.goandroytech.www.rahisipay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

/**
 * @author james.buretta
 */
public class Action {
    private static String ENDPOINT="https://bcx.ercis.co.tz/api/transactions/"; //URL To Post Request
    private static String API_ENDPOINT="https://bcx.ercis.co.tz/api/transactions/";
    private static String downloadURL="https://bcx.ercis.co.tz/app";
    private static Action sInstance=null; //Instance used to access request
    private RequestQueue mRequestQueue;
    private static  Context mCtx;



    private Action(Context context){
        mCtx=context;
        mRequestQueue=getRequestQueue();
        sInstance=this;
    }

    public static synchronized Action getInstance(Context context){
        if(sInstance==null)
        {
            sInstance=new Action(context);
        }

        return sInstance;
    }



    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static String guid(String pan, String mob_nr){

        String in=pan+mob_nr;

        String out;

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");

            md.update(in.getBytes());

            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }


            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            out=hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return out;

    }

    public static String stan()
    {
        Random random = new Random();
        String generatedStan = String.format("%04d", random.nextInt(10000));
        return generatedStan;
    }

    public static String getRrn(String stan){//Stan you can use any 6digits value.
        String rrn="";

        rrn=getJulianDate()+stan;


        return rrn;
    }

    public static synchronized  int resolveRspCode(String rspCode){
        int messageCode=0;

        switch (rspCode)
        {
            case "7101":messageCode=R.string.rsp7101;break;
            case "7201":messageCode=R.string.rsp7201;break;
            case "7202":messageCode=R.string.rsp7202;break;
            case "7203":messageCode=R.string.rsp7203;break;
            case "7204":messageCode=R.string.rsp7204;break;
            case "7205":messageCode=R.string.rsp7205;break;
            case "7206":messageCode=R.string.rsp7206;break;
            case "7207":messageCode=R.string.rsp7207;break;
            case "7208":messageCode=R.string.rsp7208;break;
            case "7209":messageCode=R.string.rsp7209;break;
            case "7210":messageCode=R.string.rsp7210;break;
            case "7211":messageCode=R.string.rsp7211;break;
            case "7212":messageCode=R.string.rsp7212;break;
            case "7213":messageCode=R.string.rsp7213;break;
            case "7214":messageCode=R.string.rsp7214;break;
            case "7215":messageCode=R.string.rsp7215;break;
            case "7216":messageCode=R.string.rsp7216;break;
            case "7217":messageCode=R.string.rsp7217;break;
            case "7219":messageCode=R.string.rsp7219;break;
            case "7234":messageCode=R.string.rsp7234;break;
            case "7281":messageCode=R.string.rsp7281;break;
            case "7242":messageCode=R.string.rsp7242;break;
            case "01":messageCode=R.string.rsp01;break;
            default:messageCode=R.string.rspUnkown;break;

        }


        return messageCode;
    }

    public static   String getJulianDate(){

        String julianDate="";

        Date dt = new Date();

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(dt);

        int curr_year = cal.get(Calendar.YEAR);
        String curr_day_of_yr=String.format("%03d", cal.get(Calendar.DAY_OF_YEAR));
        String curr_hr_of_day=String.format("%02d",cal.get(Calendar.HOUR_OF_DAY));

        //Getting last digit of the year.
        int last_digit=curr_year%10;

        //Getting a number of day of the year
        julianDate=last_digit+curr_day_of_yr+curr_hr_of_day;


        return julianDate;

    }

    public static String getFormatedAmount(String amount){

        return NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount));
    }

    /*Dialog clickers*/
    public static DialogInterface.OnClickListener exitApplication() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        };
    }

    public static DialogInterface.OnClickListener minimiseApplication(){
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
    }

    public static String getFormatedAmountVisa(String amount){

        return NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(amount));
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static synchronized JSONObject getJSONObject(Msg msg)  {
        JSONObject jsonObject = new JSONObject();
         try {
                 jsonObject.accumulate("MTI",       msg.get_MTI());
                 jsonObject.accumulate("field_3",   msg.get_processing_code());
                 jsonObject.accumulate("field_4",   msg.get_amount());
                 jsonObject.accumulate("field_7",   msg.get_transmission_datetime());
                 jsonObject.accumulate("field_11",  msg.get_stan());
                 jsonObject.accumulate("field_12",  msg.get_localtime());
                 jsonObject.accumulate("field_37",  msg.get_field_37());
                 jsonObject.accumulate("field_39",  msg.get_rsp_code());
                 jsonObject.accumulate("field_41",  msg.get_terminal_id());
                 jsonObject.accumulate("field_42",  msg.get_business_name());
                 jsonObject.accumulate("field_48",  msg.get_additional_data());
                 jsonObject.accumulate("field_52",  msg.get_PIN_data());
                 jsonObject.accumulate("field_58",  msg.get_provider_name());
                 jsonObject.accumulate("field_61",  msg.get_extended_tran_type());
                 jsonObject.accumulate("field_62",  msg.get_field_62());
                 jsonObject.accumulate("field_67",  msg.get_extended_payment_code());
                 jsonObject.accumulate("field_68",  msg.get_field_68());
                 jsonObject.accumulate("field_69",  msg.get_field_69());
                 jsonObject.accumulate("field_100", msg.get_receiving_inst());
                 jsonObject.accumulate("field_102", msg.get_from_acc());
                 jsonObject.accumulate("field_103", msg.get_to_acc());
                 jsonObject.accumulate("field_128", msg.get_field_128());
                 jsonObject.accumulate("field_nfc", msg.getNfcTagId());
                 jsonObject.accumulate("field_bus_id", msg.getField_bus_id());
                 jsonObject.accumulate("amount",msg.getAmount());
                 jsonObject.accumulate("account",msg.getAccount_number());
                 jsonObject.accumulate("password",msg.getPassword());
                 jsonObject.accumulate("bin",msg.getBIN());
                 jsonObject.accumulate("token",msg.getToken());
                 jsonObject.accumulate("f_name",msg.getFname());
                 jsonObject.accumulate("l_name",msg.getLname());
                 jsonObject.accumulate("m_name",msg.getMname());
                 jsonObject.accumulate("pan",msg.getPan());
                 jsonObject.accumulate("bank",msg.getIssuer());
                 jsonObject.accumulate("guid",msg.getGuid());
                 jsonObject.accumulate("newGuid",msg.getNew_guid());
                 jsonObject.accumulate("phone_number",msg.getPhone_number());
                 jsonObject.accumulate("date_time",msg.getConsentDatetime());
                 jsonObject.accumulate("app_type","1");
                 jsonObject.accumulate("ref_no",msg.getRef_no());
                 jsonObject.accumulate("utility_type",msg.getUtility_type());
                 jsonObject.accumulate("receiver_uid",msg.getRecipient_uid());
                 jsonObject.accumulate("recipient_account",msg.getRecipientAccount());
                 jsonObject.accumulate("agent_id",msg.getAgent_id());
                 jsonObject.accumulate("merchant_id",msg.getMerchant_id());
                 jsonObject.accumulate("customer_id",msg.getCustomer_id());
                 jsonObject.accumulate("dob",msg.getBirth_date());
                 jsonObject.accumulate("id_type",msg.getIdentity_type());
                 jsonObject.accumulate("image",msg.getImage());
                 jsonObject.accumulate("identity_image",msg.getIdentity_image());
                 jsonObject.accumulate("physical_address",msg.getPhysical_address());
                 jsonObject.accumulate("identity_number",msg.getIdentity_number());
                 jsonObject.accumulate("gender",msg.getGender());
                 jsonObject.accumulate("type",msg.getType());


         }
         catch (JSONException e) {
             jsonObject=null;
         }

        return jsonObject;
    }


    public static class InputFilterMinMax implements InputFilter {
        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }









    public static synchronized JSONObject postDeposit(Msg msg)  {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("MTI",msg.get_MTI());
            jsonObject.accumulate("amount",msg.getAmount());
            jsonObject.accumulate("account",msg.getAccount_number());
            jsonObject.accumulate("bin", msg.getBIN());
            jsonObject.accumulate("receiver_uid",msg.getRecipient_uid());
            jsonObject.accumulate("recipient_account",msg.getRecipientAccount());
            jsonObject.accumulate("app_type","1");
            jsonObject.accumulate("customer_id",msg.getCustomer_id());
        }
        catch (JSONException e) {
            jsonObject=null;
        }

        return jsonObject;
    }

    public static synchronized JSONObject postRegistration(Msg msg)  {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("MTI",msg.get_MTI());
            jsonObject.accumulate("p_number",msg.getP_number());
            jsonObject.accumulate("pin",msg.get_PIN_data());
            jsonObject.accumulate("field_68",msg.get_field_68());
            jsonObject.accumulate("field_69",msg.get_field_69());
            jsonObject.accumulate("bin",msg.getBIN());
            jsonObject.accumulate("pin1",msg.getPin1());
            jsonObject.accumulate("pin2",msg.getPin2());
        }
        catch (JSONException e) {
            jsonObject=null;
        }

        return jsonObject;
    }





    //Deposit
    public static synchronized  JSONObject getCashDeposit(Msg msg){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.accumulate("MTI", msg.get_MTI());
            jsonObject.accumulate("account", msg.getAccount_number());
            jsonObject.accumulate("amount",msg.getAmount());
            jsonObject.accumulate("trans_type",msg.get_extended_tran_type());

        }catch (JSONException e){
            e.printStackTrace();
        }

        return  jsonObject;
    }


    //Cash withdrawal
    public static synchronized  JSONObject getCashWithdraw(Msg msg){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.accumulate("MTI", msg.get_MTI());
            jsonObject.accumulate("reference_no", msg.getUtility_control());
            jsonObject.accumulate("pin",msg.getPassword());
            jsonObject.accumulate("amount",msg.getAmount());
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  jsonObject;
    }


    //Balance Inquiry
    public static synchronized  JSONObject getInquiy(Msg msg){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.accumulate("MTI", msg.get_MTI());
            jsonObject.accumulate("account",msg.getInquiry());
            jsonObject.accumulate("bin",msg.getBIN());
            jsonObject.accumulate("msisdn",msg.getPhone_number());
            jsonObject.accumulate("TYPE",msg.get_extended_tran_type());
            jsonObject.accumulate("app_type","1");
            jsonObject.accumulate("customer_id",msg.getCustomer_id());
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  jsonObject;
    }

    public static synchronized  JSONObject getPayment(Msg msg){
        JSONObject jsonObject = new JSONObject();
        try{

            jsonObject.accumulate("MTI", msg.get_MTI());
            jsonObject.accumulate("spCode", msg.getSpCode());
            jsonObject.accumulate("PayRefId", msg.getPayRefId());
            jsonObject.accumulate("ResultUrl", msg.getResultUrl());
            jsonObject.accumulate("BillCtrNum", msg.getBillCtrNum());
            jsonObject.accumulate("PaidAmt", msg.getPaidAmt());
            jsonObject.accumulate("CCy", msg.getCCy());
            jsonObject.accumulate("DptCellNum", msg.getDptCellNum());
            jsonObject.accumulate("DptName", msg.getDptName());
            jsonObject.accumulate("PyrEmailAddr", msg.getPyrEmailAddr());

        }catch (JSONException e){
            e.printStackTrace();
        }

        return  jsonObject;
    }



    public static synchronized  JSONObject getJSONObject (JSONArray msg,int counter){
        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject=msg.getJSONObject(counter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObject;

    }

    public static synchronized JSONArray getJSONArray(JSONObject msg){

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(msg);

        return jsonArray;
    }

    public static synchronized JSONArray getJSONArray2(JSONObject msg){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(msg);

        return jsonArray;
    }

    public static void DefaultLanguage(Context context, String lang){
        String languageToLoad  = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public static synchronized String parseResponse(JSONObject response){
        String parsedRsp="";
        try {
               String processingCode=response.getString("field_3");
               String extendedTranType=response.getString("field_61");
               String responseCode=response.getString("field_39");
               String MTI=response.getString("MTI");

               if (processingCode.length() > 2) {
                   String tranType=processingCode.substring(0,2);
                   if(MTI.equals("0210")&& tranType.equals("31") && extendedTranType.equals("9006") && responseCode.equals("00")){
                      parsedRsp="Description: " + response.getString("field_48") + "\n\n" +
                                "Quantity: " + response.getString("field_67") + "\n\n"+
                                "Amount: " + response.getString("field_4") + "\n\n";
                   }
                   else
                   {
                        parsedRsp="Error code: "+response.getString("field_39");
                   }

               }
        }
        catch(JSONException ex){

        }
        return parsedRsp;

    }

    public static synchronized String getFieldValue(JSONObject response,String field){
        String responseCode="";
        try {

             responseCode=response.getString(field);
        }
        catch(JSONException ex){

        }
        return responseCode;

    }

    public static synchronized JSONObject getInnerJsonObject(String innerJson){

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(innerJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    public static String getFormatedAmountMinistatement(String amount){
        amount = amount.substring(0,amount.length()-3);
        return NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount));
    }






    /*
    *Formats incoming date format to desire date format
     */
    private static String tarehe_iliyopo;
    public static String datetime(String tarehe)
    {

        String string = tarehe;

        String originalStringFormat = "yyyyMMddHHmmss";
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

    public static synchronized  String parseErrorResponse(VolleyError ex){

        String message="";
        if (ex instanceof TimeoutError || ex instanceof NoConnectionError) {
            message="Connection could not be established";
        } else if (ex instanceof AuthFailureError) {
            message="HTTP Authentication Could not be done";
        } else if (ex instanceof ServerError) {
            message="Server error";
        } else if (ex instanceof NetworkError) {
            message="Network error";
        } else if (ex instanceof ParseError) {
            message="Incorrect Format Returned";//Parse error
        }else{
            message="Unknown error";
        }
        return message;
    }



    public static String getFormattedDate(String inputFormatStr,String outputFormatStr,String dateString){
        DateFormat outputFormat = new SimpleDateFormat(outputFormatStr);
        DateFormat inputFormat =  new SimpleDateFormat(inputFormatStr);
        String outputText="";

        try
        {
            Date date = inputFormat.parse(dateString);
            outputText = outputFormat.format(date);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return outputText;
    }

    public static String getEndpoint(String finalURL){
        return ENDPOINT+finalURL;
    }


    public static String getApiEndpoint(String finalURL){
        return API_ENDPOINT+finalURL;
    }


    public static String getDownloadendpoint(){
        return downloadURL;
    }

    public static synchronized  String getRightJustifiedText(String str,int perLineLength){

        return insertPeriodically(str, "\n", perLineLength);
    }

    public static String insertPeriodically(String text, String insert, int period)
    {
        StringBuilder builder = new StringBuilder(text.length() + insert.length() * (text.length()/period)+1);

        int index = 0;
        String prefix = "";
        while (index < text.length())
        {
            // Don't put the insert in the very first iteration.This is easier than appending it *after* each substring
            builder.append(prefix);
            prefix = insert;
            builder.append(padRight(text.substring(index, Math.min(index + period, text.length())),24));
            index += period;
        }
        return builder.toString();
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public static String getstringResource(Context ctx,int resourceID,String append,Boolean isUpperCase){
        String string=(ctx.getResources().getString(resourceID)+append);

        if(isUpperCase)
        {
            string=string.toUpperCase();
        }

        return string;
    }

    @SuppressLint("SimpleDateFormat")
    public static String set_transmission_datetime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
        String _datetime = sdf.format(cal.getTime()).toString();
        return _datetime;
    }

    @SuppressLint("SimpleDateFormat")
    public static String set_transmission_date() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String _datetime = sdf.format(cal.getTime()).toString();
        return _datetime;
    }

    @SuppressLint("SimpleDateFormat")
    public static String set_transaction_date(String datetime) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String _datetime = sdf.format(datetime);
        return _datetime;
    }

}


