package com.example.finalprojectgamebook.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    final String TAG = "MyFirebaseMessaging";
    private FirebaseAuth firebaseAuth;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Intent intent = new Intent("message_received");
            intent.putExtra("message",remoteMessage.getData().get("message"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplication().getApplicationContext(), 0, resultIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);



                    //if the application is not in forground post notification
            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this,"id_1");

            if(Build.VERSION.SDK_INT >= 28) {
                NotificationChannel channel = new NotificationChannel("id_1", "name_1", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId("id_1");
            }
            builder.setContentTitle("" + getText(R.string.You_have_a_new_message)).setContentText(remoteMessage.getData().get("message")).setSmallIcon(android.R.drawable.star_on).setContentIntent(resultPendingIntent);
            manager.notify(1,builder.build());
        }

        // Check if message contains a notification payload.

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    @Override
    public void onNewToken(@NonNull String s) {
        Log.d(TAG, "Refreshed token: " + s);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(s);
    }


}
