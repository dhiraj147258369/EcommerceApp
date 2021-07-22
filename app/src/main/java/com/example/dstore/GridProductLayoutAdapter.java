package com.example.dstore;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,null);
            ImageView productImage = view.findViewById(R.id.horizontal_product_img);
            TextView productTitle = view.findViewById(R.id.horizontal_product_title);
            TextView productDesc = view.findViewById(R.id.horizontal_product_desc);
            TextView productPrice = view.findViewById(R.id.horizontal_product_price);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//              Intent intent = new Intent(parent.getContext(),ProductActivity.class);
//            //  productIntent.putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(position).getProductID());
//                    intent.putExtra("IDD",horizontalProductScrollModelList.get(position).getProductID());
//                    parent.getContext().startActivity(intent);
//
//                }
//            });
         //   productImage.setImageResource(horizontalProductScrollModelList.get(position).getProductImage());

            Glide.with(parent.getContext()).load(horizontalProductScrollModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place_holder)).into(productImage);
            productTitle.setText(horizontalProductScrollModelList.get(position).getProductTitle());
            productDesc.setText(horizontalProductScrollModelList.get(position).getProductDesc());
            productPrice.setText("Rs."+horizontalProductScrollModelList.get(position).getProductPrice()+"/-");

        }else{
            view = convertView;
        }
        return view;
    }
    }

