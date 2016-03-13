package com.example.irfan.sales.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.example.irfan.sales.R;
import com.example.irfan.sales.helper.ShoppingCartHelper;
import com.example.irfan.sales.object.Product;

import java.util.List;

/**
 * Created by Irfan on 1/3/2016.
 */
public class ProductDetailsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetails);

        int productIndex= getIntent().getExtras().getInt(ShoppingCartHelper.PRODUCT_INDEX);
        int orderIndex= getIntent().getExtras().getInt(ShoppingCartHelper.ORDER_INDEX);

        List<Product> catalog;

//        if(orderIndex==1)
//            catalog = ShoppingCartHelper.getCartList();
//        else
            catalog = ShoppingCartHelper.getCatalog(getResources());

        final Product selectedProduct= catalog.get(productIndex);

//        ImageView productImageView = (ImageView) findViewById(R.id.ImageViewProduct);
//        productImageView.setImageDrawable(selectedProduct.productImage);

        TextView productTitleTextView= (TextView) findViewById(R.id.TextViewProductTitle);
        productTitleTextView.setText(selectedProduct.title);

        TextView productDetailsTextView = (TextView) findViewById(R.id.TextViewProductDetails);
        productDetailsTextView.setText(selectedProduct.description);

        TextView productPriceTextView = (TextView) findViewById(R.id.TextViewPrice);
        productPriceTextView.setText("Price : " + selectedProduct.CurrencyFormat(selectedProduct.price));

        TextView productStockTextView = (TextView) findViewById(R.id.TextViewStock);
        productStockTextView.setText("Stock : "+String.valueOf(selectedProduct.stock));
//        TextView textViewCurrentQuantity= (TextView) findViewById(R.id.textViewCurrentlyInCart);
//        textViewCurrentQuantity.setText(new StringBuilder().append(R.string.currentCart).append(ShoppingCartHelper.getProductQuantity(selectedProduct)).toString()) ;

        final EditText editTextQuantity= (EditText) findViewById(R.id.editTextQuantity);

        Button addToCartButton = (Button) findViewById(R.id.ButtonAddToCart);
        if(orderIndex==1) {
            addToCartButton.setVisibility(View.VISIBLE);
            editTextQuantity.setVisibility(View.VISIBLE);
        }else {
            addToCartButton.setVisibility(View.GONE);
            editTextQuantity.setVisibility(View.GONE);
        }
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(editTextQuantity.getText().toString());
                    if (quantity < 0) {
                        Toast.makeText(getBaseContext(), R.string.biggerThanZero, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), R.string.numericOnly, Toast.LENGTH_SHORT).show();
                    return;
                }

                ShoppingCartHelper.setQuantity(selectedProduct, quantity);
//                Intent intent = getIntent();
//                Bundle bundle = new Bundle();
//                bundle.putString("key", "refresh");
//                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
                finish();
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
