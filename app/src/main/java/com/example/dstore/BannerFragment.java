package com.example.dstore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerFragment extends Fragment {


    public BannerFragment() {
        // Required empty public constructor
    }
    private ViewPager bannerSliderViewPager;
    private List<SliderModel> sliderModelList;
   private int currentpage = 2;
  private Timer timer;
  final private long DELAY_TIME = 3000;
  final private long PERIOD_TIME = 3000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        bannerSliderViewPager =view.findViewById(R.id.banner_viewpager);

        sliderModelList = new ArrayList<>();

//        sliderModelList.add(new SliderModel(R.drawable.banner_3,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_4,"#cccccc"));
//
//        sliderModelList.add(new SliderModel(R.drawable.banner_1,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_2,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_3,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_4,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_1,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_2,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_3,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_4,"#cccccc"));
//
//        sliderModelList.add(new SliderModel(R.drawable.banner_1,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_2,"#cccccc"));

//        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
//        bannerSliderViewPager.setAdapter(sliderAdapter);
//        bannerSliderViewPager.setClipToPadding(false);
//        bannerSliderViewPager.setPageMargin(20);
//        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//              currentpage = position;
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//               if (state == ViewPager.SCROLL_STATE_IDLE){
//                   pageLooper();
//               }
//            }
//        };
//        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

      //  startBannerSlideShow();
//
//        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                pageLooper();
//                stopBannerSlideShow();
//                if (event.getAction() == MotionEvent.ACTION_UP){
//                    startBannerSlideShow();
//                }
//                return false;
//            }
//        });
        return view;
    }

    private void pageLooper(){
        if (currentpage == sliderModelList.size()-2){
            currentpage = 2;
            bannerSliderViewPager.setCurrentItem(currentpage,false);
        }
        if (currentpage == 1){
            currentpage =sliderModelList.size() -3 ;
            bannerSliderViewPager.setCurrentItem(currentpage,false);
        }
    }

    private void startBannerSlideShow(){
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentpage >= sliderModelList.size()){
                    currentpage = 1;
                }
                bannerSliderViewPager.setCurrentItem(currentpage++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },DELAY_TIME,PERIOD_TIME);
    }

    private void stopBannerSlideShow(){
        timer.cancel();
    }
}