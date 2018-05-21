package com.example.jiwon_hae.software_practice.tmap.location_information.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 5. 1..
 */

public class register_current_location extends StringRequest{
    private static final String URL = "http://13.125.170.236";
    private Map<String, String> paramters = new HashMap();

    public register_current_location(String user_id, String current_location, Response.Listener<String> listener) {
        super(1, URL, listener, null);
        this.paramters.put("user_email", user_id);
        this.paramters.put("user_location", current_location);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
