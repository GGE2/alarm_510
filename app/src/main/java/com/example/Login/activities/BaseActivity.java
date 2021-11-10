package com.example.Login.activities;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.example.alarm.R;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    public static ArrayList<Activity> actList = new ArrayList<Activity>();


    public void actFinish() {
        for (int i = 0; i < actList.size(); i++) {
            actList.get(i).finish();
        }

    }


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actList.add(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        actList.remove(this);
    }


}