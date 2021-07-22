package com.example.dstore;

import java.util.PropertyResourceBundle;

public class WishListModel {

    private String productID;
    private String productImage;
    private String productTitle;
    private String productPrice;
    private String rating;
    private long totalRatings;
    private long freeCoupens;
    private String cuttedPrice;
    private boolean COD;
    private boolean inStock;

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

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
    public String getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public long getTotalRatings() {
        return totalRatings;
    }
    public void setTotalRatings(long totalRatings) {
        this.totalRatings = totalRatings;
    }
    public long getFreeCoupens() {
        return freeCoupens;
    }
    public void setFreeCoupens(long freeCoupens) {
        this.freeCoupens = freeCoupens;
    }
    public String getCuttedPrice() {
        return cuttedPrice;
    }
    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }
    public WishListModel(String productID,String productImage, String productTitle, String productPrice, String rating, long totalRatings, long freeCoupens, String cuttedPrice, Boolean COD,Boolean inStock) {
       this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.rating = rating;
        this.totalRatings = totalRatings;
        this.freeCoupens = freeCoupens;
        this.cuttedPrice = cuttedPrice;
        this.COD = COD;
        this.inStock = inStock;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
