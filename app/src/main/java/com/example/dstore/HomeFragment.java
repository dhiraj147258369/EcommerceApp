package com.example.dstore;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.dstore.DBqueries.lists;
import static com.example.dstore.DBqueries.loadCategoryNames;
import static com.example.dstore.DBqueries.loadFragmentData;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    /////// Main Recycler view//////////
    private HomepageAdapter homepageAdapter;
     private RecyclerView homePageRecView;
    /////// Main Recycler view//////////
    private FirebaseFirestore firebaseFirestore;
    private ImageView noInternetConnectionImg;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
  private Button retryBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore =FirebaseFirestore.getInstance();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        noInternetConnectionImg = view.findViewById(R.id.no_internet_connection);
        homePageRecView = view.findViewById(R.id.testing_rec_view);
        retryBtn = view.findViewById(R.id.retry_btn);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.red),getContext().getResources().getColor(R.color.red),getContext().getResources().getColor(R.color.red));

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homePageRecView.setLayoutManager(testingLayoutManager);

        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(Integer.parseInt(String.valueOf(0)));
            noInternetConnectionImg.setVisibility(View.GONE);
            homePageRecView.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.GONE);


            if (lists.size() == 0){
                loadCategoryNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                loadFragmentData(homePageRecView,getContext(),0,"Home");
            }else{
                homepageAdapter = new HomepageAdapter(lists.get(0));
                homepageAdapter.notifyDataSetChanged();
            }
            homePageRecView.setAdapter(homepageAdapter);
        }else{
            MainActivity.drawer.setDrawerLockMode(Integer.parseInt(String.valueOf(1)));

            Glide.with(this).load(R.drawable.no_internet).into(noInternetConnectionImg);
            noInternetConnectionImg.setVisibility(View.VISIBLE);
        }

        ////////////FAKE LISTS//////////
        List<CategoryModel> categoryModelFAKEList = new ArrayList<CategoryModel>();
        categoryModelFAKEList.add(new CategoryModel("null",""));
        categoryModelFAKEList.add(new CategoryModel("",""));
        categoryModelFAKEList.add(new CategoryModel("",""));
        categoryModelFAKEList.add(new CategoryModel("",""));
        categoryModelFAKEList.add(new CategoryModel("",""));
        categoryModelFAKEList.add(new CategoryModel("",""));

        List<SliderModel> sliderModelFAKEList = new ArrayList<>();
        sliderModelFAKEList.add(new SliderModel("null","#cccccc"));
        sliderModelFAKEList.add(new SliderModel("null","#cccccc"));
        sliderModelFAKEList.add(new SliderModel("null","#cccccc"));
        sliderModelFAKEList.add(new SliderModel("null","#cccccc"));
        sliderModelFAKEList.add(new SliderModel("null","#cccccc"));
        sliderModelFAKEList.add(new SliderModel("null","#cccccc"));

        List<HorizontalProductScrollModel> horizontalProductScrollFAKEModelList = new ArrayList<HorizontalProductScrollModel>();
        horizontalProductScrollFAKEModelList.add(new HorizontalProductScrollModel("","null","","",""));
        horizontalProductScrollFAKEModelList.add(new HorizontalProductScrollModel("","null","","",""));
        horizontalProductScrollFAKEModelList.add(new HorizontalProductScrollModel("","null","","",""));
        horizontalProductScrollFAKEModelList.add(new HorizontalProductScrollModel("","null","","",""));
        horizontalProductScrollFAKEModelList.add(new HorizontalProductScrollModel("","null","","",""));
        horizontalProductScrollFAKEModelList.add(new HorizontalProductScrollModel("","null","","",""));
        horizontalProductScrollFAKEModelList.add(new HorizontalProductScrollModel("","null","","",""));
        horizontalProductScrollFAKEModelList.add(new HorizontalProductScrollModel("","null","","",""));

        homePageModelFakeList.add(new HomePageModel(categoryModelFAKEList,4));
        homePageModelFakeList.add(new HomePageModel(0,sliderModelFAKEList));
        homePageModelFakeList.add(new HomePageModel(1,"","#FFFFFF",""));
        homePageModelFakeList.add(new HomePageModel(2,"","#FFFFFF",horizontalProductScrollFAKEModelList,new ArrayList<>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#FFFFFF",horizontalProductScrollFAKEModelList));

        homepageAdapter = new HomepageAdapter(homePageModelFakeList);
        homePageRecView.setAdapter(homepageAdapter);
        ////////////FAKE LISTS//////////


        /////////SWIPE REFRESH LAYOUT
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
              reloadPage();
            }
        });

        /////////SWIPE REFRESH LAYOUT

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });
        return view;
    }

    private void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();
        lists.clear();
        loadCategoryNames.clear();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(Integer.parseInt(String.valueOf(0)));

            noInternetConnectionImg.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);

            homePageRecView.setAdapter(homepageAdapter);
            homePageRecView.setVisibility(View.VISIBLE);
            homepageAdapter = new HomepageAdapter(homePageModelFakeList);
            loadCategoryNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecView,getContext(),0,"Home");
        }else{
            MainActivity.drawer.setDrawerLockMode(Integer.parseInt(String.valueOf(1)));
            Toast.makeText(getContext(),"No connection",Toast.LENGTH_SHORT).show();
            Glide.with(getContext()).load(R.drawable.no_internet).into(noInternetConnectionImg);
            noInternetConnectionImg.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);

            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        homepageAdapter.notifyDataSetChanged();
    }


    private void extraData(){
        //        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        categoryRecyclerView.setLayoutManager(layoutManager);

//        firebaseFirestore.collection("HOMEPAGE").orderBy("index")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//              if (task.isSuccessful()){
//
//              }
//            }
//        });
//         categoryModelList = new ArrayList<CategoryModel>();
//        categoryModelList.add(new CategoryModel("link","abc"));
//        categoryModelList.add(new CategoryModel("link","sadf"));
//        categoryModelList.add(new CategoryModel("link","dff"));
//        categoryModelList.add(new CategoryModel("link","dfbbr"));
//        categoryModelList.add(new CategoryModel("link","hhf"));
//        categoryModelList.add(new CategoryModel("link","hhf"));
//        categoryModelList.add(new CategoryModel("link","hhf"));
//
//        categoryAdapter = new CategoryAdapter(categoryModelList);
//        categoryRecyclerView.setAdapter(categoryAdapter);
//        categoryAdapter.notifyDataSetChanged();
        /////// Banner//////////
        // bannerSliderViewPager =view.findViewById(R.id.banner_viewpager);

        // sliderModelList = new ArrayList<>();
//        sliderModelList.add(new SliderModel(R.drawable.banner_1,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_2,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_3,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_4,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_1,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_2,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_3,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_4,"#cccccc"));

//        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
//        bannerSliderViewPager.setAdapter(sliderAdapter);
//        bannerSliderViewPager.setClipToPadding(false);
//        bannerSliderViewPager.setPageMargin(20);
//        bannerSliderViewPager.setCurrentItem(currentpage);
//        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//            currentpage = position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                if (state == ViewPager.SCROLL_STATE_IDLE){
//                    pageLooper();
//                }
//            }
//        };
//        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

        // startBannerSlideShow();
//        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                pageLooper();
//                stopBannerSLideShow();
//                if (event.getAction() == MotionEvent.ACTION_UP){
//                    startBannerSlideShow();
//                }
//                return false;
//            }
//        });
        /////// Banner//////////

        /////// STRIP AD//////////
//        stripImage = view.findViewById(R.id.strip_ad_image);
//        stripAdContainer = view.findViewById(R.id.strip_ad_container);
//
//        stripImage.setImageResource(R.drawable.banner_3);
//        stripAdContainer.setBackgroundColor(Color.parseColor("#cccccc"));
        /////// STRIP AD//////////

        /////// Horizontal Product Layout//////////
//        horizontalLayoutTitle = view.findViewById(R.id.horizontal_scrolllayout_title);
//        horizotnalViewAllBtn =   view.findViewById(R.id.h_scroll_viewall_btn);
//        horizontalProductRecView = view.findViewById(R.id.h_product_rec_view);

//        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<HorizontalProductScrollModel>();
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.phone_front,"Redmi 5A","HD Quality","Rs.19,999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.phone_front,"Redmi 5A","HD Quality","Rs.19,999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.phone_front,"Redmi 5A","HD Quality","Rs.19,999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.phone_front,"Redmi 5A","HD Quality","Rs.19,999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.phone_front,"Redmi 5A","HD Quality","Rs.19,999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.phone_front,"Redmi 5A","HD Quality","Rs.19,999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.phone_front,"Redmi 5A","HD Quality","Rs.19,999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.phone_front,"Redmi 5A","HD Quality","Rs.19,999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.phone_front,"Redmi 5A","HD Quality","Rs.19,999/-"));

        //  HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        horizontalProductRecView.setLayoutManager(linearLayoutManager);
//        horizontalProductRecView.setAdapter(horizontalProductScrollAdapter);
//        horizontalProductScrollAdapter.notifyDataSetChanged();
        /////// Horizontal Product Layout//////////


        /////// Banner//////////
//      private void pageLooper(){
//        if (currentpage == sliderModelList.size()-2){
//            currentpage = 2;
//            bannerSliderViewPager.setCurrentItem(currentpage,false);
//        }
//          if (currentpage == 1){
//              currentpage = sliderModelList.size()-3;
//              bannerSliderViewPager.setCurrentItem(currentpage,false);
//          }
//      }
//
//      private void startBannerSlideShow(){
//          Handler handler = new Handler();
//          Runnable update = new Runnable() {
//              @Override
//              public void run() {
//               if (currentpage>=sliderModelList.size()){
//                   currentpage =1;
//               }
//               bannerSliderViewPager.setCurrentItem(currentpage++,true);
//              }
//          };
//
//          timer = new Timer();
//          timer.schedule(new TimerTask() {
//              @Override
//              public void run() {
//                  handler.post(update);
//              }
//          },DELAY_TIME,PERIOD_TIME);
//      }
//
//      private void stopBannerSLideShow(){
//        timer.cancel();
//      }
        /////// Banner//////////


        ////Grid product layout///
//        gridLayoutTitle = view.findViewById(R.id.grid_product_layout_title);
//        gridLayoutViewAllBtn =   view.findViewById(R.id.grid_product_layout_view_all_btn);
        // gridView = view.findViewById(R.id.grid_product_layout_gridview);
        // gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductScrollModelList));
        ////Grid product layout///

        //////////////////
    }





}