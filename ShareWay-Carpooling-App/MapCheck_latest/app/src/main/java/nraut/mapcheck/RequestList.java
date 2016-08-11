package nraut.mapcheck;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.backendless.persistence.BackendlessDataQuery;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestList extends BaseActivity {

    //List<Map<String, String>> geoListPassengerFiltered = new ArrayList<Map<String, String>>();
    Context context;

    private List<ListRowStruct2> availableList;
    Adapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        //getRequestListData();

        Intent i = getIntent();
        availableList = (List<ListRowStruct2>) i.getSerializableExtra("availableList");
        if(availableList.isEmpty()){
            Toast.makeText(this,"No requests received",Toast.LENGTH_SHORT).show();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.requestList1);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new Adapter2(availableList);
        recyclerView.setAdapter(adapter);

        TouchHelperCallback2 callback = new TouchHelperCallback2(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void getRequestListData(final Context context) {
        String loginEmail = new SharedPrefEmail(context.getApplicationContext()).getEmail("LoginEmail");
        final String[] whereClause = {"requestedEmailAddress = '"+loginEmail+"'"};
        final BackendlessDataQuery[] dataQuery = {new BackendlessDataQuery()};
        dataQuery[0].setWhereClause(whereClause[0]);
        final String[] emailList = new String[1];
        emailList[0] = "(";
        final String[] whereClause1 = new String[1];
        final Map<String,String> status = new HashMap<String,String>();

        //check if any request is pending for the user
        Backendless.Data.of(RequestTable.class).find(dataQuery[0], new AsyncCallback<BackendlessCollection<RequestTable>>() {
            @Override
            public void handleResponse(BackendlessCollection<RequestTable> requestTableBackendlessCollection) {
                Iterator<RequestTable> iterator = requestTableBackendlessCollection.getCurrentPage().iterator();
                while (iterator.hasNext()) {
                    RequestTable requestTable = iterator.next();
                    if (! iterator.hasNext()) {
                        emailList[0] = emailList[0] + "'" + requestTable.email + "'";
                    } else {
                        emailList[0] = emailList[0] + "'" + requestTable.email + "',";
                    }
                    status.put(requestTable.email,requestTable.status);
                }

                //display the list of users who has requested
                emailList[0] = emailList[0] + ")";
                whereClause1[0] = "email in " + emailList[0];

                BackendlessDataQuery dataQuery2 = new BackendlessDataQuery();
                dataQuery2.setWhereClause(whereClause1[0]);
                final List<Users> users = new ArrayList<Users>();

                //get data from users table
                Backendless.Data.of(Users.class).find(dataQuery2, new AsyncCallback<BackendlessCollection<Users>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<Users> usersBackendlessCollection) {
                        Iterator<Users> iterator = usersBackendlessCollection.getData().iterator();
                        while (iterator.hasNext()) {
                            Users userTable = iterator.next();
                            System.out.println(userTable.email);
                            users.add(userTable);
                        }

                        addRequestData(users,context,status);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        System.out.println("Error while fetching data from users table");
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Error while fetching data from Request table");
            };
        });
    }

    private void addRequestData(List<Users> users,Context context,Map<String,String> status) {
        String defaultPic = "defaultpic.png";
        availableList = new ArrayList<ListRowStruct2>();
        for(Users user:users){
            String statusVal = status.get(user.email);
            availableList.add(new ListRowStruct2(defaultPic,user.name,user.origin,
                    user.destination,user.email,user.mobilenumber,statusVal));
        }
        Intent i = new Intent(context,RequestList.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("availableList", (Serializable) availableList);
        context.startActivity(i);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.requestmenu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.RequestMenu:
//                RequestList req = new RequestList();
//                req.getRequestListData(this);
//                return true;
//            case R.id.Uninstall:
//                Uri packageUri = Uri.parse("package:"+this.getApplication().getPackageName());
//                Intent uninstall = new Intent(Intent.ACTION_DELETE,packageUri);
//                startActivity(uninstall);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
