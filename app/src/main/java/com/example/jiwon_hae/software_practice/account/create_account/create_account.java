package com.example.jiwon_hae.software_practice.account.create_account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.account.create_account.volley.create_account_volley;
import com.example.jiwon_hae.software_practice.account.login.login_activity;

import org.json.JSONException;
import org.json.JSONObject;

public class create_account extends AppCompatActivity {
    private boolean account_result = false;

    private EditText email_input_edittext;
    private EditText username_input_edittext;
    private EditText password_input_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        create_account("sample_email", "sample_username", "sample_password");
    }

    private void create_account(String email, String username, String password){

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    account_result = jsonObject.getBoolean("success");

                    if(account_result){
                        Intent to_login = new Intent(create_account.this, login_activity.class);
                        startActivity(to_login);
                    }else{
                        Toast.makeText(create_account.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                        Toast.makeText(create_account.this, "잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        create_account_volley create_account_request = new create_account_volley(email, username, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(create_account_request);

        Log.e("account_result", String.valueOf(account_result));
    }
}
