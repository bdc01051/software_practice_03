package com.example.jiwon_hae.software_practice.firebase.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 6. 10..
 */

public class send_fcm_message_volley extends StringRequest {
    private static String request_address = "http://13.125.170.236/software_practice03/database/send_fcm_messages.php";

    private Map<String, String> paramters = new HashMap();

    public send_fcm_message_volley(String user_token, String message, String channel, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("user_token", user_token);
        this.paramters.put("message",message);
        this.paramters.put("channel", channel);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
