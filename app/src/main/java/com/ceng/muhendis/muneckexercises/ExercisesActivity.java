package com.ceng.muhendis.muneckexercises;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceng.muhendis.muneckexercises.adapters.ExerciseListAdapter;
import com.ceng.muhendis.muneckexercises.helpers.FirebaseDBHelper;
import com.ceng.muhendis.muneckexercises.helpers.FunctionsHelper;
import com.ceng.muhendis.muneckexercises.helpers.ProgressBack;
import com.ceng.muhendis.muneckexercises.jobs.DemoSyncJob;
import com.ceng.muhendis.muneckexercises.model.ExerciseFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.FinishedExerciseFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.UserFirebaseDb;
import com.ceng.muhendis.muneckexercises.services.AlarmNotificationService;
import com.ceng.muhendis.muneckexercises.services.NotificationService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import muhendis.diabetex.db.entity.ExerciseEntity;
import muhendis.diabetex.db.entity.UserEntity;
import muhendis.diabetex.db.viewmodel.ExerciseViewModel;
import muhendis.diabetex.db.viewmodel.StatisticsViewModel;
import muhendis.diabetex.db.viewmodel.UserViewModel;

import static java.sql.Types.NULL;

public class ExercisesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Button mSelectExerciseButton;
    private static int currentPid,userID=2;
    private static UserEntity currentUser;
    private final String PID_KEY = "ProgramId";
    private UserViewModel mUserViewModel;
    private ExerciseViewModel mExerciseViewModel;
    private StatisticsViewModel mStatisticsViewModel;
    FirebaseDBHelper mFirebaseDBHelper;
    Chronometer timer;
    long startTime,elapsedTime=0;
    Activity activity=this;
    MenuItem exercisesMenuItem,statisticsMenuItem,settingsMenuItem,logoutMenuItem;

    private final String TAG = "ExercisesActivity";


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoSyncJob.schedulePeriodicJob();

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            setContentView(R.layout.activity_exercises2_english);
            ((TextView)findViewById(R.id.exercises_toolbar_title)).setText("My Exercises");
        }
        else{
            setContentView(R.layout.activity_exercises2);
        }



        timer = new Chronometer(this);
        startTime = System.currentTimeMillis();

        mFirebaseDBHelper = new FirebaseDBHelper(getApplicationContext());
        syncMediaFiles(this);
        //setAlarms();
        //Get the pid from the intent
        Intent intent = getIntent();
        currentPid = intent.getIntExtra(PID_KEY,NULL);

        boolean isSnack = intent.getBooleanExtra("SNACK",false);

        if(isSnack)
            showSnack("Egzersizi tamamladınız. Bilgiler kaydedildi.");


        Toolbar toolbar = findViewById(R.id.exerciseToolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        //View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main2, null);
        //navigationView.addHeaderView(header);
        //mFirebaseDBHelper.syncNavigationBar(this,navigationView);


        TabLayout tabLayout = findViewById(R.id.exerciseTabs);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.greyDarker));
        tabLayout.setTabTextColors(getResources().getColor(R.color.greyDark),getResources().getColor(R.color.greyDarker));
        tabLayout.setSelectedTabIndicatorHeight(4);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.exerciseViewPagerContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

    }

    public boolean getIsLanguageChanging(){
        SharedPreferences sharedPref = this.getSharedPreferences(getResources().getString(R.string.saved_user_file_key),Context.MODE_PRIVATE);
        boolean isChanging = sharedPref.getBoolean(getString(R.string.saved_user_isLanguageChangingForNotification_key), false);
        return isChanging;
    }


    @Override
    protected void onStart() {
        super.onStart();
        elapsedTime = 0;
        startTime = System.currentTimeMillis();
        Intent i = new Intent(this, NotificationService.class);
        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            i.putExtra(Keys.SERVICE_MESSAGE_IS_ENGLISH,true);
        }
        else{
            i.putExtra(Keys.SERVICE_MESSAGE_IS_ENGLISH,false);
        }
        i.putExtra(Keys.SERVICE_MESSAGE_IS_LANGUAGE_CHANGING,getIsLanguageChanging());

        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.saved_user_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_user_isLanguageChangingForNotification_key), false);
        editor.apply();


        startService(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Keys.elapsedTime += System.currentTimeMillis() - startTime;
        //Log.d(TAG,"PAUSED - TIMER VALUE: "+(int)(elapsedTime));
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
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"EXERCISE DESTROYED ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    public void setAlarms(){
        Thread t = new Thread(                        new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                Alarm alarm = new Alarm();
                alarm.setAlarm(context);

                AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context, Alarm.class);
                i.putExtra(Keys.ALARM_MESSAGE,"Günaydın");

                PendingIntent pi = PendingIntent.getBroadcast(context, 815, i, 0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 15);

                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pi);

                ////////////////
                i = new Intent(context, Alarm.class);
                i.putExtra(Keys.ALARM_MESSAGE,"Güne omurganın düzgünlüğünü sağlayarak başlamaya ne dersin?");

                pi = PendingIntent.getBroadcast(context, 1015, i, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 15);


                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pi);

                ///////////////

                i = new Intent(context, Alarm.class);
                i.putExtra(Keys.ALARM_MESSAGE,"Hadi omurganın dikliğini hisset, tek tek omurlarını hisset, başının üzerinde bir şey varmış gibi dik dur.");

                pi = PendingIntent.getBroadcast(context, 1215, i, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 15);


                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pi);


                ///////////////

                i = new Intent(context, Alarm.class);
                i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

                pi = PendingIntent.getBroadcast(context, 1415, i, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 14);
                calendar.set(Calendar.MINUTE, 15);


                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pi);

                ///////////////

                i = new Intent(context, Alarm.class);
                i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt. Kendini iyi hisset!");

                pi = PendingIntent.getBroadcast(context, 1615, i, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 16);
                calendar.set(Calendar.MINUTE, 15);


                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pi);

                ///////////////

                i = new Intent(context, Alarm.class);
                i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu düzelt ve vücudundaki gerginlikleri azalt!");

                pi = PendingIntent.getBroadcast(context, 1815, i, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 18);
                calendar.set(Calendar.MINUTE, 15);


                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pi);

                ///////////////

                i = new Intent(context, Alarm.class);
                i.putExtra(Keys.ALARM_MESSAGE,"Duruşunu korumalısın! ");

                pi = PendingIntent.getBroadcast(context, 2015, i, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 20);
                calendar.set(Calendar.MINUTE, 15);


                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pi);


            }
        });

        t.start();

        Alarm alarm = new Alarm();
        alarm.setAlarm(getApplicationContext());

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSnack(String s)
    {
        Snackbar.make(findViewById(R.id.login_layout_coordinator), s,
                Snackbar.LENGTH_LONG)
                .show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exercises) {
            // Handle the camera action
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(ExercisesActivity.this,StatisticsActivity.class);

            startActivity(intent);
            ExercisesActivity.this.finish();
            return true;

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(ExercisesActivity.this,SettingsActivity.class);

            startActivity(intent);
            ExercisesActivity.this.finish();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class ExerciseListFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private final String TAG = "ExerciseListFragment";

        public ExerciseListFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ExerciseListFragment newInstance(int sectionNumber) {
            ExerciseListFragment fragment = new ExerciseListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.content_exercises, container, false);


            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
             /*
             * Add a recylerview to show all exercises in the main screen
             */
            RecyclerView recyclerView = rootView.findViewById(R.id.exerciseRecyclerview);

            //Register for contex menu to handle long click events in recyclerview item

            RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
            recycledViewPool.setMaxRecycledViews(0, 30);


            final ExerciseListAdapter adapter = new ExerciseListAdapter(getActivity());
            //Log.d(TAG,"USER-ID-LIST-ADAPTER-INTENT: "+userID);
            adapter.currenPid = userID;
            for (int i = 0; i < 30; i++) {
                ExerciseListAdapter.ExerciseViewHolder simpleViewHolder = adapter.createViewHolder(container,0);
                recycledViewPool.putRecycledView(simpleViewHolder);
            }
            recyclerView.setRecycledViewPool(recycledViewPool);

            recyclerView.setAdapter(adapter);
            recyclerView.setItemViewCacheSize(30);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            String exerciseKey="exercises";
            if(FunctionsHelper.IsLanguageEnglish(getContext())){
                exerciseKey = "exercises_eng";
            }

            if(sectionNumber==1){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference();


                myRef.child(exerciseKey).orderByKey().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<ExerciseFirebaseDb> exerciseList = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            final List<Integer> indexChange = new ArrayList<>();
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                // do something with the individual "issues"
                                ExerciseFirebaseDb exercise = issue.getValue(ExerciseFirebaseDb.class);
                                //indexChange.add(0);
                                exerciseList.add(exercise);
                            }
                            //adapter.setExercisesFirebase(exerciseList,1,indexChange);
                            myRef.child("users").orderByChild("token").equalTo(FirebaseInstanceId.getInstance().getToken()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // dataSnapshot is the "issue" node with all children with id 0
                                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                            // do something with the individual "issues"
                                            UserFirebaseDb user = issue.getValue(UserFirebaseDb.class);
                                            String userId = issue.getKey();
                                            String today = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                                            myRef.child("finished_exercises").orderByChild("pid_date").equalTo(userId+"_"+today).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        // dataSnapshot is the "issue" node with all children with id 0


                                                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                                            // do something with the individual "issues"
                                                            FinishedExerciseFirebaseDb finishedExercise = issue.getValue(FinishedExerciseFirebaseDb.class);
                                                            Iterator<ExerciseFirebaseDb> it = exerciseList.iterator();
                                                            int i=0;
                                                            while (it.hasNext()) {
                                                                ExerciseFirebaseDb ex = it.next();
                                                                //Log.d(TAG,"Exercise name in LİST: "+ex.getName()+"---Finished Ex NAme: "+finishedExercise.getEx_name());

                                                                if (ex.getName().equals(finishedExercise.getEx_name())) {
                                                                    //exerciseList.set(i,null);
                                                                    it.remove();
                                                                    /*indexChange.remove(i);
                                                                    for(int counter=i;i<indexChange.size();counter++)
                                                                        indexChange.set(counter,indexChange.get(counter)-1);
                                                                    i--;*/
                                                                    Log.d(TAG,"REMOVED ID:"+ex.getEid());
                                                                }
                                                                i++;
                                                            }
                                                            //Log.d(TAG,"EXERCISE LIST SIZE: "+exerciseList.size());
                                                            //exerciseList.remove(exercise);

                                                        }

                                                    }
                                                    else{
                                                        Log.d(TAG,"NOT Exists");
                                                    }
                                                    adapter.setExercisesFirebase(exerciseList,1,indexChange);
                                                    recyclerView.setAdapter(adapter);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        }
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
                        else{
                            Log.d(TAG,"NOT Exists");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else if(sectionNumber==2){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                myRef.child("users").orderByChild("token").equalTo(FirebaseInstanceId.getInstance().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                // do something with the individual "issues"
                                String userId = issue.getKey();
                                String today = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                                //Log.d(TAG,"TODAY: "+today);
                                FirebaseDatabase.getInstance().getReference().child("finished_exercises").orderByChild("pid_date").equalTo(userId+"_"+today).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        final List<ExerciseFirebaseDb> exerciseList = new ArrayList<>();
                                        if (dataSnapshot.exists()) {
                                            String exerciseKey="exercises";
                                            if(FunctionsHelper.IsLanguageEnglish(getContext())){
                                                exerciseKey = "exercises_eng";
                                            }
                                            // dataSnapshot is the "issue" node with all children with id 0
                                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                                // do something with the individual "issues"
                                                FinishedExerciseFirebaseDb finishedExercise = issue.getValue(FinishedExerciseFirebaseDb.class);
                                                FirebaseDatabase.getInstance().getReference().child(exerciseKey).orderByKey().equalTo(String.valueOf(finishedExercise.getEid())).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        final List<Integer> indexChange = new ArrayList<>();

                                                        if (dataSnapshot.exists()) {

                                                            // dataSnapshot is the "issue" node with all children with id 0
                                                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                                                // do something with the individual "issues"
                                                                ExerciseFirebaseDb exercise = issue.getValue(ExerciseFirebaseDb.class);
                                                                exerciseList.add(exercise);
                                                                //indexChange.add(0);

                                                            }

                                                        }
                                                        else{
                                                            Log.d(TAG,"NOT Exists");
                                                        }
                                                        adapter.setExercisesFirebase(exerciseList,2,indexChange);
                                                        recyclerView.setAdapter(adapter);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });


                                            }
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
                        else{

                            CharSequence text = "Kullanıcı kaydı bulunamadı";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(getContext(), text, duration);
                            toast.show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            return rootView;
        }



    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ExerciseListFragment (defined as a static inner class below).
            return ExerciseListFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Give page titles according to its position
            if(position==0)
            {
                if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                    return "INCOMPLETE";
                }
                else{
                    return getResources().getString(R.string.exercise_tab1_header);
                }
            }
            else
            if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                return "COMPLETED";
            }
            else{
                return getResources().getString(R.string.exercise_tab2_header);
            }
        }

    }

    public void syncMediaFiles(Activity activity){

        boolean isAllVideosDownloaded=true;

        for(int i=1;i<19;i++){
            String videoFilePath = Environment.getExternalStorageDirectory()
                    + File.separator + ".mü_neck_exercises/videos/" + i +".mp4";

            File videoFile = new File(videoFilePath);

            if (!videoFile.exists())
            {
                isAllVideosDownloaded=false;
            }
        }

        if(!isAllVideosDownloaded){
            if(!(mFirebaseDBHelper.isOnline()))
            {
                if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                    showAlertNoInternetNotCancelable("Download Error","There is an error while downloading the media files. Please make sure you have internet connection and restart the app.");

                }
                else{
                    showAlertNoInternetNotCancelable("Program Verileri İndirilemedi","Program verilerini indirmek için lütfen internete bağlı olduğunuzdan emin olup tekrar deneyiniz.");

                }
            }
            else{
                mFirebaseDBHelper.getExerciseVideoLinksAndDownload(this);
            }
        }

    }

    public void showAlertNoInternetNotCancelable(String title,String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.no_internet);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }



}
