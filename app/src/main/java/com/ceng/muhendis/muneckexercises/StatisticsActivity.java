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
    LineChart chart;
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
        chart = (LineChart) findViewById(R.id.chart);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view4);
        navigationView.setNavigationItemSelectedListener(this);

        //setUpGraph1();
        //setUpGraph2();
        mFirebaseDBHelper.setupGraph1(this);
        mFirebaseDBHelper.setupGraph2(this);
        /*
        Calendar calendar = Calendar.getInstance();
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

        graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3),
                new DataPoint(d4, 2),
                new DataPoint(d5, 6),
                new DataPoint(d6, 8),
                new DataPoint(d7, 10)
        });

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(20);

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Günlük Egzersiz Sayısı");
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(20);
        graph.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.LEFT);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.addSeries(series);
        //graph.getLegendRenderer().setVisible(true);
        //graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 4 because of the space
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true);
// set manual x bounds to have nice steps
        //graph.getViewport().setMinX(d1.getTime());
        //graph.getViewport().setMaxX(d3.getTime());
        //graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Pazartesi", "Salı", "Çarşamba","Perşembe","Cuma","Cumartesi","Pazar"});
        //staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph2 = (GraphView) findViewById(R.id.graph2);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 10),
                new DataPoint(2, 15),
                new DataPoint(3, 13),
                new DataPoint(4, 12),
                new DataPoint(5, 26),
                new DataPoint(6, 26),
                new DataPoint(7, 26)
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            series2.setColor(getColor(R.color.colorAccentButton));
        }
        else{
            series2.setColor(Color.BLUE);
        }
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10);
        series2.setThickness(8);
        graph2.addSeries(series2);
        graph2.getGridLabelRenderer().setVerticalAxisTitle("Günlük Egzersiz Süresi");
        graph2.getGridLabelRenderer().setVerticalAxisTitleTextSize(20);

         staticLabelsFormatter = new StaticLabelsFormatter(graph2);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Pazartesi", "Salı", "Çarşamba","Perşembe","Cuma","Cumartesi","Pazar"});
        graph2.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);*/



    }

    public void setUpGraph1()
    {
        barChart = (BarChart) findViewById(R.id.barChart);


        int[] numArr = {1};
        Calendar calendar = Calendar.getInstance();

        Date d1 = calendar.getTime();

        final HashMap<Integer, String>numMap = new HashMap<>();
        numMap.put(1, new SimpleDateFormat("dd-MM-yyyy").format(d1));

        List<BarEntry> entries1 = new ArrayList<BarEntry>();

        for(int num : numArr){
            entries1.add(new BarEntry(num, 30));
        }


        BarDataSet dataSet = new BarDataSet(entries1, "Toplam Egzersiz Süresi (dk)");
        dataSet.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccentButton, null) );
        BarData data = new BarData(dataSet);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value+"dk";
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
        desc.setText("Günlük Yapılan Toplam Egzersiz Süresi");
        barChart.setDescription(desc);
        barChart.animateY(1000);
        barChart.invalidate();
        /*List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));

        BarDataSet set = new BarDataSet(entries, "Günlük Egzersiz Süresi");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        */
    }

    public void setUpGraph2()
    {
        chart = (LineChart) findViewById(R.id.chart);


        int[] numArr = {1,2,3,4,5,6,7};
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

        final HashMap<Integer, String>numMap = new HashMap<>();
        numMap.put(1, new SimpleDateFormat("dd-MM-yyyy").format(d1));
        numMap.put(2, new SimpleDateFormat("dd-MM-yyyy").format(d2));
        numMap.put(3, new SimpleDateFormat("dd-MM-yyyy").format(d3));
        numMap.put(4, new SimpleDateFormat("dd-MM-yyyy").format(d4));
        numMap.put(5, new SimpleDateFormat("dd-MM-yyyy").format(d5));
        numMap.put(6, new SimpleDateFormat("dd-MM-yyyy").format(d6));
        numMap.put(7, new SimpleDateFormat("dd-MM-yyyy").format(d7));

        List<Entry> entries1 = new ArrayList<Entry>();

        for(int num : numArr){
            entries1.add(new Entry(num, num*num));
        }

        LineDataSet dataSet = new LineDataSet(entries1, "Günlük Toplam Egzersiz Süresi (dk)");
        dataSet.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccentButton, null) );
        dataSet.setCircleColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        LineData data = new LineData(dataSet);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value+"dk";
            }
        });

        XAxis xAxis = chart.getXAxis();
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
        chart.setData(data);
        chart.setNoDataText("Egzersiz verisi bulunamadı");
        Description desc = new Description();
        desc.setYOffset(-10);
        desc.setText("Haftalık Yapılan Toplam Egzersiz Süreleri");
        chart.setDescription(desc);
        chart.animateY(1000, Easing.EasingOption.Linear);
        chart.invalidate();
        /*List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));

        BarDataSet set = new BarDataSet(entries, "Günlük Egzersiz Süresi");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        */
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
            mFirebaseDBHelper.logout_user(this);
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
