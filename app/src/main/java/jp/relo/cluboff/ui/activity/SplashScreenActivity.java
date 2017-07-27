package jp.relo.cluboff.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.List;

import framework.phvtActivity.BaseActivity;
import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.api.MyCallBack;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.model.LoginRequest;
import jp.relo.cluboff.model.VersionReponse;
import jp.relo.cluboff.util.ConstansSharedPerence;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by HuyTran on 3/21/17.
 */

public class SplashScreenActivity extends BaseActivity {

    private static final int DELAY_TIME_SPLASH_ACTIVITY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushvisorHandlerActivity.checkOpenedThisScreen=false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoLogin();
            }
        }, DELAY_TIME_SPLASH_ACTIVITY);
    }


    private void goNextScreen() {
        PushvisorHandlerActivity.checkOpenedThisScreen = true;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void goMainScreen() {
        PushvisorHandlerActivity.checkOpenedThisScreen = true;
        startActivity(new Intent(this, MainTabActivity.class));
        finish();
    }
    private void autoLogin(){
        LoginRequest loginRequest = LoginSharedPreference.getInstance(this).get(ConstansSharedPerence.TAG_LOGIN_INPUT,LoginRequest.class);
        if(loginRequest==null){
            goNextScreen();
        }else{
            String kaiinno = loginRequest.getKaiinno();
            String emailad = loginRequest.getEmailad();
            String brandid = loginRequest.getBrandid();
            boolean isNetworkAvailable = Utils.isNetworkAvailable(this);
            if(StringUtil.isEmpty(kaiinno)||StringUtil.isEmpty(emailad)||StringUtil.isEmpty(brandid)||!isNetworkAvailable){
                goNextScreen();
            }else{
                String usernameEN = "";
                String userMailEN = "";
                String passwordEN = "";
                try {
                    usernameEN = new String(BackAES.encrypt(kaiinno,AESHelper.password, AESHelper.type));
                    userMailEN = new String(BackAES.encrypt(emailad,AESHelper.password, AESHelper.type));
                    passwordEN = new String(BackAES.encrypt(brandid,AESHelper.password, AESHelper.type));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addSubscription(apiInterfaceJP.logon(usernameEN,userMailEN,passwordEN), new MyCallBack<LoginReponse>() {
                    @Override
                    public void onSuccess(LoginReponse model) {
                        if(model!=null){
                            if(Constant.HTTPOKJP.equals((model.getHeader().getStatus()))){
                                int brandid=0;
                                try {
                                    brandid = Utils.convertInt(BackAES.decrypt(model.getInfo().getBrandid(), AESHelper.password, AESHelper.type));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                updateData();
                                setGoogleAnalytic(brandid);

                            }else{
                                goNextScreen();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        goNextScreen();
                    }

                    @Override
                    public void onFinish() {
                    }
                });
            }
        }


    }



    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
    }

    @Override
    protected void registerEventHandlers() {
    }
    public void setGoogleAnalytic(int brandid){
        ReloApp reloApp = (ReloApp) getApplication();
        reloApp.trackingWithAnalyticGoogleServices(Constant.GA_CATALOGY,Constant.GA_ACTION,Constant.GA_DIMENSION_VALUE,brandid);
    }

    private void updateData(){
        addSubscription(apiInterface.checkVersion(),new MyCallBack<VersionReponse>() {
            @Override
            public void onSuccess(VersionReponse model) {
                if(Utils.convertIntVersion(model.getVersion())>LoginSharedPreference.getInstance(getApplicationContext()).getVersion()){
                    goNextScreen();
                }else{
                    goMainScreen();
                }
            }

            @Override
            public void onFailure(String msg) {
                AppLog.log(msg);
                goMainScreen();
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
