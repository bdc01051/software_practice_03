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
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.artik.ARTIKLoginActivity;
import com.example.jiwon_hae.software_practice.artik.AuthManager;

import org.json.JSONException;
import org.json.JSONObject;

public class login_activity extends AppCompatActivity {
    private ImageView logo_image;
    private Button artik_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);

        if(AuthManager.init(this)) {
            Intent auto_login = new Intent(login_activity.this, ARTIKLoginActivity.class);
            auto_login.putExtra("auto_login", true);
            startActivity(auto_login);
        }

        artik_btn = (Button)findViewById(R.id.artik_btn);
        artik_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_create_acc= new Intent(login_activity.this, ARTIKLoginActivity.class);
                startActivity(to_create_acc);
            }
        });
    }


}
