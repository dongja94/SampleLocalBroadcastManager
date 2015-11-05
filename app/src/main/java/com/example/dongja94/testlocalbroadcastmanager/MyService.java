package com.example.dongja94.testlocalbroadcastmanager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    public MyService() {
    }
    public static final String ACTION_COUNT = "com.example.dongja94.testlocalbroadcast.action.COUNT";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    boolean isRunning = false;
    int mCount = 0;
    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning) {
                    mCount++;
                    if (mCount % 5 == 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ACTION_COUNT);
                                intent.putExtra("count", mCount);
                                LocalBroadcastManager.getInstance(MyService.this).sendBroadcastSync(intent);
                                boolean isProcessed = intent.getBooleanExtra("result", false);
                                if (!isProcessed) {
                                     Toast.makeText(MyService.this, "not process", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    Log.i("MyService", "count : " + mCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }
}
