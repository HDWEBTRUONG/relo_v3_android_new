package jp.relo.cluboff.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import framework.phvtActivity.BaseActivity;
import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.api.MyCallBack;
import jp.relo.cluboff.model.Info;
import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.model.LoginRequest;
import jp.relo.cluboff.model.VersionReponse;
import jp.relo.cluboff.util.ConstansSharedPerence;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;

/**
 * Created by tonkhanh on 8/1/17.
 */

public class HandlerStartActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean notFirst = LoginSharedPreference.getInstance(this).get(Constant.TAG_IS_FIRST, Boolean.class);
        if(notFirst){
            if(LoginSharedPreference.getInstance(this).get(ConstansSharedPerence.TAG_LOGIN_SAVE, Info.class) !=null){
                goMainScreen();
            }else{
                goNextScreen();
            }
            //autoLogin();
        }else{
            goSplashScreen();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    private void goNextScreen() {
        PushvisorHandlerActivity.checkOpenedThisScreen = true;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    private void goSplashScreen() {
        PushvisorHandlerActivity.checkOpenedThisScreen = true;
        startActivity(new Intent(this, SplashScreenActivity.class));
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
                    usernameEN = new String(BackAES.encrypt(kaiinno, AESHelper.password, AESHelper.type));
                    userMailEN = new String(BackAES.encrypt(emailad,AESHelper.password, AESHelper.type));
                    passwordEN = new String(BackAES.encrypt(brandid,AESHelper.password, AESHelper.type));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showLoading(this);
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
                    public void onFailure(int msg) {
                        AppLog.log(""+msg);
                        goNextScreen();
                    }

                    @Override
                    public void onFinish() {
                        hideLoading();
                    }
                });
            }
        }


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
            public void onFailure(int msg) {
                AppLog.log(""+msg);
                goMainScreen();
            }

            @Override
            public void onFinish() {

            }
        });
    }
    public void setGoogleAnalytic(int brandid){
        ReloApp reloApp = (ReloApp) getApplication();
        reloApp.trackingWithAnalyticGoogleServices(Constant.GA_CATALOGY,Constant.GA_ACTION,Constant.GA_DIMENSION_VALUE,brandid);
    }
}
