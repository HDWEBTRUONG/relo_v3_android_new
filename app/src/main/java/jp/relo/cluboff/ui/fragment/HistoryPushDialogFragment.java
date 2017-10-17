package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.HistoryPushDTO;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.services.MyAppVisorPushIntentService;
import jp.relo.cluboff.ui.BaseDialogFragmentToolbar;
import jp.relo.cluboff.adapter.HistoryPushAdapter;
import jp.relo.cluboff.util.Constant;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by tonkhanh on 6/8/17.
 */

public class HistoryPushDialogFragment extends BaseDialogFragmentToolbar implements HistoryPushAdapter.iCallDismiss {

    RecyclerView rclHistoryPush;
    ArrayList<HistoryPushDTO> listData= new ArrayList<>();
    HistoryPushAdapter adapter;
    MyDatabaseHelper myDatabaseHelper;
    Handler mHander;
    HistoryPushAdapter.iCallDetailCoupon miCallDetailCoupon;

    public static final int HANDLER_LOAD_PUSH = 0;
    public static final int HANDLER_SET_ADAPTER = 1;
    public static final int HANDLER_CLOSE = 2;

    @Override
    protected void init(View view) {
        rclHistoryPush = (RecyclerView) view.findViewById(R.id.rclHistoryPush);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rclHistoryPush.setLayoutManager(mLayoutManager);
    }

    public void setICallDetailCoupon(HistoryPushAdapter.iCallDetailCoupon miCallDetailCoupon){
        this.miCallDetailCoupon = miCallDetailCoupon;
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ReloApp)getActivity().getApplication()).trackingAnalytics(Constant.GA_HISTORYPUSH_SCREEN);
        myDatabaseHelper=new MyDatabaseHelper(getActivity());
        myDatabaseHelper.updateRead();
        EventBus.getDefault().post(new MessageEvent(MyAppVisorPushIntentService.class.getSimpleName()));

        mHander = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case HANDLER_LOAD_PUSH:
                        loaddata();
                        break;
                    case HANDLER_SET_ADAPTER:
                        setAdapter();
                        break;
                    case HANDLER_CLOSE:
                        dismiss();
                        break;
                }
            }
        };
        mHander.sendEmptyMessage(HANDLER_LOAD_PUSH);
    }

    private void loaddata() {
        listData.clear();
        myDatabaseHelper.getPushRX().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HistoryPushDTO>>() {
                    @Override
                    public void call(List<HistoryPushDTO> historyPushDTOs) {
                        listData.addAll(historyPushDTOs);
                        mHander.sendEmptyMessage(HANDLER_SET_ADAPTER);
                    }
                });
    }

    private void setAdapter(){
        if(adapter==null){
            adapter = new HistoryPushAdapter(getActivity(), listData, miCallDetailCoupon, this);
        }else{
            adapter.setNotifyDataSetChanged(listData);
        }
        rclHistoryPush.setAdapter(adapter);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_history_push;
    }

    @Override
    public void bindView(View view) {

    }

    @Override
    public void setupActionBar() {
        tvMenuSubTitle.setText(R.string.title_history_push);
        ivMenuRight.setVisibility(View.VISIBLE);
        ivMenuRight.setImageResource(R.drawable.icon_close);
        ivMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().post(new MessageEvent(HistoryPushDialogFragment.class.getSimpleName()));
    }

    @Override
    public void callDismiss() {
        mHander.sendEmptyMessage(HANDLER_CLOSE);
    }
}
