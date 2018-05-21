package com.example.jiwon_hae.software_practice.tmap.location_information;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jiwon_hae.software_practice.tmap.location_information.volley.register_current_location;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiwon_hae on 2018. 5. 1..
 */

public class drunken_person extends Service {
    private int startId;
    private Thread update_location_thread;
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        mContext = this;

        if(intent.hasExtra("userID") && intent.hasExtra("notHome") && Boolean.parseBoolean(intent.getStringExtra("notHome"))== true) {
            final String userId = intent.getStringExtra("userID");
            get_user_location get_location_class = new get_user_location(this);
            final String current_location_LatLng = String.valueOf(get_location_class.getCurrentLocation());

            update_location_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Response.Listener<String> responseListener = new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("response");

                                if(success){
                                    Toast.makeText(drunken_person.this, "register_succeess", Toast.LENGTH_SHORT).show();
                                    Log.e("upload_user_location", "success");
                                }else{
                                    Log.e("upload_user_location", "failed");
                                }

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    register_current_location register_location = new register_current_location(userId, current_location_LatLng, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    queue.add(register_location);

                    try {
                        update_location_thread.sleep(300000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        return START_NOT_STICKY;
    }

}
