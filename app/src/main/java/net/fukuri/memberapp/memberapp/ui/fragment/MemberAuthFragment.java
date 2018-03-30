package net.fukuri.memberapp.memberapp.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import framework.phvtFragment.BaseFragment;
import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.ReloApp;
import net.fukuri.memberapp.memberapp.api.ApiClientJP;
import net.fukuri.memberapp.memberapp.api.ApiInterface;
import net.fukuri.memberapp.memberapp.model.BlockEvent;
import net.fukuri.memberapp.memberapp.util.LoginSharedPreference;
import net.fukuri.memberapp.memberapp.util.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;

/**
 * Created by tonkhanh on 10/18/17.
 */

public class MemberAuthFragment extends BaseFragment {
    TextView tvValid;
    TextView tvMemberID;
    String userID;
    String pass;
    Observable observable;
    Subscription subscription= null;
    public static final String TAG = MemberAuthFragment.class.getSimpleName();

    @Override
    public int getRootLayoutId() {
        return R.layout.member_auth_layout;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        tvValid = (TextView) root.findViewById(R.id.tvValid);
        tvMemberID = (TextView) root.findViewById(R.id.tvMemberID);

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
        tvValid.setText(MessageFormat.format(getString(R.string.member_auth_note_only_today), new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime())));
        if (!userID.contains("-")) {
            userID = new StringBuffer(userID).insert(userID.length() - 4, "-").toString();
        }
        tvMemberID.setText(userID);

    }

    /*private void checkAuthMember(){
        ApiInterface apiInterface = ApiClientJP.getClient().create(ApiInterface.class);
        apiInterface.memberAuthHTML(userID, pass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();
                try {
                    if(response!=null && response.body()!=null){
                        Document document = Jsoup.parse(response.body().string());
                        if(!Utils.isAuthSuccess(getActivity(),document)){
                            handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    //handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                //handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
            }
        });
    }*/

}
