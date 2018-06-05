package com.example.jiwon_hae.software_practice.account.login.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 5. 2..
 */

public class request_login extends StringRequest {
    private static String request_address = "http://13.125.170.236/software_practice03/database/login.php";

    private Map<String, String> paramters = new HashMap();

    public request_login(String user_id, String password, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("login_email", user_id);
        this.paramters.put("login_password", password);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
