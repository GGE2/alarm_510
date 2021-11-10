package com.example.MainService.Alarm.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class AlarmReceiver extends BroadcastReceiver{
    private static final String TAG = AlarmReceiver.class.getSimpleName();
    Context mContext;

    PowerManager powerManager;
    private static PowerManager.WakeLock wakeLock;
    SharedPreferences pref;
    String get_state, get_type,get_request, get_repeat;
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        get_state = intent.getExtras().getString("state","");
        get_type = intent.getExtras().getString("tv_type","");
        get_request = intent.getExtras().getString("request_code","");
        get_repeat = intent.getExtras().getString("tv_repeat","");

        Log.e(TAG, "Alarm state : " + get_state);
        Log.e(TAG,"Type : " + get_type);
        Log.e(TAG,"Request : " + get_request);
        Log.e(TAG,"Repeat : " + get_repeat);

            AlarmReceiverChk(context, intent);

            if (get_state.matches(".*" + "ALARM_ON" + ".*")) {
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat weekdayformat = new SimpleDateFormat("EE", Locale.KOREAN);
                String weekday = weekdayformat.format(date);
                Log.e(TAG,"요일? : "+weekday);
                if(get_repeat.contains(weekday)) {
                    Intent start_intent = new Intent(context, SetAlarmActivity.class);
                    start_intent.putExtra("request_code", get_request);
                    start_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(start_intent);
                } else {
                    Log.e(TAG,"xml_요일이 맞지 않습니다.");
                }
            } else {
                Log.e(TAG,"ALARM OFF");
            }
        }

    private void AlarmReceiverChk(final Context context, final Intent intent){
        Log.d(TAG, "Alarm Receiver started!");
        switch (get_state){
            case "ALARM_ON":
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat weekdayformat = new SimpleDateFormat("EE", Locale.KOREAN);
                String weekday = weekdayformat.format(date);
                Log.e(TAG,"요일? : "+weekday);
                if(get_repeat.contains(weekday)) {
                    acquireCPUWakeLock(context, intent);
                    // RingtoneService 서비스 intent 생성
                    Intent serviceIntent = new Intent(context, RingtoneService.class);
                    serviceIntent.putExtra("state", get_state);
                    serviceIntent.putExtra("tv_type", get_type);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        context.startForegroundService(serviceIntent);
                    } else {
                        context.startService(serviceIntent);
                    }
                } else {
                    Log.e(TAG,"service_요일이 맞지 않습니다.");
                }
                break;
            case "ALARM_OFF": // stopService 가 동작하지 않아서 startService 로 처리하고 나서....
                releaseCpuLock();
                Intent stopIntent = new Intent(context, RingtoneService.class);
                stopIntent.putExtra("state", get_state);
                stopIntent.putExtra("tv_type", get_type);
                Log.e(TAG,"알람 취소 : "+get_state);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    context.startForegroundService(stopIntent);
//                    context.stopService(stopIntent);

                } else {
                    context.startService(stopIntent);
//                    context.stopService(stopIntent);
                }
                break;
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private void acquireCPUWakeLock(Context context, Intent intent) {
        // 잠든 화면 깨우기
        if (wakeLock != null) {
            return;
        }
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "WAKELOCK");
        wakeLock.acquire();
        Log.e("PushWakeLock", "Acquire cpu WakeLock = " + wakeLock);
    }

    private void releaseCpuLock() {
        Log.e("PushWakeLock", "Releasing cpu WakeLock = " + wakeLock);

        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}
