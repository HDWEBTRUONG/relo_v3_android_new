package jp.relo.cluboff.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import framework.phvtActivity.BaseActivity;
import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.api.ApiClient;
import jp.relo.cluboff.api.ApiClientJP;
import jp.relo.cluboff.api.ApiInterface;
import jp.relo.cluboff.api.MyCallBack;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.Info;
import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.model.LoginRequest;
import jp.relo.cluboff.model.ValueLoginOld;
import jp.relo.cluboff.model.VersionReponse;
import jp.relo.cluboff.util.ConstansSharedPerence;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tonkhanh on 8/1/17.
 */

public class HandlerStartActivity extends BaseActivity {
    MyDatabaseHelper sqLiteOpenHelper;
    Handler handler;
    public static final int GOTOSCREEN =1;
    public static final int CHECKAUTH =2;
    public static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onResume() {
        super.onResume();
        final boolean notFirst = LoginSharedPreference.getInstance(this).get(Constant.TAG_IS_FIRST, Boolean.class);
        sqLiteOpenHelper = new MyDatabaseHelper(this);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==GOTOSCREEN){
                    if(!StringUtil.isEmpty(LoginSharedPreference.getInstance(HandlerStartActivity.this).getUserName())&&
                            !StringUtil.isEmpty(LoginSharedPreference.getInstance(HandlerStartActivity.this).getPass())){
                        goMainScreen();
                    }else{
                        if(notFirst){
                            goNextScreen();
                            //autoLogin();
                        }else{
                            goSplashScreen();
                        }
                    }

                }else if(msg.what == CHECKAUTH){
                    checkAuthMember();
                }
                return false;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new LoadValueLoginTask().execute("titanium");
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    private void goNextScreen() {
        PushvisorHandlerActivity.checkOpenedThisScreen = true;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    private void goSplashScreen() {
        PushvisorHandlerActivity.checkOpenedThisScreen = true;
        startActivity(new Intent(this, SplashScreenActivity.class));
        finish();
    }

    private void goMainScreen() {
        PushvisorHandlerActivity.checkOpenedThisScreen = true;
        startActivity(new Intent(this, MainTabActivity.class));
        finish();
    }

    private void checkAuthMember(){
        ApiInterface apiInterface = ApiClientJP.getClient().create(ApiInterface.class);
        LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(this);
        if(StringUtil.isEmpty(loginSharedPreference.getUserName())){
            handler.sendEmptyMessage(GOTOSCREEN);
        }else{
            apiInterface.memberAuthHTML(loginSharedPreference.getUserName(), loginSharedPreference.getPass()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideLoading();
                    try {
                        if(response!= null && response.body()!=null){
                            Document document = Jsoup.parse(response.body().string());
                            Utils.isAuthSuccess(HandlerStartActivity.this, document);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        handler.sendEmptyMessage(GOTOSCREEN);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppLog.log("Err: "+t.toString());
                    hideLoading();
                    handler.sendEmptyMessage(GOTOSCREEN);
                }
            });
        }

    }

    private class LoadValueLoginTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            String result =  Utils.loadSharedPrefs(HandlerStartActivity.this, params[0]);
            if(StringUtil.isEmpty(LoginSharedPreference.getInstance(HandlerStartActivity.this).getUserName())){
                ValueLoginOld loginOld = new Gson().fromJson(result, ValueLoginOld.class);
                if(loginOld!=null && !StringUtil.isEmpty(loginOld.getPassword()) && !StringUtil.isEmpty(loginOld.getMember_id())){
                    LoginSharedPreference.getInstance(HandlerStartActivity.this).setUserName(loginOld.getMember_id());
                    LoginSharedPreference.getInstance(HandlerStartActivity.this).setPass(loginOld.getPassword());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            handler.sendEmptyMessage(CHECKAUTH);

        }
    }
}
