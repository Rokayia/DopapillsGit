package com.example.dopapillsgitModel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.dopapillsgit.R;

public class TimeAlarm extends BroadcastReceiver {

    /********************************** Attribut de la classe*************************************/
    NotificationManager nm;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence from = "developpez.et";
        CharSequence message = "message de notif";
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
        Notification notif = new Notification();
        //  notif.setLatestEventInfo(context, from, message, contentIntent);
        nm.notify(1, notif);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("message de notif");
        notif = builder.build();
    }

}
