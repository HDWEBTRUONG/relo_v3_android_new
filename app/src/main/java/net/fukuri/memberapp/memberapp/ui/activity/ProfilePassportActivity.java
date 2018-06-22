package net.fukuri.memberapp.memberapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.listener.PPSDKDemoGeoAreaListener;
import net.fukuri.memberapp.memberapp.ui.BaseActivityToolbar;
import net.fukuri.memberapp.memberapp.util.MyJavascriptInterface;
import net.fukuri.memberapp.memberapp.util.PPSDKDemoLog;
import net.fukuri.memberapp.memberapp.util.PPSDKDemoPPsdkSettings;
import net.fukuri.memberapp.memberapp.util.PPSDKDemoSharedPreferences;

import framework.phvtUtils.AppLog;
import jp.profilepassport.android.PPGeoAreaResult;
import jp.profilepassport.android.PPSDKManager;
import jp.profilepassport.android.PPSDKManagerListener;
import jp.profilepassport.android.PPSettingsManager;

/**
 * Created by tonkhanh on 5/8/18.
 */

public class ProfilePassportActivity extends BaseActivityToolbar implements PPSDKManagerListener {
    public static ProfilePassportActivity profilePassportActivity;
    WebView wvPassPort;

    public static ProfilePassportActivity getInstall(){
        if(profilePassportActivity==null){
            profilePassportActivity = new ProfilePassportActivity();
        }
        return profilePassportActivity;
    }

    @Override
    public void setupToolbar() {
        tvTitleCenter.setText("ProfilePassport");
        tvTitleCenter.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wvPassPort = (WebView) findViewById(R.id.wvPassPort);
        wvPassPort.loadUrl("file:///android_asset/index.html");
        wvPassPort.addJavascriptInterface(new MyJavascriptInterface(this, wvPassPort), "MyHandler");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public void callAllowedSDKPassport(){
        PPSDKDemoPPsdkSettings.setPPsdkSetting(ProfilePassportActivity.this, PPSDKDemoSharedPreferences.getPPSDKChecked(
                getApplicationContext()), this);
    }

    public void callDeniedSDKPassport(){

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
        /*PPSDKDemoPPsdkSettings.setPPsdkSetting(ProfilePassportActivity.this, PPSDKDemoSharedPreferences.getPPSDKChecked(
                getApplicationContext()), this);*/
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == PPSDKManager.PERMISSION_STORAGE_REQUEST_CODE){
            PPSDKDemoPPsdkSettings.setPPsdkSetting(getApplicationContext(),
                    PPSDKDemoSharedPreferences.getPPSDKChecked(getApplicationContext()), this);
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

    @Override
    public void onSuccessServiceStart() {
        PPSDKDemoLog.d("Service Start Success 2");
        setGeoAreaDetect();
        startBeaconDetect();
        PPSettingsManager.setNotificationLargeIcon(getApplicationContext(), R.mipmap.ic_launcher);
        PPSettingsManager.setNotificationSmallIcon(getApplicationContext(), R.mipmap.ic_launcher);
        //Toast.makeText(this, "SDK Service Start Success", Toast.LENGTH_SHORT).show();
        gotoMain();
    }

    @Override
    public void onFailureServiceStart(int errorCode) {
        PPSDKDemoLog.d("Service Start Failed Error Code: " + errorCode);

        PPSDKDemoSharedPreferences.setOptInShown(this, false);
        //Toast.makeText(this, "SDK Service Start Failed: " + errorCode, Toast.LENGTH_SHORT).show();
        gotoMain();
    }

    public void gotoMain(){
        Intent mainActivity = new Intent(this, MainTabActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void startGeoAreaMonitoring() {

        boolean isChecked = PPSDKDemoSharedPreferences.getGeoAreaDetectChecked(this);
        if (isChecked) {
            PPSDKDemoLog.d("GeoArea Monitoring Start");
            PPSDKDemoGeoAreaListener.setPPSDKDemoGeoAreaActivity(this);
            PPSDKManager.startGeofenceMonitoring(this);
        } else {
            PPSDKDemoLog.d("GeoArea Monitoring doesn't Start");
            PPSDKManager.stopGeofenceMonitoring(this);
        }
    }

    private void setGeoAreaDetect() {
        boolean isChecked = PPSDKDemoSharedPreferences.getGeoAreaDetectChecked(this);
        if (isChecked) {
            PPSDKDemoGeoAreaListener.setPPSDKDemoGeoAreaActivity(this);
            boolean permission_location = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(permission_location){
                // ユーザへパーミッションが必要な旨を表示する例
                Toast toast = Toast.makeText(getApplicationContext(), "ジオエリア検知には「位置情報」のアプリ権限が必要です。", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }else{
                PPSDKDemoLog.d("GeoArea Detect Service Start");
                PPSDKManager.startGeofenceMonitoring(this);
            }
        }else{
            PPSDKDemoLog.d("GeoArea Monitoring doesn't Start");
            PPSDKManager.stopGeofenceMonitoring(this);
        }
    }
    private void startBeaconDetect() {
        boolean isChecked = PPSDKDemoSharedPreferences.getBeaconDetectChecked(this);
        if (isChecked) {
            PPSDKDemoLog.d("Beacon Detect Start");
            PPSDKManager.startBeaconDetection(this);
        } else {
            PPSDKDemoLog.d("Beacon Detect doesn't Start");
            PPSDKManager.stopBeaconDetection(this);
        }
    }
}
