package com.terna.mymall.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponceModel {

    @SerializedName("success")
    private int success;

    @SerializedName("msg")
    private String msg;


    @SerializedName("data")
    private List<OrderModel> data;

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

    public List<OrderModel> getData() {
        return data;
    }

    public void setData(List<OrderModel> data) {
        this.data = data;
    }

    public OrderResponceModel(int success, String msg, List<OrderModel> data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }
}
