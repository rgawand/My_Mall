package com.terna.mymall.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponceModel implements Parcelable {

    @SerializedName("success")
    private int success;

    @SerializedName("msg")
    private String msg;


    @SerializedName("data")
    private List<ProductModel> data;

    public ProductResponceModel(int success, String msg, List<ProductModel> data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.success);
        dest.writeString(this.msg);
        dest.writeTypedList(this.data);
    }

    protected ProductResponceModel(Parcel in) {
        this.success = in.readInt();
        this.msg = in.readString();
        this.data = in.createTypedArrayList(ProductModel.CREATOR);
    }

    public static final Parcelable.Creator<ProductResponceModel> CREATOR = new Parcelable.Creator<ProductResponceModel>() {
        @Override
        public ProductResponceModel createFromParcel(Parcel source) {
            return new ProductResponceModel(source);
        }

        @Override
        public ProductResponceModel[] newArray(int size) {
            return new ProductResponceModel[size];
        }
    };

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

    public List<ProductModel> getData() {
        return data;
    }

    public void setData(List<ProductModel> data) {
        this.data = data;
    }
}
