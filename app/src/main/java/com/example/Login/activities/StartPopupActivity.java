package com.example.Login.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.R;

public class StartPopupActivity extends AppCompatActivity {

    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.id_popup);
        Intent getData = getIntent();
        String inputEmail=getData.getStringExtra("userEmail");
        text = findViewById(R.id.get_id);
        text.setText(inputEmail);

        int width = (int)(dm.widthPixels*0.9);
        int height = (int)(dm.heightPixels*0.5);
        getWindow().getAttributes().width= width;
        getWindow().getAttributes().height = height;
    }

    public void popupClose(View v) {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }



}
