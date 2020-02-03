package com.terna.mymall.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.terna.mymall.R;
import com.terna.mymall.fragment.HomeFragment;
import com.terna.mymall.helper.AppUtils;
import com.terna.mymall.helper.SessionHelper;
import com.terna.mymall.helper.ui.CButton;
import com.terna.mymall.helper.ui.CTextView;
import com.terna.mymall.model.OrderRequestModel;
import com.terna.mymall.model.ProductModel;
import com.terna.mymall.model.ResponceModel;
import com.terna.mymall.retrofit.ApiClient;
import com.terna.mymall.retrofit.ApiInterface;
import com.terna.mymall.room.AppDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.terna.mymall.helper.AppUtils.ChangeFormat;
import static com.terna.mymall.helper.AppUtils.addReminder;
import static com.terna.mymall.helper.AppUtils.closeProgress;
import static com.terna.mymall.helper.AppUtils.getCurrentDateTime;
import static com.terna.mymall.helper.AppUtils.showProgress;
import static com.terna.mymall.helper.URLS.BASE_URL;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView mPaymentList;
    private CTextView mPShowPrice;
    private CButton mPBuyNow;
    List<ProductModel> productModelsList = new ArrayList<>();
    Context mContext = this;
    ArrayList<ProductModel> productModels1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPaymentList = findViewById(R.id.payment_list);
        mPShowPrice = findViewById(R.id.p_show_price);
        mPBuyNow = findViewById(R.id.p_buy_now);

        String mode = getIntent().getStringExtra("MODE");

        if (mode.equals("PAYMENT")) {
            getSupportActionBar().setTitle("PAYMENT");
            productModels1 = getIntent().getParcelableArrayListExtra("DATA");
            Log.e("PAYMENT", "PAYMENT MODE");
        } else {
            getSupportActionBar().setTitle("MY CART");
            productModels1 = getIntent().getParcelableArrayListExtra("DATA");
            Log.e("PAYMENT", "MY CART MODE");
        }


        long sum = 0;
        if (productModels1 != null && productModels1.size() != 0) {
            mPBuyNow.setAlpha(1);
            for (ProductModel productModel : productModels1) {
                productModelsList.add(productModel);

                sum = sum + Long.parseLong(productModel.getPrice());

            }

            mPShowPrice.setText("Rs. " + sum);
            mPaymentList.setLayoutManager(new LinearLayoutManager(mContext));
            mPaymentList.setAdapter(new ProductAdapter());

            long finalSum = sum;
            mPBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Action")
                            .setContentText("You want to buy all this product?")
                            .setCancelText("Take Latter")
                            .setConfirmText("Take Now")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    //Take Latter
                                    sDialog.cancel();


                                    final Calendar c = Calendar.getInstance();
                                    int mYear, mMonth, mDay, mHour, mMinute;
                                    mYear = c.get(Calendar.YEAR);
                                    mMonth = c.get(Calendar.MONTH);
                                    mDay = c.get(Calendar.DAY_OF_MONTH);
                                    mHour = c.get(Calendar.HOUR_OF_DAY);
                                    mMinute = c.get(Calendar.MINUTE);
                                    StringBuffer dString = new StringBuffer();
                                    final int[] statrYear = {0};
                                    final int[] startMonth = {0};
                                    final int[] startDay = {0};
                                    final int[] startHour = {0};
                                    final int[] startMinut = {0};

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(PaymentActivity.this,
                                            new DatePickerDialog.OnDateSetListener() {

                                                @Override
                                                public void onDateSet(DatePicker view, int year,
                                                                      int monthOfYear, int dayOfMonth) {
                                                    statrYear[0] = year;
                                                    startMonth[0] = (monthOfYear + 1);
                                                    startDay[0] = dayOfMonth;

                                                    dString.append(ChangeFormat(dayOfMonth) + "/" + ChangeFormat((monthOfYear + 1)) + "/" + year);
                                                    TimePickerDialog timePickerDialog = new TimePickerDialog(PaymentActivity.this,
                                                            new TimePickerDialog.OnTimeSetListener() {

                                                                @Override
                                                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                      int minute) {
                                                                    startHour[0] = hourOfDay;
                                                                    startMinut[0] = minute;

                                                                    dString.append(" " + ChangeFormat(hourOfDay) + ":" + ChangeFormat(minute));
                                                                    //txtTime.setText(hourOfDay + ":" + minute);
                                                                    Log.e("Payment", "Date Time" + dString.toString());
                                                                    ArrayList<OrderRequestModel> model = new ArrayList<>();
                                                                    for (int i = 0; i < productModels1.size(); i++) {
                                                                        model.add(new OrderRequestModel(
                                                                                Integer.parseInt(productModels1.get(i).getProduct_id()),
                                                                                AppDatabase.getInstance(mContext).UserDAO().getUserData().getUser_id(),
                                                                                "1",
                                                                                dString.toString(),
                                                                                getCurrentDateTime(),
                                                                                "" + finalSum
                                                                        ));
                                                                    }
                                                                    addReminder(mContext, productModels1, statrYear[0], startMonth[0],
                                                                            startDay[0], startHour[0], startMinut[0]);
                                                                    takeNow(model);
                                                                }
                                                            }, mHour, mMinute, true);
                                                    timePickerDialog.show();

                                                }
                                            }, mYear, mMonth, mDay);
                                    datePickerDialog.show();
                                    ;
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    //Take Now
                                    sweetAlertDialog.cancel();
                                    ArrayList<OrderRequestModel> model = new ArrayList<>();
                                    for (int i = 0; i < productModels1.size(); i++) {
                                        model.add(new OrderRequestModel(
                                                Integer.parseInt(productModels1.get(i).getProduct_id()),
                                                AppDatabase.getInstance(mContext).UserDAO().getUserData().getUser_id(),
                                                "0",
                                                getCurrentDateTime(),
                                                getCurrentDateTime(),
                                                "" + finalSum
                                        ));
                                    }
                                    takeNow(model);
                                }
                            })

                            .show();
                }
            });
        } else {
            Log.e("Reminder", "No Product");
        }
    }

    //take now

    void takeNow(ArrayList<OrderRequestModel> model) {
        showProgress(this, "Wait...");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponceModel> addOrder = apiService.addOrder(model);

        addOrder.enqueue(new Callback<ResponceModel>() {
            @Override
            public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
                closeProgress();

                if (response.body() != null && response.body().getSuccess() == 1) {
                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Order Status")
                            .setContentText("Order Place Successfully")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    finish();
                                }
                            })
                            .show();

                } else if (response.body() != null && response.body().getSuccess() == 0) {
                    AppUtils.showAlert(mContext, "Error", response.body().getMsg(), SweetAlertDialog.ERROR_TYPE);
                } else {
                    AppUtils.showAlert(mContext, "Error", null, SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<ResponceModel> call, Throwable t) {
                closeProgress();
                AppUtils.showAlert(mContext, "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);
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
        public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder h, int position) {

            h.mShopName.setText("Name: " + productModelsList.get(position).getName());

            String url = BASE_URL + productModelsList.get(position).getImg_1();
            Log.e("ShopList", "url=" + url);

            Glide
                    .with(mContext)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(h.mShopImage);


            h.mShopImage.setOnClickListener(v -> {

            });

            h.mProductPrice.setText("Price: Rs. " + productModelsList.get(position).getPrice());
            h.mProductShortDescription.setVisibility(View.GONE);

            h.mShopImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(mContext, ProductDetailsActivity.class).putExtra("DATA", productModelsList.get(position)));
                }
            });

        }


        @Override
        public int getItemCount() {
            return productModelsList != null ? productModelsList.size() : 0;
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
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }
}


