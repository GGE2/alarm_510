package com.example.Login.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.alarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SignUp2Activity extends BaseActivity {

    private int check_sum=0;


    private String user_name, user_phone_number;
    private Pattern pattern;
    private String user_birth;
    private TextView check_name, check_phone_number;
    private ArrayList<String> yearList = new ArrayList<>();
    private String[] y_List, m_List, d_List;
    private ArrayList<String> monthList = new ArrayList<>();
    private ArrayList<String> dayList = new ArrayList<>();
    private String input_year, input_month, input_day;
    EditText input_name, input_phone_number;
    AppCompatButton pre_btn, next_brn;
    MaterialSpinner year, month, day;
    ArrayAdapter yearAdap, monthAdap, dayAdap;
    Calendar calendar = Calendar.getInstance();
    int cYear = calendar.get(Calendar.YEAR);


    public SignUp2Activity() {

    }


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up2);
        check_name = findViewById(R.id.signup2_check_name);
        check_phone_number = findViewById(R.id.signup2_check_phone_number);

        input_name = findViewById(R.id.signup2_name_input);
        input_phone_number = findViewById(R.id.signup2_phone_input);

        user_name = input_name.getText().toString();
        user_phone_number = input_phone_number.getText().toString();

        next_brn = findViewById(R.id.signup2_fin_btn);

        year = findViewById(R.id.signup2_year_date);
        month = findViewById(R.id.signup2_month_date);
        day = findViewById(R.id.signup2_day_date);


        setList();

        y_List = yearList.toArray(new String[yearList.size()]);
        d_List = dayList.toArray(new String[dayList.size()]);

        yearAdap = new SpinnerArrayAdapter(this, R.layout.spinner_layout, y_List);
        year.setAdapter(yearAdap);
        year.setTextSize(18);

        year.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                input_year = yearList.get(position);
            }
        });

        m_List = monthList.toArray(new String[monthList.size()]);
        monthAdap = new SpinnerArrayAdapter(this, R.layout.spinner_layout, m_List);
        month.setAdapter(monthAdap);
        month.setTextSize(18);
        month.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                input_month = monthList.get(position);
                if (Integer.parseInt(input_month) < 10) {
                    input_month = "0" + input_month;
                }
            }
        });


        d_List = dayList.toArray(new String[dayList.size()]);
        dayAdap = new SpinnerArrayAdapter(this, R.layout.spinner_layout, d_List);
        day.setAdapter(dayAdap);
        day.setTextSize(18);
        day.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                input_day = dayList.get(position);
                if (Integer.parseInt(input_day) < 10) {
                    input_day = "0" + input_day;
                }
            }
        });

        pre_btn = findViewById(R.id.signup2_pre_btn);


        //LoginActivity로 이동
        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        //SignUpActivity로 이동
        next_brn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = input_name.getText().toString();
                String userPhoneNumber = input_phone_number.getText().toString();
                setUserBirth(input_year, input_month, input_day);
                if (check_text(userName, userPhoneNumber) && check_text(input_year, input_month, input_day)) {
                    checkAnswerSubmission(new FirebaseCallBack<Boolean>() {
                        @Override
                        public void callback(Boolean data) {
                                if(data){
                                    Intent intent = new Intent(SignUp2Activity.this, SignUpActivity.class);
                                    intent.putExtra("userName", userName);
                                    intent.putExtra("userPhoneNumber", userPhoneNumber);
                                    intent.putExtra("userBirth", getUserBirth());
                                    startActivity(intent);
                                }
                                else{
                                    check_phone_number.setText("이미 가입된 계정의 휴대전화 입니다.");
                                }
                        }
                    });


                }

            }
        });


    }
    private interface SimpleCallback {
        void callback(Object data);
    }


    private interface FirebaseCallBack<T> {
        void callback(T data);
    }

    //비동기->동기
    private void checkAnswerSubmission(@NonNull FirebaseCallBack<Boolean> finishedCallBack){
        DatabaseReference answerDatabase = FirebaseDatabase.getInstance().getReference("Alarm").child("UserAccount");
        answerDatabase.orderByKey().equalTo(input_phone_number.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int check=0;
                for(DataSnapshot datas:snapshot.getChildren()) {
                    if(datas.exists()){
                        check++;
                    }

                }
                if(check==0)
                    finishedCallBack.callback(true);
                else
                    finishedCallBack.callback(false);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }







    //화면 밖 클릭시 키보드 종료
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();

        if (v != null && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) && v instanceof EditText && !v.getClass().getName().startsWith("android.webkit.")) {
            int[] scrcoords = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + v.getLeft() - scrcoords[0];
            float y = event.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(event);
    }


    //년도,월,일 List 초기화
    private void setList() {


        for (int i = cYear; i >= 1930; i--) {
            yearList.add(Integer.toString(i));
        }
        for (int i = 1; i <= 12; i++) {
            monthList.add(Integer.toString(i));
        }
        for (int i = 1; i <= 31; i++) {
            dayList.add(Integer.toString(i));
        }

    }

    private void setUserBirth(String y, String m, String d) {
        this.user_birth = y + m + d;

    }

    public String getUserBirth() {
        return this.user_birth;
    }

    private boolean check_text(String name, String number) {
        if (name.equals("")) {
            check_phone_number.setText("");
            check_name.setText("이름을 확인해주세요.");
            return false;
        }
        else if(name.matches("[0-9|a-z|A-Z]*")){
            check_phone_number.setText("");
            check_name.setText("유효하지 않은 이름 형식입니다.");
            return false;
        }
        else {
            check_name.setText("");
        }

        if (number.equals("") || number.length() != 11) {
            check_phone_number.setText("휴대폰 번호를 확인해주세요.");
            return false;
        }

        else {
            check_phone_number.setText("");
        }
        return true;
    }

    private boolean check_text(String y, String m, String d) {
        if (y == null || m == null || d == null) {
            TextView tvToastMsg = new TextView(getApplicationContext());
            tvToastMsg.setText("생년월일을 확인해주세요");
            //tvToastMsg.setBackgroundResource(R.drawable.bt_tag);
            tvToastMsg.setTextColor(getResources().getColor(R.color.main_background));
            tvToastMsg.setTextSize(16);
            final Toast toastMsg = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
            toastMsg.setGravity(Gravity.CENTER | Gravity.BOTTOM, 100, 200);
            toastMsg.setView(tvToastMsg);
            toastMsg.show();

            return false;
        }
        return true;
    }


}

