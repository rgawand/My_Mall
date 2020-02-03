package com.terna.mymall.retrofit;

import com.terna.mymall.model.LoginRequest;
import com.terna.mymall.model.LoginResponce;
import com.terna.mymall.model.OrderRequestModel;
import com.terna.mymall.model.OrderResponceModel;
import com.terna.mymall.model.ProductModel;
import com.terna.mymall.model.ProductResponceModel;
import com.terna.mymall.model.ProductResquestModel;
import com.terna.mymall.model.ResponceModel;
import com.terna.mymall.model.ShopModel;
import com.terna.mymall.model.ShopResopnceModel;
import com.terna.mymall.model.UserData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.terna.mymall.helper.URLS.*;


public interface ApiInterface {
    @POST(LOGIN)
    Call<LoginResponce> loginCall(@Body LoginRequest loginRequest);

    @POST(REGISTRATION)
    Call<LoginResponce> registration(@Body UserData userData);

    @POST(ADD_SHOP)
    Call<ResponceModel> addShopCall(@Body ShopModel shopModel);

    @POST(UPDATE_SHOP)
    Call<ResponceModel> updateShopCall(@Body ShopModel shopModel);

    @POST(DELETE_SHOP)
    Call<ResponceModel> deleteShopCall(@Body ShopModel shopModel);

    @POST(DELETE_PRODUCT)
    Call<ResponceModel> deleteProductCall(@Body ProductModel shopModel);

    @POST(GET_SHOPS)
    Call<ShopResopnceModel> getAllShopCall();

    @POST(ADD_PRODUCT)
    Call<ResponceModel> addProduct(@Body ProductModel productModel);

    @POST(UPDATE_PRODUCT)
    Call<ResponceModel> updateProduct(@Body ProductModel productModel);

    @GET(GET_PRODUCT)
    Call<ProductResponceModel> getAllProduct(@Query("id") String id);

    @POST(ADD_ORDER)
    Call<ResponceModel> addOrder(@Body ArrayList<OrderRequestModel> requestModel);

    @POST(GET_ORDER)
    Call<OrderResponceModel> getAllOrder(@Body OrderRequestModel requestModel);
}
