package com.terna.mymall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.terna.mymall.R;
import com.terna.mymall.helper.AppUtils;
import com.terna.mymall.helper.SessionHelper;
import com.terna.mymall.helper.ui.CTextView;
import com.terna.mymall.model.ProductModel;
import com.terna.mymall.model.ProductResponceModel;
import com.terna.mymall.model.ProductResquestModel;
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

public class ShopHomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    List<ProductModel> productModels;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.recyclerView);
        mFab = findViewById(R.id.fab);

        mFab.setOnClickListener(view -> {
            startActivity(new Intent(this, AddProductActivity.class).putExtra("MODE", "add"));
        });

    }

    void webCall() {
        AppUtils.showProgress(mContext, "Loading...");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ProductResponceModel> getAllProduct =
                apiService.getAllProduct(
                        "" + AppDatabase.getInstance(mContext).UserDAO().getUserData().getUser_id());

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
                        AppUtils.showAlert(mContext, response.body().getMsg(), null, SweetAlertDialog.ERROR_TYPE);

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
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.product_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder h, int position) {

            h.mShopName.setText("Name: "+productModels.get(position).getName());

            String url = BASE_URL + productModels.get(position).getImg_1();
            Log.e("ShopList", "url=" + url);

            Glide
                    .with(mContext)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(h.mShopImage);


            h.mShopImage.setOnClickListener(v -> {
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Select You want to Delete or Update")
                        .setConfirmText("Delete")
                        .setCancelText("Update")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                //Delete.
                                deleteCall(productModels.get(position).getProduct_id());
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                startActivity(new Intent(mContext, AddProductActivity.class).putExtra("MODE", "update")
                                        .putExtra("DATA", productModels.get(position)));

                            }
                        })
                        .show();
            });

            h.mProductPrice.setText("Price: Rs. "+productModels.get(position).getPrice());
            h.mProductShortDescription.setText("Description: "+productModels.get(position).getShort_des());

        }

        void deleteCall(String product)
        {
            AppUtils.showProgress(mContext, "Wait...");
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<ResponceModel> deleteShopCall=apiService.deleteProductCall(new ProductModel(product));

            deleteShopCall.enqueue(new Callback<ResponceModel>() {
                @Override
                public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
                    AppUtils.closeProgress();
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            webCall();
                        } else {
                            AppUtils.showAlert(mContext, response.body().getMsg(), null, SweetAlertDialog.ERROR_TYPE);
                        }
                    }else
                    {
                        AppUtils.showAlert(mContext, "No Product...", null, SweetAlertDialog.ERROR_TYPE);
                    }
                }

                @Override
                public void onFailure(Call<ResponceModel> call, Throwable t) {
                    AppUtils.closeProgress();
                    AppUtils.showAlert(mContext, "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashbord_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_setting:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirm")
                        .setContentText("Are you sure you want to log out?")
                        .setConfirmText("Logout")
                        .setCancelText("Cancel")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismissWithAnimation();
                            finish();
                            SessionHelper.getInstance(this).logout(this);
                        })
                        .show();

                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        webCall();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirm")
                .setContentText("Are you sure you want to exit?")
                .setConfirmText("Exit")
                .setCancelText("Cancel")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
