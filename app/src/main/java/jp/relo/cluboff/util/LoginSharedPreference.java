package jp.relo.cluboff.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tonkhanh on 5/26/17.
 */

public class LoginSharedPreference {
    public final String KEY_USER="userID";
    public final String KEY_ID_APP="IDApp";//pass word
    public final String KEY_MAIL="UserMail";
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
    public void setAppID(String appID){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID_APP, appID);
        editor.apply();
    }
    public void setUserMail(String userMail){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MAIL, userMail);
        editor.apply();
    }
    public void setLogin(String userID,String appID,String userMail){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, userID);
        editor.putString(KEY_ID_APP, appID);
        editor.putString(KEY_MAIL, userMail);
        editor.apply();
    }
    public String getUserID(){
        return sharedPreferences.getString(KEY_USER,"");
    }
    public String getAppID(){
        return sharedPreferences.getString(KEY_ID_APP,"");
    }
    public String getUserMail(){
        return sharedPreferences.getString(KEY_MAIL,"");
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
