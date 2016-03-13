package com.example.irfan.sales.service;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.irfan.sales.R;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Lenovo on 2/7/2016.
 */
public class GPSLoggerService extends Service {


    public static final String DATABASE_NAME = "GPSLOGGERDB";
    public static final String POINTS_TABLE_NAME = "LOCATION_POINTS";
    public static final String TRIPS_TABLE_NAME = "TRIPS";

    private final DecimalFormat sevenSigDigits = new DecimalFormat("0.#######");
    private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private LocationManager lm;
    private LocationListener locationListener;
    private SQLiteDatabase db;

    private static long minTimeMillis = 5000; //1000 * 60 * 1;
    private static long minDistanceMeters = 20;
    private static float minAccuracyMeters = 60;

    private int lastStatus = 0;
    private static boolean showingDebugToast = true;

    private static final String tag = "GPSLoggerService";
    public GPSLoggerService() {
    }

    public GPSLoggerService(Context context) {
       lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean getLocation(String provider) {

        if (lm.isProviderEnabled(provider)) {
            return true;
        }
        return false;
    }

    LocalBroadcastManager broadcaster;
    /** Called when the activity is first created. */
    private void startLoggerService() {
//        broadcaster = LocalBroadcastManager.getInstance(this);
        // ---use the LocationManager class to obtain GPS locations---
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                minTimeMillis,
                minDistanceMeters,
                locationListener);
//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeMillis, minDistanceMeters, locationListener);

        initDatabase();
    }

    private void initDatabase() {
        db = this.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
        db.execSQL("drop table if Exists "+ POINTS_TABLE_NAME);
//        Log.i(tag, "Database drop table");
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                POINTS_TABLE_NAME + " (LOCATION_ID integer primary key autoincrement,CREATED_DATE VARCHAR, LATITUDE REAL, LONGITUDE REAL," +
                "ALTITUDE REAL, ACCURACY REAL, SPEED REAL, BEARING REAL);");
        db.close();
        Log.i(tag, "Database opened ok");
    }

    private void shutdownLoggerService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.removeUpdates(locationListener);
    }

    static final public String GPS_RESULT = "GPSLoggerService.REQUEST_PROCESSED";
    static public String GPS_LAT = "GPSLoggerService.LATITUDE";
    static public String GPS_LONGI = "GPSLoggerService.LONGITUDE";

    public void sendResult(double latitude, double longitude) {
        Intent intent = new Intent(GPS_RESULT);
        if(latitude > 0.0) {
            intent.putExtra(GPS_LAT, latitude);
            intent.putExtra(GPS_LONGI , longitude );
        }
        broadcaster.sendBroadcast(intent);
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location loc) {
            if (loc != null) {
                    Log.e("locUpdate",loc.getLatitude()+"");
//                sendResult(loc.getLatitude(),loc.getLongitude());

                boolean pointIsRecorded = false;
                try {
                    if (loc.hasAccuracy() && loc.getAccuracy() <= minAccuracyMeters) {
                    pointIsRecorded = true;
                    GregorianCalendar greg = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
                    StringBuffer queryBuf = new StringBuffer();
//                    timestampFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    timestampFormat.setTimeZone(TimeZone.getDefault());

                    queryBuf.append("INSERT INTO " + POINTS_TABLE_NAME +
                            " (CREATED_DATE,LATITUDE,LONGITUDE,ALTITUDE,ACCURACY,SPEED,BEARING) VALUES (" +
                            "'" + timestampFormat.format(greg.getTime()) + "'," +
                            loc.getLatitude() + "," +
                            loc.getLongitude() + "," +
                            (loc.hasAltitude() ? loc.getAltitude() : "NULL") + "," +
                            (loc.hasAccuracy() ? loc.getAccuracy() : "NULL") + "," +
                            (loc.hasSpeed() ? loc.getSpeed() : "NULL") + "," +
                            (loc.hasBearing() ? loc.getBearing() : "NULL") + ");");
                    Log.i(tag, queryBuf.toString());

                    db = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
                    db.execSQL(queryBuf.toString());
                    }
                } catch (Exception e) {
                    Log.e(tag, e.toString());
                } finally {
                    if (db.isOpen())
                        db.close();
                }
                if (pointIsRecorded) {
                    if (showingDebugToast) Toast.makeText(
                            getBaseContext(),
                            "Location stored: \nLat: " + sevenSigDigits.format(loc.getLatitude())
                                    + " \nLon: " + sevenSigDigits.format(loc.getLongitude())
                                    + " \nAlt: " + (loc.hasAltitude() ? loc.getAltitude() + "m" : "?")
                                    + " \nAcc: " + (loc.hasAccuracy() ? loc.getAccuracy() + "m" : "?"),
                            Toast.LENGTH_SHORT).show();

                        showingDebugToast=false;

                } else {
                    if (showingDebugToast) Toast.makeText(
                            getBaseContext(),
                            "Location not accurate enough: \nLat: " + sevenSigDigits.format(loc.getLatitude())
                                    + " \nLon: " + sevenSigDigits.format(loc.getLongitude())
                                    + " \nAlt: " + (loc.hasAltitude() ? loc.getAltitude()+"m":"?")
                                    + " \nAcc: " + (loc.hasAccuracy() ? loc.getAccuracy()+"m":"?"),
                            Toast.LENGTH_SHORT).show();
                    showingDebugToast=true;
                }
            }
        }

        public void onProviderDisabled(String provider) {
            if (showingDebugToast) Toast.makeText(getBaseContext(), "onProviderDisabled: " + provider,
                    Toast.LENGTH_SHORT).show();

        }

        public void onProviderEnabled(String provider) {
            if (showingDebugToast) Toast.makeText(getBaseContext(), "onProviderEnabled: " + provider,
                    Toast.LENGTH_SHORT).show();

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            String showStatus = null;
            if (status == LocationProvider.AVAILABLE)
                showStatus = "Available";
            if (status == LocationProvider.TEMPORARILY_UNAVAILABLE)
                showStatus = "Temporarily Unavailable";
            if (status == LocationProvider.OUT_OF_SERVICE)
                showStatus = "Out of Service";
//            if (status != lastStatus && showingDebugToast) {
//                Toast.makeText(getBaseContext(),
//                        "new status: " + showStatus,
//                        Toast.LENGTH_SHORT).show();
//            }
            lastStatus = status;
        }

    }

    // Below is the service framework methods

    private NotificationManager mNM;

    @Override
    public void onCreate() {
        super.onCreate();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        startLoggerService();

        // Display a notification about us starting. We put an icon in the
        // status bar.
        showNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        shutdownLoggerService();
        showingDebugToast=true;
        // Cancel the persistent notification.
        mNM.cancel(R.string.local_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the
        // expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.gpslogger16,
                text, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, GPSLoggerService.class), 0);

        // Set the info for the views that show in the notification panel.

//		notification.setLatestEventInfo(this, getText(R.string.service_name),
//				text, contentIntent);

        Notification.Builder builder = new Notification.Builder(GPSLoggerService.this);
        builder.setContentTitle(getText(R.string.service_name))
                .setContentText(text)
                .build();
        notification = builder.getNotification();
        // Send the notification.
        // We use a layout id because it is a unique number. We use it later to
        // cancel.
        mNM.notify(R.string.local_service_started, notification);
    }

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public static void setMinTimeMillis(long _minTimeMillis) {
        minTimeMillis = _minTimeMillis;
    }

    public static long getMinTimeMillis() {
        return minTimeMillis;
    }

    public static void setMinDistanceMeters(long _minDistanceMeters) {
        minDistanceMeters = _minDistanceMeters;
    }

    public static long getMinDistanceMeters() {
        return minDistanceMeters;
    }

    public static float getMinAccuracyMeters() {
        return minAccuracyMeters;
    }

    public static void setMinAccuracyMeters(float minAccuracyMeters) {
        GPSLoggerService.minAccuracyMeters = minAccuracyMeters;
    }

    public static void setShowingDebugToast(boolean showingDebugToast) {
        GPSLoggerService.showingDebugToast = showingDebugToast;
    }

    public static boolean isShowingDebugToast() {
        return showingDebugToast;
    }

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        GPSLoggerService getService() {
            return GPSLoggerService.this;
        }
    }

}
