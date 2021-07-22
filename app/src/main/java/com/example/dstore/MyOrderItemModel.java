package com.example.dstore;

public class MyOrderItemModel {

    private int productImage;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private int rating;
    private String productTitle;

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getDeliveryyStatus() {
        return deliveryyStatus;
    }

    public void setDeliveryyStatus(String deliveryyStatus) {
        this.deliveryyStatus = deliveryyStatus;
    }

    public MyOrderItemModel(int productImage,int rating, String productTitle, String deliveryyStatus) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.deliveryyStatus = deliveryyStatus;
        this.rating = rating;
    }

    private String deliveryyStatus;

}
