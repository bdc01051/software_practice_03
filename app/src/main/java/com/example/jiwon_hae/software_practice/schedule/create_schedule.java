package com.example.jiwon_hae.software_practice.schedule;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.schedule.volley.create_schedule_volley;
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

public class create_schedule extends AppCompatActivity {

    private TextView schedule_id;
    private TextView schedule_date;
    private TextView schedule_day;
    private TextView schedule_time_display;
    private TextView schedule_time;
    private CollapsibleCalendar collapsibleCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        schedule_id = (TextView)findViewById(R.id.schedule_id);
        schedule_date = (TextView)findViewById(R.id.schedule_date);
        schedule_time = (TextView)findViewById(R.id.set_schedule_time_btn);
        schedule_day = (TextView)findViewById(R.id.schedule_day);
        schedule_time_display = (TextView)findViewById(R.id.schedule_time_display);

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
        String code_digits = code[0].toUpperCase();

        schedule_id.setText(code_digits);

        collapsibleCalendar = findViewById(R.id.calendarView);
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                String year = String.valueOf(day.getYear());
                String year_ = year.substring(2);

                schedule_date.setText(year_ + "." + (day.getMonth() + 1) + "." + day.getDay());

                try {
                    SimpleDateFormat regularDateFormat = new SimpleDateFormat("dd:MM:yyyy");

                    Date date = regularDateFormat.parse(day.getDay()+":"+(day.getMonth()+1) + ":" + day.getYear());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    getDay(calendar.get(Calendar.DAY_OF_WEEK));

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
                String time = selectedHour+ ":"+selectedMinute;
                schedule_time_display.setVisibility(View.VISIBLE);
                schedule_time_display.setText(time);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("SET TIME");
        mTimePicker.show();
    }

    public void register_schedule(String date, String time, String placeName, String placeLatLng){
        String userName = "sampleUser";

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("response");

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        create_schedule_volley register_schedule_volley = new create_schedule_volley(userName,date, time, placeName, placeLatLng, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(register_schedule_volley);
    }
}
