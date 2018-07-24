package com.ceng.muhendis.muneckexercises.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.ceng.muhendis.muneckexercises.Alarm;
import com.ceng.muhendis.muneckexercises.Keys;
import com.ceng.muhendis.muneckexercises.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;
import java.util.HashMap;

public class NotificationService extends Service {
    final String TAG ="NotificationService";
    public NotificationService() {
    }

    public static Alarm alarm;
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent.getBooleanExtra(Keys.SERVICE_MESSAGE_IS_LANGUAGE_CHANGING,false)){
            if(intent.getBooleanExtra(Keys.SERVICE_MESSAGE_IS_ENGLISH,false)){
                updateAlarmsEnglish();
            }
            else{
                updateAlarmsTurkish();
            }
        }
        else{
            if(intent.getBooleanExtra(Keys.SERVICE_MESSAGE_IS_ENGLISH,false)){
                setAlarmsEnglish();
            }
            else{
                setAlarms();
            }
        }
        Alarm.isFirst = false;


        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        if(intent.getBooleanExtra(Keys.SERVICE_MESSAGE_IS_LANGUAGE_CHANGING,false)){
            if(intent.getBooleanExtra(Keys.SERVICE_MESSAGE_IS_ENGLISH,false)){
                updateAlarmsEnglish();
            }
            else{
                updateAlarmsTurkish();
            }
        }
        else{
            if(intent.getBooleanExtra(Keys.SERVICE_MESSAGE_IS_ENGLISH,false)){
                setAlarmsEnglish();
            }
            else{
                setAlarms();
            }
        }

        Alarm.isFirst = false;


    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    /*@Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d(TAG,"SERVICE STARTED");
        //start a separate thread and start listening to your network object
        new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("users").orderByChild("token").equalTo(FirebaseInstanceId.getInstance().getToken()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null)
                        {
                            for (DataSnapshot issue :
                                    dataSnapshot.getChildren()) {
                                FirebaseDatabase.getInstance().getReference().child("settings").orderByChild("pid").equalTo(issue.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d(TAG,"NOTIFICATION FIREBASE UPDATED");
                                        FirebaseDatabase.getInstance().getReference().child("settings").setValue(new Integer(1));
                                        Intent restartService = new Intent(getApplicationContext(),
                                                this.getClass());
                                        restartService.setPackage(getPackageName());
                                        PendingIntent restartServicePI = PendingIntent.getService(
                                                getApplicationContext(), 1, restartService,
                                                PendingIntent.FLAG_ONE_SHOT);

                                        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);
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
        }.run();
    }*/


    public void updateAlarmsTurkish(){
        Context context = getApplicationContext();
        alarm = new Alarm();
        alarm.setAlarm(context);

        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Günaydın");

        PendingIntent pi = PendingIntent.getBroadcast(context, 815, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);


        ////////////////
        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Güne omurganın düzgünlüğünü sağlayarak başlamaya ne dersin?");

        pi = PendingIntent.getBroadcast(context, 1015, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Hadi omurganın dikliğini hisset, tek tek omurlarını hisset, başının üzerinde bir şey varmış gibi dik dur.");

        pi = PendingIntent.getBroadcast(context, 1215, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

        pi = PendingIntent.getBroadcast(context, 1415, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt. Kendini iyi hisset!");

        pi = PendingIntent.getBroadcast(context, 1615, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt ve vücudundaki gerginlikleri azalt!");

        pi = PendingIntent.getBroadcast(context, 1815, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

        pi = PendingIntent.getBroadcast(context, 2015, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);



        alarm.updateAlarmTurkish(getApplicationContext());

    }
    public void updateAlarmsEnglish(){
        Context context = getApplicationContext();
        alarm = new Alarm();
        alarm.setAlarm(context);

        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Good morning");

        PendingIntent pi = PendingIntent.getBroadcast(context, 815, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);


        ////////////////
        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"What do you say to start the day by providing the  smoothness  of the spine?");

        pi = PendingIntent.getBroadcast(context, 1015, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Come on feel the stiffness of the spine,  feel the individual vertebrae, stand up as if something is on your head.");

        pi = PendingIntent.getBroadcast(context, 1215, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Protect your posture! ");

        pi = PendingIntent.getBroadcast(context, 1415, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Fix the posture. Feel good!");

        pi = PendingIntent.getBroadcast(context, 1615, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Fix the posture and reduce the tension in your body!");

        pi = PendingIntent.getBroadcast(context, 1815, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Protect your posture!");

        pi = PendingIntent.getBroadcast(context, 2015, i, PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 15);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);



        alarm.updateAlarmEnglish(getApplicationContext());


    }
    public void setAlarms(){
        Context context = getApplicationContext();
        alarm = new Alarm();
        alarm.setAlarm(context);

        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Günaydın");

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

        pi = PendingIntent.getBroadcast(context, 1015, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1015, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Hadi omurganın dikliğini hisset, tek tek omurlarını hisset, başının üzerinde bir şey varmış gibi dik dur.");

        pi = PendingIntent.getBroadcast(context, 1215, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1215, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

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
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt. Kendini iyi hisset!");

        pi = PendingIntent.getBroadcast(context, 1615, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1615, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt ve vücudundaki gerginlikleri azalt!");

        pi = PendingIntent.getBroadcast(context, 1815, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1815, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

        pi = PendingIntent.getBroadcast(context, 2015, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 2015, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }



        alarm.setAlarm(getApplicationContext());

    }
    public void setAlarmsEnglish(){
        Context context = getApplicationContext();
        alarm = new Alarm();
        alarm.setAlarm(context);

        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Good morning");

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

        pi = PendingIntent.getBroadcast(context, 1015, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1015, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Come on feel the stiffness of the spine,  feel the individual vertebrae, stand up as if something is on your head.");

        pi = PendingIntent.getBroadcast(context, 1215, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1215, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }


        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Protect your posture!");

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
        i.putExtra(Keys.ALARM_MESSAGE,"Fix the posture. Feel good!");

        pi = PendingIntent.getBroadcast(context, 1615, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1615, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Fix the posture and reduce the tension in your body!");

        pi = PendingIntent.getBroadcast(context, 1815, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 1815, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        ///////////////

        i = new Intent(context, Alarm.class);
        i.putExtra(Keys.ALARM_MESSAGE,"Protect your posture!");

        pi = PendingIntent.getBroadcast(context, 2015, i, PendingIntent.FLAG_NO_CREATE);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 15);

        if(pi==null){
            pi = PendingIntent.getBroadcast(context, 2015, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }



        alarm.setAlarmEnglish(getApplicationContext());

    }
}
