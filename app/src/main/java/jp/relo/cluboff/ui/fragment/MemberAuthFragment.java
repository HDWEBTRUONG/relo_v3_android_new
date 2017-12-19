package jp.relo.cluboff.ui.fragment;

import android.content.Intent;
import android.net.Uri;
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

import framework.phvtFragment.BaseFragment;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.api.ApiClientJP;
import jp.relo.cluboff.api.ApiInterface;
import jp.relo.cluboff.model.BlockEvent;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tonkhanh on 10/18/17.
 */

public class MemberAuthFragment extends BaseFragment {
    TextView tvValid;
    TextView tvMemberID;
    TextView tvPhone;
    View svError;
    String userID;
    Handler handler;

    public static final int UPDATE_LAYOUT=1;
    public static final int UPDATE_LAYOUT_ERROR=2;
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
        svError =  root.findViewById(R.id.svError);

    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tvPhone.getText().toString()));
                startActivity(intent);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case UPDATE_LAYOUT:
                        tvValid.setText(MessageFormat.format(getString(R.string.member_auth_note_only_today),new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime())));
                        if(!userID.contains("-")){
                            userID =new StringBuffer(userID).insert(userID.length()-4, "-").toString();
                        }
                        tvMemberID.setText(userID);
                        break;
                    case UPDATE_LAYOUT_ERROR:
                        svError.setVisibility(View.VISIBLE);
                        EventBus.getDefault().post(new BlockEvent());
                        break;
                }
                return false;
            }
        });

        ApiInterface apiInterface = ApiClientJP.getClient().create(ApiInterface.class);
        LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
        userID = loginSharedPreference.getUserName();
        String pass = loginSharedPreference.getPass();
        apiInterface.memberAuthHTML(userID, pass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();
                try {
                    Document document = Jsoup.parse(response.body().string());
                    Element divChildren = document.select("html").first();
                    for (int i = 0; i < divChildren.childNodes().size(); i++) {
                        Node child = divChildren.childNode(i);
                        if (child.nodeName().equals("#comment")) {
                            String valueAuth = child.toString();
                            if(Utils.parserInt(valueAuth.substring(valueAuth.indexOf("<STS>")+5,valueAuth.indexOf("</STS>")))==0){
                                LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
                                loginSharedPreference.setKEY_APPU(valueAuth.substring(valueAuth.indexOf("<APPU>")+6,valueAuth.indexOf("</APPU>")));
                                loginSharedPreference.setKEY_APPP(valueAuth.substring(valueAuth.indexOf("<APPP>")+6,valueAuth.indexOf("</APPP>")));
                                handler.sendEmptyMessage(UPDATE_LAYOUT);
                            }else{
                                handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
                            }
                            return;
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppLog.log("Err: "+t.toString());
                hideLoading();
                handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
            }
        });
    }
}
