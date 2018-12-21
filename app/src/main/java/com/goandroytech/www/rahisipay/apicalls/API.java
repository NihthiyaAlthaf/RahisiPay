package com.goandroytech.www.rahisipay.apicalls;

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


    //variables

    public static String PHONE = "phone";
    public static String PIN = "pin";
    public static String SERVICE_ACCOUNT = "service_account";
    public static String LINK_SERVICE = "link_service_id";
    public static String SERVICE_ID = "service_id";
    public static String PAYMENT_SERVICE_ID = "payment_service_id";
    public static String AMOUNT = "amount";
    public static String LINKED_PAYMENT = "linked_payment";
}
