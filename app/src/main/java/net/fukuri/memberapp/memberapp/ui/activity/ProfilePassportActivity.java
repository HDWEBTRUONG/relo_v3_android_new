package net.fukuri.memberapp.memberapp.ui.activity;

import android.os.Bundle;
import android.view.View;

import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.listener.PPSDKDemoGeoAreaListener;
import net.fukuri.memberapp.memberapp.ui.BaseActivityToolbar;
import net.fukuri.memberapp.memberapp.util.PPSDKDemoPPsdkSettings;
import net.fukuri.memberapp.memberapp.util.PPSDKDemoSharedPreferences;

import framework.phvtUtils.AppLog;
import jp.profilepassport.android.PPGeoAreaResult;
import jp.profilepassport.android.PPSDKManager;

/**
 * Created by tonkhanh on 5/8/18.
 */

public class ProfilePassportActivity extends BaseActivityToolbar {

    @Override
    public void setupToolbar() {
        tvTitleCenter.setText("ProfilePassport");
        tvTitleCenter.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.profilepassport_layout;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        PPSDKDemoPPsdkSettings.setPPsdkSetting(ProfilePassportActivity.this, PPSDKDemoSharedPreferences.getPPSDKChecked(
                getApplicationContext()));
        PPSDKDemoGeoAreaListener.setPPSDKDemoGeoAreaActivity(ProfilePassportActivity.this);
        PPSDKManager.startGeofenceMonitoring(this);
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == PPSDKManager.PERMISSION_STORAGE_REQUEST_CODE){
            PPSDKDemoPPsdkSettings.setPPsdkSetting(getApplicationContext(),
                    PPSDKDemoSharedPreferences.getPPSDKChecked(getApplicationContext()));
        }
    }

    public void setGeoAreaAt(final PPGeoAreaResult geoAreaResult) {
        if (null == geoAreaResult) {
            return;
        }
        AppLog.log("PPGeoAreaResult");
    }

    public void setGeoAreaLeft(final PPGeoAreaResult geoAreaResult) {
        if (null == geoAreaResult) {
            return;
        }
        AppLog.log("setGeoAreaLeft");
    }

}
