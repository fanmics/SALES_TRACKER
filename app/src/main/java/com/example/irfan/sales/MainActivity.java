package com.example.irfan.sales;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.irfan.sales.activity.ChartActivity;
import com.example.irfan.sales.activity.MapsActivity;
import com.example.irfan.sales.activity.ShoppingCartActivity;
import com.example.irfan.sales.activity.ShoppingCartEntry;
import com.example.irfan.sales.activity.StockActivity;
import com.example.irfan.sales.fragment.ListRouteFragment;
import com.example.irfan.sales.fragment.MainMenuDrawerFragment;
import com.example.irfan.sales.helper.ShoppingCartHelper;
import com.example.irfan.sales.object.Product;
import com.example.irfan.sales.service.GPSLoggerService;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
       implements MainMenuDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    public MainMenuDrawerFragment mMainMenuDrawerFragment;

    Bundle bundle;

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public FragmentManager fragmentManager;
    private BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mMainMenuDrawerFragment = (MainMenuDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mMainMenuDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                double lat = intent.getDoubleExtra(GPSLoggerService.GPS_LAT,0.0);
//                double longi = intent.getDoubleExtra(GPSLoggerService.GPS_LONGI,0.0);
//
//            }
//        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
//                new IntentFilter(GPSLoggerService.GPS_RESULT)
//        );
//    }
//
//    @Override
//    protected void onStop() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
//        super.onStop();
//
//    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        fragmentManager = getFragmentManager();
        Fragment fragment = new Fragment();
        switch (position) {
            default:
            case 0:
                fragment = PlaceholderFragment.newInstance(position + 1);

                break;
            case 1:
//                fragment = new StockFragment();
//                fragment.setArguments(getBundle());
                Intent intentStock = new Intent(getBaseContext(), StockActivity.class);
                intentStock.putExtra(ShoppingCartHelper.ORDER_INDEX, 2);
                startActivity(intentStock);
                break;
            case 2:
                fragment = new ListRouteFragment();
                fragment.setArguments(getBundle());

                break;
            case 3:
                startActivity(new Intent(this, ShoppingCartActivity.class));
                break;

            case 4:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("page", "Customer");
                startActivity(intent);
                break;
            case 5:
                startActivity(new Intent(this, ChartActivity.class));
                break;

            case 6:
                ShoppingCartHelper.setCartMap(new HashMap<Product, ShoppingCartEntry>());
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                preferences.edit().remove("statusText").commit();
                stopGPSService();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void startGPSService() {
        startService(new Intent(MainActivity.this, GPSLoggerService.class));

    }

    public void stopGPSService() {
        stopService(new Intent(MainActivity.this, GPSLoggerService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mMainMenuDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

//        if (mTitle == getString(R.string.title_section2)) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
            implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private TextView txtStatus;
        View rootView;
        Button button;
        Button button2;
        Button button3;
//        Button button4;
//        Button button5;
        Button buttonStart;
        Button buttonStop;
        Button buttonViewMap;

        Activity _activity;
        SharedPreferences preferences;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            setRetainInstance(true);
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            button = (Button) rootView.findViewById(R.id.button1);
            button.setOnClickListener(this);
            button2 = (Button) rootView.findViewById(R.id.button2);
            button2.setOnClickListener(this);
            button3 = (Button) rootView.findViewById(R.id.button3);
            button3.setOnClickListener(this);
//            button4 = (Button) rootView.findViewById(R.id.button4);
//            button4.setOnClickListener(this);
//            button5 = (Button) rootView.findViewById(R.id.button5);
//            button5.setOnClickListener(this);
            buttonStart = (Button) rootView.findViewById(R.id.buttonStart);
            buttonStart.setOnClickListener(this);
            buttonStop = (Button) rootView.findViewById(R.id.buttonStop);
            buttonStop.setOnClickListener(this);
            buttonViewMap = (Button) rootView.findViewById(R.id.buttonViewMap);
            buttonViewMap.setOnClickListener(this);
            txtStatus = (TextView) rootView.findViewById(R.id.section_status);
            // SharedPreference
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

            // Retrieving
            String serviceStatus= preferences.getString("statusText", "");
            txtStatus.setText(serviceStatus);
            if(serviceStatus.equals("Start")){
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);
            }else{
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
            }
            txtStatus.setText(serviceStatus);
            preferences.edit().remove("statusText").commit();
            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.button1:
                    ((MainActivity) _activity).mMainMenuDrawerFragment.mCurrentSelectedPosition = 1;
//                    ((MainActivity) _activity).fragmentManager.beginTransaction().replace(R.id.container, new StockFragment()).commit();
                    Intent intentStock = new Intent(getActivity(), StockActivity.class);
                    intentStock.putExtra(ShoppingCartHelper.ORDER_INDEX, 2);
                    startActivity(intentStock);
                    break;
                case R.id.button2:
                    ((MainActivity) _activity).mMainMenuDrawerFragment.mCurrentSelectedPosition = 2;
                    ((MainActivity) _activity).fragmentManager.beginTransaction().replace(R.id.container, new ListRouteFragment()).commit();
                    break;
                case R.id.button3:
                    startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
                    break;
//                case R.id.button4:
//                    intent = new Intent(getActivity(), MapsActivity.class);
//                    intent.putExtra("page", "Customer");
//                    startActivity(intent);
//                    break;
//                case R.id.button5:
//                    startActivity(new Intent(getActivity(), ChartActivity.class));
//                    break;
                case R.id.buttonStart:
                    GPSLoggerService gpsLoggerService = new GPSLoggerService(getActivity().getApplicationContext());
                    boolean checkLocation = gpsLoggerService.getLocation(LocationManager.GPS_PROVIDER);

                    if (checkLocation) {
                        txtStatus.setText(R.string.start);
                        buttonStart.setEnabled(false);
                        buttonStop.setEnabled(true);
                        ((MainActivity) _activity).startGPSService();
                    } else {
                        showSettingsAlert("GPS");
                    }
                    break;
                case R.id.buttonStop:
                    txtStatus.setText(R.string.stop);
                    buttonStart.setEnabled(true);
                    buttonStop.setEnabled(false);
                    ((MainActivity) _activity).stopGPSService();
                    break;
                case R.id.buttonViewMap:
                    intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("page", "Main");
                    startActivity(intent);
                    break;
            }
            preferences.edit().putString("statusText", txtStatus.getText().toString()).commit();
        }

        public void showSettingsAlert(String provider) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder( ((MainActivity) _activity));

            alertDialog.setTitle(provider + " SETTINGS");
            alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");

            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            ((MainActivity) _activity).startActivity(intent);
                        }
                    });

            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
        }

        @Override
        public void onAttach(final Activity activity) {
            super.onAttach(activity);
            _activity = activity;
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));

        }
    }

}
