package com.example.Login.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.alarm.R;
import com.example.Login.activities.ResetPopupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ResetPwFragment extends Fragment
{


    public static ResetPwFragment newInstance(){
        return new ResetPwFragment();
    }


    TextView pw_check;
    EditText pw1,pw2;
    AppCompatButton reset_btn;


    

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.reset_pw,container,false);


        pw_check = vg.findViewById(R.id.reset_pw_check_text);
        pw1 = vg.findViewById(R.id.reset_pw_input1);
        pw2 = vg.findViewById(R.id.reset_pw_input2);
        reset_btn = vg.findViewById(R.id.reset_btn);


        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Alarm");
                String newPassword1 = pw1.getText().toString();
                String newPassword2 = pw2.getText().toString();
                Map<String,Object> taskMap = new HashMap<String,Object>();
                taskMap.put("userPw",newPassword1);

                if(signup_check_finalpw(pw1,pw2)){
                            ref.child("UserAccount").child("01023343106").updateChildren(taskMap);
                }
                else{
                    pw_check.setText("비밀번호가 일치하지 않습니다");
                }


            }
        });



        return vg;



    }

    private boolean signup_check_finalpw(EditText text1,EditText text2){
        if(text1.getText().toString().equals(text2.getText().toString()))
            return true;
        else
            return false;
    }
    private void openPopup(){
        Intent intent = new Intent(getActivity(), ResetPopupActivity.class);
        startActivity(intent);
    }
    private void check_text_clear(TextView text){
        text.setText("");

    }



}
