package com.example.chatapp.Activity.Model;

public class Users {
    String uid;
    String username;
    String email;
    String iamgeurl;
    String statues;

    public Users(String uid, String username, String email, String iamgeurl, String statues) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.iamgeurl = iamgeurl;
        this.statues = statues;
    }

    public Users() {
    }

    public String getUid() {
        return uid;
    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIamgeurl() {
        return iamgeurl;
    }

    public void setIamgeurl(String iamgeurl) {
        this.iamgeurl = iamgeurl;
    }
}
