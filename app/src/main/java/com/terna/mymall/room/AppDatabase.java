package com.terna.mymall.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.terna.mymall.model.ProductModel;
import com.terna.mymall.model.UserData;
import com.terna.mymall.room.dao.AddCartDAO;

@Database(entities = {UserData.class, ProductModel.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDataDAO UserDAO();
    public abstract AddCartDAO AddToCartDAO();


    private static AppDatabase noteDB;

    public static AppDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }

    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "mall_db")
                .allowMainThreadQueries().build();
    }

}
