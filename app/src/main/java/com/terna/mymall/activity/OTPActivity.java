package com.terna.mymall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.terna.mymall.R;
import com.terna.mymall.helper.AppUtils;
import com.terna.mymall.helper.ui.CButton;
import com.terna.mymall.helper.ui.CEditText;
import com.terna.mymall.helper.SessionHelper;
import com.terna.mymall.model.LoginRequest;
import com.terna.mymall.model.LoginResponce;
import com.terna.mymall.retrofit.ApiClient;
import com.terna.mymall.retrofit.ApiInterface;
import com.terna.mymall.room.AppDatabase;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.terna.mymall.helper.AppUtils.closeProgress;
import static com.terna.mymall.helper.AppUtils.showAlert;
import static com.terna.mymall.helper.AppUtils.showProgress;

public class OTPActivity extends AppCompatActivity {

    private CEditText mOtpEdOtp;
    private CButton mOtpBtn;
    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //firebase auth object
    private FirebaseAuth mAuth;

    Context mContex = this;
    String mobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        //initializing objects
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("OTP Verification");

        mAuth = FirebaseAuth.getInstance();

        mOtpEdOtp = findViewById(R.id.otp_ed_otp);
        mOtpBtn = findViewById(R.id.otp_btn);

        mobileNumber = getIntent().getStringExtra("MOBILE");
        sendVerificationCode(mobileNumber);
        mOtpBtn.setOnClickListener(v -> {
            if (mOtpEdOtp.getText().toString().equals("0000")) {
                LoginCall(mobileNumber);
            } else {
                Toast.makeText(this, "Enter Correct OTP!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                mOtpEdOtp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            // mResendToken = forceResendingToken;
        }
    };

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                          Log.e("OTPActivity","OTP V Done");
                            LoginCall(mobileNumber);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                          /*  Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();*/

                            Toast.makeText(mContex, "Invalid code entered...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void LoginCall(String mobileNumber) {
        Log.e("OTPActivity","LoginCall");
        showProgress(mContex, "Wait...");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<LoginResponce> loginCall = apiService.loginCall(new LoginRequest(mobileNumber));

        loginCall.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {

                closeProgress();
                if (response.body()!=null&& response.body().getSuccess() == 1) {

                    AppDatabase.getInstance(mContex).UserDAO().insertUserData(response.body().getData());
                    SessionHelper.getInstance(mContex).setLoginFlag(1);

                    switch (response.body().getData().getUser_type())
                    {
                        case "0":
                        {
                            startActivity(new Intent(mContex, AdminHomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }break;

                        case "1":
                        {   startActivity(new Intent(mContex, ShopHomeActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }break;

                        case "2":
                        {
                            startActivity(new Intent(mContex, UserHomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));


                        }break;
                    }
                  } else if (response.body()!=null&& response.body().getSuccess() == 0){
                    startActivity(new Intent(mContex, RegistrationActivity.class).putExtra("MOBILE", mobileNumber)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }else {
                    AppUtils.showAlert(mContex,"Error",null,SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponce> call, Throwable t) {
                closeProgress();
                showAlert(mContex, "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);
            }
        });
    }


    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }
}
