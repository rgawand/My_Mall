package com.terna.mymall.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderRequestModel implements Parcelable {

    int product_id;
    int user_id;
    String taken_type;
    String deliver_date;
    String place_order_date;
    String price;

    public OrderRequestModel(int product_id, int user_id, String taken_type, String deliver_date, String place_order_date, String price) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.taken_type = taken_type;
        this.deliver_date = deliver_date;
        this.place_order_date = place_order_date;
        this.price = price;
    }

    public OrderRequestModel(int user_id) {
        this.user_id = user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTaken_type() {
        return taken_type;
    }

    public void setTaken_type(String taken_type) {
        this.taken_type = taken_type;
    }

    public String getDeliver_date() {
        return deliver_date;
    }

    public void setDeliver_date(String deliver_date) {
        this.deliver_date = deliver_date;
    }

    public String getPlace_order_date() {
        return place_order_date;
    }

    public void setPlace_order_date(String place_order_date) {
        this.place_order_date = place_order_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.product_id);
        dest.writeInt(this.user_id);
        dest.writeString(this.taken_type);
        dest.writeString(this.deliver_date);
        dest.writeString(this.place_order_date);
        dest.writeString(this.price);
    }

    protected OrderRequestModel(Parcel in) {
        this.product_id = in.readInt();
        this.user_id = in.readInt();
        this.taken_type = in.readString();
        this.deliver_date = in.readString();
        this.place_order_date = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<OrderRequestModel> CREATOR = new Parcelable.Creator<OrderRequestModel>() {
        @Override
        public OrderRequestModel createFromParcel(Parcel source) {
            return new OrderRequestModel(source);
        }

        @Override
        public OrderRequestModel[] newArray(int size) {
            return new OrderRequestModel[size];
        }
    };
}
