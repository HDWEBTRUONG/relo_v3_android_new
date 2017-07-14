package jp.relo.cluboff.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gcm.GCMBaseIntentService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.HistoryPushDTO;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.ui.activity.DetailPushActivity;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;

/**
 * Created by tonkhanh on 7/14/17.
 */

public class MyAppVisorPushIntentService extends GCMBaseIntentService {
    MyDatabaseHelper myDatabaseHelper;
    private Bundle bundle=null;
    public MyAppVisorPushIntentService() {
    }
    @Override
    protected void onMessage(Context context, Intent intent) {
        myDatabaseHelper = new MyDatabaseHelper(getBaseContext());
        if(isBackgroundRunning(getBaseContext())){
            bundle = intent.getExtras();
            if(bundle!=null){
                String message = bundle.getString("message");
                String title = bundle.getString("title");
                String url = bundle.getString("url");

                //Debug Params message
                String xString = bundle.getString("x");
                String yString = bundle.getString("y");
                String zString = bundle.getString("z");
                String wString = bundle.getString("w");
                HistoryPushDTO dataPush = new HistoryPushDTO();
                dataPush.setTitlePush(title);
                dataPush.setTimeHis(Utils.dateTimeValue());
                dataPush.setContentHis(message);
                dataPush.setxHis(xString);
                dataPush.setyHis(yString);
                dataPush.setzHis(zString);
                dataPush.setwHis(wString);
                dataPush.setUrlHis(wString);
                myDatabaseHelper.savePush(dataPush);
                int numPush = LoginSharedPreference.getInstance(getApplicationContext()).getPush();
                numPush++;
                LoginSharedPreference.getInstance(getApplicationContext()).setPush(numPush);
            }
            EventBus.getDefault().post(new MessageEvent(MyAppVisorPushIntentService.class.getSimpleName()));
        }

    }

    @Override
    protected void onError(Context context, String s) {

    }

    @Override
    protected void onRegistered(Context context, String s) {

    }

    @Override
    protected void onUnregistered(Context context, String s) {

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
