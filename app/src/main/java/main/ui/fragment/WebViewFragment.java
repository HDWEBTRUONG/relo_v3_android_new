package main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import framework.phvtFragment.BaseFragment;
import main.R;
import main.ReloApp;
import main.ui.BaseFragmentBottombar;
import main.ui.BaseFragmentToolbarBottombar;
import main.util.Constant;

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
    public void setupToolbar() {
        switch (checkWebview){
            case Constant.FORGET_PASSWORD:
                lnToolbar.setVisibility(View.VISIBLE);
                title_toolbar.setVisibility(View.VISIBLE);
                title_toolbar.setText(R.string.forget_title);
                imvMenu.setVisibility(View.VISIBLE);
                imvMenu.setImageResource(R.drawable.ic_close_48dp);
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
                imvMenu.setImageResource(R.drawable.ic_close_48dp);
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
        lnBottom.setVisibility(View.VISIBLE);
        imvBackBottomBar.setVisibility(View.VISIBLE);
        imvForwardBottomBar.setVisibility(View.VISIBLE);
        imvCopyBottomBar.setVisibility(View.VISIBLE);
        imvReloadBottomBar.setVisibility(View.VISIBLE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mWebView!=null&&mWebView.getParent()!=null){
            ((RelativeLayout)mWebView.getParent()).removeView(mWebView);
        }
    }

}
