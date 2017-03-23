package bsvandroid.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import bsvandroid.R;
import bsvandroid.framework.phvtActivity.BaseActivity;
import bsvandroid.framework.phvtUtils.NetworkUtil;
import bsvandroid.main.util.DialogFactory;

/**
 * Created by HuyTran on 3/21/17.
 */

public class SplashScreenActivity extends BaseActivity {

    private static final int DELAY_TIME_SPLASH_ACTIVITY = 2000;
    private Animation fadeIn, fadeOut;
    private RelativeLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //== delay this screen a particular time
//        imgApdll.setAnimation(fadeIn);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainProcess();
            }
        }, DELAY_TIME_SPLASH_ACTIVITY);
    }

    private void mainProcess() {
        boolean isNetWorkAvailable = NetworkUtil.isOnline(this);
        if (!isNetWorkAvailable) {
            DialogFactory.showMessage(this, getString(R.string.no_internet));
        } else {
            goNextScreen();
        }
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
//        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_splash);
//        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_splash);
        splashLayout = (RelativeLayout) findViewById(R.id.splash_layout);
    }

    @Override
    protected void registerEventHandlers() {
/*
        Listener Animation of imgApdll
        Invisible Apdll image for display Splash Image
        */
        /*fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                imgApdll.setAnimation(fadeOut);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                imgApdll.setVisibility(View.GONE);
//                splashLayout.setBackgroundResource(R.drawable.bg_splash_screen);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
    }


}
