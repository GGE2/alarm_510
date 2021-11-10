package com.example.Login.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.alarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class SignUpActivity extends BaseActivity {

    private int overlap_address = 1;
    private int id_check = -1;
    private int pw_check = -1;
    private int pw_finalcheck = -1;
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증
    private DatabaseReference mFirebaseRef; //실시간 데이터베이스

    private String User_Name, User_PhoneNumber, User_Birth;


    AppCompatButton fin_btn, pre_btn;
    EditText signup_id_input, signup_pw_input, signup_pw_check_input;
    TextView id, pw, pw_ck;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseRef = FirebaseDatabase.getInstance().getReference("Alarm");

        fin_btn = findViewById(R.id.signup_next_btn);
        pre_btn = findViewById(R.id.signup_pre_btn);

        signup_id_input = findViewById(R.id.signup_id_input);
        signup_pw_input = findViewById(R.id.signup_pw_input);
        signup_pw_check_input = findViewById(R.id.signup_pw_check_input);

        id = findViewById(R.id.signup_login_check_text);
        pw = findViewById(R.id.signup_pwc_check_text);
        pw_ck = findViewById(R.id.signup_pw_check_check_text);


        fin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                User_Name = intent.getStringExtra("userName");
                User_PhoneNumber = intent.getStringExtra("userPhoneNumber");
                User_Birth = intent.getStringExtra("userBirth");
                String User_ID = signup_id_input.getText().toString();
                String User_PW = signup_pw_input.getText().toString();
                String User_PW_CHECK = signup_pw_check_input.getText().toString();

                if (check_input_account(User_ID, User_PW, User_PW_CHECK)) {
                    checkAnswerSubmission(new FirebaseCallBack<Boolean>() {
                        @Override
                        public void callback(Boolean data) {
                            if (data) {
                                //Firebase Auth 진행
                                mFirebaseAuth.createUserWithEmailAndPassword(User_ID, User_PW).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                            UserAccount account = new UserAccount();
                                            account.setIdToken(firebaseUser.getUid());
                                            account.setUserId(firebaseUser.getEmail());
                                            account.setUserPw(User_PW);
                                            account.setUserName(User_Name);
                                            account.setUserBirth(User_Birth);
                                            account.setUserPhoneNumber(User_PhoneNumber);
                                            //database에 삽입
                                            mFirebaseRef.child("UserAccount").child(account.getUserPhoneNumber()).setValue(account);
                                            actFinish();     //SignupActivity , Siggup2Activity 종료
                                        } else {
                                            Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                id.setText("이미 가입된 이메일 주소입니다.");
                                pw.setText("");
                                pw_ck.setText("");
                            }
                        }
                    });

                }
            }
        });

        //SignUp2Activity로 이동
        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private interface SimpleCallback {
        void callback(Object data);
    }


    private interface FirebaseCallBack<T> {
        void callback(T data);
    }

    private void checkAnswerSubmission(@NonNull FirebaseCallBack<Boolean> finishedCallBack) {
        DatabaseReference answerDatabase = FirebaseDatabase.getInstance().getReference("Alarm").child("UserAccount");
        answerDatabase.orderByChild("userId").equalTo(signup_id_input.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int check = 0;
                if (snapshot.exists()) {
                    check++;
                }
                if (check == 0)
                    finishedCallBack.callback(true);
                else
                    finishedCallBack.callback(false);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private boolean check_input_account(String email, String password, String pw_check) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!pattern.matcher(email).matches()) {
            id.setText("유효하지 않은 이메일 형식입니다");
            pw.setText("");
            pw_ck.setText("");
            return false;
        } else if (password.equals("")) {
            id.setText("");
            pw_ck.setText("");
            pw.setText("비밀번호를 입력해주세요");
            return false;
        } else if (pw_check.equals("")) {
            id.setText("");
            pw.setText("");
            pw_ck.setText("비밀번호 확인을 입력해주세요");
            return false;
        } else if (password.length() < 6) {
            id.setText("");
            pw.setText("비밀번호를 6자리 이상으로 입력해주세요.");
            pw_ck.setText("");
            return false;
        } else if (pattern.matcher(email).matches() && !(password.equals("")) && !(pw_check.equals("")) && !(password.equals(pw_check))) {
            Log.e("pattern", "4");
            id.setText("");
            pw.setText("");
            pw_ck.setText("비밀번호가 일치하지 않습니다");
            return false;
        } else
            return true;

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
    private void check_text_clear(TextView text){
        text.setText("");

    }
    private void all_text_clear(){
        id.setText("");
        pw.setText("");
        pw_ck.setText("");
    }

    private boolean signup_check_id(EditText text){
            if(text.getText().toString().equals(""))
                return false;
            else
                return true;
    }

    private boolean signup_check_finalpw(EditText text1,EditText text2){
        if(text1.getText().toString().equals(text2.getText().toString()))
            return true;
        else
            return false;
    }


}
