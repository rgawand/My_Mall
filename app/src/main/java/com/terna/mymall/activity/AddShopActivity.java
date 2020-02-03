package com.terna.mymall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.terna.mymall.R;
import com.terna.mymall.helper.AppUtils;
import com.terna.mymall.helper.ui.CButton;
import com.terna.mymall.helper.ui.CEditText;
import com.terna.mymall.model.ResponceModel;
import com.terna.mymall.model.ShopModel;
import com.terna.mymall.retrofit.ApiClient;
import com.terna.mymall.retrofit.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.terna.mymall.helper.URLS.BASE_URL;

public class AddShopActivity extends AppCompatActivity {

    private AppCompatImageView mShopBanner;
    private CEditText mShopName;
    private CEditText mShopOwnerName;
    private CEditText mShopNumber;
    private CButton mBtn;
    String encodedString;

    Context mContext = this;
    ShopModel getShopModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mShopBanner = findViewById(R.id.shop_banner);
        mShopName = findViewById(R.id.shop_name);
        mShopOwnerName = findViewById(R.id.shop_owner_name);
        mShopNumber = findViewById(R.id.shop_number);
        mBtn = findViewById(R.id.btn);
        String mode = getIntent().getStringExtra("MODE");

        if (mode.equals("add")) {
            mBtn.setText("ADD SHOP");
            Log.e("SHOP","Mode=add");
        } else {
            Log.e("SHOP","Mode=update");

            getShopModel = getIntent().getParcelableExtra("DATA");

            mShopName.setText(getShopModel.getName());
            mShopOwnerName.setText(getShopModel.getFull_name());
            mShopNumber.setText(getShopModel.getMobile_no());
            mShopNumber.setEnabled(false);

            String url = BASE_URL + getShopModel.getImage();

            Glide
                    .with(mContext)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(mShopBanner);


            mBtn.setText("UPDATE SHOP");
        }

        mShopBanner.setOnClickListener(view -> {

            ImagePicker.create(this)
                    .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                    .folderMode(true) // folder mode (false by default)
                    .toolbarFolderTitle("Folder") // folder selection title
                    .toolbarImageTitle("Tap to select") // image selection title
                    .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                    .includeVideo(false) // Show video on image picker
                    .single() // single mode
                    .limit(1) // max images can be selected (99 by default)
                    .showCamera(true) // show camera or not (true by default)
                    .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                    .enableLog(false) // disabling log
                    .start(); // start image picker activity with request code
        });


        mBtn.setOnClickListener(view -> {
            String sName = mShopName.getText().toString();
            String wName = mShopOwnerName.getText().toString();
            String sNumber = mShopNumber.getText().toString();
            convertImageIntoBase64();
            if (sName.isEmpty()) {
                AppUtils.showAlert(mContext, "Enter Shop Name", null, SweetAlertDialog.WARNING_TYPE);

            } else if (wName.isEmpty()) {
                AppUtils.showAlert(mContext, "Enter Owner Name", null, SweetAlertDialog.WARNING_TYPE);

            } else if (sNumber.length() != 10 || sNumber.isEmpty()) {
                AppUtils.showAlert(mContext, "Enter Mobile Number", null, SweetAlertDialog.WARNING_TYPE);

            } else if (encodedString == null || encodedString.isEmpty()) {
                AppUtils.showAlert(mContext, "Select Image", null, SweetAlertDialog.WARNING_TYPE);

            } else {

                if (mode.equals("add")) {
                    ShopModel shopModel = new ShopModel(wName, "", sNumber, "",
                            "1", sName, encodedString);
                    AddShop(shopModel);
                } else {
                    ShopModel shopModel = new ShopModel(getShopModel.getShop_id(),getShopModel.getUser_id(),wName,
                            "", sNumber, "","1", sName, encodedString);
                    updateShop(shopModel);
                }
            }
        });
    }


    void AddShop(ShopModel shopModel) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponceModel> addShopCall = apiService.addShopCall(shopModel);

        addShopCall.enqueue(new Callback<ResponceModel>() {
            @Override
            public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {

                if (response.body().getSuccess() == 1) {
                    finish();
                } else {
                    AppUtils.showAlert(mContext, "Error", response.body().getMsg(), SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<ResponceModel> call, Throwable t) {
                Log.e("SHOP","getLocalizedMessage()="+t.getLocalizedMessage());
                AppUtils.showAlert(mContext, "Server Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);

            }
        });
    }


    void updateShop(ShopModel shopModel)
    {

        Log.e("SHOP","SHOP="+new Gson().toJson(shopModel));

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponceModel> updateShopCall = apiService.updateShopCall(shopModel);

        updateShopCall.enqueue(new Callback<ResponceModel>() {
            @Override
            public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
                Log.e("SHOP","ResponceModel="+response.body().toString());
                if (response.body().getSuccess() == 1) {
                    finish();
                } else {
                    AppUtils.showAlert(mContext, "Error", response.body().getMsg(), SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<ResponceModel> call, Throwable t) {
                AppUtils.showAlert(mContext, "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

            if(image!=null) {
                Glide.with(this)
                        .load(image.getPath())
                        .centerCrop()
                        .into(mShopBanner);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    void convertImageIntoBase64() {
        BitmapDrawable drawable = (BitmapDrawable) mShopBanner.getDrawable();
        if(drawable!=null) {
            Bitmap bm = drawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedString = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
