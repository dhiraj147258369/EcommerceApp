package com.example.dstore;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomepageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastPosition = -1;
    public HomepageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool =new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
       switch (homePageModelList.get(position).getType()){
           case 0:
               return HomePageModel.BANNER_SLIDER;

           case 1:
               return HomePageModel.STRIP_AD_BANNER;

           case 2:
               return HomePageModel.HORIZONAL_PRODUCT_VIEW;

           case 3:
               return HomePageModel.GRID_PRODUCT_VIEW;
           case 4:
               return HomePageModel.CATEGORY_VIEW;
           default:
               return -1;
       }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case HomePageModel.BANNER_SLIDER:
                View bannerSLiderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout,parent,false);
              return new BannerSliderViewHolder(bannerSLiderView);

            case HomePageModel.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout,parent,false);
                return new StripAdBannerViewHolder(stripAdView);

            case HomePageModel.HORIZONAL_PRODUCT_VIEW:
                View horizontal_product_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout,parent,false);
                return new HorizontalProductViewHolder(horizontal_product_view);

            case HomePageModel.GRID_PRODUCT_VIEW:
                View grid_product_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout,parent,false);
                return new GridProductViewHolder(grid_product_view);

            case HomePageModel.CATEGORY_VIEW:
                View category_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout,parent,false);
                return new CategoryViewHolder(category_view);

            default:
            return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
     switch (homePageModelList.get(position).getType()){
         case HomePageModel.BANNER_SLIDER:
             List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
             ((BannerSliderViewHolder)holder).setBannerSliderViewPager(sliderModelList);
             break;

             case HomePageModel.STRIP_AD_BANNER:
                String resource = homePageModelList.get(position).getResource();
                String color = homePageModelList.get(position).getBackgroundColor();
                String idd = homePageModelList.get(position).getIdd();
                 ((StripAdBannerViewHolder)holder).setStripAdImage(resource,color,idd);
              break;

         case HomePageModel.HORIZONAL_PRODUCT_VIEW:
             List<WishListModel> wishListModelList = homePageModelList.get(position).getViewAllProductList();
             String h_color =  homePageModelList.get(position).getBackgroundColor();
             String h_title =  homePageModelList.get(position).getTitle();
               List<HorizontalProductScrollModel> h_horizontalProductScrollModels =  homePageModelList.get(position).getHorizontalProductScrollModelList();
             ((HorizontalProductViewHolder)holder).setHorizontalProductLayout(h_horizontalProductScrollModels,h_title,h_color,wishListModelList);
             break;

         case HomePageModel.GRID_PRODUCT_VIEW:
             String grid_color =  homePageModelList.get(position).getBackgroundColor();
             String title =  homePageModelList.get(position).getTitle();
             List<HorizontalProductScrollModel> horizontalProductScrollModels =  homePageModelList.get(position).getHorizontalProductScrollModelList();
             ((GridProductViewHolder)holder).setGridProductLayout(horizontalProductScrollModels,title,grid_color);
             break;

             case HomePageModel.CATEGORY_VIEW:
                 List<CategoryModel> categoryModelList = homePageModelList.get(position).getCategoryModelList();
                 ((CategoryViewHolder)holder).setCategory(categoryModelList);
                 break;
         default:
             return;
     }
     if (lastPosition < position){
         Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
         holder.itemView.setAnimation(animation);
         lastPosition = position;
     }

    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        private ViewPager bannerSliderViewPager;
        private int currentpage;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private List<SliderModel> arrangedList;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerSliderViewPager = itemView.findViewById(R.id.banner_viewpager);
        }

        private void setBannerSliderViewPager(final List<SliderModel> sliderModelList) {
            currentpage = 2;
            if (timer != null){
                timer.cancel();
            }
            arrangedList = new ArrayList<>();
            for (int x=0;x<sliderModelList.size();x++){
                arrangedList.add(x,sliderModelList.get(x));
            }
            arrangedList.add(0,sliderModelList.get(sliderModelList.size()-2));
            arrangedList.add(1,sliderModelList.get(sliderModelList.size()-1));
            arrangedList.add(sliderModelList.get(0));
            arrangedList.add(sliderModelList.get(1));

            SliderAdapter sliderAdapter = new SliderAdapter(arrangedList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);
        //    bannerSliderViewPager.setCurrentItem(currentpage);
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentpage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(arrangedList);
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

            startBannerSlideShow(arrangedList);
            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLooper(arrangedList);
                    stopBannerSLideShow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSlideShow(arrangedList);
                    }
                    return false;
                }
            });
        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (currentpage == sliderModelList.size() - 2) {
                currentpage = 2;
                bannerSliderViewPager.setCurrentItem(currentpage, false);
            }
            if (currentpage == 1) {
                currentpage = sliderModelList.size() - 3;
                bannerSliderViewPager.setCurrentItem(currentpage, false);
            }
        }

        private void startBannerSlideShow(final List<SliderModel> sliderModelList) {
            Handler handler = new Handler();
            Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentpage >= sliderModelList.size()) {
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
            }, DELAY_TIME, PERIOD_TIME);
        }

        private void stopBannerSLideShow() {
            timer.cancel();
        }
    }
    public class StripAdBannerViewHolder extends RecyclerView.ViewHolder{

        private ImageView stripImage;
        private RelativeLayout stripAdContainer;

        public StripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            stripImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);

        }

        private void setStripAdImage(String resource ,String color,final String idd){
           // stripImage.setImageResource(resource);
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place_holder)).into(stripImage);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));

            stripImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent stripIntent = new Intent(itemView.getContext(),ProductActivity.class);
                    stripIntent.putExtra("IDD",idd);
                    itemView.getContext().startActivity(stripIntent);
                }
            });
        }
    }
    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder{

        private TextView horizontalLayoutTitle;
        private Button horizotnalViewAllBtn;
        private RecyclerView horizontalProductRecView;
        private RelativeLayout relativeLayout;
        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);

            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scrolllayout_title);
            horizotnalViewAllBtn =   itemView.findViewById(R.id.h_scroll_viewall_btn);
            horizontalProductRecView = itemView.findViewById(R.id.h_product_rec_view);
        horizontalProductRecView.setRecycledViewPool(recycledViewPool);
        relativeLayout  = itemView.findViewById(R.id.horizontal_scroll_container);
        }

        private void setHorizontalProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList,String title,String color,List<WishListModel> wishListModelList){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                relativeLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            }
            horizontalLayoutTitle.setText(title);

            if (horizontalProductScrollModelList.size() > 3){
                horizotnalViewAllBtn.setVisibility(View.VISIBLE);
                horizotnalViewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishListModelList = wishListModelList;
                        Intent viewAllintent = new Intent(itemView.getContext(),ViewAllActivity.class);
                        viewAllintent.putExtra("layout_code",0);
                        viewAllintent.putExtra("title",title);
                        itemView.getContext().startActivity(viewAllintent);
                    }
                });
            }else{
                horizotnalViewAllBtn.setVisibility(View.INVISIBLE);
            }
            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            horizontalProductRecView.setLayoutManager(linearLayoutManager);
            horizontalProductRecView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }


    }
    public class GridProductViewHolder extends RecyclerView.ViewHolder{

        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        private GridLayout gridProductLayout;
        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);

            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutViewAllBtn =   itemView.findViewById(R.id.grid_product_layout_view_all_btn);
            gridProductLayout =   itemView.findViewById(R.id.grid_layout);

        }

        private void setGridProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList, String title, String grid_color){

            gridLayoutTitle.setText(title);
            gridProductLayout.setBackgroundColor(Color.parseColor(grid_color));

            for (int x =0 ; x<4 ;x++){
                ImageView productImage = gridProductLayout.getChildAt(x).findViewById(R.id.horizontal_product_img);
                 TextView productTitle= gridProductLayout.getChildAt(x).findViewById(R.id.horizontal_product_title);
                TextView productDesc = gridProductLayout.getChildAt(x).findViewById(R.id.horizontal_product_desc);
                TextView productPrice = gridProductLayout.getChildAt(x).findViewById(R.id.horizontal_product_price);

             Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage())
                     .apply(new RequestOptions().placeholder(R.drawable.place_holder))
                     .into(productImage);
             //   productImage.setImageResource(horizontalProductScrollModelList.get(x).getProductImage());
                productTitle.setText(horizontalProductScrollModelList.get(x).getProductTitle());
                productDesc.setText(horizontalProductScrollModelList.get(x).getProductDesc());
                productPrice.setText("Rs."+horizontalProductScrollModelList.get(x).getProductPrice()+"/-");
                gridProductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#ffffff"));

                if (!title.equals("")){
                    int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                        intent.putExtra("IDDD",horizontalProductScrollModelList.get(finalX).getProductID());
                        itemView.getContext().startActivity(intent);
                    }
                });
                }

            }
            if (!title.equals("")){

                gridLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewAllActivity.horizontalProductScrollModelList = horizontalProductScrollModelList;
                    Intent viewAllintent = new Intent(itemView.getContext(),ViewAllActivity.class);
                    viewAllintent.putExtra("layout_code",1);
                    viewAllintent.putExtra("title",title);
                    itemView.getContext().startActivity(viewAllintent);
                }
            });
          }
        }

    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView categoryRecyclerView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryRecyclerView = itemView.findViewById(R.id.category_recyclerview);

        }
        private void setCategory(List<CategoryModel> categoryModelList){

            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            categoryRecyclerView.setLayoutManager(layoutManager);
            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList,itemView.getContext());
             categoryRecyclerView.setAdapter(categoryAdapter);
             categoryAdapter.notifyDataSetChanged();
        }
    }
}
