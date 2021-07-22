package com.example.dstore;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.TransitionManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ResetPasswordFragment extends Fragment {

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

        private Button resetBtn;
        private EditText resetEmailEdittext;
        private TextView goBack;

    private FrameLayout parentFrameLayout;

    private FirebaseAuth firebaseAuth;

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        resetBtn = view.findViewById(R.id.resetPasswordBtn);
        resetEmailEdittext = view.findViewById(R.id.reset_email);
        parentFrameLayout = getActivity().findViewById(R.id.register_activity_framelayout);
        goBack = view.findViewById(R.id.go_back_from_reset_fragment);

        firebaseAuth = FirebaseAuth.getInstance();

        emailIconContainer = view.findViewById(R.id.forgot_password_email_icon_container);
        emailIcon = view.findViewById(R.id.email_icon);
        emailIconText = view.findViewById(R.id.email_icon_text);
        progressBar = view.findViewById(R.id.progressBar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resetEmailEdittext.addTextChangedListener(new TextWatcher() {
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

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new LoginFragment());
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(emailIconContainer); //this causes to all elements change their places with animation
                emailIconText.setVisibility(View.GONE);
                emailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                resetBtn.setEnabled(false);
                resetBtn.setTextColor(getResources().getColor(R.color.grey));

                firebaseAuth.sendPasswordResetEmail(resetEmailEdittext.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                              //      Toast.makeText(getActivity(), "Email sent successfully.", Toast.LENGTH_LONG).show();
                                    emailIcon.setVisibility(View.VISIBLE);
                                    emailIconText.setText("Email sent Successfully");
                                    emailIconText.setTextColor(getResources().getColor(R.color.green));
                                    emailIconText.setVisibility(View.VISIBLE);

                                }else{
                                    String error = task.getException().getMessage();
                                    emailIconText.setText(error);
                                    emailIconText.setTextColor(getResources().getColor(R.color.red));
                                    TransitionManager.beginDelayedTransition(emailIconContainer);
                                    emailIconText.setVisibility(View.VISIBLE);
                                    resetBtn.setEnabled(true);
                                    resetBtn.setTextColor(getResources().getColor(R.color.white));
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    private void checkInputs() {
        if (TextUtils.isEmpty(resetEmailEdittext.getText())){
            resetBtn.setEnabled(false);
            resetBtn.setTextColor(getResources().getColor(R.color.grey));
        }else{
            resetBtn.setEnabled(true);
            resetBtn.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

}