package main;

import android.app.Application;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;

import main.ui.webview.CustomWebViewClient;
import main.util.Constant;

/**
 * Created by HuyTran on 3/21/17.
 */

public class ReloApp extends Application {
    private Tracker mTracker;

    private WebView wvForgetPass;
    private WebView wvCanNotLogin;
    private WebView wvAreaCoupon;
    private WebView wvMemberCoupon;

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
        wvForgetPass=setupWebview(Constant.WEBVIEW_URL_FORGET_LOGIN);
        wvCanNotLogin=setupWebview(Constant.WEBVIEW_URL_CAN_NOT_LOGIN);
        wvAreaCoupon=setupWebview(Constant.WEBVIEW_URL_AREA_COUPON);
        wvMemberCoupon=setupWebview(Constant.WEBVIEW_URL_MEMBER_COUPON);

    }

    private WebView setupWebview(String url){
        ////////////// setting webview
        WebView mWebView = new WebView(getApplicationContext());
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //set responsive
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //set zoomable
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        //////////////

        mWebView.setWebViewClient(new CustomWebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage(getResources().getString(R.string.error_ssl));
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
            }

        });
        mWebView.loadUrl(url);
        return mWebView;
    }
    public WebView getWebView(int index){
        switch (index){
            case Constant.FORGET_PASSWORD:
                return wvForgetPass;
            case Constant.CAN_NOT_LOGIN:
                return wvCanNotLogin;
            case Constant.AREA_COUPON:
                return wvAreaCoupon;
            case Constant.MEMBER_COUPON:
                return wvMemberCoupon;
        }
        return null;
    }

}
