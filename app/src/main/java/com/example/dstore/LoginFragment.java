package com.example.dstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.example.dstore.RegisterActivity.onResetPasswordFragment;

public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginBtn;
    private TextView skip, forgotPassword,dontHaveAnAccTv;

    private FrameLayout parentFrameLayout;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginEmail = view.findViewById(R.id.login_email_ed);
        loginPassword = view.findViewById(R.id.login_password_ed);
        loginBtn = view.findViewById(R.id.login_btn);
        skip = view.findViewById(R.id.login_skip_tv);
        forgotPassword = view.findViewById(R.id.forgot_password_tv);
        dontHaveAnAccTv = view.findViewById(R.id.dont_have_an_acc_tv);

        parentFrameLayout = getActivity().findViewById(R.id.register_activity_framelayout);
        progressBar = view.findViewById(R.id.register_progressbar);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (disableCloseBtn){
            skip.setVisibility(View.INVISIBLE);
        }else{
            skip.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginEmail.addTextChangedListener(new TextWatcher() {
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
        loginPassword.addTextChangedListener(new TextWatcher() {
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

        dontHaveAnAccTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new RegisterFragment());
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordFragment =true;
                setFragment(new ResetPasswordFragment());
            }
        });
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

    private void checkEmailAndPassword() {
        if (loginEmail.getText().toString().matches(emailpattern)){

            if (loginPassword.length() >= 8){

                progressBar.setVisibility(View.VISIBLE);
                loginBtn.setEnabled(false);
                loginBtn.setTextColor(getResources().getColor(R.color.grey));

                firebaseAuth.signInWithEmailAndPassword(loginEmail.getText().toString(),loginPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                        mainIntent();
                                }else{
                                    progressBar.setVisibility(View.INVISIBLE);
                                    loginBtn.setEnabled(true);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();

        }
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(loginEmail.getText())) {
            if (!TextUtils.isEmpty(loginPassword.getText())) {
                loginBtn.setEnabled(true);
                loginBtn.setTextColor(getResources().getColor(R.color.white));
            } else {

            }
        }     else{  loginBtn.setEnabled(false);
            loginBtn.setTextColor(getResources().getColor(R.color.grey));
            loginBtn.setEnabled(false);
            loginBtn.setTextColor(getResources().getColor(R.color.grey));
        }

    }

    private void setFragment(Fragment fragment) {
        @SuppressLint("UseRequireInsteadOfGet") FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}