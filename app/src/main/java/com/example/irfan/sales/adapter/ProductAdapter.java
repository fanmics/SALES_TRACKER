package com.example.irfan.sales.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.irfan.sales.object.Product;
import com.example.irfan.sales.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Irfan on 1/2/2016.
 */
public class ProductAdapter extends BaseAdapter implements Filterable {
    private List<Product> mProductList;
    private List<Product> mProductFiltered;
    private LayoutInflater mInflater;
    private boolean mShowQuantity;
    private boolean mShowBtnAdd;
    ItemFilter mFilter = new ItemFilter();

    public ProductAdapter(List<Product> list, LayoutInflater inflater) {
        mProductList = list;
        mProductFiltered = list;
        mInflater = inflater;
    }

    public void setmShowQuantity(boolean mShowQuantity) {
        this.mShowQuantity = mShowQuantity;
    }

    public void setmShowBtnAdd(boolean mShowBtnAdd) {
        this.mShowBtnAdd = mShowBtnAdd;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ViewItem {
//        ImageView productImageView;
        TextView productTitle;
        TextView productStock;
        TextView productPrice;
        TextView productQuantity;
//        Button productButton;
    }

    TextView productStockTitle;
    TextView productQuantityTitle;

    @Override
    public int getCount() {
        return mProductFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item, null);
            item = new ViewItem();
//            item.productImageView = (ImageView) convertView.findViewById(R.id.ImageViewItem);
            item.productTitle = (TextView) convertView.findViewById(R.id.TextViewItem);
            item.productStock = (TextView) convertView.findViewById(R.id.TextViewStock);
            item.productQuantity = (TextView) convertView.findViewById(R.id.TextViewQuantity);
            item.productPrice = (TextView) convertView.findViewById(R.id.TextViewPrice);
            productStockTitle = (TextView) convertView.findViewById(R.id.TextViewStock_Title);
            productQuantityTitle= (TextView) convertView.findViewById(R.id.TextViewQuantity_Title);
//            item.productButton = (Button) convertView.findViewById(R.id.ButtonAddProduct);
            convertView.setTag(item);
        } else {
            item = (ViewItem) convertView.getTag();
        }

        final Product curProduct = mProductFiltered.get(position);
//        item.productImageView.setImageDrawable(curProduct.productImage);
        item.productTitle.setText(curProduct.title);

        NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(2);

        item.productPrice.setText(formatter.format(curProduct.price));
        if (mShowQuantity) {
            item.productQuantity.setText(String.valueOf(curProduct.quantity));
            productQuantityTitle.setVisibility(View.VISIBLE);
            item.productQuantity.setVisibility(View.VISIBLE);
            productStockTitle.setVisibility(View.GONE);
            item.productStock.setVisibility(View.GONE);

        } else {
            item.productStock.setText(String.valueOf(curProduct.stock));
            item.productStock.setVisibility(View.VISIBLE);
            item.productQuantity.setVisibility(View.GONE);
            productStockTitle.setVisibility(View.VISIBLE);
        }

//        if (!mShowBtnAdd) {
//            item.productButton.setVisibility(View.GONE);
//        } else {
//            item.productButton.setVisibility(View.VISIBLE);
//            item.productButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v.getContext() instanceof MainActivity) {
//                        ShoppingCartHelper.setQuantity(curProduct, 1);
//                        ((MainActivity) v.getContext()).onNavigationDrawerItemSelected(2);
//                    }
//                }
//            });
//        }
        return convertView;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Product> list = mProductList;

            int count = list.size();
            final ArrayList<Product> nlist = new ArrayList<Product>(count);

            Product filterableProduct;
            for (int i = 0; i < count; i++) {
                filterableProduct = list.get(i);
                if (filterableProduct.title.toLowerCase().contains(filterString) ||
                        filterableProduct.description.toLowerCase().contains(filterString)||
                        filterableProduct.code.toLowerCase().contains(filterString)) {
                    nlist.add(filterableProduct);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mProductFiltered = (List<Product>) results.values;
            notifyDataSetChanged();
        }
    }
}
