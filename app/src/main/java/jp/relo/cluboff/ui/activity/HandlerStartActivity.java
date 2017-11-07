package jp.relo.cluboff.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import framework.phvtActivity.BaseActivity;
import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.api.ApiClient;
import jp.relo.cluboff.api.ApiInterface;
import jp.relo.cluboff.api.MyCallBack;
import jp.relo.cluboff.database.MyDatabaseHelper;
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
    MyDatabaseHelper sqLiteOpenHelper;
    Handler handler;
    public static final int GOTOSCREEN =1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final boolean notFirst = LoginSharedPreference.getInstance(this).get(Constant.TAG_IS_FIRST, Boolean.class);
        sqLiteOpenHelper = new MyDatabaseHelper(this);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==GOTOSCREEN){
                    if(notFirst){
                        if(LoginSharedPreference.getInstance(HandlerStartActivity.this).get(ConstansSharedPerence.TAG_LOGIN_SAVE, Info.class) !=null){
                            goMainScreen();
                        }else{
                            goNextScreen();
                        }
                        //autoLogin();
                    }else{
                        goSplashScreen();
                    }
                }
                return false;
            }
        });
        checkUpdateData();
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

    private void checkUpdateData(){
        addSubscription(apiInterface.checkVersion(),new MyCallBack<VersionReponse>(){
            @Override
            public void onSuccess(VersionReponse model) {
                boolean isUpdate = Utils.convertIntVersion(model.getVersion())> LoginSharedPreference.getInstance(getApplicationContext()).getVersion();
                if(isUpdate){
                    ReloApp.setVersionApp(Utils.convertIntVersion(model.getVersion()));
                    ReloApp.setIsUpdateData(isUpdate);
                    sqLiteOpenHelper.clearData();
                }
            }

            @Override
            public void onFailure(int msg) {
                AppLog.log("Error: "+msg);
            }

            @Override
            public void onFinish() {
                handler.sendEmptyMessage(GOTOSCREEN);
            }
        }
        );
    }
}
