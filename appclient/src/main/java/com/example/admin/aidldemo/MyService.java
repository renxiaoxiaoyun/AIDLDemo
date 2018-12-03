package com.example.admin.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import com.example.appservice.Book;

import java.lang.ref.WeakReference;
import java.util.List;

public class MyService extends Service {
    private static final  int SAY_HELLO = 0;
    private static class MyHanlder extends Handler{
        private final WeakReference<MyService> mService;

        private MyHanlder(MyService mService) {
            this.mService = new WeakReference<MyService>(mService);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SAY_HELLO:

                    Message message = (Message) msg.obj;
                    Toast.makeText(mService.get(), String.valueOf(message.what),Toast.LENGTH_LONG).show();
            }
        }
    }

    private Messenger messenger = new Messenger(new MyHanlder(this));

    @Override
    public IBinder onBind(Intent intent) {
       return messenger.getBinder();
    }
}
