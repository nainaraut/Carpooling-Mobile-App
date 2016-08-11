package nraut.mapcheck;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class BaseActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    @Override
    public void setContentView(int layoutResID)
    {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ShareWay");
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("Activity Title");
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

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_aboutus) {
            // Handle the camera action
            Intent intent = new Intent(this, AboutUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            String loginEmail = new SharedPrefEmail(getApplicationContext()).getEmail("LoginEmail");
            ProfilePage profile = new ProfilePage();
            profile.showprofile(loginEmail,this);

        } else if (id == R.id.nav_history) {
//            Intent intent = new Intent(this, AboutUs.class);
//            startActivity(intent);

        } else if (id == R.id.nav_payment) {
//            Intent intent = new Intent(this, AboutUs.class);
//            startActivity(intent);

        } else if (id == R.id.nav_setting) {
//            Intent intent = new Intent(this, AboutUs.class);
//            startActivity(intent);

        }
        else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
            finish();

        }

       // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // drawer.closeDrawer(GravityCompat.START);
        return true;
       }
}
