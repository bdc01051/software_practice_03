package com.example.jiwon_hae.software_practice.account.login;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.account.create_account.create_account;
import com.example.jiwon_hae.software_practice.account.login.volley.request_login;
import com.example.jiwon_hae.software_practice.main;

import org.json.JSONException;
import org.json.JSONObject;

public class login_activity extends AppCompatActivity {

    private ImageView logo_image;
    private Button login_btn;
    private TextView create_account_btn;

    private EditText login_email;
    private EditText login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
//
//        logo_image = (ImageView)findViewById(R.id.logo_image);
//        login_email = (EditText)findViewById(R.id.login_email);
//        login_password = (EditText)findViewById(R.id.login_password);
//
//        Glide.with(this)
//                .load(R.drawable.beer_icon_100)
//                .into(logo_image);
//
//        login_btn = (Button)findViewById(R.id.login_btn);
//        login_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Response.Listener<String> responseListener = new Response.Listener<String>(){
//                    @Override
//                    public void onResponse(String response) {
//                        try{
//                            JSONObject jsonObject = new JSONObject(response);
//                            String server_response = jsonObject.getString("login");
//                            Log.e("server_response", server_response);
//
//                            if (server_response.equals("login_success")){
//                                String username = jsonObject.getString("username");
//
//                                Intent to_main_activity = new Intent(login_activity.this, main.class);
//                                to_main_activity.putExtra("username", username);
//                                startActivity(to_main_activity);
//                            }
//
//                        }catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                request_login create_account_request = new request_login(login_email.getText().toString(), login_password.getText().toString(), responseListener);
//                RequestQueue queue = Volley.newRequestQueue(login_activity.this);
//                queue.add(create_account_request);
//            }
//        });
//
//        create_account_btn = (TextView)findViewById(R.id.create_account);
//        create_account_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent to_create_acc= new Intent(login_activity.this, create_account.class);
//                startActivity(to_create_acc);
//            }
//        });
    }
}
