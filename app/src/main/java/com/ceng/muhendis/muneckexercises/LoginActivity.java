package com.ceng.muhendis.muneckexercises;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ceng.muhendis.muneckexercises.helpers.FirebaseDBHelper;
import com.ceng.muhendis.muneckexercises.helpers.FunctionsHelper;
import com.ceng.muhendis.muneckexercises.model.UserFirebaseDb;
import com.ceng.muhendis.muneckexercises.services.NotificationService;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import muhendis.diabetex.db.entity.UserEntity;
import muhendis.diabetex.db.viewmodel.UserViewModel;


/**
 * Created by muhendis on 14.11.2017.
 */

public class LoginActivity extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private TextView welcomeST;
    private EditText etEmail;
    private EditText etPassword;
    private UserViewModel mUserViewModel;
    private final String PID_KEY = "ProgramId";
    static int mPid=1;
    FirebaseDBHelper mFirebaseDBHelper;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private final String TAG="LoginActivity";
    private Button btnLogin,btnRegister,btnChangeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAndUpdateVersion();

        if(checkIsLoggedIn())
        {
            Log.d(TAG,"LOGGED IN");
            Date psd = null;
            try {
                psd = new SimpleDateFormat("dd-MM-yyyy").parse(getProgramStartDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(psd);
            calendar.add(Calendar.DATE,42);

            Intent intent;

            if((checkIsFirstSurveyCompleted() && Calendar.getInstance().getTime().before(calendar.getTime())) || checkIsLastSurveyCompleted() )
            {
                intent  = new Intent(this,ExercisesActivity.class);

            }
            else if(!checkIsLastSurveyCompleted() && Calendar.getInstance().getTime().after(calendar.getTime()))
            {
                intent  = new Intent(this,FirstSurveyActivity.class);
                intent.putExtra(Keys.IF_SIX_WEEKS_PASSED,true);
            }
            else
            {
                intent  = new Intent(this,FirstSurveyActivity.class);
                intent.putExtra(Keys.IF_SIX_WEEKS_PASSED,false);
            }
            startActivity(intent);
            this.finish();

        }
        else{
            Log.d(TAG,"NOT LOGGED IN");
        }
        setContentView(R.layout.activity_login);



        //startService(new Intent(LoginActivity.this, NotificationService.class));


        // Get Reference to variables
        etEmail = (EditText) findViewById(R.id.emailEditText);
        etPassword = (EditText) findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        mFirebaseDBHelper = new FirebaseDBHelper(getApplicationContext());
        btnChangeLanguage = findViewById(R.id.changeLanguageButton);
        welcomeST = findViewById(R.id.welcomeST);

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            welcomeST.setText("Welcome");
            etEmail.setHint("Email");
            etPassword.setHint("Password");
            btnLogin.setText("Login");
            btnRegister.setText("Register");
            btnChangeLanguage.setText("Dili Türkçe'ye Çevir");
        }

        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                    changeLanguage(false);
                }
                else{
                    changeLanguage(true);
                }
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        // Check if user logged in

        /*if(!mFirebaseDBHelper.isOnline())
        {
            showAlertNoInternet();
        }
        else{
            mFirebaseDBHelper.isUserLoggedIn(this);
        }*/
        Intent intent = getIntent();
        boolean isSnack = intent.getBooleanExtra("SNACK",false);
        boolean isSnackExit = intent.getBooleanExtra("Snack_exit",false);

        if(isSnack)
            showSnack(getResources().getString(R.string.registerSuccessfull));
        if(isSnackExit){
            if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                showSnack("Logout succesful");
            }
            else{
                showSnack("Başarı ile çıkış yapıldı");
            }
        }


    }

    public void showSnack(String s)
    {
        Snackbar.make(findViewById(R.id.login_layout_coordinator), s,
                Snackbar.LENGTH_LONG)
                .show();
    }

    public void changeLanguage(boolean isEnglish){
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.saved_user_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_user_isLanguageEnglish_key), isEnglish);
        editor.putBoolean(getString(R.string.saved_user_isLanguageChangingForNotification_key), true);

        editor.apply();

    }

    // Triggers when LOGIN Button clicked
    public void checkLogin(View arg0) {
        if(!mFirebaseDBHelper.isOnline())
        {
            showAlertNoInternet();
        }
        else{
            // Get text from email and passord field
            final String email = etEmail.getText().toString();
            final String password = etPassword.getText().toString();

            // Initialize  AsyncLogin() class with email and password
            //new AsyncLogin().execute(email,password);
            mFirebaseDBHelper.checkLogin(email,password,this);

        }

    }
    public boolean checkIsLoggedIn(){
        SharedPreferences sharedPref = this.getSharedPreferences(getResources().getString(R.string.saved_user_file_key),Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean(getString(R.string.saved_user_isloggedin_key), false);
        return isLoggedIn;
    }

    public boolean checkIsFirstSurveyCompleted(){
        SharedPreferences sharedPref = this.getSharedPreferences(getResources().getString(R.string.saved_user_file_key),Context.MODE_PRIVATE);
        boolean isFirstSurveyCompleted = sharedPref.getBoolean(getString(R.string.saved_user_isfirstsurveycompleted_key), false);
        return isFirstSurveyCompleted;
    }

    public boolean checkIsLastSurveyCompleted(){
        SharedPreferences sharedPref = this.getSharedPreferences(getResources().getString(R.string.saved_user_file_key),Context.MODE_PRIVATE);
        boolean isLastSurveyCompleted = sharedPref.getBoolean(getString(R.string.saved_user_islastsurveycompleted_key), false);
        return isLastSurveyCompleted;
    }

    public String getProgramStartDate(){
        SharedPreferences sharedPref = this.getSharedPreferences(getResources().getString(R.string.saved_user_file_key),Context.MODE_PRIVATE);
        String startDate = sharedPref.getString(getString(R.string.saved_user_programstartdate_key),"");
        return startDate;
    }

    // Triggers when REGISTER Button clicked
    public void registerUser(View arg0) {

        Intent intent = new Intent(LoginActivity.this,TermsofUseActivity.class);
        startActivity(intent);

    }

    public void showAlertNoInternet()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

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

    public void showAlertInvalidUserNameOrPassword()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("HATALI EPOSTA VEYA PAROLA");
        alertDialogBuilder.setIcon(R.drawable.invalid_cross);

        // set dialog message
        alertDialogBuilder
                .setMessage("Lütfen bilgilerinizi kontrol edip tekrar deneyiniz.")
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

    public void checkAndUpdateVersion(){
        SharedPreferences prefs = this.getSharedPreferences(getResources().getString(R.string.saved_user_file_key),Context.MODE_PRIVATE);
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            if ( prefs.getLong( getResources().getString(R.string.saved_user_versionNumberKey), 0) < pInfo.versionCode ) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(getResources().getString(R.string.saved_user_isloggedin_key), false);
                editor.putLong(getResources().getString(R.string.saved_user_versionNumberKey), pInfo.versionCode);
                editor.commit();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showAlertConnectionProblem()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("BAĞLANTI PROBLEMİ");
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


}
