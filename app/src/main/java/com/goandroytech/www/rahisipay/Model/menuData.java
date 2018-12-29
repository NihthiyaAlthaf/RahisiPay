package com.goandroytech.www.rahisipay.Model;

/**
 * Created by michael.nkotagu on 6/18/2015.
 */

public class menuData {


    String name;
    String description;
    int image;


    public menuData(String name, String description, int image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public int getImage() {
        return image;
    }


}