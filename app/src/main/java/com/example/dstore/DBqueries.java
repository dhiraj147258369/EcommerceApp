package com.example.dstore;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
  // public static List<HomePageModel> homePageModelList = new ArrayList<>();
    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadCategoryNames = new ArrayList<>();
    public static List<String> wishlist = new ArrayList<>();
    public static List<WishListModel> wishListModelList = new ArrayList<>();
    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();
    public static List<String> cartList = new ArrayList<>();
    public static List<AddressesModel> addressesModelList = new ArrayList<>();

    public static int selectedAddress = -1;

    public static void loadFragmentData(RecyclerView homepageRecyclerView, Context context, int index, String categoryName){

        firebaseFirestore.collection("CATEGORY")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS")
                .orderBy("index")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                if ((long)documentSnapshot.get("view_type") == 0){
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners =(long)documentSnapshot.get("no_of_banners");
                                    for (int x = 1 ;x < no_of_banners + 1;x++){
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString(),documentSnapshot.get("banner_"+x+"_background").toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0,sliderModelList));

                                }
                                else if ((long)documentSnapshot.get("view_type") == 1){
                                    lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_image").toString(),documentSnapshot.get("background").toString(),documentSnapshot.get("idd").toString()));
                                }
                                else if ((long)documentSnapshot.get("view_type") == 4){
                                    List<CategoryModel> categoryModelList= new ArrayList<>();

                                    long no_of_h_categories =(long)documentSnapshot.get("no_of_h_categories");

                                    for (int x = 1 ; x < no_of_h_categories+1;x++ ){
                                        categoryModelList.add(new CategoryModel(documentSnapshot.get("h_cat_icon_"+x).toString(),documentSnapshot.get("h_cat_name_"+x).toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(categoryModelList,4));
                                }
                                else if ((long)documentSnapshot.get("view_type") == 2){
                                    List<HorizontalProductScrollModel> hProductList = new ArrayList<>();
                                    List<WishListModel> viewAllProductList = new ArrayList<>();
                                    long pr_total_no = (long)documentSnapshot.get("pr_total_no");
                                    for (int a = 1; a < pr_total_no+1; a++){
                                        hProductList.add(new HorizontalProductScrollModel(
                                                  documentSnapshot.get("pr_id_"+a).toString()
                                                ,documentSnapshot.get("pr_image_"+a).toString()
                                                ,documentSnapshot.get("pr_title_"+a).toString()
                                                ,documentSnapshot.get("pr_subtitle_"+a).toString()
                                                ,documentSnapshot.get("pr_price_"+a).toString()
                                        ));

                                        viewAllProductList.add(new WishListModel(
                                                documentSnapshot.get("pr_id_"+a).toString()
                                                ,documentSnapshot.get("pr_image_"+a).toString()
                                                ,documentSnapshot.get("pr_full_title_"+a).toString()
                                                ,documentSnapshot.get("pr_price_"+a).toString()
                                                ,documentSnapshot.get("pr_avg_rating_"+a).toString()
                                                ,(long)documentSnapshot.get("pr_total_ratings_"+a)
                                                ,(long)documentSnapshot.get("pr_free_coupens_"+a)
                                                ,documentSnapshot.get("pr_cutted_price_"+a).toString()
                                                ,(boolean)documentSnapshot.get("pr_COD_"+a)
                                                ,(boolean)documentSnapshot.get("pr_in_stock_"+a)
                                        ));
                                    }
                                    lists.get(index).add(new HomePageModel(2,documentSnapshot.get("pr_layout_title").toString(),documentSnapshot.get("pr_background").toString(),hProductList,viewAllProductList));
                                }
//                                else if ((long)documentSnapshot.get("view_type") == 2) {
//                                    List<WishListModel> viewAllProductList = new ArrayList<>();
//                                    List<HorizontalProductScrollModel> horizontalProductScrollModelListt = new ArrayList<>();
//
//                                    long no_of_horizontal_products = (long) documentSnapshot.get("no_of_horizontal_products");
//                                    for (int x = 1; x < no_of_horizontal_products + 1; x++) {
//                                        horizontalProductScrollModelListt.add(new HorizontalProductScrollModel(
//                                                documentSnapshot.get("h_product_id_"+x).toString()
//                                                ,documentSnapshot.get("h_product_img_"+x).toString()
//                                                ,documentSnapshot.get("h_product_title_"+x).toString()
//                                                ,documentSnapshot.get("h_product_subtitle_"+x).toString()
//                                                ,documentSnapshot.get("h_product_price_"+x).toString()));
//
//                                        viewAllProductList.add(new WishListModel(
//                                                documentSnapshot.get("h_product_img_"+x).toString()
//                                                ,documentSnapshot.get("h_product_full_title_"+x).toString()
//                                                ,documentSnapshot.get("h_product_subtitle_"+x).toString()
//                                                ,documentSnapshot.get("h_product_price_"+x).toString()
//                                                ,(long)documentSnapshot.get("total_ratings_"+x)
//                                                ,(long)documentSnapshot.get("free_coupens_"+x)
//                                                ,documentSnapshot.get("cutted_price_"+x).toString()
//                                                ,(boolean)documentSnapshot.get("COD_"+x)
//                                        ));
//                                    }
//                                    lists.get(index).add(new HomePageModel(2,documentSnapshot.get("h_deal_main_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductScrollModelListt,viewAllProductList));
//                                }
//                                else if ((long)documentSnapshot.get("view_type") == 3) {
//                                    List<HorizontalProductScrollModel> gridProductList = new ArrayList<>();
//
//                                    long no_of_grid_products = (long) documentSnapshot.get("no_of_grid_products");
//                                    for (int x = 1; x < no_of_grid_products + 1; x++) {
//
//                                        gridProductList.add(new HorizontalProductScrollModel(
//                                                documentSnapshot.get("product_id_"+x).toString()
//                                                ,documentSnapshot.get("product_img_"+x).toString(),
//                                                documentSnapshot.get("product_title_"+x).toString(),
//                                                documentSnapshot.get("product_subtitle_"+x).toString(),
//                                                documentSnapshot.get("product_price_"+x).toString()));



//                                    }
//                                    lists.get(index).add(new HomePageModel(3,documentSnapshot.get("grid_layout_title").toString(),documentSnapshot.get("grid_layout_background").toString(),gridProductList));
//                                }
                            }
                          HomepageAdapter homepageAdapter = new HomepageAdapter(lists.get(index));
                            homepageRecyclerView.setAdapter(homepageAdapter);
                            homepageAdapter.notifyDataSetChanged();

                          HomeFragment.swipeRefreshLayout.setRefreshing(false);
                        }else{

                        }
                    }
                });

    }

   public static void loadWishList(final Context context, Dialog dialog,final boolean loadProductData){
       wishlist.clear();
        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_WISHLIST")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            for (long x = 0 ; x < (long)task.getResult().get("list_size"); x++){
                                if (wishlist.size() == 0){
                                    wishlist.add(task.getResult().get("product_ID_"+x).toString());
                                }

                                if (DBqueries.wishlist.contains(ProductDetailsActivity.productID)) {
                                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                                    if(ProductDetailsActivity.addToWishListbtn != null){
                                        ProductDetailsActivity.addToWishListbtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                                    }
                                } else {
                                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                                    if(ProductDetailsActivity.addToWishListbtn != null) {
                                        ProductDetailsActivity.addToWishListbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#cccccc")));
                                    }
                                }

                                if (loadProductData) {
                                    wishListModelList.clear();
                                    String productID = task.getResult().get("product_ID_"+x).toString();
                                    firebaseFirestore.collection("PRODUCT")
                                            .document(productID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        wishListModelList.add(new WishListModel(
                                                                  productID
                                                                , task.getResult().get("pr_image_1").toString()
                                                                , task.getResult().get("pr_title").toString()
                                                                , task.getResult().get("pr_price").toString()
                                                                , task.getResult().get("pr_avg_rating").toString()
                                                                , (long) task.getResult().get("pr_total_ratings")
                                                                , (long) task.getResult().get("pr_free_coupens")
                                                                , task.getResult().get("pr_cutted_price").toString()
                                                                 , (boolean) task.getResult().get("pr_COD")
                                                                , (boolean) task.getResult().get("in_stock")
                                                        ));

                                                        //     wishlist.add(productID);
                                                         MyWishListFragment.wishListAdapter.notifyDataSetChanged();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
   }

   public static void removeFormWishList(final int index,final Context context){
        String removedProductId = wishlist.get(index);
       wishlist.remove(index);

       Map<String,Object> updateWishList = new HashMap<>();
       for (int x = 0; x < wishlist.size();x++){
           updateWishList.put("product_ID_"+x,wishlist.get(x));
       }
       updateWishList.put("list_size",(long)wishlist.size());

       firebaseFirestore.collection("USERS")
               .document(FirebaseAuth.getInstance().getUid())
               .collection("USER_DATA")
               .document("MY_WISHLIST")
               .set(updateWishList)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           if (wishListModelList.size() != 0){
                               wishListModelList.remove(index);

                               MyWishListFragment.wishListAdapter.notifyDataSetChanged();
                           }

                           ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                           Toast.makeText(context,"REMOVED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                       }else{
                           if(ProductDetailsActivity.addToWishListbtn != null) {
                               ProductDetailsActivity.addToWishListbtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                           }
                           wishlist.add(index,removedProductId);
                           String error = task.getException().getMessage();
                           Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                           }
//                       if(ProductDetailsActivity.addToWishListbtn != null){
//                           ProductDetailsActivity.addToWishListbtn.setEnabled(true);
//                     }
                       ProductDetailsActivity.running_wishlist_query = false;
                   }
               });
   }

   public static void loadRatingList(final Context context){


            myRatedIds.clear();
            myRating.clear();

            firebaseFirestore.collection("USERS")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA")
                    .document("MY_RATINGS")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                    myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                                    myRating.add((long) task.getResult().get("rating_" + x));

                                    if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailsActivity.productID)) {
                                        ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x)))-1;
                                       if ( ProductDetailsActivity.rateNowContainer != null) {
                                           ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                                       }
                                    }
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }

  public static void loadCartList(final Context context,final Dialog dialog,final boolean loadProductData,final TextView badgeCount,final TextView cartTotalAmount){
      cartList.clear();
      firebaseFirestore.collection("USERS")
              .document(FirebaseAuth.getInstance().getUid())
              .collection("USER_DATA")
              .document("MY_CART")
              .get()
              .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if (task.isSuccessful()){

                          for (long x = 0 ; x < (long)task.getResult().get("list_size"); x++){
                                    
                                  cartList.add(task.getResult().get("product_ID_"+x).toString());

                              if (DBqueries.cartList.contains(ProductDetailsActivity.productID)) {
                                  ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;

                              } else {
                                  ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;

                              }

                              if (loadProductData) {
                                  cartItemModelList.clear();
                                  String productID = task.getResult().get("product_ID_"+x).toString();
                                  firebaseFirestore.collection("PRODUCT")
                                          .document(productID)
                                          .get()
                                          .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                      int index = 0;
                                                      if (cartList.size() >= 2){
                                                          index = cartList.size() -2;
                                                      }
                                                      cartItemModelList.add(index,new CartItemModel(
                                                              CartItemModel.CART_ITEM
                                                              ,productID
                                                              , task.getResult().get("pr_image_1").toString()
                                                              , task.getResult().get("pr_title").toString()
                                                              , (long) task.getResult().get("pr_free_coupens")
                                                              , task.getResult().get("pr_price").toString()
                                                              , task.getResult().get("pr_cutted_price").toString()
                                                              ,(long)1
                                                              ,(long)0
                                                              ,(long)0
                                                              ,(boolean)task.getResult().get("in_stock")
                                                      ));

                                                      if (cartList.size() == 1){
                                                          cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                                          LinearLayout parent = (LinearLayout)cartTotalAmount.getParent();
                                                          parent.setVisibility(View.VISIBLE);
                                                      }
                                                      if (cartList.size() == 0){
                                                          cartItemModelList.clear();
                                                      }
                                                      //     wishlist.add(productID);
                                                      MyCartFragment.cartAdapter.notifyDataSetChanged();
                                                  } else {
                                                      String error = task.getException().getMessage();
                                                      Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                          });
                              }
                          }
                          if (cartList.size() != 0){
                              badgeCount.setVisibility(View.VISIBLE);
                          }
                          else {
                              badgeCount.setVisibility(View.INVISIBLE);
                          }
                          if (DBqueries.cartList.size() < 99){
                              badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                          }else{
                              badgeCount.setText("99");
                          }
                      }else{
                          String error = task.getException().getMessage();
                          Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                      }
                      dialog.dismiss();
                  }
              });
  }

  public static void removeFromCart(final int index,final Context context,final TextView cartTotalAmount){
      String removedProductId = cartList.get(index);
      cartList.remove(index);

      Map<String,Object> updateCartList = new HashMap<>();
      for (int x = 0; x < cartList.size();x++){
          updateCartList.put("product_ID_"+x,wishlist.get(x));
      }
      updateCartList.put("list_size",(long)wishlist.size());

      firebaseFirestore.collection("USERS")
              .document(FirebaseAuth.getInstance().getUid())
              .collection("USER_DATA")
              .document("MY_CART")
              .set(updateCartList)
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          if (cartItemModelList.size() != 0){
                              cartItemModelList.remove(index);

                              MyCartFragment.cartAdapter.notifyDataSetChanged();
                          }
                          if (cartList.size() == 0){
                              LinearLayout parent =(LinearLayout) cartTotalAmount.getParent();
                                parent.setVisibility(View.GONE);
                              cartItemModelList.clear();
                          }
                          Toast.makeText(context,"REMOVED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                      }else{
                          cartList.add(index,removedProductId);
                          String error = task.getException().getMessage();
                          Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                      }
//                       if(ProductDetailsActivity.addToWishListbtn != null){
//                           ProductDetailsActivity.addToWishListbtn.setEnabled(true);
//                     }
                      ProductDetailsActivity.running_cart_query = false;
                  }
              });
  }

  public static void loadAddresses(final Context context,Dialog loadingDialog){
        addressesModelList.clear();
      firebaseFirestore.collection("USERS")
              .document(FirebaseAuth.getInstance().getUid())
              .collection("USER_DATA")
              .document("MY_ADDRESSES")
              .get()
              .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if (task.isSuccessful()){
                          Intent deliveryIntent;
                          if ((long)task.getResult().get("list_size") == 0){
                               deliveryIntent = new Intent(context,AddAddressesActivity.class);
                               deliveryIntent.putExtra("INTENT","deliveryIntent");
                          }else{

                              for (long x = 1 ; x < (long)task.getResult().get("list_size") +1; x++){
                                  addressesModelList.add(new AddressesModel(
                                          task.getResult().get("fullname_"+x).toString()
                                          ,task.getResult().get("address_"+x).toString()
                                           ,task.getResult().get("pincode_"+x).toString()
                                          ,(boolean) task.getResult().get("selected_"+x)
                                  ));

                                  if ((boolean)task.getResult().get("selected_"+x)){
                                      selectedAddress = Integer.parseInt(String.valueOf(x-1));
                                  }
                              }
                               deliveryIntent = new Intent(context,DeliveryActivity.class);
                          }
                          context.startActivity(deliveryIntent);

                      } else {
                          String error = task.getException().getMessage();
                          Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                      }
                      loadingDialog.dismiss();
                  }
              });
  }
   public static void clearData(){
        lists.clear();
        loadCategoryNames.clear();
        wishlist.clear();
        wishListModelList.clear();
        cartList.clear();
        cartItemModelList.clear();
        myRatedIds.clear();
        myRating.clear();
        addressesModelList.clear();
   }
}
