package com.terna.mymall.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.terna.mymall.model.ProductModel;

import java.util.List;

@Dao
public interface AddCartDAO {

    @Insert
    void addToCart(ProductModel productModel);

    @Query("SELECT * FROM productmodel")
    List<ProductModel> getCartItem();

    @Delete
    void removeFromCart(ProductModel ProductModel);
}
