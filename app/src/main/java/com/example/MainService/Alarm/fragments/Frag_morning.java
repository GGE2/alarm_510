package com.example.MainService.Alarm.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.MainService.Alarm.activities.Alarm_Problem;
import com.example.MainService.Alarm.activities.DivideFragmentActivity;
import com.example.MainService.Alarm.activities.SetAlarmActivity;
import com.example.MainService.Alarm.activities.Time;
import com.example.alarm.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Frag_morning extends Fragment {
    View view1;
    int line_count = 0;
    String am_pm, get_hour, get_minute, get_nq, get_code;
    String  str_hour ="", str_minute ="", str_tv_repeat = "", str_tv_qt = "",
            str_tv_memo = "", str_tv_nq = "", str_tv_type = "";
    private static String file[] = new String[100];
    SharedPreferences pref;

    private static final String TAG = Frag_morning.class.getSimpleName();
    TextView tv_hour_morning, tv_minute_morning, tv_am_pm_morning;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.ring_morning, container, false); // 프래그먼트 연결
        tv_hour_morning = (TextView) view1.findViewById(R.id.tv_hour_morning);
        tv_minute_morning = (TextView) view1.findViewById(R.id.tv_minute_morning);
        tv_am_pm_morning = (TextView) view1.findViewById(R.id.tv_am_pm_morning);

        pref = getActivity().getSharedPreferences("alarm", Activity.MODE_PRIVATE);
        am_pm = pref.getString("am_pm","");
        get_hour = pref.getString("hour","");
        get_minute = pref.getString("minute","");
        get_nq = pref.getString("nq", "");
        get_code = pref.getString("request_id","");

        tv_am_pm_morning.setText(am_pm);
        tv_hour_morning.setText(get_hour);
        tv_minute_morning.setText(get_minute);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(get_nq.matches(".*" + "문제없이 알람끄기" + ".*")){
                    getActivity().finish();
                    getActivity().finishAndRemoveTask();
                } else {
                    Intent intent = new Intent(getActivity(), Alarm_Problem.class);
                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        }, 4000);
        return view1;
    }
}