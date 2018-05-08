/**
 * Copyright (C) Blogwatcher, inc. All Rights Reserved.
 */
package net.fukuri.memberapp.memberapp.listener;

import android.content.Context;

import net.fukuri.memberapp.memberapp.ui.activity.ProfilePassportActivity;

import jp.profilepassport.android.PPGeoAreaListener;
import jp.profilepassport.android.PPGeoAreaResult;
import jp.profilepassport.android.PPGeoAreaTag;
import jp.profilepassport.android.PPSDKManager;

public class PPSDKDemoGeoAreaListener extends PPGeoAreaListener {

    private static ProfilePassportActivity sPPSDKDemoGeoAreaActivity;

    public static void setPPSDKDemoGeoAreaActivity(final ProfilePassportActivity demoGeoAreaActivity) {
        if (null == sPPSDKDemoGeoAreaActivity) {
            sPPSDKDemoGeoAreaActivity = demoGeoAreaActivity;
        }
    }

    @Override
    public void onGeoAreaAt(final Context context, final PPGeoAreaResult geoAreaResult) {

        if ((null == geoAreaResult) || (null == geoAreaResult.getPpGeoArea()) ||
                (null == geoAreaResult.getPpGeoArea().getTags()) ||
                (0 == geoAreaResult.getPpGeoArea().getTags().size())) {
            return;
        }
        for (PPGeoAreaTag geoAreaTag : geoAreaResult.getPpGeoArea().getTags()) {

            viewGeoAreaPushNotification(context, geoAreaTag);
        }
        if (null != sPPSDKDemoGeoAreaActivity) {
            sPPSDKDemoGeoAreaActivity.setGeoAreaAt(geoAreaResult);
        }
    }

    @Override
    public void onGeoAreaLeft(final Context context, final PPGeoAreaResult geoAreaResult) {

        if ((null == geoAreaResult) || (null == geoAreaResult.getPpGeoArea()) ||
                (null == geoAreaResult.getPpGeoArea().getTags()) ||
                (0 == geoAreaResult.getPpGeoArea().getTags().size())) {
            return;
        }
        if (null != sPPSDKDemoGeoAreaActivity) {
            sPPSDKDemoGeoAreaActivity.setGeoAreaLeft(geoAreaResult);
        }
    }

    private void viewGeoAreaPushNotification(final Context context, final PPGeoAreaTag geoAreaTag) {

        if ((null == geoAreaTag) || (null == context)) {
            return;
        }
        PPSDKManager.sendTagNotification(context, geoAreaTag, PPSDKDemoNotificationListener.getInstance(context));
    }
}
