package com.example.sofi.serviceapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class SecondService extends Service {

    public static String TAG = "test01";
    public int interval = 3000;
    Timer timer;
    private TimerTask timerTask;

    public SecondService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public void increaseSpeed(){
        interval += 1000;
        doWork(interval);
    }

    public void decreaseSpeed(){
        if (interval < 2000 ){
            interval = 1000;
        }else
        interval -= 1000;
        doWork(interval);
    }

    private void doWork(int time){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "in doWork RUN method");
            }
        };
        timer.schedule(timerTask, 1000, time);
    }

    public class MyBinder extends Binder{
        public SecondService getService(){
            return SecondService.this;
        }
    }
}


