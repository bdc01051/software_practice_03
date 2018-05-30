package com.example.jiwon_hae.software_practice.artik;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import net.openid.appauth.AuthState;

import org.json.JSONException;

public final class AuthManager {

    private static SharedPreferences preferences;
    private static final String AUTH_PREFERENCES_NAME = "AuthStatePreference";
    private static final String AUTH_STATE = "AUTH_STATE";

    private AuthManager () {}

    public static void init (Activity a) {
        preferences = a.getSharedPreferences(AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    public static void init (Service s) {
        preferences = s.getSharedPreferences(AuthStateDAL.AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static AuthState getAuthState () {
        String str = preferences.getString(AUTH_STATE, null);

        if (!TextUtils.isEmpty(str)) {
            try {
                return AuthState.jsonDeserialize(str);
            } catch(JSONException ignore) { /* do nothing */ }
        }

        return new AuthState();
    }

    public static void saveAuthState (@NonNull AuthState state) {
        preferences .edit()
                    .putString(AUTH_STATE, state.jsonSerializeString())
                    .apply();
    }

    public static void resetAuthState () {
        preferences .edit()
                    .remove(AUTH_STATE)
                    .apply();
    }
}
