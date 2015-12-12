package com.example.anand.todolist;

/**
 * Created by Anand on 12-12-2015.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // When our Alarm time is triggered , this method will be executed (onReceive)
        // We're invoking a service in this method which shows Notification to the User
        String remind = intent.getStringExtra("remind");
        String location = intent.getStringExtra("location");
        int yea=intent.getIntExtra("yea", 0);
        int mon=intent.getIntExtra("mon",0);
        int day=intent.getIntExtra("day",0);
        int hr=intent.getIntExtra("hr",0);
        int min=intent.getIntExtra("min",0);
        int sec=intent.getIntExtra("sec",0);
        Intent myIntent = new Intent(context, NotificationService.class);
        myIntent.putExtra("remind",remind);
        myIntent.putExtra("location",location);
        myIntent.putExtra("yea",yea);
        myIntent.putExtra("mon", mon);
        myIntent.putExtra("day",day);
        myIntent.putExtra("hr",hr);
        myIntent.putExtra("min",min);
        myIntent.putExtra("sec",sec);

        context.startService(myIntent);
    }

}
