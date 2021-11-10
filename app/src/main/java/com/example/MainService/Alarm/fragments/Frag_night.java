package com.example.MainService.Alarm.fragments;

import android.content.Intent;
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
import com.example.MainService.Alarm.activities.Time;
import com.example.alarm.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Frag_night extends Fragment {
    View view1;
    TextView tv_am, tv_hour, tv_minute, tv_memo_night;
    int line_count = 0;
    String am_pm, get_hour, get_minute;
    int int_hour, int_minute;
    String  str_hour ="", str_minute ="", str_tv_repeat = "", str_tv_qt = "",
            str_tv_memo = "", str_tv_nq = "", str_tv_type = "";
    private static String file[] = new String[100];
    private static final String TAG = Frag_night.class.getSimpleName();


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.ring_night, container, false); // 프래그먼트 연결
        tv_am = (TextView) view1.findViewById(R.id.tv_am);
        tv_hour = (TextView) view1.findViewById(R.id.tv_hour);
        tv_minute = (TextView) view1.findViewById(R.id.tv_minute);
        tv_memo_night = (TextView) view1.findViewById(R.id.tv_memo_night);

        /*try {
            String filename = getActivity().getDataDir() + "/alarmdata.json";
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                file[line_count] = line;
                line_count++;
                System.out.println(line);
                line = br.readLine();
            }
            br.close();
            System.out.println("3번 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Time time = gson.fromJson(file[0], Time.class);

        str_tv_memo = time.getTv_memo();
        am_pm = time.getAm_pm();
        str_hour = time.getHour();
        str_minute = time.getMinute();


        str_tv_memo.trim(); am_pm.trim();
        str_tv_repeat.trim(); str_tv_qt.trim();
        str_hour.trim(); str_minute.trim();
        str_tv_nq.trim();

        get_hour = str_hour;
        get_minute = str_minute;
        get_hour = get_hour.substring(0,get_hour.length() - 1);

        Log.e(TAG,"오전 오후 값 : "+am_pm);
        Log.e(TAG,"hour값 : "+ get_hour);
        Log.e(TAG,"minute값 : "+ get_minute);
        Log.e(TAG,"memo값 : "+ str_tv_memo);

        tv_am.setText(am_pm);
        tv_hour.setText(get_hour);
        tv_minute.setText(get_minute);
        tv_memo_night.setText(str_tv_memo);*/

        return view1;
    }
}