package com.ceng.muhendis.muneckexercises;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ceng.muhendis.muneckexercises.helpers.FirebaseDBHelper;
import com.ceng.muhendis.muneckexercises.helpers.FunctionsHelper;
import com.ceng.muhendis.muneckexercises.services.NotificationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.Duration;

import muhendis.diabetex.db.entity.UserEntity;
import muhendis.diabetex.db.viewmodel.UserViewModel;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "SettingsActivity";
    ArrayList<View> timesCheckBoxList,daysCheckBoxList;
    LinearLayout mLinearLayout;
    Map<String,Boolean> timesMap,daysMap;
    FirebaseDBHelper mFirebaseDbHelper;
    long startTime,elapsedTime=0;
    Activity activity = this;
    Switch englishLanguageSwitch;
    TextView generalInfo,part1Header,part2Header,languageTitle;
    CheckBox mondayCB,tuesdayCB,wednesdayCB,thusrdayCB,fridayCB,saturdayCB,sundayCB;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            setContentView(R.layout.activity_settings_english);
            saveButton = findViewById(R.id.btn_save_settings);
            saveButton.setText("SAVE");

            mondayCB = findViewById(R.id.settingsMondayCB);
            tuesdayCB = findViewById(R.id.settingsTuesdayCB);
            wednesdayCB = findViewById(R.id.settingsWednesdayCB);
            thusrdayCB = findViewById(R.id.settingsThursdayCB);
            fridayCB = findViewById(R.id.settingsFridayCB);
            saturdayCB = findViewById(R.id.settingsSaturdayCB);
            sundayCB = findViewById(R.id.settingsSundayCB);

            mondayCB.setText("Monday");
            tuesdayCB.setText("Tuesday");
            wednesdayCB.setText("Wednesday");
            thusrdayCB.setText("Thursday");
            fridayCB.setText("Friday");
            saturdayCB.setText("Saturday");
            sundayCB.setText("Sunday");
        }
        else{
            setContentView(R.layout.activity_settings);
        }

        generalInfo = findViewById(R.id.settingsInfoStaticText);
        part1Header = findViewById(R.id.settingsPart1StaticText);
        part2Header = findViewById(R.id.settingsPart2StaticText);
        languageTitle = findViewById(R.id.settingsLanguageHeaderStaticText);



        mFirebaseDbHelper = new FirebaseDBHelper(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext()))
        {
            generalInfo.setText("Choose the appropriate time zone in which you would like to receive notifications to exercise during the day.");
            part1Header.setText("The time zone you want to receive notifications (You must select at least 3 different time zones)");
            part2Header.setText("Days you want to receive notifications (You must choose at least 3 days)");
            languageTitle.setText("Language");
            ((TextView)findViewById(R.id.settings_toolbar_title)).setText("My Settings");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view3);
        navigationView.setNavigationItemSelectedListener(this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        englishLanguageSwitch = findViewById(R.id.languageSwitch);
        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            englishLanguageSwitch.setChecked(true);
        }
        englishLanguageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((Switch)v).isChecked();

                String title = "Dil Değişikliği";
                String message = "Dil ayarlarının uygulanması için uygulama baştan başlatılacaktır";
                String btnText = "Tamam";
                String btnCancelText = "Vazgeç";
                if(!isChecked){
                    title = "Change Language";
                    message = "The app will be restarted in order to apply changes";
                    btnText = "Ok";
                    btnCancelText = "Cancel";
                }


                // set title
                alertDialogBuilder.setTitle(title);

                // set dialog message
                alertDialogBuilder
                        .setMessage(message)
                        .setCancelable(false)
                        .setNegativeButton(btnCancelText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((Switch)v).setChecked(!isChecked);
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(btnText,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                changeLanguage(isChecked);
                                Intent i = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                        })
                ;

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        mLinearLayout = findViewById(R.id.settingsLinearLayout);
        timesCheckBoxList = new ArrayList<>();
        daysCheckBoxList = new ArrayList<>();

        mLinearLayout.findViewsWithText(timesCheckBoxList,"time_interval",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        mLinearLayout.findViewsWithText(daysCheckBoxList,"settings_days",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        mFirebaseDbHelper.setSettingsCheckBoxes(this);
        int i=0;
        for (View v :
                daysCheckBoxList) {
            CheckBox c = (CheckBox)v;
            if(c.isChecked())
                Log.d(TAG,"CHECKED ID: "+i+"--TEXT: "+c.getText());
            i++;
        }

    }

    public void changeLanguage(boolean isEnglish){
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.saved_user_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_user_isLanguageEnglish_key), isEnglish);
        editor.putBoolean(getString(R.string.saved_user_isLanguageChangingForNotification_key), true);

        editor.apply();

    }

    public Map<String,Boolean> convert2Map(ArrayList<View> mView,boolean isDay){
        Map<String,Boolean> map = new HashMap<>();

        for (View view :
                mView) {
            int index = mView.indexOf(view);

            if(isDay)
                map.put("id"+String.valueOf(index+1),((CheckBox)view).isChecked());
            else
                map.put("id"+String.valueOf(index+8),((CheckBox)view).isChecked());
        }
        return map;
    }

    public boolean checkAtLeastThreeChecked(){
        int daysChecked=0,timesChecked=0;

        for (View view :
                daysCheckBoxList) {
            if(((CheckBox)view).isChecked())
                daysChecked++;
        }

        for (View view :
                timesCheckBoxList) {
            if(((CheckBox)view).isChecked())
                timesChecked++;
        }
        if(timesChecked<3 && daysChecked<3)
        {
            Toast.makeText(getApplicationContext(),"Lütfen en az 3 zaman aralığı ve 3 gün seçiniz", Toast.LENGTH_LONG).show();
        }
        else if(timesChecked<3)
        {
            Toast.makeText(getApplicationContext(),"Lütfen en az 3 zaman aralığı seçiniz", Toast.LENGTH_LONG).show();
        }
        else if(daysChecked<3)
        {
            Toast.makeText(getApplicationContext(),"Lütfen en az 3 gün seçiniz", Toast.LENGTH_LONG).show();
        }
        else
        {
            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exercises) {
            Intent intent = new Intent(SettingsActivity.this,ExercisesActivity.class);

            startActivity(intent);
            SettingsActivity.this.finish();
            return true;
            // Handle the camera action
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(SettingsActivity.this,StatisticsActivity.class);

            startActivity(intent);
            SettingsActivity.this.finish();
            return true;

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_exit) {
            FunctionsHelper.showAlertAndLogout(this,mFirebaseDbHelper);

            return true;
            /*if(!mFirebaseDBHelper.isOnline())
            {
                mFirebaseDBHelper.showAlertNoInternet(this);
                return false;
            }

            else{
                mFirebaseDBHelper.logout_user(this);
                return true;
            }*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void saveSettings(View btn){

        if(checkAtLeastThreeChecked())
        {
            timesMap = convert2Map(timesCheckBoxList,false);
            daysMap = convert2Map(daysCheckBoxList,true);

            mFirebaseDbHelper.insertSettings(timesMap,daysMap);
            if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                Toast.makeText(getApplicationContext(),"Settings updated", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Ayarlar Kaydedildi", Toast.LENGTH_LONG).show();
            }


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        elapsedTime = 0;
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mFirebaseDbHelper.insertUsageStatistics(activity,(Keys.elapsedTime/1000));
                Keys.elapsedTime=0;
            }
        });
        t.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
    }

}
