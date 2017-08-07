package jp.relo.cluboff.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.text.MessageFormat;

import jp.relo.cluboff.R;
import jp.relo.cluboff.model.MemberPost;
import jp.relo.cluboff.model.SaveLogin;
import jp.relo.cluboff.ui.BaseFragmentBottombar;
import jp.relo.cluboff.ui.webview.MyWebViewClient;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.Utils;
import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;
import jp.relo.cluboff.util.iUpdateIU;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class PostMemberWebViewFragment extends BaseFragmentBottombar {

    WebView mWebView;
    private int checkWebview;
    boolean isLoadding = false;
    boolean isVisibleToUser;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_UP){
                    getActivity().finish();
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
    public void setupBottombar() {
        switch (checkWebview){
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
                if(mWebView!=null &&  mWebView.getUrl()!=null){
                    Utils.showDialogLIB(getActivity(), R.string.title_browser, R.string.content_browser,
                            R.string.btn_browser, new iUpdateIU() {
                                @Override
                                public void updateError(int txt) {
                                    if(txt == 0){
                                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl())));
                                    }
                                }
                            });
                }
            }
        });

        imvReloadBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl( "javascript:window.location.reload( true )" );

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
    public void onStart() {
        super.onStart();
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
                isLoadding = true;
                if(isVisibleToUser){
                    showLoading(getActivity());
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isLoadding = false;
                if(isVisible()){
                    hideLoading();
                    imvBackBottomBar.setEnabled(mWebView.canGoBack());
                    imvForwardBottomBar.setEnabled(mWebView.canGoForward());
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
        loadUrl();
    }

    private void loadUrl(){
        String url="";
        MemberPost memberPost = new MemberPost();
        SaveLogin saveLogin = SaveLogin.getInstance(getActivity());
        if(saveLogin!=null){
            try {
                url = MessageFormat.format(Constant.TEMPLATE_URL_MEMBER, BackAES.decrypt(saveLogin.getUrlEncrypt(), AESHelper.password, AESHelper.type));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String  usernameEn = "";
            String  COA_APPEn = "1";
            try {
                usernameEn = new String(BackAES.encrypt(saveLogin.getKaiinno(), AESHelper.password, AESHelper.type));
                COA_APPEn = new String(BackAES.encrypt("1", AESHelper.password, AESHelper.type));
            } catch (Exception e) {
                e.printStackTrace();
            }
            memberPost.setU(usernameEn);
            memberPost.setCOA_APP(COA_APPEn);
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
}
