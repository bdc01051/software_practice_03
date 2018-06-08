package com.example.jiwon_hae.software_practice.firebase;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 6. 9..
 */

public class update_fcm_token extends StringRequest {
    private static String request_address = "http://13.125.170.236/software_practice03/database/update_fcm_token.php";

    private Map<String, String> paramters = new HashMap();

    public update_fcm_token(String user_email, String user_token, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("user_email", user_email);
        this.paramters.put("user_token", user_token);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }

}
