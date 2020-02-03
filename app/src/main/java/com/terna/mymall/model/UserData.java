package com.terna.mymall.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class UserData {
    @PrimaryKey
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("full_name")
    private String full_name;

    @SerializedName("email_id")
    private String email_id;

    @SerializedName("mobile_no")
    private String mobile_no;

    @SerializedName("full_address")
    private String full_address;

    @SerializedName("user_type")
    private String user_type;

    @Ignore
    public UserData(String full_name, String email_id, String mobile_no, String full_address, String user_type) {
        this.full_name = full_name;
        this.email_id = email_id;
        this.mobile_no = mobile_no;
        this.full_address = full_address;
        this.user_type = user_type;
    }

    public UserData(int user_id, String full_name, String email_id, String mobile_no, String full_address, String user_type) {
        this.user_id = user_id;
        this.full_name = full_name;
        this.email_id = email_id;
        this.mobile_no = mobile_no;
        this.full_address = full_address;
        this.user_type = user_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
