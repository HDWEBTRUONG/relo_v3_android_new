package jp.relo.cluboff.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import framework.phvtFragment.BaseFragment;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.BuildConfig;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.api.ApiClientJP;
import jp.relo.cluboff.api.ApiInterface;
import jp.relo.cluboff.model.BlockEvent;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import jp.relo.cluboff.util.ase.EventBusTimeReload;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.TimeInterval;

/**
 * Created by tonkhanh on 10/18/17.
 */

public class MemberAuthFragment extends BaseFragment {
    TextView tvValid;
    TextView tvMemberID;
    TextView tvPhone;
    View svError;
    String userID;
    String pass;
    Handler handler;
    Observable observable;
    Subscription subscription= null;

    public static final int UPDATE_LAYOUT = 1;
    public static final int UPDATE_LAYOUT_ERROR = 2;
    public static final String TAG = MemberAuthFragment.class.getSimpleName();

    @Override
    public int getRootLayoutId() {
        return R.layout.member_auth_layout;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        tvValid = (TextView) root.findViewById(R.id.tvValid);
        tvMemberID = (TextView) root.findViewById(R.id.tvMemberID);
        tvPhone = (TextView) root.findViewById(R.id.tvPhone);
        svError = root.findViewById(R.id.svError);

    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
        userID = loginSharedPreference.getUserName();
        pass = loginSharedPreference.getPass();
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvPhone.getText().toString()));
                startActivity(intent);
            }
        });
        tvValid.setText(MessageFormat.format(getString(R.string.member_auth_note_only_today), new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime())));
        if (!userID.contains("-")) {
            userID = new StringBuffer(userID).insert(userID.length() - 4, "-").toString();
        }
        tvMemberID.setText(userID);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_LAYOUT_ERROR:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    svError.setVisibility(View.VISIBLE);
                                    EventBus.getDefault().post(new BlockEvent());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        break;
                }
                return false;
            }
        });
        if(ReloApp.isBlockAuth()){
            handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
        }else{
            checkAuthMember();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    private void checkAuthMember(){
        ApiInterface apiInterface = ApiClientJP.getClient().create(ApiInterface.class);
        apiInterface.memberAuthHTML(userID, pass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();
                try {
                    Document document = Jsoup.parse(response.body().string());
                    if(Utils.isAuthSuccess(getActivity(),document)){
                           /* if(subscription!=null){
                                subscription.unsubscribe();
                            }
                            observable = Observable.interval(30, TimeUnit.MINUTES)
                                    .timeInterval();
                            observable.observeOn(AndroidSchedulers.mainThread());
                            subscription = observable.subscribe(new Action1<TimeInterval<Long>>() {
                                        @Override
                                        public void call(TimeInterval<Long> longTimeInterval) {
                                            EventBus.getDefault().post(new EventBusTimeReload(longTimeInterval.getValue()));
                                        }
                                    });*/

                    }else{
                        handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
            }
        });
    }

}
