package com.example.dstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private FirebaseFirestore firebaseFirestore;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PRODUCT DETAIL");
        firebaseFirestore =FirebaseFirestore.getInstance();

        title = findViewById(R.id.titleee);

//        firebaseFirestore.collection("PRODUCT")
//                .document(getIntent().getStringExtra("IDD"))
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()){
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            String stringg = documentSnapshot.get("pr_title").toString();
//                            title.setText(stringg);
//                        }else{
//                          String error = task.getException().getMessage();
//                            Toast.makeText(ProductActivity.this,error,Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

    }
}