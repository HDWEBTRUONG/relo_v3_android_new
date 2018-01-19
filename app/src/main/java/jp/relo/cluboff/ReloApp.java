package jp.relo.cluboff;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import framework.phvtCommon.AppState;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.util.ase.EventBusTimeReload;
import rx.Notification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;


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
