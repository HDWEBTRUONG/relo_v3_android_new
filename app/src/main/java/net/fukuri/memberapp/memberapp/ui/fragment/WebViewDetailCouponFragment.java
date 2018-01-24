package net.fukuri.memberapp.memberapp.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import framework.phvtUtils.AppLog;
import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.ReloApp;
import net.fukuri.memberapp.memberapp.model.PostDetail;
import net.fukuri.memberapp.memberapp.ui.BaseDialogFragmentToolbarBottombar;
import net.fukuri.memberapp.memberapp.ui.webview.MyWebViewClient;
import net.fukuri.memberapp.memberapp.util.Constant;
import net.fukuri.memberapp.memberapp.views.MyWebview;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class WebViewDetailCouponFragment extends BaseDialogFragmentToolbarBottombar {

    MyWebview mWebView;
    private String url;
    private String kaiinno = "";
    private String shgrid = "";
    private String senicode = "";
    private String urlType = "";
    ProgressBar horizontalProgress;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_UP){
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });

        Bundle bundle = getArguments();
        url = bundle.getString(Constant.KEY_LOGIN_URL);
        urlType = bundle.getString(Constant.KEY_URL_TYPE);
        if(!CouponListFragment.WILL_NET_SERVER.equals(urlType)){
            kaiinno = bundle.getString(Constant.TAG_KAIINNO);
            senicode = bundle.getString(Constant.TAG_SENICODE);
        }
        shgrid = bundle.getString(Constant.TAG_SHGRID);
        mWebView = (MyWebview) view.findViewById(R.id.wvCoupon);
        horizontalProgress = (ProgressBar) view.findViewById(R.id.horizontalProgress);
        setupWebView();
        ((ReloApp)getActivity().getApplication()).trackingAnalytics(Constant.GA_DETAIL_SCREEN);
        setGoogleAnalyticDetailCoupon(Constant.GA_CATALOGY_DETAIL,Constant.GA_ACTION_DETAIL,shgrid,Constant.GA_VALUE_DETAIL);

    }

    @Override
    public void setupBottomlbar() {
        lnBottom.setVisibility(View.VISIBLE);
        imvBackBottomBar.setVisibility(View.VISIBLE);
        imvForwardBottomBar.setVisibility(View.VISIBLE);
        imvBrowserBottomBar.setVisibility(View.VISIBLE);
        imvReloadBottomBar.setVisibility(View.VISIBLE);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goBack();
                imvBackBottomBar.setEnabled(false);
                llBack.setEnabled(false);
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
                mWebView.loadUrl( "javascript:window.location.reload( true )" );
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
        tvMenuSubTitle.setText(R.string.title_detail_coupon);
    }
    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    public void bindView(View view) {

    }
    @Override
    public void onStop() {
        super.onStop();
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
                if(isVisible()){
                    imvBackBottomBar.setEnabled(mWebView.canGoBack());
                    imvForwardBottomBar.setEnabled(mWebView.canGoForward());
                    llBack.setEnabled(mWebView.canGoBack());
                    llForward.setEnabled(mWebView.canGoForward());
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.clearCache(true);
            }
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //super.onReceivedError(view, request, error);
                hideLoading();
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                hideLoading();
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                    dismiss();
                    return true;

                }
                return false;
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient(){
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
                tvMenuTitle.setText(title);
            }
        });
        loadDetail();
    }
    private void loadDetail(){
        if(CouponListFragment.WILL_NET_SERVER.equals(urlType)){
            mWebView.loadUrl(url);
        }else{
            PostDetail postDetail = new PostDetail();
            postDetail.setKaiinno(kaiinno);
            postDetail.setRequestno(shgrid);
            postDetail.setSenicode(senicode);

            mWebView.postUrl( url, postDetail.toString().getBytes());
            AppLog.log(url);
            AppLog.log(postDetail.toString());
        }
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
}
