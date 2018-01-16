package jp.relo.cluboff.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.util.Utils;

/**
 * Created by tonkhanh on 1/9/18.
 */

public class MyWebview extends WebView {
    public MyWebview(Context context) {
        super(context);
        initView(context);
    }

    public MyWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @SuppressLint("JavascriptInterface")
    private void initView(Context context){
        // i am not sure with these inflater lines
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WebSettings webSettings = getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setUserAgentString(webSettings.getUserAgentString()+"ReloClub");

        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.clearHistory();
        this.clearCache(true);
        this.clearFormData();
    }
}
