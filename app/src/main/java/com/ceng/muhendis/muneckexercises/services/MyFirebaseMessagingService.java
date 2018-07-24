package com.ceng.muhendis.muneckexercises.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ceng.muhendis.muneckexercises.MainActivity;
import com.ceng.muhendis.muneckexercises.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by muhendis on 24.11.2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    final String TAG = "MyFirebaseMS";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("body"));

            String body = remoteMessage.getData().get("body");
            String title = remoteMessage.getData().get("title");
            create_notification(title,body);

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void create_notification(String title, String body)
    {

        /*
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        String id = "my_channel_01";
        // The user-visible name of the channel.
        CharSequence name = "Notification Name";
        // The user-visible description of the channel.
        String description = "Notificaiton Description";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.

            mChannel.setDescription(description);

            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
            Log.d(TAG, "Notificaiton created");

        }

        Log.d(TAG, "Notificaiton NOT created");
        */
        //Get an instance of NotificationManager//


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(body)
                        .setContentText(title);

        Intent resultIntent = new Intent(this, MainActivity.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

        mBuilder.setContentIntent(resultPendingIntent);


        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//

        mNotificationManager.notify(001, mBuilder.build());



    }
}
