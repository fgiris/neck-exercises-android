package com.ceng.muhendis.muneckexercises;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.ceng.muhendis.muneckexercises.model.SettingsFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.UserFirebaseDb;
import com.ceng.muhendis.muneckexercises.services.AlarmNotificationService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by muhendis on 1.04.2018.
 */

public class Alarm extends BroadcastReceiver {
    private final String TAG ="MYALARM";
    public static boolean isFirst=true;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        FirebaseApp.initializeApp(context);
        if (intent.getAction()!= null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if(isEnglish(context)){
                setAlarmsEnglish(context);
            }
            else{
                setAlarms(context);
            }
        }

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.marmara_logo);

        if(timeOfDay >= 0 && timeOfDay < 8){
        }
        else{
            String message = intent.getStringExtra(Keys.ALARM_MESSAGE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String notificationBody = message;
                if(message==null)
                     notificationBody = "Egzersiz vaktin geldi. Egzersiz yapmaya ne dersin?";

                CharSequence name = "my_channel_01";
                String description = "my_channel_01_desc";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("8888", name, importance);
                channel.setDescription(description);
                //channel.enableLights(true);
                //Sets the notification light color for notifications posted to this channel, if the device supports this feature.
                //channel.setLightColor(Color.BLUE);

                //channel.setShowBadge(true);
                channel.setVibrationPattern(new long[]{1000,1000});
                channel.enableVibration(true);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "8888")
                        .setLargeIcon(icon)
                        .setBadgeIconType(R.drawable.marmara_logo)
                        .setSmallIcon(R.drawable.marmara_logo)
                        .setContentTitle("M.Ü. Neck Exercises")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText(notificationBody);


                Intent resultIntent = new Intent(context, ExercisesActivity.class);
                int _id = (int) System.currentTimeMillis();

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                context,
                                _id,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManagerCompat mNotificationManager =

                        NotificationManagerCompat.from(context);

                mNotificationManager.notify(001, mBuilder.build());
            }
            else{
                NotificationCompat.Builder mBuilder;
                if(message!=null)
                {
                    mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.marmara_logo)
                            .setContentTitle("M.Ü. Neck Exercises")
                            .setSound(alarmSound)
                            .setVibrate(new long[] { 1000, 1000})
                            .setContentText(message);
                }
                else
                {
                    mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.marmara_logo)
                            .setContentTitle("M.Ü. Neck Exercises")
                            .setSound(alarmSound)
                            .setVibrate(new long[] { 1000, 1000})
                            .setContentText("Egzersiz vaktin geldi. Egzersiz yapmaya ne dersin?");

                }

                Intent resultIntent = new Intent(context, LoginActivity.class);
                int _id = (int) System.currentTimeMillis();
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                context,
                                _id,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =

                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                mNotificationManager.notify(001, mBuilder.build());
            }

        }


    }

    public void setAlarms(Context context){

        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Günaydın\uD83D\uDE0A güne hareket katarak başlamaya ne dersin?");

        PendingIntent pi = PendingIntent.getBroadcast(context, 815, i, PendingIntent.FLAG_NO_CREATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 815, i,0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ////////////////
        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Güne omurganın düzgünlüğünü sağlayarak başlamaya ne dersin?");

        pi = PendingIntent.getBroadcast(context, 945, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 945, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Hadi omurganın dikliğini hisset, tek tek omurlarını hisset, başının üzerinde bir şey varmış gibi dik dur.");

        pi = PendingIntent.getBroadcast(context, 1115, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1115, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

        pi = PendingIntent.getBroadcast(context, 1245, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1245, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt. Kendini iyi hisset!");

        pi = PendingIntent.getBroadcast(context, 1415, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1415, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt ve vücudundaki gerginlikleri azalt!");

        pi = PendingIntent.getBroadcast(context, 1545, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1545, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Başın önde mi duruyor, dikkat et duruşunu düzeltelim. ");

        pi = PendingIntent.getBroadcast(context, 1715, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1715, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Uzun süredir telefona mı bakıyorsun? Bir ara vermeye ne dersin?");

        pi = PendingIntent.getBroadcast(context, 1845, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1845, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Uzun süredir başın önde mi duruyor, egzersiz yapmaya ne dersin?");

        pi = PendingIntent.getBroadcast(context, 2015, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 2015, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

        pi = PendingIntent.getBroadcast(context, 2145, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 2145, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }



        setAlarm(context);

    }
    public void setAlarmsEnglish(Context context){


        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Good Morning\uD83D\uDE0A How about starting the day by adding movement?");

        PendingIntent pi = PendingIntent.getBroadcast(context, 815, i, PendingIntent.FLAG_NO_CREATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 815, i,0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ////////////////
        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"What do you say to start the day by providing the  smoothness  of the spine?");

        pi = PendingIntent.getBroadcast(context, 945, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 945, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Come on feel the stiffness of the spine,  feel the individual vertebrae, stand up as if something is on your head.");

        pi = PendingIntent.getBroadcast(context, 1115, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1115, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Protect your posture!");

        pi = PendingIntent.getBroadcast(context, 1245, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1245, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Fix the posture. Feel good!");

        pi = PendingIntent.getBroadcast(context, 1415, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1415, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Fix the posture and reduce the tension in your body!");

        pi = PendingIntent.getBroadcast(context, 1545, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1545, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Is standing in front of the head, let's fix the attention posture.");

        pi = PendingIntent.getBroadcast(context, 1715, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1715, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Are you looking at the phone for a long time?");

        pi = PendingIntent.getBroadcast(context, 1845, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1845, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Is it long in front of the head, how about exercising?");

        pi = PendingIntent.getBroadcast(context, 2015, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 2015, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Protect your posture!");

        pi = PendingIntent.getBroadcast(context, 2145, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 45);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 2145, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }



        setAlarmEnglish(context);

    }

    public void setAlarm(final Context context)
    {

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("token").equalTo(FirebaseInstanceId.getInstance().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference().child("settings").orderByChild("pid").equalTo(issue.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null)
                                {
                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {
                                        SettingsFirebaseDb settings = issue.getValue(SettingsFirebaseDb.class);
                                        for(Map.Entry<String, Boolean> entry : settings.getNotificationDay().entrySet()) {
                                            String keyDay = entry.getKey().substring(2);
                                            boolean valueDay = entry.getValue();

                                            for(Map.Entry<String, Boolean> entryTime : settings.getNotificationTime().entrySet()){
                                                String keyTime = entryTime.getKey().substring(2);
                                                boolean valueTime = entryTime.getValue();
                                                AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                                final Intent i = new Intent(context, Alarm.class);

                                                PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, PendingIntent.FLAG_NO_CREATE);
                                                //PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, 0);


                                                if(valueDay && valueTime)
                                                {


                                                    Log.d("ALARM_MANAGER","KeyDay: "+keyDay+"---KeyDayEntry:"+entryTime.getKey()+"----keyTime: "+keyTime);
                                                    // Set the alarm to start at approximately 2:00 p.m.
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                                    calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(keyDay)+1);
                                                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(keyTime));
                                                    calendar.set(Calendar.MINUTE, 00);
                                                    calendar.set(Calendar.SECOND, 00);

                                                    Log.d("ALARM_MANAGER","Calendar Time: "+calendar.getTime());


                                                    // With setInexactRepeating(), you have to use one of the AlarmManager interval
                                                    // constants--in this case, AlarmManager.INTERVAL_DAY.
                                                    pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, PendingIntent.FLAG_NO_CREATE);
                                                    if(pi==null){
                                                        Log.d("ALARM_MANAGER_LOG","SETTING ALARM: "+keyDay+keyTime);
                                                        pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, 0);

                                                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                                                AlarmManager.INTERVAL_DAY*7, pi);
                                                    }
                                                    else{
                                                        Log.d("ALARM_MANAGER_LOG","ALARM ALREADY EXISTS: "+keyDay+keyTime);

                                                    }

                                                }
                                                else{
                                                    if(pi!=null){
                                                        Log.d("ALARM_MANAGER_LOG","CANCELING ALARM"+keyDay+keyTime);

                                                        am.cancel(pi);
                                                        pi.cancel();
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 , pi); // Millisec * Second * Minute
    }

    public void setAlarmEnglish(final Context context)
    {

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("token").equalTo(FirebaseInstanceId.getInstance().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference().child("settings").orderByChild("pid").equalTo(issue.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null)
                                {
                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {
                                        SettingsFirebaseDb settings = issue.getValue(SettingsFirebaseDb.class);
                                        for(Map.Entry<String, Boolean> entry : settings.getNotificationDay().entrySet()) {
                                            String keyDay = entry.getKey().substring(2);
                                            boolean valueDay = entry.getValue();

                                            for(Map.Entry<String, Boolean> entryTime : settings.getNotificationTime().entrySet()){
                                                String keyTime = entryTime.getKey().substring(2);
                                                boolean valueTime = entryTime.getValue();
                                                AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                                final Intent i = new Intent(context, Alarm.class);
                                                i.putExtra(Keys.ALARM_MESSAGE,"It's time to make exercise. How about to start exercises?");

                                                PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, PendingIntent.FLAG_NO_CREATE);
                                                //PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, 0);


                                                if(valueDay && valueTime)
                                                {


                                                    Log.d("ALARM_MANAGER","KeyDay: "+keyDay+"---KeyDayEntry:"+entryTime.getKey()+"----keyTime: "+keyTime);
                                                    // Set the alarm to start at approximately 2:00 p.m.
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                                    calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(keyDay)+1);
                                                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(keyTime));
                                                    calendar.set(Calendar.MINUTE, 00);
                                                    calendar.set(Calendar.SECOND, 00);

                                                    Log.d("ALARM_MANAGER","Calendar Time: "+calendar.getTime());


                                                    // With setInexactRepeating(), you have to use one of the AlarmManager interval
                                                    // constants--in this case, AlarmManager.INTERVAL_DAY.
                                                    pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, PendingIntent.FLAG_NO_CREATE);
                                                    if(pi==null){
                                                        Log.d("ALARM_MANAGER LOG","SETTING: "+keyDay+keyTime);
                                                        pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, 0);

                                                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                                                AlarmManager.INTERVAL_DAY*7, pi);
                                                    }

                                                }
                                                else{
                                                    if(pi!=null){
                                                        am.cancel(pi);
                                                        pi.cancel();
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 , pi); // Millisec * Second * Minute
    }

    public void updateAlarmTurkish(final Context context)
    {

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("token").equalTo(FirebaseInstanceId.getInstance().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference().child("settings").orderByChild("pid").equalTo(issue.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null)
                                {
                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {
                                        SettingsFirebaseDb settings = issue.getValue(SettingsFirebaseDb.class);
                                        for(Map.Entry<String, Boolean> entry : settings.getNotificationDay().entrySet()) {
                                            String keyDay = entry.getKey().substring(2);
                                            boolean valueDay = entry.getValue();

                                            for(Map.Entry<String, Boolean> entryTime : settings.getNotificationTime().entrySet()){
                                                String keyTime = entryTime.getKey().substring(2);
                                                boolean valueTime = entryTime.getValue();
                                                AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                                final Intent i = new Intent(context, Alarm.class);

                                                PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, PendingIntent.FLAG_NO_CREATE);
                                                //PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, 0);


                                                if(valueDay && valueTime)
                                                {


                                                    Log.d("ALARM_MANAGER","KeyDay: "+keyDay+"---KeyDayEntry:"+entryTime.getKey()+"----keyTime: "+keyTime);
                                                    // Set the alarm to start at approximately 2:00 p.m.
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                                    calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(keyDay)+1);
                                                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(keyTime));
                                                    calendar.set(Calendar.MINUTE, 00);
                                                    calendar.set(Calendar.SECOND, 00);

                                                    Log.d("ALARM_MANAGER","Calendar Time: "+calendar.getTime());


                                                    // With setInexactRepeating(), you have to use one of the AlarmManager interval
                                                    // constants--in this case, AlarmManager.INTERVAL_DAY.
                                                    pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                                            AlarmManager.INTERVAL_DAY*7, pi);

                                                }
                                                else{
                                                    if(pi!=null){
                                                        am.cancel(pi);
                                                        pi.cancel();
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 , pi); // Millisec * Second * Minute
    }

    public void updateAlarmEnglish(final Context context)
    {

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("token").equalTo(FirebaseInstanceId.getInstance().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference().child("settings").orderByChild("pid").equalTo(issue.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null)
                                {
                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {
                                        SettingsFirebaseDb settings = issue.getValue(SettingsFirebaseDb.class);
                                        for(Map.Entry<String, Boolean> entry : settings.getNotificationDay().entrySet()) {
                                            String keyDay = entry.getKey().substring(2);
                                            boolean valueDay = entry.getValue();

                                            for(Map.Entry<String, Boolean> entryTime : settings.getNotificationTime().entrySet()){
                                                String keyTime = entryTime.getKey().substring(2);
                                                boolean valueTime = entryTime.getValue();
                                                AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                                final Intent i = new Intent(context, Alarm.class);
                                                i.putExtra(Keys.ALARM_MESSAGE,"It's time to make exercise. How about to start exercises?");


                                                PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, PendingIntent.FLAG_NO_CREATE);
                                                //PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, 0);


                                                if(valueDay && valueTime)
                                                {


                                                    Log.d("ALARM_MANAGER","KeyDay: "+keyDay+"---KeyDayEntry:"+entryTime.getKey()+"----keyTime: "+keyTime);
                                                    // Set the alarm to start at approximately 2:00 p.m.
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                                    calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(keyDay)+1);
                                                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(keyTime));
                                                    calendar.set(Calendar.MINUTE, 00);
                                                    calendar.set(Calendar.SECOND, 00);

                                                    Log.d("ALARM_MANAGER","Calendar Time: "+calendar.getTime());


                                                    // With setInexactRepeating(), you have to use one of the AlarmManager interval
                                                    // constants--in this case, AlarmManager.INTERVAL_DAY.

                                                    pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                                            AlarmManager.INTERVAL_DAY*7, pi);

                                                }
                                                else{
                                                    if(pi!=null){
                                                        am.cancel(pi);
                                                        pi.cancel();
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 , pi); // Millisec * Second * Minute
    }

    public boolean isEnglish(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.saved_user_file_key),Context.MODE_PRIVATE);
        boolean isEnglish = sharedPref.getBoolean(context.getString(R.string.saved_user_isLanguageEnglish_key), false);
        return isEnglish;
    }

    public void cancelAlarm(final Context context)
    {


        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("token").equalTo(FirebaseInstanceId.getInstance().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        FirebaseDatabase.getInstance().getReference().child("settings").orderByChild("pid").equalTo(issue.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null)
                                {
                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {
                                        SettingsFirebaseDb settings = issue.getValue(SettingsFirebaseDb.class);
                                        for(Map.Entry<String, Boolean> entry : settings.getNotificationDay().entrySet()) {
                                            String keyDay = entry.getKey().substring(2);
                                            boolean valueDay = entry.getValue();
                                            if(valueDay)
                                            {
                                                for(Map.Entry<String, Boolean> entryTime : settings.getNotificationTime().entrySet()){
                                                    String keyTime = entryTime.getKey().substring(2);
                                                    boolean valueTime = entryTime.getValue();
                                                    if(valueTime)
                                                    {
                                                        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                                        final Intent i = new Intent(context, Alarm.class);
                                                        PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(keyDay+keyTime), i, 0);
                                                        //PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, 0);


                                                        // With setInexactRepeating(), you have to use one of the AlarmManager interval
                                                        // constants--in this case, AlarmManager.INTERVAL_DAY.
                                                        am.cancel(pi);
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
