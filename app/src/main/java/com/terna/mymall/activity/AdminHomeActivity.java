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
import com.terna.mymall.model.ResponceModel;
import com.terna.mymall.model.ShopModel;
import com.terna.mymall.model.ShopResopnceModel;
import com.terna.mymall.retrofit.ApiClient;
import com.terna.mymall.retrofit.ApiInterface;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.terna.mymall.helper.URLS.BASE_URL;

public class AdminHomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;

    List<ShopModel> shopModels;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.recyclerView);
        mFab = findViewById(R.id.fab);

        mFab.setOnClickListener(view -> {
            startActivity(new Intent(this, AddShopActivity.class).putExtra("MODE","add"));
        });
        AppUtils.showProgress(mContext, "Loading...");

    }

    void webCall() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ShopResopnceModel> getallShop = apiService.getAllShopCall();

        getallShop.enqueue(new Callback<ShopResopnceModel>() {
            @Override
            public void onResponse(Call<ShopResopnceModel> call, Response<ShopResopnceModel> response) {
                AppUtils.closeProgress();
                if (response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        shopModels=response.body().getShopModels();
                        Log.e("ShopList","ShopList="+shopModels.size());
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new ShopsAdapter());
                    } else {
                        AppUtils.showAlert(mContext, response.body().getMsg(), null, SweetAlertDialog.ERROR_TYPE);
                    }
                }else
                {
                    AppUtils.showAlert(mContext, "No Shops...", null, SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<ShopResopnceModel> call, Throwable t) {
                AppUtils.closeProgress();
                AppUtils.showAlert(mContext, "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);
            }
        });
    }

    class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.MyViewHolder> {

        @NonNull
        @Override
        public ShopsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_shop_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ShopsAdapter.MyViewHolder h, int position) {

            h.mUsShopName.setText(shopModels.get(position).getName());

            String url = BASE_URL + shopModels.get(position).getImage();
            Log.e("ShopList","url="+url);

            Glide.with(mContext)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(h.mUsShopImage);

            h.mUsShopImage.setOnClickListener(v -> {
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Select You want to Delete or Update")
                        .setConfirmText("Delete")
                        .setCancelText("Update")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                //Delete
                                deleteCall(shopModels.get(position).getShop_id());
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                startActivity(new Intent(mContext,AddShopActivity.class).putExtra("MODE","update")
                                .putExtra("DATA",shopModels.get(position)));


                            }
                        })
                        .show();
            });


        }

        @Override
        public int getItemCount() {
            return shopModels != null ? shopModels.size() : 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private AppCompatImageView mUsShopImage;
            private CTextView mUsShopName;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                mUsShopImage = itemView.findViewById(R.id.us_shop_image);
                mUsShopName = itemView.findViewById(R.id.us_shop_name);
            }
        }

        void deleteCall(int shopid)
        {
            AppUtils.showProgress(mContext, "Wait...");
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<ResponceModel> deleteShopCall=apiService.deleteShopCall(new ShopModel(shopid));

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
                        AppUtils.showAlert(mContext, "No Shops...", null, SweetAlertDialog.ERROR_TYPE);
                    }
                }

                @Override
                public void onFailure(Call<ResponceModel> call, Throwable t) {
                    AppUtils.closeProgress();
                    AppUtils.showAlert(mContext, "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);
                }
            });
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
