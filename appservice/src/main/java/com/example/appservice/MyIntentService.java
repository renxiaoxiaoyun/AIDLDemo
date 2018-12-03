package com.example.appservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    private final String TAG = getClass().getSimpleName();

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG,"this is an IntentService!!");
        String str = intent.getStringExtra("service");
        Log.d(TAG,str);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"intentservice creat()");
    }



    @Override
    public void onDestroy() {
        Log.d(TAG,"intentservice destory()");
        super.onDestroy();
    }


}
