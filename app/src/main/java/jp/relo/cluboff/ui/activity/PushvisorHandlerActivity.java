package jp.relo.cluboff.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import biz.appvisor.push.android.sdk.AppVisorPush;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.util.Constant;

/**
 * Created by tonkhanh on 7/21/17.
 */

public class PushvisorHandlerActivity extends Activity {

    //main AppVisor processor
    private AppVisorPush appVisorPush;

    //Main AppVisor customer data through BUNDLE Data
    private Bundle bundle=null;
    public static boolean checkOpenedThisScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pushProcess();
        setHandler();
    }

    public void setHandler(){
        if (checkOpenedThisScreen) {
            AppLog.log("------> FoodCoach opening .......... <------ ");
            Intent intent = new Intent(PushvisorHandlerActivity.this, MainTabActivity.class);
            if(bundle != null) {
                String screenTarget = bundle.getString("w");
                Bundle mBundle = new Bundle();
                mBundle.putString(Constant.TARGET_PUSH, screenTarget);
                intent.putExtras(mBundle);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            //If app's not running
            AppLog.log("------>  FoodCoach closed .......... <------- ");
            Intent intent = new Intent(PushvisorHandlerActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //handle message from PushVisor
    public void pushProcess() {
        this.appVisorPush = AppVisorPush.sharedInstance();
        this.appVisorPush.setAppInfor(getApplicationContext(), Constant.APPVISOR_ID);

        // プッシュ通知の関連設定(GCM_SENDER_ID、アイコン、ステータスバーアイコン、プッシュ通知で起動するクラス名、タイトル)
        this.appVisorPush.startPush(Constant.GCM_SENDER_ID, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                PushvisorHandlerActivity.class, getString(R.string.app_name));
        // プッシュ通知の反応率を測定(必須)
        this.appVisorPush.trackPushWithActivity(this);

        String mDevice_Token_Pushnotification = this.appVisorPush.getDeviceID();
        AppLog.log("###################################");
        AppLog.log("####### [ Appvisor uuid ] = ", mDevice_Token_Pushnotification);
        AppLog.log("###################################");

        //Push message & data available data
        if (this.appVisorPush.checkIfStartByAppVisorPush(this)) {
            //Configuration PushVisor
            bundle = this.appVisorPush.getBundleFromAppVisorPush(this);

        }
    }
}
