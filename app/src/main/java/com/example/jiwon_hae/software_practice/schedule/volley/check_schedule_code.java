package com.example.jiwon_hae.software_practice.schedule.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 5. 5..
 */

public class check_schedule_code extends StringRequest{
    private static String request_address = "http://13.125.170.236/software_practice03/database/get_schedule.php";

    private Map<String, String> paramters = new HashMap();

    public check_schedule_code(String code, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("schedule_code", code);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
