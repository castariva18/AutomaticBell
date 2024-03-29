package org.ganymede.automaticbell;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_NAME = "Main Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notification description";
    private static final String CHANNEL_ID = "NotificationChannelbaru";
    private static NotificationManager notificationManager;
    private Context base;
    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

    public NotificationHelper(Context base) {
        super(base);
        this.base = base;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {

        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationChannel.enableVibration(true);
        notificationChannel.enableLights(true);
        notificationChannel.canShowBadge();
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationChannel.setLightColor(getResources().getColor(R.color.colorAccent));
        getNotificationManager().createNotificationChannel(notificationChannel);
    }
    public NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) base.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public void notify(String message, String title, boolean setActive) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(base, CHANNEL_ID);
        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setContentTitle(title);
        builder.setSmallIcon(R.drawable.notif);
        builder.setContentText(message);
        builder.setSound(notificationUri);
        builder.setLights(0xFFFFFF00, 0, 2000);
        builder.setVibrate(new long[]{1000, 2000, 3000, 4000});
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        if (setActive == false) {

        } else {
            getNotificationManager().notify(9001, builder.build());
        }
    }
}