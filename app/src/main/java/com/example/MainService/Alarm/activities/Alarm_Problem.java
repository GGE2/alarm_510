package com.example.MainService.Alarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Login.etc.PreferenceManager;
import com.example.MainService.WAN.fragments.Frag_wronganswer_note;
import com.example.alarm.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.android.volley.*;
import com.google.gson.Gson;

import static java.lang.Integer.parseInt;

public class Alarm_Problem extends AppCompatActivity {
    Button go_back;
    Button reset_btn;
    Button next_btn;
    Button number1;
    Button number2;
    Button number3;
    Button number4;
    Button pass_btn;
    TextView timer_text, problem_type_text, problem_text;
    SharedPreferences pref;
    int minute,second;
    String category_code, url, user_answer, problemid, select_problem, select_querydata, user_id;
    String select_code = "", get_request;
    int count=0, int_nq, pri_count = 0;
    boolean answer_check;
    int line_count = 0;
    private static String file[] = new String[100];
    private static final String TAG = Alarm_Problem.class.getSimpleName();
    String  str_hour ="", str_minute ="", str_tv_repeat = "", str_tv_qt = "",
            str_tv_memo = "", str_tv_nq = "", str_tv_type = "";
    String get_code,get_nq,get_qt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_problem);
        user_id = PreferenceManager.getString(getApplicationContext(), "User_Uid");

        pass_btn = (Button)findViewById(R.id.pass_btn);
        reset_btn = (Button)findViewById(R.id.reset_btn);
        next_btn = (Button)findViewById(R.id.next_btn);
        number1 = (Button) findViewById(R.id.number1);
        number2 = (Button)findViewById(R.id.number2);
        number3 = (Button)findViewById(R.id.number3);
        number4 = (Button)findViewById(R.id.number4);
        problem_text = (TextView)findViewById(R.id.problem_text);
        timer_text = (TextView)findViewById(R.id.timer_text);

        problem_type_text=(TextView)findViewById(R.id.problem_type_text);

        pref = getSharedPreferences("pref_alarm", Activity.MODE_PRIVATE);
        pri_count = pref.getInt("count",0);
        Log.e(TAG,"pri_count: "+pri_count);
        if(pri_count != 0)
            count = pri_count;
        Log.e(TAG,"count: "+count);

        pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
        get_nq = pref.getString("nq", "");
        get_qt = pref.getString("qt", "");

        reset_btn.setVisibility(View.GONE);
        next_btn.setVisibility(View.GONE);

        int_nq = Integer.parseInt(get_nq);
        problem_type_text.setText(get_qt);
        select_code.trim();
        if(get_qt.matches(".*" + "영단어" + ".*"))
        {
            select_code += "category_id=1&";
        }
        if(get_qt.matches(".*" + "수도" + ".*"))
        {
            select_code += "category_id=2&";
        }
        if(get_qt.matches(".*" + "속담" + ".*"))
        {
            select_code += "category_id=3&";
        }
        if(get_qt.matches(".*" + "사자성어" + ".*"))
        {
            select_code += "category_id=4&";
        }
        if(get_qt.matches(".*" + "역사" + ".*"))
        {
            select_code += "category_id=5&";
        }
        if(get_qt.matches(".*" + "통합" + ".*"))
        {
            select_code += "category_id=6&";
        }

        if (select_code != null && (select_code).contains("&"))
            select_code = select_code.substring(0, select_code.length() - 1);

        Log.e(TAG,"코드 값 : " + select_code);
        url ="http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8002/alarmproblem?"+select_code;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String question = obj.getString("question");
                            String answer = obj.getString("answer");
                            String problem_id = obj.getString("problem_id");
                            JSONArray no_answers = obj.getJSONArray("no_answers");
                            no_answers.put(answer);
                            List<String> example = new ArrayList<String>();
                            for(int i = 0; i<no_answers.length(); i++){
                                example.add(no_answers.get(i).toString());
                            }
                            Collections.shuffle(example);

                            second=30;

                            Log.e("TAG", url );
                            Timer timer = new Timer();
                            TimerTask CDT = new TimerTask() {
                                @Override
                                public void run() { //시간초 동안 할일
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            timer_text.setText(minute  + " " +":" + " " + second); // TextView의 text 값을 정해진 분 : 초로 바꾸기

                                        }
                                    });

                                    second--;
                                    if(second >= 60){ //분 초 계산
                                        minute += second/60;
                                        second = second%60;
                                    }
                                    if(second == -1){ //분이 남아있을때 초 계산
                                        if(minute >= 1){
                                            minute -= 1;
                                            second += 59;
                                        }
                                    }
                                    if (second <= 10 && minute == 0){ // 10초남았을때 글씨 빨간색
                                        timer_text.setTextColor(Color.parseColor("#ff0000"));
                                    }
                                    if (second == 0 && minute == 0) // 시간끝나면 타이머 종료(임시 MainActivity로 이동)
                                    {
                                        StringRequest postRequest = new StringRequest(
                                                Request.Method.POST, "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8002/solve",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                    }
                                                }
                                        ){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError{
                                                Map<String, String> params = new HashMap<>();
                                                    answer_check = false;
                                                    params.put("problem_id",problem_id);
                                                    params.put("user_id",user_id);
                                                    params.put("answer_check", String.valueOf(answer_check));
                                                return  params;
                                            }
                                        };
                                        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
                                        queue.add(postRequest);
                                        timer.cancel();
                                        finish();

                                    }
                                }
                            };
                            timer.schedule(CDT,1000,1000);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    problem_text.setText(question);
                                    number1.setText(example.get(0));
                                    number2.setText(example.get(1));
                                    number3.setText(example.get(2));
                                    number4.setText(example.get(3));
                                    next_btn.setOnClickListener(new View.OnClickListener() { //틀렸는지 맞았는지 체크
                                        @Override
                                        public void onClick(View view) {

                                            StringRequest postRequest = new StringRequest(
                                                    Request.Method.POST, "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8002/solve",
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                        }
                                                    }
                                            ){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError{
                                                    Map<String, String> params = new HashMap<>();
                                                    if(user_answer == answer) {
                                                        Log.e("TAG", "정답" );
                                                        answer_check = true;
                                                        params.put("problem_id",problem_id);
                                                        params.put("user_id",user_id);
                                                        params.put("answer_check", String.valueOf(answer_check));
                                                        Handler handler = new Handler(Looper.getMainLooper());
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ImageView imageView = new ImageView(getApplicationContext());
                                                                imageView.setImageResource(R.drawable.success);
                                                                Toast myToast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_SHORT);
                                                                myToast.setView(imageView);
                                                                myToast.setGravity(Gravity.CENTER, 0,0 );
                                                                myToast.show();
                                                            }
                                                        }, 0);
                                                    }else{
                                                        Log.e("TAG", "오답" );
                                                        answer_check = false;
                                                        params.put("problem_id",problem_id);
                                                        params.put("user_id",user_id);
                                                        params.put("answer_check", String.valueOf(answer_check));
                                                        Handler handler = new Handler(Looper.getMainLooper());
                                                        if(select_problem == "wrong_problem"){
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    ImageView imageView = new ImageView(getApplicationContext());
                                                                    imageView.setImageResource(R.drawable.failure);
                                                                    Toast myToast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_SHORT);
                                                                    myToast.setView(imageView);
                                                                    myToast.setGravity(Gravity.CENTER, 0,0 );
                                                                    Toast answerToast = Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_SHORT);
                                                                    myToast.show();
                                                                    answerToast.show();
                                                                }
                                                            }, 0);
                                                        }else{
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    ImageView imageView = new ImageView(getApplicationContext());
                                                                    imageView.setImageResource(R.drawable.failure);
                                                                    Toast myToast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_SHORT);
                                                                    myToast.setView(imageView);
                                                                    myToast.setGravity(Gravity.CENTER, 0,0 );
                                                                    myToast.show();
                                                                }
                                                            }, 0);
                                                        }
                                                    }
                                                    return  params;
                                                }
                                            };
                                            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
                                            queue.add(postRequest);
                                            timer.cancel();

                                           if(count == (int_nq - 1)) {
                                               pref = getSharedPreferences("pref_alarm", Activity.MODE_PRIVATE);
                                               SharedPreferences.Editor editor = pref.edit();
                                               count = 0;
                                               editor.putInt("count", count);
                                               editor.commit();
                                               Log.e(TAG,"finish");
                                               finish();
                                               finishAndRemoveTask();
                                           }
                                            else {
                                               pref = getSharedPreferences("pref_alarm", Activity.MODE_PRIVATE);
                                               SharedPreferences.Editor editor = pref.edit();
                                               count ++;
                                               editor.putInt("count", count);
                                               editor.commit();
                                               Log.e(TAG,"count값: "+count+", nq값: "+int_nq);
                                               finish();
                                               startActivity(getIntent());
                                            }
                                        }
                                    });

                                    pass_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            timer.cancel();
                                            finish();
                                            startActivity(getIntent());
                                        }

                                        private void getPreferences() {
                                        }
                                    });
                                    number1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            user_answer =  number1.getText().toString();
                                            number1.setSelected(true);
                                            number2.setVisibility(View.GONE);
                                            number3.setVisibility(View.GONE);
                                            number4.setVisibility(View.GONE);
                                            reset_btn.setVisibility(View.VISIBLE);
                                            next_btn.setVisibility(View.VISIBLE);

                                        }
                                    });

                                    number2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            user_answer =  number2.getText().toString();
                                            number2.setSelected(true);
                                            number1.setVisibility(View.GONE);
                                            number3.setVisibility(View.GONE);
                                            number4.setVisibility(View.GONE);
                                            reset_btn.setVisibility(View.VISIBLE);
                                            next_btn.setVisibility(View.VISIBLE);

                                        }
                                    });

                                    number3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            user_answer =  number3.getText().toString();
                                            number3.setSelected(true);
                                            number1.setVisibility(View.GONE);
                                            number2.setVisibility(View.GONE);
                                            number4.setVisibility(View.GONE);
                                            reset_btn.setVisibility(View.VISIBLE);
                                            next_btn.setVisibility(View.VISIBLE);
                                        }
                                    });

                                    number4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            user_answer =  number4.getText().toString();
                                            number4.setSelected(true);
                                            number1.setVisibility(View.GONE);
                                            number2.setVisibility(View.GONE);
                                            number3.setVisibility(View.GONE);
                                            reset_btn.setVisibility(View.VISIBLE);
                                            next_btn.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    reset_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                            number1.setSelected(false);
                                            number2.setSelected(false);
                                            number3.setSelected(false);
                                            number4.setSelected(false);

                                            number1.setVisibility(View.VISIBLE);
                                            number2.setVisibility(View.VISIBLE);
                                            number3.setVisibility(View.VISIBLE);
                                            number4.setVisibility(View.VISIBLE);

                                            reset_btn.setVisibility(View.GONE);
                                            next_btn.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.toString());
            }
        });
        queue.add(stringRequest);
    }
}



