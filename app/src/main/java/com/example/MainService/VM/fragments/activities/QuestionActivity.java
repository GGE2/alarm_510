package com.example.MainService.VM.fragments.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.alarm.R;

public class QuestionActivity extends AppCompatActivity {
    TextView tv_title_ques;
    EditText et_que1, tv_que2;
    Button btn_title_ques;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);

        btn_title_ques = findViewById(R.id.btn_title_ques);
        btn_title_ques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title_ques = findViewById(R.id.tv_title_ques);
        et_que1 = findViewById(R.id.et_que1);
        tv_que2 = findViewById(R.id.tv_que2);
    }
}