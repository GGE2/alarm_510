package com.example.MainService.VM.fragments.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.alarm.R;

public class NoticeActivity extends AppCompatActivity {
    TextView tv_title_note, tv_info_note;
    Button btn_title_note;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        btn_title_note = findViewById(R.id.btn_title_note);
        btn_title_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title_note = findViewById(R.id.tv_title_note);
        tv_info_note = findViewById(R.id.tv_info_note);
    }
}