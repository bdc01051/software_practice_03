package com.example.jiwon_hae.software_practice.account.create_account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jiwon_hae.software_practice.R;
import com.example.jiwon_hae.software_practice.account.create_account.volley.create_account_volley;
import com.example.jiwon_hae.software_practice.account.create_account.volley.request_check_email;
import com.example.jiwon_hae.software_practice.account.create_account.volley.validate_username;
import com.example.jiwon_hae.software_practice.account.login.login_activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class create_account extends AppCompatActivity {
    private EditText create_acc_password;
    private EditText create_acc_password_recheck;

    private EditText create_acc_email_acc;
    private EditText create_acc_email_add;
    private EditText create_acc_userName;
    private Spinner create_acc_email_list;

    private Button next;

    boolean final_check_email = false;
    boolean final_check_username = false;
    boolean final_check_pw = false;
    boolean final_check_re_pw = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        create_acc_email_acc = (EditText)findViewById(R.id.create_acc_email);
        create_acc_email_add = (EditText)findViewById(R.id.create_acc_email_add);
        create_acc_email_list = (Spinner)findViewById(R.id.create_acc_spinner);

        create_acc_userName = (EditText)findViewById(R.id.create_acc_userName);

        create_acc_password = (EditText)findViewById(R.id.create_acc_password);
        create_acc_password_recheck = (EditText)findViewById(R.id.create_acc_recheckPW);

        setCreateAccEmailList(create_acc_email_list, create_acc_email_add, create_acc_email_acc);
        checkUsername(create_acc_userName);
        checkPassword(create_acc_password, create_acc_password_recheck);

        next = (Button)findViewById(R.id.create_acc_next);
        next.setClickable(false);

        if(final_check_email && final_check_pw && final_check_re_pw && final_check_username){
            next.setClickable(true);
        }else{
            next.setClickable(false);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!final_check_email){
                    Toast.makeText(create_account.this, "이메일을 확인해주세요", Toast.LENGTH_SHORT).show();
                }else if(!final_check_username){
                    Toast.makeText(create_account.this, "유저이름을 확인해주세요", Toast.LENGTH_SHORT).show();
                }else if(!final_check_pw || !final_check_re_pw){
                    Toast.makeText(create_account.this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();

                }else{
                    Response.Listener<String> responseListener = new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if(success){
                                    Intent to_main = new Intent(create_account.this, login_activity.class);
                                    startActivity(to_main);
                                    Toast.makeText(create_account.this, "가입해주셔서 감사합니다", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(create_account.this, "잠시후에 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    create_account_volley Validate = new create_account_volley(create_acc_email_acc.getText().toString()+"@"+create_acc_email_add.getText().toString(), create_acc_userName.getText().toString(),create_acc_password_recheck.getText().toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(create_account.this);
                    queue.add(Validate);
                }


            }
        });
    }

    public void checkPassword(final EditText password, final EditText passwordCheck){
        final String[] pw_init = {password.getText().toString()};

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final_check_re_pw = false;
                passwordCheck.setTextColor(Color.RED);
            }

            @Override
            public void afterTextChanged(Editable s) {
                pw_init[0] = password.getText().toString();
                String pw_check = passwordCheck.getText().toString();

                if(checkPassword(pw_init[0])){
                    password.setTextColor(Color.BLACK);
                    final_check_pw = true;
                }else{
                    password.setTextColor(Color.RED);
                    final_check_pw = false;
                }

                if(!pw_init[0].equals(pw_check)){
                    passwordCheck.setTextColor(Color.RED);
                    final_check_re_pw = false;
                }
            }
        });

        passwordCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String pw_check = passwordCheck.getText().toString();

                if(pw_init[0].equals(pw_check)){
                    passwordCheck.setTextColor(Color.BLACK);
                    final_check_re_pw = true;
                }else{
                    passwordCheck.setTextColor(Color.RED);
                    final_check_re_pw = false;
                }
            }
        });
    }

    public void checkUsername(final EditText username){
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!v.hasFocus()){
                    Response.Listener<String> responseListener = new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if(success){
                                    username.setTextColor(Color.BLACK);
                                    final_check_username = true;
                                    Toast.makeText(create_account.this, "사용할 수 있는 유저이름입니다", Toast.LENGTH_SHORT).show();

                                }else{
                                    username.setTextColor(Color.RED);
                                    Toast.makeText(create_account.this, "이미 존재하는 유저이름입니다", Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    validate_username Validate = new validate_username(username.getText().toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(create_account.this);
                    queue.add(Validate);
                }
            }
        });
    }

    public void setCreateAccEmailList(Spinner emailList, final EditText emailWriter, final EditText email_acc){
        ArrayAdapter choose_email_adapter = ArrayAdapter.createFromResource(create_account.this, R.array.email_choice,R.layout.create_account_spinner);
        choose_email_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        create_acc_email_list.setAdapter(choose_email_adapter);

        create_acc_email_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    emailWriter.setText("");
                    emailWriter.setHint("직접입력");

                    emailWriter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(!v.hasFocus()){
                                if(!emailWriter.getText().toString().equals("")){
                                    if(!checkEmail(email_acc.getText().toString() +"@" + emailWriter.getText().toString())){
                                        emailWriter.setTextColor(Color.RED);
                                        Toast.makeText(create_account.this, "올바른 이메일 주소를 입력해주세요", Toast.LENGTH_SHORT).show();

                                    }else{
                                        emailWriter.setTextColor(Color.BLACK);

                                        Response.Listener<String> responseListener = new Response.Listener<String>(){
                                            @Override
                                            public void onResponse(String response) {
                                                try{
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");

                                                    if(success){
                                                        emailWriter.setTextColor(Color.BLACK);
                                                        final_check_email= true;

                                                        Toast.makeText(create_account.this, "사용하셔도 되는 이메일입니다", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        emailWriter.setTextColor(Color.RED);
                                                        Toast.makeText(create_account.this, "이미 사용중인 이메일입니다", Toast.LENGTH_SHORT).show();
                                                    }

                                                }catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        request_check_email Validate = new request_check_email(email_acc.getText().toString() +"@" + emailWriter.getText().toString(), responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(create_account.this);
                                        queue.add(Validate);
                                    }
                                }
                            }
                        }
                    });

                }else if(position==1){
                    emailWriter.setText("naver.com");
                }else if(position==2){
                    emailWriter.setText("daum.com");
                }else if(position==3){
                    emailWriter.setText("hanmail.net");
                }else if(position==4){
                    emailWriter.setText("gmail.com");
                }else if(position==5) {
                    emailWriter.setText("yahoo.com");
                }

                if(!emailWriter.getText().toString().equals("")){
                    if(!checkEmail(email_acc.getText().toString() +"@" + emailWriter.getText().toString())){
                        emailWriter.setTextColor(Color.RED);
                        Toast.makeText(create_account.this, "올바른 이메일 주소를 입력해주세요", Toast.LENGTH_LONG).show();

                    }else{
                        Response.Listener<String> responseListener = new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");

                                    if(success){
                                        emailWriter.setTextColor(Color.BLACK);
                                        final_check_email= true;

                                        Toast.makeText(create_account.this, "사용하셔도 되는 이메일입니다", Toast.LENGTH_SHORT).show();
                                    }else{
                                        emailWriter.setTextColor(Color.RED);
                                        Toast.makeText(create_account.this, "이미 사용중인 이메일입니다", Toast.LENGTH_SHORT).show();
                                    }

                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        request_check_email Validate = new request_check_email(email_acc.getText().toString() +"@" + emailWriter.getText().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(create_account.this);
                        queue.add(Validate);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public boolean checkPassword(String pw){
        String regex = "([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~]).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pw);
        boolean pw_format_true = m.matches();
        return pw_format_true;
    }

    public boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean email_format_true = m.matches();
        return email_format_true;
    }
}
