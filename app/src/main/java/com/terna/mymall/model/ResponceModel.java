package com.terna.mymall.model;

import com.google.gson.annotations.SerializedName;

public class ResponceModel {
    @SerializedName("success")
    private int success;

    @SerializedName("msg")
    private String msg;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponceModel{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
