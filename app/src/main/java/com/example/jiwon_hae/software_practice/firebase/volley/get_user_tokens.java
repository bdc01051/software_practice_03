package com.example.jiwon_hae.software_practice.firebase.volley;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 6. 11..
 */

public class get_user_tokens extends StringRequest {
    private static String request_address = "http://13.125.170.236/software_practice03/database/get_user_tokens.php";

    private Map<String, String> paramters = new HashMap();

    public get_user_tokens(String list_of_friends, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("friends_list", list_of_friends);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
