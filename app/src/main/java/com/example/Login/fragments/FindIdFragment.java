package com.example.Login.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.alarm.R;
import com.example.Login.activities.StartPopupActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.CLIPBOARD_SERVICE;

public class FindIdFragment extends Fragment
{
    private String userEmail="";
    TextView id_frag_check;
    EditText id_frag_inputPhoneNumber;
    AppCompatButton id_frag_btn;

    public static FindIdFragment newInstance(){

        return new FindIdFragment();
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.find_id_frag,container,false);


          id_frag_check = vg.findViewById(R.id.findid_email_check_text);
          id_frag_btn = vg.findViewById(R.id.findid_btn);
          id_frag_inputPhoneNumber = vg.findViewById(R.id.findid_phoneNumber_input);


          id_frag_btn.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View v) {
                  String input_userPhoneNumber = id_frag_inputPhoneNumber.getText().toString();
                  final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Alarm");
                  DatabaseReference ref = rootRef.child("UserAccount").child(input_userPhoneNumber);
                  ValueEventListener eventListener = new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                          if(snapshot.child("userId").exists()){
                              final String value = snapshot.child("userId").getValue().toString();
                                userEmail = value;
                                openPopup(userEmail);
                                id_frag_check.setText("");
                                setClipBoardManager(userEmail);
                          }
                          else{
                              id_frag_check.setText("입력하신 번호로 가입된 계정이 없습니다.");
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


    //사용자 이메일 유효성 검사
    private boolean check_user_email(String email){

        if(email.equals("admin@admin.com"))
            return true;

        return false;

    }
    private void check_text_clear(TextView text){
        text.setText("");

    }

    private void openPopup(String userEmail){

        String email = userEmail;
        Intent intent = new Intent(getActivity(),StartPopupActivity.class);
        intent.putExtra("userEmail",email);
        startActivity(intent);

    }
    //아이디 찾기 후 아이디 찾은 아이디 클립보드에 저장
    private void setClipBoardManager(String email){
        String userEmail = email;
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", userEmail);
        clipboard.setPrimaryClip(clip);

    }


}
