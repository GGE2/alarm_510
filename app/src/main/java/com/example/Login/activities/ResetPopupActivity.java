package com.example.Login.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.alarm.R;

import static com.example.Login.activities.FindAccountActivity.findAccountActivity;

public class ResetPopupActivity extends AppCompatActivity {


    AppCompatButton reset_btn;
    TextView text;
    FindAccountActivity fa = (FindAccountActivity) findAccountActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.reset_popup);

        text = findViewById(R.id.reset_text_popup);
        reset_btn = findViewById(R.id.reset_popup_btn);
        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");
        String str = email+"\n위 주소로 비밀번호 재설정\n이메일을 발송하였습니다.";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new StyleSpan(Typeface.BOLD),0,email.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(ssb);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                fa.finish();
            }
        });



    }

    public void popupClose(View v) {
        finish();
        fa.finish();

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
