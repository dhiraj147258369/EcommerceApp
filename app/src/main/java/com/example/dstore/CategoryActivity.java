package com.example.dstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static com.example.dstore.DBqueries.lists;
import static com.example.dstore.DBqueries.loadCategoryNames;
import static com.example.dstore.DBqueries.loadFragmentData;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);

        categoryRecView = findViewById(R.id.category_recyclerview);

        HomepageAdapter homepageAdapter;
        List<SliderModel> sliderModelList = new ArrayList<>();

//        sliderModelList.add(new SliderModel(R.drawable.banner_3,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_4,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_1,"#cccccc"));
//
//        sliderModelList.add(new SliderModel(R.drawable.banner_2,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_3,"#cccccc"));
//
//        sliderModelList.add(new SliderModel(R.drawable.banner_4,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_1,"#cccccc"));
//        sliderModelList.add(new SliderModel(R.drawable.banner_2,"#cccccc"));



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

     //   HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);



        //////////////////

        ////////////FAKE LISTS//////////
        List<CategoryModel> categoryModelFAKEList = new ArrayList<CategoryModel>();
        categoryModelFAKEList.add(new CategoryModel("null",""));
        categoryModelFAKEList.add(new CategoryModel("null",""));
        categoryModelFAKEList.add(new CategoryModel("null",""));
        categoryModelFAKEList.add(new CategoryModel("null",""));
        categoryModelFAKEList.add(new CategoryModel("null",""));
        categoryModelFAKEList.add(new CategoryModel("null",""));

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
/////////////////
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(CategoryActivity.this);
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        categoryRecView.setLayoutManager(testingLayoutManager);
        homepageAdapter = new HomepageAdapter(homePageModelFakeList);
        categoryRecView.setAdapter(homepageAdapter);

//        List<HomePageModel> homePageModelList = new ArrayList<>();
//        homePageModelList.add(new HomePageModel(0,sliderModelList));
    //    homePageModelList.add(new HomePageModel(1,R.drawable.banner_3,"#cccccc"));
//        homePageModelList.add(new HomePageModel(2,"Deals of the day",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"HOT DEALS",horizontalProductScrollModelList));


        int listPosition = 0;
        for (int x = 0; x < loadCategoryNames.size() ;x++){
            if (loadCategoryNames.get(x).equals(title.toUpperCase())){
                listPosition = x;
            }
        }
        if (listPosition == 0){
            loadCategoryNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecView,this,loadCategoryNames.size()-1,title);
        }else{
            homepageAdapter = new HomepageAdapter(lists.get(listPosition));
        }
        homepageAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_icon) {
            return true;
        }else if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}