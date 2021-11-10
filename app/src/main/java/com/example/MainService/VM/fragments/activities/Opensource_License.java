package com.example.MainService.VM.fragments.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.alarm.R;

public class Opensource_License extends AppCompatActivity {
    ConstraintLayout const_title_open;
    TextView tv_title_open, tv_info_open;
    Button btn_title_open;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opensource_license);

        btn_title_open = findViewById(R.id.btn_title_open);
        btn_title_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        const_title_open = findViewById(R.id.const_title_open);
        tv_title_open = findViewById(R.id.tv_title_open);
        tv_info_open = findViewById(R.id.tv_info_open);
    }
}
