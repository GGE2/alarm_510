package com.example.MainService.Alarm.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.R;

public class ShowNotification extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shownotification);
        context = getBaseContext();

        TextView textView = (TextView) findViewById(R.id.txt_notiview);
        textView.setText("Notification replyview");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.cancelNotification(context,0);
    }
}
