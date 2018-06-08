package com.example.jiwon_hae.software_practice.schedule.at_main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.artik.service.artik_service;
import com.example.jiwon_hae.software_practice.drunk_check.drunk_check;
import com.example.jiwon_hae.software_practice.main;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import cloud.artik.client.JSON;

/**
 * Created by jiwon_hae on 2018. 5. 4..
 */

public class main_listview_adapter extends BaseAdapter{
    private Context mContext;
    private Activity mActivity;
    //private ArrayList<String> current_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> current_schedule = new ArrayList<>();

    private ArrayList<main_listview_item> monday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> tuesday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> wednesday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> thursday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> friday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> satday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> sunday_schedule = new ArrayList<>();

    private LayoutInflater inflater;
    private TextView time_display;
    private TextView am_pm;


    private String userName;

    public main_listview_adapter(Context context, String userName, TextView time_display, TextView am_pm) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.userName = userName;
        this.time_display = time_display;
        this.am_pm = am_pm;
    }

    @Override
    public int getCount() {
        return current_schedule.size();
    }

    @Override
    public Object getItem(int i) {
        return current_schedule.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.main_listview_item, null);
        TextView schedule_title = view.findViewById(R.id.schedule_name);
        TextView schedule_venue = view.findViewById(R.id.schedule_venue);
        TextView schedule_time = view.findViewById(R.id.schedule_time);
        TextView schedule_date = view.findViewById(R.id.schedule_date);

        schedule_title.setText(current_schedule.get(i).getTitle());
        schedule_venue.setText(current_schedule.get(i).getVenue());
        schedule_time.setText(current_schedule.get(i).getStartTime() + " ~ " + current_schedule.get(i).getEndTime());
        schedule_date.setText(current_schedule.get(i).getDate());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display_select_schedule(current_schedule.get(i));
            }

        });

        return view;
    }

    public void display_select_schedule(final main_listview_item item) {
        View view = inflater.inflate(R.layout.schedule_alert_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setPositiveButton("SELECT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseMessaging.getInstance().subscribeToTopic(item.getId());

                        saveCurrent_schedule(item);
                        try {
                            setCurrent_schedule(time_display, am_pm);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent start_artik = new Intent(
                                mContext,//현재제어권자
                                artik_service.class); // 이동할 컴포넌트

                        start_artik.putExtra("userName", userName);
                        start_artik.putExtra("start_time", item.getEndTime());

                        mContext.startService(start_artik);

                        /*if (getDate_today().replace("0", "").equals(item.getDate())) {
                            saveCurrent_schedule(item);
                            try {
                                setCurrent_schedule(time_display, am_pm);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent start_artik = new Intent(
                                    mContext,//현재제어권자
                                    artik_service.class); // 이동할 컴포넌트

                            start_artik.putExtra("userName", userName);
                            start_artik.putExtra("startTime", item.getDate());

                            mContext.startService(start_artik);
                        }else{
                            Toast.makeText(mContext, "오늘 일정이 아닙니다", Toast.LENGTH_SHORT).show();
                        }
                        */

                    }
                });

        TextView title = view.findViewById(R.id.schedule_title);
        title.setText(item.getTitle());

        TextView participants = view.findViewById(R.id.schedule_participants);
        participants.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        participants.setSingleLine(true);
        participants.setSelected(true);

        participants.setText(item.getParticipants().replace("$", " , "));

        TextView date = view.findViewById(R.id.schedule_date);
        date.setText(item.getStartTime());

        TextView startTime = view.findViewById(R.id.schedule_start_time_display);
        startTime.setText(item.getStartTime());

        TextView endTime = view.findViewById(R.id.schedule_end_time_display);
        endTime.setText(item.getEndTime());

        TextView venue = view.findViewById(R.id.schedule_venue_name);
        venue.setText(item.getVenue());

        MapView mapview = view.findViewById(R.id.mapView);

        mapview.onCreate(null);
        mapview.onResume();

        mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                String location = item.getLatlng().replace(")", "").replace("(", "").replace("lat/lng:","").replace(" ","");
                String[] latlng = location.split(",");

                LatLng position = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));

                googleMap.addMarker(new MarkerOptions().position(position).title(item.getTitle())).showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16.0f));
            }
        });

        AlertDialog display_schedule = builder.create();
        display_schedule.create();

        display_schedule.setCanceledOnTouchOutside(true);
        display_schedule.show();
    }

    public String getDate_today() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd");
        return sdf.format(date);
    }

    public void add_schedule(main_listview_item item) {
        try {
            SimpleDateFormat regularDateFormat = new SimpleDateFormat("dd:MM:yyyy");
            String[] date_string = item.getDate().split(Pattern.quote("."));

            int year_calendar, month_calendar, day_calendar;
            day_calendar = Integer.parseInt(date_string[2]);
            month_calendar = Integer.parseInt(date_string[1]);
            year_calendar = Integer.parseInt(date_string[0]) + 2000;

            Date date = regularDateFormat.parse(day_calendar + ":" + month_calendar + ":" + year_calendar);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String day = getDay(calendar.get(Calendar.DAY_OF_WEEK));

            switch (day) {
                case "MON":
                    if (check_repetition(monday_schedule, item)) {
                        monday_schedule.add(item);
                    }

                    break;
                case "TUE":
                    if (check_repetition(tuesday_schedule, item)) {
                        tuesday_schedule.add(item);
                    }
                    break;
                case "WED":
                    if (check_repetition(wednesday_schedule, item)) {
                        wednesday_schedule.add(item);
                    }
                    break;
                case "THUR":
                    if (check_repetition(thursday_schedule, item)) {
                        thursday_schedule.add(item);
                    }
                    break;
                case "FRI":
                    if (check_repetition(friday_schedule, item)) {
                        friday_schedule.add(item);
                    }
                    break;
                case "SAT":
                    if (check_repetition(satday_schedule, item)) {
                        satday_schedule.add(item);
                    }
                    break;
                case "SUN":
                    if (check_repetition(sunday_schedule, item)) {
                        sunday_schedule.add(item);
                    }
                    break;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public boolean check_repetition(ArrayList<main_listview_item> day, main_listview_item item) {
        boolean no_repetition = true;

        for (int i = 0; i < day.size(); i++) {
            if (day.get(i).getId().equals(item.getId())) {
                no_repetition = false;
            }
        }

        return no_repetition;
    }

    public void clear() {
        current_schedule.clear();
        monday_schedule.clear();
        tuesday_schedule.clear();
        wednesday_schedule.clear();
        thursday_schedule.clear();
        friday_schedule.clear();
        satday_schedule.clear();
        sunday_schedule.clear();
    }

    public void update_schedule_display(String day) {
        switch (day) {
            case "monday":
                current_schedule = monday_schedule;
                break;
            case "tuesday":
                current_schedule = tuesday_schedule;
                break;
            case "wednesday":
                current_schedule = wednesday_schedule;
                break;
            case "thursday":
                current_schedule = thursday_schedule;
                break;
            case "friday":
                current_schedule = friday_schedule;
                break;
            case "saturday":
                current_schedule = satday_schedule;
                break;
            case "sunday":
                current_schedule = sunday_schedule;
                break;
        }
        notifyDataSetChanged();
    }

    private SharedPreferences schedule_;
    private SharedPreferences.Editor schedule_edit;

    public void saveCurrent_schedule(main_listview_item item){
        schedule_ = mContext.getSharedPreferences("SCHEDULE", Context.MODE_PRIVATE);
        schedule_edit = schedule_.edit();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("start_time", item.getStartTime());
            jsonObject.put("end_time", item.getEndTime());

            schedule_edit.putString("schedule", jsonObject.toString());
            schedule_edit.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCurrent_schedule(TextView set_time_display, TextView set_am_pm) throws JSONException {
        SharedPreferences schedule_ = mContext.getSharedPreferences("SCHEDULE", Context.MODE_PRIVATE);

        if(schedule_.getString("schedule", "").contains("start_time")) {
            JSONObject jsonObject = new JSONObject(schedule_.getString("schedule", ""));
            String[] time = jsonObject.getString("start_time").split(":");

            if (Integer.parseInt(time[0]) < 12) {
                if (Integer.parseInt(time[0]) < 10) {
                    set_time_display.setText("0" + time[0] + ":" + time[1]);
                }
                set_am_pm.setText("AM");
            } else {
                if (Integer.parseInt(time[0]) - 12 < 10) {
                    set_time_display.setText("0" + (Integer.parseInt(time[0]) - 12) + ":" + time[1]);
                }
                set_am_pm.setText("PM");
            }
        }else{
            set_am_pm.setText("");
            set_time_display.setText("NONE");
        }

    }

    public String getDay(int day) {
        String day_text = "";
        switch (day) {
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
}
