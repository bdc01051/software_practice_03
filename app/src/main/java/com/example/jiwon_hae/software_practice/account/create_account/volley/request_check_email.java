package com.example.jiwon_hae.software_practice.account.create_account.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon_hae on 2018. 5. 2..
 */

public class request_check_email extends StringRequest{
    private static String request_address = "http://13.125.170.236/software_practice03/database/validate_email.php";

    private Map<String, String> paramters = new HashMap();

    public request_check_email(String user_id, Response.Listener<String> listener) {
        super(1, request_address, listener, null);
        this.paramters.put("user_email", user_id);
    }

    public Map<String, String> getParams() {
        return this.paramters;
    }
}
