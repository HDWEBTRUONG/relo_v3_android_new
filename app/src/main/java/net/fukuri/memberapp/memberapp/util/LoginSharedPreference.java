package net.fukuri.memberapp.memberapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by tonkhanh on 5/26/17.
 */

public class LoginSharedPreference {
    public final String KEY_USER="userID";
    public final String KEY_PASS="passwodk";
    public final String KEY_APPU="appU";
    public final String KEY_APPP="appp";
    public final String KEY_TAB="tab";

    public final String VERSION = "version";
    public final String TIME_STOP = "timeStop";
    public final String FLAG_DOWNLOAD_DONE = "FLAG_DOWNLOAD_DONE";
    public final String FLAT_DENIED_PASSPORT = "FLAT_DENIED_PASSPORT";
    public final String FLAT_REQUIRE_PASSPORT = "FLAT_REQUIRE_PASSPORT";
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

    public void setCookie(String content){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.WEB_COOKIE, content);
        editor.apply();
    }
    public String getCookie(){
        return sharedPreferences.getString(Constant.WEB_COOKIE,"");
    }

    public void setDeniedPassport(boolean isDenide){
        setRequirePassported();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FLAT_DENIED_PASSPORT, isDenide);
        editor.apply();
    }

    public boolean isDeniedPassport(){
        return sharedPreferences.getBoolean(FLAT_DENIED_PASSPORT,false);
    }
    public void setRequirePassported(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FLAT_REQUIRE_PASSPORT, true);
        editor.apply();
    }

    public boolean isRequirePassported(){
        return sharedPreferences.getBoolean(FLAT_REQUIRE_PASSPORT,false);
    }



    public void setVersion(int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VERSION, value);
        editor.apply();
    }
    public int getVersion(){
        return sharedPreferences.getInt(VERSION,0);
    }

    public void setDownloadDone(boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FLAG_DOWNLOAD_DONE, value);
        editor.apply();
    }
    public boolean checkDownloadDone(){
        return sharedPreferences.getBoolean(FLAG_DOWNLOAD_DONE,false);
    }

    public void setValueStop(long value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME_STOP, value);
        editor.apply();
    }

    public void setUserName(String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, value);
        editor.apply();
    }

    public String getUserName(){
        return sharedPreferences.getString(KEY_USER,"");
    }

    public void setPass(String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PASS, value);
        editor.apply();
    }

    public String getPass(){
        return sharedPreferences.getString(KEY_PASS,"");
    }

    public void forceLogout(){
        setPass("");
    }
    public void logout(){
        setUserName("");
        setPass("");
    }

    public void setTabSave(int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_TAB, value);
        editor.apply();
    }

    public int getTabSave(){
        return sharedPreferences.getInt(KEY_TAB,0);
    }

    public void setKEY_APPU(String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_APPU, value);
        editor.apply();
    }

    public String getKEY_APPU(){
        return sharedPreferences.getString(KEY_APPU,"");
    }

    public void setKEY_APPP(String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_APPP, value);
        editor.apply();
    }

    public String getKEY_APPP(){
        return sharedPreferences.getString(KEY_APPP,"");
    }
    public long getValueStop(){
        return sharedPreferences.getLong(TIME_STOP,0);
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        }else {
            editor.putString(key, new Gson().toJson(data));
        }
        editor.apply();
    }
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass) {
        if (anonymousClass == String.class) {
            return (T) sharedPreferences.getString(key, "");
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(sharedPreferences.getBoolean(key, false));
        } else if (anonymousClass == Float.class) {
            return (T) Float.valueOf(sharedPreferences.getFloat(key, 0));
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(sharedPreferences.getInt(key, 0));
        } else if (anonymousClass == Long.class) {
            return (T) Long.valueOf(sharedPreferences.getLong(key, 0));
        }else{
            try{
                return new Gson().fromJson(sharedPreferences.getString(key, ""),anonymousClass);
            }catch (JsonSyntaxException exJ){
               return null;
            }
        }
    }

}
