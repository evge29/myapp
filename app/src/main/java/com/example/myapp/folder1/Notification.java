package com.example.myapp.folder1;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.myapp.MainActivity;
import com.example.myapp.R;


public class Notification {
    private PendingIntent notificationPendingIntent;

    public android.app.Notification setNotification(Context context, String title, String text, int icon) {
        if (notificationPendingIntent == null) {
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        }

        android.app.Notification notification;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // OREO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Permanent Notification";
            int importance = NotificationManager.IMPORTANCE_LOW;

            String CHANNEL_ID = "uk.ac.shef.oak.channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            String description = "I would like to receive travel alerts and notifications for:";
            channel.setDescription(description);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            notification = notificationBuilder
                    .setSmallIcon(icon)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(notificationPendingIntent)
                    .build();

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification = new NotificationCompat.Builder(context, "channel")
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(android.app.Notification.PRIORITY_MIN)
                    .setContentIntent(notificationPendingIntent).build();
        } else {
            notification = new NotificationCompat.Builder(context, "channel")
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(android.app.Notification.PRIORITY_MIN)
                    .setContentIntent(notificationPendingIntent).build();
        }

        return notification;
    }

}