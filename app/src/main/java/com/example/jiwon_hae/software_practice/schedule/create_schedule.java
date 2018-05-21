package com.example.jiwon_hae.software_practice.schedule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.schedule.volley.create_schedule_volley;

import org.json.JSONException;
import org.json.JSONObject;

public class create_schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
    }

    public void register_schedule(String date, String time, String placeName, String placeLatLng){
        String userName = "sampleUser";

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("response");

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        create_schedule_volley register_schedule_volley = new create_schedule_volley(userName,date, time, placeName, placeLatLng, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(register_schedule_volley);
    }
}
