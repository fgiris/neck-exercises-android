package com.ceng.muhendis.muneckexercises;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ceng.muhendis.muneckexercises.adapters.ProgramListAdapter;
import com.ceng.muhendis.muneckexercises.services.MyJobService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import muhendis.diabetex.db.entity.ProgramEntity;
import muhendis.diabetex.db.entity.UserEntity;
import muhendis.diabetex.db.viewmodel.ProgramViewModel;
import muhendis.diabetex.db.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private ProgramViewModel mProgramViewModel;
    private UserViewModel mUserViewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Add a recylerview to show all exercises in the main screen
         */
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ProgramListAdapter adapter = new ProgramListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize mProgramViewModel to cache the current data
        mProgramViewModel = ViewModelProviders.of(this).get(ProgramViewModel.class);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);


        // Add an observer for the LiveData returned by getAllExercises method
        mProgramViewModel.getAllPrograms().observe(this, new Observer<List<ProgramEntity>>() {
            @Override
            public void onChanged(@Nullable final List<ProgramEntity> programs) {
                // Update the cached copy of the words in the adapter.
                adapter.setPrograms(programs);
            }
        });

        /*
         * Set your own toolbar
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));


        // Get the main screen linear layout
        //LinearLayout main_screen_ll=findViewById(R.id.main_screen_linear_layout);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mUserViewModel.getAllUsers().observe(this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(@Nullable List<UserEntity> userEntity) {
                TextView navbarNameInitials = findViewById(R.id.navbarNameInitials);
                TextView navbarUserName = findViewById(R.id.navbarUserNameSurname);
                TextView navbarUserMail = findViewById(R.id.navbarUserMail);
                for (UserEntity mUser :
                        userEntity) {
                    String name = mUser.getName();
                    String surname = mUser.getSurname();
                    String mail = mUser.getEmail();
                    String navbarNameSurnameText ="Merhaba";
                    String navbarUserMailText = "";
                    String initials="X";
                    //Check name and surname if null and update initial in logo
                    if(name != null || name !="")
                    {
                        initials=String.valueOf(name.charAt(0));
                        navbarNameSurnameText = name;
                    }
                    if(surname != null || surname !="")
                    {
                        initials+=String.valueOf(surname.charAt(0));
                        navbarNameSurnameText += " "+surname;
                    }
                    if(mail != null || mail !="")
                    {
                        navbarUserMailText = mail;
                    }
                    if(navbarNameInitials!=null)
                        navbarNameInitials.setText(initials);
                    if(navbarUserName!=null)
                        navbarUserName.setText(navbarNameSurnameText);
                    if(navbarUserMail!=null)
                        navbarUserMail.setText(navbarUserMailText);
                }

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        String token = FirebaseInstanceId.getInstance().getToken();
        //Log.d("TOKEN: ",token);

        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class) // the JobService that will be called
                .setTag("my-unique-tag")        // uniquely identifies the job
                .build();

        dispatcher.mustSchedule(myJob);


        // Dynamically add exercise to main screen from the program
        //1st download the ex-image
        //show The Image in a ImageView
        /*ImageView ex_image = new ImageView(this);
        new DownloadImageTask(ex_image)
        .execute("https://www.gstatic.com/webp/gallery3/1.png");
        LinearLayout ex_item_rl = new LinearLayout(this);
        ex_item_rl.addView(ex_image);
        main_screen_ll.addView(ex_item_rl);*/



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exercises) {
            // Handle the camera action
        } else if (id == R.id.nav_statistics) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}
