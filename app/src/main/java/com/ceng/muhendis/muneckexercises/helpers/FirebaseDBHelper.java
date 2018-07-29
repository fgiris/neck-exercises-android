package com.ceng.muhendis.muneckexercises.helpers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ceng.muhendis.muneckexercises.Alarm;
import com.ceng.muhendis.muneckexercises.ExercisesActivity;
import com.ceng.muhendis.muneckexercises.FirstSurveyActivity;
import com.ceng.muhendis.muneckexercises.Keys;
import com.ceng.muhendis.muneckexercises.LoginActivity;
import com.ceng.muhendis.muneckexercises.R;
import com.ceng.muhendis.muneckexercises.SettingsActivity;
import com.ceng.muhendis.muneckexercises.model.ExerciseFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.FinishedExerciseFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.MobileAppSatisfactionSurveyFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.MuscleSurveyFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.PainSurveyFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.SettingsFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.UsageStatisticsFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.UserFirebaseDb;
import com.ceng.muhendis.muneckexercises.services.NotificationService;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by muhendis on 29.03.2018.
 */

public class FirebaseDBHelper {

    FirebaseDatabase database;
    DatabaseReference myRef;
    Context context;
    final String TAG="FirebaseDBHelper";

    public FirebaseDBHelper(Context context) {

        this.database = FirebaseDatabase.getInstance();
        this.myRef = this.database.getReference();
        this.context = context;

    }

    public String getFirebaseToken()
    {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public void insertUser(final UserFirebaseDb user)
    {
        final DatabaseReference userReference = myRef.child("users");
        userReference.keepSynced(true);

        Query userQuery = userReference.orderByChild("email").equalTo(user.getEmail());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"Data Snapshot: "+dataSnapshot.getChildren());

                Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshotIterable.iterator();

                if(dataSnapshotIterator.hasNext())
                {
                    //CharSequence text = "Bu emaile kayıtlı kullanıcı bulunmaktadır!";
                    CharSequence text = "Bu emaile kayıtlı kullanıcı zaten bulunmaktadır. Lütfen giriş yapmayı deneyiniz";
                    if(FunctionsHelper.IsLanguageEnglish(context)){
                        text ="This email is already registered to the system";
                    }
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    Log.d(TAG,"KULLANICI EKLENEMEDİ EMAİL ZATEN VAR");
                }
                else
                {
                    /*Map<String,Object> userMap = new HashMap<>();
                    userMap.put(user.getEmail(),user);*/

                    String userId= myRef.child("users").push().getKey();
                    myRef.child("users").child(userId).setValue(user);


                    Log.d(TAG,"KULLANICI EKLENDİ");
                    CharSequence text = "Kullanıcı hesabınız başarı ile oluşturuldu";

                    if(FunctionsHelper.IsLanguageEnglish(context)){
                        text ="Your registration is successful";
                    }
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CharSequence text = "Veritabanı hatası! Error: "+databaseError.getMessage();
                if(FunctionsHelper.IsLanguageEnglish(context)){
                    text = "Database Error! Error Code: "+databaseError.getMessage();
                }
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void isUserLoggedIn(final LoginActivity loginActivity){

        final ProgressDialog pdLoading = new ProgressDialog(loginActivity);
        pdLoading.setCancelable(false);
        pdLoading.setMessage("Başlatılıyor");
        pdLoading.show();

        DatabaseReference userReference = myRef.child("users");
        Query userQuery = userReference.orderByChild("token").equalTo(getFirebaseToken());

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        UserFirebaseDb user = issue.getValue(UserFirebaseDb.class);
                        Date psd = null;
                        try {
                            psd = new SimpleDateFormat("dd-MM-yyyy").parse(user.getProgramStartDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(psd);
                        calendar.add(Calendar.DATE,42);


                        Log.d(TAG,"Exists User name: "+user.getName());
                        if(user.getIsLoggedIn())
                        {
                            Log.d(TAG,"Logged in");
                            Intent intent;
                            if((user.getIsFirstSurveyCompleted() && Calendar.getInstance().getTime().before(calendar.getTime())) || user.isLastSurveyCompleted() )
                            {
                                intent  = new Intent(loginActivity,ExercisesActivity.class);

                            }
                            else if(!user.isLastSurveyCompleted() && Calendar.getInstance().getTime().after(calendar.getTime()))
                            {
                                intent  = new Intent(loginActivity,FirstSurveyActivity.class);
                                intent.putExtra(Keys.IF_SIX_WEEKS_PASSED,true);
                            }
                            else
                            {
                                intent  = new Intent(loginActivity,FirstSurveyActivity.class);
                                intent.putExtra(Keys.IF_SIX_WEEKS_PASSED,false);
                            }

                            pdLoading.dismiss();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            loginActivity.finish();
                        }
                        else
                        {
                            pdLoading.dismiss();
                            Log.d(TAG,"NOT Logged in");
                        }



                    }
                }
                else{
                    pdLoading.dismiss();
                    Log.d(TAG,"NOT Exists");
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CharSequence text = "Veritabanı hatası! Error: "+databaseError.getMessage();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    public void checkLogin(String mail,String password,final  LoginActivity loginActivity){

        final DatabaseReference userReference = myRef.child("users");
        Query userQuery = userReference.orderByChild("email_password").equalTo(mail+"_"+password);

        final ProgressDialog pdLoading = new ProgressDialog(loginActivity);
        pdLoading.setCancelable(false);
        String message = "Giriş Yapılıyor";
        if(FunctionsHelper.IsLanguageEnglish(context)){
            message = "Logging in";
        }
        pdLoading.setMessage(message);
        pdLoading.show();

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        pdLoading.dismiss();
                        UserFirebaseDb user = issue.getValue(UserFirebaseDb.class);
                        Date psd = null;
                        try {
                            psd = new SimpleDateFormat("dd-MM-yyyy").parse(user.getProgramStartDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(psd);
                        calendar.add(Calendar.DATE,42);


                        user.setLoggedIn(true);
                        user.setToken(getFirebaseToken());
                        Map<String,Object> userMap = new HashMap<>();
                        userMap.put(issue.getKey(),user);
                        userReference.updateChildren(userMap);
                        Intent intent;

                        Log.d(TAG,"EDITING SHARED PREF");
                        SharedPreferences sharedPref = loginActivity.getSharedPreferences(loginActivity.getResources().getString(R.string.saved_user_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(loginActivity.getString(R.string.saved_user_isloggedin_key), true);
                        editor.putString(loginActivity.getString(R.string.saved_user_email_key), user.getEmail());
                        editor.putString(loginActivity.getString(R.string.saved_user_name_key), user.getName());
                        editor.putBoolean(loginActivity.getString(R.string.saved_user_isfirstsurveycompleted_key), user.getIsFirstSurveyCompleted());
                        editor.putBoolean(loginActivity.getString(R.string.saved_user_islastsurveycompleted_key), user.isLastSurveyCompleted());
                        editor.putString(loginActivity.getString(R.string.saved_user_programstartdate_key), user.getProgramStartDate());


                        editor.apply();

                        if(user.getIsFirstSurveyCompleted())
                        {
                            intent  = new Intent(loginActivity,ExercisesActivity.class);

                        }
                        else
                            intent  = new Intent(loginActivity,FirstSurveyActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        loginActivity.finish();
                        Log.d(TAG,"Exists User name: "+user.getName());

                    }
                }
                else{
                    pdLoading.dismiss();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            loginActivity);

                    String title = "HATALI EPOSTA VEYA PAROLA";
                    String message = "Lütfen bilgilerinizi kontrol edip tekrar deneyiniz.";
                    String buttonText = "Tamam";
                    if(FunctionsHelper.IsLanguageEnglish(context)){
                        title = "Invalid email or password";
                        message = "You entered wrong email or password. Please try again";
                        buttonText = "OK";
                    }
                    // set title
                    alertDialogBuilder.setTitle(title);
                    alertDialogBuilder.setIcon(R.drawable.invalid_cross);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(buttonText,new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity

                                }
                            })
                    ;

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                    Log.d(TAG,"NOT Exists");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CharSequence text = "Veritabanı hatası! Error: "+databaseError.getMessage();
                if(FunctionsHelper.IsLanguageEnglish(context)){
                    text = "Database Error! Error Code: "+databaseError.getMessage();
                }
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });


    }

    public void insertMuscleSurvey(final int questionNumber, final boolean isBeforeTreatment, final ArrayList<View> radioGroupList)
    {
        DatabaseReference userReference = myRef.child("users");
        Query userQuery = userReference.orderByChild("token").equalTo(getFirebaseToken());

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        DatabaseReference muscleSurveyReference = myRef.child("muscle_survey").push();
                        MuscleSurveyFirebaseDb muscleSurvey = new MuscleSurveyFirebaseDb(questionNumber,isBeforeTreatment,issue.getKey(),radioGroupList);
                        muscleSurveyReference.setValue(muscleSurvey);
                    }
                }
                else{
                    Log.d(TAG,"User could not be found to update MUSCLE SURVEY");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CharSequence text = "Veritabanı hatası! Error: "+databaseError.getMessage();
                if(FunctionsHelper.IsLanguageEnglish(context)){
                    text = "Database Error! Error Code: "+databaseError.getMessage();
                }
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    public void insertMobileSurvey(final int satisfactionLevel)
    {
        DatabaseReference userReference = myRef.child("users");
        Query userQuery = userReference.orderByChild("token").equalTo(getFirebaseToken());

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        DatabaseReference mobileSurveyRef = myRef.child("mobile_app_satisfaction_survey").push();
                        MobileAppSatisfactionSurveyFirebaseDb mobile = new MobileAppSatisfactionSurveyFirebaseDb(issue.getKey(),satisfactionLevel);
                        mobileSurveyRef.setValue(mobile);
                    }
                }
                else{
                    Log.d(TAG,"User could not be found to update MUSCLE SURVEY");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CharSequence text = "Veritabanı hatası! Error: "+databaseError.getMessage();
                if(FunctionsHelper.IsLanguageEnglish(context)){
                    text = "Database Error! Error Code: "+databaseError.getMessage();
                }
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    public void insertPainSurvey(final boolean isBeforeTreatment, final int painLevel, final FirstSurveyActivity firstSurveyActivity)
    {
        DatabaseReference userReference = myRef.child("users");
        Query userQuery = userReference.orderByChild("token").equalTo(getFirebaseToken());

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        DatabaseReference painSurveyReference = myRef.child("pain_survey").push();
                        PainSurveyFirebaseDb painSurvey = new PainSurveyFirebaseDb(issue.getKey(),painLevel,isBeforeTreatment);
                        painSurveyReference.setValue(painSurvey);
                        UserFirebaseDb user = issue.getValue(UserFirebaseDb.class);
                        if(isBeforeTreatment)
                            user.setFirstSurveyCompleted(true);
                        else
                            user.setLastSurveyCompleted(true);
                        Map<String,Object> userMap = new HashMap<>();
                        userMap.put(issue.getKey(),user);
                        myRef.child("users").updateChildren(userMap);

                        Map<String,Boolean> notificaionDays = new HashMap<>();
                        notificaionDays.put("id1",true);
                        notificaionDays.put("id2",false);
                        notificaionDays.put("id3",true);
                        notificaionDays.put("id4",false);
                        notificaionDays.put("id5",false);
                        notificaionDays.put("id6",true);
                        notificaionDays.put("id7",false);

                        Map<String,Boolean> notificationTimes = new HashMap<>();
                        for (int i=8;i<24;i++)
                        {
                            if(i==10 || i==14 ||i==19){
                                notificationTimes.put("id"+String.valueOf(i),true);
                            }
                            else
                                notificationTimes.put("id"+String.valueOf(i),false);
                        }
                        SettingsFirebaseDb settings = new SettingsFirebaseDb(issue.getKey(),notificaionDays,notificationTimes);
                        myRef.child("settings").push().setValue(settings);

                        Intent i = new Intent(context, NotificationService.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startService(i);
                        /*Thread t = new Thread(                        new Runnable() {
                            @Override
                            public void run() {
                                Alarm alarm = new Alarm();
                                alarm.setAlarm(context);

                                AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                Intent i = new Intent(context, Alarm.class);
                                i.putExtra(Keys.ALARM_MESSAGE,"Günaydın");

                                PendingIntent pi = PendingIntent.getBroadcast(context, 815, i, PendingIntent.FLAG_NO_CREATE);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.HOUR_OF_DAY, 8);
                                calendar.set(Calendar.MINUTE, 15);

                                if(pi!=null)
                                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, pi);

                                ////////////////
                                i = new Intent(context, Alarm.class);
                                i.putExtra(Keys.ALARM_MESSAGE,"Güne omurganın düzgünlüğünü sağlayarak başlamaya ne dersin?");

                                pi = PendingIntent.getBroadcast(context, 1015, i, PendingIntent.FLAG_NO_CREATE);
                                calendar.set(Calendar.HOUR_OF_DAY, 10);
                                calendar.set(Calendar.MINUTE, 15);


                                if(pi!=null)
                                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                            AlarmManager.INTERVAL_DAY, pi);

                                ///////////////

                                i = new Intent(context, Alarm.class);
                                i.putExtra(Keys.ALARM_MESSAGE,"Hadi omurganın dikliğini hisset, tek tek omurlarını hisset, başının üzerinde bir şey varmış gibi dik dur.");

                                pi = PendingIntent.getBroadcast(context, 1215, i, PendingIntent.FLAG_NO_CREATE);
                                calendar.set(Calendar.HOUR_OF_DAY, 12);
                                calendar.set(Calendar.MINUTE, 15);


                                if(pi!=null)
                                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                            AlarmManager.INTERVAL_DAY, pi);


                                ///////////////

                                i = new Intent(context, Alarm.class);
                                i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

                                pi = PendingIntent.getBroadcast(context, 1415, i, PendingIntent.FLAG_NO_CREATE);
                                calendar.set(Calendar.HOUR_OF_DAY, 14);
                                calendar.set(Calendar.MINUTE, 15);


                                if(pi!=null)
                                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                            AlarmManager.INTERVAL_DAY, pi);

                                ///////////////

                                i = new Intent(context, Alarm.class);
                                i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt. Kendini iyi hisset!");

                                pi = PendingIntent.getBroadcast(context, 1615, i, PendingIntent.FLAG_NO_CREATE);
                                calendar.set(Calendar.HOUR_OF_DAY, 16);
                                calendar.set(Calendar.MINUTE, 15);


                                if(pi!=null)
                                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                            AlarmManager.INTERVAL_DAY, pi);

                                ///////////////

                                i = new Intent(context, Alarm.class);
                                i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt ve vücudundaki gerginlikleri azalt!");

                                pi = PendingIntent.getBroadcast(context, 1815, i, PendingIntent.FLAG_NO_CREATE);
                                calendar.set(Calendar.HOUR_OF_DAY, 18);
                                calendar.set(Calendar.MINUTE, 15);


                                if(pi!=null)
                                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                            AlarmManager.INTERVAL_DAY, pi);

                                ///////////////

                                i = new Intent(context, Alarm.class);
                                i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

                                pi = PendingIntent.getBroadcast(context, 2015, i, PendingIntent.FLAG_NO_CREATE);
                                calendar.set(Calendar.HOUR_OF_DAY, 20);
                                calendar.set(Calendar.MINUTE, 15);


                                if(pi!=null)
                                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                            AlarmManager.INTERVAL_DAY, pi);


                            }
                        });

                        t.start();
                        */
                        Intent intent;
                        intent  = new Intent(firstSurveyActivity,SettingsActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        firstSurveyActivity.finish();
                        Log.d(TAG,"Exists User name: "+user.getName());

                    }
                }
                else{
                    Log.d(TAG,"User could not be found for PAIN SURVEY");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CharSequence text = "Veritabanı hatası! Error: "+databaseError.getMessage();
                if(FunctionsHelper.IsLanguageEnglish(context)){
                    text = "Database Error! Error Code: "+databaseError.getMessage();
                }
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });



    }

    public void syncNavigationBar(final ExercisesActivity activity, final NavigationView navigationView)
    {
        DatabaseReference userReference = myRef.child("users");
        Query userQuery = userReference.orderByChild("token").equalTo(getFirebaseToken());

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView navbarNameInitials = activity.findViewById(R.id.navbarNameInitials2);
                TextView navbarUserName = activity.findViewById(R.id.navbarUserNameSurname2);
                TextView navbarUserMail = activity.findViewById(R.id.navbarUserMail2);

                //TextView navbarNameInitials = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navbarNameInitials2);
                //TextView navbarUserName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navbarUserNameSurname2);
                //TextView navbarUserMail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navbarUserMail2);

                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        UserFirebaseDb user = issue.getValue(UserFirebaseDb.class);

                        navbarUserName.setText(user.getName());
                        navbarUserMail.setText(user.getEmail());

                        String [] nameArray = user.getName().split(" ");
                        if(nameArray.length>1)
                        {
                            if((nameArray[0]!="" || nameArray[0]!=null)&& (nameArray[nameArray.length-1]!="" || nameArray[nameArray.length-1]!=null))
                            {
                                navbarNameInitials.setText(nameArray[0].toUpperCase().charAt(0)+nameArray[nameArray.length-1].toUpperCase().charAt(0));
                            }
                            else
                            {
                                navbarNameInitials.setText("X");
                            }
                        }
                        else {
                            if(nameArray!=null)
                            {
                                if(nameArray[0]!="" || nameArray[0]!=null)
                                {
                                    navbarNameInitials.setText(nameArray[0].toUpperCase().charAt(0));
                                }
                                else
                                {
                                    navbarNameInitials.setText("X");
                                }
                            }
                        }


                    }
                }
                else{
                    navbarUserName.setText("Merhaba");
                    navbarUserMail.setText("");
                    navbarNameInitials.setText("X");
                    Log.d(TAG,"NOT Exists");
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CharSequence text = "Veritabanı hatası! Error: "+databaseError.getMessage();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    public void insertSettings(final Map<String,Boolean> timesMap, final Map<String,Boolean> daysMap){
        myRef.child("users").orderByChild("token").equalTo(getFirebaseToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        final SettingsFirebaseDb settings = new SettingsFirebaseDb(issue.getKey(),daysMap,timesMap);
                        myRef.child("settings").orderByChild("pid").equalTo(settings.getPid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null)
                                {

                                    //Alarm alarm = new Alarm();
                                    //alarm.cancelAlarm(context);

                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {

                                        Map<String,Object> settingsMap = new HashMap<>();
                                        settingsMap.put(issue.getKey(),settings);
                                        myRef.child("settings").updateChildren(settingsMap);
                                    }
                                    /*new Runnable() {
                                        @Override
                                        public void run() {
                                            Alarm alarm = new Alarm();
                                            alarm.setAlarm(context);
                                        }
                                    }.run();*/
                                    Intent i = new Intent(context, NotificationService.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startService(i);

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

    public void logout_user(Activity activity)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getResources().getString(R.string.saved_user_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(activity.getString(R.string.saved_user_isloggedin_key), false);
        editor.apply();

        myRef.child("users").orderByChild("token").equalTo(getFirebaseToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        UserFirebaseDb user = issue.getValue(UserFirebaseDb.class);
                        user.setToken("");
                        user.setLoggedIn(false);
                        Map<String,Object> user2updateMap = new HashMap<>();
                        user2updateMap.put(issue.getKey(),user);
                        myRef.child("users").updateChildren(user2updateMap);
                        Log.d(TAG,"USER UPDATED AND LOGGED OUT");

                        Intent intent = new Intent(activity,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Snack_exit",true);
                        context.startActivity(intent);
                        activity.finish();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showAlertNoInternet(Activity activity)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set title
        alertDialogBuilder.setTitle("İNTERNET BAĞLANTISI YOK");
        alertDialogBuilder.setIcon(R.drawable.no_internet);

        // set dialog message
        alertDialogBuilder
                .setMessage("Lütfen internet bağlantınızı kontrol edip tekrar deneyiniz.")
                .setCancelable(false)
                .setPositiveButton("TAMAM",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity

                    }
                })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void setSettingsCheckBoxes(Activity activity)
    {
        myRef.child("users").orderByChild("token").equalTo(getFirebaseToken()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        myRef.child("settings").orderByChild("pid").equalTo(issue.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null)
                                {
                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {
                                        SettingsFirebaseDb settings = issue.getValue(SettingsFirebaseDb.class);
                                        Map<String,Boolean> dayMap = settings.getNotificationDay();
                                        Map<String,Boolean> timeMap = settings.getNotificationTime();
                                        LinearLayout mLinearLayout = activity.findViewById(R.id.settingsLinearLayout);
                                        ArrayList<View> timesCheckBoxList = new ArrayList<>();
                                        ArrayList<View> daysCheckBoxList = new ArrayList<>();

                                        mLinearLayout.findViewsWithText(timesCheckBoxList,"time_interval",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                        mLinearLayout.findViewsWithText(daysCheckBoxList,"settings_days",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);


                                        for(Map.Entry<String, Boolean> entry : dayMap.entrySet()) {
                                            String key = entry.getKey().substring(2);
                                            boolean value = entry.getValue();
                                            ((CheckBox)(daysCheckBoxList.get(Integer.parseInt(key)-1))).setChecked(value);
                                        }

                                        for(Map.Entry<String, Boolean> entry : timeMap.entrySet()) {
                                            String key = entry.getKey().substring(2);
                                            boolean value = entry.getValue();
                                            ((CheckBox)(timesCheckBoxList.get(Integer.parseInt(key)-8))).setChecked(value);
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
    
    public void insertUsageStatistics(Activity activity,int seconds)
    {
        myRef.child("users").orderByChild("token").equalTo(getFirebaseToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    Log.d(TAG,"USER FOUND FOR USAGE STAT");
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        final String today = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                        UsageStatisticsFirebaseDb us = new UsageStatisticsFirebaseDb(issue.getKey(),today,seconds);
                        //myRef.child("usage_statistics").push().setValue(us);
                        myRef.child("usage_statistics").orderByChild("pid_date").equalTo(issue.getKey()+"_"+today).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG,"ON DATA CHANGE FOR USAGE STAT");

                                if(dataSnapshot.exists())
                                {
                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {
                                        UsageStatisticsFirebaseDb usfirebase = issue.getValue(UsageStatisticsFirebaseDb.class);
                                        usfirebase.setSeconds(usfirebase.getSeconds()+seconds);
                                        Map<String,Object> map =new HashMap<>();
                                        map.put(issue.getKey(),usfirebase);
                                        myRef.child("usage_statistics").updateChildren(map);
                                    }
                                }
                                else
                                {
                                    myRef.child("usage_statistics").push().setValue(us);
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

    public void setupGraph1(Activity activity){
        myRef.child("users").orderByChild("token").equalTo(getFirebaseToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        final String today = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                        myRef.child("finished_exercises").orderByChild("pid_date").equalTo(issue.getKey()+"_"+today).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null){
                                    int totalTime=0;
                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {
                                        FinishedExerciseFirebaseDb fe = issue.getValue(FinishedExerciseFirebaseDb.class);
                                        totalTime+= fe.getDuration();
                                    }
                                    BarChart barChart = activity.findViewById(R.id.barChart);



                                    Calendar calendar = Calendar.getInstance();

                                    Date d1 = calendar.getTime();

                                    final HashMap<Integer, String>numMap = new HashMap<>();
                                    numMap.put(1, new SimpleDateFormat("dd-MM-yyyy").format(d1));

                                    List<BarEntry> entries1 = new ArrayList<BarEntry>();
                                    entries1.add(new BarEntry(1,totalTime/60));

                                    String label = "Toplam Egzersiz Süresi (dk)";
                                    String description = "Günlük Yapılan Toplam Egzersiz Süresi";

                                    if(FunctionsHelper.IsLanguageEnglish(context)){
                                        label = "Total Exercise Time (min)";
                                        description = "Daily Finished Exercise Time";
                                    }
                                    BarDataSet dataSet = new BarDataSet(entries1, label);
                                    dataSet.setColor(ResourcesCompat.getColor(activity.getResources(), R.color.colorAccentButton, null) );
                                    BarData data = new BarData(dataSet);
                                    data.setValueFormatter(new IValueFormatter() {
                                        @Override
                                        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                                            String timeExt = "dk";
                                            if(FunctionsHelper.IsLanguageEnglish(context)){

                                                timeExt = "min";
                                            }
                                            return (int)value+timeExt;
                                        }
                                    });
                                    data.setBarWidth(0.1f);

                                    XAxis xAxis = barChart.getXAxis();
                                    xAxis.setLabelRotationAngle(45);
                                    xAxis.setValueFormatter(new DefaultAxisValueFormatter(0) {
                                        @Override public String getFormattedValue(float value, AxisBase axis) {

                                            Log.d(TAG,"X VALUE: "+value+"--LAbel count:"+axis.getLabelCount());
                                            return numMap.get((int)(value));
                                        }
                                        @Override
                                        public int getDecimalDigits() {
                                            return 0;
                                        }
                                    });
                                    xAxis.setGranularity(1f);

                                    barChart.setData(data);
                                    barChart.setNoDataText("Egzersiz verisi bulunamadı");
                                    Description desc = new Description();
                                    desc.setYOffset(-10);
                                    desc.setText(description);
                                    barChart.setDescription(desc);
                                    barChart.animateY(1000);
                                    barChart.invalidate();
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

    public void setupGraph2(Activity activity){
        myRef.child("users").orderByChild("token").equalTo(getFirebaseToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    for (DataSnapshot issue :
                            dataSnapshot.getChildren()) {
                        myRef.child("finished_exercises").orderByChild("pid").equalTo(issue.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null){
                                    BarChart chart = activity.findViewById(R.id.chart);


                                    int[] numArr = {1,2,3,4,5,6,7};
                                    int[] times = {0,0,0,0,0,0,0};
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.add(Calendar.DATE, -6);
                                    Date d1 = calendar.getTime();
                                    calendar.add(Calendar.DATE, 1);
                                    Date d2 = calendar.getTime();
                                    calendar.add(Calendar.DATE, 1);
                                    Date d3 = calendar.getTime();
                                    calendar.add(Calendar.DATE, 1);
                                    Date d4 = calendar.getTime();
                                    calendar.add(Calendar.DATE, 1);
                                    Date d5 = calendar.getTime();
                                    calendar.add(Calendar.DATE, 1);
                                    Date d6 = calendar.getTime();
                                    calendar.add(Calendar.DATE, 1);
                                    Date d7 = calendar.getTime();

                                    String simpled1 = new SimpleDateFormat("dd-MM-yyyy").format(d1);
                                    String simpled2 = new SimpleDateFormat("dd-MM-yyyy").format(d2);
                                    String simpled3 = new SimpleDateFormat("dd-MM-yyyy").format(d3);
                                    String simpled4 = new SimpleDateFormat("dd-MM-yyyy").format(d4);
                                    String simpled5 = new SimpleDateFormat("dd-MM-yyyy").format(d5);
                                    String simpled6 = new SimpleDateFormat("dd-MM-yyyy").format(d6);
                                    String simpled7 = new SimpleDateFormat("dd-MM-yyyy").format(d7);

                                    Log.d(TAG,"SIMPLE DATE D7: "+simpled7);



                                    for (DataSnapshot issue :
                                            dataSnapshot.getChildren()) {
                                        FinishedExerciseFirebaseDb fe = issue.getValue(FinishedExerciseFirebaseDb.class);
                                        if(fe.getDate().equals(simpled1))
                                        {
                                            times[0]+=fe.getDuration();
                                        }
                                        else if(fe.getDate().equals(simpled2))
                                        {
                                            times[1]+=fe.getDuration();
                                        }
                                        else if(fe.getDate().equals(simpled3))
                                        {
                                            times[2]+=fe.getDuration();
                                        }
                                        else if(fe.getDate().equals(simpled4))
                                        {
                                            times[3]+=fe.getDuration();
                                        }
                                        else if(fe.getDate().equals(simpled5))
                                        {
                                            times[4]+=fe.getDuration();
                                        }
                                        else if(fe.getDate().equals(simpled6))
                                        {
                                            times[5]+=fe.getDuration();
                                        }
                                        else if(fe.getDate().equals(simpled7))
                                        {
                                            times[6]+=fe.getDuration();
                                            Log.d(TAG,"777777de----fe.getdur: "+fe.getDuration());
                                        }
                                        Log.d(TAG,"----fe.getDate: "+fe.getDate());


                                    }

                                    List<BarEntry> entries1 = new ArrayList<BarEntry>();

                                    final HashMap<Integer, String>numMap = new HashMap<>();
                                    numMap.put(1, new SimpleDateFormat("dd-MM-yyyy").format(d1));
                                    numMap.put(2, new SimpleDateFormat("dd-MM-yyyy").format(d2));
                                    numMap.put(3, new SimpleDateFormat("dd-MM-yyyy").format(d3));
                                    numMap.put(4, new SimpleDateFormat("dd-MM-yyyy").format(d4));
                                    numMap.put(5, new SimpleDateFormat("dd-MM-yyyy").format(d5));
                                    numMap.put(6, new SimpleDateFormat("dd-MM-yyyy").format(d6));
                                    numMap.put(7, new SimpleDateFormat("dd-MM-yyyy").format(d7));

                                    for(int num : numArr){
                                        entries1.add(new BarEntry(num, times[num-1]/60));
                                        Log.d(TAG,"Array Element "+(num-1)+": "+times[num-1]);
                                    }

                                    String label = "Günlük Toplam Egzersiz Süresi (dk)";
                                    String description = "Haftalık Yapılan Toplam Egzersiz Süreleri";

                                    if(FunctionsHelper.IsLanguageEnglish(context)){
                                        label = "Daily Total Exercise Time (min)";
                                        description = "Finished Exercise Time in Last Week";
                                    }

                                    BarDataSet dataSet = new BarDataSet(entries1, label);
                                    dataSet.setColor(ResourcesCompat.getColor(activity.getResources(), R.color.colorAccentButton, null) );
                                    //dataSet.setCircleColor(ResourcesCompat.getColor(activity.getResources(), R.color.colorPrimary, null));
                                    BarData data = new BarData(dataSet);
                                    data.setValueFormatter(new IValueFormatter() {
                                        @Override
                                        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                                            String timeExt = "dk";
                                            if(FunctionsHelper.IsLanguageEnglish(context)){

                                                timeExt = "min";
                                            }
                                            return (int)value+timeExt;
                                        }
                                    });

                                    XAxis xAxis = chart.getXAxis();
                                    xAxis.setLabelRotationAngle(45);
                                    xAxis.setValueFormatter(new DefaultAxisValueFormatter(0) {
                                        @Override public String getFormattedValue(float value, AxisBase axis) {

                                            //Log.d(TAG,"X VALUE: "+value+"--LAbel count:"+axis.getLabelCount());
                                            return numMap.get((int)(value));
                                        }
                                        @Override
                                        public int getDecimalDigits() {
                                            return 0;
                                        }
                                    });
                                    xAxis.setGranularity(1f);
                                    chart.setData(data);
                                    chart.setNoDataText("Egzersiz verisi bulunamadı");
                                    Description desc = new Description();
                                    desc.setYOffset(-10);
                                    desc.setText(description);
                                    chart.setDescription(desc);
                                    chart.animateY(1000, Easing.EasingOption.Linear);
                                    chart.invalidate();
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

    public void getExerciseVideoLinksAndDownload(Activity activity){
        myRef.child("exercises").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ExerciseFirebaseDb> exerciseList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    final List<String> videoLinks = new ArrayList<>();
                    final List<String> videoFileNames = new ArrayList<>();

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        ExerciseFirebaseDb exercise = issue.getValue(ExerciseFirebaseDb.class);
                        //indexChange.add(0);
                        exerciseList.add(exercise);
                        videoLinks.add(exercise.getVideo_link());
                        videoFileNames.add(String.valueOf(exercise.getEid())+".mp4");
                    }

                    ProgressBack videoProgress = new ProgressBack();
                    videoProgress.activity = activity;

                    String[] videoLinksArray = new String[videoLinks.size()];
                    videoLinks.toArray(videoLinksArray);

                    String[] videoFileNamesArray = new String[videoFileNames.size()];
                    videoFileNames.toArray(videoFileNamesArray);

                    videoProgress.execute(videoLinksArray,videoFileNamesArray);

                }
                else{
                    Log.d(TAG,"NOT Exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
