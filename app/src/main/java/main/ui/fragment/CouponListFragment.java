package main.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import framework.phvtFragment.BaseFragment;
import main.R;
import main.ReloApp;
import main.database.MyDatabaseHelper;
import main.model.CouponDTO;
import main.ui.BaseFragmentBottombar;
import main.ui.activity.WebviewActivity;
import main.ui.adapter.CouponListAdapter;
import main.util.Constant;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponListFragment extends BaseFragmentBottombar implements View.OnClickListener,CouponListAdapter.iClickButton{

    LinearLayout lnCatalory;
    ListView lvCategoryMenu;
    MaterialSpinner spinner;
    MyDatabaseHelper myDatabaseHelper;
    CouponListAdapter adapter;
    ArrayList<CouponDTO> listCoupon=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDatabaseHelper=new MyDatabaseHelper(getActivity());
    }

    private void init(View view) {
        lnCatalory = (LinearLayout) view.findViewById(R.id.lnCatalory);
        lvCategoryMenu = (ListView) view.findViewById(R.id.list_category_listview);
        spinner = (MaterialSpinner) view.findViewById(R.id.spinnerCategory);
        spinner.setItems("カテゴリを選ぶ 1", "カテゴリを選ぶ 2", "カテゴリを選ぶ 3", "カテゴリを選ぶ 4", "カテゴリを選ぶ 5");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(getActivity(), "Clicked " + item, Toast.LENGTH_SHORT).show();
            }
        });
        lnCatalory.setOnClickListener(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_coupon_list;
    }
    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        init(root);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listCoupon = getListData();
        setAdapter();
    }

    @Override
    public void setupBottombar() {
        lnBottom.setVisibility(View.VISIBLE);
        imvBackBottomBar.setVisibility(View.VISIBLE);
        imvForwardBottomBar.setVisibility(View.VISIBLE);
        imvBrowserBottomBar.setVisibility(View.GONE);
        imvReloadBottomBar.setVisibility(View.VISIBLE);

        //temp
        imvBackBottomBar.setEnabled(false);
        imvForwardBottomBar.setEnabled(false);
    }

    private ArrayList getListData() {
        ArrayList<CouponDTO> results = new ArrayList<CouponDTO>();
        results = myDatabaseHelper.getAllCoupon();
        // Add some more dummy data for testing
        return results;
    }
    private void setAdapter(){
        if(adapter==null){
            adapter =new CouponListAdapter(getContext(), listCoupon,this);
        }else{
            adapter.notifyDataSetChanged();
        }
        lvCategoryMenu.setAdapter(adapter);
    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //Test GA
        Activity act = getActivity();
        if (act != null) {
            ReloApp app = (ReloApp) act.getApplication();
            app.trackingAnalytics(Constant.GA_POPULAR_COUPON_SCREEN);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.lnCatalory){
            //clickCategoryMenu();
            spinner.expand();
        }
    }
    @Override
    public void callback(CouponDTO data) {
        Toast.makeText(getActivity(),"Click: "+data.getName(),Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_LOGIN_URL, data.getC_url());
        bundle.putInt(Constant.KEY_CHECK_WEBVIEW,Constant.DETAIL_COUPON);
        Intent intent = new Intent(getActivity(), WebviewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/
}