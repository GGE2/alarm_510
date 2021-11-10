package com.example.MainService.Alarm.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Login.etc.PreferenceManager;
import com.example.alarm.R;
import com.example.MainService.Alarm.fragments.Frag_alarm;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FixAlarmActivity extends AppCompatActivity {

    public static TextView text_add, text_qt, text_explain, text_clock, text_repeat, text_type, text_nq,
            tv_qt, tv_ex, tv_repeat, tv_type, tv_nq;
    private ToggleButton tg_Monday, tg_Tuesday, tg_Wednesday, tg_Thursday, tg_Friday, tg_Saturday, tg_Sunday;
    private RadioGroup rg, rg_nq;
    private RadioButton rb_bell, rb_vibration, rb_nq1, rb_nq2, rb_nq3, rb_nqno;
    private EditText et_explain;
    private Button btn_cancel, btn_store;
    private RelativeLayout set_cat,set_alarm_info,set_alarm_type,set_pro;

    private CheckBox cb_eng, cb_su, cb_sok, cb_saja, cb_his, cb_tong;
    private RelativeLayout dia_cat, dia_repeat, dia_type, dia_nq, dia_clock;
    private TimePicker tp;
    private int hour, minute, originhour, nMinute ,int_hour, int_minute;

    private static final String TAG = FixAlarmActivity.class.getSimpleName();
    String pri_tg = "";
    private static String am_pm, str_qt, str_memo, str_repeat, str_type, str_nq, str_hour, str_min,
            pri_str_hour ="", pri_str_minute ="", pri_str_repeat = "", pri_str_qt = "", json, iv_c,
    pri_str_ex = "", pri_str_type ="", pri_str_nq = "",
            more_str_qt = "", more_str_ex ="", more_str_type = "", more_str_nq = "";
    String result = "";
    String result_tg = "";
    String result_type = "";
    String result_nq = "";
    String result_ex = "";
    String user_id;
    String url, check_switch = "1";
    int id_mon = 0, id_tue = 1, id_wed = 2, id_thur = 3, id_fri = 4, id_sat = 5, id_sun = 6;
    int totalID = 0;
    String REQUEST_ID;
    private static String file[] = new String[100];
    int line_count = 0, iv_bell = R.drawable.bell, iv_vibration = R.drawable.vibration, iv_change = 0;
    public static final int REQUEST_CODE1 = 1000;
    AlarmManager alarm_manager;
    SharedPreferences pref;
    PendingIntent pendingIntent;
    private static int REQUEST_CODE3 = 1111;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm);

        try {
            String filename = getDataDir() + "/fixdata.json";
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line!=null) {
                sb.append(line);
                file[line_count] = line;
                line_count++;
                System.out.println(line);
                line = br.readLine();
            }
            br.close();
            System.out.println("br.readLine 반복문 종료");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Time time = gson.fromJson(file[0], Time.class);

        Log.e(TAG,"json값 확인 1"+file[0]);
        pri_str_ex = time.getTv_memo();
        am_pm = time.getAm_pm();
        pri_str_repeat = time.getTv_repeat();
        pri_str_qt  = time.getTv_qt();
        pri_str_hour = time.getHour();
        pri_str_minute = time.getMinute();
        pri_str_type = time.getTv_type();
        pri_str_nq = time.getTv_nq();

        pri_str_ex.trim(); am_pm.trim();
        pri_str_repeat.trim(); pri_str_qt.trim();
        pri_str_hour.trim(); pri_str_minute.trim();
        pri_str_type.trim(); pri_str_nq.trim();

        if(pri_str_repeat.contains("월")) totalID += id_mon;
        if(pri_str_repeat.contains("화")) totalID += id_tue;
        if(pri_str_repeat.contains("수")) totalID += id_wed;
        if(pri_str_repeat.contains("목")) totalID += id_thur;
        if(pri_str_repeat.contains("금")) totalID += id_fri;
        if(pri_str_repeat.contains("토")) totalID += id_sat;
        if(pri_str_repeat.contains("일")) totalID += id_sun;

        REQUEST_ID = totalID +""+ pri_str_hour +""+ pri_str_minute +"";
        Log.e(TAG,""+REQUEST_ID);
        deleteRequest(REQUEST_ID);
        totalID = 0;

        tp = (TimePicker) findViewById(R.id.tp);

        text_add = findViewById(R.id.text_add);
        text_qt = findViewById(R.id.text_qt);
        text_explain = findViewById(R.id.text_explain);
        text_clock = findViewById(R.id.text_clock);
        text_repeat = findViewById(R.id.text_repeat);
        text_type = findViewById(R.id.text_type);
        text_nq = findViewById(R.id.text_nq);

        tv_qt = (TextView) findViewById(R.id.tv_qt);
        tv_ex = (TextView) findViewById(R.id.tv_ex);
        tv_repeat = (TextView) findViewById(R.id.tv_repeat);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_nq = (TextView) findViewById(R.id.tv_nq);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_store = findViewById(R.id.btn_store);


        set_cat = findViewById(R.id.question_type);
        set_alarm_info = findViewById(R.id.alarm_explain);
        set_alarm_type = findViewById(R.id.alarm_type);
        set_pro = findViewById(R.id.number_of_question);


        tg_Monday = (ToggleButton) findViewById(R.id.tg_Monday);
        tg_Tuesday = (ToggleButton) findViewById(R.id.tg_Tuesday);
        tg_Wednesday = (ToggleButton) findViewById(R.id.tg_Wednesday);
        tg_Thursday = (ToggleButton) findViewById(R.id.tg_Thursday);
        tg_Friday = (ToggleButton) findViewById(R.id.tg_Friday);
        tg_Saturday = (ToggleButton) findViewById(R.id.tg_Saturday);
        tg_Sunday = (ToggleButton) findViewById(R.id.tg_Sunday);

        tv_ex.setText(pri_str_ex);
        tv_repeat.setText(pri_str_repeat);
        tv_qt.setText(pri_str_qt);
        tv_type.setText(pri_str_type);
        tv_nq.setText(pri_str_nq);

        pri_str_hour = pri_str_hour.substring(0,pri_str_hour.length() - 1);

        int_hour = Integer.parseInt(pri_str_hour);
        int_minute = Integer.parseInt(pri_str_minute);

        tp.setHour(int_hour);
        tp.setMinute(int_minute);

        if(pri_str_repeat.matches(".*" + "월" + ".*"))
            tg_Monday.setChecked(true);
        if(pri_str_repeat.matches(".*" + "화" + ".*"))
            tg_Tuesday.setChecked(true);
        if(pri_str_repeat.matches(".*" + "수" + ".*"))
            tg_Wednesday.setChecked(true);
        if(pri_str_repeat.matches(".*" + "목" + ".*"))
            tg_Thursday.setChecked(true);
        if(pri_str_repeat.matches(".*" + "금" + ".*"))
            tg_Friday.setChecked(true);
        if(pri_str_repeat.matches(".*" + "토" + ".*"))
            tg_Saturday.setChecked(true);
        if(pri_str_repeat.matches(".*" + "일" + ".*"))
            tg_Sunday.setChecked(true);


        tg_Monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    result_tg += "월,";
                    pri_tg = ToggleCheck(tg_Monday, tg_Tuesday, tg_Wednesday, tg_Thursday, tg_Friday, tg_Saturday, tg_Sunday);
                    if ((result_tg).compareTo(pri_tg) == 0) tv_repeat.setText(result_tg);
                    else tv_repeat.setText(pri_tg);
                    pri_tg = "";
                } else {
                    result_tg = result_tg.replace("월,", "");
                    tv_repeat.setText(result_tg);
                }
            }
        });

        tg_Tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    result_tg += "화,";
                    pri_tg = ToggleCheck(tg_Monday, tg_Tuesday, tg_Wednesday, tg_Thursday, tg_Friday, tg_Saturday, tg_Sunday);
                    if ((result_tg).compareTo(pri_tg) == 0) tv_repeat.setText(result_tg);
                    else tv_repeat.setText(pri_tg);
                    pri_tg = "";
                } else {
                    result_tg = result_tg.replace("화,", "");
                    tv_repeat.setText(result_tg);
                }
            }
        });

        tg_Wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    result_tg += "수,";
                    pri_tg = ToggleCheck(tg_Monday, tg_Tuesday, tg_Wednesday, tg_Thursday, tg_Friday, tg_Saturday, tg_Sunday);
                    if ((result_tg).compareTo(pri_tg) == 0) tv_repeat.setText(result_tg);
                    else tv_repeat.setText(pri_tg);
                    pri_tg = "";
                } else {
                    result_tg = result_tg.replace("수,", "");
                    tv_repeat.setText(result_tg);
                }
            }
        });

        tg_Thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    result_tg += "목,";
                    pri_tg = ToggleCheck(tg_Monday, tg_Tuesday, tg_Wednesday, tg_Thursday, tg_Friday, tg_Saturday, tg_Sunday);
                    if ((result_tg).compareTo(pri_tg) == 0) tv_repeat.setText(result_tg);
                    else tv_repeat.setText(pri_tg);
                    pri_tg = "";
                } else {
                    result_tg = result_tg.replace("목,", "");
                    tv_repeat.setText(result_tg);
                }
            }
        });

        tg_Friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    result_tg += "금,";
                    pri_tg = ToggleCheck(tg_Monday, tg_Tuesday, tg_Wednesday, tg_Thursday, tg_Friday, tg_Saturday, tg_Sunday);
                    if ((result_tg).compareTo(pri_tg) == 0) tv_repeat.setText(result_tg);
                    else tv_repeat.setText(pri_tg);
                    pri_tg = "";
                } else {
                    result_tg = result_tg.replace("금,", "");
                    tv_repeat.setText(result_tg);
                }
            }
        });

        tg_Saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    result_tg += "토,";
                    pri_tg = ToggleCheck(tg_Monday, tg_Tuesday, tg_Wednesday, tg_Thursday, tg_Friday, tg_Saturday, tg_Sunday);
                    if ((result_tg).compareTo(pri_tg) == 0) tv_repeat.setText(result_tg);
                    else tv_repeat.setText(pri_tg);
                    pri_tg = "";
                } else {
                    result_tg = result_tg.replace("토,", "");
                    tv_repeat.setText(result_tg);
                }
            }
        });

        tg_Sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    result_tg += "일,";
                    pri_tg = ToggleCheck(tg_Monday, tg_Tuesday, tg_Wednesday, tg_Thursday, tg_Friday, tg_Saturday, tg_Sunday);
                    if ((result_tg).compareTo(pri_tg) == 0) tv_repeat.setText(result_tg);
                    else tv_repeat.setText(pri_tg);
                    pri_tg = "";
                } else {
                    result_tg = result_tg.replace("일,", "");
                    tv_repeat.setText(result_tg);
                }
            }
        });

        tp.setIs24HourView(false); //오전, 오후 형식
       /* tp.setOnTimeChangedListener(this); //현재 시간값 받기*/

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FixAlarmActivity.this, "알람을 수정해주세요!", Toast.LENGTH_SHORT).show();
//                Intent sendIntent = new Intent(FixAlarmActivity.this, Frag_alarm.class);
//                setResult(RESULT_OK, sendIntent);
//                finish();
            }
        });

        set_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_catagory, null);
                AlertDialog.Builder ad = new AlertDialog.Builder(FixAlarmActivity.this, R.style.MyDialogTheme);
                ad.create();
                ad.setTitle("출제 문제");
                ad.setMessage("카테고리를 골라주세요");
                ad.setView(dialogView);

                more_str_qt = tv_qt.getText()+"";
                cb_eng = (CheckBox) dialogView.findViewById(R.id.cb_eng);
                cb_su = (CheckBox) dialogView.findViewById(R.id.cb_su);
                cb_sok = (CheckBox) dialogView.findViewById(R.id.cb_sok);
                cb_saja = (CheckBox) dialogView.findViewById(R.id.cb_saja);
                cb_his = (CheckBox) dialogView.findViewById(R.id.cb_his);
                cb_tong = (CheckBox) dialogView.findViewById(R.id.cb_tong);

                if(pri_str_qt.matches(".*" + "통합" + ".*") || more_str_qt.matches(".*" + "통합" + ".*"))
                    cb_tong.setChecked(true);
                    cb_eng.setChecked(false);
                    cb_su.setChecked(false);
                    cb_sok.setChecked(false);
                    cb_saja.setChecked(false);
                    cb_his.setChecked(false);
                if(pri_str_qt.matches(".*" + "영단어" + ".*")|| more_str_qt.matches(".*" + "영단어" + ".*"))
                    cb_eng.setChecked(true);
                if(pri_str_qt.matches(".*" + "수도" + ".*")|| more_str_qt.matches(".*" + "수도" + ".*"))
                    cb_su.setChecked(true);
                if(pri_str_qt.matches(".*" + "속담" + ".*")|| more_str_qt.matches(".*" + "속담" + ".*"))
                    cb_sok.setChecked(true);
                if(pri_str_qt.matches(".*" + "사자성어" + ".*")|| more_str_qt.matches(".*" + "사자성어" + ".*"))
                    cb_saja.setChecked(true);
                if(pri_str_qt.matches(".*" + "역사" + ".*")|| more_str_qt.matches(".*" + "역사" + ".*"))
                    cb_his.setChecked(true);

                pri_str_qt = "";
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_qt.setText(SendCheck(cb_eng, cb_su, cb_sok, cb_saja, cb_his, cb_tong));
                        result = "";
                        dialog.dismiss();
                    }


                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();

            }

        });

        set_alarm_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ad = new AlertDialog.Builder(FixAlarmActivity.this, R.style.MyDialogTheme);
                ad.setTitle("알람 설명");
                ad.setMessage("알람에 대해 메모하세요");
                et_explain = new EditText(FixAlarmActivity.this);
                more_str_ex = tv_ex.getText() + "";
                if(pri_str_ex != null)
                    et_explain.setText(pri_str_ex);
                else if (more_str_ex != null)
                    et_explain.setText(more_str_ex);
                pri_str_ex = "";
                et_explain.setPrivateImeOptions("defaultInputmode=korean;");
                et_explain.setTextColor(Color.parseColor("#FFFFFF"));
                ad.setView(et_explain);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result_ex = et_explain.getText().toString();
                        tv_ex.setText(result_ex);
                        result_ex = "";
                        dialog.dismiss();
                    }

                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();

            }

        });

        set_alarm_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_type, null);
                AlertDialog.Builder ad = new AlertDialog.Builder(FixAlarmActivity.this, R.style.MyDialogTheme);
                ad.setTitle("알람 타입");
                ad.setMessage("알람 타입을 골라주세요(진동/벨소리)");
                ad.setView(dialogView);

                rg = (RadioGroup) dialogView.findViewById(R.id.rg);
                rb_bell = (RadioButton) dialogView.findViewById(R.id.rb_bell);
                rb_vibration = (RadioButton) dialogView.findViewById(R.id.rb_vibration);

                dia_type = (RelativeLayout) dialogView.findViewById(R.id.dia_type);

                more_str_type = tv_type.getText() + "";
                if(pri_str_type.matches(".*" + "벨" + ".*") || more_str_type.matches(".*" + "벨" + ".*"))
                    rb_bell.setChecked(true);
                if(pri_str_type.matches(".*" + "진동" + ".*") || more_str_type.matches(".*" + "진동" + ".*"))
                    rb_vibration.setChecked(true);

                pri_str_type = "";
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_type.setText(TypeCheck(rb_bell, rb_vibration));
                        result_type = "";
                        dialog.dismiss();
                    }

                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();

            }

        });

        set_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_nq, null);
                AlertDialog.Builder ad = new AlertDialog.Builder(FixAlarmActivity.this, R.style.MyDialogTheme);
                ad.setTitle("문제 출제수");
                ad.setMessage("문제 출제 수를 정해주세요(최대 3개)");
                ad.setView(dialogView);

                rg_nq = (RadioGroup) dialogView.findViewById(R.id.rg_nq);

                rb_nq1 = (RadioButton) dialogView.findViewById(R.id.rb_nq1);
                rb_nq2 = (RadioButton) dialogView.findViewById(R.id.rb_nq2);
                rb_nq3 = (RadioButton) dialogView.findViewById(R.id.rb_nq3);
                rb_nqno = (RadioButton) dialogView.findViewById(R.id.rb_nqno);

                dia_nq = (RelativeLayout) dialogView.findViewById(R.id.dia_nq);

                more_str_nq = tv_nq.getText() + "";
                if(pri_str_nq.matches(".*" + "1" + ".*") || more_str_nq.matches(".*" + "1" + ".*"))
                    rb_nq1.setChecked(true);
                if(pri_str_nq.matches(".*" + "2" + ".*") || more_str_nq.matches(".*" + "2" + ".*"))
                    rb_nq2.setChecked(true);
                if(pri_str_nq.matches(".*" + "3" + ".*") || more_str_nq.matches(".*" + "3" + ".*"))
                    rb_nq3.setChecked(true);
                if(pri_str_nq.matches(".*" + "문제" + ".*") || more_str_nq.matches(".*" + "문제" + ".*"))
                    rb_nqno.setChecked(true);

                pri_str_nq ="";
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_nq.setText(NQCheck(rb_nq1, rb_nq2, rb_nq3, rb_nqno));
                        result_nq = "";
                        dialog.dismiss();
                    }

                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();

            }

        });

        btn_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    hour = tp.getHour();
                    minute = tp.getMinute();
                } else {
                    hour = tp.getCurrentHour();
                    minute = tp.getCurrentMinute();
                }

                str_qt = tv_qt.getText() + "";
                str_memo = tv_ex.getText() + "";
                str_repeat = tv_repeat.getText() + "";
                str_type = tv_type.getText() + "";
                str_nq = tv_nq.getText() + "";

                str_qt.trim();
                str_repeat.trim();
                try {
                    Character pos_str_qt = str_qt.charAt((str_qt.length() - 1));
                    Character pos_str_repeat = str_repeat.charAt((str_repeat.length() - 1));

                    if (str_qt != null && (pos_str_qt.toString()).contains(","))
                        str_qt = str_qt.substring(0, str_qt.length() - 1);

                    if (str_repeat != null && (pos_str_repeat.toString()).contains(","))
                        str_repeat = str_repeat.substring(0, str_repeat.length() - 1);

                    originhour = hour;
                    am_pm = AM_PM(hour);
                    hour = timeSet(hour);
                    str_hour = hour + ":";
                    str_min = minute + "";

                } catch (Exception e) {
                    Log.e(TAG, "에러");
                }
                if (str_repeat.contains("월")) totalID += id_mon;
                if(str_repeat.contains("화")) totalID += id_tue;
                if(str_repeat.contains("수")) totalID += id_wed;
                if(str_repeat.contains("목")) totalID += id_thur;
                if(str_repeat.contains("금")) totalID += id_fri;
                if(str_repeat.contains("토")) totalID += id_sat;
                if(str_repeat.contains("일")) totalID += id_sun;

                REQUEST_ID = totalID +""+ hour +""+ minute +"";
                Log.e(TAG,""+REQUEST_ID);

                try {
                    Gson gson = new Gson();
                    JsonObject obj = new JsonObject();

                    obj.addProperty("tv_memo", str_memo);
                    obj.addProperty("am_pm", am_pm);
                    obj.addProperty("tv_repeat", str_repeat);
                    obj.addProperty("tv_qt", str_qt);
                    obj.addProperty("hour", str_hour);
                    obj.addProperty("minute", str_min);
                    obj.addProperty("tv_type", iv_change);
                    obj.addProperty("tv_nq", str_nq);
                    obj.addProperty("check_switch",check_switch);
                    json = gson.toJson(obj);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                FileWriter fw = null;
                String filename = getDataDir() + "/data.json";
                try {

                    fw = new FileWriter(filename);
                    System.out.println(filename);

                    BufferedWriter bufwr = new BufferedWriter(fw);
                    bufwr.write(json);
                    bufwr.close();

                    System.out.println("파일 쓰기 완료");
                    Intent sendIntent = new Intent(FixAlarmActivity.this, Frag_alarm.class);
                    setResult(RESULT_OK, sendIntent);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                    alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    final Calendar calendar = Calendar.getInstance();
                    Log.e(TAG,"알람 설정중 "+originhour+": "+minute);

                    // 현재 지정된 시간으로 알람 시간 설정
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, originhour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);

                    // reveiver에 string 값 넘겨주기
                    Intent alarmIntent = new Intent(FixAlarmActivity.this, AlarmReceiver.class);
                    alarmIntent.putExtra("state", "ALARM_ON");
                    alarmIntent.putExtra("tv_type",str_type);
                    alarmIntent.putExtra("request_code",REQUEST_ID);
                    alarmIntent.putExtra("tv_repeat",str_repeat);
                    alarmIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                    REQUEST_CODE3 = Integer.parseInt(REQUEST_ID);
                    Log.e(TAG, "" + REQUEST_CODE3);
                    // receiver를 동작하게 하기 위해 PendingIntent의 인스턴스를 생성할 때, getBroadcast 라는 메소드를 사용
                    // requestCode는 나중에 Alarm을 해제 할때 어떤 Alarm을 해제할지를 식별하는 코드
                    pendingIntent = PendingIntent.getBroadcast(FixAlarmActivity.this, REQUEST_CODE3, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    long currentTime = System.currentTimeMillis(); // 현재 시간
                    //long triggerTime = SystemClock.elapsedRealtime() + 1000*60;
                    long triggerTime = calendar.getTimeInMillis(); // 알람을 울릴 시간
                    long interval = 1000 * 60 * 60 * 24 * 7; // 일주일 시간

                    while (currentTime > triggerTime) { // 현재 시간보다 작다면
                        triggerTime += interval; // 다음날 울리도록 처리
                    }
                    Log.e(TAG, "set Alarm : " + hour + " 시 " + minute + "분");

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
                        alarm_manager.set(AlarmManager.RTC_WAKEUP, triggerTime,pendingIntent);
                        //알람 매니저를 통한 반복알람 설정
                        alarm_manager.setRepeating(AlarmManager.RTC, triggerTime, interval, pendingIntent);
                        // interval : 다음 알람이 울리기까지의 시간
                    }

                Toast.makeText(FixAlarmActivity.this, "알람 예정 시간 : " + am_pm + " " + hour + "시 " + minute + "분", Toast.LENGTH_SHORT).show();


                user_id = PreferenceManager.getString(getApplicationContext(), "User_Uid");
                url = "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8001/alarms";
                RequestQueue queue = Volley.newRequestQueue(FixAlarmActivity.this);

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
                        Log.e("Fix_request", "fix_request");
                        params.put("id", user_id);
                        params.put("tv_memo", str_memo);
                        params.put("am_pm", am_pm);
                        params.put("tv_repeat", str_repeat);
                        params.put("tv_qt", str_qt);
                        params.put("tv_hour", str_hour);
                        params.put("tv_minute", str_min);
                        params.put("tv_type", str_type);
                        params.put("tv_nq", str_nq);
                        params.put("request_id", REQUEST_ID);
                        params.put("switch","1");
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
                stringRequest.setShouldCache(false);
                queue.add(stringRequest);
                finish();
            }
        });


    }
    public void deleteRequest(String REQUEST_ID) {
        user_id = PreferenceManager.getString(getApplicationContext(), "User_Uid");
        url = "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8001/deletealarms";
        RequestQueue queue = Volley.newRequestQueue(FixAlarmActivity.this);

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
    }

    private int timeSet(int hour) {
        if (hour > 12) {
            hour -= 12;
        }
        return hour;
    }

    private String AM_PM(int hour) {
        if (hour >= 12) {
            am_pm = "오후";
        } else {
            am_pm = "오전";
        }
        return am_pm;
    }

    private String SendCheck(CheckBox cb_eng, CheckBox cb_su, CheckBox cb_sok, CheckBox cb_saja, CheckBox cb_his, CheckBox cb_tong) {

        if (cb_eng.isChecked()) result += cb_eng.getText().toString() + ",";
        if (cb_su.isChecked()) result += cb_su.getText().toString() + ",";
        if (cb_sok.isChecked()) result += cb_sok.getText().toString() + ",";
        if (cb_saja.isChecked()) result += cb_saja.getText().toString() + ",";
        if (cb_his.isChecked()) result += cb_his.getText().toString() + ",";
        if (cb_tong.isChecked()) {
            result = "";
            result += cb_tong.getText().toString() + ",";
        }


        return result;
    }

    private String ToggleCheck(ToggleButton tg_Monday, ToggleButton tg_Tuesday, ToggleButton tg_Wednesday, ToggleButton tg_Thursday, ToggleButton tg_Friday, ToggleButton tg_Saturday, ToggleButton tg_Sunday) {

        if (tg_Monday.isChecked()) pri_tg += tg_Monday.getText().toString() + ",";
        if (tg_Tuesday.isChecked()) pri_tg += tg_Tuesday.getText().toString() + ",";
        if (tg_Wednesday.isChecked()) pri_tg += tg_Wednesday.getText().toString() + ",";
        if (tg_Thursday.isChecked()) pri_tg += tg_Thursday.getText().toString() + ",";
        if (tg_Friday.isChecked()) pri_tg += tg_Friday.getText().toString() + ",";
        if (tg_Saturday.isChecked()) pri_tg += tg_Saturday.getText().toString() + ",";
        if (tg_Sunday.isChecked()) pri_tg += tg_Sunday.getText().toString() + ",";

        return pri_tg;
    }

    private String TypeCheck(RadioButton rb_bell, RadioButton rb_vibration) {
        if (rb_bell.isChecked()) result_type += rb_bell.getText().toString();
        if (rb_vibration.isChecked()) result_type += rb_vibration.getText().toString();

        return result_type;
    }

    private String NQCheck(RadioButton rb_nq1, RadioButton rb_nq2, RadioButton rb_nq3, RadioButton rb_nqno) {
        if (rb_nq1.isChecked()) result_nq += rb_nq1.getText().toString();
        if (rb_nq2.isChecked()) result_nq += rb_nq2.getText().toString();
        if (rb_nq3.isChecked()) result_nq += rb_nq3.getText().toString();
        if (rb_nqno.isChecked()) result_nq += rb_nqno.getText().toString();

        return result_nq;
    }

   /* public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        hourOfDay = int_hour;
        minute = int_minute;
        nHour = hourOfDay;
        nMinute = minute;
    }*/

}