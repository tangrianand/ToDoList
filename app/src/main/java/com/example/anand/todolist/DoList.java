package com.example.anand.todolist;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DoList extends AppCompatActivity {

    Button btnGPSShowLocation;
    Button btnShowAddress,btndate;
    TextView tvAddress,textView;
    private TimePicker timePicker1;
    private TextView time;
    private Calendar calendar;
    private String format = "";
    private DatePicker datePicker;
    private TextView dateView;
    private int year, month, day;

    AppLocationService appLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dolist);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        textView = (TextView) findViewById(R.id.textView);
        appLocationService = new AppLocationService(DoList.this);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker);
        time = (TextView) findViewById(R.id.time);
        dateView = (TextView) findViewById(R.id.viewdate);
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year,month+1,day);


        btndate = (Button) findViewById(R.id.btn_date);

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                year = calendar.get(Calendar.YEAR);

                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                setDate(arg0);

            }
        });


       final Test mGPS = new Test(this);
        if (mGPS.canGetLocation) {
            mGPS.getLocation();
            textView.setText("Latitude: " + mGPS.getLatitude() +" "+ "Longitude: " + mGPS.getLongitude());

        }
        else {
            showSettingsAlert();
        }


        Location location = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            double latitude = mGPS.getLatitude();
            double longitude = mGPS.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());
        } else {
            showSettingsAlert();
        }


        btnGPSShowLocation = (Button) findViewById(R.id.btnGPSShowLocation);

        btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (mGPS.canGetLocation) {
                    mGPS.getLocation();
                    textView.setText("Latitude: " + mGPS.getLatitude() +" "+ "Longitude: " + mGPS.getLongitude());

                }
                else {
                    showSettingsAlert();
                }
            }


    });
      btnShowAddress = (Button) findViewById(R.id.btnShowAddress);
        btnShowAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Location location = appLocationService
                        .getLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    double latitude = mGPS.getLatitude();
                    double longitude = mGPS.getLongitude();
                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude,
                            getApplicationContext(), new GeocoderHandler());
                } else {
                    showSettingsAlert();
                }


            }
        });

    }
    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
       showTime(hour,min);

    }
    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        }
        else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        //time.setText(new StringBuilder().append(hour).append(" : ").append(min).append(" ").append(format));
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                DoList.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        DoList.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            tvAddress.setText(locationAddress);
        }
    }
}