package net.fukuri.memberapp.memberapp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.fukuri.memberapp.memberapp.R;

public class DeepLinkActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webView;
    private ImageView ivMenuRight,imvReloadBottomBar;
    private TextView tvMenuSubTitle;
    private String urlnew;
    private ProgressBar horizontalProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent data = getIntent();
        if (data.getData().getEncodedSchemeSpecificPart().equals("//")) {
            startActivityMain();
        }

        setContentView(R.layout.activity_deeplink);

        init();

        String url = data.getData().getSchemeSpecificPart();


        if (url.contains("https") || url.equals("http")) {
            if (url.startsWith("//")) {
                urlnew = url.substring(2);
                webView.loadUrl(urlnew);
            } else {
                webView.loadUrl(url);
            }
        } else {
            urlnew = "https:" + url;
            webView.loadUrl(urlnew);
        }





        webView.setWebChromeClient(new WebChromeClient() {
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

    }

    private void init(){

        ivMenuRight = (ImageView) findViewById(R.id.ivMenuRight);
        webView = (WebView) findViewById(R.id.webview);
        tvMenuSubTitle = (TextView) findViewById(R.id.tvMenuSubTitle);
        imvReloadBottomBar= (ImageView) findViewById(R.id.imvReloadBottomBar);

        horizontalProgress = (ProgressBar) findViewById(R.id.horizontalProgress);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(viewClient);





        ivMenuRight.setOnClickListener(this);
        imvReloadBottomBar.setOnClickListener(this);
    }

    private void startActivityMain(){
        Intent intent = new Intent(this, MainTabActivity.class);
        startActivity(intent);
        finish();
    }

    private WebViewClient viewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            tvMenuSubTitle.setText(view.getTitle());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ivMenuRight) {
             startActivityMain();
        } else {
            webView.loadUrl("javascript:window.location.reload( true )");
        }


    }
}
