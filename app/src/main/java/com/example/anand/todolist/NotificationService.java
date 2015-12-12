package com.example.anand.todolist;

/**
 * Created by Anand on 12-12-2015.
 */
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

public class NotificationService extends Service {

    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
   // @TargetApi(16)
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        // Getting Notification Service
        mManager = (NotificationManager) this.getApplicationContext()
                .getSystemService(
                        this.getApplicationContext().NOTIFICATION_SERVICE);
        String remind = intent.getStringExtra("remind");
        String location = intent.getStringExtra("location");
        int yea=intent.getIntExtra("yea", 0);
        int mon=intent.getIntExtra("mon",0);
        int day=intent.getIntExtra("day",0);
        int hr=intent.getIntExtra("hr",0);
        int min=intent.getIntExtra("min", 0);
        int sec=intent.getIntExtra("sec", 0);
		/*
		 * When the user taps the notification we have to show the Home Screen
		 * of our App, this job can be done with the help of the following
		 * Intent.
		 */
        Intent intent1 = new Intent(this.getApplicationContext(), DialogActivity.class);
        intent1.putExtra("remind", remind);
        intent1.putExtra("location", location);
        intent1.putExtra("yea",yea);
        intent1.putExtra("mon", mon);
        intent1.putExtra("day",day);
        intent1.putExtra("hr",hr);
        intent1.putExtra("min",min);
        intent1.putExtra("sec",sec);


        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
                this.getApplicationContext(), 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sound = Uri.parse("file:///android_asset/hopo.mp3");
        Notification noti = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(remind)
                .setContentText("Location: " + location)
                .setTicker("ToDoListReminder").setAutoCancel(true)
                .setSmallIcon(R.mipmap.td).setSound(sound).setContentIntent(pendingNotificationIntent).build();


        NotificationManager noteManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        noteManager.notify(1, noti);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
