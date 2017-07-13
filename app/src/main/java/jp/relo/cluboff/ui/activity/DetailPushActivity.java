package jp.relo.cluboff.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import biz.appvisor.push.android.sdk.AppVisorPush;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ui.BaseActivityToolbar;
import jp.relo.cluboff.util.Constant;

public class DetailPushActivity extends BaseActivityToolbar {
    private AppVisorPush appVisorPush;

    //Main AppVisor data through BUNDLE Data
    private Bundle bundle=null;
    private TextView tvPushMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pushProcess();
    }

    @Override
    public void setupToolbar() {
        lnToolbar.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.GONE);
        title_toolbar.setVisibility(View.VISIBLE);
        title_toolbar.setText(R.string.test_push_handel);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_detail_push;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        tvPushMessage = (TextView) findViewById(R.id.tvPushMessage);
    }

    @Override
    protected void registerEventHandlers() {

    }

    //handle message from PushVisor
    public void pushProcess(){
        this.appVisorPush = AppVisorPush.sharedInstance();
        this.appVisorPush.setAppInfor(getApplicationContext(), getString(R.string.appvisor_push_app_id));

        // プッシュ通知の関連設定(GCM_SENDER_ID、アイコン、ステータスバーアイコン、プッシュ通知で起動するクラス名、タイトル)
        this.appVisorPush.startPush(Constant.GCM_SENDER_ID, R.mipmap.ic_launcher, R.mipmap.ic_launcher, DetailPushActivity.class, getString(R.string.app_name));
        // プッシュ通知の反応率を測定(必須)
        this.appVisorPush.trackPushWithActivity(this);
        //Push message & data available data
        if (this.appVisorPush.checkIfStartByAppVisorPush(this)){
            //Configuration PushVisor
            bundle = this.appVisorPush.getBundleFromAppVisorPush(this);
            String message = bundle.getString("message");
            String title = bundle.getString("title");

            //Debug Params message
            String xString = bundle.getString("x");
            String yString = bundle.getString("y");
            String zString = bundle.getString("z");
            String wString = bundle.getString("w");

            //Debug infomation push
            message = message + " X=" + xString + " Y=" + yString + " Z=" + zString + " W="+ wString;

            //Logging
            Log.i("TAG","###################################");
            Log.i("TAG","####### [ Appvisor message ] = "+ message);
            Log.i("TAG","####### [ Appvisor title ] = "+ title);
            Log.i("TAG","###################################");
            tvPushMessage.setText("Title: "+title+"\nMessage: "+ message);
        }
    }
}
