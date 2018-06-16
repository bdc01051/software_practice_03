package com.example.jiwon_hae.software_practice.schedule.at_main;

import java.util.ArrayList;

/**
 * Created by jiwon_hae on 2018. 5. 4..
 */

public class main_listview_item {
    String title;
    String start_time;
    String end_time;
    String date;
    String latlng;
    String venue;
    String participants;
    String id;

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setVenue(String venue){
        this.venue = venue;
    }

    public String getVenue(){
        return this.venue;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setParticipants(String participants){
        this.participants = participants;
    }

    public void setStartTime(String time){
        this.start_time = time;
    }
    public void setEndTime(String time){
        this.end_time = time;
    }

    public void setLatlng(String latlng){
        this.latlng = latlng;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTitle(){
        return this.title;
    }

    public String getParticipants(){
        return this.participants;
    }

    public String getStartTime(){
        return this.start_time;
    }

    public String getEndTime(){
        return this.end_time;
    }

    public String getLatlng(){
        return this.latlng;
    }

    public String getDate(){
        return this.date;
    }

}
