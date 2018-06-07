package com.example.jiwon_hae.software_practice.schedule.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 6. 8..
 */

public class get_user_schedule_volley extends StringRequest{

    private static String request_address = "http://13.125.170.236/software_practice03/database/get_user_schedule.php";

    private Map<String, String> paramters = new HashMap();

    public get_user_schedule_volley(String user_id, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("user_email",user_id);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
