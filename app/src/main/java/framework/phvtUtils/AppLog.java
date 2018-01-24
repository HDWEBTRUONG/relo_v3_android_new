package framework.phvtUtils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import net.fukuri.memberapp.BuildConfig;


/**
 * Framework PHATVT
 *
 * @author PhatVan ヴァン  タン　ファット
 * @since:  11-2015
 *
 */

public class AppLog {
    //TAG Class
    static String TAG = "PHVT";


    // --------------------------------------------------------
    // Show Log info
    synchronized public static void log(String content) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, content);
        }
    }

    synchronized public static void log(String tag, String content) {
            if (BuildConfig.DEBUG) {
                Log.i(tag, content);
            }
    }

    // --------------------------------------------------------
    // Show Log Error
    synchronized public static void log_error(String content) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, content);
        }
    }

    // --------------------------------------------------------
    // Show Log URL
    synchronized public static void log_url(String content) {
        if (BuildConfig.DEBUG) {
            Log.i("", "--------------------------------------");
            Log.i(TAG, content);
            Log.i("", "--------------------------------------");
        }
    }


    // Show log Webview HOST - APARAMS
    synchronized public static void showLogWebview(String _host , String _param ){
        String _pra[];

        if (BuildConfig.DEBUG) {

            _pra = _param.split("&");
            //
            log("---------------------------------------------");
            //host data
            log(_host);

            // org params data
            log("ORG PARAMS :   " + _param);

            // params elements anystic
            String s = "";
            if (_pra.length > 0) {
                for (int i = 0; i < _pra.length; i++) {
                    s = s + _pra[i] + "\n";
                }
            }
            log(s);
            log("---------------------------------------------");
        }
    }


    //Log JSON
    synchronized  public static void logJson(String tag,String title,String data){
        if (BuildConfig.DEBUG) {
            try {
                Log.i(tag, title);
                JSONObject obj = new JSONObject(data);
                Log.d(tag, obj.toString(4));
            } catch (JSONException e) {
                Log.d(TAG, "Data: "+data);
                e.printStackTrace();
            }
        }
    }


}
