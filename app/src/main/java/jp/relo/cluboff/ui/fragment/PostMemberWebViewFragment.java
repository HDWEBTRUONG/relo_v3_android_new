package jp.relo.cluboff.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.MessageFormat;

import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.model.ControlWebEventBus;
import jp.relo.cluboff.model.MemberPost;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.model.ReloadEvent;
import jp.relo.cluboff.model.SaveLogin;
import jp.relo.cluboff.ui.BaseDialogFragmentToolbarBottombar;
import jp.relo.cluboff.ui.BaseFragmentBottombar;
import jp.relo.cluboff.ui.webview.MyWebViewClient;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;
import jp.relo.cluboff.util.iUpdateIU;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class PostMemberWebViewFragment extends BaseDialogFragmentToolbarBottombar {

    WebView mWebView;
    private int checkWebview;
    boolean isLoadding = false;
    boolean isVisibleToUser;
    String strBrowser = "";

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
                llBrowser.setEnabled(false);

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
                if(mWebView!=null &&  mWebView.getUrl()!=null){
                    Utils.showDialogLIB(getActivity(), R.string.title_browser, R.string.content_browser,
                            R.string.btn_browser, new iUpdateIU() {
                                @Override
                                public void updateError(int txt) {
                                    if(txt == 0){
                                        if(StringUtil.isEmpty(strBrowser)){
                                            strBrowser = mWebView.getUrl();
                                        }
                                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strBrowser)));
                                    }
                                }
                            });
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
                if(isVisibleToUser){
                    showLoading(getActivity());
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(StringUtil.isEmpty(strBrowser)){
                    strBrowser = url;
                }
                isLoadding = false;
                if(isVisible()){
                    hideLoading();
                    imvBackBottomBar.setEnabled(mWebView.canGoBack());
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
    }

    @Subscribe
    public void onEvent(ReloadEvent event) {
        if(event.isReload()&&!isLoadding){
            loadUrl();
        }
    }
}
