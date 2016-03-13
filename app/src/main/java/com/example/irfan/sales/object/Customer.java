package com.example.irfan.sales.object;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Lenovo on 2/21/2016.
 */
public class Customer {
    public String name;
    public String address;
    public String phone;
    public LatLng location;
    public LatLng visit;


    public String barcode;
    public double limit;


    public Customer(String name,String address,String phone, double limit, LatLng location){
        this.name = name;
        this.address= address;
        this.phone= phone;
        this.limit = limit;
        this.location = location;
    }
}
