package net.fukuri.memberapp.memberapp.util;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import net.fukuri.memberapp.memberapp.ui.activity.ProfilePassportActivity;

import framework.phvtUtils.AppLog;

public class MyJavascriptInterface {
    WebView webView;
    ProfilePassportActivity activity;

    public MyJavascriptInterface(ProfilePassportActivity activity, WebView w) {
        this.webView = w;
        this.activity = activity;
    }

    @JavascriptInterface
    public void callAllowedSDKPassport() {
        AppLog.log("---callAllowedSDKPassport-");
        activity.callAllowedSDKPassport();
    }

    @JavascriptInterface
    public void callDeniedSDKPassport() {
        AppLog.log("---callDeniedSDKPassport-");
        activity.callDeniedSDKPassport();
    }
}
