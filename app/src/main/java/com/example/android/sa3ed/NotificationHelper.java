package com.example.android.sa3ed;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private static Uri alarmSound;
    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);

        createChannel();

    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getChannelNotification() {
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Wash")
                .setContentText("Wash Your hand now!.")
                .setSmallIcon(R.drawable.ic_bell)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(alarmSound);
    }
    public NotificationCompat.Builder getChannelNotification1() {
        alarmSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Drink")
                .setContentText("Drink hot Coffe Now!.")
                .setSmallIcon(R.drawable.ic_bell)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(alarmSound);

    }
}
