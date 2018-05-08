/**
 * Copyright (C) Blogwatcher, inc. All Rights Reserved.
 */
package net.fukuri.memberapp.memberapp.listener;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import framework.phvtUtils.AppLog;
import jp.profilepassport.android.PPNotification;
import jp.profilepassport.android.PPNotificationListener;

public class PPSDKDemoNotificationListener implements PPNotificationListener {

    private static PPSDKDemoNotificationListener sPPSDKDemoNotificationListener;
    private final Context context;

    private PPSDKDemoNotificationListener(final Context context) {
        this.context = context;
    }

    public static PPSDKDemoNotificationListener getInstance(final Context context) {
        if (null == sPPSDKDemoNotificationListener) {
            sPPSDKDemoNotificationListener = new PPSDKDemoNotificationListener(context);
        }
        return sPPSDKDemoNotificationListener;
    }

    @Override
    public void onFailureNotice(final int errorCode) {
        AppLog.log("Push Notification Failed");
        AppLog.log("Push Notification Failed Error Code: " + errorCode);
    }

    @Override
    public void didReceiveExtraDataNotifications(final List<PPNotification> notificationList) {
        if ((null == notificationList) || notificationList.isEmpty()) {
            return;
        }

        for (PPNotification notification : notificationList) {
            String noticeId = notification.getNotificationId();
            String data = notification.getExtraData();
            Toast.makeText(context, "noticeId: + " + noticeId + "\ndata:" + data, Toast.LENGTH_SHORT).show();
        }
    }
}
