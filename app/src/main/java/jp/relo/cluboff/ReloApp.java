package jp.relo.cluboff;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import jp.relo.cluboff.util.LoginSharedPreference;


/**
 * Created by HuyTran on 3/21/17.
 */

public class ReloApp extends Application {
    private KeyStore keyStore;
    private int version;
    private Tracker mTracker;



    @Override
    public void onCreate() {
        super.onCreate();
        version = LoginSharedPreference.getInstance(getApplicationContext()).getVersion();

        try {
            keyStore=KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    public KeyStore getKeyStore(){
        return keyStore;
    }
    public int getVersion(){
        return version;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }


    /**
     * Tracking this Action by some params input
     * @param categoryId
     * @param actionId
     * @param labelId
     * @param valueId
     */
    public void trackingWithAnalyticGoogleServices(String categoryId, String actionId, String labelId, long valueId){
        getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(categoryId)
                .setAction(actionId)
                .setLabel(labelId)
                .setValue(valueId)
                .build());

    }


    /**
     * Tracking this Action by screen
     * @param nameScreen
     */
    public void trackingAnalyticByScreen(String nameScreen){
        getDefaultTracker();
        mTracker.setScreenName(nameScreen);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    public void trackingAnalytics(Boolean isScreenOnly, String screenName, String category, String action, String label, long value) {
        if (isScreenOnly) {
            trackingAnalyticByScreen(screenName);
        } else {
            trackingWithAnalyticGoogleServices(category, action, label, value);
        }
    }

    //Screen Only
    public void trackingAnalytics(String screenName) {
        trackingAnalytics(true, screenName, "", "", "", 0);
    }

    //Event
    public void trackingAnalytics(String category, String action, String label, long value) {
        trackingAnalytics(false, "", category, action, label, value);
    }
}
