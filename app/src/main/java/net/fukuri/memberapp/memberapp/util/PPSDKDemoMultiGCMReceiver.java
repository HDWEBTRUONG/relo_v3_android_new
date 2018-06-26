/**
 * Copyright (C) Blogwatcher, inc. All Rights Reserved.
 */
package net.fukuri.memberapp.memberapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.fukuri.memberapp.memberapp.ui.activity.MainTabActivity;
import net.fukuri.memberapp.memberapp.ui.activity.ProfilePassportActivity;

import framework.phvtUtils.AppLog;

public class PPSDKDemoMultiGCMReceiver extends BroadcastReceiver {

    private static ProfilePassportActivity sPPSDKDemoMultiGCMActivity;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
            handelMultiGCMReceive(context, intent);
        } else if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
            handleMultiGCMRegistration(context, intent);
        }
    }

    private void handelMultiGCMReceive(final Context context, final Intent intent) {
        if (null == sPPSDKDemoMultiGCMActivity) {
            return;
        }
        String id = intent.getStringExtra("id");
        String message = intent.getStringExtra("message");
        Toast.makeText(context.getApplicationContext(), "Receive ID:" + id + ", Message:" + message, Toast.LENGTH_LONG)
                .show();
        AppLog.log("Receive ID:" + id + ", Message:" + message);

        String fromSenderId = intent.getStringExtra("from");
        AppLog.log("From SenderID:" + fromSenderId);
    }

    private void handleMultiGCMRegistration(final Context context, final Intent intent) {
        if (null == sPPSDKDemoMultiGCMActivity) {
            return;
        }

        final String regId = intent.getStringExtra("registration_id");
        String unregistered = intent.getStringExtra("unregistered");
        String errorMess = intent.getStringExtra("error");

        if (null != regId) {
            AppLog.log("Receive RegistrationID:" + regId);
        } else if (null != unregistered) {
            AppLog.log("Receive Unregister ID:" + unregistered);
        } else if (null != errorMess) {
            AppLog.log("Receive Error Message:" + errorMess);
        }
    }

    public static void setPPSDKDemoMultiGCMActivity(final ProfilePassportActivity multiGCMActivity) {
        if (null == sPPSDKDemoMultiGCMActivity) {
            sPPSDKDemoMultiGCMActivity = multiGCMActivity;
        }
    }
}
