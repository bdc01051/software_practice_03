package com.example.jiwon_hae.software_practice.account.create_account.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 5. 15..
 */

public class create_account_volley extends StringRequest {
    private static String request_address = "http://13.125.170.236/software_practice03/database/create_account.php";

    private Map<String, String> paramters = new HashMap();

    public create_account_volley(String user_email, String userName, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("user_email", user_email);
        this.paramters.put("user_name", userName);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
