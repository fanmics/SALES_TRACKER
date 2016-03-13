package com.example.irfan.sales.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.irfan.sales.R;
import com.example.irfan.sales.helper.CustomerHelper;
import com.example.irfan.sales.object.Customer;
import com.example.irfan.sales.object.Product;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lenovo on 2/21/2016.
 */
public class CustomerDetailsActivity extends ActionBarActivity {
    public Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerdetails);

        int custIndex= getIntent().getExtras().getInt(CustomerHelper.CUST_INDEX);

        List<Customer> customers= CustomerHelper.getList(getResources());
        final Customer selectedCustomer= customers.get(custIndex);
        TextView custTitleTextView= (TextView) findViewById(R.id.TextViewCustomerTitle);
        custTitleTextView.setText(selectedCustomer.name);

        TextView custAddressTextView= (TextView) findViewById(R.id.TextViewAddress);
        custAddressTextView.setText("Address : " + selectedCustomer.address);

        TextView custPhoneTextView= (TextView) findViewById(R.id.TextViewPhone);
        custPhoneTextView.setText("Phone : " + selectedCustomer.phone);

        TextView custLimitTextView= (TextView) findViewById(R.id.TextViewLimit);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(2);
        custLimitTextView.setText("Limit : " + formatter.format( selectedCustomer.limit));

        Button btnShowMap1 = (Button) findViewById(R.id.ButtonViewMap1);
        btnShowMap1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent(CustomerDetailsActivity.this, MapsActivity.class);
                intent.putExtra("page", "Detail");
                intent.putExtra("lat",selectedCustomer.location.latitude);
                intent.putExtra("longi",selectedCustomer.location.longitude);
                startActivity(intent);

            }
        });
        Button btnShowMap2 = (Button) findViewById(R.id.ButtonViewMap2);
        btnShowMap2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent(CustomerDetailsActivity.this, MapsActivity.class);
                intent.putExtra("page", "Summary");
                intent.putExtra("lat",selectedCustomer.location.latitude);
                intent.putExtra("longi",selectedCustomer.location.longitude);
                startActivity(intent);

            }
        });
        Button btnOrder = (Button) findViewById(R.id.ButtonOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent(CustomerDetailsActivity.this, OrderActivity.class);
                intent.putExtra("page", "Summary");
                startActivity(intent);

            }
        });
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle(R.string.title_section2);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
