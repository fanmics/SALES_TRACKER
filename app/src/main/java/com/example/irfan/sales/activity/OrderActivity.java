package com.example.irfan.sales.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irfan.sales.MainActivity;
import com.example.irfan.sales.helper.CustomerHelper;
import com.example.irfan.sales.helper.ListViewHelper;
import com.example.irfan.sales.object.Customer;
import com.example.irfan.sales.object.Product;
import com.example.irfan.sales.activity.ProductDetailsActivity;
import com.example.irfan.sales.R;
import com.example.irfan.sales.helper.ShoppingCartHelper;
import com.example.irfan.sales.adapter.ProductAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Irfan on 1/3/2016.
 */
public class OrderActivity extends ActionBarActivity {
    private ProductAdapter mProductAdapter;
    private List<Product> mCartList;
    private List<Customer> mCustomers;
    public EditText name;
    public EditText address;
    public EditText creditnote;
    private double limitCredit;
    public DecimalFormat formatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Button btnNext = (Button) findViewById(R.id.btnnext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Save Transaction",Toast.LENGTH_SHORT).show();
            }
        });

        name = (EditText) findViewById(R.id.clientname);
        address = (EditText) findViewById(R.id.address);
        creditnote = (EditText) findViewById(R.id.creditnote);

        Spinner spinner= (Spinner) findViewById(R.id.spinner);
        final int iCurrentSelection = spinner.getSelectedItemPosition();
        mCustomers = CustomerHelper.getList(getResources());

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),"posisi" + String.valueOf(position),Toast.LENGTH_SHORT).show();
                if (position > 0)
                    position = position - 1;
                name.setText("Name : " + mCustomers.get(position).name);
                address.setText("Address : " +mCustomers.get(position).address);
                creditnote.setText("Limit : " + formatter.format(mCustomers.get(position).limit));
                limitCredit = mCustomers.get(position).limit;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btnAddProduct = (Button) findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), StockActivity.class);
                intent.putExtra(ShoppingCartHelper.ORDER_INDEX, 1);
                startActivity(intent);

            }
        });
        // Initializing
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle(R.string.title_section3);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Bundle extras = data.getExtras();
//        String strtext =   extras.getString("key");
//        if(strtext.equals("refresh"))
        mCartList = ShoppingCartHelper.getCartList();
        mProductAdapter = new ProductAdapter(mCartList, getLayoutInflater());
        mProductAdapter.setmShowQuantity(true);
        mProductAdapter.setmShowBtnAdd(false);
//        mProductAdapter.notifyDataSetChanged();

        ListView listViewOrder = (ListView)findViewById(R.id.ListViewOrder);
        listViewOrder.setAdapter(mProductAdapter);
        ListViewHelper.setListViewHeightBasedOnChildren(listViewOrder);
        listViewOrder.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productDetailsIntent = new Intent(view.getContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ShoppingCartHelper.PRODUCT_INDEX, position);
                productDetailsIntent.putExtra(ShoppingCartHelper.ORDER_INDEX, 1);

                startActivityForResult(productDetailsIntent, 1);
            }
        });

        TextView txtTotal= (TextView) findViewById(R.id.textViewTotal);

        double total=0;
        for(int i=0; i <mCartList.size();i++ ){
            total = total + (mCartList.get(i).quantity * mCartList.get(i).price);
        }
        txtTotal.setText("Total : " + formatter.format(total));
        TextView txtLimitCredit= (TextView) findViewById(R.id.textViewLimitMsg);
        if(total > limitCredit)
            txtLimitCredit.setText(R.string.overCredit);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
//                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
        return true;
    }
}
