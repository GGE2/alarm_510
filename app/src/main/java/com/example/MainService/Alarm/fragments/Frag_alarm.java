package com.example.MainService.Alarm.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Login.etc.PreferenceManager;
import com.example.MainService.Alarm.activities.AddAlarmActivity;
//import com.example.MainService.Alarm.activities.OnTimeItemLongClickListener;
import com.example.MainService.Alarm.activities.FixAlarmActivity;
import com.example.MainService.Alarm.activities.ReStartService;
import com.example.MainService.Alarm.activities.RingtoneService;
import com.example.alarm.R;
import com.example.MainService.Alarm.activities.AdapterActivity;
import com.example.MainService.Alarm.activities.Time;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class Frag_alarm extends Fragment {
    private View view1, view2;
    private ImageButton btn_plus;
    private AdapterActivity arrayAdapter;
    private Handler handler;
    private ListView listView;
    private ImageView iv;
    public Parcelable Pstate;

    private static final String TAG = Frag_alarm.class.getSimpleName();

    public String tv_memo = "", tv_repeat = "";

    public static final int REQUEST_CODE1 = 1000;
    public static final int REQUEST_CODE2 = 1001;
    ArrayList<Parcelable>  dataset;
    int adapterPosition;
    private static String file[] = new String[100];
    private int mPosition = RecyclerView.NO_POSITION;
    private static Bundle mBundleRecyclerViewState;
    public LinearLayoutManager lm;
    public RecyclerView rv;
    String str_tv_memo = "", am_pm = "", str_tv_repeat = "", str_tv_qt = "", str_hour = "", str_minute = "", str_tv_type = "", str_tv_nq = "";
    String testvalue = "";
    String user_id;
    String url;
    String json;
    int pri_state = 0;
    String current_switch = "1";
    SharedPreferences pref;
    int count;
    ArrayList<Time> arrayList = new ArrayList<Time>();


    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.frag_alarm, container, false); // 프래그먼트 연결
        btn_plus = (ImageButton) view1.findViewById(R.id.btn_plus);
        rv = (RecyclerView) view1.findViewById(R.id.recycler_view);
        if(pri_state == 0) {
            sendRequest();
        }
        pri_state++;

        arrayAdapter = new AdapterActivity(getLayoutInflater(), arrayList);
                        lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rv.setLayoutManager(lm);


                        arrayAdapter.setOnItemClickListener(new AdapterActivity.OnTimeItemClickListener() {
                            @Override
                            public void onItemClick(AdapterActivity.ViewHolder holder, View v, int position) {
                                Log.e(TAG, "click");
                                FileWriter fw = null;

                                Collections.sort(arrayList, new Comparator<Time>() {
                                    @Override
                                    public int compare(Time o1, Time o2) {
                                        int result = 0, i_hour1, i_hour2, i_min1, i_min2;
                                        String s_hour1, s_hour2, s_apm1, s_apm2, s_min1, s_min2;
                                        s_hour1 = o1.getHour();
                                        s_hour2 = o2.getHour(); // 시
                                        s_apm1 = o1.getAm_pm();
                                        s_apm2 = o2.getAm_pm(); // 오전 오후
                                        s_min1 = o1.getMinute();
                                        s_min2 = o2.getMinute(); // 분
                                        s_hour1 = s_hour1.substring(0, s_hour1.length() - 1); // ":" 빼주는 작업
                                        s_hour2 = s_hour2.substring(0, s_hour2.length() - 1);
                                        i_hour1 = Integer.parseInt(s_hour1); // string -> int
                                        i_hour2 = Integer.parseInt(s_hour2);
                                        i_min1 = Integer.parseInt(s_min1); //string -> int
                                        i_min2 = Integer.parseInt(s_min2);

                                        if (s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오전" + ".*")) {
                                            if (i_hour1 < i_hour2) {
                                                result = -1; // 첫번째 시 < 두번째 시
                                            } else if (i_hour1 == i_hour2) { // 첫번째 시 == 두번째 시
                                                if (i_min1 < i_min2) // 첫번째 분 < 두번째 분
                                                    result = -1;
                                                else if (i_min1 == i_min2) // 첫번째 분 == 두번째 분
                                                    result = 0;
                                                else if (i_min1 > i_min2) // 첫번째 분 > 두번째 분
                                                    result = 1;
                                            } else if (i_hour1 > i_hour2) {
                                                result = 1;
                                            }
                                        }
                                        if (s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오후" + ".*")) {
                                            result = -1;
                                        }
                                        if (s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오전" + ".*")) {
                                            result = 1;
                                        }
                                        if (s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오후" + ".*")) {
                                            if (i_hour1 < i_hour2) {
                                                result = -1; // 첫번째 시 < 두번째 시
                                            } else if (i_hour1 == i_hour2) { // 첫번째 시 == 두번째 시
                                                if (i_min1 < i_min2) // 첫번째 분 < 두번째 분
                                                    result = -1;
                                                else if (i_min1 == i_min2) // 첫번째 분 == 두번째 분
                                                    result = 0;
                                                else if (i_min1 > i_min2) // 첫번째 분 > 두번째 분
                                                    result = 1;
                                            } else if (i_hour1 > i_hour2) {
                                                result = 1;
                                            }
                                        }
                                        return result;
                                    }
                                });

                                try {
                                    Gson gson = new Gson();
                                    JsonObject obj = new JsonObject();

                                    obj.addProperty("tv_memo", arrayList.get(position).getTv_memo());
                                    obj.addProperty("am_pm", arrayList.get(position).getAm_pm());
                                    obj.addProperty("tv_repeat", arrayList.get(position).getTv_repeat());
                                    obj.addProperty("tv_qt", arrayList.get(position).getTv_qt());
                                    obj.addProperty("hour", arrayList.get(position).getHour());
                                    obj.addProperty("minute", arrayList.get(position).getMinute());
                                    obj.addProperty("tv_type", arrayList.get(position).getTv_type());
                                    obj.addProperty("tv_nq", arrayList.get(position).getTv_nq());
                                    obj.addProperty("switch",arrayList.get(position).getSwitch());
                                    json = gson.toJson(obj);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String filename = getActivity().getDataDir() + "/fixdata.json";
                                try {
                                    fw = new FileWriter(filename);
                                    System.out.println(filename);

                                    BufferedWriter bufwr = new BufferedWriter(fw);
                                    bufwr.write(json);

                                    bufwr.close();
                                    arrayList.remove(position);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Intent intent1 = new Intent(getActivity(), FixAlarmActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE2);
                                arrayAdapter.notifyDataSetChanged();


                            }
                        });
                        rv.setAdapter(arrayAdapter);


                        btn_plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view2) {
                                Intent intent = new Intent(getActivity(), AddAlarmActivity.class);
                                startActivityForResult(intent, REQUEST_CODE1);
                            }
                        });

                        return view1;
                    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //시간 리스트 추가
        if (requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null) {

            int line_count = 0, iv_bell = R.drawable.bell, iv_vibration = R.drawable.vibration;

            Collections.sort(arrayList, new Comparator<Time>() {
                @Override
                public int compare(Time o1, Time o2) {
                    int result = 0, i_hour1, i_hour2, i_min1, i_min2;
                    String s_hour1, s_hour2, s_apm1, s_apm2, s_min1, s_min2;
                    s_hour1 = o1.getHour();
                    s_hour2 = o2.getHour(); // 시
                    s_apm1 = o1.getAm_pm();
                    s_apm2 = o2.getAm_pm(); // 오전 오후
                    s_min1 = o1.getMinute();
                    s_min2 = o2.getMinute(); // 분
                    s_hour1 = s_hour1.substring(0, s_hour1.length() - 1); // ":" 빼주는 작업
                    s_hour2 = s_hour2.substring(0, s_hour2.length() - 1);
                    i_hour1 = Integer.parseInt(s_hour1); // string -> int
                    i_hour2 = Integer.parseInt(s_hour2);
                    i_min1 = Integer.parseInt(s_min1); //string -> int
                    i_min2 = Integer.parseInt(s_min2);

                    if (s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오전" + ".*")) {
                        if (i_hour1 < i_hour2) {
                            result = -1; // 첫번째 시 < 두번째 시
                        } else if (i_hour1 == i_hour2) { // 첫번째 시 == 두번째 시
                            if (i_min1 < i_min2) // 첫번째 분 < 두번째 분
                                result = -1;
                            else if (i_min1 == i_min2) // 첫번째 분 == 두번째 분
                                result = 0;
                            else if (i_min1 > i_min2) // 첫번째 분 > 두번째 분
                                result = 1;
                        } else if (i_hour1 > i_hour2) {
                            result = 1;
                        }
                    }
                    if (s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오후" + ".*")) {
                        result = -1;
                    }
                    if (s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오전" + ".*")) {
                        result = 1;
                    }
                    if (s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오후" + ".*")) {
                        if (i_hour1 < i_hour2) {
                            result = -1; // 첫번째 시 < 두번째 시
                        } else if (i_hour1 == i_hour2) { // 첫번째 시 == 두번째 시
                            if (i_min1 < i_min2) // 첫번째 분 < 두번째 분
                                result = -1;
                            else if (i_min1 == i_min2) // 첫번째 분 == 두번째 분
                                result = 0;
                            else if (i_min1 > i_min2) // 첫번째 분 > 두번째 분
                                result = 1;
                        } else if (i_hour1 > i_hour2) {
                            result = 1;
                        }
                    }
                    return result;
                }
            });

            try {
                // 파일읽어오기 : 내부 디랙토리, 파일이름
                String filename = getActivity().getDataDir() + "/data.json";
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
                System.out.println("1번 완료");

                Gson gson = new Gson();
                Time time = gson.fromJson(file[0], Time.class);

                str_tv_memo = time.getTv_memo();
                am_pm = time.getAm_pm();
                str_tv_repeat = time.getTv_repeat();
                str_tv_qt = time.getTv_qt();
                str_hour = time.getHour();
                str_minute = time.getMinute();
                str_tv_type = time.getTv_type();
                str_tv_nq = time.getTv_nq();
                current_switch = time.getSwitch();

                try {
                    str_tv_memo.trim();
                    am_pm.trim();
                    str_tv_repeat.trim();
                    str_tv_qt.trim();
                    str_hour.trim();
                    str_minute.trim();
                    str_tv_nq.trim();

                } catch (Exception e) {

                }

//                arrayList.add(new Time(str_tv_memo, am_pm, str_tv_repeat, str_tv_qt, str_hour, str_minute, str_tv_type, str_tv_nq));
                arrayAdapter.addItem(str_tv_memo, am_pm, str_tv_repeat, str_tv_qt, str_hour, str_minute, str_tv_type, str_tv_nq, current_switch);
                arrayAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //시간 리스트 터치 시 변경된 시간값 저장
        if (requestCode == REQUEST_CODE2 && resultCode == RESULT_OK && data != null) {
            String pri_file[] = new String[100];

            int line_count = 0, iv_bell = R.drawable.bell, iv_vibration = R.drawable.vibration;
            Collections.sort(arrayList, new Comparator<Time>() {
                @Override
                public int compare(Time o1, Time o2) {
                    int result = 0, i_hour1, i_hour2, i_min1, i_min2;
                    String s_hour1, s_hour2, s_apm1, s_apm2, s_min1, s_min2;
                    s_hour1 = o1.getHour();
                    s_hour2 = o2.getHour(); // 시
                    s_apm1 = o1.getAm_pm();
                    s_apm2 = o2.getAm_pm(); // 오전 오후
                    s_min1 = o1.getMinute();
                    s_min2 = o2.getMinute(); // 분
                    s_hour1 = s_hour1.substring(0, s_hour1.length() - 1); // ":" 빼주는 작업
                    s_hour2 = s_hour2.substring(0, s_hour2.length() - 1);
                    i_hour1 = Integer.parseInt(s_hour1); // string -> int
                    i_hour2 = Integer.parseInt(s_hour2);
                    i_min1 = Integer.parseInt(s_min1); //string -> int
                    i_min2 = Integer.parseInt(s_min2);

                    if (s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오전" + ".*")) {
                        if (i_hour1 < i_hour2) {
                            result = -1; // 첫번째 시 < 두번째 시
                        } else if (i_hour1 == i_hour2) { // 첫번째 시 == 두번째 시
                            if (i_min1 < i_min2) // 첫번째 분 < 두번째 분
                                result = -1;
                            else if (i_min1 == i_min2) // 첫번째 분 == 두번째 분
                                result = 0;
                            else if (i_min1 > i_min2) // 첫번째 분 > 두번째 분
                                result = 1;
                        } else if (i_hour1 > i_hour2) {
                            result = 1;
                        }
                    }
                    if (s_apm1.matches(".*" + "오전" + ".*") && s_apm2.matches(".*" + "오후" + ".*")) {
                        result = -1;
                    }
                    if (s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오전" + ".*")) {
                        result = 1;
                    }
                    if (s_apm1.matches(".*" + "오후" + ".*") && s_apm2.matches(".*" + "오후" + ".*")) {
                        if (i_hour1 < i_hour2) {
                            result = -1; // 첫번째 시 < 두번째 시
                        } else if (i_hour1 == i_hour2) { // 첫번째 시 == 두번째 시
                            if (i_min1 < i_min2) // 첫번째 분 < 두번째 분
                                result = -1;
                            else if (i_min1 == i_min2) // 첫번째 분 == 두번째 분
                                result = 0;
                            else if (i_min1 > i_min2) // 첫번째 분 > 두번째 분
                                result = 1;
                        } else if (i_hour1 > i_hour2) {
                            result = 1;
                        }
                    }
                    return result;
                }
            });

            try {
                // 파일읽어오기 : 내부 디랙토리, 파일이름
                String filename = getActivity().getDataDir() + "/data.json";
                FileReader fr = new FileReader(filename);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    pri_file[line_count] = line;
                    line_count++;
                    System.out.println(line);
                    line = br.readLine();
                }
                br.close();
                System.out.println("2번 완료");

                Gson gson = new Gson();
                Time time = gson.fromJson(pri_file[0], Time.class);

                str_tv_memo = time.getTv_memo();
                am_pm = time.getAm_pm();
                str_tv_repeat = time.getTv_repeat();
                str_tv_qt = time.getTv_qt();
                str_hour = time.getHour();
                str_minute = time.getMinute();
                str_tv_type = time.getTv_type();
                str_tv_nq = time.getTv_nq();
                current_switch = time.getSwitch();
                Log.e(TAG,"switch2 : "+current_switch);

                str_tv_memo.trim();
                am_pm.trim();
                str_tv_repeat.trim();
                str_tv_qt.trim();
                str_hour.trim();
                str_minute.trim();
                str_tv_nq.trim();
                Log.e(TAG, "json값 확인 2" + pri_file[0]);

//                arrayList.add(new Time(str_tv_memo, am_pm, str_tv_repeat, str_tv_qt, str_hour, str_minute, str_tv_type, str_tv_nq));

                arrayAdapter.addItem(str_tv_memo, am_pm, str_tv_repeat, str_tv_qt, str_hour, str_minute, str_tv_type, str_tv_nq, current_switch);
                arrayAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void sendRequest() {
        user_id = PreferenceManager.getString(getActivity().getApplicationContext(),"User_Uid");
        url = "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8001/alarmlist?id="+user_id;
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
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


//                                arrayList.add(new Time(str_tv_memo, am_pm, str_tv_repeat, str_tv_qt, str_hour, str_minute, str_tv_type, str_tv_nq));

                                    arrayAdapter.bindItem(pri_memo, pri_ampm, pri_repeat, pri_qt, pri_hour, pri_minute, pri_type, pri_nq, check_switch);
                                    arrayAdapter.notifyDataSetChanged();

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
    }


}






