package com.example.product.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyBoundService extends Service {
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MyBoundService getService() {
            return MyBoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public String getCurrentTimestamp() {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MyBoundService created");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("MyBoundService unbounded");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("MyBoundService destroyed");
    }
}