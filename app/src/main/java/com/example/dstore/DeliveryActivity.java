package com.example.dstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView deliveryRecView;
    private Button changeOrAddNewAddressBtn;
    private TextView totalAmount;

    private TextView fullname;
    private TextView address;
    private TextView pinocde;

    private LinearLayout continueBtn;

    public static List<CartItemModel> cartItemModelList;
    public static final int SELECT_ADDRESS = 0;
    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;

    private ImageView googlePay;
    private Context context;
    private Uri uri;
    private String status;

    private LinearLayout order_confirmation_layout;
    private TextView orderId;
    private Button continueShoppingBtn;

    private final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private final int GOOGLE_PAY_REQUEST_CODE = 123;

    private boolean successResponse = false;

    public static boolean fromcart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DELIVERY");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalAmount = findViewById(R.id.total_price);

        order_confirmation_layout = findViewById(R.id.order_confirmation_layout);
        orderId = findViewById(R.id.order_id);
        continueShoppingBtn = findViewById(R.id.contiue_shopping);

        //////LOADING DIALOG//////
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //////LOADING DIALOG//////

        //////LOADING DIALOG//////
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        loadingDialog.setCancelable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        }
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //////LOADING DIALOG//////

        fullname = findViewById(R.id.name);
        address = findViewById(R.id.address);
        pinocde = findViewById(R.id.pincode);

        deliveryRecView = findViewById(R.id.delivery_rec_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecView.setLayoutManager(linearLayoutManager);

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList,totalAmount,false);
        deliveryRecView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeOrAddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);
        changeOrAddNewAddressBtn.setVisibility(View.VISIBLE);

        changeOrAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryActivity.this,MyAddressesActivity.class);
                intent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(intent);
            }
        });

        fullname.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname());
        address.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pinocde.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

        continueBtn = findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.show();

            }
        });
        googlePay = paymentMethodDialog.findViewById(R.id.google_pay_btn);
        googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
//                loadingDialog.show();

                uri = googlePayUri();

                if (isAppInstalled(context,GOOGLE_PAY_PACKAGE_NAME)){
                    if (isInternetConnectionAvailable(context)){
                        startIntent(GOOGLE_PAY_PACKAGE_NAME);
                    }else{
                        Toast.makeText(DeliveryActivity.this,"Internet not Available",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DeliveryActivity.this,"App is not installed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static Uri googlePayUri(){
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "9172437675@okbizaxis")
                .appendQueryParameter("pn", "D Mehattar")
                //     .appendQueryParameter("mc", "BCR2DN6TWPQPR73M")
                .appendQueryParameter("tr", "123456789")
                .appendQueryParameter("tn", "TRIAL")
                .appendQueryParameter("am", "1")
                .appendQueryParameter("cu", "INR")
                //      .appendQueryParameter("url", "your-transaction-url")
                .build();
    }

    private void startIntent(String packageName){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(packageName);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }
    public boolean isAppInstalled(Context context, String packageName){

        try {
            getPackageManager().getApplicationInfo(packageName,0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    private boolean isInternetConnectionAvailable(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
            status = data.getStringExtra("Status").toLowerCase();
        }
        if ((RESULT_OK == resultCode) && status.equals("Success")){

            successResponse = true;
            loadingDialog.show();

            if (MainActivity.mainActivity != null){
                MainActivity.mainActivity.finish();
                MainActivity.mainActivity = null;
                MainActivity.showCART = false;
            }

            if (ProductDetailsActivity.productDetailsActivity != null){
                ProductDetailsActivity.productDetailsActivity.finish();
                ProductDetailsActivity.productDetailsActivity = null;
            }

            if (fromcart){

                long cartlistSize = 0;
                List<Integer> indexList = new ArrayList<>();

                Map<String,Object> updateCartList = new HashMap<>();

                for (int x = 0; x < DBqueries.cartItemModelList.size();x++){
                    if (!cartItemModelList.get(x).isInStock()){
                        updateCartList.put("product_ID_"+x,cartItemModelList.get(x).getProductID());
                        cartlistSize++;
                    }else{
                        indexList.add(x);
                    }
                }
                updateCartList.put("list_size",cartlistSize);
                FirebaseFirestore.getInstance().collection("USERS")
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("USER_DATA")
                        .document("MY_WISHLIST")
                        .set(updateCartList)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    for(int x = 0 ; x < indexList.size() ; x++){
                                        DBqueries.cartList.remove(indexList.get(x));
                                        DBqueries.cartItemModelList.remove(indexList.get(x));
                                    }
                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(DeliveryActivity.this,error,Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
            }

            Toast.makeText(DeliveryActivity.this,"Transaction is SuccessFull",Toast.LENGTH_SHORT).show();
            order_confirmation_layout.setVisibility(View.VISIBLE);
            orderId.setText("ORDER ID ZXC123SDF345");

            continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            
        }else{
            Toast.makeText(DeliveryActivity.this,"Transaction is failed or cancelled",Toast.LENGTH_SHORT).show();
            order_confirmation_layout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fullname.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname());
        address.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pinocde.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (successResponse){
            finish();
            return;
        }
        super.onBackPressed();
    }
}