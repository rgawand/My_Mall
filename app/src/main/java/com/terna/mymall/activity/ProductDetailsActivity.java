package com.terna.mymall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.terna.mymall.R;
import com.terna.mymall.helper.SessionHelper;
import com.terna.mymall.helper.ui.CButton;
import com.terna.mymall.helper.ui.CTextView;
import com.terna.mymall.model.ProductModel;
import com.terna.mymall.room.AppDatabase;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.terna.mymall.helper.URLS.BASE_URL;

public class ProductDetailsActivity extends AppCompatActivity {

    ProductModel productModel;
    private SliderView mProductImageSlider;
    private CTextView mPdPrice;
    private CTextView mPdOffer;
    private CTextView mPdDescription;
    private CButton mPdAddToCart;
    private CButton mPdBuyNow;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productModel = getIntent().getParcelableExtra("DATA");


        mProductImageSlider = findViewById(R.id.product_image_slider);
        mPdPrice = findViewById(R.id.pd_price);
        mPdOffer = findViewById(R.id.pd_offer);
        mPdDescription = findViewById(R.id.pd_description);
        mPdAddToCart = findViewById(R.id.pd_add_to_cart);
        mPdBuyNow = findViewById(R.id.pd_buy_now);

        final SliderAdapterExample adapter = new SliderAdapterExample(this);

        mProductImageSlider.setSliderAdapter(adapter);


        mPdPrice.setText("Rs. " + productModel.getPrice());

        String offer = "No Offer";

        if (!productModel.getGet_more().isEmpty() && !productModel.getPersentage().isEmpty()) {
            offer = "Buy one get " + productModel.getGet_more() + " with " + productModel.getPersentage() + "% off";

        } else if (productModel.getGet_more().isEmpty() && !productModel.getPersentage().isEmpty()) {
            offer = productModel.getPersentage() + "% off";

        } else if (!productModel.getGet_more().isEmpty() && productModel.getPersentage().isEmpty()) {
            offer = "Buy one get " + productModel.getGet_more();
        }

        mPdOffer.setText(offer);

        mPdDescription.setText(productModel.getFull_des());


        mPdAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Add to Cart")
                        .setContentText("Are you sure you want to Add cart?")
                        .setConfirmText("Yes")
                        .setCancelText("Cancel")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismissWithAnimation();
                            try {
                                AppDatabase.getInstance(mContext).AddToCartDAO().addToCart(productModel);
                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        })
                        .show();
            }
        });

        mPdBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ProductModel> productModels1=new ArrayList<>();
                productModels1.add(productModel);

                startActivity(new Intent(mContext, PaymentActivity.class).putExtra("DATA",productModels1)
                .putExtra("MODE","PAYMENT"));
            }
        });
    }


    public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

        private Context context;

        public SliderAdapterExample(Context context) {
            this.context = context;
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

            switch (position) {
                case 0:
                    Glide.with(viewHolder.itemView)
                            .load(BASE_URL + productModel.getImg_1())
                            .into(viewHolder.imageViewBackground);
                    break;
                case 1:
                    Glide.with(viewHolder.itemView)
                            .load(BASE_URL + productModel.getImg_2())
                            .into(viewHolder.imageViewBackground);
                    break;
                case 2:
                    Glide.with(viewHolder.itemView)
                            .load(BASE_URL + productModel.getImg_3())
                            .into(viewHolder.imageViewBackground);
                    break;
                default:
                    Glide.with(viewHolder.itemView)
                            .load(BASE_URL + productModel.getImg_4())
                            .into(viewHolder.imageViewBackground);
                    break;

            }

        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return 4;
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                this.itemView = itemView;
            }
        }
    }
}
