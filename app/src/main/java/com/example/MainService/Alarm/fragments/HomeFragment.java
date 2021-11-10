package com.example.MainService.Alarm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Login.activities.ViewPagerAdapter;
import com.example.alarm.R;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    private ViewGroup viewGroup;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_fragment, container, false);
        //setInit();

        return viewGroup;
    }

    private void setInit() { //뷰페이저2 실행 메서드

         //setup infinity scroll viewpager
        ViewPager2 viewPageSetUp = (ViewPager2)viewGroup.findViewById(R.id.viewPager2); //여기서 뷰페이저를 참조한다.
        ViewPagerAdapter SetupPagerAdapter = new ViewPagerAdapter(getActivity()); //프래그먼트에서는 getActivity로 참조하고, 액티비티에서는 this를 사용해주세요.
        viewPageSetUp.setAdapter(SetupPagerAdapter); //FragPagerAdapter를 파라머티로 받고 ViewPager2에 전달 받는다.
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL); //방향은 가로로
        viewPageSetUp.setOffscreenPageLimit(4); //페이지 한계 지정 갯수

        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin); //페이지끼리 간격
        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset); //페이지 보이는 정도

        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
    }


}


