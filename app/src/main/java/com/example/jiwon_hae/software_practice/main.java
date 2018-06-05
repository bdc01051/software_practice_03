package com.example.jiwon_hae.software_practice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jiwon_hae.software_practice.account.create_account.volley.create_account_volley;
import com.example.jiwon_hae.software_practice.account.create_account.volley.request_check_email;
import com.example.jiwon_hae.software_practice.artik.SensorData;
import com.example.jiwon_hae.software_practice.artik.SensorDataListener;
import com.example.jiwon_hae.software_practice.drunk_check.drunk_check;
import com.example.jiwon_hae.software_practice.logout.logout;
import com.example.jiwon_hae.software_practice.schedule.at_main.main_listview_adapter;
import com.example.jiwon_hae.software_practice.schedule.schedule;
import com.example.jiwon_hae.software_practice.tmap.map_navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class main extends AppCompatActivity {
    private ImageButton alcohol_btn;
    private ImageButton navigation_btn;
    private ImageButton drunkcheck_btn;
    private ImageButton logout_btn;

    //Listview
    main_listview_adapter main_display_adapter;

    //Permission
    private static int REQUEST_LOCATION_PERMISSION = 1;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor dbEditor;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("DATABASE", Context.MODE_PRIVATE);
        dbEditor = sharedPreferences.edit();

        handler = new Handler(Looper.getMainLooper());

        if(getIntent().hasExtra("user_email") && getIntent().hasExtra("user_name")){
            Response.Listener<String> responseListener = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if(success){
                            Response.Listener<String> responseListener = new Response.Listener<String>(){
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");

                                        if(success){
                                            Toast.makeText(main.this, "가입해주셔서 감사합니다", Toast.LENGTH_SHORT).show();

                                        }else{
                                            Toast.makeText(main.this, "잠시후에 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                                        }

                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            create_account_volley Validate = new create_account_volley(getIntent().getStringExtra("user_email"), getIntent().getStringExtra("user_name"), responseListener);
                            RequestQueue queue = Volley.newRequestQueue(main.this);
                            queue.add(Validate);
                        }

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            request_check_email Validate = new request_check_email(getIntent().getStringExtra("user_email"), responseListener);
            RequestQueue queue = Volley.newRequestQueue(main.this);
            queue.add(Validate);


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_email", getIntent().getStringExtra("user_email"));
                jsonObject.put("user_name", getIntent().getStringExtra("user_name").replace("_", ""));
                jsonObject.put("auth", sharedPreferences.getString("auth",""));

                dbEditor.putString("DATABASE", jsonObject.toString());
                dbEditor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //dbEditor.putString("user_data");
        }

        this.setImageButtons();

        utility Utility = new utility();
        Utility.RequestPermission(this);

        setToggleButtons();

        ListView main_schedule_display = (ListView)findViewById(R.id.schedule_listView);
        main_display_adapter = new main_listview_adapter(this);
        main_schedule_display.setAdapter(main_display_adapter);

    }

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9998;

    @Override
    protected void onResume() {
        super.onResume();

        getToday_day();


        SensorData.requestData(new SensorDataListener() {
            @Override
            public void onSucceed(SensorData data) {
                displayToast(data.isPresent() ? "present" : "not present");
            }

            @Override
            public void onFail(Exception exc) {
                displayToast("failed : " + exc.getMessage());
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(main.this, "No Permission", Toast.LENGTH_SHORT).show();

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    private void getToday_day(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch(day){
            case Calendar.SUNDAY:
                check_off_toggleButtons("sun", toggleButtonArrayList);
                main_display_adapter.update_schedule_display("sunday");
                break;
            case Calendar.MONDAY:
                check_off_toggleButtons("mon", toggleButtonArrayList);
                main_display_adapter.update_schedule_display("monday");
                break;
            case Calendar.TUESDAY:
                check_off_toggleButtons("tues", toggleButtonArrayList);
                main_display_adapter.update_schedule_display("tuesday");
                break;
            case Calendar.WEDNESDAY:
                check_off_toggleButtons("wed", toggleButtonArrayList);
                main_display_adapter.update_schedule_display("wednesday");
                break;
            case Calendar.THURSDAY:
                check_off_toggleButtons("thurs", toggleButtonArrayList);
                main_display_adapter.update_schedule_display("thursday");
                break;
            case Calendar.FRIDAY:
                check_off_toggleButtons("fri", toggleButtonArrayList);
                main_display_adapter.update_schedule_display("friday");
                break;
            case Calendar.SATURDAY:
                check_off_toggleButtons("sat", toggleButtonArrayList);
                main_display_adapter.update_schedule_display("saturday");
                break;
        }
    }

    private void setImageButtons(){
        this.alcohol_btn = (ImageButton)findViewById(R.id.set_alocohol_imageButton);
        Glide.with(this)
                .load(R.drawable.beer_icon)
                .thumbnail(0.5f)
                .signature(new StringSignature("a"))
                .into(alcohol_btn);

        this.alcohol_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_set_schedule = new Intent(main.this, schedule.class);
                startActivity(to_set_schedule);
            }
        });

        this.drunkcheck_btn = (ImageButton)findViewById(R.id.set_drunkcheck_imageButton);
        Glide.with(this)
                .load(R.drawable.check_icon)
                .thumbnail(0.5f)
                .signature(new StringSignature("c"))
                .into(drunkcheck_btn);

        this.drunkcheck_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_drunckcheck = new Intent(main.this, drunk_check.class);
                startActivity(to_drunckcheck);
            }
        });

        this.logout_btn =(ImageButton)findViewById(R.id.set_logout_imageButton);
        Glide.with(this)
                .load(R.drawable.exit_icon)
                .thumbnail(0.5f)
                .signature(new StringSignature("d"))
                .into(logout_btn);

        this.logout_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent to_logout = new Intent(main.this, logout.class);
                startActivity(to_logout);
            }
        });

        this.navigation_btn = (ImageButton)findViewById(R.id.set_navigation_imageButton);
        Glide.with(this)
                .load(R.drawable.navigation_icon)
                .thumbnail(0.5f)
                .signature(new StringSignature("b"))
                .into(navigation_btn);

        this.navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to_call = new Intent(main.this, calling.class);
                Intent to_navigation = new Intent(main.this, map_navigation.class);
                startActivity(to_navigation);

            }
        });
    }

    private ArrayList<ToggleButton> toggleButtonArrayList = new ArrayList<>();

    private void setToggleButtons(){
        ToggleButton sunday = (ToggleButton)findViewById(R.id.toggle_btn_sunday);
        ToggleButton monday = (ToggleButton)findViewById(R.id.toggle_btn_monday);
        ToggleButton tuesday = (ToggleButton)findViewById(R.id.toggle_btn_tuesday);
        ToggleButton wednesday = (ToggleButton)findViewById(R.id.toggle_btn_wednesday);
        ToggleButton thursday = (ToggleButton)findViewById(R.id.toggle_btn_thursday);
        ToggleButton friday = (ToggleButton)findViewById(R.id.toggle_btn_friday);
        ToggleButton saturday = (ToggleButton)findViewById(R.id.toggle_btn_saturday);

        toggleButtonArrayList.add(sunday);
        toggleButtonArrayList.add(monday);
        toggleButtonArrayList.add(tuesday);
        toggleButtonArrayList.add(wednesday);
        toggleButtonArrayList.add(thursday);
        toggleButtonArrayList.add(friday);
        toggleButtonArrayList.add(saturday);

    }

    private void check_off_toggleButtons(String days, ArrayList<ToggleButton> toggleButtons){
        switch(days) {
            case "sun":
                for(int i = 0; i < toggleButtons.size() ; i++){
                    if(i != 0){
                        toggleButtons.get(i).setChecked(false);
                    }else{
                        toggleButtons.get(i).setChecked(true);
                    }
                }
                break;
            case "mon":
                for(int i = 0; i < toggleButtons.size() ; i++){
                    if(i != 1){
                        toggleButtons.get(i).setChecked(false);
                    }else{
                        toggleButtons.get(i).setChecked(true);
                    }
                }
                break;
            case "tues":
                for(int i = 0; i < toggleButtons.size() ; i++){
                    if(i != 2){
                        toggleButtons.get(i).setChecked(false);
                    }else{
                        toggleButtons.get(i).setChecked(true);
                    }
                }
                break;
            case "wed":
                for(int i = 0; i < toggleButtons.size() ; i++){
                    if(i != 3){
                        toggleButtons.get(i).setChecked(false);
                    }else{
                        toggleButtons.get(i).setChecked(true);
                    }
                }
                break;
            case "thurs":
                for(int i = 0; i < toggleButtons.size() ; i++){
                    if(i != 4){
                        toggleButtons.get(i).setChecked(false);
                    }else{
                        toggleButtons.get(i).setChecked(true);
                    }
                }
                break;
            case "fri":
                for(int i = 0; i < toggleButtons.size() ; i++){
                    if(i != 5){
                        toggleButtons.get(i).setChecked(false);
                    }else{
                        toggleButtons.get(i).setChecked(true);
                    }
                }
                break;
            case "sat":
                for(int i = 0; i < toggleButtons.size() ; i++){
                    if(i != 6){
                        toggleButtons.get(i).setChecked(false);
                    }else{
                        toggleButtons.get(i).setChecked(true);
                    }
                }
                break;

        }
    }

    public void setToggleActions(View view){
        ToggleButton s = (ToggleButton) view;

        switch (view.getId()){
            case R.id.toggle_btn_sunday:
                check_off_toggleButtons("sun", toggleButtonArrayList);
                main_display_adapter.update_schedule_display(view.getTag().toString());
                break;
            case R.id.toggle_btn_monday:
                check_off_toggleButtons("mon", toggleButtonArrayList);
                main_display_adapter.update_schedule_display(view.getTag().toString());
                break;
            case R.id.toggle_btn_tuesday:
                check_off_toggleButtons("tues", toggleButtonArrayList);
                main_display_adapter.update_schedule_display(view.getTag().toString());
                break;
            case R.id.toggle_btn_wednesday:
                check_off_toggleButtons("wed", toggleButtonArrayList);
                main_display_adapter.update_schedule_display(view.getTag().toString());
                break;
            case R.id.toggle_btn_thursday:
                check_off_toggleButtons("thurs", toggleButtonArrayList);
                main_display_adapter.update_schedule_display(view.getTag().toString());
                break;
            case R.id.toggle_btn_friday:
                check_off_toggleButtons("fri", toggleButtonArrayList);
                main_display_adapter.update_schedule_display(view.getTag().toString());
                break;
            case R.id.toggle_btn_saturday:
                check_off_toggleButtons("sat", toggleButtonArrayList);
                main_display_adapter.update_schedule_display(view.getTag().toString());
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(main.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(main.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void displayToast (final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(main.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
