package com.example.irfan.sales.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.irfan.sales.R;
import com.example.irfan.sales.adapter.ProductAdapter;
import com.example.irfan.sales.helper.ShoppingCartHelper;
import com.example.irfan.sales.object.Product;

import java.util.List;

/**
 * Created by Irfan on 1/3/2016.
 */
public class ShoppingCartActivity extends ActionBarActivity {

    private ProductAdapter mProductAdapter;
    private List<Product> mCartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);

        mCartList = ShoppingCartHelper.getCartList();

//        for (int i = 0; i < mCartList.size(); i++) {
//            mCartList.get(i).selected = false;
//        }

        final ListView listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);
        mProductAdapter = new ProductAdapter(mCartList, getLayoutInflater());
        mProductAdapter.setmShowQuantity(true);
        listViewCatalog.setAdapter(mProductAdapter);

        listViewCatalog.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent productDetailsIntent= new Intent(getBaseContext(),ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ShoppingCartHelper.PRODUCT_INDEX,position);
                startActivity(productDetailsIntent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_section4);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mProductAdapter !=null){
            mProductAdapter.notifyDataSetChanged();
        }
    }
}
