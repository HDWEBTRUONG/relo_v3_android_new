/**
 * Copyright (C) Blogwatcher, inc. All Rights Reserved.
 */
package net.fukuri.memberapp.memberapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import net.fukuri.memberapp.memberapp.listener.PPSDKDemoListener;

import jp.profilepassport.android.PPSDKManager;

public class PPSDKDemoPPsdkSettings {

    private static final String TAG = "PPSDKDemoPPsdkSettings";

    /**
     * Describe the application ID provided by the Blogwatcher.
     */
    private static final String PP_APP_ID = Constant.PP_ID;

    public static void setPPsdkSetting(final Activity activity, final boolean isStarted) {

        if (isStarted) {
            // ユーザへパーミッションが必要な旨を表示する例
            boolean permission_location = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            boolean permission_storage = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permission_location || permission_storage){
                Toast toast = Toast.makeText(activity.getApplicationContext(), "このアプリでは、「ストレージ」と「位置情報」のアプリ権限が必要です。", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                setPPsdkSetting(activity.getApplicationContext(), isStarted);
                return;
            }

            Log.d(TAG, "PPSDK Start.");
            PPSDKManager.serviceStartWithAppId(activity, PP_APP_ID, PPSDKDemoListener.getInstance(activity.getApplicationContext()));
        } else {
            Log.d(TAG, "PPSDK Stop.");
            PPSDKManager.serviceStop(activity);
        }
    }

    public static void setPPsdkSetting(final Context context, final boolean isStarted) {
        if (isStarted) {
            Log.d(TAG, "PPSDK Start.");
                        PPSDKManager.serviceStartWithAppId(context, PP_APP_ID, PPSDKDemoListener.getInstance(context));
        } else {
            Log.d(TAG, "PPSDK Stop.");
                        PPSDKManager.serviceStop(context);
        }
    }


}
