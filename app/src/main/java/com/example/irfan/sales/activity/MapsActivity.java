package com.example.irfan.sales.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.irfan.sales.parser.DirectionsJSONParser;
import com.example.irfan.sales.MainActivity;
import com.example.irfan.sales.R;
import com.example.irfan.sales.database.GPSDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> markerPoints;
    double srclatitude ;
    double srclongitude;
    double destlatitude ;
    double destlongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initializing
        markerPoints = new ArrayList<LatLng>();
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle(R.string.title_section5);
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    String page;

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("page")!= null)
        {
            page = bundle.getString("page");
        }

        if(bundle.getDouble("lat")!= 0)
        {
            destlatitude = bundle.getDouble("lat");
        }
        if(bundle.getDouble("longi")!= 0)
        {
            destlongitude = bundle.getDouble("longi");
        }
    }



    private String getDirectionsUrl(LatLng origin,LatLng dest){
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
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

    LatLng origin;
    LatLng startPos;
    LatLng endPos;
    private void drawLastLocation(){
//        PolylineOptions linedbOptions = new PolylineOptions().width(3).color(Color.RED).geodesic(true);
        try {
            Context context = getApplicationContext();
            GPSDatabase myDatabase=new GPSDatabase(context);
            myDatabase.open();
            Cursor cursor=myDatabase.getLastRows();
            cursor.moveToFirst();

            if(cursor.getCount() > 0) {
                srclatitude = Double.parseDouble(cursor.getString(1));
                srclongitude = Double.parseDouble(cursor.getString(2));

                origin = new LatLng(srclatitude, srclongitude);
                mMap.addMarker(new MarkerOptions().position(origin).title("Start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 14));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    LatLng start_position;
    LatLng end_position;
    private void drawDestination(){
        LatLng dest= new LatLng(destlatitude,destlongitude);
        mMap.addMarker(new MarkerOptions().position(dest).title("End").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 14));

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(page.equals("Detail")){
            new drawPath(mMap).execute();
            if(origin != null)
                drawDestination();
        }else if(page.equals("Summary")){
            drawLastLocation();
            if(origin != null)
                drawDestination();
        }else{
            new drawPath(mMap).execute();
        }
    }

    public class drawPath extends AsyncTask{

        private  GoogleMap map;

        public drawPath(GoogleMap map){
            this.map = map;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);

            Context context = getApplicationContext();
            GPSDatabase myDatabase=new GPSDatabase(context);
            try {
                myDatabase.open();
                Cursor cursor=myDatabase.getAllRows();
                cursor.moveToFirst();

                double oldLat=0;
                double oldLongi=0;
                for (int i = 0; i < cursor.getCount(); i++) {
                    srclatitude= Double.parseDouble(cursor.getString(2));
                    srclongitude = Double.parseDouble(cursor.getString(3));

                    origin = new LatLng(srclatitude, srclongitude);
                    if(i==0){
                        start_position = origin;
                        oldLat= srclatitude;
                        oldLongi = srclongitude;
                        options.add(origin);
                    }else if(i==cursor.getCount()-1){
                        end_position = origin;
                    }


                    if ( i > 0 )
                    {
                        Location targetLocation = new Location("Target");
                        targetLocation.setLatitude(srclatitude);
                        targetLocation.setLongitude(srclongitude);

                        Location sourceLocation =  new Location("Source");
                        sourceLocation.setLatitude(oldLat);
                        sourceLocation.setLongitude(oldLongi);

                        double distance = sourceLocation.distanceTo(targetLocation);
                        if(distance > 100){
                            oldLat= srclatitude;
                            oldLongi = srclongitude;
                            options.add(origin);
                        }
                    }

                    cursor.moveToNext();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return options;
        }

        protected void onPostExecute(Object result) {
            Polyline line = map.addPolyline((PolylineOptions) result);
            if(start_position != null) {
                map.addMarker(new MarkerOptions().position(start_position).title("Start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(start_position, 14));
            }
            if(end_position != null) {
                map.addMarker(new MarkerOptions().position(end_position).title("Current Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(end_position, 14));
            }
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception downloading", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch(Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions().width(3).color(Color.BLUE).geodesic(true);

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    lineOptions.add(position);
                }
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }
}
