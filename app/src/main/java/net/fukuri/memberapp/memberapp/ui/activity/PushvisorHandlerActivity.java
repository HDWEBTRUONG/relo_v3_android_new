package net.fukuri.memberapp.memberapp.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import biz.appvisor.push.android.sdk.AppVisorPush;
import framework.phvtUtils.AppLog;
import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.util.Constant;
import net.fukuri.memberapp.memberapp.util.LoginSharedPreference;

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
        setHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pushProcess();
    }

    public void setHandler(){
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

        /*if (checkOpenedThisScreen) {
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

            *//*if(!isBackgroundRunning(this)){
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
                finish();
            }*//*
        }else{
            //If app's not running
            Intent intent = new Intent(PushvisorHandlerActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    //handle message from PushVisor
    public void pushProcess() {
        this.appVisorPush = AppVisorPush.sharedInstance();

        this.appVisorPush.setAppInfor(getApplicationContext(), Constant.APPVISOR_ID);

        // プッシュ通知の関連設定(GCM_SENDER_ID、アイコン、ステータスバーアイコン、プッシュ通知で起動するクラス名、タイトル)
        this.appVisorPush.startPush(Constant.GCM_SENDER_ID, R.mipmap.ic_launcher, R.mipmap.ic_launcher, PushvisorHandlerActivity.class, getString(R.string.app_name));
        // プッシュ通知の反応率を測定(必須)
        this.appVisorPush.trackPushWithActivity(this);
        // BRANDID  of userPropertyGroup 1 （UserPropertyGroup1〜UserPropertyGroup5）
        try{
            this.appVisorPush.setUserPropertyWithGroup(LoginSharedPreference.getInstance(this).getUserName(),AppVisorPush.UserPropertyGroup1);
            appVisorPush.synchronizeUserProperties();
        }catch (Exception ex){
            Log.e("BSV", ex.toString());
        }

        String mDevice_Token_Pushnotification = this.appVisorPush.getDeviceID();
        AppLog.log("###################################");
        AppLog.log("####### [ Appvisor uuid ]=", mDevice_Token_Pushnotification);
        AppLog.log("###################################");
    }

    public static boolean isBackgroundRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        return true;
                    }
                }
            }
        }


        return false;
    }
}
