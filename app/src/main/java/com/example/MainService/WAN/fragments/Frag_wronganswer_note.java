package com.example.MainService.WAN.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.alarm.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;


public class Frag_wronganswer_note extends Fragment {

    Fragment frag_english, frag_capital, frag_proverb, frag_idiom, frag_history, frag_all;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wronganswer_note, container, false); // 프래그먼트 연결
        frag_english = new Wrong_english();
        frag_capital = new Wrong_capital();
        frag_proverb = new Wrong_proverb();
        frag_idiom = new Wrong_idiom();
        frag_history = new Wrong_history();
        frag_all = new Wrong_all();

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frame, frag_all).commit();

        TabLayout tabs = (TabLayout)view.findViewById(R.id.tabs);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0){

                    selected = frag_all;

                }else if (position == 1){

                    selected = frag_english;

                }else if (position == 2){

                    selected = frag_capital;

                }else if (position == 3){

                    selected = frag_proverb;
                }else if (position == 4){

                    selected = frag_idiom;
                }else if (position == 5){

                    selected = frag_history;
                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}
