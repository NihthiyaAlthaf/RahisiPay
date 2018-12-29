package com.goandroytech.www.rahisipay.Pay_Visa;

import android.content.Context;


import com.goandroytech.www.rahisipay.R;
import com.visa.mvisa.sdk.models.facade.Payee;

import java.util.ArrayList;

/**
 * Created by michael.nkotagu on 6/18/2015.
 */
public class menuBuilder {
    public String[] nameArray;
    public String[] descriptionArray;
    public int[] drawableArray;
    private UhuruDataSource db;


    public menuBuilder(String selection,Context context) {
        db = new UhuruDataSource(context);
        if (selection.equals("MainTransactions"))
        {
            this.nameArray = new String[2];
            this.descriptionArray = new String[2];
            this.drawableArray = new int[2];


            this.nameArray[0] = context.getString(R.string.agency_bank);
            this.nameArray[1] = context.getString(R.string.agency_banking_mvisa);


            this.descriptionArray[0] = "";
            this.descriptionArray[1] = context.getString(R.string.agency_banking_mvisa_desc);

            this.drawableArray[0] = R.drawable.azania_logo;
            this.drawableArray[1] = R.drawable.visalogo2;//mvisa

        }
        else if (selection.equals("MainMvisa"))
        {
            this.nameArray = new String[4];
            this.descriptionArray = new String[4];
            this.drawableArray = new int[4];

//            this.nameArray[0] = context.getString(R.string.mvisa_paymerchant);
            this.nameArray[0] = context.getString(R.string.mvisa_payfriend);
            this.nameArray[1] = context.getString(R.string.payee_list);
            this.nameArray[2] = context.getString(R.string.sales_summary);
            this.nameArray[3] = context.getString(R.string.settings);

//            this.descriptionArray[0] = context.getString(R.string.mvisa_paymerchant_desc);
            this.descriptionArray[0] = context.getString(R.string.mvisa_payfriend_desc);
            this.descriptionArray[1] = context.getString(R.string.mvisa_cash_withdrawal_desc);
            this.descriptionArray[2] = context.getString(R.string.merchant_qr_desc);
            this.descriptionArray[3] = context.getString(R.string.settingss);

//            this.drawableArray[0] = R.drawable.home_menu;
            this.drawableArray[0] = R.drawable.amount_trans;
            this.drawableArray[1] = R.drawable.transaction;
            this.drawableArray[2] = R.drawable.transaction;
            this.drawableArray[3] = R.drawable.transaction;


        }
//        else if (selection.equals("MainMvisa"))
//        {
//            this.nameArray = new String[4];
//            this.descriptionArray = new String[4];
//            this.drawableArray = new int[4];
//
//            this.nameArray[0] = context.getString(R.string.mvisa_paymerchant);
//            this.nameArray[1] = context.getString(R.string.agency_banking_cash_deposit);
//            this.nameArray[2] = context.getString(R.string.sales_summary);
//            this.nameArray[3] = context.getString(R.string.settings);
//
//            this.descriptionArray[0] = context.getString(R.string.mvisa_paymerchant_desc);
//            this.descriptionArray[1] = context.getString(R.string.mvisa_deposit_desc);
//            this.descriptionArray[2] = context.getString(R.string.merchant_qr_desc);
//            this.descriptionArray[3] = context.getString(R.string.settingss);
//
//            this.drawableArray[0] = R.drawable.home_menu;
//            this.drawableArray[1] = R.drawable.deposit_icon;
//            this.drawableArray[2] = R.drawable.sales_summary;
//            this.drawableArray[3] = R.drawable.setting_logo;
//
//        }

        else if(selection.equals("MainAgencyBanking"))
        {
            this.nameArray = new String[6];
            this.descriptionArray = new String[6];
            this.drawableArray = new int[6];


            this.nameArray[0] = context.getString(R.string.agency_banking_payment);
            this.nameArray[1] = context.getString(R.string.agency_banking_cash_withdrawal);
            this.nameArray[2] = context.getString(R.string.agency_banking_transfer);
            this.nameArray[3] = context.getString(R.string.agency_banking_utility_payment);
            this.nameArray[4] = context.getString(R.string.pay_utility);
            this.nameArray[5] = context.getString(R.string.agency_banking_about_us);

            this.descriptionArray[0] = context.getString(R.string.agency_banking_payment_desc);
            this.descriptionArray[1] = context.getString(R.string.agency_banking_cash_deposit_desc);
            this.descriptionArray[2] = context.getString(R.string.agency_banking_cash_withdrawal_desc);
            this.descriptionArray[3] = context.getString(R.string.agency_banking_utility_payment_desc);
            this.descriptionArray[4] = context.getString(R.string.pay_utility_desc);
            this.descriptionArray[5] = context.getString(R.string.agency_banking_about_us_desc);

            this.drawableArray[0] = R.drawable.cash_out;
            this.drawableArray[1] = R.drawable.withdrawal_logo;
            this.drawableArray[2] = R.drawable.transfer_logo;
            this.drawableArray[3] = R.drawable.money_logo;
            this.drawableArray[4] = R.drawable.menu_utilities;
            this.drawableArray[5] = R.drawable.aboutus_logo;
        }
        else if(selection.equals("MainAgencyBankingAgent"))
        {
            this.nameArray = new String[6];
            this.descriptionArray = new String[6];
            this.drawableArray = new int[6];


            this.nameArray[0] = context.getString(R.string.agency_banking_account_openning);
            this.nameArray[1] = context.getString(R.string.agency_banking_cash_deposit);
            this.nameArray[2] = context.getString(R.string.agency_banking_notification);
            this.nameArray[3] = context.getString(R.string.agency_banking_utility_payment);
            this.nameArray[4] = context.getString(R.string.agency_banking_payment);
            this.nameArray[5] = context.getString(R.string.agency_banking_about_us);

            this.descriptionArray[0] = context.getString(R.string.agency_banking_account_openning_desc);
            this.descriptionArray[1] = context.getString(R.string.agency_banking_cash_deposit_desc);
            this.descriptionArray[2] = context.getString(R.string.agency_banking_cash_withdrawal_desc);
            this.descriptionArray[3] = context.getString(R.string.agency_banking_utility_payment_desc);
            this.descriptionArray[4] = context.getString(R.string.agency_banking_payment_desc);
            this.descriptionArray[5] = context.getString(R.string.agency_banking_about_us_desc);


            this.drawableArray[0] = R.drawable.open_account_logo;
            this.drawableArray[1] = R.drawable.money_logo;
            this.drawableArray[2] = R.drawable.withdrawal_logo;
            this.drawableArray[3] = R.drawable.money_logo;
            this.drawableArray[4] = R.drawable.topup_account_logo;
            this.drawableArray[5] = R.drawable.aboutus_logo;
        }
        else if(selection.equals("MainAgencyBankingMerchant"))
        {
            this.nameArray = new String[4];
            this.descriptionArray = new String[4];
            this.drawableArray = new int[4];


            this.nameArray[0] = context.getString(R.string.agency_banking_notification);
            this.nameArray[1] = context.getString(R.string.agency_banking_payment);
            this.nameArray[2] = context.getString(R.string.agency_banking_utility_payment);
            this.nameArray[3] = context.getString(R.string.agency_banking_about_us);

            this.descriptionArray[0] = context.getString(R.string.agency_banking_cash_withdrawal_desc);
            this.descriptionArray[1] = context.getString(R.string.agency_banking_payment_desc);
            this.descriptionArray[2] = context.getString(R.string.agency_banking_utility_payment_desc);
            this.descriptionArray[3] = context.getString(R.string.agency_banking_about_us_desc);

            this.drawableArray[0] = R.drawable.withdrawal_logo;
            this.drawableArray[1] = R.drawable.topup_account_logo;
            this.drawableArray[2] = R.drawable.money_logo;
            this.drawableArray[3] = R.drawable.aboutus_logo;
        }
        else if (selection.equals("MainMDC"))
        {
            this.nameArray = new String[2];
            this.descriptionArray = new String[2];
            this.drawableArray = new int[2];

            this.nameArray[0] = context.getString(R.string.h1_temporary);
            this.nameArray[1] = context.getString(R.string.h1_permanent);

            this.descriptionArray[0] = context.getString(R.string.p_temporary);
            this.descriptionArray[1] = context.getString(R.string.p_permanent);

            this.drawableArray[0] = R.drawable.menu_instant;
            this.drawableArray[1] = R.drawable.menu_recurring;

        }
        else if (selection.equals("MainDataTools"))
        {
            this.nameArray = new String[2];
            this.descriptionArray = new String[2];
            this.drawableArray = new int[2];

            this.nameArray[0] = "Record Business Owner";
            this.nameArray[1] = "Record Business";


            this.descriptionArray[0] = "Make instant collection of fees such as fines etc.";
            this.descriptionArray[1] = "Make collection of recurring payments";


            this.drawableArray[0] = R.drawable.menu_instant;
            this.drawableArray[1] = R.drawable.menu_recurring;

        }
        else if (selection.equals("MainHospitals"))
        {
            this.nameArray = new String[2];
            this.descriptionArray = new String[2];
            this.drawableArray = new int[2];

            this.nameArray[0] = context.getString(R.string.h1_hospital_cash);
            this.nameArray[1] = context.getString(R.string.h1_hospital_bima);

            this.descriptionArray[0] = context.getString(R.string.p_hospital_cash);
            this.descriptionArray[1] = context.getString(R.string.p_hospital_bima);

            this.drawableArray[0] = R.drawable.menu_instant;
            this.drawableArray[1] = R.drawable.menu_recurring;

        }

        else if(selection.equals("PayeeList"))
        {
                ArrayList<Payee> payees = db.getAllFriends();



                this.nameArray = new String[payees.size()];
                this.descriptionArray = new String[payees.size()];
                this.drawableArray = new int[payees.size()];

                for(int i=0; i<payees.size(); i++)
                {
                    Payee payee = new Payee();
                    this.nameArray[i]=payee.getPayeeName();
                    this.descriptionArray[i]=payee.getPayeeCardNumber();
                    this.drawableArray[i] = R.drawable.menu_recurring;
                }


        }

        else if(selection.equals("TransactionList"))
        {
//            ArrayList<Msg> payees = db.getTransactions();
//            this.nameArray = new String[payees.size()];
//            this.descriptionArray = new String[payees.size()];
//            this.drawableArray = new int[payees.size()];
//
//            for(int i=0; i<payees.size(); i++)
//            {
//                Payee payee = new Payee();
//                this.nameArray[i]=payee.getPayeeName();
//                this.descriptionArray[i]=payee.getPayeeCardNumber();
//                this.drawableArray[i] = R.drawable.menu_recurring;
//            }


        }


    }
}
