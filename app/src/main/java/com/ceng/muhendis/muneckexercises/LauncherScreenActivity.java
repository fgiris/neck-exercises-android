package com.ceng.muhendis.muneckexercises;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ceng.muhendis.muneckexercises.services.NotificationService;

/**
 * Created by muhendis on 14.11.2017.
 */

public class LauncherScreenActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_launcher_screen);
        //startService(new Intent(LauncherScreenActivity.this, NotificationService.class));
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(LauncherScreenActivity.this,LoginActivity.class);
                LauncherScreenActivity.this.startActivity(mainIntent);
                LauncherScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
