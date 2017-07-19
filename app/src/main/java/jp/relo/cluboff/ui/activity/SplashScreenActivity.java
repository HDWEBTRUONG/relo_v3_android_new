package jp.relo.cluboff.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import biz.appvisor.push.android.sdk.AppVisorPush;
import framework.phvtActivity.BaseActivity;
import framework.phvtDatabase.DatabaseHelper;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.EASHelper;
import jp.relo.cluboff.util.Utils;

/**
 * Created by HuyTran on 3/21/17.
 */

public class SplashScreenActivity extends BaseActivity {

    private static final int DELAY_TIME_SPLASH_ACTIVITY = 2000;
    public static boolean checkOpenedThisScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkOpenedThisScreen=false;
        Utils.createNewKeys(this,((ReloApp)getApplication()).getKeyStore());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goNextScreen();
            }
        }, DELAY_TIME_SPLASH_ACTIVITY);
    }


    //TODO make redirection after splash screen gone
    private void goNextScreen() {
        checkOpenedThisScreen = true;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
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
}
