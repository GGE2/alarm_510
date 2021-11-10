package com.example.MainService.VM.fragments.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.alarm.R;

public class Term_Of_Service extends AppCompatActivity {
    ConstraintLayout const_title_term;
    TextView tv_title_term, tv_info_term;
    Button btn_title_term;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_of_service);

        btn_title_term = findViewById(R.id.btn_title_term);
        btn_title_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        const_title_term = findViewById(R.id.const_title_term);
        tv_title_term = findViewById(R.id.tv_title_term);
        tv_info_term = findViewById(R.id.tv_info_term);
    }
}
