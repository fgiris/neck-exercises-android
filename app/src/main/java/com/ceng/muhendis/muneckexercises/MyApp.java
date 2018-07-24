package com.ceng.muhendis.muneckexercises;

import android.app.Application;

import com.ceng.muhendis.muneckexercises.jobs.DemoJobCreator;
import com.evernote.android.job.JobManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        JobManager.create(this).addJobCreator(new DemoJobCreator());
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
