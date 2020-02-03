package com.terna.mymall.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class ProductModel implements Parcelable {

    @NonNull
    @PrimaryKey
    @SerializedName("product_id")
    private String product_id;

    @SerializedName("img_1")
    private String img_1;

    @SerializedName("img_2")
    private String img_2;

    @SerializedName("img_3")
    private String img_3;

    @SerializedName("img_4")
    private String img_4;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("short_des")
    private String short_des;

    @SerializedName("persentage")
    private String persentage;

    @SerializedName("get_more")
    private String get_more;

    @SerializedName("full_des")
    private String full_des;

    @SerializedName("shop_id")
    private String shop_id;

    @SerializedName("user_id")
    private String user_id;

    @Ignore
    public ProductModel(String product_id) {
        this.product_id = product_id;
    }

    @Ignore
    public ProductModel(String img_1, String img_2, String img_3, String img_4, String name, String price, String short_des, String persentage, String get_more, String full_des, String shop_id, String user_id) {
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_3 = img_3;
        this.img_4 = img_4;
        this.name = name;
        this.price = price;
        this.short_des = short_des;
        this.persentage = persentage;
        this.get_more = get_more;
        this.full_des = full_des;
        this.shop_id = shop_id;
        this.user_id = user_id;
    }

    public ProductModel(String product_id, String img_1, String img_2, String img_3, String img_4, String name, String price, String short_des, String persentage, String get_more, String full_des, String shop_id, String user_id) {
        this.product_id = product_id;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_3 = img_3;
        this.img_4 = img_4;
        this.name = name;
        this.price = price;
        this.short_des = short_des;
        this.persentage = persentage;
        this.get_more = get_more;
        this.full_des = full_des;
        this.shop_id = shop_id;
        this.user_id = user_id;
    }

    public String getImg_1() {
        return img_1;
    }

    public void setImg_1(String img_1) {
        this.img_1 = img_1;
    }

    public String getImg_2() {
        return img_2;
    }

    public void setImg_2(String img_2) {
        this.img_2 = img_2;
    }

    public String getImg_3() {
        return img_3;
    }

    public void setImg_3(String img_3) {
        this.img_3 = img_3;
    }

    public String getImg_4() {
        return img_4;
    }

    public void setImg_4(String img_4) {
        this.img_4 = img_4;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShort_des() {
        return short_des;
    }

    public void setShort_des(String short_des) {
        this.short_des = short_des;
    }

    public String getPersentage() {
        return persentage;
    }

    public void setPersentage(String persentage) {
        this.persentage = persentage;
    }

    public String getGet_more() {
        return get_more;
    }

    public void setGet_more(String get_more) {
        this.get_more = get_more;
    }

    public String getFull_des() {
        return full_des;
    }

    public void setFull_des(String full_des) {
        this.full_des = full_des;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_id);
        dest.writeString(this.img_1);
        dest.writeString(this.img_2);
        dest.writeString(this.img_3);
        dest.writeString(this.img_4);
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.short_des);
        dest.writeString(this.persentage);
        dest.writeString(this.get_more);
        dest.writeString(this.full_des);
        dest.writeString(this.shop_id);
        dest.writeString(this.user_id);
    }



    protected ProductModel(Parcel in) {
        this.product_id = in.readString();
        this.img_1 = in.readString();
        this.img_2 = in.readString();
        this.img_3 = in.readString();
        this.img_4 = in.readString();
        this.name = in.readString();
        this.price = in.readString();
        this.short_des = in.readString();
        this.persentage = in.readString();
        this.get_more = in.readString();
        this.full_des = in.readString();
        this.shop_id = in.readString();
        this.user_id = in.readString();
    }

    public static final Parcelable.Creator<ProductModel> CREATOR = new Parcelable.Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel source) {
            return new ProductModel(source);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };
}
