package com.example.irfan.sales.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.irfan.sales.R;
import com.example.irfan.sales.object.Customer;

import java.util.List;

/**
 * Created by Lenovo on 2/21/2016.
 */
public class CustomerAdapter extends BaseAdapter {
    private List<Customer> mCustomers;
    private LayoutInflater mLayoutInflater;

    public CustomerAdapter(List<Customer> customers, LayoutInflater inflater){
        this.mCustomers = customers;
        this.mLayoutInflater= inflater;
    }
    @Override
    public int getCount() {
        return mCustomers.size();
    }

    @Override
    public Object getItem(int position) {
        return mCustomers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CustomerItem item;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.customer, null);
            item = new CustomerItem();
//            item.productImageView = (ImageView) convertView.findViewById(R.id.ImageViewItem);
            item.txTitle = (TextView) convertView.findViewById(R.id.TextViewCustomer);
            convertView.setTag(item);
        } else {
            item = (CustomerItem) convertView.getTag();
        }
        final Customer curCustomer = mCustomers.get(position);
//        item.productImageView.setImageDrawable(curProduct.productImage);
        item.txTitle.setText(curCustomer.name);

        return convertView;
    }
    private class CustomerItem {
        //        ImageView productImageView;
        TextView txTitle;
//        Button productButton;
    }
}