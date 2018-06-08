package com.example.jiwon_hae.software_practice.tmap;

import android.content.Context;
import android.location.LocationListener;
import android.util.Log;

import com.bumptech.glide.load.Key;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.skt.Tmap.TMapGpsManager;

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

/**
 * Created by jiwon_hae on 2018. 5. 1..
 */

public class tmap {
    private final String TMAPApiKey = "81048e2f-85cb-4d5e-bd9b-f273347fe20f";
    private Context mContext;
    private static int mMarkerID;

    private LatLng user_current_location;

    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<LatLng> mapPoints = new ArrayList<>();
    private TMapGpsManager gps = null;
    private MapView mMapView;

    private LocationListener locationListener;
/*
    public tmap(Context context, MapView mMapView) {
        this.mContext = context;
        TMapTapi tmaptapi = new TMapTapi(mContext);
        tmaptapi.setSKTMapAuthentication(TMAPApiKey);
        this.mMarkerID = 0;
        this.mMapView = mMapView;

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
    }*/

    public ArrayList<LatLng> getLocationData (final String nav_type, final LatLng startPoint, final LatLng endPoint) {
        HttpClient httpClient = new DefaultHttpClient();
        String urlString = "";

        if (nav_type.equals("pedestrian")) {
            urlString = "https://apis.skplanetx.com/tmap/routes/" + nav_type + "?version=1&format=json&appKey="+ this.TMAPApiKey;
        } else if (nav_type.equals("car")) {
            urlString = "https://apis.skplanetx.com/tmap/routes?version=1&format=json&appKey="+this.TMAPApiKey;
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
                this.mapPoints = new ArrayList();

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
                        this.mapPoints.add(new LatLng(latJson, lonJson));
                    }
                    if (geoType.equals("LineString")) {
                        for (int j = 0; j < coordinates.length(); j++) {
                            JSONArray JLinePoint = coordinates.getJSONArray(j);
                            lonJson = JLinePoint.getDouble(0);
                            latJson = JLinePoint.getDouble(1);
                            Log.d("TAG", "-");
                            Log.d("TAG", lonJson + "," + latJson + "\n");
                            //review_adapter.this.mapPoints.add(new LatLng(latJson, lonJson));
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

        return this.mapPoints;
    }

}
