package nraut.mapcheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfilePage extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    List<Users> data = new ArrayList<Users>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        final EditText fname = (EditText) findViewById(R.id.firstname);
        final EditText phno = (EditText) findViewById(R.id.phoneno);
        final EditText emailId = (EditText) findViewById(R.id.emailid);
        final ImageView image1 = (ImageView) findViewById(R.id.image);

        Intent i = getIntent();
        //data = (List<Users>)i.getSerializableExtra("userdata");
        emailId.setText(i.getStringExtra("Email"));
        fname.setText(i.getStringExtra("name"));
        phno.setText(i.getStringExtra("mobile"));
        emailId.setEnabled(false);
        fname.setEnabled(false);
        phno.setEnabled(false);
    }

    public void showprofile(String loginEmail1, final Context context) {
        String loginEmail = new SharedPrefEmail(getApplicationContext()).getEmail("LoginEmail");
        String whereClause;
        whereClause = "email =" + loginEmail ;

        //whereClause = "email = 'abc@gmail.com'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Backendless.Data.of(Users.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Users>>() {

            @Override
            public void handleResponse(BackendlessCollection<Users> usersBackendlessCollection) {
                Iterator<Users> iterator = usersBackendlessCollection.getCurrentPage().iterator();
                Intent i = new Intent(context, ProfilePage.class);
                if (iterator.hasNext()) {
                    Users user = iterator.next();
                    i.putExtra("Email", user.email);
                    i.putExtra("name", user.name);
                    i.putExtra("mobile", user.mobilenumber);
                }


                System.out.println("Success in profile");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                finish();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Record fetching failed");
            }
        });


    }

    public void onEdit(View view){
        Button b1 = (Button)findViewById(R.id.button2);
        Button b2 = (Button)findViewById(R.id.button);
        final EditText fname = (EditText) findViewById(R.id.firstname);
        final EditText phno = (EditText) findViewById(R.id.phoneno);
        final EditText emailId = (EditText) findViewById(R.id.emailid);
        final ImageView image1 = (ImageView) findViewById(R.id.image);
        b1.setEnabled(true);
        b2.setEnabled(false);
        fname.setEnabled(true);
        phno.setEnabled(true);
    }

    public void onSave(View view)
    {
        //String whereClause = "email = 'abc@gmail.com'";
        final EditText fname = (EditText) findViewById(R.id.firstname);
        final String fname1 = fname.getText().toString();

        final EditText phno = (EditText) findViewById(R.id.phoneno);
        final String phno1 = phno.getText().toString();
        String loginEmail = new SharedPrefEmail(getApplicationContext()).getEmail("LoginEmail");
        String whereClause;
        whereClause = "email =" + loginEmail ;


        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Backendless.Data.of(Users.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Users>>() {
            @Override
            public void handleResponse(BackendlessCollection<Users> usersBackendlessCollection) {
                Iterator<Users> iterater = usersBackendlessCollection.getCurrentPage().iterator();
                if (iterater.hasNext()) {
                    Users userTable = iterater.next();
                    userTable.setName(fname1);
                    userTable.setMobilenumber(phno1);
                    Backendless.Persistence.save(userTable, new AsyncCallback<Users>() {
                        @Override
                        public void handleResponse(Users users) {
                            System.out.println("Sucesss");
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            System.out.println("Error");
                        }
                    });
                }
                else {
                    System.out.println("Record doesnot exists");
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Errorrrr");
            }
        });
    }
}

