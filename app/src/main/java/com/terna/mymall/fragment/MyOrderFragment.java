package com.terna.mymall.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.terna.mymall.R;
import com.terna.mymall.helper.AppUtils;
import com.terna.mymall.helper.ui.CTextView;
import com.terna.mymall.model.OrderModel;
import com.terna.mymall.model.OrderRequestModel;
import com.terna.mymall.model.OrderResponceModel;
import com.terna.mymall.retrofit.ApiClient;
import com.terna.mymall.retrofit.ApiInterface;
import com.terna.mymall.room.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.terna.mymall.helper.URLS.BASE_URL;

public class MyOrderFragment extends Fragment {
    private RecyclerView mRecyclerView;
    View root;
    List<OrderModel> rootData = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    void webCall() {
        AppUtils.showProgress(getActivity(), "Loading...");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<OrderResponceModel> getAllOrder = apiService.getAllOrder(new OrderRequestModel(AppDatabase.getInstance(getActivity()).UserDAO().getUserData().getUser_id()));

        getAllOrder.enqueue(new Callback<OrderResponceModel>() {
            @Override
            public void onResponse(Call<OrderResponceModel> call, Response<OrderResponceModel> response) {
                AppUtils.closeProgress();
                if (response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        rootData.clear();
                        for(int i=0;i<response.body().getData().size();i++)
                        {
                            if (response.body().getData().get(i).getTaken_type().equals("0")) {
                                rootData.add(response.body().getData().get(i));
                            }
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerView.setAdapter(new ProducrApdater());
                    } else {
                        AppUtils.showAlert(getActivity(), response.body().getMsg(), null, SweetAlertDialog.ERROR_TYPE);
                    }
                } else {
                    AppUtils.showAlert(getActivity(), "No Shops...", "Error", SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<OrderResponceModel> call, Throwable t) {
                AppUtils.closeProgress();
            }
        });
    }

    class ProducrApdater extends RecyclerView.Adapter<ProducrApdater.MyViewHolder> {

        @NonNull
        @Override
        public ProducrApdater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.my_order_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProducrApdater.MyViewHolder h, int position) {
            OrderModel orderModel = rootData.get(position);
                String url = BASE_URL + orderModel.getImg_1();
                Log.e("ORDERLIST", "url=" + url);

                Glide
                        .with(getActivity())
                        .load(url)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher)
                        .into(h.mShopImage);

                h.mShopName.setText(orderModel.getName());

                if (orderModel.getTaken_type().equals("1")) {  //Take Latter

                    h.taken_type.setText("Action Type: Take Later");
                    h.date_time.setText("Date & Time: " + orderModel.getDeliver_date());

                } else {   //Take Now

                    h.taken_type.setText("Action Type: Order Taken ");
                    h.date_time.setText("Date & Time: " + orderModel.getDeliver_date());
                }
        }

        @Override
        public int getItemCount() {
            return rootData != null ? rootData.size() : 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private AppCompatImageView mShopImage;
            private CTextView mShopName;
            private CTextView taken_type;
            private CTextView date_time;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                mShopImage = itemView.findViewById(R.id.shop_image);
                mShopName = itemView.findViewById(R.id.shop_name);
                taken_type = itemView.findViewById(R.id.taken_type);
                date_time = itemView.findViewById(R.id.date_time);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        webCall();
    }
}