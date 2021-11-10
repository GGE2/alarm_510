package com.example.MainService.VM.fragments.fragments;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;

        import android.os.Bundle;
        import android.os.Environment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AlertDialog;
        import androidx.fragment.app.Fragment;

        import com.example.Login.activities.LoginActivity;
        import com.example.MainService.Alarm.activities.AddAlarmActivity;

        import com.example.MainService.Alarm.activities.DivideFragmentActivity;
        import com.example.MainService.Alarm.activities.SetAlarmActivity;
        import com.example.MainService.Alarm.fragments.Frag_alarm;
        import com.example.MainService.VM.fragments.activities.NoticeActivity;
        import com.example.MainService.VM.fragments.activities.QuestionActivity;
        import com.example.MainService.VM.fragments.activities.ServiceInformation;
        import com.example.alarm.R;
        import com.google.firebase.auth.FirebaseAuth;
        import com.kakao.auth.ISessionCallback;
        import com.kakao.auth.Session;
        import com.kakao.usermgmt.UserManagement;
        import com.kakao.usermgmt.callback.LogoutResponseCallback;


public class Frag_viewmore extends Fragment {
    private View view;
    private FirebaseAuth mAuth;
    private Button btn_logout, btn_setting, btn_question, btn_notice, btn_information, btn_test;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_viewmore,container,false); // 프래그먼트 연결
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_setting = view.findViewById(R.id.btn_setting);
        btn_question = view.findViewById(R.id.btn_question);
        btn_notice = view.findViewById(R.id.btn_notice);
        btn_information = view.findViewById(R.id.btn_information);
        SharedPreferences pref = this.getActivity().getSharedPreferences("User_Token", Context.MODE_PRIVATE);
        String user_token = pref.getString("token","");
        btn_test = view.findViewById(R.id.btn_test);

        mAuth = FirebaseAuth.getInstance();

        //로그아웃 버튼 클릭시 dialog 생성
        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("token",user_token);
                AlertDialog.Builder exit_alert = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
                exit_alert.create();
                exit_alert.setMessage("로그아웃 하시겠습니까?");
                exit_alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(user_token.equals("1")) {
                            pref.edit().clear();
                            logout();
                            ((DivideFragmentActivity) getActivity()).finish();
                        }
                        else{
                            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    pref.edit().clear();
                                    logout();
                                    ((DivideFragmentActivity) getActivity()).finish();
                                }
                            });
                        }
                    }
                });

                exit_alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                exit_alert.show();

            }
        });


//        btn_setting.setOnClickListener(new View.OnClickListener() {}

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetAlarmActivity.class);
                startActivity(intent);
            }
        });

        btn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentq = new Intent(getActivity(), QuestionActivity.class);
                startActivity(intentq);
            }
        });

        btn_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentn = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intentn);
            }
        });

        btn_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenti = new Intent(getActivity(), ServiceInformation.class);
                startActivity(intenti);
            }
        });


        return view;
    }


    //로그아웃
    private void logout(){
        FirebaseAuth.getInstance().signOut();
    }


}