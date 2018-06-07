package com.example.jiwon_hae.software_practice.schedule;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.main;
import com.example.jiwon_hae.software_practice.schedule.volley.create_schedule_volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class create_schedule extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView schedule_id;
    private TextView schedule_date;
    private TextView schedule_day;
    private TextView schedule_time_display;
    private TextView schedule_time;
    private LinearLayout venue_layout;
    private LinearLayout venue_select_layout;
    private Button venue_select_btn;
    private Button change_venue_button;
    private TextView venue_title;
    private EditText schedule_title;

    private CollapsibleCalendar collapsibleCalendar;

    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sharedPreferences;

    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        sharedPreferences = getSharedPreferences("DATABASE", Context.MODE_PRIVATE);

        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("DATABASE",""));
            user_email = jsonObject.getString("user_email");
            Log.e("username", user_email);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        schedule_id = (TextView)findViewById(R.id.schedule_id);
        schedule_date = (TextView)findViewById(R.id.schedule_date);
        schedule_time = (TextView)findViewById(R.id.set_schedule_time_btn);
        schedule_day = (TextView)findViewById(R.id.schedule_day);
        schedule_time_display = (TextView)findViewById(R.id.schedule_time_display);
        venue_layout = (LinearLayout)findViewById(R.id.venue_layout);
        venue_select_btn = (Button)findViewById(R.id.venue_select_btn);
        venue_title = (TextView)findViewById(R.id.schedule_venue_name);
        change_venue_button = (Button)findViewById(R.id.venue_select_btn_1);

        schedule_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_setter_dialog();
            }
        });

        schedule_time_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_setter_dialog();
            }
        });

        String uuid = UUID.randomUUID().toString();
        String[] code = uuid.split("-");
        code_digits = code[0].toUpperCase();

        schedule_id.setText(code_digits);

        collapsibleCalendar = findViewById(R.id.calendarView);
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                String year = String.valueOf(day.getYear());
                String year_ = year.substring(2);

                schedule_date.setText(year_ + "." + (day.getMonth() + 1) + "." + day.getDay());
                schedule_date_text = year_ + "." + (day.getMonth() + 1) + "." + day.getDay();
                try {
                    SimpleDateFormat regularDateFormat = new SimpleDateFormat("dd:MM:yyyy");

                    Date date = regularDateFormat.parse(day.getDay()+":"+(day.getMonth()+1) + ":" + day.getYear());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    schedule_day.setText(getDay(calendar.get(Calendar.DAY_OF_WEEK)));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });

        venue_select_layout = (LinearLayout)findViewById(R.id.select_venue_layout);

        venue_select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PlacePicker.IntentBuilder intentBuilder =
                        new PlacePicker.IntentBuilder();
                try {
                    int PLACE_PICKER_REQUEST = 1;
                    Intent intent = intentBuilder.build(create_schedule.this);
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                // Start the intent by requesting a result,
                // identified by a request code.

            }
        });

        change_venue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PlacePicker.IntentBuilder intentBuilder =
                        new PlacePicker.IntentBuilder();
                try {
                    int PLACE_PICKER_REQUEST = 1;
                    Intent intent = intentBuilder.build(create_schedule.this);
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

        schedule_title = (EditText)findViewById(R.id.schedule_title);

        submit_btn = (Button)findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(code_digits == null || schedule_title.getText().toString()== null || user_email == null|| schedule_date_text == null || schedule_date_time == null || venue_name == null || venue_address == null || venue_latlng== null){
                    Toast.makeText(create_schedule.this, "모든 항목을 기입해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    register_schedule(code_digits, schedule_title.getText().toString(), user_email, schedule_date_text, schedule_date_time, venue_name, venue_address, venue_latlng);
                }
            }
        });

    }

    private String code_digits;
    private String user_email;
    private String schedule_date_text;
    private String schedule_date_time;
    private String venue_name;
    private String venue_latlng;
    private String venue_address;

    private final int REQUEST_PLACE_PICKER = 1234;

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();

            venue_layout.setVisibility(View.VISIBLE);
            venue_select_layout.setVisibility(View.GONE);
            venue_title.setText(name.toString());
            venue_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(create_schedule.this, address,Toast.LENGTH_SHORT).show();
                }
            });

            venue_latlng = String.valueOf(place.getLatLng());
            venue_name = String.valueOf(place.getName());
            venue_address = String.valueOf(place.getAddress());

            //mViewName.setText(name);
            //mViewAddress.setText(address);
            //mViewAttributions.setText(Html.fromHtml(attributions));

        } else {
            Log.e("onActivityResult", String.valueOf(resultCode));
        }
    }

    public String getDay(int day){
        String day_text = "";
        switch(day){
            case 1:
                day_text = "SUN";
                break;
            case 2:
                day_text = "MON";
                break;
            case 3:
                day_text = "TUE";
                break;
            case 4:
                day_text = "WED";
                break;
            case 5:
                day_text = "THUR";
                break;
            case 6:
                day_text = "FRI";
                break;
            case 7:
                day_text = "SAT";
                break;
        }

        return day_text;
    }

    public void time_setter_dialog(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(create_schedule.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                schedule_time.setVisibility(View.INVISIBLE);
                schedule_date_time = selectedHour+ ":"+selectedMinute;

                schedule_time_display.setVisibility(View.VISIBLE);
                schedule_time_display.setText(schedule_date_time);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("SET TIME");
        mTimePicker.show();
    }

    public void register_schedule(String schedule_id,String schedule_title, String user_id, String date, String time, String place_name, String place_address, String placeLatLng){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if(success){
                        Intent to_main = new Intent(create_schedule.this, main.class);
                        startActivity(to_main);
                    }else{
                        Toast.makeText(create_schedule.this, "잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        create_schedule_volley register_schedule_volley = new create_schedule_volley(schedule_id, schedule_title, user_id, date, time, place_name, place_address, placeLatLng, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(register_schedule_volley);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("connection_failed", String.valueOf(connectionResult));
    }
}
