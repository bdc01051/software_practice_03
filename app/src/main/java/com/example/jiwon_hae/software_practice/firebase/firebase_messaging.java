package com.example.jiwon_hae.software_practice.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.main;
import com.example.jiwon_hae.software_practice.tmap.map_navigation;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiwon_hae on 2018. 6. 9..
 */

public class firebase_messaging extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            try {
                sendNotification(remoteMessage.getNotification().getBody());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    private void sendNotification(String messageBody) throws JSONException {
        JSONObject jsonObject = new JSONObject(messageBody);
        JSONObject message = new JSONObject(jsonObject.getString("message"));

        int notification_id = (int)Math.random();

        Intent intent_main = new Intent(this, main.class);
        intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent to_nav = new Intent(this, map_navigation.class);
        to_nav.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String message_content = message.getString("user_name").replace("_","") + "이 아직 집 도착못했어요";;
        String channelId = getString(R.string.default_notification_channel_id);

        PendingIntent pendingIntent;

        if(message.getBoolean("user_home")){
            message_content = message.getString("user_name").replace("_","") + "이 집에 잘 도착했어요";

            pendingIntent = PendingIntent.getActivity(this, notification_id /* Request code */, intent_main,
                    PendingIntent.FLAG_ONE_SHOT);
        }else{
            to_nav.putExtra("friends_name", message.getString("user_name"));
            to_nav.putExtra("user_latlng", message.getString("user_latlng"));
            to_nav.putExtra("notification_id", notification_id);
            pendingIntent = PendingIntent.getActivity(this, notification_id /* Request code */, to_nav,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle("취중고홈")
                        .setContentText(message_content)
                        .setAutoCancel(true)
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setSmallIcon(R.drawable.beer_notification_icon)
                        .setLights(000000255,500,2000)
                        .setVibrate(new long[]{0,3000})
                        .setContentIntent(pendingIntent);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("DATABASE", MODE_PRIVATE);
        JSONObject jsonObject_user = new JSONObject(sharedPreferences.getString("DATABASE", ""));

        if(!message.getString("sender_id").equals(jsonObject_user.getString("user_email"))){
           //Log.e("here", "no notification");
            notificationManager.notify(notification_id /* ID of notification */, notificationBuilder.build());
        }
    }

}
