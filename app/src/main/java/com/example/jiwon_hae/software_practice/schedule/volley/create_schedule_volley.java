package com.example.jiwon_hae.software_practice.schedule.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 5. 5..
 */

public class create_schedule_volley extends StringRequest {

    private static String request_address = "http://13.125.170.236";

    private Map<String, String> paramters = new HashMap();

    public create_schedule_volley(String user_id, String date, String time, String place, String placeLatLng, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("schedule_organizer", user_id);
        this.paramters.put("schedule_date", date);
        this.paramters.put("schedule_time", time);
        this.paramters.put("schedule_place_name", time);
        this.paramters.put("schedule_placeLatLng", placeLatLng);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
