package com.example.irfan.sales.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.List;


import com.example.irfan.sales.object.Product;
import com.example.irfan.sales.R;
import com.example.irfan.sales.helper.ShoppingCartHelper;
import com.example.irfan.sales.adapter.ProductAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by Irfan on 1/3/2016.
 */
public class StockActivity extends ActionBarActivity {
    private Activity _activity;
    private ProductAdapter mProductAdapter;
    private List<Product> mCartList;
    public EditText inputSearch;
    private Handler handler = new Handler();
    public Fragment fragment;
    private int orderIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        fragment = this;
        setContentView(R.layout.catalog);
        mCartList = ShoppingCartHelper.getCatalog(getResources());
        mProductAdapter = new ProductAdapter(mCartList,getLayoutInflater());
        mProductAdapter.setmShowQuantity(false);
        mProductAdapter.setmShowBtnAdd(false);

//        if(getArguments()!=null) {
//            fragmentId = getArguments().getInt("order");
//        }
//
//        if(fragmentId==2)
//            mProductAdapter.setmShowBtnAdd(true);

        ListView listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);
        listViewCatalog.setAdapter(mProductAdapter);

        orderIndex= getIntent().getExtras().getInt(ShoppingCartHelper.ORDER_INDEX);

        listViewCatalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productDetailsIntent = new Intent(getBaseContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ShoppingCartHelper.PRODUCT_INDEX, position);
                productDetailsIntent.putExtra(ShoppingCartHelper.ORDER_INDEX, orderIndex);

                startActivity(productDetailsIntent);
            }
        });

//        Button viewShoppingCart = (Button) getView().findViewById(R.id.ButtonViewCart);
//        viewShoppingCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fragmentId != 2) {
//                    Intent viewShoppingCartIntent = new Intent(getActivity().getBaseContext(), ShoppingCartActivity.class);
//                    startActivity(viewShoppingCartIntent);
//                } else {
//                    ((MainActivity) _activity).onNavigationDrawerItemSelected(2);
//                    ((MainActivity) _activity).onSectionAttached(2);
//
//                }
//            }
//        });
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mProductAdapter != null)
                    mProductAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button btnScan = (Button) findViewById(R.id.ButtonScan);
        btnScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new IntentIntegrator(StockActivity.this).initiateScan();

            }
        });
        Button btnReset = (Button) findViewById(R.id.ButtonReset);
        btnReset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                inputSearch.setText("");

            }
        });

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle(R.string.title_section1);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            inputSearch.setText(result);
                        }
                    });
                }
                break;
            default:
        }
    }

}
