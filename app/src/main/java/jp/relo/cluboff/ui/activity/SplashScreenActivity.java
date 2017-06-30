package jp.relo.cluboff.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.List;

import framework.phvtActivity.BaseActivity;
import framework.phvtUtils.AppLog;
import framework.phvtUtils.NetworkUtil;
import framework.phvtUtils.SharedPreferencesUtil;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.BuildConfig;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.model.UserInfo;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.DialogFactory;
import jp.relo.cluboff.util.Utils;

/**
 * Created by HuyTran on 3/21/17.
 */

public class SplashScreenActivity extends BaseActivity {

    private static final int DELAY_TIME_SPLASH_ACTIVITY = 2000;
    private RelativeLayout splashLayout;
    private UserInfo mLocalUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.createNewKeys(this,((ReloApp)getApplication()).getKeyStore());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainProcess();
            }
        }, DELAY_TIME_SPLASH_ACTIVITY);
    }


    private void mainProcess() {
        NCMB.initialize(this, Constant.NOTIFY_APPLICATION_KEY_NCMB,
                Constant.NOTIFY_CLIENT_KEY_NCMB);
        // Check to get user info or sign up an new account
        String json = SharedPreferencesUtil.getString(this, Constant.PREF_USER_INFO, "");
        mLocalUserInfo = UserInfo.fromJson(json, UserInfo.class);
        boolean isNetWorkAvailable = NetworkUtil.isOnline(this);
        if (!isNetWorkAvailable&&mLocalUserInfo==null) {
            DialogFactory.showMessage(this, getString(R.string.no_internet));
        } else if (mLocalUserInfo == null|| StringUtil.isEmpty(mLocalUserInfo.getObjectId())) {//== mLocalUserInfo == null && isNetWorkAvailable
            // Read the saved gcm registration id from shared preferences.
            registrationId();

        } else if (isNetWorkAvailable) {//== mLocalUserInfo != null && isNetWorkAvailable
            getUserInfo(mLocalUserInfo.getObjectId());

        } else { //== mLocalUserInfo != null && !isNetWorkAvailable
            goNextScreen();
        }
    }

    public void registrationId() {
        if(BuildConfig.DEBUG){
            goNextScreen();
        }
        if(!Utils.isGooglePlayServicesAvailable(this))
        {
            return;
        }

        //installationの作成
        //GCMからRegistrationIdを取得

        final NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();
        AppLog.log("Sender ID",Constant.GCM_SENDER_ID);
        installation.getRegistrationIdInBackground(Constant.GCM_SENDER_ID
                , new DoneCallback() {
                    @Override
                    public void done(NCMBException e) {
                        if (e == null) {
                            //成功
                            try {
                                //mBaaSに端末情報を保存
                                installation.save();
                                getDeviceTokenToSignup();
                            } catch (NCMBException saveError) {
                                //保存失敗
                                AppLog.log("Has NCMBException", saveError.toString());
                                if (NCMBException.DUPLICATE_VALUE.equals(saveError.getCode())) {
                                    //保存失敗 : registrationID重複
                                    updateInstallation(installation);
                                } else {
                                    //保存失敗 : その他
                                    saveError.printStackTrace();
                                    AppLog.log("SplasScreenActivity Error1", saveError.toString());
                                }
                            }
                        } else {
                            //ID取得失敗
                            AppLog.log("SplasScreenActivity Error2", e.toString());
                        }
                    }
                });
    }
    //Update Install when user Re-Intalled app
    public static void updateInstallation(final NCMBInstallation installation) {

        //installationクラスを検索するクエリの作成
        NCMBQuery<NCMBInstallation> query = NCMBInstallation.getQuery();

        //同じRegistration IDをdeviceTokenフィールドに持つ端末情報を検索する
        query.whereEqualTo("deviceToken", installation.getDeviceToken());

        //データストアの検索を実行
        query.findInBackground(new FindCallback<NCMBInstallation>() {
            @Override
            public void done(List<NCMBInstallation> results, NCMBException e) {

                //検索された端末情報のobjectIdを設定
                installation.setObjectId(results.get(0).getObjectId());

                //端末情報を更新する
                installation.saveInBackground();
            }
        });
    }

    // 登録端末のdeviceTokenを取得する
    public void getDeviceTokenToSignup() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void[] params) {
                String deviceToken = null;
                try {
                    NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();
                    deviceToken = installation.getDeviceToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return deviceToken;
            }

            @Override
            protected void onPostExecute(String deviceToken) {
                if (deviceToken != null) {
                    SharedPreferencesUtil.putString(getBaseContext(), Constant.GCM_SENDER_ID, deviceToken);
                    signup(deviceToken);
                }
            }
        }.execute(null, null, null);
    }

    private void signup(final String deviceToken) {
        //RegisterID Push
        AppLog.log("Device Token", deviceToken);
        //Call api register id
        //if success get user infor and registerDeviceToken

        /*mLocalUserInfo = (UserInfo) data;
        registerDeviceToken(deviceToken, mLocalUserInfo.getObjectId());*/

    }

    private void registerDeviceToken(String gcmRegId, String objectId) {
        //Call api register device token RegisterID Push and go to main
        goNextScreen();
    }
    private void getUserInfo(String objectId) {
        //call api user info in server push
        /*RestfulUtil.getUserInfo(this, objectId, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                mLocalUserInfo = (UserInfo) data;
                goNextScreen();
            }

            @Override
            public void onDownloadFailed(Exception e, int requestCode, int responseCode) {

            }
        });*/
    }


    //TODO make redirection after splash screen gone
    private void goNextScreen() {
        saveUserInfoToLocal();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void saveUserInfoToLocal() {
        //TODO: DELETE TEMP
        if(BuildConfig.DEBUG){
            if(mLocalUserInfo==null){
                mLocalUserInfo = new UserInfo();
            }
        }
        ((ReloApp) (getApplication())).setUserInfo(mLocalUserInfo);
        SharedPreferencesUtil.putString(getBaseContext(), Constant.PREF_USER_INFO, mLocalUserInfo.toJson());
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        splashLayout = (RelativeLayout) findViewById(R.id.splash_layout);
    }

    @Override
    protected void registerEventHandlers() {
    }


}
