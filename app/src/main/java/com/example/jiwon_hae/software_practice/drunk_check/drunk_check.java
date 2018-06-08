package com.example.jiwon_hae.software_practice.drunk_check;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.main;

public class drunk_check extends AppCompatActivity {
    private Button button2;
    TextView TestCase;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drunk_check);

        TestCase= (TextView) findViewById(R.id.textView3);
        TestCase.setText(getResources().getString(R.string.Test_DrunkCheck_case));
        TestCase.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        TestCase.setSingleLine(true);
        TestCase.setSelected(true);

        final String B = TestCase.getText().toString();

        this.button2 = (Button)findViewById(R.id.Drunk_Check_Test);
        this.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText = (EditText)findViewById(R.id.editText6);
                String A = editText.getText().toString();

                if(A.equals(B)){
                    SharedPreferences schedule_ = getSharedPreferences("SCHEDULE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor schedule_editor = schedule_.edit();

                    schedule_editor.putString("schedule", null);
                    schedule_editor.commit();

                    Toast.makeText(drunk_check.this,"안전히 귀가하세요",Toast.LENGTH_SHORT).show();

                    Intent to_main = new Intent(drunk_check.this, main.class);
                    startActivity(to_main);

                    Intent sendIntent = new Intent("com.example.jiwon_hae.software_practice.DRUNK_CHECK");
                    sendIntent.putExtra("checked_not_drunk", true);
                    sendBroadcast(sendIntent);

                }
                else
                    Toast.makeText(drunk_check.this,"아직 취하신거 같습니다. 다시 한번 시도해주세요",Toast.LENGTH_SHORT).show();
            }

        });
    }




//    public void setIconImages(){
//        ImageView logo_icon = (ImageView)findViewById(R.id.application_logo);
//
//        Glide.with(this)
//                .load(R.drawable.beer_icon_100)
//                .signature(new StringSignature("logo_B"))
//                .into(logo_icon);
//
//        ImageView title_icon = (ImageView)findViewById(R.id.schedule_title_icon);
//        Glide.with(this)
//                .load(R.drawable.title_icon)
//                .thumbnail(0.5f)
//                .signature(new StringSignature("title_A"))
//                .into(title_icon);
//
//        ImageView lock_icon = (ImageView)findViewById(R.id.schedule_lock_icon);
//        Glide.with(this)
//                .load(R.drawable.lock_icon1)
//                .thumbnail(0.5f)
//                .signature(new StringSignature("lock_A"))
//                .into(lock_icon);
//    }
}

