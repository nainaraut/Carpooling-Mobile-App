package nraut.mapcheck;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by naina on 5/22/2016.
 */
public class ProcessData extends BaseActivity{
    List<GeoPoint> geoListDest;
    List<GeoPoint> geoListOrigin;
    List<Map<String, String>> geoListPassengerFiltered = new ArrayList<Map<String, String>>();
    Context context;

    private List<ListRowStruct> availableList;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_display);
        Intent i = getIntent();
        availableList = (List<ListRowStruct>) i.getSerializableExtra("availableList");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.availableList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new Adapter(availableList);
        recyclerView.setAdapter(adapter);

        TouchHelperCallback callback = new TouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
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

    public void getOriginGeoData(List<GeoPoint> geoListOrigin){
        System.out.println("In Process Data Origin");
        this.geoListOrigin = geoListOrigin;
        for(GeoPoint obj : geoListOrigin){
            System.out.println("Process Data"+obj.getLatitude());
        }
    }

    public void getDestGeoData(List<GeoPoint> geoListDest,Context context){
        this.context = context;
        System.out.println("In Process Data Destination");
        this.geoListDest = geoListDest;
        for(GeoPoint obj : geoListDest){
            System.out.println("Process Data"+obj.getLatitude());
        }
        filterGeoData();
    }

    public void filterGeoData(){
        Iterator<GeoPoint> iterator2 = geoListDest.iterator();
        Iterator<GeoPoint> iterator1 = geoListOrigin.iterator();

        while (iterator1.hasNext()){
            GeoPoint geoPoint = iterator1.next();
            String email1 = (String) geoPoint.getMetadata("EmailAddress");
            while (iterator2.hasNext()){
                GeoPoint geoPoint2 = iterator2.next();
                String email2 = (String) geoPoint2.getMetadata("EmailAddress");
                if(email1.equals(email2)){
                    System.out.println("In filter method" + email2);
                    Map<String, String> filteredGeopoint = new HashMap<>();
                    filteredGeopoint.put("Email", email1);
                    filteredGeopoint.put("LatitudeOrigin",Double.toString(geoPoint.getLatitude()));
                    filteredGeopoint.put("LongitudeOrigin",Double.toString(geoPoint.getLongitude()));
                    filteredGeopoint.put("LatitudeDest",Double.toString(geoPoint2.getLatitude()));
                    filteredGeopoint.put("LongitudeDest",Double.toString(geoPoint2.getLongitude()));
                    geoListPassengerFiltered.add(filteredGeopoint);
                }
            }
            iterator2 = geoListDest.iterator();
        }
        getUsersTableData(geoListPassengerFiltered);

    }

    private void getUsersTableData(final List<Map<String, String>> geoListPassengerFiltered) {
        String whereClause;
        int count = 0;
        String emailList = "(";
        final List<Users> loginData = new ArrayList<Users>();

        for(Map<String, String> obj : geoListPassengerFiltered){
            count++;
            if(count == geoListPassengerFiltered.size()){
                emailList = emailList+"'"+obj.get("Email")+"'";
            }
            else{
                emailList = emailList+"'"+obj.get("Email")+"',";
            }
        }
        emailList = emailList+")";
        System.out.println(emailList);

        whereClause = "email in "+emailList;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);

        final String finalEmailList = emailList;
        Backendless.Data.of(Users.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Users>>() {
            @Override
            public void handleResponse(BackendlessCollection<Users> usersBackendlessCollection) {
                Iterator<Users> iterator = usersBackendlessCollection.getData().iterator();
                while (iterator.hasNext()) {
                    Users userTable = iterator.next();
                    System.out.println(userTable.email);
                    loginData.add(userTable);
                }

                //get status from the RequestTable
                getStatus(finalEmailList, loginData);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("No matching records in Users table");
                Toast.makeText(context, "No matching route for user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStatus(String requestedEmails, final List<Users> loginData) {
        final String loginEmail = new SharedPrefEmail(context.getApplicationContext()).getEmail("LoginEmail");
        requestedEmails = requestedEmails.substring(0,requestedEmails.length()-1);
        requestedEmails = requestedEmails+",'"+loginEmail+"')";
        System.out.println("final email list"+requestedEmails);
        String whereClause = "email in "+requestedEmails+" and requestedEmailAddress in "+requestedEmails;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        final Map<String,String> status = new HashMap<String,String>();
        final List<RequestTable> reqData = new ArrayList<RequestTable>();

        //get the existing record
        Backendless.Data.of(RequestTable.class).find(dataQuery, new AsyncCallback<BackendlessCollection<RequestTable>>() {
            @Override
            public void handleResponse(BackendlessCollection<RequestTable> requestTableBackendlessCollection) {
                Iterator<RequestTable> iterater = requestTableBackendlessCollection.getCurrentPage().iterator();
                while (iterater.hasNext()) {
                    RequestTable req = iterater.next();
                    //new change
                    if(req.email.equals(loginEmail) || req.requestedEmailAddress.equals(loginEmail)){
                        status.put(req.requestedEmailAddress,req.status);
                        reqData.add(req);
                        System.out.println(req.status);
                    }
                }
                callDisplayList(geoListPassengerFiltered, loginData,reqData,loginEmail,status);
            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Error in getting status in ProcessData");
            }
        });
    }

    public void callDisplayList(List<Map<String, String>> geoListPassengerFiltered,
                                List<Users> loginData,List<RequestTable> reqData,
                                String loginEmail,Map<String,String> status
                                ){
        String defaultPic = "defaultpic.png";
        availableList = new ArrayList<ListRowStruct>();
        for(Users login : loginData){
            String email1 = login.email;
            for(Map<String, String> geo : geoListPassengerFiltered){
                String email2 = geo.get("Email");
                if(email1.equals(email2)){
                    String statusVal = status.get(geo.get("Email"));
                    availableList.add(new ListRowStruct(defaultPic,login.name,login.origin,
                            login.destination,login.email,login.mobilenumber,statusVal));
                }
            }
        }

        List<ListRowStruct> availableList2 = new ArrayList<ListRowStruct>();
        availableList2.addAll(availableList);

        for(RequestTable req:reqData){
//            if(req.requestedEmailAddress.equals(loginEmail) ) {
                for(ListRowStruct data:availableList2){
                    if(!req.email.equals(loginEmail)){
                        data.status = "AVAILABLE";
                    }
                    if(data.email.equals(req.email) && loginEmail.equals(req.requestedEmailAddress)&& !req.email.equals(loginEmail)) {
                        availableList.remove(data);
                    }
//                }
            }
        }

        Intent i = new Intent(context,ProcessData.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("availableList", (Serializable) availableList);
        context.startActivity(i);
        finish();
    }
}
