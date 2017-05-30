package main.ui.fragment;

import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import main.ReloApp;
import main.ui.BaseFragmentToolbar;
import main.ui.webview.CustomWebViewClient;
import main.util.Constant;
import main.util.Utils;

/**
 * Created by tonkhanh on 5/18/17.
 */

public class WebViewFragment extends BaseFragmentToolbar {

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
        myContainer = (RelativeLayout) view.findViewById(R.id.lnContainerWv);
        if(mWebView!=null&&mWebView.getParent()!=null){
            ((RelativeLayout)mWebView.getParent()).removeView(mWebView);
        }
        if(checkWebview==Constant.DETAIL_COUPON){
            mWebView.loadUrl(url);
        }
        myContainer.addView(mWebView,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {

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

    @Override
    public void onResume() {
        super.onResume();
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
        }else if(checkWebview== Constant.DETAIL_COUPON){ // 5.coupon detail
            mToolbarTilte.setText("Detail coupon");
            imgClose.setVisibility(View.GONE);
        }

        lnGroupTitle.setVisibility(View.VISIBLE);
        lnGroupArrow.setVisibility(View.VISIBLE);
        rlGroupClose.setVisibility(View.VISIBLE);
    }



    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mWebView!=null&&mWebView.getParent()!=null){
            ((RelativeLayout)mWebView.getParent()).removeView(mWebView);
        }
    }

}
