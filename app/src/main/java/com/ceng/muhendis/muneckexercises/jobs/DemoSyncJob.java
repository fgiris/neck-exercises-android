package com.ceng.muhendis.muneckexercises.jobs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ceng.muhendis.muneckexercises.Alarm;
import com.ceng.muhendis.muneckexercises.services.NotificationService;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.firebase.FirebaseApp;

import java.util.concurrent.TimeUnit;

public class DemoSyncJob extends Job {
    public static final String TAG = "job_demo_tag";

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        //FirebaseApp.initializeApp(getContext());
        Log.d("EVERNOTE_MY_JOB","NOTIFICATION RECIEVED Service Counter:");
        //Intent i = new Intent(getContext(), NotificationService.class);
        //getContext().startService(i);
        Alarm alarm = new Alarm();
        Alarm.isFirst=false;



        /*PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(getContext())
                .setContentTitle("Android Job Demo")
                .setContentText("Notification from Android Job Demo App.")
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setColor(Color.RED)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(getContext())
                .notify(new Random().nextInt(), notification);*/


        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        new JobRequest.Builder(DemoSyncJob.TAG)
                .setExecutionWindow(30_000L, 40_000L)
                .build()
                .schedule();
    }

    public static void schedulePeriodicJob() {
        int jobId = new JobRequest.Builder(DemoSyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15))
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}
