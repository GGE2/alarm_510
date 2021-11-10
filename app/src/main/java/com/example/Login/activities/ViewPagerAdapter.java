package com.example.Login.activities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.MainService.Alarm.fragments.Frag_alarm;
import com.example.MainService.Alarm.fragments.Frag_morning;
import com.example.MainService.StudyGame.fragments.Frag_study;
import com.example.MainService.VM.fragments.fragments.Frag_viewmore;
import com.example.MainService.WAN.fragments.Frag_wronganswer_note;

import org.jetbrains.annotations.NotNull;


public class ViewPagerAdapter extends FragmentStateAdapter {

    public int mcount=4;

    public ViewPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        switch (index)
        {
            case 0:
                return new Frag_alarm();
            case 1:
                return new Frag_study();
            case 2:
                return new Frag_wronganswer_note();
            case 3:
                return new Frag_viewmore();
            default:
                return new Frag_alarm();

        }

    }


    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public int getRealPosition(int position){
        return position%mcount;
    }


}
