package com.example.MainService.VM.fragments.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.alarm.R;

public class Privacy_Policy extends AppCompatActivity {
    ConstraintLayout const_title_pri;
    TextView tv_title_pri, tv_info_pri;
    Button btn_title_pri;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);

        btn_title_pri = findViewById(R.id.btn_title_pri);
        btn_title_pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        const_title_pri = findViewById(R.id.const_title_pri);
        tv_title_pri = findViewById(R.id.tv_title_pri);
        tv_info_pri = findViewById(R.id.tv_info_pri);
    }
}
