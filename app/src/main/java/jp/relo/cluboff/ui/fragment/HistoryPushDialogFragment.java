package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import jp.relo.cluboff.R;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.HistoryPushDTO;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.services.MyAppVisorPushIntentService;
import jp.relo.cluboff.ui.BaseDialogFragmentToolbar;
import jp.relo.cluboff.ui.adapter.HistoryPushAdapter;

/**
 * Created by tonkhanh on 6/8/17.
 */

public class HistoryPushDialogFragment extends BaseDialogFragmentToolbar {
    RecyclerView rclHistoryPush;
    ArrayList<HistoryPushDTO> listData= new ArrayList<>();
    HistoryPushAdapter adapter;
    MyDatabaseHelper myDatabaseHelper;


    @Override
    protected void init(View view) {
        rclHistoryPush = (RecyclerView) view.findViewById(R.id.rclHistoryPush);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rclHistoryPush.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loaddata();
    }

    private void loaddata() {
        myDatabaseHelper=new MyDatabaseHelper(getActivity());
        listData = myDatabaseHelper.getPush();
        setAdapter();
    }

    private void setAdapter(){
        if(adapter==null){
            adapter = new HistoryPushAdapter(getActivity(), listData);
        }else{
            adapter.notifyDataSetChanged();
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
        lnToolbar.setVisibility(View.VISIBLE);
        title_toolbar.setVisibility(View.VISIBLE);
        title_toolbar.setText(R.string.title_history_push);
        imvMenu.setVisibility(View.VISIBLE);
        imvMenu.setImageResource(R.drawable.icon_close);
        imvMenu.setOnClickListener(new View.OnClickListener() {
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
}
