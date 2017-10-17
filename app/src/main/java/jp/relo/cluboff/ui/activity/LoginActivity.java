package jp.relo.cluboff.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.BuildConfig;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.api.MyCallBack;
import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.model.LoginRequest;
import jp.relo.cluboff.ui.BaseActivityToolbar;
import jp.relo.cluboff.util.ConstansSharedPerence;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;

/**
 * Created by quynguyen on 3/22/17.
 */

public class LoginActivity extends BaseActivityToolbar implements View.OnClickListener {

    ImageView img_logo;
    TextView link_webview_not_login;
    TextView link_webview_faq;
    Button btnLogin;
    EditText etUser,etPass;
    TextView txt_show_error;
    Handler mhandler;
    public static final int MSG_ERROR_EMPTY = 0;
    public static final int MSG_ERROR_FAILURE = 1;
    public static final int MSG_ERROR_ELSE = 2;
    public static final int MSG_NOT_NETWORK = 3;
    public static final int MSG_ENABLE_LOGIN = 4;
    public static final int MSG_GOTO_MAIN = 5;
    public static final int MSG_ERROR_MAIL_EMPTY = 6;
    public static final int MSG_ERROR_ID_EMPTY = 7;
    public static final int MSG_ERROR_ID_BRAND_EMPTY = 8;
    public static final int MSG_ERROR_ID_LOGIN_EMPTY = 9;
    public static final int MSG_ERROR_ALL_EMPTY = 10;


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
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_login_wrong_id_password);
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
                    case MSG_ERROR_MAIL_EMPTY:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_mail_empty);
                        break;
                    case MSG_ERROR_ID_EMPTY:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_id_empty);
                        break;
                    case MSG_ERROR_ID_BRAND_EMPTY:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_id_brand_empty);
                        break;
                    case MSG_ERROR_ID_LOGIN_EMPTY:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_id_login_empty);
                        break;
                    case MSG_ERROR_ALL_EMPTY:
                        Utils.showDialogLIB(LoginActivity.this,R.string.error_all_empty);
                        break;
                }
            }
        };
        init();
    }

    private void init() {
        img_logo = (ImageView) findViewById(R.id.img_logo);
        link_webview_not_login = (TextView) findViewById(R.id.link_webview_not_login);
        link_webview_faq = (TextView) findViewById(R.id.link_webview_faq);
        txt_show_error = (TextView) findViewById(R.id.txt_show_error);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);

        btnLogin = (Button) findViewById(R.id.bt_login);
        btnLogin.setOnClickListener(this);
        link_webview_not_login.setOnClickListener(this);
        link_webview_faq.setOnClickListener(this);

        if(BuildConfig.DEBUG){
            img_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoMain();
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
                showLoading(this);
                btnLogin.setEnabled(false);
                addSubscription(apiInterfaceJP.logon(username,pass), new MyCallBack<LoginReponse>() {
                    @Override
                    public void onSuccess(LoginReponse model) {
                        if(model!=null){
                            if(Constant.HTTPOKJP.equals((model.getHeader().getStatus()))){
                                int brandid=0;
                                try {
                                    brandid = Utils.convertInt(Utils.removeString(BackAES.decrypt(model.getInfo().getBrandid(), AESHelper.password, AESHelper.type)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //Save reponse value
                                LoginSharedPreference.getInstance(LoginActivity.this).put(ConstansSharedPerence.TAG_LOGIN_SAVE,model.getInfo());
                                //save value input
                                LoginSharedPreference.getInstance(LoginActivity.this).put(ConstansSharedPerence.TAG_LOGIN_INPUT,
                                        new LoginRequest(username,pass));
                                setGoogleAnalyticLogin(brandid);
                                //save user and password encrypt KeyStore
                                mhandler.sendEmptyMessage(MSG_GOTO_MAIN);
                            }else{
                                mhandler.sendEmptyMessage(MSG_ERROR_ELSE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int msg) {
                        Utils.showDialogAPI(LoginActivity.this,msg);

                    }

                    @Override
                    public void onFinish() {
                        hideLoading();
                        mhandler.sendEmptyMessage(MSG_ENABLE_LOGIN);
                    }
                });

            }else if(StringUtil.isEmpty(username) && StringUtil.isEmpty(pass)){
                mhandler.sendEmptyMessage(MSG_ERROR_ALL_EMPTY);
            }else if(StringUtil.isEmpty(username) && StringUtil.isEmpty(pass)){
                mhandler.sendEmptyMessage(MSG_ERROR_ID_EMPTY);
            }else if(StringUtil.isEmpty(username)){
                mhandler.sendEmptyMessage(MSG_ERROR_ID_LOGIN_EMPTY);
            }else if(StringUtil.isEmpty(pass)){
                mhandler.sendEmptyMessage(MSG_ERROR_ID_BRAND_EMPTY);
            }

        }else{
            mhandler.sendEmptyMessage(MSG_NOT_NETWORK);
        }
    }

    public void setGoogleAnalyticLogin(long brandid){
        ReloApp reloApp = (ReloApp) getApplication();
        reloApp.trackingWithAnalyticGoogleServices(Constant.GA_CATALOGY_LOGIN,Constant.GA_ACTION_LOGIN,Constant.GA_LABLE_LOGIN,brandid);
    }



    /**
     *
     * Show webview with current url
     * @param key a key of url
     * @param url a url address
     * @param keyCheckWebview change title webview
     * <p>Note:
     *       <b>1 - Forget ID/Password</b>
     *       <b>2 - You can not login</b>
     * </p>
     */

    private void goNextWebview(String key, String url, int keyCheckWebview) {
        Bundle bundle = new Bundle();
        bundle.putString(key,url);
        bundle.putInt(Constant.KEY_CHECK_WEBVIEW, keyCheckWebview);
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        link_webview_not_login.setPaintFlags(link_webview_not_login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        link_webview_faq.setPaintFlags(link_webview_not_login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        hideSoftKeyboard();
        LoginRequest loginRequest = LoginSharedPreference.getInstance(this).get(ConstansSharedPerence.TAG_LOGIN_INPUT, LoginRequest.class);
        if(loginRequest!=null){
            etUser.setText(loginRequest.getLOGINID());
            etPass.setText(loginRequest.getPASSWORD());
        }
    }

    @Override
    public void setupToolbar() {
        tvMenuTitle.setText(R.string.string_login);
        tvMenuSubTitle.setText(R.string.title_login);
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
            case R.id.link_webview_not_login:
                openTutorial();
                break;
            case R.id.link_webview_faq:
                clickLinkFAQ();
                break;
        }
    }
    public void openTutorial(){
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
    }


    private void gotoMain(){
        Intent mainActivity = new Intent(this, MainTabActivity.class);
                        startActivity(mainActivity);
                        finish();
    }
    public void clickLinkFAQ(){
        goNextWebview(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_FAQ, Constant.FAQ);
    }

}
