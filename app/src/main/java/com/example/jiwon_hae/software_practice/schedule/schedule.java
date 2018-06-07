package com.example.jiwon_hae.software_practice.schedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.jiwon_hae.software_practice.main;
import com.example.jiwon_hae.software_practice.schedule.volley.check_schedule_code;
import com.example.jiwon_hae.software_practice.schedule.volley.create_schedule_volley;
import com.example.jiwon_hae.software_practice.schedule.volley.join_schedule_volley;

import org.json.JSONException;
import org.json.JSONObject;

public class schedule extends AppCompatActivity {
    private EditText schedule_id_editText;
    private String schedule_id;
    private String user_email;
    private Button join_schedule_btn;
    private Button create_schedule_btn;

    private TextView schedule_title;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        sharedPreferences = getSharedPreferences("DATABASE", Context.MODE_PRIVATE);

        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("DATABASE",""));
            user_email = jsonObject.getString("user_email");
            Log.e("username", user_email);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        schedule_title = (TextView)findViewById(R.id.schedule_title);

        join_schedule_btn = (Button)findViewById(R.id.join_schedule_btn);
        create_schedule_btn = (Button)findViewById(R.id.create_schedule_btn);

        setIconImages();
        schedule_id_editText = (EditText)findViewById(R.id.schedule_id);

        schedule_id_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success){
                                join_schedule_btn.setVisibility(View.VISIBLE);
                                create_schedule_btn.setVisibility(View.GONE);

                                String title = jsonObject.getString("schedule_title");
                                schedule_title.setText(title);
                                schedule_id = String.valueOf(charSequence);

                            }else{
                                join_schedule_btn.setVisibility(View.GONE);
                                create_schedule_btn.setVisibility(View.VISIBLE);
                                schedule_title.setText("title of the schedule");
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                check_schedule_code register_schedule_volley = new check_schedule_code(String.valueOf(charSequence), responseListener);
                RequestQueue queue = Volley.newRequestQueue(schedule.this);
                queue.add(register_schedule_volley);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void to_schedule_register(View view){
        Intent to_create_schedule = new Intent(schedule.this, create_schedule.class);
        to_create_schedule.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(to_create_schedule);
    }

    public void join_schedule(View view){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    boolean success = jsonObject.getBoolean("success");

                    if(success){
                        Intent to_main = new Intent(schedule.this, main.class);
                        startActivity(to_main);
                    }

                    boolean existing_member = jsonObject.getBoolean("exisiting_member");
                    if(existing_member){
                        Toast.makeText(schedule.this, "이미 참석한 일정입니다", Toast.LENGTH_SHORT).show();
                    }


                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        join_schedule_volley join_schedule_volley = new join_schedule_volley(schedule_id, user_email, responseListener);
        RequestQueue queue = Volley.newRequestQueue(schedule.this);
        queue.add(join_schedule_volley);
    }

    public void setIconImages(){
        ImageView logo_icon = (ImageView)findViewById(R.id.application_logo);

        Glide.with(this)
                .load(R.drawable.beer_icon_100)
                .signature(new StringSignature("logo_B"))
                .into(logo_icon);

        ImageView title_icon = (ImageView)findViewById(R.id.schedule_title_icon);
        Glide.with(this)
                .load(R.drawable.title_icon)
                .thumbnail(0.5f)
                .signature(new StringSignature("title_A"))
                .into(title_icon);

        ImageView lock_icon = (ImageView)findViewById(R.id.schedule_lock_icon);
        Glide.with(this)
                .load(R.drawable.lock_icon1)
                .thumbnail(0.5f)
                .signature(new StringSignature("lock_A"))
                .into(lock_icon);
    }

}
