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
import android.widget.CheckBox;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button facebookButton;
    private Button googleButton;
    private Button loginButton;
    private EditText username ;
    private EditText password;
    //private CheckBox rememberLoginBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
        initUI();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        String appVersion = "v1";
        Backendless.initApp(this, "70FB1EEF-DCFE-793B-FF27-12585EB80800", "2C883CD4-31B8-03DB-FF5D-954B2545B800", appVersion);
    }

    private void initUI() {
        facebookButton = (Button) findViewById( R.id.loginFacebookButton );
        googleButton = (Button) findViewById(R.id.loginGoogleButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        username = (EditText)findViewById(R.id.UserName);
        password = (EditText)findViewById(R.id.Password);
        //rememberLoginBox = (CheckBox) findViewById( R.id.rememberLoginBox );

        // Login buttin is clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked();
            }
        });
        //Facebook Buton clicked
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginWithFacebookButtonClicked();
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginWithGoogleButtonClicked();
            }
        });

    }

    public void onLoginButtonClicked()
    {
        final String userName = username.getText().toString();
        String passWord = password.getText().toString();
//        boolean rememberLogin = rememberLoginBox.isChecked();

        Backendless.UserService.login(userName, passWord, new DefaultCallback<BackendlessUser>(MainActivity.this) {
            public void handleResponse(BackendlessUser backendlessUser) {
                super.handleResponse(backendlessUser);
                System.out.println("Login Successfull");
                Intent intent = new Intent(MainActivity.this, DecisionPage.class);
                intent.putExtra("loginEmail", userName);
                startActivity(intent);
            }
        }, false);
    }



    private void onLoginWithFacebookButtonClicked() {
        Map<String, String> facebookFieldsMapping = new HashMap<>();
        facebookFieldsMapping.put( "name", "name" );
        facebookFieldsMapping.put( "gender", "gender" );
        facebookFieldsMapping.put( "email", "email" );

        List<String> facebookPermissions = new ArrayList<>();
        facebookPermissions.add( "email" );

        Backendless.UserService.loginWithFacebook(MainActivity.this, null,
                facebookFieldsMapping, facebookPermissions,
                new SocialCallback<BackendlessUser>(MainActivity.this) {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        System.out.println("Facebook Logged in");

                        startActivity(new Intent(getBaseContext(), DecisionPage.class));
                        finish();
                    }
                });
    }

    public void onLoginWithGoogleButtonClicked()
    {
        Map<String, String> googleFieldsMapping = new HashMap<>();
        googleFieldsMapping.put( "name", "name" );
        googleFieldsMapping.put( "gender", "gender" );
        googleFieldsMapping.put("email", "email");

        List<String> googlePermissions = new ArrayList<>();

        Backendless.UserService.loginWithGooglePlus(MainActivity.this, null,
                googleFieldsMapping, googlePermissions, new SocialCallback<BackendlessUser>(MainActivity.this) {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                startActivity(new Intent(getBaseContext(), DecisionPage.class));
                finish();
            }
        });
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

    public void onRegister(View view){
        Intent intent = new Intent(this,Profile_Creation.class);
        startActivity(intent);
        finish();
    }

    public void onLogin(View view){
        EditText email = (EditText)findViewById(R.id.UserName);
        String loginEmail = email.getText().toString().toLowerCase();
        Intent intent = new Intent(this,DecisionPage.class);
        intent.putExtra("loginEmail",loginEmail);
        startActivity(intent);
        finish();
    }
}
