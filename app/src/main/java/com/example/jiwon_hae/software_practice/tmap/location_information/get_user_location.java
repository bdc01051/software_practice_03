package com.example.jiwon_hae.software_practice.tmap.location_information;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jiwon_hae on 2018. 5. 1..
 */

public class get_user_location {
    private Context mContext;
    private LocationListener locationListener;

    public get_user_location(Context mContext){
        this.mContext = mContext;

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }


    public LatLng getCurrentLocation() {
        Location location;
        LatLng user_current_location = null;

        LocationManager locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

        boolean isNetworkEnabled = locationManager.isProviderEnabled("network");
        boolean isGPSEnabled = locationManager.isProviderEnabled("gps");

        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates("network", 1000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, this.locationListener);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return user_current_location;
            }
            location = locationManager.getLastKnownLocation("network");
            user_current_location = new LatLng(location.getLatitude(), location.getLongitude());
        } else if (isGPSEnabled) {
            locationManager.requestLocationUpdates("gps", 1000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, this.locationListener);
            location = locationManager.getLastKnownLocation("gps");
            user_current_location = new LatLng(location.getLatitude(), location.getLongitude());
        }

        return user_current_location;
    }
}
