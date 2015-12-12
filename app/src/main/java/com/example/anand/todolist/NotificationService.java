package com.example.anand.todolist;

/**
 * Created by Anand on 12-12-2015.
 */
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
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
		/*
		 * When the user taps the notification we have to show the Home Screen
		 * of our App, this job can be done with the help of the following
		 * Intent.
		 */
        Intent intent1 = new Intent(this.getApplicationContext(), DoList.class);

       /* Notification notification = new Notification(R.mipmap.td,
                "See My App something for you", System.currentTimeMillis());

        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);*/

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
                this.getApplicationContext(), 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

       /* notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.setLatestEventInfo(this.getApplicationContext(),
               "SANBOOK", "See My App something for you",
                pendingNotificationIntent);

        mManager.notify(0, notification);*/

        Notification noti = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(remind)
                .setContentText("Location: " + location)
                .setTicker("ToDoListReminder")
                .setSmallIcon(R.mipmap.td)
                .setContentIntent(pendingNotificationIntent).build();

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
