package net.fukuri.memberapp.services;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

/**
 * Created by tonkhanh on 7/14/17.
 */

public class MyAppVisorPushBroadcastReceiver  extends GCMBroadcastReceiver {
    public MyAppVisorPushBroadcastReceiver() {
    }
    protected String getGCMIntentServiceClassName(Context context) {
        String className = "jp.relo.cluboff.services.MyAppVisorPushIntentService";
        return className;
    }
}
