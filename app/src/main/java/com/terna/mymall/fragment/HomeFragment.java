package com.terna.mymall.fragment;

import android.content.Intent;
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
import com.terna.mymall.activity.ViewProductByShopActivity;
import com.terna.mymall.helper.AppUtils;
import com.terna.mymall.helper.ui.CTextView;
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

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;

    List<ShopModel> shopModels;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    void webCall() {
        AppUtils.showProgress(getActivity(), "Loading...");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ShopResopnceModel> getallShop = apiService.getAllShopCall();

        getallShop.enqueue(new Callback<ShopResopnceModel>() {
            @Override
            public void onResponse(Call<ShopResopnceModel> call, Response<ShopResopnceModel> response) {
                AppUtils.closeProgress();
                if (response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        shopModels = response.body().getShopModels();
                        Log.e("ShopList", "ShopList=" + shopModels.size());
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerView.setAdapter(new ShopsAdapter());
                    } else {
                        AppUtils.showAlert(getActivity(), "No Shops...", null, SweetAlertDialog.ERROR_TYPE);
                    }
                } else {
                    AppUtils.showAlert(getActivity(), "No Shops...", "Error", SweetAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Call<ShopResopnceModel> call, Throwable t) {
                AppUtils.closeProgress();
                AppUtils.showAlert(getActivity(), "Error", t.getLocalizedMessage(), SweetAlertDialog.ERROR_TYPE);
            }
        });
    }

    class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.MyViewHolder> {

        @NonNull
        @Override
        public ShopsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ShopsAdapter.MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.user_shop_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ShopsAdapter.MyViewHolder h, int position) {

            h.mUsShopName.setText(shopModels.get(position).getName());

            String url = BASE_URL + shopModels.get(position).getImage();
            Log.e("ShopList", "url=" + url);

            Glide
                    .with(getActivity())
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .into(h.mUsShopImage);

            h.mUsShopImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(getActivity(), ViewProductByShopActivity.class)
                            .putExtra("ID",""+shopModels.get(position).getUser_id())
                            .putExtra("name",""+shopModels.get(position).getName())
                    );
                }
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


    }

    @Override
    public void onResume() {
        super.onResume();
        webCall();
    }
}