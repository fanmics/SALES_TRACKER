package com.example.irfan.sales.object;

import android.graphics.drawable.Drawable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Irfan on 1/2/2016.
 */
public class Product {

    public String title;
//    public Drawable productImage;
    public String code;
    public String description;
    public double   price;
    public int stock;
    public int quantity;
    public int discount;
    public double discountPrice;
    public boolean selected;

//    public Product(String title, Drawable  productImage,String description, double price, int stock, int discount)
    public Product(String title, String code,String description, double price, int stock, int discount)
    {
        this.title = title;
//        this.productImage= productImage;
        this.code =  code;
        this.description= description;
        this.price= price;
        this.stock = stock;
        this.discount = discount;
        this.discountPrice= (discount/100) * price;
    }

    public String CurrencyFormat(double value){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(value);
    }
}
