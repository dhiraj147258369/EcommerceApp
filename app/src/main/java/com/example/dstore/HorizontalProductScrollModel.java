package com.example.dstore;

public class HorizontalProductScrollModel {


    private String productID;
    private String productImage;
    private String productTitle;
    private String productDesc;
    private String productPrice;


    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public HorizontalProductScrollModel(String productID, String productImage, String productTitle, String productDesc, String productPrice) {
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productDesc = productDesc;
        this.productPrice = productPrice;

    }



}
