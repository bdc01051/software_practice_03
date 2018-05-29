package com.example.jiwon_hae.software_practice.account.login;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.account.login.volley.request_login;
import com.example.jiwon_hae.software_practice.main;

import org.json.JSONException;
import org.json.JSONObject;

public class login_activity extends AppCompatActivity {
    private boolean grant_access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        getSupportActionBar().hide();

        do_login("sample_name", "sample_password");
    }

    private boolean do_login(String user_email, String password){

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String server_response = jsonObject.getString("response");

                    if(server_response.equals("login_failed")){
                        grant_access = false;
                    }else{
                        Intent to_main_activity = new Intent(login_activity.this, main.class);
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        request_login create_account_request = new request_login(user_email, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(create_account_request);

        return grant_access;
    }

}
