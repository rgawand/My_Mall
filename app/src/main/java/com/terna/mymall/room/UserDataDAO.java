package com.terna.mymall.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.terna.mymall.model.UserData;

@Dao
public interface UserDataDAO {
    @Insert
    void insertUserData(UserData userData);

    @Query("SELECT * FROM UserData LIMIT 1")
    UserData getUserData();

    @Delete
    void deleteTask(UserData note);
}
