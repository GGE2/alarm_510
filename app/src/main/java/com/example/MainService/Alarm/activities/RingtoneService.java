package com.example.MainService.Alarm.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.alarm.R;

public class RingtoneService extends Service {
    private static final String TAG = RingtoneService.class.getSimpleName();
    MediaPlayer mediaPlayer;
    String getState, getType;
    Vibrator vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서) 데이터를 주고받을 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand startID === "+startId); // 계속 증가되는 값
        getState = intent.getExtras().getString("state");
        getType = intent.getExtras().getString("tv_type");
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        Log.e("TAG","onStartCommand getState : " + getState);
        Log.e("TAG","and type : " + getType);

        switch (getState) {
            case "ALARM_ON":
                if(getType.matches(".*" + "벨" + ".*")) {
                    NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                    notificationHelper.createNotification("알람시작", "알람음이 재생됩니다.");
                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer.create(this, R.raw.ouu);
                        mediaPlayer.start();

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                Log.e(TAG, "mediaPlayer Completed!");
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.release();
                            }
                        });
                    }
                    break;
                } else if (getType.matches(".*" + "진동" + ".*")){
                    NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                    notificationHelper.createNotification("알람시작", "알람음이 재생됩니다.");

                    Log.e("Noti","vibrate");

                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                        vibrator.vibrate(VibrationEffect.createWaveform(new long[]{3000, 1000, 3000, 1000}, new int[]{100, 100, 100, 100}, 0));
                        break;
                    }
                    else {
                        vibrator.vibrate(new long[]{3000, 1000, 3000, 1000}, 0);
                        break;
                    }
                }
            case "ALARM_OFF":
                Log.e(TAG,"state"+getState);
                Log.e(TAG, "onStartCommand Stoped!");

                if(getType.matches(".*" + "벨" + ".*")) {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying() == true) {
                            mediaPlayer.stop();
                            mediaPlayer.release(); // 자원 반환
                            mediaPlayer = null;
                        }
                    }
                    stopSelf();
                    break;
                } if (getType.matches(".*" + "진동" + ".*")) {
                    vibrator.cancel();
                    Log.e(TAG,"vib is canceled");
                break;
                }
                default:
                        break;

        }
        return START_NOT_STICKY;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.i(TAG, "RingtoneService Started");
//        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
//        notificationHelper.createNotification("알람시작", "알람음이 재생됩니다.");
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        startForegroundService();

    }

    void startForegroundService() {

        Intent notificationIntent = new Intent(this, SetAlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_shownotification);

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "service_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "smart_channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
//                .setContent(remoteViews)
                .setContentIntent(pendingIntent);

        startForeground(1, builder.build());
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Action Service Ended");
        super.onDestroy();
        stopForeground(true);
    }
}
