package jp.relo.cluboff.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import biz.appvisor.push.android.sdk.AppVisorPush;
import framework.phvtActivity.BaseActivity;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.Utils;

/**
 * Created by HuyTran on 3/21/17.
 */

public class SplashScreenActivity extends BaseActivity {

    private static final int DELAY_TIME_SPLASH_ACTIVITY = 2000;
    private RelativeLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }



    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        splashLayout = (RelativeLayout) findViewById(R.id.splash_layout);
    }

    @Override
    protected void registerEventHandlers() {
    }

}
