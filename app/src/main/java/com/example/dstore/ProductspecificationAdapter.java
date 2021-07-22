package com.example.dstore;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductspecificationAdapter extends RecyclerView.Adapter<ProductspecificationAdapter.viewHolder> {

    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductspecificationAdapter(List<ProductSpecificationModel> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModelList.get(position).getType()){
            case 0:
                return ProductSpecificationModel.SPECIFICATION_TITLE;
            case 1:
                return ProductSpecificationModel.SPECIFICATION_BODY;

            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public ProductspecificationAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                   TextView title = new TextView(parent.getContext());
                   title.setTypeface(null, Typeface.BOLD);
                   title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16,parent.getContext()),setDp(16,parent.getContext()),setDp(16,parent.getContext()),setDp(16,parent.getContext()));
              title.setLayoutParams(layoutParams);

              return new viewHolder(title);

                case ProductSpecificationModel.SPECIFICATION_BODY:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specification_item_layout,parent,false);
                    return new viewHolder(view);

            default:
                return  null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ProductspecificationAdapter.viewHolder holder, int position) {

        switch (productSpecificationModelList.get(position).getType()){
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                 holder.setTitle(productSpecificationModelList.get(position).getTitle());
                 break;
                case ProductSpecificationModel.SPECIFICATION_BODY:
                    String featuretitle = productSpecificationModelList.get(position).getFeatureName();
                    String featuredetail = productSpecificationModelList.get(position).getFeatureValue();
                    holder.setFeature(featuretitle,featuredetail);
                    break;
            default:
                return;
        }

    }


    @Override
    public int getItemCount() {
        return productSpecificationModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView featureName;
        private TextView featureValue;
        private TextView title;

        public viewHolder(@NonNull View itemView) {
            super(itemView);


        }

        private void setTitle(String titleText){
            title = (TextView) itemView;
            title.setText(titleText);
        }

        private void setFeature(String title,String featureDetail){
            featureName = itemView.findViewById(R.id.feature_name);
            featureValue = itemView.findViewById(R.id.feature_value);
            featureName.setText(title);
            featureValue.setText(featureDetail);
        }
    }


    private int setDp(int dp, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
}
