package jp.relo.cluboff.ui.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import biz.appvisor.push.android.sdk.AppVisorPush;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.HistoryPushDTO;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.ui.fragment.HistoryPushDialogFragment;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.Utils;

public class DetailPushActivity extends Activity {
    private AppVisorPush appVisorPush;

    //Main AppVisor data through BUNDLE Data
    private Bundle bundle=null;
    MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDatabaseHelper=new MyDatabaseHelper(this);
        boolean isActivityFound = false;
        // check App running or close or foreground & Background
        if(SplashScreenActivity.checkOpenedThisScreen == true){
            isActivityFound = true;
        }else{
            isActivityFound = false;
        }

        pushProcess(isActivityFound);
    }

    private void handlePush(boolean isActivityFound) {
        //if App running
        if (isActivityFound) {
            AppLog.log("------>  isActivityFound .......... <------- ");
            finish();
            EventBus.getDefault().post(new MessageEvent(DetailPushActivity.class.getSimpleName()));
        }else {
            //If app's not running
            AppLog.log("------>  FoodCoach closed .......... <------- ");
            Intent intent = new Intent(DetailPushActivity.this, SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    //handle message from PushVisor
    public void pushProcess(boolean isActivityFound){
        this.appVisorPush = AppVisorPush.sharedInstance();
        this.appVisorPush.setAppInfor(getApplicationContext(), getString(R.string.appvisor_push_app_id));

        // プッシュ通知の関連設定(GCM_SENDER_ID、アイコン、ステータスバーアイコン、プッシュ通知で起動するクラス名、タイトル)
        this.appVisorPush.startPush(Constant.GCM_SENDER_ID, R.mipmap.ic_launcher, R.mipmap.ic_launcher, DetailPushActivity.class, getString(R.string.app_name));
        // プッシュ通知の反応率を測定(必須)
        this.appVisorPush.trackPushWithActivity(this);
        this.appVisorPush.changePushRecieveStatus(false);
        //Push message & data available data
        if (this.appVisorPush.checkIfStartByAppVisorPush(this)){
            //Configuration PushVisor
            bundle = this.appVisorPush.getBundleFromAppVisorPush(this);
            String message = bundle.getString("message");
            String title = bundle.getString("title");
            String url = bundle.getString("url");

            //Debug Params message
            String xString = bundle.getString("x");
            String yString = bundle.getString("y");
            String zString = bundle.getString("z");
            String wString = bundle.getString("w");

            /*HistoryPushDTO dataPush = new HistoryPushDTO();
            dataPush.setTitlePush(title);
            dataPush.setTimeHis(Utils.dateTimeValue());
            dataPush.setContentHis(message);
            dataPush.setxHis(xString);
            dataPush.setyHis(yString);
            dataPush.setzHis(zString);
            dataPush.setwHis(wString);
            dataPush.setUrlHis(wString);
            myDatabaseHelper.savePush(dataPush);*/
        }
        handlePush(isActivityFound);
    }
}
