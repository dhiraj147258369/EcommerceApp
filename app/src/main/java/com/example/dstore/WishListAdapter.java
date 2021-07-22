package com.example.dstore;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishListAdapter  extends RecyclerView.Adapter<WishListAdapter.viewHolder> {
    private List<WishListModel> wishListModelList;
    private Boolean wishList = false;
    private int lastPosition = -1;

    public WishListAdapter(List<WishListModel> wishListModelList,Boolean wishList) {
        this.wishListModelList = wishListModelList;
        this.wishList = wishList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        String id = wishListModelList.get(position).getProductID();
        String resource = wishListModelList.get(position).getProductImage();
        String title = wishListModelList.get(position).getProductTitle();
        long freecoupens = wishListModelList.get(position).getFreeCoupens();
        String rating = wishListModelList.get(position).getRating();
        long totalRatings = wishListModelList.get(position).getTotalRatings();
        String price = wishListModelList.get(position).getProductPrice();
        String cuttedprice = wishListModelList.get(position).getCuttedPrice();
        Boolean paymentmethod = wishListModelList.get(position).isCOD();
        Boolean inStock = wishListModelList.get(position).isInStock();

        holder.setData(id,resource,title,freecoupens,rating,totalRatings,price,cuttedprice,paymentmethod,position,inStock);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteBtn.setEnabled(false);
//                    if (!ProductDetailsActivity.running_wishlist_query){
//                        ProductDetailsActivity.running_wishlist_query = true;
                DBqueries.removeFormWishList(position,holder.itemView.getContext());
                // }
            }
        });
        if (lastPosition < position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishListModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView coupenIcon;
        private TextView productTitle;
        private TextView freeCoupens;
        private TextView rating;
        private TextView totalRatings;
        private TextView productPrice;
        private TextView cuttedPrice;
        private View price_cut;
        private TextView paymentMethod;
       private ImageView deleteBtn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            coupenIcon = itemView.findViewById(R.id.badge);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupens = itemView.findViewById(R.id.free_coupens);
            rating = itemView.findViewById(R.id.tv_product_rating_mini_view);
            totalRatings = itemView.findViewById(R.id.total_ratings);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            price_cut = itemView.findViewById(R.id.price_cut);
            paymentMethod= itemView.findViewById(R.id.cod_available);
            deleteBtn= itemView.findViewById(R.id.delete);
        }
        private void setData(String id, String resource, String title, long freeCoupensNo, String averageRate, long totalRatingNo, String price, String cuttedPriceValue, Boolean COD, int index, Boolean inStock){
           // productImage.setImageResource(resource);
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place_holder)).into(productImage);
            productTitle.setText(title);
            if (freeCoupensNo != 0 && inStock){

                coupenIcon.setVisibility(View.VISIBLE);
                if(freeCoupensNo == 1){
                    freeCoupens.setText("free "+freeCoupensNo+" coupen");
                }else{
                    freeCoupens.setText("free "+freeCoupensNo+" coupens");
                }
            }else {
                coupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }

            if (inStock) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    productPrice.setTextColor(itemView.getContext().getColor(R.color.black));
                }
                rating.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                cuttedPrice.setVisibility(View.VISIBLE);

                rating.setText(averageRate);
                totalRatings.setText(totalRatingNo + " ratings");
                productPrice.setText(String.valueOf(index));
                cuttedPrice.setText(cuttedPriceValue);
                if (COD) {
                    paymentMethod.setText("COD available");
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    productPrice.setTextColor(itemView.getContext().getColor(R.color.grey));
                }
                rating.setVisibility(View.INVISIBLE);
                productPrice.setText("OUT OF STOCK");
                totalRatings.setVisibility(View.INVISIBLE);
                cuttedPrice.setVisibility(View.INVISIBLE);
            }

            if (wishList){
                deleteBtn.setVisibility(View.VISIBLE);
            }else{
                deleteBtn.setVisibility(View.GONE);
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                    intent.putExtra("IDDD",id);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
