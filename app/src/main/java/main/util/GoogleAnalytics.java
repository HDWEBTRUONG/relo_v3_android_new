package main.util;

import android.app.Activity;

import main.ReloApp;

/**
 * Created by HuyTran on 3/23/17.
 */

public class GoogleAnalytics {

    //General
    public static void trackingAnalytics(Activity activity, Boolean isScreenOnly, String screenName, String category, String action, String label, long value) {
        if (activity != null) {
            ReloApp application = (ReloApp) activity.getApplication();
            if (isScreenOnly) {
                application.trackingAnalyticByScreen(screenName);
            } else {
                application.trackingWithAnalyticGoogleServices(category, action, label, value);
            }
        }
    }

    //Screen Only
    public static void trackingAnalytics(Activity activity, String screenName) {
        trackingAnalytics(activity, true, screenName, "", "", "", 0);
    }

    //Event
    public static void trackingAnalytics(Activity activity, String category, String action, String label, long value) {
        trackingAnalytics(activity, false, "", category, action, label, value);
    }
}
