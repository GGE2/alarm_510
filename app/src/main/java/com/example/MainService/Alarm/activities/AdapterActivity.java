package com.example.MainService.Alarm.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Login.etc.PreferenceManager;
import com.example.MainService.Alarm.fragments.Frag_alarm;
import com.example.alarm.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterActivity extends RecyclerView.Adapter<AdapterActivity.ViewHolder> {

    public ArrayList<Time> arrayList = new ArrayList<Time>();
     OnTimeItemClickListener listener = null ;
    public LayoutInflater inflater;
    static SharedPreferences pref;
    AlarmManager alarm_manager;
    PendingIntent pendingIntent;
    String get_hour, get_minute, get_am_pm, get_repeat, get_type, get_memo, get_nq, get_qt;
    String json;
    static String get_tv_type;
    int int_hour, originhour;
    int int_minute;
    int week;
    static int iv_bell = R.drawable.bell;
    static int iv_vibration = R.drawable.vibration;
    private final String pref_key = "toggle";
    private static int REQUEST_CODE3 = 0;
    String user_id;
    String url, check_switch;
    int id_mon = 0, id_tue = 1, id_wed = 2, id_thur = 3, id_fri = 4, id_sat = 5, id_sun = 6;
    int totalID = 0;
    String REQUEST_ID;
    static HashMap<Integer, Boolean> checkMap = new HashMap<>();

    private static final String TAG = AdapterActivity.class.getSimpleName();

    public interface OnTimeItemClickListener {
        void onItemClick(ViewHolder holder, View v, int position);
    }



    public AdapterActivity(LayoutInflater inflater, ArrayList<Time> arrayList){
        this.arrayList = arrayList;
        this.inflater = inflater;
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public AdapterActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View convertView = inflater.inflate(R.layout.round_theme, parent, false) ;
        AdapterActivity.ViewHolder vh = new AdapterActivity.ViewHolder(convertView);

        TextView tv_memo = (TextView)convertView.findViewById(R.id.list_tv_memo);
        TextView am_pm = (TextView)convertView.findViewById(R.id.am_pm);
        TextView tv_repeat = (TextView)convertView.findViewById(R.id.list_tv_repeat);
        TextView tv_qt = (TextView)convertView.findViewById(R.id.list_tv_qt);
        TextView hourText = (TextView)convertView.findViewById(R.id.textTime1);
        TextView minuteText = (TextView)convertView.findViewById(R.id.textTime2);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_change);
        Switch sw = (Switch) convertView.findViewById(R.id.switchBtn);
        RelativeLayout rl_text = (RelativeLayout) convertView.findViewById(R.id.rl_text);
        TextView tv_nq = (TextView)convertView.findViewById(R.id.list_tv_nq);

        vh.tv_memo = tv_memo;
        vh.am_pm = am_pm;
        vh.tv_repeat = tv_repeat;
        vh.tv_qt = tv_qt;
        vh.hourText = hourText;
        vh.minuteText = minuteText;
        vh.iv = iv;
        vh.sw = sw;
        vh.tv_nq = tv_nq;

//        Intent alarmIntent = new Intent(convertView.getContext(), AlarmReceiver.class);
//        alarmIntent.putExtra("state", "ALARM_ON");
//        alarmIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);


//         receiver를 동작하게 하기 위해 PendingIntent의 인스턴스를 생성할 때, getBroadcast 라는 메소드를 사용
//         requestCode는 나중에 Alarm을 해제 할때 어떤 Alarm을 해제할지를 식별하는 코드
//        pendingIntent = PendingIntent.getBroadcast(convertView.getContext(), REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm_manager = (AlarmManager) convertView.getContext().getSystemService(ALARM_SERVICE);

        Collections.sort(arrayList, new Comparator<Time>() {
            @Override
            public int compare(Time o1, Time o2) {
                int result = 0, i_hour1, i_hour2, i_min1, i_min2;
                String s_hour1, s_hour2, s_apm1, s_apm2, s_min1, s_min2;
                s_hour1 = o1.getHour(); s_hour2 = o2.getHour(); // 시
                s_apm1 = o1.getAm_pm(); s_apm2 = o2.getAm_pm(); // /오전 오후
                s_min1 = o1.getMinute(); s_min2 = o2.getMinute(); // 분
                s_hour1 = s_hour1.substring(0,s_hour1.length() - 1); // ":" 빼주는 작업
                s_hour2 = s_hour2.substring(0,s_hour2.length() - 1);
                i_hour1 = Integer.parseInt(s_hour1); // string -> int
                i_hour2 = Integer.parseInt(s_hour2);
                i_min1 = Integer.parseInt(s_min1); //string -> int
                i_min2 = Integer.parseInt(s_min2);

                if(s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오전" + ".*") ){
                    if(i_hour1 < i_hour2){
                        result = -1; // 첫번째 시 < 두번째 시
                    } else if (i_hour1 == i_hour2){ // 첫번째 시 == 두번째 시
                        if(i_min1 < i_min2) // 첫번째 분 < 두번째 분
                            result = -1;
                        else if(i_min1 == i_min2) // 첫번째 분 == 두번째 분
                            result = 0;
                        else if(i_min1 > i_min2) // 첫번째 분 > 두번째 분
                            result = 1;
                    } else if(i_hour1 > i_hour2){
                        result = 1;
                    }
                }
                if(s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오후" + ".*") ) {
                    result = -1;
                }
                if(s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오전" + ".*") ) {
                    result = 1;
                }
                if(s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오후" + ".*") ) {
                    if(i_hour1 < i_hour2){
                        result = -1; // 첫번째 시 < 두번째 시
                    } else if (i_hour1 == i_hour2){ // 첫번째 시 == 두번째 시
                        if(i_min1 < i_min2) // 첫번째 분 < 두번째 분
                            result = -1;
                        else if(i_min1 == i_min2) // 첫번째 분 == 두번째 분
                            result = 0;
                        else if(i_min1 > i_min2) // 첫번째 분 > 두번째 분
                            result = 1;
                    } else if(i_hour1 > i_hour2){
                        result = 1;
                    }
                }
                return result;
            }
        });
        return vh;

        }


    public void setOnItemClickListener(OnTimeItemClickListener listener) {
        this.listener = listener ;
    }
    public void onItemClick(ViewHolder holder,View v, int position){
        if(listener != null)
            listener.onItemClick(holder,v,position);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e(TAG,"들어온 값 체크"+position);

        Collections.sort(arrayList, new Comparator<Time>() {
            @Override
            public int compare(Time o1, Time o2) {
                int result = 0, i_hour1, i_hour2, i_min1, i_min2;
                String s_hour1, s_hour2, s_apm1, s_apm2, s_min1, s_min2;
                s_hour1 = o1.getHour(); s_hour2 = o2.getHour(); // 시
                s_apm1 = o1.getAm_pm(); s_apm2 = o2.getAm_pm(); // 오전 오후
                s_min1 = o1.getMinute(); s_min2 = o2.getMinute(); // 분
                s_hour1 = s_hour1.substring(0,s_hour1.length() - 1); // ":" 빼주는 작업
                s_hour2 = s_hour2.substring(0,s_hour2.length() - 1);
                i_hour1 = Integer.parseInt(s_hour1); // string -> int
                i_hour2 = Integer.parseInt(s_hour2);
                i_min1 = Integer.parseInt(s_min1); //string -> int
                i_min2 = Integer.parseInt(s_min2);

                if(s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오전" + ".*") ){
                    if(i_hour1 < i_hour2){
                        result = -1; // 첫번째 시 < 두번째 시
                    } else if (i_hour1 == i_hour2){ // 첫번째 시 == 두번째 시
                        if(i_min1 < i_min2) // 첫번째 분 < 두번째 분
                            result = -1;
                        else if(i_min1 == i_min2) // 첫번째 분 == 두번째 분
                            result = 0;
                        else if(i_min1 > i_min2) // 첫번째 분 > 두번째 분
                            result = 1;
                    } else if(i_hour1 > i_hour2){
                        result = 1;
                    }
                }
                if(s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오후" + ".*") ) {
                    result = -1;
                }
                if(s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오전" + ".*") ) {
                    result = 1;
                }
                if(s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오후" + ".*") ) {
                    if(i_hour1 < i_hour2){
                        result = -1; // 첫번째 시 < 두번째 시
                    } else if (i_hour1 == i_hour2){ // 첫번째 시 == 두번째 시
                        if(i_min1 < i_min2) // 첫번째 분 < 두번째 분
                            result = -1;
                        else if(i_min1 == i_min2) // 첫번째 분 == 두번째 분
                            result = 0;
                        else if(i_min1 > i_min2) // 첫번째 분 > 두번째 분
                            result = 1;
                    } else if(i_hour1 > i_hour2){
                        result = 1;
                    }
                }
                return result;
            }
        });

        holder.onBind(arrayList.get(position));

        holder.itemView.setTag(position);
        holder.rl_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    int checkpos = (Integer) holder.getAdapterPosition();
                    get_hour = arrayList.get(checkpos).getHour();
                    get_hour = get_hour.substring(0, get_hour.length() - 1);
                    get_minute = arrayList.get(checkpos).getMinute();
                    get_repeat = arrayList.get(checkpos).getTv_repeat();
                    get_type = arrayList.get(checkpos).getTv_type();

                    if(get_repeat.contains("월")) totalID += id_mon;
                    if(get_repeat.contains("화")) totalID += id_tue;
                    if(get_repeat.contains("수")) totalID += id_wed;
                    if(get_repeat.contains("목")) totalID += id_thur;
                    if(get_repeat.contains("금")) totalID += id_fri;
                    if(get_repeat.contains("토")) totalID += id_sat;
                    if(get_repeat.contains("일")) totalID += id_sun;

                    REQUEST_ID = totalID +""+ get_hour +""+ get_minute +"";
                    totalID = 0;
                    url = "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8001/deletealarms";
                    RequestQueue queue = Volley.newRequestQueue(v.getContext());

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
                    ) {

                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            Log.e(TAG, "deleteRequest");
                            Log.e(TAG,REQUEST_ID);
                            params.put("request_id", REQUEST_ID);

                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
                    stringRequest.setShouldCache(false);
                    queue.add(stringRequest);

//                    pref = v.getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putString("state", "ALARM_OFF");
//                    editor.putString("tv_type", get_type);
//                    editor.commit();

                    REQUEST_CODE3 = Integer.parseInt(REQUEST_ID);
                    // 알람매니저 취소

                    Intent alarmIntent = new Intent(v.getContext(), AlarmReceiver.class);
                    alarmIntent.putExtra("state", "ALARM_OFF");
                    alarmIntent.putExtra("tv_type", get_type);

                    pendingIntent = PendingIntent.getBroadcast(v.getContext(), REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarm_manager.cancel(pendingIntent);
                    pendingIntent.cancel();
                    // 알람 취소
                    v.getContext().sendBroadcast(alarmIntent);

//                    Toast.makeText(v.getContext(), "알람 취소", Toast.LENGTH_SHORT).show();

                    PendingIntent sender = PendingIntent.getBroadcast(v.getContext(), REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_NO_CREATE);
                    if (sender == null) {
                        Log.e(TAG,"알람 없음!");
                    } else {
                        Log.e(TAG,"알람 있음! : " +sender);
                    }

                   listener.onItemClick(holder, v, checkpos);

                }
            }
        });
        holder.rl_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
           AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.create();

                builder.setTitle("알람 삭제");
                builder.setMessage("알람을 삭제할까요?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int checkpos = (Integer) holder.getAdapterPosition();
                        get_hour = arrayList.get(checkpos).getHour();
                        get_hour = get_hour.substring(0, get_hour.length() - 1);
                        get_minute = arrayList.get(checkpos).getMinute();
                        get_repeat = arrayList.get(checkpos).getTv_repeat();
                        get_type = arrayList.get(checkpos).getTv_type();
                        holder.sw.setChecked(false);

                        if (get_repeat.contains("월")) totalID += id_mon;
                        if(get_repeat.contains("화")) totalID += id_tue;
                        if(get_repeat.contains("수")) totalID += id_wed;
                        if(get_repeat.contains("목")) totalID += id_thur;
                        if(get_repeat.contains("금")) totalID += id_fri;
                        if(get_repeat.contains("토")) totalID += id_sat;
                        if(get_repeat.contains("일")) totalID += id_sun;

                        REQUEST_ID = totalID +""+ get_hour +""+ get_minute +"";
                        REQUEST_CODE3 = Integer.parseInt(REQUEST_ID);
                        totalID = 0;
                        url = "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8001/deletealarms";
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                        ) {

                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                Log.e(TAG, "deleteRequest");
                                Log.e(TAG,REQUEST_ID);
                                params.put("request_id", REQUEST_ID);

                                return params;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
                        stringRequest.setShouldCache(false);
                        queue.add(stringRequest);

//                        pref = v.getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = pref.edit();
//                        editor.putString("state", "ALARM_OFF");
//                        editor.putString("tv_type", get_type);
//                        editor.commit();

                        Intent alarmIntent = new Intent(v.getContext(), AlarmReceiver.class);
                        alarmIntent.putExtra("state", "ALARM_OFF");
                        alarmIntent.putExtra("tv_type", get_type);

                        Log.e(TAG,"deleteREQUESTCODE : "+REQUEST_CODE3);

                        pendingIntent = PendingIntent.getBroadcast(v.getContext(), REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarm_manager.cancel(pendingIntent);
                        pendingIntent.cancel();
                        // 알람 취소
                        v.getContext().sendBroadcast(alarmIntent);

                        Toast.makeText(v.getContext(), "알람 취소", Toast.LENGTH_SHORT).show();

                        removeItem(checkpos);

                        notifyDataSetChanged();
                        PendingIntent sender = PendingIntent.getBroadcast(v.getContext(), REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_NO_CREATE);
                        if (sender == null) {
                            Log.e(TAG,"알람 없음!");
                        } else {
                            Log.e(TAG,"알람 있음! : " +sender);
                        }
                    }

                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });

        holder.sw.setOnClickListener(new CompoundButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean state = holder.sw.isChecked();
                if (state) {

                    //1. 스위치가 off -> on 으로 변경되었을 경우.
                    holder.tv_repeat.setTextColor(Color.parseColor("#54D2C2"));
                    holder.rl_text.setBackgroundResource(R.drawable.round_back);

                    int checkpos = (Integer) holder.getAdapterPosition();

                    try {
                        Gson gson = new Gson();
                        JsonObject obj = new JsonObject();

                        obj.addProperty("tv_memo", arrayList.get(checkpos).getTv_memo());
                        obj.addProperty("am_pm", arrayList.get(checkpos).getAm_pm());
                        obj.addProperty("tv_repeat", arrayList.get(checkpos).getTv_repeat());
                        obj.addProperty("tv_qt", arrayList.get(checkpos).getTv_qt());
                        obj.addProperty("hour", arrayList.get(checkpos).getHour());
                        obj.addProperty("minute", arrayList.get(checkpos).getMinute());
                        obj.addProperty("tv_type", arrayList.get(checkpos).getTv_type());
                        obj.addProperty("tv_nq", arrayList.get(checkpos).getTv_nq());
                        obj.addProperty("check_switch", arrayList.get(checkpos).getSwitch());
                        json = gson.toJson(obj);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    get_memo = arrayList.get(checkpos).getTv_memo();
                    get_am_pm = arrayList.get(checkpos).getAm_pm();
                    get_hour = arrayList.get(checkpos).getHour();
                    get_minute = arrayList.get(checkpos).getMinute();
                    get_repeat = arrayList.get(checkpos).getTv_repeat();
                    get_type = arrayList.get(checkpos).getTv_type();
                    check_switch = arrayList.get(checkpos).getSwitch();
                    get_nq = arrayList.get(checkpos).getTv_nq();
                    get_qt = arrayList.get(checkpos).getTv_qt();
                    String str_am_pm = get_am_pm;
                    get_hour = get_hour.substring(0, get_hour.length() - 1);
                    get_hour.trim();
                    get_minute.trim();

                    if (get_repeat.contains("월")) { totalID += id_mon; }
                    if(get_repeat.contains("화")) { totalID += id_tue; }
                    if(get_repeat.contains("수")) { totalID += id_wed; }
                    if(get_repeat.contains("목")) { totalID += id_thur; }
                    if(get_repeat.contains("금")) { totalID += id_fri; }
                    if(get_repeat.contains("토")) { totalID += id_sat; }
                    if(get_repeat.contains("일")) { totalID += id_sun; }

                    REQUEST_ID = totalID +""+ get_hour +""+ get_minute +"";
                    totalID = 0;

                    int_hour = Integer.parseInt(get_hour);
                    int_minute = Integer.parseInt(get_minute);

                    Log.e(TAG,"am_pm? "+get_am_pm);

                    if (get_am_pm.matches(".*" + "오후" + ".*")){
                        originhour = int_hour + 12;
                    } else {
                        originhour = int_hour;
                    }

                        alarm_manager = (AlarmManager) v.getContext().getSystemService(ALARM_SERVICE);

                        Log.e(TAG,"알람 설정중 "+originhour+": "+int_minute);


                        // 현재 지정된 시간으로 알람 시간 설정
                        final Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, originhour);
                        calendar.set(Calendar.MINUTE, int_minute);
                        calendar.set(Calendar.SECOND, 0);

                        pref = v.getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("state", "ALARM_ON");
                        editor.putString("tv_type", get_type);
                        editor.commit();

                        REQUEST_CODE3 = Integer.parseInt(REQUEST_ID);

                        url = "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8001/modifyalarms";
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                        ) {

                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                Log.e(TAG, "Modify on "+REQUEST_ID);
                                params.put("switch", "1");
                                params.put("request_id", REQUEST_ID);

                                return params;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
                        stringRequest.setShouldCache(false);
                        queue.add(stringRequest);

                        // receiver를 동작하게 하기 위해 PendingIntent의 인스턴스를 생성할 때, getBroadcast 라는 메소드를 사용
                        // requestCode는 나중에 Alarm을 해제 할때 어떤 Alarm을 해제할지를 식별하는 코드

                        Intent alarmIntent = new Intent(v.getContext(), AlarmReceiver.class);
                        alarmIntent.putExtra("state", "ALARM_ON");
                        alarmIntent.putExtra("tv_type",get_type);
                        alarmIntent.putExtra("request_code",REQUEST_ID);
                        alarmIntent.putExtra("tv_repeat",get_repeat);
                        alarmIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        pendingIntent = null;
                        pendingIntent = PendingIntent.getBroadcast(v.getContext(), REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        long currentTime = System.currentTimeMillis(); // 현재 시간
                        //long triggerTime = SystemClock.elapsedRealtime() + 1000*60;
                        long triggerTime = calendar.getTimeInMillis(); // 알람을 울릴 시간
                        long interval = 1000 * 60 * 60 * 24* 7; // 1주일 시간

                        while (currentTime > triggerTime) { // 현재 시간보다 작다면
                            triggerTime += interval; // 다음날 울리도록 처리
                        }
                        Log.e(TAG,""+currentTime+"<? "+triggerTime);

                        // 알림 세팅 : AlarmManager 인스턴스에서 set 메소드를 실행시키는 것은 단발성 Alarm을 생성하는 것
                        // RTC_WAKEUP : UTC 표준시간을 기준으로 하는 명시적인 시간에 intent를 발생, 장치를 깨움
                        if (Build.VERSION.SDK_INT < 23) {
                            if (Build.VERSION.SDK_INT >= 19) {
                                alarm_manager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                            } else {
                                // 알람셋팅
                                alarm_manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                            }
                        } else {  // 23 이상
                            alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                            alarm_manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                            //알람 매니저를 통한 반복알람 설정
                            alarm_manager.setRepeating(AlarmManager.RTC, triggerTime, interval, pendingIntent);
                            // interval : 다음 알람이 울리기까지의 시간
                        }
                        PendingIntent sender = PendingIntent.getBroadcast(v.getContext(), REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_NO_CREATE);
                        if (sender == null) {
                            Log.e(TAG,"알람 없음!");
                        } else {
                            Log.e(TAG,"알람 있음! : " +sender);
                        }
                    Toast.makeText(v.getContext(), "알람 예정 시간 : " + str_am_pm + " " + int_hour + "시 " + int_minute + "분", Toast.LENGTH_SHORT).show();

                } else {
                    //2. 스위치가 on -> off 로 변경되었을 경우.
                    holder.tv_repeat.setTextColor(Color.parseColor("#000000"));

                    holder.tv_memo.setTextColor(Color.parseColor("#AEACAC"));
                    holder.rl_text.setBackgroundResource(R.drawable.round_box_gray2);

                    int checkpos = (Integer) holder.getAdapterPosition();
                    try {
                        Gson gson = new Gson();
                        JsonObject obj = new JsonObject();
                        obj.addProperty("tv_memo", arrayList.get(checkpos).getTv_memo());
                        obj.addProperty("am_pm", arrayList.get(checkpos).getAm_pm());
                        obj.addProperty("tv_repeat", arrayList.get(checkpos).getTv_repeat());
                        obj.addProperty("tv_qt", arrayList.get(checkpos).getTv_qt());
                        obj.addProperty("hour", arrayList.get(checkpos).getHour());
                        obj.addProperty("minute", arrayList.get(checkpos).getMinute());
                        obj.addProperty("tv_type", arrayList.get(checkpos).getTv_type());
                        obj.addProperty("tv_nq", arrayList.get(checkpos).getTv_nq());
                        obj.addProperty("check_switch", arrayList.get(checkpos).getSwitch());
                        json = gson.toJson(obj);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    get_am_pm = arrayList.get(checkpos).getAm_pm();
                    get_hour = arrayList.get(checkpos).getHour();
                    get_minute = arrayList.get(checkpos).getMinute();
                    get_repeat = arrayList.get(checkpos).getTv_repeat();
                    get_type = arrayList.get(checkpos).getTv_type();
                    check_switch = arrayList.get(checkpos).getSwitch();
                    String str_am_pm = get_am_pm;
                    get_hour = get_hour.substring(0, get_hour.length() - 1);
                    get_hour.trim();
                    get_minute.trim();

                    if (get_repeat.contains("월")) { totalID += id_mon; }
                    if(get_repeat.contains("화")) { totalID += id_tue; }
                    if(get_repeat.contains("수")) { totalID += id_wed; }
                    if(get_repeat.contains("목")) { totalID += id_thur; }
                    if(get_repeat.contains("금")) { totalID += id_fri; }
                    if(get_repeat.contains("토")) { totalID += id_sat; }
                    if(get_repeat.contains("일")) { totalID += id_sun; }

                    REQUEST_ID = totalID +""+ get_hour +""+ get_minute +"";
                    totalID = 0;
                    REQUEST_CODE3 = Integer.parseInt(REQUEST_ID);

                    url = "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8001/modifyalarms";
                    RequestQueue queue = Volley.newRequestQueue(v.getContext());

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
                    ) {

                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            Log.e(TAG, "Modify 0ff "+REQUEST_ID);
                            params.put("switch", "0");
                            params.put("request_id", REQUEST_ID);

                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
                    stringRequest.setShouldCache(false);
                    queue.add(stringRequest);


                    // 알람매니저 취소

                    Intent alarmIntent = new Intent(v.getContext(), AlarmReceiver.class);
                    alarmIntent.putExtra("state", "ALARM_OFF");
                    alarmIntent.putExtra("tv_type", get_type);

                    pendingIntent = null;
                    pendingIntent = PendingIntent.getBroadcast(v.getContext(), REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarm_manager.cancel(pendingIntent);
                    pendingIntent.cancel();
                    // 알람 취소
                    v.getContext().sendBroadcast(alarmIntent);

                    PendingIntent sender = PendingIntent.getBroadcast(v.getContext(), REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_NO_CREATE);
                    if (sender == null) {
                        Log.e(TAG,"알람 없음!");
                    } else {
                        Log.e(TAG,"알람 있음! : " +sender);
                    }
                }
            }
        });
    }

    public void addItem( String tv_memo, String am_pm, String tv_repeat, String tv_qt, String hour, String minute, String tv_type, String tv_nq, String check_switch) {
        Collections.sort(arrayList, new Comparator<Time>() {
            @Override
            public int compare(Time o1, Time o2) {
                int result = 0, i_hour1, i_hour2, i_min1, i_min2;
                String s_hour1, s_hour2, s_apm1, s_apm2, s_min1, s_min2;
                s_hour1 = o1.getHour(); s_hour2 = o2.getHour(); // 시
                s_apm1 = o1.getAm_pm(); s_apm2 = o2.getAm_pm(); // 오전 오후
                s_min1 = o1.getMinute(); s_min2 = o2.getMinute(); // 분
                s_hour1 = s_hour1.substring(0,s_hour1.length() - 1); // ":" 빼주는 작업
                s_hour2 = s_hour2.substring(0,s_hour2.length() - 1);
                i_hour1 = Integer.parseInt(s_hour1); // string -> int
                i_hour2 = Integer.parseInt(s_hour2);
                i_min1 = Integer.parseInt(s_min1); //string -> int
                i_min2 = Integer.parseInt(s_min2);

                if(s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오전" + ".*") ){
                    if(i_hour1 < i_hour2){
                        result = -1; // 첫번째 시 < 두번째 시
                    } else if (i_hour1 == i_hour2){ // 첫번째 시 == 두번째 시
                        if(i_min1 < i_min2) // 첫번째 분 < 두번째 분
                            result = -1;
                        else if(i_min1 == i_min2) // 첫번째 분 == 두번째 분
                            result = 0;
                        else if(i_min1 > i_min2) // 첫번째 분 > 두번째 분
                            result = 1;
                    } else if(i_hour1 > i_hour2){
                        result = 1;
                    }
                }
                if(s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오후" + ".*") ) {
                    result = -1;
                }
                if(s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오전" + ".*") ) {
                    result = 1;
                }
                if(s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오후" + ".*") ) {
                    if(i_hour1 < i_hour2){
                        result = -1; // 첫번째 시 < 두번째 시
                    } else if (i_hour1 == i_hour2){ // 첫번째 시 == 두번째 시
                        if(i_min1 < i_min2) // 첫번째 분 < 두번째 분
                            result = -1;
                        else if(i_min1 == i_min2) // 첫번째 분 == 두번째 분
                            result = 0;
                        else if(i_min1 > i_min2) // 첫번째 분 > 두번째 분
                            result = 1;
                    } else if(i_hour1 > i_hour2){
                        result = 1;
                    }
                }
                return result;
            }
        });
        Time time = new Time(tv_memo, am_pm, tv_repeat, tv_qt, hour, minute, tv_type, tv_nq, check_switch);

        time.setTv_memo(tv_memo);
        time.setAm_pm(am_pm);
        time.setTv_repeat(tv_repeat);
        time.setTv_qt(tv_qt);
        time.setHour(hour);
        time.setMinute(minute);
        time.setTv_type(tv_type);
        time.setTv_nq(tv_nq);
        time.setSwitch(check_switch);

        arrayList.add(time);
    }
    public void bindItem( String tv_memo, String am_pm, String tv_repeat, String tv_qt, String hour, String minute, String tv_type, String tv_nq, String check_switch) {
        Collections.sort(arrayList, new Comparator<Time>() {
            @Override
            public int compare(Time o1, Time o2) {
                int result = 0, i_hour1, i_hour2, i_min1, i_min2;
                String s_hour1, s_hour2, s_apm1, s_apm2, s_min1, s_min2;
                s_hour1 = o1.getHour(); s_hour2 = o2.getHour(); // 시
                s_apm1 = o1.getAm_pm(); s_apm2 = o2.getAm_pm(); // 오전 오후
                s_min1 = o1.getMinute(); s_min2 = o2.getMinute(); // 분
                s_hour1 = s_hour1.substring(0,s_hour1.length() - 1); // ":" 빼주는 작업
                s_hour2 = s_hour2.substring(0,s_hour2.length() - 1);
                i_hour1 = Integer.parseInt(s_hour1); // string -> int
                i_hour2 = Integer.parseInt(s_hour2);
                i_min1 = Integer.parseInt(s_min1); //string -> int
                i_min2 = Integer.parseInt(s_min2);

                if(s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오전" + ".*") ){
                    if(i_hour1 < i_hour2){
                        result = -1; // 첫번째 시 < 두번째 시
                    } else if (i_hour1 == i_hour2){ // 첫번째 시 == 두번째 시
                        if(i_min1 < i_min2) // 첫번째 분 < 두번째 분
                            result = -1;
                        else if(i_min1 == i_min2) // 첫번째 분 == 두번째 분
                            result = 0;
                        else if(i_min1 > i_min2) // 첫번째 분 > 두번째 분
                            result = 1;
                    } else if(i_hour1 > i_hour2){
                        result = 1;
                    }
                }
                if(s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오후" + ".*") ) {
                    result = -1;
                }
                if(s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오전" + ".*") ) {
                    result = 1;
                }
                if(s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오후" + ".*") ) {
                    if(i_hour1 < i_hour2){
                        result = -1; // 첫번째 시 < 두번째 시
                    } else if (i_hour1 == i_hour2){ // 첫번째 시 == 두번째 시
                        if(i_min1 < i_min2) // 첫번째 분 < 두번째 분
                            result = -1;
                        else if(i_min1 == i_min2) // 첫번째 분 == 두번째 분
                            result = 0;
                        else if(i_min1 > i_min2) // 첫번째 분 > 두번째 분
                            result = 1;
                    } else if(i_hour1 > i_hour2){
                        result = 1;
                    }
                }
                return result;
            }
        });

        Time time = new Time(tv_memo, am_pm, tv_repeat, tv_qt, hour, minute, tv_type, tv_nq, check_switch);

        time.setTv_memo(tv_memo);
        time.setAm_pm(am_pm);
        time.setTv_repeat(tv_repeat);
        time.setTv_qt(tv_qt);
        time.setHour(hour);
        time.setMinute(minute);
        time.setTv_type(tv_type);
        time.setTv_nq(tv_nq);
        time.setSwitch(check_switch);

        arrayList.add(time);
    }

    //List 삭제 method
    public void removeItem(int position) {
        if(arrayList.size() < 1) {

        }
        else {
            arrayList.remove(position);
        }

    }


    public void removeItem() {
        if(arrayList.size() < 1) {

        }
        else {
            arrayList.remove(arrayList.size()-1);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_memo, am_pm, tv_repeat, tv_qt, hourText, minuteText, tv_nq;
        ImageView iv;
        Switch sw;
        RelativeLayout rl_text;
        String switch_change;

        ViewHolder(View itemView) {
            super(itemView);
             tv_memo = (TextView)itemView.findViewById(R.id.list_tv_memo);
             am_pm = (TextView)itemView.findViewById(R.id.am_pm);
             tv_repeat = (TextView)itemView.findViewById(R.id.list_tv_repeat);
             tv_qt = (TextView)itemView.findViewById(R.id.list_tv_qt);
             hourText = (TextView)itemView.findViewById(R.id.textTime1);
             minuteText = (TextView)itemView.findViewById(R.id.textTime2);
             iv = (ImageView) itemView.findViewById(R.id.iv_change);
             sw = (Switch) itemView.findViewById(R.id.switchBtn);
             tv_nq = (TextView)itemView.findViewById(R.id.list_tv_nq);
            rl_text = (RelativeLayout) itemView.findViewById(R.id.rl_text);


        }
        public void onBind(Time time){
            tv_memo.setText(time.getTv_memo());
            am_pm.setText(time.getAm_pm());
            tv_repeat.setText(time.getTv_repeat());
            tv_qt.setText(time.getTv_qt());
            hourText.setText(time.getHour());
            minuteText.setText(time.getMinute());
            tv_nq.setText(time.getTv_nq());
            get_tv_type = time.getTv_type();
            if(get_tv_type.matches(".*" + "벨" + ".*")){
                iv.setImageResource(iv_bell);
            } else {
                iv.setImageResource(iv_vibration);
            }
            switch_change = time.getSwitch();
            int int_switch = Integer.parseInt(switch_change);
            if(int_switch == 0){
                sw.setChecked(false);
                tv_repeat.setTextColor(Color.parseColor("#000000"));

                tv_memo.setTextColor(Color.parseColor("#AEACAC"));
                rl_text.setBackgroundResource(R.drawable.round_box_gray2);
                Log.e(TAG,"switch is "+switch_change);

            } else if (int_switch == 1){
                sw.setChecked(true);
                tv_repeat.setTextColor(Color.parseColor("#54D2C2"));
                rl_text.setBackgroundResource(R.drawable.round_back);
                Log.e(TAG,"switch is "+switch_change);
            } else {
                Log.e(TAG,"switch값 오류");
            }
        }

    }


}
