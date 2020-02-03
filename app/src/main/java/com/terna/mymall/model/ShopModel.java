package com.terna.mymall.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ShopModel implements Parcelable {

    @SerializedName("shop_id")
    private int shop_id;

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

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public ShopModel(int shop_id) {
        this.shop_id = shop_id;
    }

    public ShopModel(String full_name, String email_id, String mobile_no, String full_address, String user_type, String name, String image) {

        this.full_name = full_name;
        this.email_id = email_id;
        this.mobile_no = mobile_no;
        this.full_address = full_address;
        this.user_type = user_type;
        this.name = name;
        this.image = image;
    }

    public ShopModel(int shop_id, int user_id, String full_name, String email_id, String mobile_no, String full_address,
                     String user_type, String name, String image) {
        this.shop_id = shop_id;
        this.user_id = user_id;
        this.full_name = full_name;
        this.email_id = email_id;
        this.mobile_no = mobile_no;
        this.full_address = full_address;
        this.user_type = user_type;
        this.name = name;
        this.image = image;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shop_id);
        dest.writeInt(this.user_id);
        dest.writeString(this.full_name);
        dest.writeString(this.email_id);
        dest.writeString(this.mobile_no);
        dest.writeString(this.full_address);
        dest.writeString(this.user_type);
        dest.writeString(this.name);
        dest.writeString(this.image);
    }

    protected ShopModel(Parcel in) {
        this.shop_id = in.readInt();
        this.user_id = in.readInt();
        this.full_name = in.readString();
        this.email_id = in.readString();
        this.mobile_no = in.readString();
        this.full_address = in.readString();
        this.user_type = in.readString();
        this.name = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<ShopModel> CREATOR = new Parcelable.Creator<ShopModel>() {
        @Override
        public ShopModel createFromParcel(Parcel source) {
            return new ShopModel(source);
        }

        @Override
        public ShopModel[] newArray(int size) {
            return new ShopModel[size];
        }
    };
}
