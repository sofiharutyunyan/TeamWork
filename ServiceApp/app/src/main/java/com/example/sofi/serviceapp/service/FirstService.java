package com.example.sofi.serviceapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.util.Log;

import com.example.sofi.serviceapp.R;
import com.example.sofi.serviceapp.ScrollingActivity;
import com.example.sofi.serviceapp.ServiceDemoActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FirstService extends Service {
    private static String TAG = "serviceTest";
    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private PendingIntent pendingIntent;

    NotificationManager notificationManager;

    public FirstService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "IBinder");
        return null;
    }

    @Override // erb service start enq anum,, kanchvum e es metod@
//    arajin parametr@` intent@ activityiic starti meji intentn e,
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        int time = intent.getIntExtra("time", 1);
        String name = intent.getStringExtra("name");
//        pending intenti masn e
        pendingIntent = intent.getParcelableExtra("pending");
        Log.i(TAG, "onStartCommand(): " + " time = "+ time + "name = " + name + " startId = " + startId);
//        doWork();
        DoWork myWork = new DoWork(time, startId);
        executor.execute(myWork);
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * es naxqan executor-i hamar er,, sovorakan servic-i u intentovi tarberutyan hamar
     */
//    private void doWork() {
////        taza threadi mej qcecinq or chlrvi
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    TimeUnit.SECONDS.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }

    private class DoWork implements Runnable {

        private final int time;
        private final int id;

        public DoWork(int time, int id) {
            this.time = time;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(time);
                Log.i(TAG, "stopself" + "time =" + time + " startId = " + id);
// Intent intent = new Intent();
                Intent intent = new Intent(ServiceDemoActivity.MY_BROADCAST_ACTION);
                intent.putExtra("key", 333);

                sendNotification();
//                sendBroadcast(intent);
// pendingIntent.send(FirstService.this, ServiceDemoActivity.MY_PENDING_TASK_OK, intent);
//                stopSelf(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
// } catch (PendingIntent.CanceledException e) {
// e.printStackTrace();
            }
        }

    }
    private void sendNotification() {
        Intent intent = new Intent(this, ScrollingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setAutoCancel(true);
        builder.setTicker("this is ticker text");
        builder.setContentTitle("Whats ap notification");
        builder.setContentText("You have a new massage");
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.build();

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.nkar);
        builder.setLargeIcon(icon);

        Notification notif = builder.getNotification();
        notificationManager.notify(11, notif);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
