package com.terna.mymall.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.terna.mymall.activity.PaymentActivity;
import com.terna.mymall.activity.ReminderActivity;
import com.terna.mymall.model.ProductModel;

import java.util.ArrayList;

public class MyAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Worker", "Worker start");

        // String status_id = intent.getStringExtra("status_id");
/*        String Reminder = intent.getStringExtra("MODE");
        Log.e("Worker","Worker Reminder="+Reminder);

        ArrayList<ProductModel>  productModels1 = intent.getParcelableArrayListExtra("DATA");
        Log.e("Reminder", "productModels1.size=" + productModels1.size());
        context.startActivity(new Intent(context, PaymentActivity.class)
                .putExtra("DATA",productModels1)
                .putExtra("MODE","PAYMENT").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));*/


        context.startActivity(new Intent(context, ReminderActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }



}

