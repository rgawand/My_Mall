package com.terna.mymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.terna.mymall.R;
import com.terna.mymall.helper.ui.CButton;
import com.terna.mymall.helper.ui.CTextView;

import static com.terna.mymall.helper.URLS.BASE_URL;

public class MyCartActivity extends AppCompatActivity {

    private RecyclerView mPaymentList;
    private CTextView mPShowPrice;
    private CButton mPBuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        mPaymentList = findViewById(R.id.payment_list);
        mPShowPrice = findViewById(R.id.p_show_price);
        mPBuyNow = findViewById(R.id.p_buy_now);
    }

}
