package com.example.jiwon_hae.software_practice.account.create_account.volley;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 5. 28..
 */

public class validate_username extends StringRequest {
    final static private String URL = "http://13.125.170.236/software_practice03/database/validate_username.php";
    private Map<String, String> paramters;

    public validate_username(String username, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        paramters = new HashMap<>();
        paramters.put("userName", username);
    }

    @Override
    public Map<String, String> getParams() {
        return paramters;
    }
}
