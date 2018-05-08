/**
 * Copyright (C) Blogwatcher, inc. All Rights Reserved.
 */
package net.fukuri.memberapp.memberapp.listener;

import android.content.Context;
import android.widget.Toast;

import net.fukuri.memberapp.memberapp.util.PPSDKDemoLog;
import net.fukuri.memberapp.memberapp.util.PPSDKDemoSharedPreferences;

import jp.profilepassport.android.PPSDKManager;
import jp.profilepassport.android.PPSDKManagerListener;

public class PPSDKDemoListener implements PPSDKManagerListener {

    private static PPSDKDemoListener sPPSDKListener;
    private static Context sContext;

    private PPSDKDemoListener() {
    }

    public static PPSDKDemoListener getInstance(final Context context) {
        if (null == sPPSDKListener) {
            sPPSDKListener = new PPSDKDemoListener();
        }
        if (null == sContext) {
            sContext = context;
        }
        return sPPSDKListener;
    }

    @Override
    public void onSuccessServiceStart() {
        PPSDKDemoLog.d("Service Start Success");
        startBeaconDetect();
        startGeoAreaMonitoring();
        startWifiDetect();

        Toast.makeText(sContext, "SDK Service Start Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailureServiceStart(final int errorCode) {
        PPSDKDemoLog.d("Service Start Failed");
        PPSDKDemoLog.d("Service Start Failed Error Code: " + errorCode);

        PPSDKDemoSharedPreferences.setOptInShown(sContext, false);
        Toast.makeText(sContext, "SDK Service Start Failed: " + errorCode, Toast.LENGTH_SHORT).show();
    }

    private void startGeoAreaMonitoring() {

        boolean isChecked = PPSDKDemoSharedPreferences.getGeoAreaDetectChecked(sContext);
        if (isChecked) {
            PPSDKDemoLog.d("GeoArea Monitoring Start");
            PPSDKManager.startGeofenceMonitoring(sContext);
        } else {
            PPSDKDemoLog.d("GeoArea Monitoring doesn't Start");
            PPSDKManager.stopGeofenceMonitoring(sContext);
        }
    }

    private void startBeaconDetect() {
        boolean isChecked = PPSDKDemoSharedPreferences.getBeaconDetectChecked(sContext);
        if (isChecked) {
            PPSDKDemoLog.d("Beacon Detect Start");
            PPSDKManager.startBeaconDetection(sContext);
        } else {
            PPSDKDemoLog.d("Beacon Detect doesn't Start");
            PPSDKManager.stopBeaconDetection(sContext);
        }
    }

    private void startWifiDetect() {
        boolean isChecked = PPSDKDemoSharedPreferences.getWifiDetectChecked(sContext);
        if (isChecked) {
            PPSDKDemoLog.d("WiFi Detect Start");
            PPSDKManager.startWifiDetection(sContext);
        } else {
            PPSDKDemoLog.d("WiFi Detect doesn't Start");
            PPSDKManager.stopWifiDetection(sContext);
        }
    }
}
