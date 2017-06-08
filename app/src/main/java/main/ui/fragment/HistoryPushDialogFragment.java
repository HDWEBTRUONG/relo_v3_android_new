package main.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import main.R;
import main.model.HistoryPushDTO;
import main.ui.BaseDialogFragment;
import main.ui.BaseDialogFragmentToolbar;
import main.ui.adapter.HistoryPushAdapter;

/**
 * Created by tonkhanh on 6/8/17.
 */

public class HistoryPushDialogFragment extends BaseDialogFragmentToolbar {
    RecyclerView rclHistoryPush;
    ArrayList<HistoryPushDTO> listData= new ArrayList<>();
    HistoryPushAdapter adapter;
    @Override
    protected void init(View view) {
        rclHistoryPush = (RecyclerView) view.findViewById(R.id.rclHistoryPush);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
        HistoryPushDTO d1 = new HistoryPushDTO();
        d1.setTimeHis("2017/06/08");
        d1.setContentHis("5/5~ 5/7西武園無料DAY開催");
        d1.setLinkHis("詳細ニこちら");
        d1.setUrlHis("https://www.google.com.vn");
        listData.add(d1);
        HistoryPushDTO d2 = new HistoryPushDTO();
        d2.setTimeHis("2017/4/20");
        d2.setContentHis("人員クポンに「ケンタッキ」を追加。");
        d2.setLinkHis("詳細：」こちら");
        d2.setUrlHis("https://www.google.com.vn");
        listData.add(d2);
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
        title_toolbar.setVisibility(View.VISIBLE);
        title_toolbar.setText(R.string.title_history_push);
        imvMenu.setVisibility(View.VISIBLE);
        imvMenu.setImageResource(R.drawable.ic_close_48dp);
        imvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
