package com.example.jiwon_hae.software_practice.schedule.at_main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiwon_hae.software_practice.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by jiwon_hae on 2018. 5. 4..
 */

public class main_listview_adapter extends BaseAdapter {
    private Context mContext;
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

    public main_listview_adapter(Context context, TextView time_display, TextView am_pm){
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);

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
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.main_listview_item, null);
        TextView schedule_title = view.findViewById(R.id.schedule_name);
        TextView schedule_venue = view.findViewById(R.id.schedule_venue);
        TextView schedule_time = view.findViewById(R.id.schedule_time);
        TextView schedule_date = view.findViewById(R.id.schedule_date);

        schedule_title.setText(current_schedule.get(i).getTitle());
        schedule_venue.setText(current_schedule.get(i).getVenue());
        schedule_time.setText(current_schedule.get(i).getTime());
        schedule_date.setText(current_schedule.get(i).getDate());

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(mContext, "Test Long Click", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        return view;
    }

    public String getDate_today(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd");
        return sdf.format(date);
    }

    public void add_schedule(main_listview_item item){
        try {
            SimpleDateFormat regularDateFormat = new SimpleDateFormat("dd:MM:yyyy");
            String[] date_string = item.getDate().split(Pattern.quote("."));

            int year_calendar, month_calendar, day_calendar;
            day_calendar = Integer.parseInt(date_string[2]);
            month_calendar = Integer.parseInt(date_string[1]);
            year_calendar = Integer.parseInt(date_string[0]) + 2000;

            Date date = regularDateFormat.parse(day_calendar+":"+month_calendar + ":"+ year_calendar);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String day = getDay(calendar.get(Calendar.DAY_OF_WEEK));

            switch(day){
                case "MON":
                    if(check_repetition(monday_schedule, item)){
                        monday_schedule.add(item);
                    }

                    break;
                case "TUE":
                    if(check_repetition(tuesday_schedule, item)){
                        tuesday_schedule.add(item);
                    }
                    break;
                case "WED":
                    if(check_repetition(wednesday_schedule, item)){
                        wednesday_schedule.add(item);
                    }
                    break;
                case "THUR":
                    if(check_repetition(thursday_schedule, item)){
                        thursday_schedule.add(item);
                    }
                    break;
                case "FRI":
                    if(check_repetition(friday_schedule, item)){
                        friday_schedule.add(item);
                    }
                    break;
                case "SAT":
                    if(check_repetition(satday_schedule, item)){
                        satday_schedule.add(item);
                    }
                    break;
                case "SUN":
                    if(check_repetition(sunday_schedule, item)){
                        sunday_schedule.add(item);
                    }
                    break;
            }

            Log.e("today", getDate_today().replace("0",""));
            Log.e("today1", item.getDate());

            if(getDate_today().replace("0","").equals(item.getDate())){
                String[] time = item.getTime().split(":");

                if(Integer.parseInt(time[0]) < 12){
                    if(Integer.parseInt(time[0]) < 10){
                        time_display.setText("0"+time[0] + ":" + time[1]);
                    }
                    am_pm.setText("AM");
                }else{
                    if(Integer.parseInt(time[0])-12 < 10){
                        time_display.setText("0"+ (Integer.parseInt(time[0])-12) + ":" + time[1]);
                    }

                    am_pm.setText("PM");
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public boolean check_repetition(ArrayList<main_listview_item> day, main_listview_item item){
        boolean no_repetition = true;

        for(int i = 0 ; i < day.size() ; i++){
            if(day.get(i).getId().equals(item.getId())){
                no_repetition = false;
            }
        }

        return no_repetition;
    }

    public void clear(){
        current_schedule.clear();
        monday_schedule.clear();
        tuesday_schedule.clear();
        wednesday_schedule.clear();
        thursday_schedule.clear();
        friday_schedule.clear();
        satday_schedule.clear();
        sunday_schedule.clear();
    }

    public void update_schedule_display(String day){
        switch(day){
            case "monday":
                current_schedule = monday_schedule;
                break;
            case "tuesday":
                current_schedule =tuesday_schedule;
                break;
            case "wednesday":
                current_schedule =wednesday_schedule;
                break;
            case "thursday":
                current_schedule =thursday_schedule;
                break;
            case "friday":
                current_schedule =friday_schedule;
                break;
            case "saturday":
                current_schedule =satday_schedule;
                break;
            case "sunday":
                current_schedule =sunday_schedule;
                break;
        }
        notifyDataSetChanged();
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
}
