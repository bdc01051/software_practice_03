package com.example.jiwon_hae.software_practice.schedule.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 5. 5..
 */

public class create_schedule_volley extends StringRequest {

    private static String request_address = "http://13.125.170.236/software_practice03/database/register_schedule.php";

    private Map<String, String> paramters = new HashMap();

    public create_schedule_volley(String schedule_id,String schedule_title, String user_id, String date, String start_time,String end_time, String place_name, String place_address, String placeLatLng, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("schedule_id", schedule_id);
        this.paramters.put("schedule_title", schedule_title);
        this.paramters.put("date", date);
        this.paramters.put("start_time", start_time);
        this.paramters.put("end_time", end_time);
        this.paramters.put("venue",place_name);
        this.paramters.put("venue_latlng", placeLatLng);
        this.paramters.put("venue_address", place_address);
        this.paramters.put("user_email",user_id);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
