package com.example.jiwon_hae.software_practice.drunk_check;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiwon_hae.software_practice.R;

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
        final String B = TestCase.getText().toString();


        this.button2 = (Button)findViewById(R.id.Drunk_Check_Test);
        this.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText = (EditText)findViewById(R.id.editText6);
                String A = editText.getText().toString();

                if(A.equals(B))
                    Toast.makeText(drunk_check.this,"correct",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(drunk_check.this,"false",Toast.LENGTH_SHORT).show();
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

