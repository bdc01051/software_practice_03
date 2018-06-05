package com.example.jiwon_hae.software_practice.artik;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;
import android.widget.Toast;

import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.account.login.login_activity;
import com.example.jiwon_hae.software_practice.main;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;

import java.util.List;
import java.util.Map;

import cloud.artik.api.UsersApi;
import cloud.artik.client.ApiCallback;
import cloud.artik.client.ApiClient;
import cloud.artik.client.ApiException;
import cloud.artik.model.UserEnvelope;

import static com.example.jiwon_hae.software_practice.artik.AuthHelper.INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE;
import static com.example.jiwon_hae.software_practice.artik.AuthHelper.USED_INTENT;

public class ARTIKLoginActivity extends Activity {

    private AuthorizationService mAuthorizationService;
    private UsersApi mUsersApi = null;
    private ApiClient mApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mApiClient = new ApiClient();
        mUsersApi = new UsersApi(mApiClient);

        mAuthorizationService = new AuthorizationService(this);

        if(checkIntent(getIntent())) finish();
    }

    private void doAuth() {
        AuthorizationRequest authorizationRequest = AuthHelper.createAuthorizationRequest();

        PendingIntent authorizationIntent = PendingIntent.getActivity(
                this,
                authorizationRequest.hashCode(),
                new Intent(INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE, null, this, ARTIKLoginActivity.class),0);

        /* request sample with custom tabs */
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();

        mAuthorizationService.performAuthorizationRequest(authorizationRequest, authorizationIntent, customTabsIntent);

    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAuthorizationService.dispose();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        checkIntent(intent);
    }

    private boolean checkIntent(@Nullable Intent intent) {

        if (intent != null) {
            String action = intent.getAction();
            if(action == null) {
                if(intent.hasExtra("auto_login")) {
                    mApiClient.setAccessToken(AuthManager.getAuthState().getAccessToken());
                    getUserInfo();
                    return false;
                }
                else {
                    doAuth();
                    return true;
                }
            }
            else {
                switch (action) {
                    case INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE:
                        if (!intent.hasExtra(USED_INTENT)) {
                            handleAuthorizationResponse(intent);
                            intent.putExtra(USED_INTENT, true);
                        }
                        return false;
                }
            }
        }

        return true;
    }

    private void handleAuthorizationResponse(@NonNull Intent intent) {
        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException error = AuthorizationException.fromIntent(intent);

        if (response != null) {
            if (response.authorizationCode != null ) { // Authorization Code method: succeeded to get code
                final AuthState authState = new AuthState(response, error);

                // File 2nd call to get the token
                mAuthorizationService.performTokenRequest(response.createTokenExchangeRequest(), new AuthorizationService.TokenResponseCallback() {
                    @Override
                    public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException exception) {
                        if (tokenResponse != null) {
                            authState.update(tokenResponse, exception);
                            AuthManager.saveAuthState(authState);

                            mApiClient.setAccessToken(authState.getAccessToken());
                            getUserInfo();

                        } else {
                            Context context = getApplicationContext();
                            CharSequence text = "Token Exchange failed";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                });
            } else { // come here w/o authorization code. For example, signup finish and user clicks "Back to login"
                if (response.additionalParameters.get("status").equalsIgnoreCase("login_request")) {
                    // ARTIK Cloud instructs the app to display a sign-in form
                    doAuth();
                }
            }
        }
    }

    private void getUserInfo()
    {
        final String tag = "TAG" + " getSelfAsync";
        try {
            mUsersApi.getSelfAsync(new ApiCallback<UserEnvelope>() {
                @Override
                public void onFailure(ApiException exc, int statusCode, Map<String, List<String>> map) {
                    processFailure(tag, exc);

                    Intent to_login = new Intent(ARTIKLoginActivity.this, login_activity.class);
                    to_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(to_login);

                }

                @Override
                public void onSuccess(UserEnvelope result, int statusCode, Map<String, List<String>> map) {

                    Intent to_main = new Intent(ARTIKLoginActivity.this, main.class);
                    to_main.putExtra("user_email", result.getData().getEmail());
                    to_main.putExtra("user_name", result.getData().getFullName());
                    to_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();

                    overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                    startActivity(to_main);

                    //updateWelcomeViewOnUIThread("Welcome " + result.getData().getFullName());
                }

                @Override
                public void onUploadProgress(long bytes, long contentLen, boolean done) {
                }

                @Override
                public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                }

            });
        } catch (ApiException exc) {
            processFailure(tag, exc);
        }

    }

    private void processFailure(final String context, ApiException exc) {
        String errorDetail = " onFailure with exception" + exc;
        Log.w(context, errorDetail);
        exc.printStackTrace();
        showError(context+errorDetail, ARTIKLoginActivity.this);
    }

    static void showError(final String text, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(activity.getApplicationContext(), text, duration);
                toast.show();
            }
        });
    }

    private void updateWelcomeViewOnUIThread(final String text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mWelcome.setText(text);
            }
        });
    }
}