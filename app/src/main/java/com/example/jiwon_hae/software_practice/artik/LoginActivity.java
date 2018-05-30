/*
 * Copyright (C) 2017 Samsung Electronics Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jiwon_hae.software_practice.R;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;

import static com.example.jiwon_hae.software_practice.artik.AuthHelper.INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE;
import static com.example.jiwon_hae.software_practice.artik.AuthHelper.USED_INTENT;

public class LoginActivity extends Activity {

    private AuthorizationService mAuthorizationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuthorizationService = new AuthorizationService(this);

        Log.i("LoginActivity", "onCreate complete");
    }

    private void doAuth() {
        AuthorizationRequest authorizationRequest = AuthHelper.createAuthorizationRequest();

        PendingIntent authorizationIntent = PendingIntent.getActivity(
                this,
                authorizationRequest.hashCode(),
                new Intent(INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE, null, this, LoginActivity.class),
                0);

        /* request sample with custom tabs */
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();

        Log.i("LoginActivity", "performing auth...");
        mAuthorizationService.performAuthorizationRequest(authorizationRequest, authorizationIntent, customTabsIntent);
    }

    @Override
    protected void onStart() {
        Log.i("LoginActivity", "Entering onStart...");
        super.onStart();
        checkIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        checkIntent(intent);
    }

    private void checkIntent(@Nullable Intent intent) {
        Log.i("LoginActivity", "intent checking...");

        if (intent != null) {
            String action = intent.getAction();
            if(action == null) {
                Log.i("LoginActivity", "action is null!");
                doAuth();
            }
            else {
                switch (action) {
                    case INTENT_ARTIKCLOUD_AUTHORIZATION_RESPONSE:
                        Log.i("LoginActivity", "artik cloud auth response");
                        if (!intent.hasExtra(USED_INTENT)) {
                            handleAuthorizationResponse(intent);
                            intent.putExtra(USED_INTENT, true);
                        }
                        break;
                    case Intent.ACTION_DEFAULT:
                        Log.i("LoginActivity", "default");
                        doAuth();
                        break;
                }
            }
        }
    }

    private void handleAuthorizationResponse(@NonNull Intent intent) {
        Log.i("LoginActivity", "handle auth response");
        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException error = AuthorizationException.fromIntent(intent);

        if (response != null) {
            if (response.authorizationCode != null ) { // Authorization Code method: succeeded to get code
                Log.i("LoginActivity", "got code!");
                final AuthState authState = new AuthState(response, error);

                // File 2nd call to get the token
                mAuthorizationService.performTokenRequest(response.createTokenExchangeRequest(), new AuthorizationService.TokenResponseCallback() {
                    @Override
                    public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException exception) {
                        if (tokenResponse != null) {
                            authState.update(tokenResponse, exception);
                            AuthManager.saveAuthState(authState);
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
}
