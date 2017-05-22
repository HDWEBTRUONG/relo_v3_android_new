package main.ui.fragment;

import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import framework.phvtFragment.BaseFragment;
import framework.phvtUtils.AppLog;
import main.R;
import main.ui.BaseFragmentToolbar;
import main.ui.webview.CustomWebViewClient;
import main.util.Constant;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class WebViewFragment extends BaseFragmentToolbar {

    WebView mWebView;
    private ProgressBar horizontalProgress;
    private int checkWebview;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        init(root);

        checkWebview = getArguments().getInt(Constant.KEY_CHECK_WEBVIEW, Constant.FORGET_PASSWORD);

        String url = getArguments().getString(Constant.KEY_LOGIN_URL);

        setupWebview();

        mWebView.loadUrl(url);
    }
    @Override
    public void setupToolbar() {
        imgBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });

        imgForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }

            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mToolbar.setBackgroundResource(R.color.colorMineShaft);
        mToolbarTilte.setVisibility(View.VISIBLE);
        setToolbar(checkWebview);
    }

    private void init(View view) {
        mWebView = (WebView) view.findViewById(R.id.web_view);
        horizontalProgress = (ProgressBar) view.findViewById(R.id.progressBar2);
    }

    private void setToolbar(int checkWebview){
        // Change Title webview
        if( checkWebview== Constant.FORGET_PASSWORD ){ // 1: Forget ID/Password
            mToolbarTilte.setText(getString(R.string.txt_link_forget_id_password));
        }else if(checkWebview== Constant.CAN_NOT_LOGIN){ // 2: You can not login
            mToolbarTilte.setText(getString(R.string.txt_link_can_not_login));
        }else if(checkWebview== Constant.AREA_COUPON){ // 3. coupon area
            mToolbarTilte.setText(getString(R.string.title_coupon_area));
            imgClose.setVisibility(View.GONE);
        }else if(checkWebview== Constant.MEMBER_COUPON){ // 4.coupon membership
            mToolbarTilte.setText(getString(R.string.title_membership));
            imgClose.setVisibility(View.GONE);
        }

        lnGroupTitle.setVisibility(View.VISIBLE);
        lnGroupArrow.setVisibility(View.VISIBLE);
        rlGroupClose.setVisibility(View.VISIBLE);
    }

    private void setupWebview(){
        ////////////// setting webview

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

        //Disable cache Webview
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCachePath(getActivity().getCacheDir().getPath());
        webSettings.setAllowFileAccess(true);

        //////////////

        mWebView.setWebViewClient(new CustomWebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    horizontalProgress.setVisibility(View.GONE);
                } else {
                    horizontalProgress.setProgress(newProgress);
                    horizontalProgress.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void registerEventHandlers() {

    }
}
