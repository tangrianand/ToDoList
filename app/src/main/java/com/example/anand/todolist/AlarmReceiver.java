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
        Intent myIntent = new Intent(context, NotificationService.class);
        myIntent.putExtra("remind",remind);
        myIntent.putExtra("location",location);
        context.startService(myIntent);
    }

}
