package com.terna.mymall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.terna.mymall.R;
import com.terna.mymall.helper.AppUtils;
import com.terna.mymall.helper.ui.CButton;
import com.terna.mymall.helper.SessionHelper;
import com.terna.mymall.model.LoginResponce;
import com.terna.mymall.model.UserData;
import com.terna.mymall.retrofit.ApiClient;
import com.terna.mymall.retrofit.ApiInterface;
import com.terna.mymall.room.AppDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.terna.mymall.helper.AppUtils.closeProgress;
import static com.terna.mymall.helper.AppUtils.showAlert;
import static com.terna.mymall.helper.AppUtils.showProgress;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText mRegFullname;
    private TextInputEditText mRegEmail;
    private TextInputEditText mRegAddress;
    private TextInputEditText mRegLandmark;
    private CButton mOtpBtn;
    Context mContex = this;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");

        mobile = getIntent().getStringExtra("MOBILE");

        mRegFullname = findViewById(R.id.reg_fullname);
        mRegEmail = findViewById(R.id.reg_email);
        mRegAddress = findViewById(R.id.reg_address);
        mRegLandmark = findViewById(R.id.reg_landmark);
        mOtpBtn = findViewById(R.id.otp_btn);

        mOtpBtn.setOnClickListener(v -> {
            String fullName = mRegFullname.getText().toString().trim();
            String emailID = mRegEmail.getText().toString().trim();
            String address = mRegAddress.getText().toString().trim();
            String landmark = mRegLandmark.getText().toString().trim();

            if (fullName.isEmpty()) {
                AppUtils.showAlert(mContex, "Enter Name!!!", null, SweetAlertDialog.ERROR_TYPE);
            } else if (emailID.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(emailID).matches()) {
                AppUtils.showAlert(mContex, "Enter Email ID!!!", null, SweetAlertDialog.ERROR_TYPE);

            } else if (address.isEmpty()) {
                AppUtils.showAlert(mContex, "Enter Address!!!", null, SweetAlertDialog.ERROR_TYPE);

            } else if (landmark.isEmpty()) {
                AppUtils.showAlert(mContex, "Enter Landmark!!!", null, SweetAlertDialog.ERROR_TYPE);

            } else {


                showProgress(mContex, "Wait...");
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);

                UserData userData = new UserData(fullName, emailID, mobile, address, "2");

                Log.e("REG", "Model=" + new Gson().toJson(userData));

                Call<LoginResponce> responceCall = apiService.registration(
                        userData);

                responceCall.enqueue(new Callback<LoginResponce>() {
                    @Override
                    public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {
                        closeProgress();
                        if (response.body() != null && response.body().getSuccess() == 1) {

                            AppDatabase.getInstance(mContex).UserDAO().insertUserData(response.body().getData());
                            SessionHelper.getInstance(mContex).setLoginFlag(1);
                            startActivity(new Intent(mContex, UserHomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        } else if (response.body() != null && response.body().getSuccess() == 0) {
                            AppUtils.showAlert(mContex, "Error", response.body().getMsg(), SweetAlertDialog.ERROR_TYPE);
                        } else {
                            AppUtils.showAlert(mContex, "Error", null, SweetAlertDialog.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponce> call, Throwable t) {
                        closeProgress();
                        showAlert(mContex, "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);
                    }
                });


            }
        });
    }


    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();

    }
}
