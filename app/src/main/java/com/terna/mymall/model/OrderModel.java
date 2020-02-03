package com.terna.mymall.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrderModel implements Parcelable {

    @SerializedName("taken_type")
    String taken_type;

    @SerializedName("product_id")
    int product_id;

    @SerializedName("place_order_date")
    String place_order_date;

    @SerializedName("deliver_date")
    String deliver_date;

    @SerializedName("name")
    String name;

    @SerializedName("img_1")
    String img_1;

    public String getTaken_type() {
        return taken_type;
    }

    public void setTaken_type(String taken_type) {
        this.taken_type = taken_type;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getPlace_order_date() {
        return place_order_date;
    }

    public void setPlace_order_date(String place_order_date) {
        this.place_order_date = place_order_date;
    }

    public String getDeliver_date() {
        return deliver_date;
    }

    public void setDeliver_date(String deliver_date) {
        this.deliver_date = deliver_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_1() {
        return img_1;
    }

    public void setImg_1(String img_1) {
        this.img_1 = img_1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.taken_type);
        dest.writeInt(this.product_id);
        dest.writeString(this.place_order_date);
        dest.writeString(this.deliver_date);
        dest.writeString(this.name);
        dest.writeString(this.img_1);
    }

    public OrderModel() {
    }

    protected OrderModel(Parcel in) {
        this.taken_type = in.readString();
        this.product_id = in.readInt();
        this.place_order_date = in.readString();
        this.deliver_date = in.readString();
        this.name = in.readString();
        this.img_1 = in.readString();
    }

    public static final Parcelable.Creator<OrderModel> CREATOR = new Parcelable.Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel source) {
            return new OrderModel(source);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };
}
