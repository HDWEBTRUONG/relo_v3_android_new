package jp.relo.cluboff.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.model.MemberPost;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.model.ReloadEvent;
import jp.relo.cluboff.model.SaveLogin;
import jp.relo.cluboff.ui.BaseDialogFragmentToolbarBottombar;
import jp.relo.cluboff.ui.webview.MyWebViewClient;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class PostMemberWebViewFragment extends BaseDialogFragmentToolbarBottombar {

    WebView mWebView;
    private int checkWebview;
    boolean isLoadding = false;
    boolean isVisibleToUser;
    ProgressBar horizontalProgress;

    public static PostMemberWebViewFragment newInstance() {
        return new PostMemberWebViewFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ReloApp)getActivity().getApplication()).trackingAnalytics(Constant.GA_MEMBER_SCREEN);
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

        checkWebview = getArguments().getInt(Constant.KEY_CHECK_WEBVIEW, Constant.MEMBER_COUPON);
        mWebView = (WebView) view.findViewById(R.id.wvCoupon);
        horizontalProgress = (ProgressBar) view.findViewById(R.id.horizontalProgress);
        setupWebView();
    }

    @Override
    public void setupBottomlbar() {
        switch (checkWebview){
            case Constant.MEMBER_COUPON:
                lnBottom.setVisibility(View.VISIBLE);
                imvBackBottomBar.setVisibility(View.VISIBLE);
                imvForwardBottomBar.setVisibility(View.VISIBLE);

                //Test
                imvBrowserBottomBar.setVisibility(View.VISIBLE);
                llBrowser.setEnabled(true);

                imvReloadBottomBar.setVisibility(View.VISIBLE);
                break;
        }
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
                LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
                if(loginSharedPreference!=null){
                    try {
                        Intent internetIntent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(Constant.URL_MEMBER_BROWSER)
                                .buildUpon()
                                .appendQueryParameter("APPU", loginSharedPreference.getKEY_APPU())
                                .appendQueryParameter("APPP", loginSharedPreference.getKEY_APPP())
                                .build();
                        internetIntent.setData(uri);
                        getActivity().startActivity(internetIntent);
                        AppLog.log("URL: "+ uri.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
        tvMenuTitle.setText(R.string.member_site_title);
        tvMenuSubTitle.setText(R.string.title_member);
        ivMenuRight.setVisibility(View.VISIBLE);
        ivMenuRight.setImageResource(R.drawable.icon_close);
        ivMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    public void bindView(View view) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
                isLoadding = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isLoadding = false;
                if(isVisible()){
                    imvBackBottomBar.setEnabled(mWebView.canGoBack());
                    imvForwardBottomBar.setEnabled(mWebView.canGoForward());
                    llBack.setEnabled(mWebView.canGoBack());
                    llForward.setEnabled(mWebView.canGoForward());
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }

        });
        mWebView.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                    dismiss();
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
        });
        loadUrl();
    }

    private void loadUrl(){
        String url = Constant.TEMPLATE_URL_MEMBER;
        MemberPost memberPost = new MemberPost();
        SaveLogin saveLogin = SaveLogin.getInstance(getActivity());
        if(saveLogin!=null){
            LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
            String  userID = loginSharedPreference.getUserName();
            String  pass = loginSharedPreference.getPass();
            memberPost.setU(userID);
            memberPost.setCOA_APP(pass);
        }
        mWebView.postUrl( url, memberPost.toString().getBytes());
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
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        mWebView.stopLoading();
    }

    @Subscribe
    public void onEvent(ReloadEvent event) {
        if(event.isReload()&&!isLoadding){
            loadUrl();
        }
    }
}
