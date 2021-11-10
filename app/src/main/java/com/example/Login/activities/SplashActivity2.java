package com.example.Login.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.MainService.Alarm.activities.DivideFragmentActivity;
import com.example.alarm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity2 extends Activity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private View decorView;
    private int	uiOption;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //스플래쉬 실행시 하단바 없앰
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );
        setContentView(R.layout.splash_start);
        if(user==null) {
            startLoading();
        }
        else {
            Log.e("UID",user.getUid());
            go_main();
        }


    }



    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity2.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 100);
    }
    private void go_main(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity2.this, DivideFragmentActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }

}
