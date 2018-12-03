package com.example.admin.aidldemo;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appservice.Book;
import com.example.appservice.BookManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private static final int SAY_HELLO = 0;
    private static final int SAY_BAYBAY = 1;
    private static final int SENT_MESSAGE = 2;
    private BookManager manager;
    private List<Book> books = new ArrayList<>();
    private boolean mBound;
    TextView tv;
    Messenger messenger = null;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SAY_BAYBAY:
                    Log.d(TAG,"get reply");
//                    Toast.makeText(getApplicationContext(), (CharSequence) msg.obj,Toast.LENGTH_LONG).show();
                    Bundle bundle = msg.getData();
                    tv.setText(bundle.getString("reply"));
                    break;
                case SENT_MESSAGE:
                    Toast.makeText(getApplicationContext(), (CharSequence) msg.obj,Toast.LENGTH_LONG).show();
            }
        }
    };
    private Messenger clientMsg = new Messenger(handler);
    private ServiceConnection cnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            messenger = new Messenger(service);
            manager = BookManager.Stub.asInterface(service);
            mBound = true;

            if (manager!=null){
                Log.d(TAG,books.toString());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
    }

    public void addBook(View view){

        if (!mBound){
            bindService();
            return;
        }
        if (manager == null){
            return;
        }
        Book book = new Book();
        book.setName("将夜");
        book.setPrice(56);

        try {
            manager.addBook(book);
            Log.d(TAG,book.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindService(){
        Intent intent = new Intent(this,MyService.class);
//        intent.setAction("android.intent.action.aidlservice");
//        intent.setPackage("com.example.appservice");
        bindService(intent,cnn,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound){
            bindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound){
            unbindService(cnn);
            mBound = false;
        }
    }

    public void sayHello(View view) {
        Message msg = Message.obtain(null,100);
        Message message = Message.obtain();
        message.what = SAY_HELLO;
        message.obj = msg;
        message.replyTo = clientMsg;
        try {
            messenger.send(message);
            Log.d(TAG,"send success");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sentMessage(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain(handler);
                message.what = SENT_MESSAGE;
                message.obj = "哈哈哈";
                message.sendToTarget();
            }
        }).start();
    }
}
