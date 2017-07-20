package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import framework.phvtFragment.BaseFragment;
import jp.relo.cluboff.R;
import jp.relo.cluboff.database.ConstansDB;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.adapter.CouponListAdapter;
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
        categoryList.add(0,new CatagoryDTO(ConstansDB.COUPON_ALL,getString(R.string.catalogy)));
        categoryList.add(1,new CatagoryDTO(ConstansDB.COUPON_FAV,getString(R.string.catalogy_fav)));
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
        setAdapter(true);
    }
    private void setAdapter(boolean isReload){
        if(adapter==null){
            adapter =new CouponListAdapter(getContext(), listCoupon,this);
            lvCategoryMenu.setAdapter(adapter);
        }else{
            adapter.setDataChange(listCoupon);
            if(isReload){
                lvCategoryMenu.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void registerEventHandlers() {

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
            //url = MessageFormat.format(getString(R.string.template_url_coupon),Constant.TEST_LINK_COUPON);//""+ data.getLink_path();
            url = "http://sptest.club-off.com/relo/coa_directlink.cfm";
        }

        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_LOGIN_URL, url);
        bundle.putString(Constant.KEY_URL_TYPE, data.getCoupon_type());
        bundle.putString(Constant.TAG_USER_ID, "f4od/GCIvlp402l4ZOkYzg==");
        bundle.putString(Constant.TAG_REQUESTNO, "19136");
        bundle.putString(Constant.TAG_SENICODE, "1");
        bundle.putInt(Constant.KEY_CHECK_WEBVIEW,Constant.DETAIL_COUPON);
        iSwitchFragment.callSwitchFragment(bundle);
    }

    @Override
    public void like(int id, int isLiked) {
        int newLikeValue = isLiked == 0?1:0;
        myDatabaseHelper.likeCoupon(id,newLikeValue);
        for(int i =0; i < listCoupon.size();i++){
            if(listCoupon.get(i).getID() == id){
                listCoupon.get(i).setLiked(newLikeValue);
                break;
            }
        }
        setAdapter(false);
    }

    interface switchFragment{
        void callSwitchFragment(Bundle bundle);
    }
}