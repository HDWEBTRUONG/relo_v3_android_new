package net.fukuri.memberapp.ui.webview;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import java.util.List;

import framework.phvtUtils.AppLog;

/**
 * Created by tonkhanh on 6/22/17.
 */

public class Common {
    static AlertDialog dialog;
    public static void onSslError(Activity activity, WebView view, final SslErrorHandler handler, SslError error) {
        if (activity == null) {
            return;
        }
        if(isBackgroundRunning(activity)){
            try{
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("ssl証明書が正しくないページですが開いてもいいですか");
                builder.setCancelable(false);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                            handler.cancel();
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                dialog = builder.create();
                if(!dialog.isShowing()){
                    dialog.show();
                }

            }catch (WindowManager.BadTokenException ex){
                AppLog.log(ex.toString());
            }
        }

    }
    public static boolean isBackgroundRunning(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
        boolean isActivityFound = false;

        if (services.get(0).processName
                .equalsIgnoreCase(activity.getPackageName())) {
            isActivityFound = true;
        }
        return isActivityFound;
    }
}