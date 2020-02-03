package com.terna.mymall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.terna.mymall.R;
import com.terna.mymall.helper.AppUtils;
import com.terna.mymall.helper.ui.CButton;
import com.terna.mymall.helper.ui.CEditText;
import com.terna.mymall.helper.ui.CTextView;
import com.terna.mymall.model.ProductModel;
import com.terna.mymall.model.ResponceModel;
import com.terna.mymall.retrofit.ApiClient;
import com.terna.mymall.retrofit.ApiInterface;
import com.terna.mymall.room.AppDatabase;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.terna.mymall.helper.URLS.BASE_URL;

public class AddProductActivity extends AppCompatActivity {

    private CTextView mCTextView4;
    private AppCompatImageView mImage1;
    private AppCompatImageView mImage2;
    private AppCompatImageView mImage3;
    private AppCompatImageView mImage4;
    private CEditText mProductName;
    private CEditText mProductPrice;
    private CEditText mProductShortDescription;
    private CEditText mOfferPercent;
    private CEditText mOfferGetMore;
    private CEditText mProductFullDescription;
    private CButton mCButton;
    private int imageMode;
    String encodedString_1;
    String encodedString_2;
    String encodedString_3;
    String encodedString_4;
    Context mContext = this;

    ProductModel getModel;
    private AppCompatImageView selectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        mCTextView4 = findViewById(R.id.CTextView4);
        mImage1 = findViewById(R.id.image_1);
        mImage2 = findViewById(R.id.image_2);
        mImage3 = findViewById(R.id.image_3);
        mImage4 = findViewById(R.id.image_4);
        mProductName = findViewById(R.id.product_name);
        mProductPrice = findViewById(R.id.product_price);
        mProductShortDescription = findViewById(R.id.product_short_description);
        mOfferPercent = findViewById(R.id.offer_percent);
        mOfferGetMore = findViewById(R.id.offer_get_more);
        mProductFullDescription = findViewById(R.id.product_full_description);
        mCButton = findViewById(R.id.CButton);

        String mode = getIntent().getStringExtra("MODE");

        if (mode.equals("add")) {
            mCButton.setText("ADD PRODUCT");
            Log.e("PRODUCT", "Mode=add");
        } else {
            Log.e("PRODUCT", "Mode=update");
            getModel = getIntent().getParcelableExtra("DATA");

            mCButton.setText("UPDATE PRODUCT");

            mProductName.setText(getModel.getName());
            mProductPrice.setText(getModel.getPrice());
            mProductShortDescription.setText(getModel.getShort_des());
            mOfferPercent.setText(getModel.getPersentage());
            mOfferGetMore.setText(getModel.getGet_more());
            mProductFullDescription.setText(getModel.getFull_des());

            Glide
                    .with(mContext)
                    .load( BASE_URL + getModel.getImg_1())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(mImage1);

            Glide
                    .with(mContext)
                    .load( BASE_URL + getModel.getImg_2())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(mImage2);

            Glide
                    .with(mContext)
                    .load( BASE_URL + getModel.getImg_3())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(mImage3);

            Glide
                    .with(mContext)
                    .load( BASE_URL + getModel.getImg_4())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(mImage4);
        }

        mCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=mProductName.getText().toString();
                String price=mProductPrice.getText().toString();
                String s_des=mProductShortDescription.getText().toString();
                String percenr=mOfferPercent.getText().toString();
                String get_more=mOfferGetMore.getText().toString();
                String f_des=mProductFullDescription.getText().toString();
                convertImageIntoBase64();

                if(name.isEmpty())
                {
                    AppUtils.showAlert(mContext, "Enter Product Name", null, SweetAlertDialog.WARNING_TYPE);

                }else if(price.isEmpty())
                {
                    AppUtils.showAlert(mContext, "Enter Product Price", null, SweetAlertDialog.WARNING_TYPE);

                }else if(s_des.isEmpty())
                {
                    AppUtils.showAlert(mContext, "Enter Product Description", null, SweetAlertDialog.WARNING_TYPE);

                }else
                {
                    if (mode.equals("add")) {
                        ProductModel productModel=new ProductModel(encodedString_1,encodedString_2,encodedString_3,encodedString_4
                        ,name,price,s_des,percenr,get_more,f_des,"",
                                ""+AppDatabase.getInstance(mContext).UserDAO().getUserData().getUser_id());

                         AddProduct(productModel);
                    }else {
                        ProductModel productModel=new ProductModel(getModel.getProduct_id(),    encodedString_1,encodedString_2,encodedString_3,encodedString_4
                                ,name,price,s_des,percenr,get_more,f_des,"",
                                ""+AppDatabase.getInstance(mContext).UserDAO().getUserData().getUser_id());

                        UpdateProduct(productModel);
                    }

                }
            }
        });


        mImage1.setOnClickListener(view -> {
            imageMode = 1;
            selectView = mImage1;
            callImagePicker();
        });
        mImage2.setOnClickListener(view -> {
            imageMode = 2;
            selectView = mImage2;
            callImagePicker();
        });
        mImage3.setOnClickListener(view -> {
            imageMode = 3;
            selectView = mImage3;
            callImagePicker();
        });
        mImage4.setOnClickListener(view -> {
            imageMode = 4;
            selectView = mImage4;
            callImagePicker();
        });


    }

    void AddProduct(ProductModel productModel)
    {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponceModel> addProductCall=apiService.addProduct(productModel);

        addProductCall.enqueue(new Callback<ResponceModel>() {
            @Override
            public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {

                if(response.body().getSuccess()==1)
                {
                    finish();
                }else
                {
                    AppUtils.showAlert(mContext, "Error", response.body().getMsg(), SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<ResponceModel> call, Throwable t) {
                AppUtils.showAlert(mContext, "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);

            }
        });
    }

    void UpdateProduct(ProductModel productModel)
    {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponceModel> updateProductCall=apiService.updateProduct(productModel);

        updateProductCall.enqueue(new Callback<ResponceModel>() {
            @Override
            public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {

                if(response.body().getSuccess()==1)
                {
                    finish();
                }else
                {
                    AppUtils.showAlert(mContext, "Error", response.body().getMsg(), SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<ResponceModel> call, Throwable t) {
                AppUtils.showAlert(mContext, "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);

            }
        });
    }

    void callImagePicker() {
        ImagePicker.create(this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .includeVideo(true) // Show video on image picker
                .single() // single mode
                .limit(1) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .enableLog(false) // disabling log
                .start(); // start image picker activity with request code
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

            if (image != null) {
                Glide.with(this)
                        .load(image.getPath())
                        .centerCrop()
                        .into(selectView);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    void convertImageIntoBase64() {

        BitmapDrawable drawable1 = (BitmapDrawable) mImage1.getDrawable();
        BitmapDrawable drawable2 = (BitmapDrawable) mImage2.getDrawable();
        BitmapDrawable drawable3 = (BitmapDrawable) mImage3.getDrawable();
        BitmapDrawable drawable4 = (BitmapDrawable) mImage4.getDrawable();

        if (drawable1 != null) {
            Bitmap bm = drawable1.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedString_1 = Base64.encodeToString(b, Base64.DEFAULT);
        }

        if (drawable2 != null) {
            Bitmap bm = drawable2.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedString_2 = Base64.encodeToString(b, Base64.DEFAULT);
        }

        if (drawable3 != null) {
            Bitmap bm = drawable3.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedString_3 = Base64.encodeToString(b, Base64.DEFAULT);
        }

        if (drawable4 != null) {
            Bitmap bm = drawable4.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedString_4 = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
