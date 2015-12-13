package com.example.anand.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anand on 12-12-2015.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private ProgressDialog pDialog;
    TextView user, pass;
    Button bregister;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        bregister = (Button) findViewById(R.id.btn_register);
        user = (TextView) findViewById(R.id.input_email);
        pass = (TextView) findViewById(R.id.input_password);
        bregister.setOnClickListener(this);
    }

    public void onClick(View view) {
        new AttemptLogin().execute();
    }
    @Override
    public void onBackPressed()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Permission");
        builder.setMessage("Move to Login Screen?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();

    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        boolean failure = false;
        String username = user.getText().toString();
        String password = pass.getText().toString();


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Attempting to Register...");
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

                JSONObject json = jParser.makeHttpRequest("http://ec2fb769.ngrok.io/android/register.php", "POST", params);
                Log.d("Register attempt", json.toString());

                success = json.optInt(TAG_SUCCESS);

                if (success == 1) {
                    Log.d("Successful Registration", json.toString());
                    return json.getString(TAG_MESSAGE);

                }
                else {
                    Log.d("unSuccessful Registration", json.toString());
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
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                }
                if(message.equals("Successfully Registered")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Permission");
                    builder.setMessage("Continue to login?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.show();
                }
            }
            catch (final IllegalArgumentException e) {
                e.printStackTrace();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}


