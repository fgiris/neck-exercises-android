package com.ceng.muhendis.muneckexercises.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ceng.muhendis.muneckexercises.Alarm;
import com.ceng.muhendis.muneckexercises.Keys;
import com.ceng.muhendis.muneckexercises.LoginActivity;
import com.ceng.muhendis.muneckexercises.R;

import java.util.Calendar;

public class AlarmNotificationService extends IntentService {

    public AlarmNotificationService() {
        super("AlarmNotificationService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public AlarmNotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Alarm alarm = new Alarm();
        alarm.setAlarm(this);
        /*Log.d("AlarmNotification","Recieved");
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

        Context context = getApplicationContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setContentTitle("Notification Service Test");
        mBuilder.setContentText(context.getString(R.string.project_id));
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setSound(uri);
        mBuilder.setAutoCancel(true);

        Intent notificationIntent = new Intent(this, LoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(2, mBuilder.build());*/
    }


}
