package com.example.irfan.sales.helper;

import android.content.res.Resources;

import com.example.irfan.sales.object.Customer;
import com.example.irfan.sales.object.Product;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Vector;

/**
 * Created by Lenovo on 2/21/2016.
 */
public class CustomerHelper {
    public static final String CUST_INDEX = "CUST_INDEX";
    private static List<Customer> customers;
    public static List<Customer> getList(Resources res) {
        if (customers == null) {
            customers = new Vector<Customer>();
            customers.add(new Customer("Shop 1","pajajaran","123",100000,new LatLng(-6.594,106.805)));
            customers.add(new Customer("Shop 2","siliwangi","555",100000,new LatLng(-6.624,106.818)));
            customers.add(new Customer("Shop 3","kemang","999",100000,new LatLng(-6.487,106.742)));
        }
        return customers;
    }
}
