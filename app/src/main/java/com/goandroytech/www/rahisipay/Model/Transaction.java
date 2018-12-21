package com.goandroytech.www.rahisipay.Model;

public class Transaction {

    private String date;
    String description;
    String amount;
    String charge;
    String nature;

    public Transaction(String get_date, String get_description, String get_amount, String get_charge, String get_nature) {
        this.date = get_date;
        this.description = get_description;
        this.amount = get_amount;
        this.charge = get_charge;
        this.nature = get_nature;
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
