package com.example.jiwon_hae.software_practice.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jiwon_hae.software_practice.R;

public class schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setIconImages();
    }

    public void to_schedule_register(View view){
        Intent to_create_schedule = new Intent(schedule.this, create_schedule.class);
        to_create_schedule.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(to_create_schedule);
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
