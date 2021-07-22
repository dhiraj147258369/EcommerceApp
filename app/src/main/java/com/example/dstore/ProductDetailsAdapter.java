package com.example.dstore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ProductDetailsAdapter extends FragmentPagerAdapter {

    private int totaltabs;
    private String productDescription;
    private String prodcutOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductDetailsAdapter(FragmentManager fm, int totaltabs, String productDescription, String prodcutOtherDetails, List<ProductSpecificationModel> productSpecificationModelList) {
        super(fm);
        this.totaltabs =totaltabs;
        this.productDescription = productDescription;
        this.prodcutOtherDetails = prodcutOtherDetails;
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ProductDescriptionFragment productDescriptionFragment1 = new ProductDescriptionFragment();
                productDescriptionFragment1.body = productDescription;
                return  productDescriptionFragment1;
            case 1:
                ProductSpecificationFragment productSpecificationFragment =new ProductSpecificationFragment();
                productSpecificationFragment.productSpecificationModelList = productSpecificationModelList;
                return productSpecificationFragment;
            case 2:
                ProductDescriptionFragment productDescriptionFragment2 = new ProductDescriptionFragment();
                productDescriptionFragment2.body = prodcutOtherDetails;
                return  productDescriptionFragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totaltabs;
    }
}
