package jp.relo.cluboff;

import android.app.Application;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import jp.relo.cluboff.ui.webview.CustomWebViewClient;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;


/**
 * Created by HuyTran on 3/21/17.
 */

public class ReloApp extends Application {

    private WebView wvForgetPass;
    private WebView wvCanNotLogin;
    private WebView wvAreaCoupon;
    private WebView wvMemberCoupon;

    private KeyStore keyStore;

    private int version;


    @Override
    public void onCreate() {
        super.onCreate();
        wvForgetPass=setupWebview(Constant.WEBVIEW_URL_FORGET_LOGIN);
        wvCanNotLogin=setupWebview(Constant.WEBVIEW_URL_CAN_NOT_LOGIN);
        wvAreaCoupon=setupWebview("");//Constant.WEBVIEW_URL_AREA_COUPON);
        wvMemberCoupon=setupWebview(Constant.WEBVIEW_URL_MEMBER_COUPON);
        version = LoginSharedPreference.getInstance(getApplicationContext()).getVersion();

        try {
            keyStore=KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    private WebView setupWebview(String url){
        ////////////// setting webview
        WebView mWebView = new WebView(getApplicationContext());
        mWebView.setWebViewClient(new CustomWebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage(getResources().getString(R.string.error_ssl));
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
            }

        });
        if(!"".equals(url)){
            mWebView.loadUrl(url);
        }
        return mWebView;
    }
    public WebView getWebView(int index){
        switch (index){
            case Constant.FORGET_PASSWORD:
                return wvForgetPass;
            case Constant.CAN_NOT_LOGIN:
                return wvCanNotLogin;
            case Constant.AREA_COUPON:
                return wvAreaCoupon;
            case Constant.MEMBER_COUPON:
                return wvMemberCoupon;
            case Constant.DETAIL_COUPON:
                return setupWebview("");
        }
        return null;
    }
    public KeyStore getKeyStore(){
        return keyStore;
    }
    public int getVersion(){
        return version;
    }
}
