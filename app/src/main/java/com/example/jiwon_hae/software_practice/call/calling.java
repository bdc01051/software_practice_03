package com.example.jiwon_hae.software_practice.call;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jiwon_hae.software_practice.R;

public class calling extends AppCompatActivity {

    private EditText input_phone_no;
    private TextView add_to_contact_textView;
    private ImageButton delete_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        delete_button = (ImageButton)findViewById(R.id.delete_button);
        Glide.with(this)
                .load(R.drawable.delete_icon)
                .thumbnail(0.1f)
                .into(delete_button);
        this.set_calling();
    }

    private void set_calling() {
        this.input_phone_no = (EditText) findViewById(R.id.input_phone_number);
        this.add_to_contact_textView = (TextView) findViewById(R.id.add_to_content_text);

        input_phone_no.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(calling.this, "before", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int contact_length = input_phone_no.getText().length();

                if (contact_length > 0) {
                    add_to_contact_textView.setVisibility(View.VISIBLE);
                    delete_button.setVisibility(View.VISIBLE);
                } else if (contact_length == 0) {
                    add_to_contact_textView.setVisibility(View.INVISIBLE);
                    delete_button.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void insert_no_input(View view){
        switch(view.getId()){
            case R.id.number_input_1:
                input_phone_no.setText(input_phone_no.getText() + "1");
                break;
            case R.id.number_input_2:
                input_phone_no.setText(input_phone_no.getText() + "2");
                break;
            case R.id.number_input_3:
                input_phone_no.setText(input_phone_no.getText() + "3");
                break;
            case R.id.number_input_4:
                input_phone_no.setText(input_phone_no.getText() + "4");
                break;
            case R.id.number_input_5:
                input_phone_no.setText(input_phone_no.getText() + "5");
                break;
            case R.id.number_input_6:
                input_phone_no.setText(input_phone_no.getText() + "6");
                break;
            case R.id.number_input_7:
                input_phone_no.setText(input_phone_no.getText() + "7");
                break;
            case R.id.number_input_8:
                input_phone_no.setText(input_phone_no.getText() + "8");
                break;
            case R.id.number_input_9:
                input_phone_no.setText(input_phone_no.getText() + "9");
                break;
            case R.id.number_input_0:
                input_phone_no.setText(input_phone_no.getText() + "0");
                break;
            case R.id.delete_button:
                input_phone_no.setText(input_phone_no.getText().toString().substring(0, input_phone_no.getText().toString().length()-1));
                break;

        }
    }
}
