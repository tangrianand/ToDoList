package com.example.anand.todolist;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DoList extends AppCompatActivity {

    Button btnGPSShowLocation;
    Button btnShowAddress, btndate, bsubmit;
    TextView tvAddress, textView;
    private TimePicker timePicker1;
    private TextView time;
    private Calendar calendar, Calendar_Object;
    private String format = "";
    private TextView dateView;
    private int year, month, day;
    private TextView rem, da;
    private StringBuilder a;
    private ProgressDialog pDialog;
    private String username;
    JSONParser jParser1 = new JSONParser();
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
        Calendar_Object = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        rem = (EditText) findViewById(R.id.Textarea);
        da = (TextView) findViewById(R.id.viewdate);
        Intent intent1=getIntent();
        username=intent1.getStringExtra("username");

        final Test mGPS = new Test(this);
        if (!mGPS.isGPSEnabled || !mGPS.isNetworkEnabled) {
            showSettingsAlert();
            mGPS.getLocation();
            textView.setText("Latitude: " + mGPS.getLatitude() + " " + "Longitude: " + mGPS.getLongitude());
        }

        if (mGPS.canGetLocation) {
            mGPS.getLocation();
            textView.setText("Latitude: " + mGPS.getLatitude() + " " + "Longitude: " + mGPS.getLongitude());

        }

        if (mGPS.canGetLocation) {
            double latitude = mGPS.getLatitude();
            double longitude = mGPS.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());
        } else if (mGPS.isGPSEnabled && mGPS.isNetworkEnabled) {
            Toast.makeText(DoList.this, "Waiting For GPS!", Toast.LENGTH_SHORT).show();
        }

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


        btnGPSShowLocation = (Button) findViewById(R.id.btnGPSShowLocation);

        btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!mGPS.isGPSEnabled || !mGPS.isNetworkEnabled) {
                    showSettingsAlert();
                    mGPS.getLocation();
                    textView.setText("Latitude: " + mGPS.getLatitude() + " " + "Longitude: " + mGPS.getLongitude());
                } else if (mGPS.isGPSEnabled && mGPS.isNetworkEnabled) {
                    Toast.makeText(DoList.this, "Waiting For GPS!", Toast.LENGTH_SHORT).show();
                }

                if (mGPS.canGetLocation) {
                    mGPS.getLocation();
                    textView.setText("Latitude: " + mGPS.getLatitude() + " " + "Longitude: " + mGPS.getLongitude());
                }
            }
        });

        btnShowAddress = (Button) findViewById(R.id.btnShowAddress);

        btnShowAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!mGPS.isGPSEnabled || !mGPS.isNetworkEnabled) {
                    showSettingsAlert();
                    mGPS.getLocation();
                    textView.setText("Latitude: " + mGPS.getLatitude() + " " + "Longitude: " + mGPS.getLongitude());
                }

                if (mGPS.isGPSEnabled && mGPS.isNetworkEnabled) {
                    Toast.makeText(DoList.this, "Waiting For GPS!", Toast.LENGTH_SHORT).show();
                }

                if (mGPS.canGetLocation) {
                    double latitude = mGPS.getLatitude();
                    double longitude = mGPS.getLongitude();
                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude,
                            getApplicationContext(), new GeocoderHandler());
                } else {
                    Toast.makeText(DoList.this, "Waiting For GPS!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bsubmit = (Button) findViewById(R.id.btn_submit);

        bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setTime(arg0);
                new Submit().execute();

            }

        });

    }


    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);

    }

    public void showTime(int hour, int min) {
        if (min >= 15) {
            Calendar_Object.set(Calendar.HOUR_OF_DAY, hour);
            Calendar_Object.set(Calendar.MINUTE, min - 15);   //Considering person will put the time more than 15 minutes from the current time
            Calendar_Object.set(Calendar.SECOND, 0);
        } else if (min < 15) {
            Calendar_Object.set(Calendar.HOUR_OF_DAY, hour - 1);
            Calendar_Object.set(Calendar.MINUTE, 60 - 15 + min);
            Calendar_Object.set(Calendar.SECOND, 0);
        }


        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        a = new StringBuilder().append(hour).append(" : ").append(min).append(" ").append(format);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
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
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        Calendar_Object.set(Calendar.MONTH, month - 1);
        Calendar_Object.set(Calendar.YEAR, year);
        Calendar_Object.set(Calendar.DAY_OF_MONTH, day);
    }


    public void showSettingsAlert() {              //if gps is not enabled
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                DoList.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings Location Services?");
        alertDialog.setPositiveButton("Location Settings",
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

    private class GeocoderHandler extends Handler {     //to convert location to address
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

    class Submit extends AsyncTask<String, String, String> {  //inner class to enter details in database

        String remind = rem.getText().toString().trim();
        String location = tvAddress.getText().toString().trim();
        String dat = da.getText().toString().trim();
        String time = a.toString().trim();

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DoList.this);
            pDialog.setMessage("Submitting...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            int success;
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("remind", remind));
                params.add(new BasicNameValuePair("location", location));
                params.add(new BasicNameValuePair("dat", dat));
                params.add(new BasicNameValuePair("time", time));
                params.add(new BasicNameValuePair("username", username));
                Log.d("dry", time + remind + location + dat);
                Log.d("request!", "starting");

                JSONObject json1 = jParser1.makeHttpRequest("http://ec2fb769.ngrok.io/android/createdolist.php", "POST", params);
                Log.d("Submit attempt", json1.toString());
                success = json1.optInt("success");

                if (success == 1) {
                    Log.d("Successful Submmission", json1.toString());
                    return json1.getString("message");

                } else {
                    Log.d("No submission", json1.toString());
                    return json1.getString("message");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {
            try {
                pDialog.dismiss();
                if (message != null) {
                    Toast.makeText(DoList.this, message, Toast.LENGTH_LONG).show();
                }
                if (message.equals("Successfully entered")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DoList.this);
                    builder.setCancelable(true);
                    builder.setTitle(remind);
                    builder.setMessage("Location: " + location + "\n\n" + "Date: " + dat + "\n\n" + "Time: " + time);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.show();
                    Intent myIntent = new Intent(DoList.this, AlarmReceiver.class);
                    myIntent.putExtra("remind", remind);
                    myIntent.putExtra("location", location);
                    myIntent.putExtra("mon", Calendar_Object.get(Calendar.MONTH));
                    myIntent.putExtra("yea", Calendar_Object.get(Calendar.YEAR));
                    myIntent.putExtra("day", Calendar_Object.get(Calendar.DAY_OF_MONTH));
                    myIntent.putExtra("hr", Calendar_Object.get(Calendar.HOUR_OF_DAY));
                    myIntent.putExtra("min", Calendar_Object.get(Calendar.MINUTE));
                    myIntent.putExtra("sec", Calendar_Object.get(Calendar.SECOND));


                    PendingIntent pendingIntent = PendingIntent.getBroadcast(DoList.this,
                            0, myIntent, 0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		/*
		 * The following sets the Alarm in the specific time by getting the long
		 * value of the alarm date time which is in calendar object by calling
		 * the getTimeInMillis(). Since Alarm supports only long value , we're
		 * using this method.
		 */

                    alarmManager.set(AlarmManager.RTC, Calendar_Object.getTimeInMillis(),
                            pendingIntent);

                }
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DoList.this);
        builder.setCancelable(true);
        builder.setTitle("Permission");
        builder.setMessage("Sure to exit?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();

    }
}