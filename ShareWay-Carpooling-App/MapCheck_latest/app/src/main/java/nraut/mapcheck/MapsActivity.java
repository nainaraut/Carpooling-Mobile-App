package nraut.mapcheck;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.geo.GeoPoint;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    double longitude;
    double latitude;

    //naina 5/5/2016
    private static final String LOG_TAG = "AutoComplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBM0FSQRAKqe7NTMWpq44lr2JIbcH5vgA0";
    private static double origin_lat;
    private static double origin_lgn;
    private static double dest_lat;
    private static double dest_lgn;

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    String KEY_NAME = "LoginEmail";

    // Sharedpref file name
    private static final String PREF_NAME = "LoginEmail";
    //---
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //naina
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //---naina 5/5/2016
        AutoCompleteTextView origin = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        origin.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_struct));
        origin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                Toast.makeText(MapsActivity.this, str, Toast.LENGTH_SHORT).show();

                //get latitude and longitude of origin
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(str, 1);
                    if (addresses.size() > 0) {
                        origin_lat = addresses.get(0).getLatitude();
                        origin_lgn = addresses.get(0).getLongitude();

                        mMap.clear();
                        LatLng currLoc = new LatLng(origin_lat, origin_lgn);
                        mMap.addMarker(new MarkerOptions().position(currLoc).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
                        if (mMap != null) {
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 16));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        AutoCompleteTextView destination = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView2);
        destination.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_struct));
//        destination.setOnItemClickListener(this);
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                Toast.makeText(MapsActivity.this, str, Toast.LENGTH_SHORT).show();

                //get latitude and longitude of destination
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(str, 1);
                    if(addresses.size() > 0) {
                        dest_lat= addresses.get(0).getLatitude();
                        dest_lgn= addresses.get(0).getLongitude();
                        LatLng currLoc = new LatLng(dest_lat, dest_lgn);
                        mMap.addMarker(new MarkerOptions().position(currLoc).title(str));

                        // Getting URL to the Google Directions A
                        String url = getDirectionsUrl(origin_lat,origin_lgn,dest_lat,dest_lgn);
                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);

                        //set bounds with all the map points
                        LatLngBounds bounds1 = new LatLngBounds.Builder()
                                .include(new LatLng(origin_lat, origin_lgn))
                                .include(new LatLng(dest_lat,dest_lgn)).build();

                        Point displaySize = new Point();
                        getWindowManager().getDefaultDisplay().getSize(displaySize);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds1, displaySize.x, 250, 30));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //---
    }

    //on search button click
    public void onSearch(View view){
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String loginEmail = new SharedPrefEmail(getApplicationContext()).getEmail("LoginEmail");
        String status = "PENDING";
        DatabaseConnection db = new DatabaseConnection();
        TextView origin = (TextView)findViewById(R.id.autoCompleteTextView);
        TextView dest = (TextView)findViewById(R.id.autoCompleteTextView2);

        String origin1 = origin.getText().toString();
        String dest1 = dest.getText().toString();

        //insert origin and destination in Users table
        db.insertOriginDestination(loginEmail,origin1,dest1);

        if(type.equals("DRIVER")){
           db.saveDriverTable(loginEmail, origin_lat, origin_lgn, dest_lat, dest_lgn, "", status,getApplicationContext());
        }
        else{
            db.savePassengerTable(loginEmail,origin_lat,origin_lgn,dest_lat,dest_lgn,"",status, getApplicationContext());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, new myLocationListener());
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        longitude = myLocation.getLongitude();
        latitude = myLocation.getLatitude();

        // Add a marker in current location and move the camera
        LatLng currLoc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currLoc).title("current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 16));
        }
    }

    class myLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude  = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.requestmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.RequestMenu:
                RequestList req = new RequestList();
                req.getRequestListData(this);
                return true;
            case R.id.Uninstall:
                Uri packageUri = Uri.parse("package:"+this.getApplication().getPackageName());
                Intent uninstall = new Intent(Intent.ACTION_DELETE,packageUri);
                startActivity(uninstall);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://nraut.mapcheck/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://nraut.mapcheck/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //naina 5/5/2016
    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

        public String getDirectionsUrl(double origin_lat, double origin_lgn,double dest_lat,double dest_lgn) {

            // Origin of route
            String str_origin = "origin=" + origin_lat + "," + origin_lgn;

            // Destination of route
            String str_dest = "destination=" + dest_lat + "," + dest_lgn;

            // Sensor enabled
            String sensor = "sensor=false";

            //API key
            String key = "key=AIzaSyBM0FSQRAKqe7NTMWpq44lr2JIbcH5vgA0";

            // Building the parameters to the web service
            String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" +key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

            return url;
        }

        /**
         * A method to download json data from url
         */
        public String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
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
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        // Fetches data from url passed
        public class DownloadTask extends AsyncTask<String, Void, String> {

            // Downloading data in non-ui thread
            @Override
            protected String doInBackground(String... url) {

                // For storing data from web service
                String data = "";

                try {
                    // Fetching the data from web service
                    data = downloadUrl(url[0]);
                } catch (Exception e) {
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

        /**
         * A class to parse the Google Places in JSON format
         */
        public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            // Parsing the data in non-ui thread
            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try {
                    jObject = new JSONObject(jsonData[0]);
                    DirectionsJSONParser parser = new DirectionsJSONParser();

                    // Starts parsing data
                    routes = parser.parse(jObject);
                } catch (Exception e) {
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
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(12);
                    lineOptions.color(Color.BLUE);
                    lineOptions.geodesic(true);
                }

                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);
            }
    }
}