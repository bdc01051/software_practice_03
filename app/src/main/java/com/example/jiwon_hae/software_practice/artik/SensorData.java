package com.example.jiwon_hae.software_practice.artik;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.Map;

import cloud.artik.client.ApiCallback;
import cloud.artik.client.ApiClient;
import cloud.artik.client.ApiException;
import cloud.artik.api.DevicesStatusApi;
import cloud.artik.model.DeviceStatus;

import static com.example.jiwon_hae.software_practice.artik.Config.DEVICE_ID;

public final class SensorData {

    private final boolean presence;
    private final long time;

    private SensorData(boolean presence, long time) {
        this.presence = presence;
        this.time = time;
    }

    public boolean isPresent() {
        return presence;
    }
    public long getTime() {
        return time;
    }

    public static void requestData (final SensorDataListener listener) {

        String accessToken = AuthManager.getAuthState().getAccessToken();
        ApiClient client = new ApiClient();
        client.setAccessToken(accessToken);
        DevicesStatusApi api1 = new DevicesStatusApi(client);

        try {

            api1.getDeviceStatusAsync(DEVICE_ID, true, true,
                    new ApiCallback<DeviceStatus>() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                            listener.onFail(e);
                        }

                        @Override
                        public void onSuccess(DeviceStatus result, int statusCode, Map<String, List<String>> responseHeaders) {
                            Map<String, Object> snapshot = result.getData().getSnapshot();
                            JsonParser parser = new JsonParser();
                            JsonElement elem = parser.parse(new Gson().toJson(snapshot));
                            JsonObject sensor = elem.getAsJsonObject().get("presence").getAsJsonObject();

                            long time = sensor.get("ts").getAsLong();
                            String value = sensor.get("value").getAsString();

                            listener.onSucceed(new SensorData(value.equals("present"), time));
                        }

                        @Override
                        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                        }

                        @Override
                        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                        }
                    });

        } catch (ApiException exc) {
            listener.onFail(exc);
        }
    }
}