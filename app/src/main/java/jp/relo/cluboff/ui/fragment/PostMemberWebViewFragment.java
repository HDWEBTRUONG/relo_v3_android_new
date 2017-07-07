package jp.relo.cluboff.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.Subscribe;

import java.text.MessageFormat;

import jp.relo.cluboff.R;
import jp.relo.cluboff.model.ControlWebEventBus;
import jp.relo.cluboff.model.MemberPost;
import jp.relo.cluboff.ui.BaseFragmentBottombar;
import jp.relo.cluboff.ui.webview.MyWebViewClient;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.IControlBottom;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class PostMemberWebViewFragment extends BaseFragmentBottombar {

    WebView mWebView;
    private int checkWebview;
    RelativeLayout myContainer;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkWebview = getArguments().getInt(Constant.KEY_CHECK_WEBVIEW, Constant.MEMBER_COUPON);
        mWebView = (WebView) view.findViewById(R.id.wvCoupon);
        //if(((ReloApp)getActivity().getApplication()).getWebView().is)
        //mWebView = ((ReloApp)getActivity().getApplication()).getWebView();
        setupWebView();
        /*myContainer = (RelativeLayout) view.findViewById(R.id.lnContainerWv);
        if(mWebView!=null&&mWebView.getParent()!=null){
            ((RelativeLayout)mWebView.getParent()).removeView(mWebView);
        }
        myContainer.addView(mWebView,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));*/
        String url  = MessageFormat.format(getString(R.string.template_url_member),Constant.ACC_TEST_URL_LOGIN);
        mWebView.postUrl( url, new MemberPost().toString().getBytes());

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
                showLoading(getActivity());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(isVisible()){
                    hideLoading();
                    imvBackBottomBar.setEnabled(mWebView.canGoBack());
                    imvForwardBottomBar.setEnabled(mWebView.canGoForward());
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //TODO hide loading
                if(isVisible()){
                    hideLoading();
                }
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if(mWebView!=null&&mWebView.getParent()!=null){
            ((RelativeLayout)mWebView.getParent()).removeView(mWebView);
        }*/
    }
}
