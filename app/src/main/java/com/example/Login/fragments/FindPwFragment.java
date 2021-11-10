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

import com.example.Login.activities.ResetPopupActivity;
import com.example.alarm.R;
import com.example.Login.activities.FindAccountActivity;
import com.example.Login.activities.StartPopupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class FindPwFragment extends Fragment

{
    private String userEmail;
    FindAccountActivity faActivity;
    TextView pw_id_check , pw_email_check;
    EditText findpw_id_input,findpw_phoneNumber_input;
    AppCompatButton pw_check_btn;


    public static FindPwFragment newInstance(){

        return new FindPwFragment();
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.find_pw_frag,container,false);

        faActivity = new FindAccountActivity();
        pw_id_check = vg.findViewById(R.id.findpw_id_check_text);
        pw_email_check = vg.findViewById(R.id.findpw_email_check_text);
        pw_check_btn = vg.findViewById(R.id.findpw_btn);

        findpw_id_input = vg.findViewById(R.id.findpw_id_input);
        findpw_phoneNumber_input = vg.findViewById(R.id.findpw_phoneNumber_input);

        pw_check_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String input_id = findpw_id_input.getText().toString();
                String input_phone = findpw_phoneNumber_input.getText().toString();

                //휴대폰번호 child email과 사용자가 입력한 email 주소 확인
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Alarm");
                DatabaseReference ref = rootRef.child("UserAccount").child(input_phone);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        //휴대폰번호 child email과 사용자가 입력한 email 주소가 일치했을 경우 이메일 발송 팝업창 생성
                        if(snapshot.child("userId").exists()){
                            final String value = snapshot.child("userId").getValue().toString();
                            System.out.println("user:"+user);
                            userEmail = value;
                           if(input_id.equals(userEmail)) {
                            FirebaseAuth auth=FirebaseAuth.getInstance();
                            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        check_text_clear(pw_email_check);
                                        openPopup(userEmail);

                                    }


                                }
                            });
                                /*check_text_clear(pw_email_check);
                                openPopup(userEmail);*/
                            }
                            else{
                                pw_email_check.setText("입력하신 정보와 일치하는 계정정보가 없습니다.");
                            }
                        }
                        else{
                            pw_email_check.setText("입력하신 정보와 일치하는 계정정보가 없습니다.");
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                };
                ref.addValueEventListener(eventListener);


               }
        });


        return vg;


    }

    private void openPopup(String email){
        Intent intent = new Intent(getActivity(), ResetPopupActivity.class);
        intent.putExtra("userEmail",email);
        startActivity(intent);
    }



    private void check_text_clear(TextView text){
        text.setText("");
    }






}
