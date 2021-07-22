package com.example.dstore;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.function.LongFunction;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmountt;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList,TextView cartTotalAmountt,boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmountt = cartTotalAmountt;
        this.showDeleteBtn=showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;

            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       switch (viewType){
           case CartItemModel.CART_ITEM:
               View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
               return new CartItemViewHolder(view1);
           case CartItemModel.TOTAL_AMOUNT:
               View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout,parent,false);
               return new CartTotalAmountViewHolder(view2);
           default:
               return null;
       }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String id = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freecoupens = cartItemModelList.get(position).getFreeCoupens();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();

                ((CartItemViewHolder)holder).setItemDetails(id,resource,title,freecoupens,productPrice,cuttedPrice,offersApplied,position,inStock);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems =0;
                int totalItemPrice = 0;
                int totalAmount = 0;
                String deliveryPrice;
                int savedAmount = 0;
                for (int x =0; x< cartItemModelList.size() ;x++){

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()){
                        totalItems++;

                        totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice());
                    }
                }
                if (totalItemPrice > 500){
                    deliveryPrice= "FREE";
                    totalAmount = totalAmount + totalItemPrice;
                }else{
                    deliveryPrice = "60";
                    totalAmount = totalItemPrice +60;
                }

           //     String totalItems = cartItemModelList.get(position).getTotalItems();
            //    String totalItemPrice = cartItemModelList.get(position).getTotalItemPrice();
           //     String deliveryPrice = cartItemModelList.get(position).getDeliveyPrice();
          //     String totalAmount = cartItemModelList.get(position).getTotalAmount();
           //     String savedAmount = cartItemModelList.get(position).getSavedAmt();
                ((CartTotalAmountViewHolder)holder).totalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount);
                break;
            default:
                return;
        }

        if (lastPosition < position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private ImageView freeCoupenIcon;
        private TextView productTite;
        private TextView freeCOupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView coupensApplied;
        private TextView productQuantity;
        private LinearLayout deleteBtn;
        private LinearLayout coupenRedemptionLayout;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            freeCoupenIcon = itemView.findViewById(R.id.badge);
            productTite = itemView.findViewById(R.id.product_title);
            freeCOupens = itemView.findViewById(R.id.free_coupens);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            coupensApplied = itemView.findViewById(R.id.coupens_applied);
            productQuantity = itemView.findViewById(R.id.qty);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
            coupenRedemptionLayout = itemView.findViewById(R.id.coupen_redemption_layout);

        }

        private void setItemDetails(String id, String resource, String title, Long freeCoupenNo, String productPriceText, String cuttedPricetxt, Long offersAppliedNo,int position,boolean instock){
         //   productImage.setImageResource(resource);
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place_holder)).into(productImage);
            productTite.setText(title);

            if (instock){
                if (freeCoupenNo > 0){
                    freeCoupenIcon.setVisibility(View.VISIBLE);
                    freeCOupens.setVisibility(View.VISIBLE);
                    if (freeCoupenNo == 1){
                        freeCOupens.setText("free" + freeCoupenNo + "Coupen");
                    }
                    else {
                        freeCOupens.setText("free" + freeCoupenNo + "Coupens");
                    }
                }else{
                    freeCoupenIcon.setVisibility(View.INVISIBLE);
                    freeCOupens.setVisibility(View.INVISIBLE);
                }

                productPrice.setText("Rs."+productPriceText+"/-");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                cuttedPrice.setText("Rs."+cuttedPricetxt+"/-");
                coupenRedemptionLayout.setVisibility(View.VISIBLE);

                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog qtyDialog = new Dialog(itemView.getContext());
                        qtyDialog.setContentView(R.layout.quantity_dialog);
                        qtyDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        qtyDialog.setCancelable(false);
                        EditText qtyNumber = qtyDialog.findViewById(R.id.qty_ed);
                        Button cancelBtn = qtyDialog.findViewById(R.id.cancel_btn);
                        Button okBtn = qtyDialog.findViewById(R.id.ok_btn);

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                qtyDialog.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                productQuantity.setText("Qty: "+qtyNumber.getText());
                                qtyDialog.dismiss();
                            }
                        });
                        qtyDialog.show();
                    }
                });
                if (offersAppliedNo > 0){
                    offersApplied.setVisibility(View.VISIBLE);
                    offersApplied.setText(offersAppliedNo +"Offers applied");
                }else{
                    offersApplied.setVisibility(View.INVISIBLE);
                }
            }else{
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                cuttedPrice.setText("");
                coupenRedemptionLayout.setVisibility(View.GONE);
                freeCOupens.setVisibility(View.INVISIBLE);
                coupensApplied.setVisibility(View.GONE);
                productQuantity.setVisibility(View.INVISIBLE);
                offersApplied.setVisibility(View.GONE);
                freeCoupenIcon.setVisibility(View.INVISIBLE);
            }



            if (showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }else{
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailsActivity.running_cart_query){
                        ProductDetailsActivity.running_cart_query = true;
                        DBqueries.removeFromCart(position,itemView.getContext(),cartTotalAmountt);
                    }
                }
            });
        }
    }


    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;

        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.price_txt);
            totalItemPrice = itemView.findViewById(R.id.price_amt);
            deliveryPrice = itemView.findViewById(R.id.delivery_amt);
            totalAmount = itemView.findViewById(R.id.total_amt);
            savedAmount = itemView.findViewById(R.id.saved_amt);

        }
        private void totalAmount(int totalItemtxt,int totalItemPriceTxt,String delivryPriceTxt,int totalAmounttxt,int savedAmt){
            totalItems.setText("Price( "+totalItemtxt+" items)");
            totalItemPrice.setText("Rs."+totalItemPriceTxt+"/-");

            if (delivryPriceTxt.equals("FREE")){
                deliveryPrice.setText(delivryPriceTxt);
            }else{
                deliveryPrice.setText("Rs."+delivryPriceTxt+"/-");
            }
            totalAmount.setText("Rs."+totalAmounttxt+"/-");
            cartTotalAmountt.setText("Rs."+totalAmounttxt+"/-");
            savedAmount.setText("Your saved Rs."+savedAmt+"/-");

            LinearLayout parent =(LinearLayout) cartTotalAmountt.getParent();

            if (totalItemPriceTxt == 0){
                DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() -1);
                 parent.setVisibility(View.GONE);
            }else{
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

}
