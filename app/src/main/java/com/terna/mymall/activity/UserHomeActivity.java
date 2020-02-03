package com.terna.mymall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.terna.mymall.R;
import com.terna.mymall.helper.ui.CTextView;
import com.terna.mymall.helper.SessionHelper;
import com.terna.mymall.model.ProductModel;
import com.terna.mymall.room.AppDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class UserHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private AppBarConfiguration mAppBarConfiguration;
    Context mContext=this;
    NavController navController;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View viewHeader = navigationView.getHeaderView(0);
        CTextView name = viewHeader.findViewById(R.id.header_name);
        name.setText("Name: "+AppDatabase.getInstance(this).UserDAO().getUserData().getFull_name());
        CTextView email = viewHeader.findViewById(R.id.header_email);
        email.setText("Email Id: "+AppDatabase.getInstance(this).UserDAO().getUserData().getEmail_id());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_take_later)
                .setDrawerLayout(drawer)
                .build();


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_settings)
        {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Logout")
                    .setContentText("Are you sure?")
                    .setConfirmText("Yes")
                    .setCancelText("No")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                            AppDatabase.getInstance(mContext).UserDAO().deleteTask(
                                    AppDatabase.getInstance(mContext).UserDAO().getUserData()
                            );

                            SessionHelper.getInstance(mContext).setLoginFlag(0);
                            startActivity(new Intent(mContext, LoginActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
            {
                navController.navigate(R.id.nav_home);
                drawer.closeDrawers();
            }return true;

            case R.id.nav_gallery:
            {
                ArrayList<ProductModel> productModels1=new ArrayList<>();
                List<ProductModel> productModelList= AppDatabase.getInstance(this).AddToCartDAO().getCartItem();

                if(productModelList.size()!=0)
                productModels1.addAll(productModelList);

                startActivity(new Intent(mContext, PaymentActivity.class).putExtra("DATA",productModels1)
                        .putExtra("MODE","MY CART"));
                drawer.closeDrawers();
            }return true;

            case R.id.nav_slideshow:
            {
                navController.navigate(R.id.nav_slideshow);
                drawer.closeDrawers();
            }
            return true;

            case R.id.nav_take_later:
            {
                navController.navigate(R.id.nav_take_later);
                drawer.closeDrawers();
            }
            return true;

        }
        return false;
    }
}
