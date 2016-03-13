package com.example.irfan.sales.activity;

import com.example.irfan.sales.object.Product;

/**
 * Created by Lenovo on 1/10/2016.
 */
public class ShoppingCartEntry {
    private Product mProduct;
    private int mQuantity;

    public ShoppingCartEntry(Product product, int quantity){
        mProduct = product;
        mProduct.quantity = quantity;
        mQuantity = quantity;

    }

    public Product getProduct(){
        return mProduct;
    }

    public int getQuantity(){
        return mQuantity;
    }

    public void setQuantity(int quantity){
        mQuantity= quantity;
    }
}
