package com.terna.mymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import com.terna.mymall.R;
import com.terna.mymall.helper.ui.CButton;
import com.terna.mymall.helper.ui.CEditText;

public class LoginActivity extends AppCompatActivity {

    private CEditText mLoginPhoneNumber;
    private CButton mOtpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");
        mLoginPhoneNumber = findViewById(R.id.login_phone_number);
        mOtpBtn = findViewById(R.id.otp_btn);

        mLoginPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                int lenght=editable.length();

                if(lenght==10)
                {
                    mOtpBtn.setText("Continue");
                    mOtpBtn.setAlpha(1f);
                }else {
                    mOtpBtn.setText("Enter phone number to continue");
                    mOtpBtn.setAlpha(0.5f);
                }
            }
        });
        mOtpBtn.setOnClickListener(v->{
            String mobile = mLoginPhoneNumber.getText().toString().trim();

            if(mobile.isEmpty() || mobile.length() < 10){
                mLoginPhoneNumber.setError("Enter a valid mobile");
                mLoginPhoneNumber.requestFocus();
                return;
            }

            Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
            intent.putExtra("MOBILE", mobile);
            startActivity(intent);
        });
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();

    }
}

