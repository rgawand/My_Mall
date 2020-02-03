package com.terna.mymall.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponce {

    @SerializedName("success")
    private int success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private UserData data;

    public int getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}
