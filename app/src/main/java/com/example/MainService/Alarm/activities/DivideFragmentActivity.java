package com.example.MainService.Alarm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.Login.activities.ViewPagerAdapter;
import com.example.MainService.Alarm.fragments.HomeFragment;
import com.example.alarm.R;
import com.example.MainService.Alarm.fragments.Frag_alarm;
import com.example.MainService.StudyGame.fragments.Frag_study;
import com.example.MainService.VM.fragments.fragments.Frag_viewmore;
import com.example.MainService.WAN.fragments.Frag_wronganswer_note;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DivideFragmentActivity extends AppCompatActivity {

    public static DivideFragmentActivity divideFragmentActivity;
    ReStartService restartService;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag_alarm frag_alarm;
    private Frag_study frag_study;
    private Frag_wronganswer_note frag_wronganswer_note;
    private Frag_viewmore frag_viewmore;
    Context context;
    HomeFragment homeFragment;

    private ViewPager2 viewPageSetUp;
    private FragmentStateAdapter SetupPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        homeFragment = new HomeFragment();
        viewPageSetUp = findViewById(R.id.viewPager2);
        SetupPagerAdapter = new ViewPagerAdapter(this);

        viewPageSetUp.setAdapter(SetupPagerAdapter); //FragPagerAdapter를 파라머티로 받고 ViewPager2에 전달 받는다.
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL); //방향은 가로로
        viewPageSetUp.setOffscreenPageLimit(4); //페이지 한계 지정 갯수

        divideFragmentActivity = DivideFragmentActivity.this;
        bottomNavigationView = findViewById(R.id.bottomNavi);

        frag_alarm = new Frag_alarm();
        frag_study = new Frag_study();
        frag_wronganswer_note = new Frag_wronganswer_note();
        frag_viewmore = new Frag_viewmore();
        setFrag(0); // 첫 프래그먼트 화면 무엇으로 지정할지 설정

        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin); //페이지끼리 간격
        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset); //페이지 보이는 정도

        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0: {
                        bottomNavigationView.getMenu().findItem(R.id.action_alarm).setChecked(true);
                        break;
                    }
                    case 1: {
                        bottomNavigationView.getMenu().findItem(R.id.action_study).setChecked(true);
                        break;
                    }
                    case 2: {
                        bottomNavigationView.getMenu().findItem(R.id.action_wronganswer_note).setChecked(true);
                        break;
                    }
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.action_viewmore).setChecked(true);
                        break;
                    default:
                        bottomNavigationView.getMenu().findItem(R.id.action_alarm).setChecked(true);
                        break;
                }

            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_alarm:
                        viewPageSetUp.setCurrentItem(0);
                        break;
                    case R.id.action_study:
                        viewPageSetUp.setCurrentItem(1);
                        break;
                    case R.id.action_wronganswer_note:
                        viewPageSetUp.setCurrentItem(2);
                        break;
                    case R.id.action_viewmore:
                        viewPageSetUp.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });



        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,homeFragment).commit();

        initData();
        }
    //뒤로가기 버튼 입력시 종료창 팝업
    @Override
    public void onBackPressed() {
        AlertDialog.Builder exit_alert = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        exit_alert.create();
        exit_alert.setMessage("오일공 앱을 종료하시겠습니까?");
        exit_alert.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        exit_alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        exit_alert.show();
    }



    // 프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0 :
                ft.replace(R.id.main_frame,frag_alarm);
                ft.commit();
                break;
            case 1 :
                ft.replace(R.id.main_frame,frag_study);
                ft.commit();
                break;
            case 2 :
                ft.replace(R.id.main_frame,frag_wronganswer_note);
                ft.commit();
                break;
            case 3 :
                ft.replace(R.id.main_frame,frag_viewmore);
                ft.commit();
                break;
        }
    }

    public void initData(){

        //리스타트 서비스 생성
        restartService = new ReStartService();
        Intent intent = new Intent(this, PersistentService.class);
        intent.putExtra("state", "ALARM_ON");

        try {
            IntentFilter intentFilter = new IntentFilter("com.example.MainService.Alarm.activities.PersistentService");
            //브로드 캐스트에 등록
      registerReceiver(restartService, intentFilter);
            // 서비스 시작
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, PersistentService.class));
            } else {
                context.startService(new Intent(context, PersistentService.class));
            }
        }catch (Exception e){

        }

    }
    protected void onDestroy() {
        super.onDestroy();

        Log.i("MainActivity","onDestroy");
        //브로드 캐스트 해제
        unregisterReceiver(restartService);
    }



}