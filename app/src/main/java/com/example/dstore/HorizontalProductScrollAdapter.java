package com.example.dstore;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.viewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent,false);
        return new HorizontalProductScrollAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.viewHolder holder, int position) {
        String id =  horizontalProductScrollModelList.get(position).getProductID();
      String resource = horizontalProductScrollModelList.get(position).getProductImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String desc = horizontalProductScrollModelList.get(position).getProductDesc();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetailIntent = new Intent( holder.itemView.getContext(),ProductDetailsActivity.class);
                productDetailIntent.putExtra("IDDD",id);
                holder.itemView.getContext().startActivity(productDetailIntent);
            }
        });
       holder.setProductData(position,resource,title,desc,price);
    }

    @Override
    public int getItemCount() {
        if (horizontalProductScrollModelList.size() > 8){
            return 8;
        }else{
            return horizontalProductScrollModelList.size();
        }

    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView productDesc;
        private TextView productPrice;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            productImage =itemView.findViewById(R.id.horizontal_product_img);
            productTitle =itemView.findViewById(R.id.horizontal_product_title);
            productDesc =itemView.findViewById(R.id.horizontal_product_desc);
            productPrice =itemView.findViewById(R.id.horizontal_product_price);


        }

        private void setProductData(int position,String resource,String title,String description,String price){
          //  productImage.setImageResource(resource);
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place_holder)).into(productImage);
            productPrice.setText(price);
            productTitle.setText(title);
            productDesc.setText(description);

            if (!title.equals("")){

            }
        }

    }
}
