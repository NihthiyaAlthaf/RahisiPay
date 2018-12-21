package com.goandroytech.www.rahisipay.Model;

import java.util.ArrayList;

public class Child {
    private String title;
    String image;
    String url;
    String parent;
    String subscribe;
    String subscriptionAccount;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getSubscriptionAccount() {
        return subscriptionAccount;
    }

    public void setSubscriptionAccount(String subscriptionAccount) {
        this.subscriptionAccount = subscriptionAccount;
    }

    public Child(String get_service_id, String get_image, String get_service_name, String get_logo_url, String get_parent, String get_subscribe, String get_subscriptionAccount) {
        this.service_id=get_service_id;
        this.image=get_image;

        this.title=get_service_name;
        this.url=get_logo_url;
        this.parent=get_parent;
        this.subscribe = get_subscribe;
        this.subscriptionAccount = get_subscriptionAccount;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    String service_id;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }





}