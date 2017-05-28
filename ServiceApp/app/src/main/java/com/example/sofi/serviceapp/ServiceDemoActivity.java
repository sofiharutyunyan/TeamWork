package com.example.sofi.serviceapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sofi.serviceapp.service.FirstService;
import com.example.sofi.serviceapp.service.SecondService;

import java.util.concurrent.ExecutorService;

public class ServiceDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar prbLoading;
    private int MY_PENDING_TASK = 11;
    public static int MY_PENDING_TASK_OK = 12;
    public static int MY_PENDING_TASK_CANCEL = 13;
    private static String TAG = "test01";
    private SecondService secondService;

    private boolean isBined = false;

    public static final String MY_BROADCAST_ACTION = "jjj kkk jjj kkk lll ;;; kkk lll";
    BroadcastReceiver broadcastOfMyData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int key = 0;
            if (intent != null) {
                key = intent.getIntExtra("key", 0);
            }
            Toast.makeText(getApplicationContext(), "Received data from service: key = " + key,
                    Toast.LENGTH_SHORT).show();
        }
    };

    ServiceConnection connection = new ServiceConnection() {



        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            isBined = true;
            secondService = ((SecondService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
            isBined = false;
        }
    };
    private Intent intent;

    @Override
    protected void onStart() {
        super.onStart();
        intent = new Intent(ServiceDemoActivity.this, SecondService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_demo);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        prbLoading = (ProgressBar) findViewById(R.id.prbLoading);
        Button btnBind = (Button) findViewById(R.id.btnBind);
        Button btnUnbind = (Button) findViewById(R.id.btnUnbind);
        Button btnPlus = (Button) findViewById(R.id.btnPlus);
        Button btnMinus = (Button) findViewById(R.id.btnMinus);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        btnUnbind.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);

        prbLoading.setVisibility(View.VISIBLE);

//        broadcast lseluc filter kdnenq intentFilteri mijiov
        IntentFilter myFilter = new IntentFilter(MY_BROADCAST_ACTION);
//        register enelu masn e
        registerReceiver(broadcastOfMyData, myFilter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PENDING_TASK) {
            if (resultCode == MY_PENDING_TASK_OK) {
                int key = data.getIntExtra("key", 0);
                Toast.makeText(getApplicationContext(), "Received data from service: key = " + key,
                        Toast.LENGTH_SHORT).show();
            } else if (resultCode == MY_PENDING_TASK_CANCEL) {
                Toast.makeText(getApplicationContext(), "Failed to receive data",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ServiceDemoActivity.this, FirstService.class); //Service kertanq enpes inchxor Activitiic activity

        switch (v.getId()) {
            case R.id.btnStart:
//                start btn clicked
                PendingIntent pendingIntent = createPendingResult(MY_PENDING_TASK, new Intent(), 0);
                intent = new Intent(ServiceDemoActivity.this, FirstService.class);
                intent.putExtra("name", "Jane");
                intent.putExtra("time", 12);
                intent.putExtra("pending", pendingIntent);
                startService(intent);

//                intent = new Intent(ServiceDemoActivity.this, FirstService.class);
//                intent.putExtra("name", "Richard");
//                intent.putExtra("time", 2);
//                startService(intent);
//
//                intent = new Intent(ServiceDemoActivity.this, FirstService.class);
//                intent.putExtra("name", "Jonathan");
//                intent.putExtra("time", 15);
//                startService(intent);

                prbLoading.setVisibility(View.VISIBLE);
                break;
            case R.id.btnStop:
//                stop btn clicked
                stopService(intent);
//                prbLoading.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnBind:
//                bind service
                if (isBined) return;

        break;
        case R.id.btnUnbind:
//                Unbind service
        if (!isBined) return;
        unbindService(connection);
        isBined = false;
        break;
            case R.id.btnPlus:
                if (secondService != null){
                    secondService.increaseSpeed();
                }
                break;
            case R.id.btnMinus:
                if (secondService != null){
                    secondService.decreaseSpeed();
                }
                break;
    }

}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastOfMyData);
    }
}
