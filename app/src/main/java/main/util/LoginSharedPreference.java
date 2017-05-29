package main.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tonkhanh on 5/26/17.
 */

public class LoginSharedPreference {
    public final String KEY_USER="userID";
    public final String KEY_PASS="password";
    public final String VERSION = "version";
    public static LoginSharedPreference sharedPreference;

    private final SharedPreferences sharedPreferences;
    Context context;

    public static LoginSharedPreference getInstance(Context context) {
        if (sharedPreference == null) {
            sharedPreference = new LoginSharedPreference(context);
        }
        return sharedPreference;
    }

    public LoginSharedPreference(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void setUserName(String userID){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, userID);
        editor.apply();
    }
    public void setPassword(String password){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PASS, password);
        editor.apply();
    }
    public void setLogin(String userID,String password){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, userID);
        editor.putString(KEY_PASS, password);
        editor.apply();
    }
    public String getUserID(){
        return sharedPreferences.getString(KEY_USER,"");
    }
    public String getPassword(){
        return sharedPreferences.getString(KEY_PASS,"");
    }
    public void setVersion(int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VERSION, value);
        editor.apply();
    }
    public int getVersion(){
        return sharedPreferences.getInt(VERSION,0);
    }
}
