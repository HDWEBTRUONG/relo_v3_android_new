package net.fukuri.memberapp.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import framework.phvtUtils.StringUtil;
import net.fukuri.memberapp.R;
import net.fukuri.memberapp.ReloApp;
import net.fukuri.memberapp.ui.BaseDialogFragmentToolbarBottombar;
import net.fukuri.memberapp.ui.webview.MyWebViewClient;
import net.fukuri.memberapp.util.Constant;
import net.fukuri.memberapp.views.MyWebview;

/**
 * Created by tonkhanh on 6/8/17.
 */

public class WebViewDialogFragment extends BaseDialogFragmentToolbarBottombar {
    MyWebview mWebView;
    ProgressBar horizontalProgress;
    String subTitle="";

    public static WebViewDialogFragment newInstance(String url, String title, String subtitle) {

        Bundle args = new Bundle();
        args.putString(Constant.BUNDER_URL,url);
        args.putString(Constant.BUNDER_TITLE,title);
        args.putString(Constant.BUNDER_SUBTITLE,subtitle);
        WebViewDialogFragment fragment = new WebViewDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init(View view) {
        mWebView = (MyWebview) view.findViewById(R.id.wvCoupon);
        horizontalProgress = (ProgressBar) view.findViewById(R.id.horizontalProgress);
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ReloApp)getActivity().getApplication()).trackingAnalytics(Constant.GA_FAQ_SCREEN);
        String title="";
        String url="";
        Bundle bundle = getArguments();
        if(bundle!=null){
            title = bundle.getString(Constant.BUNDER_TITLE);
            subTitle = bundle.getString(Constant.BUNDER_SUBTITLE);
            url = bundle.getString(Constant.BUNDER_URL);
            if(title.equalsIgnoreCase(getString(R.string.menu_FAQ))){
                tvTitleCenter.setVisibility(View.VISIBLE);
                llHeader.setVisibility(View.INVISIBLE);
                viewPaddingHeader.setVisibility(View.GONE);
            }else{
                viewPaddingHeader.setVisibility(View.INVISIBLE);
                tvTitleCenter.setVisibility(View.INVISIBLE);
                llHeader.setVisibility(View.VISIBLE);
                tvMenuTitle.setText(title);
                tvMenuSubTitle.setText(subTitle);
            }

        }
        setupWebView(url);
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
                mWebView.reload();
            }
        });

        imvBackBottomBar.setEnabled(mWebView.canGoBack());
        imvForwardBottomBar.setEnabled(mWebView.canGoForward());
        llBack.setEnabled(mWebView.canGoBack());
        llForward.setEnabled(mWebView.canGoForward());
    }


    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    public void bindView(View view) {

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setupWebView(String url) {
        mWebView.setWebViewClient(new MyWebViewClient(getActivity()) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
                imvBackBottomBar.setEnabled(mWebView.canGoBack());
                imvForwardBottomBar.setEnabled(mWebView.canGoForward());
                llBack.setEnabled(mWebView.canGoBack());
                llForward.setEnabled(mWebView.canGoForward());

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
                if(StringUtil.isEmpty(subTitle)){
                    subTitle = title;
                    tvMenuSubTitle.setText(subTitle);
                }
            }
        });
        mWebView.loadUrl(url);

    }
}
