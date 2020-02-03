package com.terna.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.terna.mymall.R;
import com.terna.mymall.helper.SessionHelper;
import com.terna.mymall.room.AppDatabase;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                if(SessionHelper.getInstance(WelcomeScreenActivity.this).getLoginFlag()==0 ||
                        SessionHelper.getInstance(WelcomeScreenActivity.this).getLoginFlag()==2 )
                {
                    Intent i = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else
                {

                    switch (AppDatabase.getInstance(WelcomeScreenActivity.this).UserDAO().getUserData().getUser_type())
                    {
                        case "0":
                        {
                            startActivity(new Intent(WelcomeScreenActivity.this, AdminHomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }break;

                        case "1":
                        {
                            startActivity(new Intent(WelcomeScreenActivity.this, ShopHomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }break;

                        case "2":
                        {
                            startActivity(new Intent(WelcomeScreenActivity.this, UserHomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }break;
                    }

                }

            }
        }, 1000);
    }
}
