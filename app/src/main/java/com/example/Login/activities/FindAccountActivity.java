package com.example.Login.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.Login.fragments.FindIdFragment;
import com.example.Login.fragments.FindPwFragment;
import com.example.alarm.R;



public class FindAccountActivity extends AppCompatActivity {

    ImageButton back_page;
    AppCompatButton id_btn,pw_btn;
    public static FindAccountActivity findAccountActivity;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.find_id);

        findAccountActivity = FindAccountActivity.this;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragView, FindIdFragment.newInstance()).commit();


        back_page = findViewById(R.id.fi_back);
        id_btn = findViewById(R.id.fi_id_btn);
        pw_btn = findViewById(R.id.fi_pw_btn);

        //FragmentView(1);
        changeWhiteColor(id_btn);
        changeGrayColor(pw_btn);


        back_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //FragmentView(1);
                replaceFragment(FindIdFragment.newInstance());
                changeWhiteColor(id_btn);
                changeGrayColor(pw_btn);
            }
        });

        pw_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //FragmentView(2);
                replaceFragment(FindPwFragment.newInstance());
                changeWhiteColor(pw_btn);
                changeGrayColor(id_btn);
            }
        });


    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragView, fragment).commit();
    }



    public void changeGrayColor(AppCompatButton btn){
        btn.setBackground(getResources().getDrawable(R.drawable.round_box_gray2));
        btn.setTextColor(getResources().getColor(R.color.text_black_gray));
    }
    public void changeWhiteColor(AppCompatButton btn){
        btn.setBackground(getResources().getDrawable(R.drawable.round_box_white2));
        btn.setTextColor(getResources().getColor(R.color.main_background));
    }


    //화면 밖 클릭시 키보드 종료
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();

        if(v != null && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) && v instanceof EditText && !v.getClass().getName().startsWith("android.webkit.")){
            int[] scrcoords = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + v.getLeft() - scrcoords[0];
            float y = event.getRawY() + v.getTop() - scrcoords[1];

            if(x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(event);
    }
}
