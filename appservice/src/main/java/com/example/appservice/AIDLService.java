package com.example.appservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AIDLService extends Service {

    private static final String TAG = "AIDLService";
    private static final int SAY_HELLO = 0;
    private static final int SAY_BAYBAY = 1;
    private List<Book> books = new ArrayList<>();
    public AIDLService() {
    }

    private static class MyHandler extends Handler{
        private final WeakReference<Service> mService;

        private MyHandler(AIDLService mService) {
            this.mService = new WeakReference<Service>(mService);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SAY_HELLO:
                    Log.d(TAG,"get success");

                    Toast.makeText(mService.get(),"hello world",Toast.LENGTH_LONG).show();
                    Message message = Message.obtain();
                    message.what = SAY_BAYBAY;
//                    message.obj = "byebye";
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","后会有期");
                    message.setData(bundle);
                    try {
                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);
    private final Messenger messenger = new Messenger(mHandler);
    private BookManager.Stub bookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                Log.d(TAG,"getBooks() now books is:"+books.toString());
                if (books == null){
                    return new ArrayList<>();
                }
                return books;
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {

            if (books == null){
                books = new ArrayList<>();
            }
            if (book == null){
                book = new Book();
            }
            book.setPrice(23333);
            if(!books.contains(book)){
                books.add(book);
            }
            Log.d(TAG,"addBooks() now books is:"+books.toString());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("android 开发艺术探索");
        book.setPrice(28);
        books.add(book);
    }

    @Override
    public IBinder onBind(Intent intent) {
       Log.d(TAG,"bind success");
        return messenger.getBinder();
    }
}
