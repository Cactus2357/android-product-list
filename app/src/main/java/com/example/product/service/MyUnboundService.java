package com.example.product.service;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.product.MainActivity;
import com.example.product.R;

public class MyUnboundService extends Service {

    private static final String CHANNEL_ID = "001";
    private static final String CHANNEL_NAME = "Demo Channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String msg = (intent != null) ? intent.getStringExtra("msg") : "Service started";

        sendNotification(msg);

        // If the system kills the service, restart it (no Intent redelivery)
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH);
            channel.setDescription("Demo notification channel");
            manager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String msg) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg", msg);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Background Work Done")
                .setContentText("Your task has finished: " + msg)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Not used for unbound services
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service destroyed");
    }
}
