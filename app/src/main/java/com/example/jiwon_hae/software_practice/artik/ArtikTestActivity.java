package com.example.jiwon_hae.software_practice.artik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jiwon_hae.software_practice.R;

public class ArtikTestActivity extends Activity {

    private TextView txt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artiktest);

        final Button login_btn = (Button) findViewById(R.id.login_btn);
        final Button get_msg_btn = (Button) findViewById(R.id.get_data_btn);
        txt = (TextView) findViewById(R.id.data_txt);

        AuthManager.init(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
            }
        });

        get_msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText("updating...");
                SensorData.requestData(new SensorDataListener() {
                    @Override
                    public void onSucceed(SensorData data) {
                        changeText("data : " + data.isPresent() + ", " + data.getTime());
                    }

                    @Override
                    public void onFail(Exception exc) {
                        changeText("fail : " + exc.getMessage());
                    }
                });
            }
        });

    }

    private void startLoginActivity () {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void changeText (final String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt.setText(str);
                    }
                });
            }
        }).start();
    }
}
