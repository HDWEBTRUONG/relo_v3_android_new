package jp.relo.cluboff.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.ui.BaseFragmentToolbarBottombar;
import jp.relo.cluboff.ui.webview.MyWebViewClient;
import jp.relo.cluboff.util.Constant;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class WebViewFragment extends BaseFragmentToolbarBottombar {

    WebView mWebView;
    private int checkWebview;
    private String url;
    RelativeLayout myContainer;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        url = getArguments().getString(Constant.KEY_LOGIN_URL);
        checkWebview = getArguments().getInt(Constant.KEY_CHECK_WEBVIEW, Constant.FORGET_PASSWORD);
        mWebView = ((ReloApp)getActivity().getApplication()).getWebView(checkWebview);

        setupWebView(mWebView);

        myContainer = (RelativeLayout) view.findViewById(R.id.lnContainerWv);
        if(mWebView!=null&&mWebView.getParent()!=null){
            ((RelativeLayout)mWebView.getParent()).removeView(mWebView);
        }
        if(checkWebview==Constant.DETAIL_COUPON||checkWebview==Constant.AREA_COUPON){
            mWebView.loadUrl(url);
            AppLog.log("URL: "+url);
        }
        myContainer.addView(mWebView,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setupToolbar() {
        switch (checkWebview){
            case Constant.FORGET_PASSWORD:
                lnToolbar.setVisibility(View.VISIBLE);
                title_toolbar.setVisibility(View.VISIBLE);
                title_toolbar.setText(R.string.forget_title);
                imvMenu.setVisibility(View.VISIBLE);
                imvMenu.setImageResource(R.drawable.icon_close);
                imvMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
                break;
            case Constant.CAN_NOT_LOGIN:
                lnToolbar.setVisibility(View.VISIBLE);
                title_toolbar.setVisibility(View.VISIBLE);
                title_toolbar.setText(R.string.cannot_login_title);
                imvMenu.setVisibility(View.VISIBLE);
                imvMenu.setImageResource(R.drawable.icon_close);
                imvMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
                break;
        }

    }

    @Override
    public void setupBottombar() {
        switch (checkWebview){
            case Constant.AREA_COUPON:
                lnBottom.setVisibility(View.VISIBLE);
                imvBackBottomBar.setVisibility(View.VISIBLE);
                imvForwardBottomBar.setVisibility(View.VISIBLE);
                imvBrowserBottomBar.setVisibility(View.GONE);
                imvReloadBottomBar.setVisibility(View.VISIBLE);
                break;
            case Constant.MEMBER_COUPON:
                lnBottom.setVisibility(View.VISIBLE);
                imvBackBottomBar.setVisibility(View.VISIBLE);
                imvForwardBottomBar.setVisibility(View.VISIBLE);
                imvBrowserBottomBar.setVisibility(View.VISIBLE);
                imvReloadBottomBar.setVisibility(View.VISIBLE);
                break;
        }
        imvBackBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goBack();
                imvBackBottomBar.setEnabled(false);
            }
        });
        imvForwardBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goForward();
                imvForwardBottomBar.setEnabled(false);
            }
        });
        imvBrowserBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl())));
            }
        });

        imvReloadBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });

        imvBackBottomBar.setEnabled(mWebView.canGoBack());
        imvForwardBottomBar.setEnabled(mWebView.canGoForward());
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void registerEventHandlers() {

    }

    private void setupWebView(final WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //Disable cache Webview
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.setWebViewClient(new MyWebViewClient(getActivity()) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading(getActivity());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
                imvBackBottomBar.setEnabled(webView.canGoBack());
                imvForwardBottomBar.setEnabled(webView.canGoForward());
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //TODO hide loading
                hideLoading();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                //TODO hide loading
                hideLoading();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mWebView!=null&&mWebView.getParent()!=null){
            ((RelativeLayout)mWebView.getParent()).removeView(mWebView);
        }
    }

}
