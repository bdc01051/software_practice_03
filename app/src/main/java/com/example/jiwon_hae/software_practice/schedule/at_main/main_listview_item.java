package com.example.jiwon_hae.software_practice.schedule.at_main;

import java.util.ArrayList;

/**
 * Created by jiwon_hae on 2018. 5. 4..
 */

public class main_listview_item {
    String title;
    String time;
    String date;
    String day;
    ArrayList<String> participants = new ArrayList<>();

    public String getTitle(){
        return this.title;
    }

    public String getDay(){
        return this.day;
    }

}
