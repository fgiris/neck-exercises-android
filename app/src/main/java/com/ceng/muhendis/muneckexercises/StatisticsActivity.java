package com.ceng.muhendis.muneckexercises;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ceng.muhendis.muneckexercises.helpers.FirebaseDBHelper;
import com.ceng.muhendis.muneckexercises.helpers.FunctionsHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class StatisticsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //GraphView graph,graph2;
    final String TAG="StatisticsActivity";
    BarChart chart;
    BarChart barChart;
    FirebaseDBHelper mFirebaseDBHelper;
    long startTime,elapsedTime=0;
    Activity activity = this;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.dailyGraph:
                    barChart.setVisibility(View.VISIBLE);
                    chart.setVisibility(View.GONE);
                    mFirebaseDBHelper.setupGraph1(activity);
                    return true;
                case R.id.weeklyGraph:
                    chart.setVisibility(View.VISIBLE);
                    barChart.setVisibility(View.GONE);
                    mFirebaseDBHelper.setupGraph2(activity);
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            setContentView(R.layout.activity_statistics_english);
            ((TextView)findViewById(R.id.statistics_toolbar_title)).setText("My Statistics");
        }
        else{
            setContentView(R.layout.activity_statistics);
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mFirebaseDBHelper = new FirebaseDBHelper(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.statisticsToolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        barChart = (BarChart) findViewById(R.id.barChart);
        chart = (BarChart) findViewById(R.id.chart);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view4);
        navigationView.setNavigationItemSelectedListener(this);

        mFirebaseDBHelper.setupGraph1(this);
        mFirebaseDBHelper.setupGraph2(this);


    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exercises) {
            Intent intent = new Intent(StatisticsActivity.this,ExercisesActivity.class);

            startActivity(intent);
            return true;
            // Handle the camera action
        } else if (id == R.id.nav_statistics) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(StatisticsActivity.this,SettingsActivity.class);

            startActivity(intent);
            return true;

        } else if (id == R.id.nav_exit) {
            FunctionsHelper.showAlertAndLogout(this,mFirebaseDBHelper);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                mFirebaseDBHelper.insertUsageStatistics(activity,(Keys.elapsedTime/1000));
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
