package jp.relo.cluboff.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import framework.phvtFragment.BaseFragment;
import jp.relo.cluboff.R;
import jp.relo.cluboff.model.ControlWebEventBus;
import jp.relo.cluboff.model.PostDetail;
import jp.relo.cluboff.ui.BaseFragmentBottombar;
import jp.relo.cluboff.ui.BaseFragmentToolbarBottombar;
import jp.relo.cluboff.ui.webview.MyWebViewClient;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.IControlBottom;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class WebViewDetailCouponFragment extends BaseFragment {

    WebView mWebView;
    private String urlType = "";
    private String userid = "";
    private String requestno = "";
    private String senicode = "";
    private String url;
    public IControlBottom iControlBottom;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        url = bundle.getString(Constant.KEY_LOGIN_URL);
        urlType = bundle.getString(Constant.KEY_URL_TYPE);
        if(!CouponListFragment.WILL_NET_SERVER.equals(urlType)){
            userid = bundle.getString(Constant.TAG_USER_ID);
            requestno = bundle.getString(Constant.TAG_REQUESTNO);
            senicode = bundle.getString(Constant.TAG_SENICODE);
        }
        mWebView = (WebView) view.findViewById(R.id.wvCoupon);
        setupWebView();

    }
    public void setControlBottom(IControlBottom iControlBottom){
        this.iControlBottom =iControlBottom;
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(iControlBottom!=null){
            iControlBottom.canReload(true);
        }
    }


    @Override
    protected void registerEventHandlers() {

    }

    private void setupWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //Disable cache Webview
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mWebView.setWebViewClient(new MyWebViewClient(getActivity()) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading(getActivity());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
                if(iControlBottom!=null){
                    iControlBottom.canBack(mWebView.canGoBack());
                    iControlBottom.canForward(mWebView.canGoForward());
                }
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
        if(CouponListFragment.WILL_NET_SERVER.equals(urlType)){
            mWebView.loadUrl(url);
        }else{
            PostDetail postDetail = new PostDetail();
            postDetail.setUserid(userid);
            postDetail.setRequestno(requestno);
            postDetail.setSenicode(senicode);
            mWebView.postUrl( url, postDetail.toString().getBytes());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(iControlBottom!=null){
            iControlBottom.disableAll();
        }
    }
    @Subscribe
    public void onEvent(ControlWebEventBus event) {
        if(event.isCallBack()){
            mWebView.goBack();
        }else if(event.isCallForward()){
            mWebView.goForward();
        }else {
            mWebView.loadUrl( "javascript:window.location.reload( true )" );
            //mWebView.reload();
        }
    }

}
