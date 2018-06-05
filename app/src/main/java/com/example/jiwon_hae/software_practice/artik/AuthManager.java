package com.example.jiwon_hae.software_practice.artik;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import net.openid.appauth.AuthState;

import org.json.JSONException;

public final class AuthManager {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor preferenceEditor;
    private static final String AUTH_PREFERENCES_NAME = "DATABASE";
    private static final String AUTH_STATE = "AUTH_STATE";

    // AuthManager should NOT be instantiated; it must be shared.
    private AuthManager () {

    }

    // Shared Preferences Initializer
    // must be called before any artik-related processes.
    // it does not need to be done on every activity or service; it is enough to once.
    public static void init (Activity a) {
        preferences = a.getSharedPreferences(AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
    public static void init (Service s) {
        preferences = s.getSharedPreferences(AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // Current Auth State Getter
    public static AuthState getAuthState () {
        String str = preferences.getString(AUTH_STATE, null);

        if (!TextUtils.isEmpty(str)) {
            try {
                return AuthState.jsonDeserialize(str);
            } catch(JSONException ignore) { /* do nothing */ }
        }

        return new AuthState();
    }

    // Auth State Setter
    public static void saveAuthState (@NonNull AuthState state) {
        preferenceEditor.putString("auth", state.jsonSerializeString());
        preferenceEditor.commit();

    }

    // Auth State Cleaner
    // ex) logout from ARTIK cloud
    public static void resetAuthState () {
        preferenceEditor.clear();
        preferenceEditor.commit();

        /*
        preferences .edit()
                    .remove(AUTH_STATE)
                    .apply();

        Log.e("test", AUTH_STATE);
        */
    }
}
