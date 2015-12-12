package com.example.anand.todolist;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anand on 08-12-2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private ProgressDialog pDialog;
    TextView user, pass;
    Button blogin,bregister;
    SignInButton bgoogle;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);

        blogin = (Button) findViewById(R.id.btn_login);
        bregister = (Button) findViewById(R.id.btn_register);
        bgoogle = (SignInButton) findViewById(R.id.btn_google);
        user = (TextView) findViewById(R.id.input_email);
        pass = (TextView) findViewById(R.id.input_password);
        blogin.setBackgroundColor(Color.WHITE);
        bregister.setBackgroundColor(Color.WHITE);
        blogin.setOnClickListener(this);
        bregister.setOnClickListener(this);
        bgoogle.setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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


            public void onClick(View view) {
                switch (view.getId()) {

                    case R.id.btn_login:

                        if(!haveNetworkConnection())
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                            alertDialog.setTitle("Info");
                            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();
                        }
                        else
                        new AttemptLogin().execute();

                        break;

                    case R.id.btn_google:

                        if(!haveNetworkConnection())
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                            alertDialog.setTitle("Info");
                            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();
                        }
                        else
                        {
                        Log.d("google!", "starting");
                        Intent i=new Intent(LoginActivity.this, SigninActivity.class);
                        startActivity(i);
                        finish();
                        }
                        break;

                    case R.id.btn_register:

                        if(!haveNetworkConnection())
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                            alertDialog.setTitle("Info");
                            alertDialog.setMessage("Internet not available,\nCross check your internet connectivity and try again");
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            alertDialog.show();
                        }
                        else
                        {
                            Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                            startActivity(i);
                            finish();
                        }
                        break;

                    default:
                        break;
                }
            }

  private boolean haveNetworkConnection() {
      boolean haveConnectedWifi = false;
      boolean haveConnectedMobile = false;
      ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo[] netInfo = cm.getAllNetworkInfo();
      for (NetworkInfo ni : netInfo) {
          if (ni.getTypeName().equalsIgnoreCase("WIFI"))
              if (ni.isConnected())
                  haveConnectedWifi = true;
          if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
              if (ni.isConnected())
                  haveConnectedMobile = true;
      }
      return haveConnectedWifi || haveConnectedMobile;
  }


    class AttemptLogin extends AsyncTask<String, String, String> {

        boolean failure = false;
        String username = user.getText().toString();
        String password = pass.getText().toString();


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting to login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            int success;

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                Log.d("request!", "starting");
                JSONObject json = jParser.makeHttpRequest("http://c06d9af0.ngrok.io/android/login.php", "POST", params);
                Log.d("Login attempt", json.toString());
                success = json.optInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Successful Login", json.toString());
                        return json.getString(TAG_MESSAGE);
                    }
                    else {
                        Log.d("unSuccessful Login", json.toString());
                        return json.getString(TAG_MESSAGE);
                    }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message){
            try {
                pDialog.dismiss();
                if (message != null) {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                }
                if(message.equals("Successful login")) {
                    Intent intent = new Intent(LoginActivity.this, DoList.class);
                    startActivity(intent);
                    finish();
                }
            }
            catch (final IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}




