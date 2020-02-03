package com.terna.mymall.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.terna.mymall.R;
import com.terna.mymall.helper.AppUtils;
import com.terna.mymall.helper.ui.CTextView;
import com.terna.mymall.model.ProductModel;
import com.terna.mymall.model.ProductResponceModel;
import com.terna.mymall.model.ResponceModel;
import com.terna.mymall.retrofit.ApiClient;
import com.terna.mymall.retrofit.ApiInterface;
import com.terna.mymall.room.AppDatabase;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.terna.mymall.helper.URLS.BASE_URL;

public class ViewProductByShopActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    List<ProductModel> productModels;
    Context mContext = this;
    String passID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_by_shop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        mRecyclerView = findViewById(R.id.recyclerView);
        passID = getIntent().getStringExtra("ID");
        webCall();
    }

    void webCall() {
        AppUtils.showProgress(mContext, "Loading...");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ProductResponceModel> getAllProduct =
                apiService.getAllProduct(passID);

        getAllProduct.enqueue(new Callback<ProductResponceModel>() {
            @Override
            public void onResponse(Call<ProductResponceModel> call, Response<ProductResponceModel> response) {
                AppUtils.closeProgress();
                if (response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        productModels = response.body().getData();
                        Log.e("ShopList", "ShopList=" + productModels.size());
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new ProductAdapter());

                    } else {
                        AppUtils.showAlert(mContext,"No Products...", null, SweetAlertDialog.ERROR_TYPE);

                    }
                } else {
                    AppUtils.showAlert(mContext, "No Products...", null, SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<ProductResponceModel> call, Throwable t) {

            }
        });
    }

    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

        @NonNull
        @Override
        public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.product_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder h, int position) {

            h.mShopName.setText("Name: " + productModels.get(position).getName());

            String url = BASE_URL + productModels.get(position).getImg_1();
            Log.e("ShopList", "url=" + url);

            Glide
                    .with(mContext)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(h.mShopImage);


            h.mShopImage.setOnClickListener(v -> {

            });

            h.mProductPrice.setText("Price: Rs. " + productModels.get(position).getPrice());
            h.mProductShortDescription.setText("Description: " + productModels.get(position).getShort_des());

            h.mShopImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(mContext,ProductDetailsActivity.class).putExtra("DATA",productModels.get(position)));
                }
            });

        }


        @Override
        public int getItemCount() {
            return productModels != null ? productModels.size() : 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {


            private AppCompatImageView mShopImage;
            private CTextView mShopName;
            private CTextView mProductPrice;
            private CTextView mProductShortDescription;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                mShopImage = itemView.findViewById(R.id.shop_image);
                mShopName = itemView.findViewById(R.id.shop_name);
                mProductPrice = itemView.findViewById(R.id.product_price);
                mProductShortDescription = itemView.findViewById(R.id.product_short_description);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
