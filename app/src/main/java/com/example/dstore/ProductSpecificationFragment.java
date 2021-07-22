package com.example.dstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecificationFragment extends Fragment {


    public ProductSpecificationFragment() {
        // Required empty public constructor
    }

    private RecyclerView productSpecificationRecView;
    public List<ProductSpecificationModel> productSpecificationModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_specification, container, false);

        productSpecificationRecView = view.findViewById(R.id.product_specificaton_rec_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        productSpecificationRecView.setLayoutManager(linearLayoutManager);

//        productSpecificationModelList = new ArrayList<>();
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"General"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"Display"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        ProductspecificationAdapter productspecificationAdapter = new ProductspecificationAdapter(productSpecificationModelList);
        productSpecificationRecView.setAdapter(productspecificationAdapter);
        productspecificationAdapter.notifyDataSetChanged();
        return view;
    }



}