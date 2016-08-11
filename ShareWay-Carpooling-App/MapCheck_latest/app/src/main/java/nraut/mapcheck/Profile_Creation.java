package nraut.mapcheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Profile_Creation extends AppCompatActivity {
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phone;
    EditText pass;
    EditText cnfpass;

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    String KEY_NAME = "email";

    // Sharedpref file name
    private static final String PREF_NAME = "LoginEmail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__creation);
    }

    public void onSignUp(View view){
        firstName = (EditText)findViewById(R.id.FirstName);
        lastName = (EditText)findViewById(R.id.LastName) ;
        email = (EditText)findViewById(R.id.EmailId) ;
        phone = (EditText)findViewById(R.id.Phonenumber);
        pass = (EditText)findViewById(R.id.Password);
        cnfpass = (EditText)findViewById(R.id.CnfPassword);
        String pass1 = pass.getText().toString();
        String cnfpass1 = cnfpass.getText().toString();

        if(pass1.equals(cnfpass1)){
            String name = firstName.getText().toString() +" "+ lastName.getText().toString();
            String email1 = email.getText().toString().toLowerCase();
            String phone1 = phone.getText().toString();
            String pic = "defaultpic.png";
            DatabaseConnection db = new DatabaseConnection();
            //db.saveLoginTable(email1, name, pic, pass1, phone1);
            db.saveUsersTable(email1,name,pic,pass1,phone1);
            Intent intent = new Intent(this,DecisionPage.class);
            intent.putExtra("loginEmail",email1);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this,"Confirm password is not same as entered password",Toast.LENGTH_SHORT).show();
        }
    }
}
