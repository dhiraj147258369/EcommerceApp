package com.example.dstore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {

    private List<CategoryModel> categoryModelList;
    private Context context;

    public CategoryAdapter(List<CategoryModel> categoryModelList,Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewHolder holder, int position) {
        String icon = categoryModelList.get(position).getCategoryIconLink();
        String name = categoryModelList.get(position).getCategoryName();
        holder.setCategory(name,icon);

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private ImageView categoryIcon;
        private TextView categoryName;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.category_item_img);
            categoryName = itemView.findViewById(R.id.category_item_name);

        }

        private void setCategoryIcon(){
            //// todo: set category icon here
        }
        private void setCategory(String name,String icon){
            //// todo: set category icon here
            categoryName.setText(name);
            if (!icon.equals("null")) {
                Glide.with(context).load(icon).apply(new RequestOptions().placeholder(R.drawable.place_holder)).into(categoryIcon);
            }else{
                categoryIcon.setImageResource(R.drawable.place_holder);
            }

            if (!name.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), CategoryActivity.class);
                        intent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
