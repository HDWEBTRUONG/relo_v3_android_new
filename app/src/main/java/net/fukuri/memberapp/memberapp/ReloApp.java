package net.fukuri.memberapp.memberapp;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import net.fukuri.memberapp.memberapp.R;

import framework.phvtUtils.AppLog;


/**
 * Created by HuyTran on 3/21/17.
 */

public class ReloApp extends Application {
    private Tracker mTracker;
    private static boolean isUpdateData;
    private static int versionApp;
    public static boolean blockAuth = false;

    public static boolean isUpdateData() {
        return isUpdateData;
    }

    public static void setIsUpdateData(boolean isUpdateData) {
        ReloApp.isUpdateData = isUpdateData;
    }

    public static int getVersionApp() {
        return versionApp;
    }

    public static void setVersionApp(int versionApp) {
        ReloApp.versionApp = versionApp;
    }

    public static boolean isBlockAuth() {
        return blockAuth;
    }

    public static void setBlockAuth(boolean blockAuth) {
        ReloApp.blockAuth = blockAuth;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        isUpdateData = false;
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
        AppLog.log("Tracked: "+nameScreen);

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
