package com.example.jiwon_hae.software_practice.schedule.at_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiwon_hae.software_practice.R;

import java.util.ArrayList;

/**
 * Created by jiwon_hae on 2018. 5. 4..
 */

public class main_listview_adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> current_schedule = new ArrayList<>();
    //private ArrayList<main_listview_item> current_schedule = new ArrayList<>();

    private ArrayList<String> monday_schedule = new ArrayList<>();
    private ArrayList<String> tuesday_schedule = new ArrayList<>();
    private ArrayList<String> wednesday_schedule = new ArrayList<>();
    private ArrayList<String> thursday_schedule = new ArrayList<>();
    private ArrayList<String> friday_schedule = new ArrayList<>();
    private ArrayList<String> satday_schedule = new ArrayList<>();
    private ArrayList<String> sunday_schedule = new ArrayList<>();

    /*private ArrayList<main_listview_item> monday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> tuesday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> wednesday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> thursday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> friday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> satday_schedule = new ArrayList<>();
    private ArrayList<main_listview_item> sunday_schedule = new ArrayList<>();
    */


    private LayoutInflater inflater;

    public main_listview_adapter(Context context){
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);

        current_schedule.add("sample");
        current_schedule.add("sample1");
        current_schedule.add("sample2");
        current_schedule.add("sample3");
        current_schedule.add("sample4");
        current_schedule.add("sample5");

        monday_schedule.add("monday sample1");
        monday_schedule.add("monday sample2");
        monday_schedule.add("monday sample3");
        monday_schedule.add("monday sample4");

        tuesday_schedule.add("tuesday sample4");
        tuesday_schedule.add("monday sample4");

        wednesday_schedule.add("wed_sample");
        wednesday_schedule.add("wed1_sample");
        wednesday_schedule.add("wed2_sample");

        thursday_schedule.add("thursday_sample");
        thursday_schedule.add("thursday1_sample");
        thursday_schedule.add("thursday2_sample");
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

        schedule_title.setText(current_schedule.get(i));

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(mContext, "Test Long Click", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        return view;
    }

/*
    public void add_schedule(main_listview_item item){
        switch(item.getDay()){
            case "monday":
                monday_schedule.add(0, item);
                break;
            case "tuesday":
                tuesday_schedule.add(0, item);
                break;
            case "wednesday":
                wednesday_schedule.add(0, item);
                break;
            case "thursday":
                thursday_schedule.add(0, item);
                break;
            case "friday":
                friday_schedule.add(0, item);
                break;
            case "saturday":
                satday_schedule.add(0, item);
                break;
            case "sunday":
                sunday_schedule.add(0, item);
                break;
        }
        notifyDataSetChanged();
    }
    */

    public void update_schedule_display(String day){
        switch(day){
            case "monday":
                current_schedule =monday_schedule;
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
}
