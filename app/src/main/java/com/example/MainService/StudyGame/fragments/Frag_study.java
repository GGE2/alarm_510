package com.example.MainService.StudyGame.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alarm.R;


public class Frag_study extends Fragment {
    private View view;
    ImageButton English, capital, proverb, idiom, history, integrated;
    TextView English_text, capital_text ,proverb_text, idiom_text, history_text, integrate_text;
    String title_text;
    String category_code;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_study,container,false); // 프래그먼트 연결
        English = (ImageButton)view.findViewById(R.id.go_English);
        capital = (ImageButton)view.findViewById(R.id.go_capital);
        proverb = (ImageButton)view.findViewById(R.id.go_proverb);
        idiom = (ImageButton)view.findViewById(R.id.go_idiom);
        history = (ImageButton)view.findViewById(R.id.go_history);
        integrated = (ImageButton)view.findViewById(R.id.go_integrated);
        English_text = (TextView)view.findViewById(R.id.English_text);
        capital_text = (TextView)view.findViewById(R.id.capital_text);
        proverb_text = (TextView)view.findViewById(R.id.proverb_text);
        idiom_text = (TextView)view.findViewById(R.id.idiom_text);
        history_text = (TextView)view.findViewById(R.id.history_text);
        integrate_text = (TextView)view.findViewById(R.id.integrate_text);


        English.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                category_code = "1";
                title_text = English_text.getText().toString();
                Intent i = new Intent(getActivity(), Problem.class);
                i.putExtra("title",title_text);
                i.putExtra("category_code",category_code);
                startActivity(i);

            }
        });

        capital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_code = "2";
                title_text = capital_text.getText().toString();
                Intent i = new Intent(getActivity(),Problem.class);
                i.putExtra("title",title_text);
                i.putExtra("category_code",category_code);
                startActivity(i);
            }
        });

        proverb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_code = "3";
                title_text = proverb_text.getText().toString();
                Intent i = new Intent(getActivity(),Problem.class);
                i.putExtra("title",title_text);
                i.putExtra("category_code",category_code);
                startActivity(i);
            }
        });

        idiom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_code = "4";
                title_text = idiom_text.getText().toString();
                Intent i = new Intent(getActivity(),Problem.class);
                i.putExtra("title",title_text);
                i.putExtra("category_code",category_code);
                startActivity(i);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_code = "5";
                title_text = history_text.getText().toString();
                Intent i = new Intent(getActivity(),Problem.class);
                i.putExtra("title",title_text);
                i.putExtra("category_code",category_code);
                startActivity(i);
            }
        });

        integrated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_code = "6";
                title_text = integrate_text.getText().toString();
                Intent i = new Intent(getActivity(),Problem.class);
                i.putExtra("title",title_text);
                i.putExtra("category_code",category_code);
                startActivity(i);
            }
        });

        return view;
    }
}