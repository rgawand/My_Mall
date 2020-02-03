package com.terna.mymall.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.terna.mymall.model.ProductModel;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AppUtils {

    static SweetAlertDialog pDialog;

    public static void showProgress(Context mContext, String msg) {
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void closeProgress() {
        pDialog.dismissWithAnimation();
    }

    public static void showAlert(Context mContext, String title, String msg, int SweetAlertDialogType) {
        new SweetAlertDialog(mContext, SweetAlertDialogType)
                .setTitleText(title)
                .setContentText(msg)
                .setConfirmText("Ok")
                .show();
    }

    public static void addReminder(Context mContext, ArrayList<ProductModel> productModels,
                                   int statrYear,
                                   int startMonth, int startDay, int startHour, int startMinut
    ) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(statrYear, (startMonth - 1), startDay, startHour, startMinut, 00);
        long startMillis = beginTime.getTimeInMillis();


        Log.e("Reminder", "Reminder=" + statrYear + "_" +
                startMonth + "_" + startDay + "_" + startHour + "_" + startMinut);


        Log.e("Reminder", "productModels.size=" + productModels.size());


        //getting the alarm manager
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(mContext, MyAlarm.class);
        i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        i.putExtra("MODE", "PAYMENT");
        i.putExtra("DATA", productModels);
        i.putExtra("Reminder", "Reminder=" + statrYear + "_" +
                startMonth + "_" + startDay + "_" + startHour + "_" + startMinut);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);


        if (am != null) {
            am.set(AlarmManager.RTC_WAKEUP, startMillis, pi);
        }

        Toast.makeText(mContext, "Alarm is set", Toast.LENGTH_SHORT).show();


    }
    public static String getCurrentDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String ChangeFormat(int number)
    {
        return String.format("%02d",number);
    }
}
