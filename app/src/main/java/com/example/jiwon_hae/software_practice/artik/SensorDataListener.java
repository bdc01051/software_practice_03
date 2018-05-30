package com.example.jiwon_hae.software_practice.artik;

public interface SensorDataListener {
    void onSucceed(SensorData data);
    void onFail(Exception exc);
}