package com.example.jiwon_hae.software_practice.artik.service;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jiwon_hae.software_practice.artik.SensorData;
import com.example.jiwon_hae.software_practice.artik.SensorDataListener;
import com.example.jiwon_hae.software_practice.artik.service.volley.get_user_home_info;
import com.example.jiwon_hae.software_practice.artik.service.volley.update_user_home;
import com.example.jiwon_hae.software_practice.artik.service.volley.update_user_location_volley;
import com.example.jiwon_hae.software_practice.firebase.volley.send_fcm_message_volley;
import com.example.jiwon_hae.software_practice.main;
import com.example.jiwon_hae.software_practice.schedule.at_main.main_listview_item;
import com.example.jiwon_hae.software_practice.schedule.create_schedule;
import com.example.jiwon_hae.software_practice.schedule.volley.create_schedule_volley;
import com.example.jiwon_hae.software_practice.schedule.volley.get_schedule_volley;
import com.example.jiwon_hae.software_practice.tmap.location_information.get_user_location;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by jiwon_hae on 2018. 6. 8..
 */

public class artik_service extends Service {
    private String userName;
    private String userToken;
    private String schedule_id;
    private String[] startTime;
    private boolean startLocating;

    int startHour;
    int startMin;

    private BroadcastReceiver mReceiver;

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        start_time = intent.getStringExtra("start_time");
        schedule_id = intent.getStringExtra("schedule_id");

        SharedPreferences sharedPreferences = getSharedPreferences("DATABASE", MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("DATABASE", ""));
            userName = jsonObject.getString("user_name");
            userToken = jsonObject.getString("user_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        startLocating = false;

        track_location_handler = new location_handler();
        user_home = new user_home();

        count_thread = new count_thread(start_time);
        count_thread.start();

        mget_user_location = new get_user_location(getApplicationContext());

        return super.onStartCommand(intent, flags, startId);
    }

    private String start_time;

    private count_thread count_thread = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    location_handler track_location_handler;

    private class location_handler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LatLng myLocation = mget_user_location.getCurrentLocation();
            LatLng place_location = new LatLng(myLocation.latitude, myLocation.longitude);
            updateUserLocation(userName, place_location.toString().replace("lat/lng:", "").replace(" ","").replace(")", "").replace("(", ""));

            Toast.makeText(getApplicationContext(), "USER NOT HOME", Toast.LENGTH_SHORT).show();

            try {
                send_fcm_message(userToken, userName, schedule_id, false, place_location.toString(), "USER NOT HOME", "all");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private user_home user_home;

    private class user_home extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Toast.makeText(getApplicationContext(), "USER HOME", Toast.LENGTH_SHORT).show();

            try {
                send_fcm_message(userToken, userName, schedule_id, true, "none", "USER NOT HOME", "all");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class count_thread extends Thread implements Runnable{
        private int time_count;
        private boolean intiate_locating = false;
        private boolean home = false;
        private boolean stop = false;

        private count_thread(String time){
            String[] time_string = time.split(":");
            int hour = Integer.parseInt(time_string[0]);
            int min = Integer.parseInt(time_string[1]);

            IntentFilter intentfilter = new IntentFilter();
            intentfilter.addAction("com.example.jiwon_hae.software_practice.DRUNK_CHECK");

            mReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean check = intent.getBooleanExtra("checked_not_drunk", false);
                    stop = check;
                }
            };
            registerReceiver(mReceiver, intentfilter);

            //this.time_count = (hour * 60 * min) * 60*1000;
            this.time_count = 5;
        }

        private void startLocating(){
            this.intiate_locating = true;
        }

        @Override
        public void run() {
            super.run();

            while(!this.intiate_locating){
                if(time_count == 0){
                    this.startLocating();
                }
                this.time_count--;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(this.intiate_locating){
                while(!home && !stop){
                    SensorData.requestData(new SensorDataListener() {
                        @Override
                        public void onSucceed(SensorData data) {
                            //home = data.isPresent();
                        }
                        @Override
                        public void onFail(Exception exc) {
                            Log.e("sensor fail", exc.toString());
                        }
                    });

                    try {
                        //Thread.sleep(1000* 5 * 60);
                        Thread.sleep(1000* 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(!home && !stop){
                        track_location_handler.sendEmptyMessage(1);
                    }
                }
            }

            if(home || stop){
                count_thread.interrupt();
                user_home.sendEmptyMessage(1);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mReceiver);
    }

    private get_user_location mget_user_location;

    private void updateUserLocation(String userName, String latlng){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        update_user_location_volley update_user_location_volley = new update_user_location_volley(userName, latlng,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(update_user_location_volley);
    }

    private void send_fcm_message(String token, String userName, String schedule_id, Boolean home, String latlng, String message, String channel) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_name", userName);
        jsonObject.put("user_latlng", latlng);
        jsonObject.put("user_message", message);
        jsonObject.put("user_home", home);
        jsonObject.put("schedule_id", schedule_id);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "FCM message sent", Toast.LENGTH_SHORT).show();
            }
        };

        send_fcm_message_volley send_fcm_volley = new send_fcm_message_volley(token, jsonObject.toString(), channel,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(send_fcm_volley);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
