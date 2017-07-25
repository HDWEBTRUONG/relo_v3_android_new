package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import framework.phvtFragment.BaseFragment;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.adapter.CouponListAdapter;
import jp.relo.cluboff.database.ConstansDB;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.model.Info;
import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.util.ConstansSharedPerence;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;
import jp.relo.cluboff.views.MyMaterialSpinner;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
    private Handler mHandler;

    public static final int MSG_LOAD_CATEGORY = 0;
    public static final int MSG_LOAD_DATA = 1;
    public static final int MSG_CREATE_ADAPTER = 2;
    public static final int MSG_UPDATE_ADAPTER = 3;
    public static final int MSG_SET_CATEGORY = 4;
    ArrayList<CatagoryDTO> categoryList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void setiSwitchFragment(switchFragment iSwitchFragment){
      this.iSwitchFragment = iSwitchFragment;
    }

    private void init(View view) {
        lnCatalory = (LinearLayout) view.findViewById(R.id.lnCatalory);
        lvCategoryMenu = (ListView) view.findViewById(R.id.list_category_listview);
        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        spinner = (MyMaterialSpinner) view.findViewById(R.id.spinnerCategory);
    }

    private void loadCategory() {
        myDatabaseHelper.getCatagorysRX(ConstansDB.COUPON_ALL).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CatagoryDTO>>() {
                    @Override
                    public void call(List<CatagoryDTO> catagoryDTOs) {
                        categoryList.clear();
                        categoryList.addAll(catagoryDTOs);
                        mHandler.sendEmptyMessage(CouponListFragment.MSG_SET_CATEGORY);
                        mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_DATA);
                    }
                });


    }
    public void setCategory(){
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
        myDatabaseHelper=new MyDatabaseHelper(getActivity());
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case CouponListFragment.MSG_LOAD_CATEGORY:
                        loadCategory();
                        break;
                    case CouponListFragment.MSG_LOAD_DATA:
                        getListDataCategoryID(ConstansDB.COUPON_ALL);
                        break;
                    case CouponListFragment.MSG_CREATE_ADAPTER:
                        setAdapter(true);
                        break;
                    case CouponListFragment.MSG_UPDATE_ADAPTER:
                        setAdapter(false);
                        break;
                    case CouponListFragment.MSG_SET_CATEGORY:
                        setCategory();
                        break;
                }
            }
        };
        mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_CATEGORY);
    }


    private void getListDataCategoryID(String categoryID) {
        listCoupon.clear();
        myDatabaseHelper.getCouponWithDateCategoryIDRX(categoryID).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CouponDTO>>() {
                    @Override
                    public void call(List<CouponDTO> couponDTOs) {
                        listCoupon.addAll(couponDTOs);
                        mHandler.sendEmptyMessage(CouponListFragment.MSG_CREATE_ADAPTER);
                    }
                });

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
        Info info = LoginSharedPreference.getInstance(getActivity()).get(ConstansSharedPerence.TAG_LOGIN_SAVE, Info.class);
        if(data.getCoupon_type().equals(WILL_NET_SERVER)){
            url =data.getLink_path();
        }else{
            url = "http://sptest.club-off.com/relo/coa_directlink.cfm";
            if(info!=null){
                try {
                    url = MessageFormat.format(getString(R.string.template_url_coupon),BackAES.decrypt(info.getUrl(), AESHelper.password, AESHelper.type));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_LOGIN_URL, url);
        bundle.putString(Constant.KEY_URL_TYPE, data.getCoupon_type());
        String userID = "";
        if(info!=null){
            userID = info.getUserid();
        }
        bundle.putString(Constant.TAG_USER_ID, userID);
        bundle.putString(Constant.TAG_REQUESTNO, data.getShgrid());
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
        mHandler.sendEmptyMessage(CouponListFragment.MSG_UPDATE_ADAPTER);
    }

    interface switchFragment{
        void callSwitchFragment(Bundle bundle);
    }
}