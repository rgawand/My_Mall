package com.terna.mymall.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopResopnceModel implements Parcelable {

    @SerializedName("success")
    private int success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<ShopModel> shopModels;


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

    public List<ShopModel> getShopModels() {
        return shopModels;
    }

    public void setShopModels(List<ShopModel> shopModels) {
        this.shopModels = shopModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.success);
        dest.writeString(this.msg);
        dest.writeTypedList(this.shopModels);
    }

    public ShopResopnceModel() {
    }

    protected ShopResopnceModel(Parcel in) {
        this.success = in.readInt();
        this.msg = in.readString();
        this.shopModels = in.createTypedArrayList(ShopModel.CREATOR);
    }

    public static final Parcelable.Creator<ShopResopnceModel> CREATOR = new Parcelable.Creator<ShopResopnceModel>() {
        @Override
        public ShopResopnceModel createFromParcel(Parcel source) {
            return new ShopResopnceModel(source);
        }

        @Override
        public ShopResopnceModel[] newArray(int size) {
            return new ShopResopnceModel[size];
        }
    };
}
