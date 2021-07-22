package com.example.dstore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import static com.example.dstore.RegisterActivity.setSignUpFragment;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private AppBarConfiguration mAppBarConfiguration;

    public static DrawerLayout drawer;
    private FrameLayout frameLayout;
    private int currentFragment = -1;
    private static final int HOME_FRAGMENT =0;
    private static final int CART_FRAGMENT =1;
    private static final int ORDERS_FRAGMENT =2;
    private static final int WISHLIST_FRAGMENT =3;
    private static final int REWARDS_FRAGMENT =4;
    private static final int ACCOUNT_FRAGMENT =5;
    public static boolean showCART = false;
    private Window window;
    private NavigationView navigationView;
    private ImageView actionBarLogo;
    private  Toolbar toolbar;
    private Dialog signInDialog;
    private FirebaseUser currentUser;
    private TextView badgeCount;

    private int scrollFlags;
    private AppBarLayout.LayoutParams params;

    public static Activity mainActivity;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
        actionBarLogo = findViewById(R.id.action_bar_logo);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navigationView.setNavigationItemSelectedListener(this);
        if (currentFragment ==HOME_FRAGMENT){

            navigationView.getMenu().getItem(0).setChecked(true);
        }

        frameLayout = findViewById(R.id.framelayout_main);

        if (showCART) {
            mainActivity = this;
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("MY CART", new MyCartFragment(), -2);
        } else {
            setFragment(new HomeFragment(), HOME_FRAGMENT);

            //toogle means hamburger icon
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button signInbtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);

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
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(false);
        }else{
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(true);
        }
        invalidateOptionsMenu();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{

            if (currentFragment == HOME_FRAGMENT){
                    currentFragment = -1;
                super.onBackPressed();
            }else {
                if (showCART){
                    mainActivity = null;
                    showCART = false;
                    finish();
                }else{
                    navigationView.getMenu().getItem(0).setChecked(true);
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(),HOME_FRAGMENT);
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT){
            getSupportActionBar().setTitle("");
            getMenuInflater().inflate(R.menu.menu_main, menu);

            MenuItem cartItem = menu.findItem(R.id.my_cart);

                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeIcon =  cartItem.getActionView().findViewById(R.id.badge_icon);
                badgeIcon.setImageResource(R.drawable.ic_cart);
               badgeCount = cartItem.getActionView().findViewById(R.id.badge_count_id);

               if (currentUser != null ){
                   if (DBqueries.cartList.size() == 0) {
                       DBqueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this), false,badgeCount,new TextView(MainActivity.this));
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
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                    }
                });

        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_icon){
            return true;
        }else  if (id == R.id.my_cart){
            gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
            return true;
        }else  if (id == R.id.my_notificatons){
            return true;
        }else  if (id == android.R.id.home){
            if (showCART){
                mainActivity = null;
                showCART = false;
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void gotoFragment(String title,Fragment fragment,int fragmentNo) {
        actionBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();  //method will run again
        setFragment(fragment,fragmentNo);
        if (fragmentNo == CART_FRAGMENT || showCART){
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else{
            params.setScrollFlags(scrollFlags);
        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    MenuItem menuItem;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        menuItem = item;
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });
        if (currentUser != null) {
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    int id = menuItem.getItemId();

                    if (id == R.id.nav_home) {
                        actionBarLogo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
                    } else if (id == R.id.nav_orders) {
                        gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);

                    } else if (id == R.id.nav_wishlist) {
                        gotoFragment("MY WISHLIST", new MyWishListFragment(), WISHLIST_FRAGMENT);
                    } else if (id == R.id.nav_cart) {
                        if (currentUser == null) {
                            signInDialog.show();
                        } else {
                            gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                        }
                    } else if (id == R.id.nav_rewards) {
                        gotoFragment("My Rewards", new RewardsFragment(), REWARDS_FRAGMENT);

                    } else if (id == R.id.nav_account) {
                        gotoFragment("My ACCOUNT", new MyAccountFragment(), ACCOUNT_FRAGMENT);

                    } else if (id == R.id.nav_sign_out) {
                        FirebaseAuth.getInstance().signOut();
                        DBqueries.clearData();
                        Intent registerIntent = new Intent(MainActivity.this,RegisterActivity.class);
                        startActivity(registerIntent);
                        finish();
                    }
                }
            });

            return true;
        }else{
            signInDialog.show();
            return false;
        }

    }
    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment){

            if (fragmentNo == REWARDS_FRAGMENT){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.parseColor("#5B04B1"));
                }
                toolbar.setBackgroundColor(Color.parseColor("#5B04B1"));
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.parseColor("#000000"));
                }
                toolbar.setBackgroundColor(Color.parseColor("#ffffff"));

            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(),fragment);
            fragmentTransaction.commit();
        }
    }
}