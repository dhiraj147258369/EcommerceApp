  package com.example.dstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.dstore.MainActivity.showCART;
import static com.example.dstore.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static Activity productDetailsActivity;

    private ViewPager productImagesViewPager;
    private TabLayout viewpagerIndicator;
    private Button coupenRedeemBtn;

    public static FloatingActionButton addToWishListbtn;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;

    //////////COUPEN DIALOG//////
    public static TextView coupenTitle;
    public static TextView coupenExpiryDate;
    public static TextView coupenBody;
    private static LinearLayout  selectedCoupen;
    private static RecyclerView  coupenRecView;
    //////////COUPEN DIALOG//////

    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;

    ////RATING CODE///////////
    public static LinearLayout rateNowContainer;
    ////RATING CODE///////////

    private LinearLayout buyNowBtn;

    private FirebaseFirestore firebaseFirestore;

    private LinearLayout coupenRedemptionLayout;

    private TextView productTitle;
    private TextView avgRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private ImageView CODindicator;
    private TextView tvCODindicator;

    private TextView rewardTitle;
    private TextView rewardBody;

    private RelativeLayout productDescriptionTabContainer;
    private LinearLayout productDetailsContainer;

    private String productDescrption;
    private String productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private TextView productOnlyDescriptionBody;

    private TextView totalRatings;
    private LinearLayout ratingNumbersContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    public static int initialRating ;

    private LinearLayout addToCartBtn;
    public static MenuItem cartItem;
    private Dialog signInDialog;

    private FirebaseUser currentUser;
    public static String productID;

    private Dialog loadingDialog;
   private TextView badgeCount;
    private DocumentSnapshot documentSnapshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        productID = getIntent().getStringExtra("IDDD");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishListbtn = findViewById(R.id.floatingActionButton);
        productDetailsViewPager = findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout = findViewById(R.id.product_details_layout);
        coupenRedeemBtn = findViewById(R.id.coupen_redeem_btn);
        productTitle = findViewById(R.id.product_title);
        avgRatingMiniView = findViewById(R.id.tv_product_rating_mini_view);
        totalRatingMiniView = findViewById(R.id.total_ratings_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        CODindicator = findViewById(R.id.cod_indicator);
        tvCODindicator = findViewById(R.id.tv_cod_indicator);
        coupenRedemptionLayout = findViewById(R.id.coupen_redemption_layout);


        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);

        productDescriptionTabContainer = findViewById(R.id.product_description_tabs_container);
        productDetailsContainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);

        totalRatings = findViewById(R.id.rating_layout_total_ratings);
        ratingNumbersContainer = findViewById(R.id.rating_number_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.rating_progressbar_container);
        averageRating = findViewById(R.id.average_rating);

        addToCartBtn = findViewById(R.id.add_to_cart_btn);

        initialRating = -1;

        //////LOADING DIALOG//////
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //////LOADING DIALOG//////


        firebaseFirestore = FirebaseFirestore.getInstance();

        List<String> productImages = new ArrayList<>();
           
        firebaseFirestore.collection("PRODUCT").document(getIntent().getStringExtra("IDDD"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            loadingDialog.dismiss();
                         documentSnapshot = task.getResult();

                            for (long x = 1; x < (long)documentSnapshot.get("no_of_product_images")+1;x++){
                                productImages.add(documentSnapshot.get("pr_image_"+x).toString());
                            }
                            ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                            productImagesViewPager.setAdapter(productImagesAdapter);

                productTitle.setText(documentSnapshot.get("pr_title").toString());
                avgRatingMiniView.setText(documentSnapshot.get("pr_avg_rating").toString());
                totalRatingMiniView.setText("(" + (long) documentSnapshot.get("pr_total_ratings") + " total ratings)");
                productPrice.setText("Rs." + documentSnapshot.get("pr_price").toString() + "/-");
                cuttedPrice.setText("Rs." + documentSnapshot.get("pr_cutted_price").toString() + "/-");

                if ((boolean) documentSnapshot.get("pr_COD")) {
                    CODindicator.setVisibility(View.VISIBLE);
                    tvCODindicator.setVisibility(View.VISIBLE);
                } else {
                    CODindicator.setVisibility(View.GONE);
                    tvCODindicator.setVisibility(View.GONE);
                }

                ////reward
                rewardTitle.setText((long) documentSnapshot.get("pr_free_coupens") + documentSnapshot.get("pr_free_coupen_title").toString());
                rewardBody.setText(documentSnapshot.get("pr_price").toString());

                if ((boolean) documentSnapshot.get("pr_use_tab_layout")) {
                    productDescriptionTabContainer.setVisibility(View.VISIBLE);
                    productDetailsContainer.setVisibility(View.GONE);
                    productDescrption = documentSnapshot.get("pr_description").toString();

                    productOtherDetails = documentSnapshot.get("pr_other_details").toString();

                    for (long x = 1; x < (long) documentSnapshot.get("total_sp_titles") + 1; x++) {
                        productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("sp_title_" + x).toString()));
                        for (long y = 1; y < (long) documentSnapshot.get("sp_title_"+x+"_total_fields") + 1; y++) {
                            productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("sp_title_"+x+"_field_"+y+"_name").toString(), documentSnapshot.get("sp_title_"+x+"_field_"+y+"_value").toString()));
                        }
                    }
                } else {
                    productOnlyDescriptionBody.setText(documentSnapshot.get("pr_description").toString());
                    productDescriptionTabContainer.setVisibility(View.GONE);
                    productDetailsContainer.setVisibility(View.VISIBLE);
                }

                totalRatings.setText((long) documentSnapshot.get("pr_total_ratings") + " ratings");

                for (int x = 0; x < 5; x++) {
                    TextView rating = (TextView) ratingNumbersContainer.getChildAt(x);
                    rating.setText(String.valueOf((long) documentSnapshot.get("pr_"+ (5 - x) + "_star")));
                    ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                    int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("pr_total_ratings")));
                    progressBar.setMax(maxProgress);
                    progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get("pr_"+ (5 - x) + "_star"))));
                }

                totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("pr_total_ratings")));
                averageRating.setText(documentSnapshot.get("pr_avg_rating").toString());
                productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(), productDescrption, productOtherDetails, productSpecificationModelList));

                if (currentUser != null){
                    if (DBqueries.myRating.size() == 0){
                        DBqueries.loadRatingList(ProductDetailsActivity.this);
                    }
                    if (DBqueries.cartList.size() == 0) {
                        DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false,badgeCount,new TextView(ProductDetailsActivity.this));
                    }
                    if (DBqueries.wishlist.size() == 0) {
                        DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog,false);
                    } else {
                        loadingDialog.dismiss();
                    }
                }else{
                    loadingDialog.dismiss();
                }

                if (DBqueries.myRatedIds.contains(productID)){
                    int index = DBqueries.myRatedIds.indexOf(productID);
                    initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index)))-1;
                    setRating(initialRating);
                }
                            if (DBqueries.cartList.contains(productID)) {
                                ALREADY_ADDED_TO_CART = true;
                            } else {
                                ALREADY_ADDED_TO_CART = false;
                            }

                if (DBqueries.wishlist.contains(productID)) {
                    ALREADY_ADDED_TO_WISHLIST = true;
                    addToWishListbtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                } else {
                    addToWishListbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#cccccc")));
                    ALREADY_ADDED_TO_WISHLIST = false;
                }

                            ///////////ADD TO CART//////////////////////////////
                            if ((boolean)documentSnapshot.get("in_stock")){
                                addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (currentUser == null ){
                                            signInDialog.show();
                                        }else{
                                            //    Toast.makeText(ProductDetailsActivity.this,"ADDED TO CART",Toast.LENGTH_SHORT).show();
                                            if (!running_cart_query){
                                                running_cart_query = true;
                                                if (ALREADY_ADDED_TO_CART){
                                                    running_cart_query = false;
                                                    Toast.makeText(ProductDetailsActivity.this,"ALREADY ADDED TO CART",Toast.LENGTH_SHORT).show();

                                                }else {
                                                    Map<String, Object> addProduct = new HashMap<>();
                                                    addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                    addProduct.put("list_size", (long) (DBqueries.wishlist.size() + 1));

                                                    firebaseFirestore.collection("USERS")
                                                            .document(currentUser.getUid())
                                                            .collection("USER_DATA")
                                                            .document("MY_CART")
                                                            .update(addProduct)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        if (DBqueries.cartItemModelList.size() != 0) {
                                                                            DBqueries.cartItemModelList.add(0,new CartItemModel(
                                                                                    CartItemModel.CART_ITEM
                                                                                    ,productID
                                                                                    ,documentSnapshot.get("pr_image_1").toString()
                                                                                    ,documentSnapshot.get("pr_title").toString()
                                                                                    , (long)documentSnapshot.get("pr_free_coupens")
                                                                                    , documentSnapshot.get("pr_price").toString()
                                                                                    , documentSnapshot.get("pr_cutted_price").toString()
                                                                                    ,(long)1
                                                                                    ,(long)0
                                                                                    ,(long)0
                                                                                    ,(boolean)documentSnapshot.get("in_stock")
                                                                            ));
                                                                        }
                                                                        ALREADY_ADDED_TO_CART = true;
                                                                        DBqueries.cartList.add(productID);
                                                                        Toast.makeText(ProductDetailsActivity.this, "Added to cart Successfully", Toast.LENGTH_SHORT).show();
                                                                        invalidateOptionsMenu();
                                                                        running_cart_query = false;

                                                                    } else {
                                                                        running_cart_query = false;
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    }
                                });

                            }else{
                                buyNowBtn.setVisibility(View.GONE);
                                TextView outofStock = (TextView) addToCartBtn.getChildAt(0);
                                outofStock.setText("OUT OF STOCK");
                                outofStock.setTextColor(getResources().getColor(R.color.red));
                                outofStock.setCompoundDrawables(null,null,null,null);

                            }

                            ///////////ADD TO CART//////////

                        }else{
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


   //      List<String> productImages = new ArrayList<>();
//        productImages.add(R.drawable.phone_front);
//        productImages.add(R.drawable.phone_full);
//        productImages.add(R.drawable.phone_top);
//        productImages.add(R.drawable.phone_back);



       viewpagerIndicator.setupWithViewPager(productImagesViewPager,true);

        addToWishListbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                  signInDialog.show();
                }else{
                //    addToWishListbtn.setEnabled(false);
                    if (!running_wishlist_query){
                        running_wishlist_query = true;
                if (ALREADY_ADDED_TO_WISHLIST){

                    int index = DBqueries.wishlist.indexOf(productID);
                    DBqueries.removeFormWishList(index,ProductDetailsActivity.this);
                    addToWishListbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#cccccc")));

                }else {

                    addToWishListbtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                    Map<String, Object> addProduct = new HashMap<>();
                    addProduct.put("product_ID_" + String.valueOf(DBqueries.wishlist.size()), productID);
                    addProduct.put("list_size", (long) (DBqueries.wishlist.size() + 1));

                    firebaseFirestore.collection("USERS")
                            .document(currentUser.getUid())
                            .collection("USER_DATA")
                            .document("MY_WISHLIST")
                            .update(addProduct)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        if (DBqueries.wishListModelList.size() != 0) {
                                            DBqueries.wishListModelList.add(new WishListModel(
                                                      productID
                                                    , documentSnapshot.get("pr_image_1").toString()
                                                    , documentSnapshot.get("pr_title").toString()
                                                    , documentSnapshot.get("pr_price").toString()
                                                    , documentSnapshot.get("pr_avg_rating").toString()
                                                    , (long) documentSnapshot.get("pr_total_ratings")
                                                    , (long) documentSnapshot.get("pr_free_coupens")
                                                    , documentSnapshot.get("pr_cutted_price").toString()
                                                    , (boolean) documentSnapshot.get("pr_COD")
                                                    , (boolean) documentSnapshot.get("in_stock")
                                            ));

                                            }
                                            ALREADY_ADDED_TO_WISHLIST = true;
                                            addToWishListbtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                            DBqueries.wishlist.add(productID);
                                            Toast.makeText(ProductDetailsActivity.this, "Added to wishlist Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addToWishListbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#cccccc")));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;
                                }
                            });
                   }
                 }
                }
            }
        });


        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));

        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //////RATE CODE/////////
        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x = 0;x<rateNowContainer.getChildCount();x++){
            final int starPosition =x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                    signInDialog.show();
                    }else{
                        if (starPosition != initialRating) {
                            if (!running_rating_query) {
                                running_rating_query = true;
                                setRating(starPosition);

                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBqueries.myRatedIds.contains(productID)) {

                                    TextView oldRating = (TextView) ratingNumbersContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingNumbersContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put("pr_" + (initialRating + 1) + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put("pr_" + (starPosition + 1) + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("pr_avg_rating", calculateAverageRating((long) starPosition - initialRating, true));

                                } else {

                                    updateRating.put("pr_" + (starPosition + 1) + "_star", (Long) documentSnapshot.get("pr_" + (starPosition + 1) + "_star") + 1);
                                    updateRating.put("pr_avg_rating", calculateAverageRating((long) starPosition + 1, false));
                                    updateRating.put("pr_total_ratings", (long) documentSnapshot.get("pr_total_ratings") + 1);

                                }
                                firebaseFirestore.collection("PRODUCT")
                                        .document(productID)
                                        .update(updateRating)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Map<String, Object> myRating = new HashMap<>();
                                                    if (DBqueries.myRatedIds.contains(productID)) {
                                                        myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                                    } else {
                                                        myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                                        myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                                                        myRating.put("list_size", DBqueries.myRatedIds.size() + 1);
                                                    }

                                                    firebaseFirestore.collection("USERS")
                                                            .document(currentUser.getUid())
                                                            .collection("USER_DATA")
                                                            .document("MY_RATINGS")
                                                            .update(myRating)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        if (DBqueries.myRatedIds.contains(productID)) {
                                                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                                            TextView oldRating = (TextView) ratingNumbersContainer.getChildAt(5 - initialRating - 1);
                                                                            TextView finalRating = (TextView) ratingNumbersContainer.getChildAt(5 - starPosition - 1);
                                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                                        } else {

                                                                            DBqueries.myRatedIds.add(productID);
                                                                            DBqueries.myRating.add((long) starPosition + 1);

                                                                            TextView rating = (TextView) ratingNumbersContainer.getChildAt(5 - starPosition - 1);
                                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                                            totalRatingMiniView.setText("( " + ((long) documentSnapshot.get("pr_total_ratings") + 1) + " ratings)");
                                                                            totalRatings.setText((long) documentSnapshot.get("pr_total_ratings") + 1 + " ratings");
                                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("pr_total_ratings") + 1));
                                                                            Toast.makeText(ProductDetailsActivity.this, "THANK YOU FOR RATING", Toast.LENGTH_SHORT).show();
                                                                        }


                                                                        for (int x = 0; x < 5; x++) {
                                                                            TextView ratingFigures = (TextView) ratingNumbersContainer.getChildAt(x);

                                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                                            progressBar.setMax(maxProgress);
                                                                            progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));
                                                                        }
                                                                        initialRating = starPosition;
                                                                        averageRating.setText(calculateAverageRating(0, true));
                                                                        avgRatingMiniView.setText(calculateAverageRating(0, true));

                                                                        if (DBqueries.wishlist.contains(productID) && DBqueries.wishListModelList.size() != 0) {

                                                                            int index = DBqueries.wishlist.indexOf(productID);
                                                                            DBqueries.wishListModelList.get(index).setRating(averageRating.getText().toString());
                                                                            DBqueries.wishListModelList.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));
                                                                        }
                                                                    } else {
                                                                        setRating(initialRating);
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    running_rating_query = false;
                                                                }
                                                            });

                                                }else{
                                                    running_rating_query = false;
                                                    setRating(initialRating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }
            });
        }
        //////RATE CODE//////

        buyNowBtn = findViewById(R.id.buy_now_btn);
        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null ){
                    signInDialog.show();
                }else{
                    DeliveryActivity.fromcart = false;
                    loadingDialog.show();
                    productDetailsActivity = ProductDetailsActivity.this;
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(
                            CartItemModel.CART_ITEM
                            ,productID
                            ,documentSnapshot.get("pr_image_1").toString()
                            ,documentSnapshot.get("pr_title").toString()
                            , (long)documentSnapshot.get("pr_free_coupens")
                            , documentSnapshot.get("pr_price").toString()
                            , documentSnapshot.get("pr_cutted_price").toString()
                            ,(long)1
                            ,(long)0
                            ,(long)0
                            ,(boolean)documentSnapshot.get("in_stock")
                    ));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                    if (DBqueries.addressesModelList.size() ==0){
                        DBqueries.loadAddresses(ProductDetailsActivity.this,loadingDialog);
                    }else{
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailsActivity.this,DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });




        /////////COUPEN CODE/////////////////////////
        Dialog coupenRedeemDialog = new Dialog(ProductDetailsActivity.this);
                coupenRedeemDialog.setContentView(R.layout.coupen_redeem_dialog);
                coupenRedeemDialog.setCancelable(true);
                coupenRedeemDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                ImageView openCoupenrecView = coupenRedeemDialog.findViewById(R.id.toogle);
                  coupenRecView = coupenRedeemDialog.findViewById(R.id.coupen_recycler_view);
                  selectedCoupen = coupenRedeemDialog.findViewById(R.id.selected_coupen);
                  coupenTitle = coupenRedeemDialog.findViewById(R.id.coupen_title);
                coupenExpiryDate = coupenRedeemDialog.findViewById(R.id.coupen_validity);
                coupenBody = coupenRedeemDialog.findViewById(R.id.coupen_body);

                TextView originalPrice = coupenRedeemDialog.findViewById(R.id.original_price);
                TextView discountedPrice = coupenRedeemDialog.findViewById(R.id.discounted_price);

                LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                coupenRecView.setLayoutManager(layoutManager);

                List<RewardModel> rewardModelList = new ArrayList<>();
                rewardModelList.add(new RewardModel("Cashback", "till jun 22,2020","get 20% cashback on any product above Rs.200/-"));
                rewardModelList.add(new RewardModel("Cashback", "till jun 22,2020","get 20% cashback on any product above Rs.200/-"));
                rewardModelList.add(new RewardModel("Cashback", "till jun 22,2020","get 20% cashback on any product above Rs.200/-"));
                rewardModelList.add(new RewardModel("Cashback", "till jun 22,2020","get 20% cashback on any product above Rs.200/-"));
                rewardModelList.add(new RewardModel("Cashback", "till jun 22,2020","get 20% cashback on any product above Rs.200/-"));
                rewardModelList.add(new RewardModel("Cashback", "till jun 22,2020","get 20% cashback on any product above Rs.200/-"));
                rewardModelList.add(new RewardModel("Cashback", "till jun 22,2020","get 20% cashback on any product above Rs.200/-"));
                rewardModelList.add(new RewardModel("Cashback", "till jun 22,2020","get 20% cashback on any product above Rs.200/-"));
                rewardModelList.add(new RewardModel("Cashback", "till jun 22,2020","get 20% cashback on any product above Rs.200/-"));

                MyRewardsAdapter rewardsAdapter = new MyRewardsAdapter(rewardModelList,true);
                coupenRecView.setAdapter(rewardsAdapter);
                rewardsAdapter.notifyDataSetChanged();

                openCoupenrecView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       showDialogRecView();
                    }
                });
       /////////COUPEN CODE/////////////////////////


        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                }else{
                    coupenRedeemDialog.show();
                }
            }
        });

        //////dialog////
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button signInbtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent intent = new Intent(ProductDetailsActivity.this,RegisterActivity.class);

        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment.disableCloseBtn = true;
                LoginFragment.disableCloseBtn = true;
                setSignUpFragment = false;
                startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment.disableCloseBtn = true;
                LoginFragment.disableCloseBtn = true;
                setSignUpFragment = true;
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            coupenRedemptionLayout.setVisibility(View.GONE);
        }else{
            coupenRedemptionLayout.setVisibility(View.VISIBLE);
        }
        if (currentUser != null){
            if (DBqueries.myRating.size() == 0){
                DBqueries.loadRatingList(ProductDetailsActivity.this);
            }
            if (DBqueries.wishlist.size() == 0) {
                DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog,false);
            } else {
                loadingDialog.dismiss();
            }

        }else{
            loadingDialog.dismiss();
        }

        if (DBqueries.myRatedIds.contains(productID)){
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index)))-1;
            setRating(initialRating);
        }

       if (DBqueries.cartList.contains(productID)) {
               ALREADY_ADDED_TO_CART = true;
               } else {
                ALREADY_ADDED_TO_CART = false;
             }

        if (DBqueries.wishlist.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishListbtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
        } else {
            addToWishListbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#cccccc")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        invalidateOptionsMenu();
    }

    @SuppressLint("NewApi")
    public static void setRating(int starPosition) {
        if (starPosition > -1){
        for (int x =0; x < rateNowContainer.getChildCount() ;x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            //    starBtn.setBackgroundColor(Color.parseColor("#bebebe"));
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
            }
        }
        }
    }

    private String calculateAverageRating(long currentUserRating,Boolean update){
        Double totalStars = Double.valueOf(0);
        for (int x = 1 ; x<6 ;x++){
            TextView ratingNo = (TextView) ratingNumbersContainer.getChildAt(5-x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString())*x);
        }
        totalStars = totalStars + currentUserRating;

        if (update){
            return String.valueOf(totalStars/Long.parseLong(totalRatingsFigure.getText().toString())).substring(0,3);
        }else{
            return String.valueOf(totalStars/(Long.parseLong(totalRatingsFigure.getText().toString())+1)).substring(0,3);
        }
    }

    public static void showDialogRecView(){
        if (coupenRecView.getVisibility() == View.GONE){
            coupenRecView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        }else{
            coupenRecView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

         cartItem = menu.findItem(R.id.my_cart);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon =  cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.ic_cart);
             badgeCount = cartItem.getActionView().findViewById(R.id.badge_count_id);

        if (currentUser != null ){
            if (DBqueries.cartList.size() == 0) {
                DBqueries.loadCartList(ProductDetailsActivity.this,loadingDialog, false,badgeCount,new TextView(ProductDetailsActivity.this));
            }else{
                badgeCount.setVisibility(View.VISIBLE);
                if (DBqueries.cartList.size() < 99){
                    badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                }else{
                    badgeCount.setText("99");
                }
            }
        }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else{
                        Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                        showCART = true;
                        startActivity(intent);
                    }
                }
            });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_icon){
            return true;
        }else  if (id == R.id.my_cart) {
            if (currentUser == null) {

                signInDialog.show();
            } else{
                Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
            showCART = true;
            startActivity(intent);
            return true;
        }
        }else  if (id == android.R.id.home){
            productDetailsActivity = null;
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void temp(){
//        loadingDialog.dismiss();
//        if (task.isSuccessful()) {
//
//            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//
//                for (long x = 1; x < (long) documentSnapshot.get("no_of_product_imgs") + 1; x++) {
//                    productImages.add(documentSnapshot.get("product_image_" + x).toString());
//                }
//                ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
//                productImagesViewPager.setAdapter(productImagesAdapter);
//                productTitle.setText(documentSnapshot.get("product_title").toString());
//
//                avgRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
//                totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + " total ratings)");
//                productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
//                cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");
//
//                if ((boolean) documentSnapshot.get("COD")) {
//                    CODindicator.setVisibility(View.VISIBLE);
//                    tvCODindicator.setVisibility(View.VISIBLE);
//                } else {
//                    CODindicator.setVisibility(View.GONE);
//                    tvCODindicator.setVisibility(View.GONE);
//                }
//
//                ////reward
//                rewardTitle.setText((long) documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupen_title").toString());
//                rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());
//
//                if ((boolean) documentSnapshot.get("use_tab_layout")) {
//                    productDescriptionTabContainer.setVisibility(View.VISIBLE);
//                    productDetailsContainer.setVisibility(View.GONE);
//                    productDescrption = documentSnapshot.get("product_description").toString();
//
//                    productOtherDetails = documentSnapshot.get("product_other_details").toString();
//
//                    for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
//                        productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
//                        for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
//                            productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));
//                        }
//                    }
//                } else {
//                    productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
//                    productDescriptionTabContainer.setVisibility(View.GONE);
//                    productDetailsContainer.setVisibility(View.VISIBLE);
//                }
//
//                totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
//                for (int x = 0; x < 5; x++) {
//                    TextView rating = (TextView) ratingNumbersContainer.getChildAt(x);
//                    rating.setText(String.valueOf((long) documentSnapshot.get(5 - x + "_star")));
//                    ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
//                    int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
//                    progressBar.setMax(maxProgress);
//                    progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get(5 - x + "_star"))));
//                }
//                totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
//                averageRating.setText(documentSnapshot.get("average_rating").toString());
//                productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(), productDescrption, productOtherDetails, productSpecificationModelList));
//
//                if (DBqueries.wishlist.size() == 0) {
//                    DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog);
//                } else {
//                    loadingDialog.dismiss();
//                }
//                if (DBqueries.wishlist.contains(productID)) {
//                    ALREADY_ADDED_TO_WISHLIST = true;
//                    addToWishListbtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
//                } else {
//                    ALREADY_ADDED_TO_WISHLIST = false;
//                }
//            }
//        } else {
//            String error = task.getException().getMessage();
//            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        super.onBackPressed();
    }
}