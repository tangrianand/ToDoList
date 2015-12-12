package com.example.anand.todolist;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

/**
 * Created by Anand on 12-12-2015.
 */
public class DialogActivity extends Activity {
    private Button bdismiss,bsnooze;
    String remind,location;
    private Calendar Calendar_Object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        this.setFinishOnTouchOutside(false);
        bdismiss=(Button)findViewById(R.id.btn_dismiss);
        bsnooze=(Button)findViewById(R.id.btn_snooze);
        Intent intent = getIntent();
        Calendar_Object = Calendar.getInstance();
        int yea=intent.getIntExtra("yea", 0);
        int mon=intent.getIntExtra("mon", 0);
        int day=intent.getIntExtra("day",0);
        int hr=intent.getIntExtra("hr",0);
        int min=intent.getIntExtra("min", 0);
        int sec=intent.getIntExtra("sec", 0);
        remind = intent.getStringExtra("remind");
        location = intent.getStringExtra("location");
        Log.d("show",":"+yea+"/"+mon+"/"+day+"-"+hr+":"+min+":"+sec);

        Calendar_Object.set(Calendar.MONTH, mon);
        Calendar_Object.set(Calendar.YEAR, yea);
        Calendar_Object.set(Calendar.DAY_OF_MONTH, day);
        if(min>=30)                                     //30 minute after we press snooze
        {
        Calendar_Object.set(Calendar.HOUR_OF_DAY, hr+1);
        Calendar_Object.set(Calendar.MINUTE, min-30);
        Calendar_Object.set(Calendar.SECOND,sec);}
        else if(min<30)
        {
            Calendar_Object.set(Calendar.HOUR_OF_DAY, hr);
            Calendar_Object.set(Calendar.MINUTE, min+30);
            Calendar_Object.set(Calendar.SECOND,sec);}

        bdismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();

            }
        });
        bsnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(DialogActivity.this, AlarmReceiver.class);
                myIntent.putExtra("remind", remind);
                myIntent.putExtra("location", location);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(DialogActivity.this,
                        0, myIntent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


                alarmManager.set(AlarmManager.RTC, Calendar_Object.getTimeInMillis(),
                        pendingIntent);
                finish();
            }
        });

}
}
