package com.example.jiwon_hae.software_practice.artik.service.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 6. 8..
 */

public class update_user_location_volley extends StringRequest {
    private static String request_address = "http://13.125.170.236/software_practice03/database/update_user_location.php";

    private Map<String, String> paramters = new HashMap();

    public update_user_location_volley(String userId, String user_latlng, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("user_email", userId);
        this.paramters.put("user_latlng", user_latlng);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
