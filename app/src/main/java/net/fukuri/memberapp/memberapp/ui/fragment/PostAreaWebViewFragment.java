package net.fukuri.memberapp.memberapp.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.ReloApp;
import net.fukuri.memberapp.memberapp.model.AreaCouponPost;
import net.fukuri.memberapp.memberapp.model.MessageEvent;
import net.fukuri.memberapp.memberapp.ui.BaseDialogFragmentToolbarBottombar;
import net.fukuri.memberapp.memberapp.ui.webview.MyWebViewClient;
import net.fukuri.memberapp.memberapp.util.Constant;
import net.fukuri.memberapp.memberapp.util.LoginSharedPreference;
import net.fukuri.memberapp.memberapp.views.MyWebview;

import org.greenrobot.eventbus.EventBus;

import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class PostAreaWebViewFragment extends BaseDialogFragmentToolbarBottombar {

    MyWebview mWebView;
    private String strPost;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION};
    boolean isLoadding = false;
    boolean isVisibleToUser;
    ProgressBar horizontalProgress;
    String logUrl="";

    public static final String TAG = PostAreaWebViewFragment.class.getSimpleName();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ReloApp)getActivity().getApplication()).trackingAnalytics(Constant.GA_AREA_SCREEN);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_UP){
                    EventBus.getDefault().post(new MessageEvent(Constant.TOP_COUPON));
                    return true;

                }
                return false;
            }
        });



        mWebView = (MyWebview) view.findViewById(R.id.wvCoupon);
        horizontalProgress = (ProgressBar) view.findViewById(R.id.horizontalProgress);
        setupWebView();

        if (!checkPermissions()) {
            requestPermission();
        }else{
            loadUrl("");
        }
    }

    @Override
    public void setupBottomlbar() {
        lnBottom.setVisibility(View.VISIBLE);
        imvBackBottomBar.setVisibility(View.VISIBLE);
        imvForwardBottomBar.setVisibility(View.VISIBLE);

        imvReloadBottomBar.setVisibility(View.VISIBLE);

        // disable button browser
        llBrowser.setEnabled(false);
        imvBrowserBottomBar.setVisibility(View.GONE);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imvBackBottomBar.isEnabled()){
                    mWebView.goBack();
                    imvBackBottomBar.setEnabled(false);
                    llBack.setEnabled(false);
                }
            }
        });
        llForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goForward();
                imvForwardBottomBar.setEnabled(false);
                llForward.setEnabled(false);
            }
        });
        llBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl())));
            }
        });

        llReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.log("URL: "+logUrl);
                if(StringUtil.isEmpty(logUrl) || logUrl.contains("loc.htm")){
                    if (!checkPermissions()) {
                        requestPermission();
                    }else{
                        loadUrl("");
                    }
                }else{
                   // mWebView.loadUrl( "javascript:window.location.reload( true )" );
                    loadUrl(logUrl);
                }
            }
        });

        imvBackBottomBar.setEnabled(mWebView.canGoBack());
        imvForwardBottomBar.setEnabled(mWebView.canGoForward());
        llBack.setEnabled(mWebView.canGoBack());
        llForward.setEnabled(mWebView.canGoForward());
    }

    @Override
    public void setupActionBar() {
        ivMenuRight.setVisibility(View.VISIBLE);
        ivMenuRight.setImageResource(R.drawable.icon_close);
        ivMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvMenuTitle.setVisibility(View.VISIBLE);
        tvMenuSubTitle.setVisibility(View.VISIBLE);
        tvMenuTitle.setText(R.string.show_guide);
        tvMenuSubTitle.setText(R.string.sub_show_guide);
    }



    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    public void bindView(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void setupWebView() {
        mWebView.setWebViewClient(new MyWebViewClient(getActivity()) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isLoadding = true;
                if(isVisibleToUser){
                    showLoading(getActivity());
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                logUrl = url;
                isLoadding = false;
                AppLog.log("URL: "+url);
                if(isVisible()){
                    hideLoading();
                    if(mWebView.getUrl().endsWith("nmap.htm")){
                        imvBackBottomBar.setEnabled(false);
                    }else{
                        imvBackBottomBar.setEnabled(mWebView.canGoBack());
                    }
                    imvForwardBottomBar.setEnabled(mWebView.canGoForward());
                    llBack.setEnabled(mWebView.canGoBack());
                    llForward.setEnabled(mWebView.canGoForward());
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if(isVisible()){
                    hideLoading();
                }
                AppLog.log("Web error");
                loadUrl("");
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    horizontalProgress.setVisibility(View.GONE);
                } else {
                    horizontalProgress.setVisibility(View.VISIBLE);
                    horizontalProgress.setProgress(newProgress);
                }
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                    EventBus.getDefault().post(new MessageEvent(Constant.TOP_COUPON));
                    return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if(isVisibleToUser){
            if(isLoadding){
                showLoading(getActivity());
            }
        }
    }

    private boolean checkPermissions() {
        int findLoca = ContextCompat.checkSelfPermission(getActivity(), permissions[0]);
        return findLoca == PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermission() {
        requestPermissions(permissions, MULTIPLE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean location = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (location) {
                        Toast.makeText(getActivity(), R.string.premission_accepted, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), R.string.premissionaccepted_no_accepted, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.premission_error, Toast.LENGTH_SHORT).show();
                }
                loadUrl("");
                break;

        }
    }

    private void loadUrl(String mUrl) {
        if(StringUtil.isEmpty(mUrl)){
            mUrl = Constant.WEBVIEW_URL_AREA_COUPON;
        }
        AreaCouponPost areaCouponPost = new AreaCouponPost();
        areaCouponPost.setP_s7(LoginSharedPreference.getInstance(getActivity()).getUserName().replaceAll("-",""));
        strPost =areaCouponPost.toString();
        AppLog.log(strPost);
        mWebView.postUrl( mUrl, strPost.getBytes());
    }
}
