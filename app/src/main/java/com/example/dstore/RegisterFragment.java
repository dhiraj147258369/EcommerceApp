package com.example.dstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    private EditText registerEmail ,registerPassword,confirmRegisterPassword,fullName;
    private Button registerBtn;
    private TextView registerskip, alreadyHaveAccount;
    private FrameLayout parentFrameLayout;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    public static boolean disableCloseBtn = false;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        registerEmail = view.findViewById(R.id.register_email_ed);
        registerPassword = view.findViewById(R.id.register_password_ed);
        confirmRegisterPassword = view.findViewById(R.id.confirm_password_ed);
        fullName = view.findViewById(R.id.register_name);
        registerBtn = view.findViewById(R.id.register_btn);
        registerskip = view.findViewById(R.id.register_skip_tv);
        alreadyHaveAccount = view.findViewById(R.id.already_acc_tv);
        progressBar = view.findViewById(R.id.register_progressbar);
        parentFrameLayout = getActivity().findViewById(R.id.register_activity_framelayout);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (disableCloseBtn){
            registerskip.setVisibility(View.INVISIBLE);
        }else{
            registerskip.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       registerEmail.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                checkEmailAndPassword();
           }
       });
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new LoginFragment());
            }
        });
    }
    private void checkEmailAndPassword() {

        if (registerEmail.getText().toString().matches(emailpattern)){
                if (registerPassword.getText().toString().equals(confirmRegisterPassword.getText().toString())){

                    progressBar.setVisibility(View.VISIBLE);
                    registerBtn.setEnabled(false);
                    registerBtn.setTextColor(getResources().getColor(R.color.grey));

                    firebaseAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(),registerPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        Map<String,Object> userdata = new HashMap<>();
                                        userdata.put("fullname",fullName.getText().toString());

                                        firebaseFirestore.collection("USERS")
                                                .document(firebaseAuth.getUid())
                                                .set(userdata)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                             CollectionReference userDataReference = firebaseFirestore.collection("USERS")
                                                     .document(firebaseAuth.getUid())
                                                     .collection("USER_DATA");

                                                            Map<String,Object> wishListMap = new HashMap<>();
                                                            wishListMap.put("list_size",(long)0);

                                                            Map<String,Object> ratingsMap = new HashMap<>();
                                                            ratingsMap.put("list_size",(long)0);

                                                            Map<String,Object> cartMap = new HashMap<>();
                                                            cartMap.put("list_size",(long)0);

                                                            Map<String,Object> myAddressesMap = new HashMap<>();
                                                            myAddressesMap.put("list_size",(long)0);

                                                            List<String> documentNames = new ArrayList<>();
                                                            documentNames.add("MY_WISHLIST");
                                                            documentNames.add("MY_RATINGS");
                                                            documentNames.add("MY_CART");
                                                            documentNames.add("MY_ADDRESSES");

                                                            List<Map<String,Object>> documentFields = new ArrayList<>();
                                                            documentFields.add(wishListMap);
                                                            documentFields.add(ratingsMap);
                                                            documentFields.add(cartMap);
                                                            documentFields.add(myAddressesMap);

                                                            for (int x = 0 ; x < documentNames.size() ; x++){
                                                                int finalX = x;
                                                                userDataReference.document(documentNames.get(x))
                                                                        .set(documentFields.get(x))
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                               if (task.isSuccessful()){
                                                                                   if (finalX == documentNames.size()-1){
                                                                                       mainIntent();
                                                                                   }
                                                                            }else{
                                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                                registerBtn.setEnabled(true);
                                                                                registerBtn.setTextColor(getResources().getColor(R.color.white));
                                                                                String error = task.getException().getMessage();
                                                                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                            }

                                                        }else{
                                                                     String error = task.getException().getMessage();
                                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }else{
                                        progressBar.setVisibility(View.INVISIBLE);
                                        registerBtn.setEnabled(true);
                                        registerBtn.setTextColor(getResources().getColor(R.color.white));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    confirmRegisterPassword.setError("Password doesnt matched!");
                }
        }else{
            registerEmail.setError("Invalid Email");

        }
    }
    private void mainIntent() {
        if (disableCloseBtn){
            disableCloseBtn = false;
        }else{
            Intent mainIntent = new Intent(getActivity(),MainActivity.class);
            startActivity(mainIntent);
        }
        getActivity().finish();
    }
    private void checkInputs() {

        if (!TextUtils.isEmpty(registerEmail.getText()))
        {
            if (!TextUtils.isEmpty(fullName.getText()))
            {
                if (!TextUtils.isEmpty(registerPassword.getText()) && registerPassword.length() >= 8)
                {
                    if (!TextUtils.isEmpty(confirmRegisterPassword.getText()))
                    {
                        registerBtn.setEnabled(true);
                        registerBtn.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        registerBtn.setEnabled(false);
                        registerBtn.setTextColor(getResources().getColor(R.color.grey));
                    }
                }else{
                    registerBtn.setEnabled(false);
                    registerBtn.setTextColor(getResources().getColor(R.color.grey));
                }
            }else{
                registerBtn.setEnabled(false);
                registerBtn.setTextColor(getResources().getColor(R.color.grey));
            }
        }else{
            registerBtn.setEnabled(false);
            registerBtn.setTextColor(getResources().getColor(R.color.grey));
        }
    }
    private void setFragment(Fragment fragment) {
        @SuppressLint("UseRequireInsteadOfGet") FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}