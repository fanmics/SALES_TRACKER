package com.example.irfan.sales.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.irfan.sales.MainActivity;
import com.example.irfan.sales.R;
import com.example.irfan.sales.activity.CustomerDetailsActivity;
import com.example.irfan.sales.adapter.CustomerAdapter;
import com.example.irfan.sales.helper.CustomerHelper;
import com.example.irfan.sales.object.Customer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class ListRouteFragment extends Fragment {
    Activity _activity;
    CustomerAdapter customerAdapter;
    private List<Customer> mCustomers;
    private Fragment fragment;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return inflater.inflate(R.layout.customer_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCustomers = CustomerHelper.getList(getResources());
        customerAdapter = new CustomerAdapter(mCustomers, getActivity().getLayoutInflater());


        ((MainActivity) _activity).setBundle(null);
        ListView listViewCustomers = (ListView) getView().findViewById(R.id.ListViewCustomer);
        listViewCustomers.setAdapter(customerAdapter);

        listViewCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent custDetailsIntent = new Intent(getActivity().getBaseContext(), CustomerDetailsActivity.class);
                custDetailsIntent.putExtra(CustomerHelper.CUST_INDEX, position);
                startActivity(custDetailsIntent);
            }
        });


        Button btnScan = (Button) getView().findViewById(R.id.ButtonScan);
        btnScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentIntegrator.forFragment(fragment).initiateScan();

            }
        });

        Button btnReset = (Button) getView().findViewById(R.id.ButtonReset);
        btnReset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                inputSearch.setText("");

            }
        });
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
//                            inputSearch.setText(result);
                        }
                    });
                }
                break;
            default:
        }
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        _activity= activity;
        ((MainActivity) activity).onSectionAttached(3);
        ((MainActivity) activity).restoreActionBar();
    }

}
