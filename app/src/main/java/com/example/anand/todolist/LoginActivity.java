package com.example.anand.todolist;
import android.app.ProgressDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anand on 08-12-2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    private ProgressDialog pDialog;
    TextView user, pass;
    Button blogin,bgoogle;
    JSONParser jParser = new JSONParser();
   // private static String url = "http://localhost/android/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blogin = (Button) findViewById(R.id.btn_login);
        bgoogle = (Button) findViewById(R.id.btn_google);
        user = (TextView) findViewById(R.id.input_email);
        pass = (TextView) findViewById(R.id.input_password);
        blogin.setOnClickListener(this);
        bgoogle.setOnClickListener(this);
    }


            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_login:
                        new AttemptLogin().execute();
                    case R.id.btn_google:
                        Log.d("google!", "starting");
                       Intent intent=new Intent(LoginActivity.this, SigninActivity.class);
                        startActivity(intent);
                        finish();
                    default:
                       break;
                }

            }






    class AttemptLogin extends AsyncTask<String, String, String> {

        boolean failure = false;
        String username = user.getText().toString();
        String password = pass.getText().toString();


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting for login...");
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

                JSONObject json = jParser.makeHttpRequest("http://192.168.236.1/android/login.php", "POST", params);
               Log.d("Login attempt", json.toString());


                success = json.optInt(TAG_SUCCESS);



                    if (success == 1) {
                        Log.d("Successful Login", json.toString());
                        return json.getString(TAG_MESSAGE);
                    } else {
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
            pDialog.dismiss();
            if(message!=null){
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}




