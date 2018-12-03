package com.example.appservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.util.Printer;

public class MyService extends Service {
    private final String TAG = getClass().getSimpleName();

    private Binder mBinder = new MyBinder();
    public MyService() {
    }

    public class MyBinder extends Binder{
        void logMessage(){
            Log.d(TAG,"bind success!!");
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind()");
       return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreat()");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartConmand()");
//        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestory()");
        super.onDestroy();
    }
}
