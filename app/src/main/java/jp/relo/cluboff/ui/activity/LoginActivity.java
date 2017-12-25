package jp.relo.cluboff.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.BuildConfig;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.model.LoginRequest;
import jp.relo.cluboff.model.MemberPost;
import jp.relo.cluboff.model.ValueLoginOld;
import jp.relo.cluboff.ui.BaseActivityToolbar;
import jp.relo.cluboff.ui.fragment.WebViewDialogFragment;
import jp.relo.cluboff.ui.webview.MyWebViewClient;
import jp.relo.cluboff.util.ConstansSharedPerence;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static jp.relo.cluboff.ui.fragment.PostAreaWebViewFragment.MULTIPLE_PERMISSIONS;

/**
 * Created by quynguyen on 3/22/17.
 */

public class LoginActivity extends BaseActivityToolbar implements View.OnClickListener {

    ImageView img_logo;
    TextView link_webview_not_login;
    TextView link_webview_faq;
    TextView txt_login_error;
    Button btnLogin;
    EditText etUser,etPass;
    TextView txt_show_error;
    WebView wvLogin;
    Handler mhandler;
    public static final int MSG_ERROR_EMPTY = 0;
    public static final int MSG_ERROR_FAILURE = 1;
    public static final int MSG_ERROR_ELSE = 2;
    public static final int MSG_NOT_NETWORK = 3;
    public static final int MSG_ENABLE_LOGIN = 4;
    public static final int MSG_GOTO_MAIN = 5;
    public static final int MSG_ERROR_ID_EMPTY = 7;
    public static final int MSG_ERROR_PASS_EMPTY = 8;
    public static final int MSG_ERROR_ALL_EMPTY = 10;

    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    String error_text="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ReloApp)getApplication()).trackingAnalytics(Constant.GA_LOGIN_SCREEN);
        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_ERROR_EMPTY:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_blank_id_password);
                        btnLogin.setEnabled(true);
                        break;
                    case MSG_ERROR_FAILURE:
                        Utils.showDialogLIB(LoginActivity.this, R.string.popup_error_api);
                        btnLogin.setEnabled(true);
                        break;
                    case MSG_ERROR_ELSE:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_one_fileld_empty);
                        btnLogin.setEnabled(true);
                        break;
                    case MSG_NOT_NETWORK:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_connect_network);
                        btnLogin.setEnabled(true);
                        break;
                    case MSG_ENABLE_LOGIN:
                        btnLogin.setEnabled(true);
                        break;
                    case MSG_GOTO_MAIN:
                        gotoMain();
                        break;
                    case MSG_ERROR_ID_EMPTY:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_id_empty);
                        break;
                    case MSG_ERROR_PASS_EMPTY:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_pass_empty);
                        break;
                    case MSG_ERROR_ALL_EMPTY:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_all_empty);
                        break;
                }
            }
        };

        LoginSharedPreference  loginSharedPreference = LoginSharedPreference.getInstance(this);
        if(loginSharedPreference!=null && !StringUtil.isEmpty(loginSharedPreference.getUserName()) && !StringUtil.isEmpty(loginSharedPreference.getPass())){
            autoGotoMain();
        }else{
            if(loginSharedPreference!=null && !StringUtil.isEmpty(loginSharedPreference.getUserName())){
                error_text = getString(R.string.string_error_login);
            }
            init();
        }
    }
    private boolean checkPermissions() {
        int findLoca = ContextCompat.checkSelfPermission(this, permissions[0]);
        return findLoca == PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, MULTIPLE_PERMISSIONS);
        }
    }

    private void init() {
        img_logo = (ImageView) findViewById(R.id.img_logo);
        link_webview_not_login = (TextView) findViewById(R.id.link_webview_forget_id);
        link_webview_faq = (TextView) findViewById(R.id.link_webview_can_not_login);
        txt_show_error = (TextView) findViewById(R.id.txt_show_error);
        txt_login_error = (TextView) findViewById(R.id.txt_login_error);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);
        wvLogin = (WebView) findViewById(R.id.wvLogin);
        txt_login_error.setText(error_text);

        btnLogin = (Button) findViewById(R.id.bt_login);
        btnLogin.setOnClickListener(this);
        link_webview_not_login.setOnClickListener(this);
        link_webview_faq.setOnClickListener(this);

        if(BuildConfig.DEBUG){
            img_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //gotoMain();
                }
            });
        }

    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
    }

    @Override
    protected void registerEventHandlers() {

    }

    public void clickLogin(){
        boolean isNetworkAvailable = Utils.isNetworkAvailable(this);
        if(isNetworkAvailable) {
            final String username=etUser.getText().toString().trim();
            final String pass = etPass.getText().toString().trim();
            if (!StringUtil.isEmpty(username)&& !StringUtil.isEmpty(pass)) {
                btnLogin.setEnabled(false);
                loginWebview(username,pass);

            }else if(StringUtil.isEmpty(username) && StringUtil.isEmpty(pass)){
                mhandler.sendEmptyMessage(MSG_ERROR_ALL_EMPTY);
            }else if(StringUtil.isEmpty(username)){
                mhandler.sendEmptyMessage(MSG_ERROR_ID_EMPTY);
            }else if(StringUtil.isEmpty(pass)){
                mhandler.sendEmptyMessage(MSG_ERROR_PASS_EMPTY);
            }

        }else{
            mhandler.sendEmptyMessage(MSG_NOT_NETWORK);
        }
    }

    private void loginWebview( String userID, String pass) {
        WebSettings webSettings = wvLogin.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        wvLogin.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //Disable cache Webview
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        wvLogin.setWebViewClient(new MyWebViewClient(this) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading(LoginActivity.this);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                /*hideLoading();
                if("Now Loading".equalsIgnoreCase(view.getTitle())) return;
                if(Constant.REPONSE_SUCCESS.equalsIgnoreCase(view.getTitle())){
                    mhandler.sendEmptyMessage(MSG_ERROR_ELSE);
                    mhandler.sendEmptyMessage(MSG_ENABLE_LOGIN);
                }else{
                    mhandler.sendEmptyMessage(MSG_GOTO_MAIN);
                }*/
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideLoading();
                mhandler.sendEmptyMessage(MSG_ERROR_ELSE);
                mhandler.sendEmptyMessage(MSG_ENABLE_LOGIN);
            }
        });
        wvLogin.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                hideLoading();
                if("Now Loading".equalsIgnoreCase(view.getTitle())){
                    wvLogin.stopLoading();
                    mhandler.sendEmptyMessage(MSG_GOTO_MAIN);
                }else{
                    mhandler.sendEmptyMessage(MSG_ERROR_ELSE);
                    mhandler.sendEmptyMessage(MSG_ENABLE_LOGIN);
                }
                /*if(Constant.REPONSE_SUCCESS.equalsIgnoreCase(view.getTitle())){
                    mhandler.sendEmptyMessage(MSG_ERROR_ELSE);
                    mhandler.sendEmptyMessage(MSG_ENABLE_LOGIN);
                }else{
                    mhandler.sendEmptyMessage(MSG_GOTO_MAIN);
                }*/
            }
        });

        String url = Constant.TEMPLATE_URL_MEMBER;
        MemberPost memberPost = new MemberPost();
        memberPost.setU(userID);
        memberPost.setCOA_APP(pass);
        wvLogin.postUrl( url, memberPost.toString().getBytes());
    }

    public void setGoogleAnalyticLogin(long brandid){
        ReloApp reloApp = (ReloApp) getApplication();
        reloApp.trackingWithAnalyticGoogleServices(Constant.GA_CATALOGY_LOGIN,Constant.GA_ACTION_LOGIN,Constant.GA_LABLE_LOGIN,brandid);
    }





    private void goNextWebview(String url,String title, String subtitle) {
        WebViewDialogFragment webViewDialogFragment = WebViewDialogFragment.newInstance(url, title, subtitle);
        openDialogFragment(webViewDialogFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if (!checkPermissions()) {
            requestPermission();
        }else{
            new LoadValueLoginTask().execute("titanium");
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadValueLoginTask().execute("titanium");
        link_webview_not_login.setPaintFlags(link_webview_not_login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        link_webview_faq.setPaintFlags(link_webview_not_login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        hideSoftKeyboard();
    }

    @Override
    public void setupToolbar() {
        tvMenuTitle.setText(R.string.string_login);
        tvMenuSubTitle.setText(R.string.title_login);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean location = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (location) {
                        new LoadValueLoginTask().execute("titanium");
                    }else{
                        Toast.makeText(this, R.string.premissionaccepted_no_accepted, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.premission_error, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mhandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                clickLogin();
                break;
            case R.id.link_webview_forget_id:
                canNotLogin();
                break;
            case R.id.link_webview_can_not_login:
                clickLinkFAQ();
                break;
        }
    }
    public void canNotLogin(){
        goNextWebview(Constant.WEBVIEW_FORGET_ID, getString(R.string.string_login),getString(R.string.txt_link_forget_id));
    }


    private void gotoMain(){
        LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(this);
        loginSharedPreference.setUserName(etUser.getText().toString().trim());
        loginSharedPreference.setPass(etPass.getText().toString().trim());
        Intent mainActivity = new Intent(this, MainTabActivity.class);
                        startActivity(mainActivity);
                        finish();
    }
    private void autoGotoMain(){
        Intent mainActivity = new Intent(this, MainTabActivity.class);
        startActivity(mainActivity);
        finish();
    }
    public void clickLinkFAQ(){
        goNextWebview(Constant.WEBVIEW_CAN_NOT_LOGIN, getString(R.string.string_login), getString(R.string.title_can_not_login));
    }

    private class LoadValueLoginTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            return Utils.loadSharedPrefs(LoginActivity.this, params[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            AppLog.log("KQ: "+result);
            String valueID="";
            String valuePass="";

            if(StringUtil.isEmpty(LoginSharedPreference.getInstance(LoginActivity.this).getUserName())){
                ValueLoginOld loginOld = new Gson().fromJson(result, ValueLoginOld.class);
                if(loginOld!=null){
                    valueID = loginOld.getMember_id();
                    valuePass = loginOld.getPassword();
                }
            }else{
                valueID = LoginSharedPreference.getInstance(LoginActivity.this).getUserName();
                valuePass= LoginSharedPreference.getInstance(LoginActivity.this).getPass();
            }
            etUser.setText(valueID);
            etPass.setText(valuePass);
        }
    }

}
