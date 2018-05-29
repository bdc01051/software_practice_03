package com.example.jiwon_hae.software_practice.artik;

public interface SensorDataListener {
    void onPresent (int since, int current);
    void onLeave (int since, int current);
    void onFail (Exception error);
}
