package com.example.jiwon_hae.software_practice.firebase;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jiwon_hae.software_practice.main;
import com.example.jiwon_hae.software_practice.schedule.at_main.main_listview_item;
import com.example.jiwon_hae.software_practice.schedule.volley.get_schedule_volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiwon_hae on 2018. 6. 9..
 */

public class firebase_instance_id extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        try {
            sendRegistrationToServer(refreshedToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // [END refresh_token]

    private void sendRegistrationToServer(String token) throws JSONException {
        // TODO: Implement this method to send token to your app server.

        SharedPreferences user_info = getApplicationContext().getSharedPreferences("DATABASE", Context.MODE_PRIVATE);
        JSONObject jsonObject = new JSONObject(user_info.getString("DATABASE",""));
        String user_email = jsonObject.getString("user_email");

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        update_fcm_token get_schedule_info = new update_fcm_token(user_email, token, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(get_schedule_info);
    }
}
