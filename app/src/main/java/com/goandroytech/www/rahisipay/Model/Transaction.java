package com.goandroytech.www.rahisipay.Model;

public class Transaction {

    private String date;
    String description;
    String amount;
    String charge;
    String nature;
    String status_id;
    String status;

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Transaction(String get_date, String get_description, String get_amount, String get_charge, String get_nature, String get_status_id, String get_status) {
        this.date = get_date;
        this.description = get_description;

        this.amount = get_amount;
        this.charge = get_charge;
        this.nature = get_nature;
        this.status_id = get_status_id;
        this.status = get_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }
}
