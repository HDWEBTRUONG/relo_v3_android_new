package main.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import framework.phvtActivity.BaseActivity;
import main.R;
import main.util.Constant;

/**
 * Created by quynguyen on 3/23/17.
 */

public class WebviewActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_toolbar)
    TextView mToolbarTilte;

    @BindView(R.id.group_bt_arrow)
    LinearLayout lnGroupArrow;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.imgForward)
    ImageView imgForward;

    @BindView(R.id.web_view)
    WebView mWebView;

    @BindView(R.id.group_close)
    RelativeLayout rlGroupClose;

    @BindView(R.id.group_title)
    LinearLayout lnGroupTitle;

    @BindView(R.id.imgClose)
    ImageView imgClose;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        mToolbar.setBackgroundResource(R.color.colorMineShaft);
        mToolbarTilte.setVisibility(View.VISIBLE);

        int checkWebview = getIntent().getIntExtra(Constant.KEY_CHECK_WEBVIEW, 1);

        // Change Title webview
        if(checkWebview==1){ // 1: Forget ID/Password
            mToolbarTilte.setText(getString(R.string.txt_link_forget_id_password));
        }else if(checkWebview==2){ // 2: You can not login
            mToolbarTilte.setText(getString(R.string.txt_link_can_not_login));
        }else{ // Something else
            mToolbarTilte.setText(getString(R.string.txt_title_login));
        }

        lnGroupTitle.setVisibility(View.VISIBLE);
        lnGroupArrow.setVisibility(View.VISIBLE);
        rlGroupClose.setVisibility(View.VISIBLE);
        String url = getIntent().getStringExtra(Constant.KEY_URL_FORGET_LOGIN);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    @OnClick(R.id.imgBack)
    public void clickBack(){
        onBackPressed();
    }

    @OnClick(R.id.imgForward)
    public void clickForward(){
        mWebView.goForward();
    }

    @OnClick(R.id.imgClose)
    public void clickClose(){
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
