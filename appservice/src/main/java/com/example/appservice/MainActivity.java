package com.example.appservice;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.ConnectionService;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private ServiceConnection cnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder) service;
            myBinder.logMessage();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initHandlerThread(){
        long id = Thread.currentThread().getId();
        String processName = Thread.currentThread().getName();
        HandlerThread handlerThread = new HandlerThread("myHandlerThread");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Bundle bundle = msg.getData();
//                long id = Thread.currentThread().getId();
//                String processName = Thread.currentThread().getName();
                long id = bundle.getLong("id");
                String processName = bundle.getString("name");
                Log.d(TAG,"currentProcess :"+processName+"---"+id);
            }
        };
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);
        bundle.putString("name",processName);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public void startService(View view) {
        Intent intent = new Intent(this,MyService.class);
//        intent.setComponent(new ComponentName("com.example.appservice","com.example.appservice.MyService"));
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this,MyService.class);
//        intent.setComponent(new ComponentName("com.example.appservice","com.example.appservice.MyService"));
        stopService(intent);
    }

    public void startIntentService(View view) {
        Intent intent = new Intent(this,MyIntentService.class);
        intent.putExtra("service","我是谁，我在哪儿");
        startService(intent);
    }

    public void startHandlerThread(View view) {
        initHandlerThread();
    }

    private void bindService(){
        Intent intent = new Intent(this,MyService.class);
        bindService(intent,cnn,Context.BIND_AUTO_CREATE);
    }

    public void bindService(View view) {
        bindService();
    }

    public void unBindService(View view) {
        unbindService(cnn);
    }
}
