package com.example.jiwon_hae.software_practice.logout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.account.login.login_activity;
import com.example.jiwon_hae.software_practice.artik.AuthManager;
import com.example.jiwon_hae.software_practice.main;

public class logout extends AppCompatActivity {
    private ImageButton YES_btn;
    private ImageButton NO_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        this.YES_btn=(ImageButton)findViewById(R.id.YES_btn);
        Glide.with(this)
                .load(R.drawable.check_icon)
                .thumbnail(0.8f)
                .signature(new StringSignature("a"))
                .into(YES_btn);

        this.YES_btn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               AuthManager.resetAuthState();
               Intent to_login = new Intent(logout.this, login_activity.class);
               to_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               finish();
               startActivity(to_login);
           }
        });

        this.NO_btn=(ImageButton)findViewById(R.id.NO_btn);
        Glide.with(this)
                .load(R.drawable.no_icon)
                .thumbnail(0.8f)
                .signature(new StringSignature("b"))
                .into(NO_btn);

        this.NO_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent to_main = new Intent(logout.this, main.class);
                startActivity(to_main);
            }
        });

    }
}
