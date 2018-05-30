package com.example.jiwon_hae.software_practice.artik;

public interface SensorDataListener {

    // if data request is completed successfully, onSucceed is called.
    // parameter "data" is a SensorData object contains the most recent data.
    void onSucceed(SensorData data);

    // if there is an error while getting data, onFail is called.
    // parameter "exc" is the thrown Exception object.
    void onFail(Exception exc);
}