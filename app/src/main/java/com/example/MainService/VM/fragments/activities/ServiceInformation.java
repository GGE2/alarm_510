package com.example.MainService.VM.fragments.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.alarm.R;

public class ServiceInformation extends AppCompatActivity {
    ConstraintLayout const_title, const_info1, const_info2, const_info3;
    TextView tv_service, tv_term, tv_pri, tv_open;
    Button btn_tittle_serv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service);

        const_title = findViewById(R.id.const_title);
        const_info1 = findViewById(R.id.const_info1);
        const_info2 = findViewById(R.id.const_info2);
        const_info3 = findViewById(R.id.const_info3);

        btn_tittle_serv = findViewById(R.id.btn_title_serv);
        btn_tittle_serv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_service = findViewById(R.id.tv_service);
        tv_term = findViewById(R.id.tv_term);
        tv_pri = findViewById(R.id.tv_pri);
        tv_open = findViewById(R.id.tv_open);


        const_info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ServiceInformation.this, Term_Of_Service.class);
                startActivity(intent1);

            }
        });

        const_info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ServiceInformation.this, Privacy_Policy.class);
                startActivity(intent2);

            }
        });

        const_info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ServiceInformation.this, Opensource_License.class);
                startActivity(intent3);
            }
        });
    }
}
