package com.example.MainService.Alarm.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Login.etc.PreferenceManager;
import com.example.MainService.Alarm.fragments.Frag_morning;
import com.example.MainService.Alarm.fragments.Frag_night;
import com.example.alarm.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SetAlarmActivity extends AppCompatActivity {
    private static final String TAG = SetAlarmActivity.class.getSimpleName();

    Context mContext;
    AlarmManager alarm_manager;
    String am_pm, request = null;

    int SET_ALARM = 0;
    int line_count = 0;
    int int_hour, int_minute ,iv_change;
    String  str_hour ="", str_minute ="", str_tv_repeat = "", str_tv_qt = "",
            str_tv_memo = "", str_tv_nq = "", str_tv_type = "";
    String get_hour, get_minute;
    int id_mon = 0, id_tue = 1, id_wed = 2, id_thur = 3, id_fri = 4, id_sat = 5, id_sun = 6;
    int totalID = 0;
    String REQUEST_ID, current_switch, get_state, get_type, get_request, get_nq;
    String user_id, url;


    TextView tv_am, tv_hour, tv_minute, tv_memo_night;
    Intent alarmIntent , Intent;
    PendingIntent pendingIntent;
    Button iv_slide;
    private static int REQUEST_CODE3 = 0;
    String get_code;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring_night);
            mContext = SetAlarmActivity.this;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 프래그먼트매니저를 통해 사용
        Frag_morning frag_morning = new Frag_morning(); // 객체 생성

        Intent intent = getIntent();
        get_code = intent.getExtras().getString("request_code","");
        Log.e(TAG,"REQUEST_CODE : " + get_code);

        user_id = PreferenceManager.getString(getApplicationContext(),"User_Uid");
        url = "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8001/ringalarms?request_id="+get_code;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String pri_memo = obj.getString("tv_memo");
                                String pri_ampm = obj.getString("am_pm");
                                String pri_repeat = obj.getString("tv_repeat");
                                String pri_qt = obj.getString("tv_qt");
                                String pri_hour = obj.getString("tv_hour");
                                String pri_minute = obj.getString("tv_minute");
                                String pri_type = obj.getString("tv_type");
                                String pri_nq = obj.getString("tv_nq");
                                String request_id = obj.getString("request_id");
                                String check_switch = obj.getString("switch");

                                tv_am = findViewById(R.id.tv_am);
                                tv_hour = findViewById(R.id.tv_hour);
                                tv_minute = findViewById(R.id.tv_minute);
                                tv_memo_night = findViewById(R.id.tv_memo_night);

                                tv_am.setText(pri_ampm);
                                tv_hour.setText(pri_hour);
                                tv_minute.setText(pri_minute);
                                tv_memo_night.setText(pri_memo);

                                pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("am_pm", pri_ampm);
                                editor.putString("hour", pri_hour);
                                editor.putString("minute", pri_minute);
                                editor.putString("nq", pri_nq);
                                editor.putString("request_id",request_id);
                                editor.putString("qt",pri_qt);
                                editor.putString("type",pri_type);
                                editor.commit();

                                pri_hour = pri_hour.substring(0, pri_hour.length() - 1);
                                pri_hour.trim();
                                pri_minute.trim();

                                if (pri_repeat.contains("월")) { totalID += id_mon; }
                                if(pri_repeat.contains("화")) { totalID += id_tue; }
                                if(pri_repeat.contains("수")) { totalID += id_wed; }
                                if(pri_repeat.contains("목")) { totalID += id_thur; }
                                if(pri_repeat.contains("금")) { totalID += id_fri; }
                                if(pri_repeat.contains("토")) { totalID += id_sat; }
                                if(pri_repeat.contains("일")) { totalID += id_sun; }

                                REQUEST_ID = totalID +""+ pri_hour +""+ pri_minute +"";
                                REQUEST_CODE3 = Integer.parseInt(REQUEST_ID);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.toString());
                    }

                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

        Log.e(TAG,"요청 보냄");

            OnGestureRegisterListener onGestureRegisterListener = new OnGestureRegisterListener(this) {
                public void onSwipeRight(View view) {
                    transaction.replace(R.id.const_night, frag_morning); //layout, 교체될 layout
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    transaction.commit(); //commit으로 저장 하지 않으면 화면 전환이 되지 않음
                    releaseAlarm(mContext);
                }

                public void onSwipeLeft(View view) {
                    transaction.replace(R.id.const_night, frag_morning); //layout, 교체될 layout
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    transaction.commit(); //commit으로 저장 하지 않으면 화면 전환이 되지 않음
                    releaseAlarm(mContext);
                }

                public void onSwipeBottom(View view) {
                    transaction.replace(R.id.const_night, frag_morning); //layout, 교체될 layout
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    transaction.commit(); //commit으로 저장 하지 않으면 화면 전환이 되지 않음
                    releaseAlarm(mContext);
                }

                public void onSwipeTop(View view) {
                    transaction.replace(R.id.const_night, frag_morning); //layout, 교체될 layout
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    transaction.commit(); //commit으로 저장 하지 않으면 화면 전환이 되지 않음
                    releaseAlarm(mContext);
                }

                public void onClick(View view) {
                    transaction.replace(R.id.const_night, frag_morning); //layout, 교체될 layout
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    transaction.commit(); //commit으로 저장 하지 않으면 화면 전환이 되지 않음
                    releaseAlarm(mContext);
                }

                public boolean onLongClick(View view) {
                    transaction.replace(R.id.const_night, frag_morning); //layout, 교체될 layout
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    transaction.commit(); //commit으로 저장 하지 않으면 화면 전환이 되지 않음
                    releaseAlarm(mContext);
                    return true;
                }
            };
            iv_slide = findViewById(R.id.iv_slide);
            iv_slide.setOnTouchListener(onGestureRegisterListener);
        }


        public void releaseAlarm (Context context){
            Log.e(TAG, "unregisterAlarm");

            // 알람매니저 취소

            // 알람매니저 설정
            alarm_manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
            get_type = pref.getString("type","");
            // 알람 Receiver 인텐트 생성
            alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.putExtra("state", "ALARM_OFF");
            alarmIntent.putExtra("tv_type", get_type);
            Log.e(TAG,"cancelcode : "+ REQUEST_CODE3);
            pendingIntent = PendingIntent.getBroadcast(SetAlarmActivity.this, REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm_manager.cancel(pendingIntent);
            pendingIntent.cancel();

            // 알람 취소
            sendBroadcast(alarmIntent);

            PendingIntent sender = PendingIntent.getBroadcast(SetAlarmActivity.this, REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_NO_CREATE);
            if (sender == null) {
                Log.e(TAG,"알람 없음!");
            } else {
                Log.e(TAG,"알람 있음! : " +sender);
            }

            Toast.makeText(SetAlarmActivity.this, "알람이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }



}
