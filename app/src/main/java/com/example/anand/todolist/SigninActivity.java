package com.example.anand.todolist;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    Uri a;
    private static final String TAG = "SignInActivity";
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView, mview;
    private ProgressDialog mProgressDialog;
    String username,password;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        mStatusTextView = (TextView) findViewById(R.id.status);
        mview = (TextView) findViewById(R.id.email);
        getSupportActionBar().setTitle("Google Profile");

        findViewById(R.id.btn_signout).setOnClickListener(this);
        findViewById(R.id.btn_signin).setOnClickListener(this);
        findViewById(R.id.btn_continue).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signIn();

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);
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


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {

            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            a = acct.getPhotoUrl();

            mStatusTextView.setText(getString(R.string.signedin, acct.getDisplayName()));
            mview.setText(getString(R.string.mail, acct.getEmail()));

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);

                        // [END_EXCLUDE]
                    }
                });
        Toast.makeText(this, "Successfully Signed out!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();


    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.btn_signin).setVisibility(View.GONE);
            findViewById(R.id.btn_signout).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_continue).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText("");

            findViewById(R.id.btn_continue).setVisibility(View.GONE);
            findViewById(R.id.btn_signin).setVisibility(View.GONE);
            findViewById(R.id.btn_signout).setVisibility(View.GONE);

        }
    }

    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_signin:
                signIn();
                break;
            case R.id.btn_signout:
                signOut();
                break;
            case R.id.btn_continue:
                new AttemptLogin().execute();
                Intent intent = new Intent(SigninActivity.this, DoList.class);
                intent.putExtra("username",mview.getText().toString());
                startActivity(intent);
                finish();
                break;


        }
    }


    class AttemptLogin extends AsyncTask<String, String, String> {

        boolean failure = false;
        String username = mview.getText().toString();
        String password = mStatusTextView.getText().toString();



        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            int success;

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");

                JSONObject json = jParser.makeHttpRequest("http://ec2fb769.ngrok.io/android/googlesignin.php", "POST", params);
                Log.d("Register attempt", json.toString());

                success = json.optInt(TAG_SUCCESS);

                if (success == 1) {
                    Log.d("Successful Registration", json.toString());
                    return json.getString(TAG_MESSAGE);

                } else {
                    Log.d("unSuccessful Registration", json.toString());
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {
            try {

                if (message != null) {
                    Toast.makeText(SigninActivity.this, message, Toast.LENGTH_LONG).show();
                }

            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}







