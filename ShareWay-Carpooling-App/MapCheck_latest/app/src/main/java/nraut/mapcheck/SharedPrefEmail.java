package nraut.mapcheck;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by naina on 5/20/2016.
 */
public class SharedPrefEmail {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    String KEY_NAME = "LoginEmail";

    // Sharedpref file name
    private static final String PREF_NAME = "LoginEmail";

    SharedPrefEmail(Context context){
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveEmail(String email,String keyname){
        String loginEmail = email;
        // Storing email address in pref
        editor.putString(keyname,loginEmail);
        // commit changes
        editor.commit();
    }

    public String getEmail(String keyname){
        return pref.getString(keyname, null);
    }
}
