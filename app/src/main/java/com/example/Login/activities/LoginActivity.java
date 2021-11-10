package com.example.Login.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Login.etc.PreferenceManager;
import com.example.alarm.R;
import com.example.MainService.Alarm.activities.DivideFragmentActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends MClass {

    private static final String TAG = LoginActivity.class.getName();

    private int auto_radio_check = -1;
    private int save_radio_check = -1;
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증
    private DatabaseReference mFirebaseRef;//실시간 데이터베이스
    private Context context;
    private ISessionCallback mSessionCallback;
    private ImageView iv;
    public EditText input_id , input_pw;
    TextView find_id,signup,check_login_text;
    ImageButton kakao_btn;
    RadioButton auto_radio, save_radio;
    AppCompatButton login_bt;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        //카카오 로그인 클릭시
        mSessionCallback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                    //로그인
                UserManagement.getInstance().me(new MeV2ResponseCallback() {


                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        // 세션이 닫힘
                        Log.e("Login SessionClosed","Login Sucess");
                        Toast.makeText(context, "세션이 닫힘  다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("Login Failure","Login Sucess");
                        //로그인 실패
                        Toast.makeText(context, "로그인 도중 오류발생", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        progressON(LoginActivity.this,"로그인중입니다 잠시만 기다려주세요");
                        String accessToken = Session.getCurrentSession().getAccessToken();
                        getFirebaseJwt(accessToken).continueWithTask(new Continuation<String, Task<AuthResult>>() {
                            @Override
                            public Task<AuthResult> then(@NonNull Task<String> task) throws Exception {
                                String firebaseToken = task.getResult();
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                return auth.signInWithCustomToken(firebaseToken);
                            }
                        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent access_login = new Intent(LoginActivity.this, DivideFragmentActivity.class);
                                    PreferenceManager.setString(context,"token","0");
                                    startActivity(access_login);
                                    progressOFF();
                                } else {
                                    progressOFF();
                                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                                        @Override
                                        public void onCompleteLogout() {
                                            Toast.makeText(getApplicationContext(), "카카오계정 로그인에 실패하였습니다", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    if (task.getException() != null) {
                                        progressOFF();
                                    }
                                }
                            }
                        });


                    }
                });
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                if (exception != null) {
                    Log.e(TAG, exception.toString());
                }
            }
        };

        Session.getCurrentSession().addCallback(mSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseRef = FirebaseDatabase.getInstance().getReference("Alarm");
        find_id = (TextView)findViewById(R.id.login_pw_text);
        signup = (TextView)findViewById(R.id.signUp_text);
        check_login_text = (TextView)findViewById(R.id.check_login_text);
        input_id = (EditText)findViewById(R.id.id_input);
        input_pw = (EditText)findViewById(R.id.pw_input);
        login_bt = (AppCompatButton)findViewById(R.id.login_btn);
        auto_radio = (RadioButton)findViewById(R.id.auto_login_radio);
        save_radio = (RadioButton)findViewById(R.id.save_id_radio);




        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressON(LoginActivity.this,"로그인중입니다 잠시만 기다려주세요");
                String input_emailid=input_id.getText().toString();
                String input_namepw = input_pw.getText().toString();

                if(input_emailid.equals("")||input_namepw.equals("")) {
                    check_login_text.setText("아이디와 비밀번호를 입력해주세요");
                    progressOFF();
                }
                else {
                    mFirebaseAuth.signInWithEmailAndPassword(input_emailid, input_namepw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //User UID 값
                                PreferenceManager.setString(context,"User_Uid",user.getUid());
                                PreferenceManager.setString(context,"token","1");
                                Intent access_login = new Intent(LoginActivity.this, DivideFragmentActivity.class);
                                startActivity(access_login);
                                textClear(input_id, input_pw, check_login_text);
                                progressOFF();
                            } else {
                                check_login_text.setText("아이디 또는 비밀번호를 잘못 입력하셨습니다");
                                progressOFF();
                            }

                        }

                    });
                }

            }
        });


        auto_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_auto_check(auto_radio);
            }
        });
        save_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_save_check(save_radio);
            }
        });


        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findIdIntent = new Intent(LoginActivity.this, FindAccountActivity.class);
                startActivity(findIdIntent);
            }
        });



        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, SignUp2Activity.class);
                startActivity(signupIntent);

            }
        });



    }

    //카카오로부터 임시토큰 발급후 서버로 이동
    private Task<String> getFirebaseJwt(final String kakaoAccessToken) {
        final TaskCompletionSource<String> source = new TaskCompletionSource<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.validation_server_domain)+"/verifyToken";
        HashMap<String, String> validationObject = new HashMap<>();
        validationObject.put("token", kakaoAccessToken);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(validationObject), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String firebaseToken = response.getString("firebase_token");
                    source.setResult(firebaseToken);
                } catch (Exception e) {
                    source.setException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                source.setException(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", kakaoAccessToken);
                return params;
            }
        };

        queue.add(request);
        return source.getTask();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data))
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mSessionCallback);
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


    //자동 로그인 버튼 유효성 검사
    private void radio_auto_check(RadioButton rb){

        if(auto_radio_check==-1){
            rb.setChecked(true);
            rb.setTextColor(getResources().getColor(R.color.main_background));
            auto_radio_check=1;
        }
        else{
            rb.setChecked(false);
            rb.setTextColor(getResources().getColor(R.color.text_gray));
            auto_radio_check=-1;
        }

    }
    //아이디 저장 버튼 유효성 검사
    private void radio_save_check(RadioButton rb){

        if(save_radio_check==-1){
            rb.setChecked(true);
            rb.setTextColor(getResources().getColor(R.color.main_background));
            save_radio_check=1;
        }
        else{
            rb.setChecked(false);
            rb.setTextColor(getResources().getColor(R.color.text_gray));
            save_radio_check=-1;
        }

    }
    private void textClear(EditText id,EditText pw,TextView check){
        id.setText("");
        pw.setText("");
        check.setText("");


    }






}