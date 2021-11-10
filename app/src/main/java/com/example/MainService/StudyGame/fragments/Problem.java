package com.example.MainService.StudyGame.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.android.volley.*;

public class Problem extends AppCompatActivity {
    ImageButton go_back;
    Button reset_btn;
    Button next_btn;
    Button number1;
    Button number2;
    Button number3;
    Button number4;
    Button pass_btn;
    TextView timer_text, problem_type_text, problem_text;
    int minute,second;
    String category_code, url, user_answer, problemid, select_problem,select_querydata, select_code, user_id;
    boolean answer_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        user_id = PreferenceManager.getString(getApplicationContext(), "User_Uid");

        go_back = (ImageButton)findViewById(R.id.Back);
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
        problem_type_text.setText(getIntent().getStringExtra("title")); //MainAcitivity에서 type 가져오기
        problemid = getIntent().getStringExtra("problem_id");
        category_code = getIntent().getStringExtra("category_code");

        //////////////////초기 버튼숨기기/////////////////
        reset_btn.setVisibility(View.GONE);
        next_btn.setVisibility(View.GONE);

        /////////////////request값에 따른 버튼기능 차별화///////////////
        if(problemid !=null){
            select_problem = "wrong_problem";
            select_querydata = "problem_id";
            select_code = problemid;
            pass_btn.setVisibility(View.INVISIBLE);
            next_btn.setText("확인");
        }else{
            select_problem = "randomproblem";
            select_querydata = "category_id";
            select_code = category_code;
            next_btn.setText("다음 문제");
        }

        /////////////////문제 출제////////////////////////
        RequestQueue queue = Volley.newRequestQueue(this);
        url ="http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8002/"+select_problem+"?"+select_querydata+"=" + select_code;
        Log.e("TAG", url );
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
                            Log.e("정답", answer );

                            ////////////////////타이머/////////////////////////
                            second=30;
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
                                        StringRequest timeout = new StringRequest(Request.Method.POST, "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8002/solve",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                answer_check = false;
                                                params.put("problem_id",problem_id);
                                                params.put("user_id",user_id);
                                                params.put("answer_check", String.valueOf(answer_check));
                                                return params;
                                            }
                                        };
                                        timeout.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
                                        queue.add(timeout);
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

                                    /////////////////오답여부체크/////////////////////
                                    next_btn.setOnClickListener(new View.OnClickListener() {
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
                                                            Log.e("post에러", error.toString() );
                                                        }
                                                    }
                                            ){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError{
                                                    Map<String, String> params = new HashMap<>();
                                                    Log.e("TAG", "Hash Map" );
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

                                            if(select_problem == "wrong_problem"){
                                                if(user_answer != answer) {
                                                    Handler handler = new Handler(Looper.getMainLooper());
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    }, 4200);
                                                }else{
                                                    Handler handler = new Handler(Looper.getMainLooper());
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            finish();
                                                        }
                                                    }, 2000);
                                                }
                                            }else{
                                                Handler handler = new Handler(Looper.getMainLooper());
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        finish();
                                                        startActivity(getIntent());
                                                    }
                                                }, 2000);

                                            }
                                        }
                                    });

                                    //////////////각종버튼기능////////////////
                                    go_back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            timer.cancel();
                                            finish();
                                        }
                                    });

                                    pass_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            timer.cancel();
                                            finish();
                                            startActivity(getIntent());
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
    }

}