package main;

import android.app.Application;
import android.os.Environment;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;

/**
 * Created by HuyTran on 3/21/17.
 */

public class ReloApp extends Application {

    protected File extStorageAppBasePath;

    protected File extStorageAppCachePath;

    private Tracker mTracker;

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

    @Override
    public void onCreate() {
        super.onCreate();
        // Check if the external storage is writeable
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            // Retrieve the base path for the application in the external storage
            File externalStorageDir = Environment.getExternalStorageDirectory();

            if (externalStorageDir != null)
            {
                // {SD_PATH}/Android/data/com.blogspot.geekonjava.androidwebviewcacheonsd
                extStorageAppBasePath = new File(externalStorageDir.getAbsolutePath() +
                        File.separator + "Android" + File.separator + "data" +
                        File.separator + getPackageName());
            }

            if (extStorageAppBasePath != null)
            {
                // {SD_PATH}/Android/data/com.blogspot.geekonjava.androidwebviewcacheonsd/cache
                extStorageAppCachePath = new File(extStorageAppBasePath.getAbsolutePath() +
                        File.separator + "cache");

                boolean isCachePathAvailable = true;

                if (!extStorageAppCachePath.exists())
                {
                    // Create the cache path on the external storage
                    isCachePathAvailable = extStorageAppCachePath.mkdirs();
                }

                if (!isCachePathAvailable)
                {
                    // Unable to create the cache path
                    extStorageAppCachePath = null;
                }
            }
        }
    }
    @Override
    public File getCacheDir()
    {
        // NOTE: this method is used in Android 2.2 and higher

        if (extStorageAppCachePath != null)
        {
            // Use the external storage for the cache
            return extStorageAppCachePath;
        }
        else
        {
            // /data/data/com.blogspot.geekonjava.androidwebviewcacheonsd/cache
            return super.getCacheDir();
        }
    }
}
