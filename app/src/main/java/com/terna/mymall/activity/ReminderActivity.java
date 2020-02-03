package com.terna.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.terna.mymall.R;

public class ReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().hide();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,WelcomeScreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
