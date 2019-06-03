package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ashis on 26/03/2017.
 */

public class SPLib {
    private static final String SP_FILE_NAME = "women_safety";
    public SharedPreferences sharedpreferences;
    Context context;

    public SPLib(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getPref(String key) {
        return sharedpreferences.getString(key, null);
    }

    public void clearSharedPrefs() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean checkSharedPrefs(String key) {
        if (sharedpreferences.contains(key)) {
            return true;
        }
        return false;
    }

    public SharedPreferences getInstance() {
        return sharedpreferences;
    }
    public class Key
    {
        public static final String MyPREFERENCES = "MyPrefs" ;
        public static final String Sp_firstname= "Sp_Firstname" ;
        public static final String Sp_lastname = "Sp_Lastname" ;
        public static final String Sp_email= "Sp_Email" ;
        public static final String Sp_contactno= "Sp_Contactno" ;



    }
}
