package jp.relo.cluboff.util;

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
    public final String KEY_ID_APP="IDApp";//pass word
    public final String KEY_MAIL="UserMail";
    public final String VERSION = "version";
    public final String TIME_STOP = "timeStop";
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

    public void setLogin(String userID,String appID,String userMail){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, userID);
        editor.putString(KEY_ID_APP, appID);
        editor.putString(KEY_MAIL, userMail);
        editor.apply();
    }

    public void setVersion(int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VERSION, value);
        editor.apply();
    }
    public int getVersion(){
        return sharedPreferences.getInt(VERSION,0);
    }

    public void setValueStop(long value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME_STOP, value);
        editor.apply();
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
