package com.goandroytech.www.rahisipay.Model;

public class Service_Model {
    private String title;
    String image;
    String url;
    String parent;
    String subscribed;
    String subscriptionAccount;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    String service_id;
    public String getUrl() {
        return url;
    }

    public String getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(String subscribed) {
        this.subscribed = subscribed;
    }

    public String getSubscriptionAccount() {
        return subscriptionAccount;
    }

    public void setSubscriptionAccount(String subscriptionAccount) {
        this.subscriptionAccount = subscriptionAccount;
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

    public Service_Model(String get_service_id, String image, String title, String get_logo_url, String get_parent, String get_subscribed, String get_subscriptionAccount) {
        this.service_id=get_service_id;
        this.image=image;
        this.title = title;
        this.url = get_logo_url;
        this.parent=get_parent;
        this.subscribed=get_subscribed;
        this.subscriptionAccount=get_subscriptionAccount;
    }




}