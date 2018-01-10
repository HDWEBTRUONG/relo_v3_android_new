package jp.relo.cluboff.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.adapter.HistoryPushAdapter;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.HistoryPushDTO;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.ui.BaseDialogFragmentToolbar;
import jp.relo.cluboff.ui.BaseDialogFragmentToolbarBottombar;
import jp.relo.cluboff.ui.webview.MyWebViewClient;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.views.MyWebview;

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
            tvMenuTitle.setText(title);
            tvMenuSubTitle.setText(subTitle);
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
