package net.fukuri.memberapp.memberapp.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

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
        webSettings.setSupportMultipleWindows(false);
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setUserAgentString(webSettings.getUserAgentString()+"ReloClub");
        webSettings.setAllowFileAccess(true);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);



        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.clearHistory();
        this.clearCache(true);
        this.clearFormData();
    }
}
