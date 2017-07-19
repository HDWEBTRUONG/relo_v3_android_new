package jp.relo.cluboff.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.ArrayList;

import framework.phvtFragment.BaseFragment;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.ui.adapter.CouponListAdapter;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.views.MyMaterialSpinner;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponListFragment extends BaseFragment implements View.OnClickListener,CouponListAdapter.iClickButton{

    LinearLayout lnCatalory;
    ListView lvCategoryMenu;
    TextView tvCategory;
    MyMaterialSpinner spinner;
    MyDatabaseHelper myDatabaseHelper;
    CouponListAdapter adapter;
    ArrayList<CouponDTO> listCoupon=new ArrayList<>();
    public static String WILL_NET_SERVER="1";
    public switchFragment iSwitchFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDatabaseHelper=new MyDatabaseHelper(getActivity());
    }
    public void setiSwitchFragment(switchFragment iSwitchFragment){
      this.iSwitchFragment = iSwitchFragment;
    }

    private void init(View view) {
        lnCatalory = (LinearLayout) view.findViewById(R.id.lnCatalory);
        lvCategoryMenu = (ListView) view.findViewById(R.id.list_category_listview);
        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        spinner = (MyMaterialSpinner) view.findViewById(R.id.spinnerCategory);
        setCategory();
    }

    private void setCategory() {
        ArrayList<CatagoryDTO> categoryList=myDatabaseHelper.getCategory();
        CatagoryDTO allItem = new CatagoryDTO("","All");
        categoryList.add(0,allItem);
        spinner.setItems(categoryList);
        spinner.setOnItemSelectedListener(new MyMaterialSpinner.OnItemSelectedListener<CatagoryDTO>() {

            @Override
            public void onItemSelected(MyMaterialSpinner view, int position, long id, CatagoryDTO item) {
                getListDataCategoryID(item.getCatagoryID());
                tvCategory.setText(item.getGetCatagoryName());
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
        getListDataCategoryID("");
    }


    private void getListDataCategoryID(String categoryID) {
        listCoupon.clear();
        listCoupon = myDatabaseHelper.getCouponWithDateCategoryID(categoryID);
        setAdapter();
    }
    private void setAdapter(){
        if(adapter==null){
            adapter =new CouponListAdapter(getContext(), listCoupon,this);
        }else{
            adapter.setDataChange(listCoupon);
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
        String url="";
        if(data.getCoupon_type().equals(WILL_NET_SERVER)){
            url =data.getLink_path();
        }else{
            //ToDo add url
            url = MessageFormat.format(getString(R.string.template_url_coupon),Constant.TEST_LINK_COUPON);//""+ data.getLink_path();
        }

        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_LOGIN_URL, url);
        bundle.putInt(Constant.KEY_CHECK_WEBVIEW,Constant.DETAIL_COUPON);
        iSwitchFragment.callSwitchFragment(bundle);
    }

    @Override
    public void like(int id) {
        myDatabaseHelper.likeCoupon(id);
        for(CouponDTO item : listCoupon){
            if(item.getID() == id){
                item.setLiked(1);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    interface switchFragment{
        void callSwitchFragment(Bundle bundle);
    }
}