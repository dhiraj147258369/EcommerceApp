package com.example.dstore;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.dstore.RegisterActivity.onResetPasswordFragment;

public class RewardsFragment extends Fragment {


    public RewardsFragment() {
        // Required empty public constructor
    }
    private RecyclerView rewardsRecView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        rewardsRecView = view.findViewById(R.id.my_rewards_rec_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rewardsRecView.setLayoutManager(linearLayoutManager);

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

         MyRewardsAdapter rewardsAdapter = new MyRewardsAdapter(rewardModelList,false);
         rewardsRecView.setAdapter(rewardsAdapter);
         rewardsAdapter.notifyDataSetChanged();
        return view;
    }


}