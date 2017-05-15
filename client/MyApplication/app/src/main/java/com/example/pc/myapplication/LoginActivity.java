package com.example.pc.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnSingLogin;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    private LoginButton loginFacebook;
    private CallbackManager callbackManager;
    private View view;
    private ReceiverOnSingLogin onSignLogin;
    private ProgressDialog progressDialog;
    private TwitterLoginButton loginTwitter;
    private boolean isLinking; // cuando se llama a esta activity para linkears en splex
    private TripTP tripTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isLinking = getIntent().getBooleanExtra(Consts.IS_LINKING, false);

        FacebookSdk.sdkInitialize(getApplicationContext());

        loginTwitter = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                updateWithToken(null, result.data);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();

        callbackManager = CallbackManager.Factory.create();
        view = findViewById(R.id.relLogin);
        loginFacebook = (LoginButton)findViewById(R.id.login_button);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        onSignLogin = new ReceiverOnSingLogin(this, progressDialog, isLinking);

        loginFacebook.registerCallback(callbackManager, this);
        loginFacebook.setReadPermissions(Arrays.asList("email"));

        tripTP = (TripTP) getApplication();

        if (isLinking) {
            if (tripTP.getSocialDef().equals(Consts.S_FACEBOOK)) {
                loginFacebook.setVisibility(View.GONE);
            } else if (tripTP.getSocialDef().equals(Consts.S_TWITTER)) {
                loginTwitter.setVisibility(View.GONE);
            }

            findViewById(R.id.tryIt).setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(onSignLogin,
                new IntentFilter(Consts.POST_SIGN_LOGIN));

        if (!isLinking) {
            updateWithToken(AccessToken.getCurrentAccessToken(), twitterSession);
        }

    }

    private void updateWithToken(AccessToken currentAccessToken, TwitterSession twitterSession) {

        if (currentAccessToken != null) {
            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();
            try {
                data.put(Consts.ACC_TOKEN, currentAccessToken.getToken());
                body.put(Consts.DATA, data);

                splexLogin(body.toString(), Consts.FACEBOOK, Consts.S_FACEBOOK);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (twitterSession != null) {

            Call<User> userResult = Twitter.getApiClient(twitterSession).getAccountService()
                    .verifyCredentials(true, false);

            userResult.enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> userResult) {
                    tripTP.setScreenName(userResult.data.screenName);
                }
                @Override
                public void failure(TwitterException e) {}
            });

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();
            try {
                data.put(Consts.ACC_TOKEN, twitterSession.getAuthToken().token);
                data.put(Consts.ACC_TOKEN_SECRET, twitterSession.getAuthToken().secret);
                body.put(Consts.DATA, data);

                splexLogin(body.toString(), Consts.TWITTER, Consts.S_TWITTER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            view.setVisibility(View.VISIBLE);
        }

    }

    public void onStart() {
        super.onStart();

    }

    public void onStop() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onStop();
    }

    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onSignLogin);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
                loginTwitter.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void splexLogin(String body, String provider, String sProdider) {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
            String urlSignLogin;
            String rMethod;
            boolean isLogin;

            if (isLinking) {
                urlSignLogin = Consts.SPLEX_URL + Consts.SIGNUP + provider;
                rMethod = Consts.PUT;
                isLogin = false;
            } else {
                urlSignLogin = Consts.SPLEX_URL + Consts.SIGNUP + Consts.LOGIN + provider;
                tripTP.setSocialDef(sProdider);
                rMethod = Consts.POST;
                isLogin = true;
            }

            Map<String, String> header = Consts.getSplexHeader(tripTP, isLogin);

            InternetClient client = new InfoClient(getApplicationContext(),
                    Consts.POST_SIGN_LOGIN, urlSignLogin, header,rMethod, body, true);
            client.createAndRunInBackground();

        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        updateWithToken(loginResult.getAccessToken(), null);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    public void tryIt(View view) {
        tripTP.setSocialDef("");
        tripTP.setLogin(false);
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
        this.finish();
    }
}
