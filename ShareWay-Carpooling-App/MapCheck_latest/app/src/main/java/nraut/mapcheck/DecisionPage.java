package nraut.mapcheck;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class DecisionPage extends BaseActivity {

    SharedPrefEmail obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicision_page);
        Intent intent = getIntent();
        String loginEmail = intent.getStringExtra("loginEmail");
        obj = new SharedPrefEmail(getApplicationContext());
        //String loginEmail1 = loginEmail.toLowerCase();
        obj.saveEmail(loginEmail,"LoginEmail");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    public void startMapActivity(View view){
        Button driverBtn = (Button)findViewById(R.id.driver);
        Button passengerBtn = (Button) findViewById(R.id.passenger);
        String type = null;
        if(driverBtn.isPressed()){
            type = "DRIVER";
        }
        else if(passengerBtn.isPressed()){
            type = "PASSENGER";
        }
        obj.saveEmail(type,"type");
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        //finish();
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

}
