package com.example.dstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressesActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private EditText city;
    private EditText locality;
    private EditText flatNo;
    private EditText pincode;
    private EditText landmark;
    private EditText name;
    private EditText mobileNo;
    private EditText altMobileNo;

    private String selectedState;
    private String [] stateList ;
    private AppCompatSpinner stateSpinner;
    private Button saveBtn;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_addresses);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MY ORDERS");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flatNo = findViewById(R.id.flat_no);
        pincode = findViewById(R.id.pincode);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.number);
        altMobileNo = findViewById(R.id.alt_number);
        stateSpinner = findViewById(R.id.state_spinner);

        //////LOADING DIALOG//////
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //////LOADING DIALOG//////
        stateList = getResources().getStringArray(R.array.india_states);

        ArrayAdapter spinnerAddapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.india_states));
        spinnerAddapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(spinnerAddapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saveBtn = findViewById(R.id.save_btn);
         saveBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if (!TextUtils.isEmpty(city.getText())){
                     if (!TextUtils.isEmpty(locality.getText())){
                         if (!TextUtils.isEmpty(flatNo.getText())){
                             if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 6){
                                 if (!TextUtils.isEmpty(name.getText())){
                                     if (!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() ==  10){

                                         loadingDialog.show();

                                         String fullAddress =flatNo.getText().toString()+" "+locality.getText().toString()+" "+landmark.getText().toString()
                                                              +" "+city.getText().toString()+" "+selectedState;
                                         Map<String,Object> addAddress = new HashMap<>();
                                          addAddress.put("list_size",(long)DBqueries.addressesModelList.size()+1);
                                          if (TextUtils.isEmpty(altMobileNo.getText())){
                                              addAddress.put("fullname_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),name.getText().toString()+" - "+mobileNo.getText().toString());
                                          }else{
                                              addAddress.put("fullname_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),name.getText().toString()+" - "+mobileNo.getText().toString()+" or "+altMobileNo.getText().toString());
                                          }
                                         addAddress.put("address_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),fullAddress);
                                         addAddress.put("pincode_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),pincode.getText().toString());
                                         addAddress.put("selected_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),true);
                                         if (DBqueries.addressesModelList.size() > 0) {
                                             addAddress.put("selected_" + (DBqueries.selectedAddress + 1), false);
                                         }

                                         FirebaseFirestore.getInstance().collection("USERS")
                                                 .document(FirebaseAuth.getInstance().getUid())
                                                 .collection("USER_DATA")
                                                 .document("MY_ADDRESSES")
                                                 .update(addAddress)
                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         if (task.isSuccessful()) {
                                                             if (DBqueries.addressesModelList.size() > 0)
                                                             {
                                                                 DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                             }

                                                             if (TextUtils.isEmpty(altMobileNo.getText())){
                                                                 DBqueries.addressesModelList.add(new AddressesModel(
                                                                         name.getText().toString()+" - "+mobileNo.getText().toString()
                                                                         ,fullAddress
                                                                         ,pincode.getText().toString()
                                                                         ,true
                                                                 ));
                                                             }else{
                                                                 DBqueries.addressesModelList.add(new AddressesModel(
                                                                         name.getText().toString()+" - "+mobileNo.getText().toString()+" or "+altMobileNo.getText().toString()
                                                                         ,fullAddress
                                                                         ,pincode.getText().toString()
                                                                         ,true
                                                                 ));
                                                             }

                                                             if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                                 Intent intent = new Intent(AddAddressesActivity.this, DeliveryActivity.class);
                                                                 startActivity(intent);
                                                             }else{
                                                                 MyAddressesActivity.refreshItem(DBqueries.selectedAddress,DBqueries.addressesModelList.size()-1);
                                                             }
                                                             DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                                             finish();

                                                         } else {
                                                             String error = task.getException().getMessage();
                                                             Toast.makeText(AddAddressesActivity.this, error, Toast.LENGTH_SHORT).show();
                                                         }
                                                         loadingDialog.dismiss();
                                                     }
                                                 });
                                     }
                                     else{
                                         mobileNo.requestFocus();
                                         Toast.makeText(AddAddressesActivity.this, "Please Provide Valid Mobile Number", Toast.LENGTH_SHORT).show();

                                     }
                                 }
                                 else{
                                     name.setSelected(true);

                                 }
                             }
                             else{
                                 pincode.requestFocus();
                                 Toast.makeText(AddAddressesActivity.this, "Please Provide Valid pincode", Toast.LENGTH_SHORT).show();

                             }
                         }
                         else{
                             flatNo.requestFocus();

                         }
                     }
                     else{
                         locality.requestFocus();

                     }
                     }
                 else{
                     city.requestFocus();
                 }

             }
         });

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
}