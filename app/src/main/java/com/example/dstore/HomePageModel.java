package com.example.dstore;

import java.util.ArrayList;
import java.util.List;

public class HomePageModel {

    public static final int BANNER_SLIDER =0;
    public static final int STRIP_AD_BANNER =1;
    public static final int HORIZONAL_PRODUCT_VIEW =2;
    public static final int GRID_PRODUCT_VIEW =3;
    public static final int CATEGORY_VIEW =4;

    private int type;
    private String backgroundColor;

    public int getType(){
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    /////Banner////
    private List<SliderModel> sliderModelList ;
    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }
    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    /////Banner////


    ///////STRIP AD//////////
      private String resource;



    private String idd;
    public HomePageModel(int type, String resource, String backgroundColor,String idd) {
        this.type = type;
        this.resource = resource;
        this.backgroundColor = backgroundColor;
        this.idd = idd;
    }
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getIdd() {
        return idd;
    }
    public void setIdd(String idd) {
        this.idd = idd;
    }

///////STRIP AD//////////


    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;


    private String title;
    ///////horizontal product////////////     && GRID PRODUCT LAYOUT

    private List<WishListModel> viewAllProductList;
    public HomePageModel(int type, String title,String backgroundColor, List<HorizontalProductScrollModel> horizontalProductScrollModelList,List<WishListModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.viewAllProductList = viewAllProductList;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }
    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }
    public List<WishListModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<WishListModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }
    ///////horizontal product////////////

    /////////////GRID CODE//////
    public HomePageModel(int type, String title,String backgroundColor, List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }
    /////////////GRID CODE//////


    //////CATEGORY/////////////
    List<CategoryModel> categoryModelList;
    public HomePageModel( List<CategoryModel> categoryModelList,int type) {
        this.type = type;
        this.categoryModelList = categoryModelList;
    }
    public List<CategoryModel> getCategoryModelList() {
        return categoryModelList;
    }
    public void setCategoryModelList(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }
//////CATEGORY/////////////



}
