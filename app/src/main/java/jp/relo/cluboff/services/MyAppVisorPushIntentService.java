package jp.relo.cluboff.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.HistoryPushDTO;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.ui.activity.MainTabActivity;
import jp.relo.cluboff.ui.activity.PushActivity;
import jp.relo.cluboff.ui.activity.SplashScreenActivity;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;

/**
 * Created by tonkhanh on 7/14/17.
 */

public class MyAppVisorPushIntentService extends GCMBaseIntentService {
    MyDatabaseHelper myDatabaseHelper;
    private Bundle bundle=null;
    public static final int NOTIFICATION_ID = 1;
    public MyAppVisorPushIntentService() {
    }
    @Override
    protected void onMessage(Context context, Intent intent) {
        myDatabaseHelper = new MyDatabaseHelper(getBaseContext());
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
                if(isBackgroundRunning()){
                    AppLog.log("In isBackgroundRunning");
                    EventBus.getDefault().post(new MessageEvent(MyAppVisorPushIntentService.class.getSimpleName()));
                }else{
                    AppLog.log("Out isBackgroundRunning");
                    sendNotification(getBaseContext(),bundle);
                }
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
    public boolean isBackgroundRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
        boolean isActivityFound = false;

        if (services.get(0).processName
                .equalsIgnoreCase(getPackageName())) {
            isActivityFound = true;
        }

        return isActivityFound;
    }
    public void sendNotification(Context ctx, Bundle mBundle) {
        String message = bundle.getString("message");
        String title = bundle.getString("title");
        Intent intent = new Intent(ctx, SplashScreenActivity.class);
        intent.putExtras(mBundle);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, b.build());
    }



}
