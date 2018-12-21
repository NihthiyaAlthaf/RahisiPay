package com.goandroytech.www.rahisipay.Model;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Msg {

    private String MTI;
    private String field_3;
    private String field_4;
    private String field_7;
    private String field_11;
    private String field_12;
    private String field_37;
    private String field_38;
    private String field_39;
    private String field_41;
    private String field_42;
    private String field_43;
    private String field_48;
    private String field_52;
    private String field_58;
    private String field_61;
    private String field_62;
    private String field_67;
    private String field_68;
    private String field_69;
    private String field_100;
    private String field_102;
    private String field_103;
    private String field_128;
    private String field_nfc;
    private String field_bus_id;
    private String utility_control;
    private String service_selected;
    private String bill_request;
    private String amount, agent_name, transaction_time;
    private String account_number,password;
    private String BIN;
    private String inquiry;
    private String agent_id;
    private String p_number;
    private String fname, lname, mname;
    private String trans_type;
    private String pin1, pin2;
    private String token;
    private String name, phone_number,pan,guid,issuer,consentDatetime;
    private String ref_no, utility_type;
    private String recipientAccount, recipient_uid,merchant_id;
    private String customer_id;
    private String birth_date,identity_type,image,physical_address,identity_number,gender;
    private String identity_image;
    private String bank_name,bin,identity;
    private String type;
    private String agent_pan, location,approval_code,ret_no,sender_account;
    private String old_guid,new_guid;

    public String getOld_guid() {
        return old_guid;
    }

    public void setOld_guid(String old_guid) {
        this.old_guid = old_guid;
    }

    public String getNew_guid() {
        return new_guid;
    }

    public void setNew_guid(String new_guid) {
        this.new_guid = new_guid;
    }

    public String getAgent_pan() {
        return agent_pan;
    }

    public void setAgent_pan(String agent_pan) {
        this.agent_pan = agent_pan;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getApproval_code() {
        return approval_code;
    }

    public void setApproval_code(String approval_code) {
        this.approval_code = approval_code;
    }

    public String getRet_no() {
        return ret_no;
    }

    public void setRet_no(String ret_no) {
        this.ret_no = ret_no;
    }

    public String getSender_account() {
        return sender_account;
    }

    public void setSender_account(String sender_account) {
        this.sender_account = sender_account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity_image() {
        return identity_image;
    }

    public void setIdentity_image(String identity_image) {
        this.identity_image = identity_image;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(String identity_type) {
        this.identity_type = identity_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhysical_address() {
        return physical_address;
    }

    public void setPhysical_address(String physical_address) {
        this.physical_address = physical_address;
    }

    public String getIdentity_number() {
        return identity_number;
    }

    public void setIdentity_number(String identity_number) {
        this.identity_number = identity_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getRecipientAccount() {
        return recipientAccount;
    }

    public void setRecipientAccount(String recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public String getRecipient_uid() {
        return recipient_uid;
    }

    public void setRecipient_uid(String recipient_uid) {
        this.recipient_uid = recipient_uid;
    }

    public String getRef_no() {
        return ref_no;
    }

    public void setRef_no(String ref_no) {
        this.ref_no = ref_no;
    }

    public String getUtility_type() {
        return utility_type;
    }

    public void setUtility_type(String utility_type) {
        this.utility_type = utility_type;
    }

    public String getPin1() {
        return pin1;
    }

    public void setPin1(String pin1) {
        this.pin1 = pin1;
    }

    public String getPin2() {
        return pin2;
    }

    public void setPin2(String pin2) {
        this.pin2 = pin2;
    }

    public String getConsentDatetime() {
        return consentDatetime;
    }

    public void setConsentDatetime(String consentDatetime) {
        this.consentDatetime = consentDatetime;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getP_number() {
        return p_number;
    }

    public void setP_number(String p_number) {
        this.p_number = p_number;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getInquiry() {
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }

    public String getBIN() {
        return BIN;
    }

    public void setBIN(String BIN) {
        this.BIN = BIN;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    //Payment Variables
    private String SpCode;
    private String PayRefId;
    private String ResultUrl;
    private String BillCtrNum;
    private String PaidAmt;
    private String CCy;
    private String DptCellNum;
    private String DptName;
    private String PyrEmailAddr;

    public String getPyrEmailAddr() {
        return PyrEmailAddr;
    }

    public void setPyrEmailAddr(String pyrEmailAddr) {
        PyrEmailAddr = pyrEmailAddr;
    }

    public String getSpCode() {
        return SpCode;
    }

    public void setSpCode(String spCode) {
        SpCode = spCode;
    }

    public String getPayRefId() {
        return PayRefId;
    }

    public void setPayRefId(String payRefId) {
        PayRefId = payRefId;
    }

    public String getResultUrl() {
        return ResultUrl;
    }

    public void setResultUrl(String resultUrl) {
        ResultUrl = resultUrl;
    }

    public String getBillCtrNum() {
        return BillCtrNum;
    }

    public void setBillCtrNum(String billCtrNum) {
        BillCtrNum = billCtrNum;
    }

    public String getPaidAmt() {
        return PaidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        PaidAmt = paidAmt;
    }

    public String getCCy() {
        return CCy;
    }

    public void setCCy(String CCy) {
        this.CCy = CCy;
    }

    public String getDptCellNum() {
        return DptCellNum;
    }

    public void setDptCellNum(String dptCellNum) {
        DptCellNum = dptCellNum;
    }

    public String getDptName() {
        return DptName;
    }

    public void setDptName(String dptName) {
        DptName = dptName;
    }

    public String getBill_request() {
        return bill_request;
    }

    public void setBill_request(String bill_request) {
        this.bill_request = bill_request;
    }

    public String getUtility_control() {
        return utility_control;
    }

    public void setUtility_control(String utility_control) {
        this.utility_control = utility_control;
    }

    public String getService_selected() {
        return service_selected;
    }

    public void setService_selected(String service_selected) {
        this.service_selected = service_selected;
    }

    public String getField_bus_id() {
        return field_bus_id;
    }

    public void setField_bus_id(String field_bus_id) {
        this.field_bus_id = field_bus_id;
    }

    public String getNfcTagId(){return field_nfc;}
    public String get_MTI() {
        return MTI;
    }
    public String get_processing_code() {
        return field_3;
    }
    public String get_amount() {
        return field_4;
    }
    public String get_transmission_datetime() {
        return field_7;
    }
    public String get_stan() {
        return field_11;
    }
    public String get_localtime() {
        return field_12;
    }
    public String get_terminal_id() {
        return field_41;
    }
    public String get_additional_data() {
        return field_48;
    }
    public String get_PIN_data() {
        return field_52;
    }
    public String get_extended_tran_type() {
        return field_61;
    }
    public String get_extended_payment_code() {
        return field_67;
    }
    public String get_receiving_inst() {
        return field_100;
    }
    public String get_from_acc() {
        return field_102;
    }
    public String get_to_acc() {
        return field_103;
    }
    public String get_rsp_code() {
        return field_39;
    }
    public String get_receipt_no() {
        return field_38;
    }
    public String get_business_name() {
        return field_42;
    }
    public String get_agent_location() {
        return field_43;
    }
    public String get_provider_name() {
        return field_58;
    }
    public String get_field_62() {
        return field_62;
    }
    public String get_field_68() {
        return field_68;
    }
    public String get_field_69() {
        return field_69;
    }
    public String get_field_128() {
        return field_128;
    }
    public String get_field_37() {
        return field_37;
    }

    public void setNfcTagId(String value){
        this.field_nfc=value;
    }
    public void set_MTI(String value) {
        this.MTI=value;
    }
    public void set_processing_code(String value) {
        this.field_3=value;
    }
    public void set_amount(String value) {
        this.field_4=value;
    }
    @SuppressLint("SimpleDateFormat")
    public void set_transmission_datetime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String _datetime = sdf.format(cal.getTime()).toString();
        this.field_7=_datetime;
    }
    public void set_stan(String _STAN) {
        this.field_11 = _STAN;//field_7.substring(6)
    }
    public void set_stan() {
        this.field_11 = field_7.substring(6);
    }
    public void set_localtime() {
        this.field_12 = field_7.substring(6);
    }
    public void set_terminal_id(String value) {
        this.field_41=value;
    }
    public void set_PIN_data(String value) {
        this.field_52=value;
    }
    public void set_receiving_inst(String value) {
        this.field_100=value;
    }
    public void set_from_acc(String value) {
        this.field_102=value;
    }
    public void set_to_acc(String value) {

        this.field_103=value;
    }
    public void set_rsp_code(String value) {
        this.field_39=value;
    }
    public void set_receipt_no(String value) {
        this.field_38=value;
    }
    public void set_business_name(String value) {
        this.field_42=value;
    }
    public void set_agent_location(String value) {
        this.field_43=value;
    }
    public void set_provider_name(String value) {
        this.field_58=value;
    }
    public void set_extended_payment_code(String value){
        this.field_67=value;
    }
    public void set_extended_tran_type(String value){
        this.field_61=value;
    }
    public void set_additional_data(String value){
        this.field_48=value;
    }
    public void set_field_62(String value){
        this.field_62=value;
    }
    public void set_field_68(String value){
        this.field_68=value;
    }
    public void set_field_69(String value){
        this.field_69=value;
    }
    public void set_field_128(String value){
        this.field_128=value;
    }
    public void set_field_37(String field_37) {
        this.field_37 = field_37;
    }

}