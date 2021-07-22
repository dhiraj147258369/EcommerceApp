package com.example.dstore;

import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.viewHolder> {

    private List<RewardModel> rewardModelList;
   private Boolean useMiniLayout = false;

    public MyRewardsAdapter(List<RewardModel> rewardModelList,Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
       if (useMiniLayout){
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_litem_layout, parent, false);
       }else{
          view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout, parent, false);
       }

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
       String title = rewardModelList.get(position).getTitle();
        String expiryDate = rewardModelList.get(position).getExpiryDate();
        String coupenBody = rewardModelList.get(position).getCoupenBody();

        holder.setData(title,expiryDate,coupenBody);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.coupen_title);
            coupenExpiryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body);
        }

         private void setData(String title,String date,String body){
            coupenTitle.setText(title);
            coupenExpiryDate.setText(date);
            coupenBody.setText(body);

            if (useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetailsActivity.coupenTitle.setText(title);
                        ProductDetailsActivity.coupenExpiryDate.setText(date);
                        ProductDetailsActivity.coupenBody.setText(body);

                    }
                });
            }
         }
    }
}
