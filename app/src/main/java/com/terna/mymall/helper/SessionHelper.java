package com.terna.mymall.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.terna.mymall.activity.LoginActivity;
import com.terna.mymall.room.AppDatabase;

public class SessionHelper {

    Context mContext;
    private static SessionHelper sessionHelper;

    static SharedPreferences.Editor editor;
    static SharedPreferences sharedPreferences;

    private static final String TAG_FILE_NAME = "logtracker";

    private static final String LOGIN_FLAG = "login_flag";

    public SessionHelper(Context mContext) {
        this.mContext = mContext;
    }

    public static SessionHelper getInstance(Context paramContext) {
        if (sessionHelper == null) {
            sessionHelper = new SessionHelper(paramContext);
        }
        sharedPreferences = paramContext.getSharedPreferences(TAG_FILE_NAME, 0);
        editor = sharedPreferences.edit();
        return sessionHelper;
    }

    //Login Status
    public void setLoginFlag(int status) { //0,1,2
        editor.putInt(LOGIN_FLAG, status);
        editor.commit();
    }

    public int getLoginFlag() {
        return sharedPreferences.getInt(LOGIN_FLAG, 0);
    }


    //logout
    public void logout(Context mContext) {

        editor.clear();
        editor.commit();

        try {
            AppDatabase.getInstance(mContext).UserDAO().deleteTask(AppDatabase.getInstance(mContext).UserDAO().getUserData());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mContext.startActivity(new Intent(mContext, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }


}
