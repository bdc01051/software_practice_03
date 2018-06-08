package com.example.jiwon_hae.software_practice.tmap;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.Key;
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.tmap.location_information.get_user_location;
import com.example.jiwon_hae.software_practice.tmap.location_information.volley.request_user_location_volley;
import com.example.jiwon_hae.software_practice.utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.example.jiwon_hae.software_practice.utility.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class map_navigation extends AppCompatActivity implements OnMapReadyCallback{

    private int REQUEST_LOCATION_PERMISSION = 12345;
    private Fragment mapView;

    private ToggleButton request_walking_path;
    private ToggleButton request_driving_path;

    //Location Information
    private LatLng myLocation;
    private Thread nav_thread;

    //Navigation Information
    Polyline car_path;
    Polyline walk_path;

    //Samples
    private LatLng sampleDATA;
    private LatLng sampleDATA1;
    private String friendsID ;

    get_user_location mGet_user_location;

    private SupportMapFragment mapFragment;

    private GoogleMap googleMaps;
    private boolean polyline_car_check = false;
    private boolean polyline_walk_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navigation);

        mGet_user_location = new get_user_location(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        request_walking_path =(ToggleButton)findViewById(R.id.walking_nav_btn);
        request_driving_path =(ToggleButton)findViewById(R.id.car_nav_btn);

        sampleDATA = new LatLng(37.300583, 126.970785);
        sampleDATA1 = new LatLng(37.266773, 126.999418);

        setToggleButtonActions();
        //request_for_userLocation(friendsID);
    }

    private void setToggleButtonActions(){
        request_walking_path.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    request_driving_path.setChecked(false);

                    try {
                        getLocationData("pedestrian", mGet_user_location.getCurrentLocation(), sampleDATA);
                        ArrayList<LatLng> path = mapPoints;

                        if (path != null) {
                            polyline_walk_check = true;
                            walk_path = googleMaps.addPolyline(new PolylineOptions().clickable(false).addAll(path).color(-16776961).width(10.0f));
                            return;
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(polyline_walk_check) {
                        walk_path.remove();
                        mapPoints.clear();
                    }
                }
            }
        });

        request_driving_path.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    request_walking_path.setChecked(false);

                    try {
                        //getLocationData("car", mGet_user_location.getCurrentLocation(), sampleDATA);
                        getLocationData("car", sampleDATA1, sampleDATA);
                        ArrayList<LatLng> path = mapPoints;

                        if (path != null) {
                            polyline_car_check = true;
                            car_path = googleMaps.addPolyline(new PolylineOptions().clickable(false).addAll(path).color(-16776961).width(10.0f));
                            return;
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(polyline_car_check){
                        car_path.remove();
                        mapPoints.clear();
                    }

                }
            }
        });
    }


    private LatLng request_for_userLocation(String userID){
        final String[] location_information = new String[1];
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("response");

                    if(success){
                        location_information[0] = jsonObject.getString("location_information");
                        Toast.makeText(map_navigation.this, "register_succeess", Toast.LENGTH_SHORT).show();
                        Log.e("request_user_location", location_information.toString());

                    }else{
                        Log.e("request_user_location", "failed");
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        request_user_location_volley request_location = new request_user_location_volley(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request_location);

        String[] latLng = location_information.toString().split(",");
        double latitude = Double.parseDouble(latLng[0]);
        double longitude = Double.parseDouble(latLng[1]);

        return new LatLng(latitude, longitude);
    }

    @Override
    protected void onPause() {
        super.onPause();

        utility Utility = new utility();
        Utility.RequestPermission(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Toast.makeText(map_navigation.this, String.valueOf(requestCode), Toast.LENGTH_SHORT).show();

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(map_navigation.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(map_navigation.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMaps = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            getCurret_user_location(googleMap);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getCurret_user_location(GoogleMap googleMap){
        myLocation = mGet_user_location.getCurrentLocation();
        LatLng place_location = new LatLng(myLocation.latitude, myLocation.longitude);
        googleMap.addMarker(new MarkerOptions().position(place_location).title("My Location")).showInfoWindow();

        googleMap.addMarker(new MarkerOptions().position(sampleDATA).title("Destination")).showInfoWindow();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place_location, 16.0f));
        googleMap.setMapType(1);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    public ArrayList<LatLng> mapPoints = new ArrayList<>();

    public ArrayList<LatLng> getLocationData (final String nav_type, final LatLng startPoint, final LatLng endPoint) throws InterruptedException {
        Log.e("test", nav_type);

        Thread nav_thread = new Thread() {
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                String urlString = "";

                if (nav_type.equals("pedestrian")) {
                    urlString = "https://api2.sktelecom.com/tmap/routes/pedestrian?version=1&format=json&appKey=81048e2f-85cb-4d5e-bd9b-f273347fe20f";
                } else if (nav_type.equals("car")) {
                    urlString = "https://api2.sktelecom.com/tmap/routes?version=1&format=json&appKey=81048e2f-85cb-4d5e-bd9b-f273347fe20f";
                }

                try {
                    URI uri = new URI(urlString);
                    HttpPost httpPost = new HttpPost();
                    httpPost.setURI(uri);
                    List<BasicNameValuePair> nameValuePairs = new ArrayList();
                    nameValuePairs.add(new BasicNameValuePair("startX", Double.toString(startPoint.longitude)));
                    nameValuePairs.add(new BasicNameValuePair("startY", Double.toString(startPoint.latitude)));
                    nameValuePairs.add(new BasicNameValuePair("endX", Double.toString(endPoint.longitude)));
                    nameValuePairs.add(new BasicNameValuePair("endY", Double.toString(endPoint.latitude)));
                    nameValuePairs.add(new BasicNameValuePair("startName", "출발지"));
                    nameValuePairs.add(new BasicNameValuePair("endName", "도착지"));
                    nameValuePairs.add(new BasicNameValuePair("reqCoordType", "WGS84GEO"));
                    nameValuePairs.add(new BasicNameValuePair("resCoordType", "WGS84GEO"));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    int code = response.getStatusLine().getStatusCode();
                    String message = response.getStatusLine().getReasonPhrase();

                    if (response.getEntity() != null) {
                        String responseString = EntityUtils.toString(response.getEntity(), Key.STRING_CHARSET_NAME);
                        String strData = "";
                        Log.d("TAG", "0\n");
                        JSONObject jSONObject = new JSONObject(responseString);
                        Log.d("TAG", "1\n");
                        JSONArray features = jSONObject.getJSONArray("features");

                        for (int i = 0; i < features.length(); i++) {
                            double lonJson;
                            double latJson;
                            JSONObject test2 = features.getJSONObject(i);
                            if (i == 0) {
                                test2.getJSONObject("properties");
                            }
                            JSONObject geometry = test2.getJSONObject("geometry");
                            JSONArray coordinates = geometry.getJSONArray("coordinates");
                            String geoType = geometry.getString("type");
                            if (geoType.equals("Point")) {
                                lonJson = coordinates.getDouble(0);
                                latJson = coordinates.getDouble(1);
                                Log.d("TAG", "-");
                                Log.d("TAG", lonJson + "," + latJson + "\n");
                                mapPoints.add(new LatLng(latJson, lonJson));
                            }
                            if (geoType.equals("LineString")) {
                                for (int j = 0; j < coordinates.length(); j++) {
                                    JSONArray JLinePoint = coordinates.getJSONArray(j);
                                    lonJson = JLinePoint.getDouble(0);
                                    latJson = JLinePoint.getDouble(1);
                                    Log.d("TAG", "-");
                                    Log.d("TAG", lonJson + "," + latJson + "\n");
                                    mapPoints.add(new LatLng(latJson, lonJson));
                                }
                            }
                        }
                    }

                } catch (URISyntaxException e) {
                    Log.e("TAG", e.getLocalizedMessage());
                    e.printStackTrace();
                } catch (ClientProtocolException e2) {
                    Log.e("TAG", e2.getLocalizedMessage());
                    e2.printStackTrace();
                } catch (IOException e3) {
                    Log.e("TAG", e3.getLocalizedMessage());
                    e3.printStackTrace();
                } catch (JSONException e4) {
                    e4.printStackTrace();
                }
            }

        };

        nav_thread.start();

        try {
            nav_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nav_thread.interrupt();

        return mapPoints;
    }

}
