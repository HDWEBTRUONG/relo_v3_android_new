package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.database.ConstansDB;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.ui.activity.MainTabActivity;
import jp.relo.cluboff.util.ConstanArea;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.OnSwipeTouchListener;
import jp.relo.cluboff.views.MyMaterialSpinner;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by tonkhanh on 10/16/17.
 */

public class CouponListAreaFragment extends CouponListFragment {
    HorizontalScrollView svMenu;
    RadioGroup rgArea;
    RadioButton rbArea1;
    View rlViewAreaMap;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        svMenu = (HorizontalScrollView)view.findViewById(R.id.svMenu);
        svMenu.setVisibility(View.VISIBLE);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.head_arear_coupon, null, false);
        lvCoupon.addHeaderView(header, null, false);
        rlViewAreaMap = header.findViewById(R.id.rlViewAreaMap);

        rlViewAreaMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof MainTabActivity){
                    PostAreaWebViewFragment postAreaWebViewFragment = new PostAreaWebViewFragment();
                    ((MainTabActivity) getActivity()).openDialogFragment(postAreaWebViewFragment);
                }
            }
        });

        rgArea =(RadioGroup) view.findViewById(R.id.rgArea);
        rbArea1 =(RadioButton) view.findViewById(R.id.rbArea1);

    }

    @Override
    protected void setEventCategory() {
        spinner.setOnItemSelectedListener(new MyMaterialSpinner.OnItemSelectedListener<CatagoryDTO>() {
            @Override
            public void onItemSelected(MyMaterialSpinner view, int position, long id, CatagoryDTO item) {
                areaName = getAreaName();
                positionView = 0;
                categoryID = item.getCatagoryID();
                getListDataCategoryID(categoryID);
                tvCategory.setText(item.getGetCatagoryName());
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
    }

    private String getAreaName(){
        String result=ConstanArea.HOKKAIDO;
        switch (rgArea.getCheckedRadioButtonId()){
            case R.id.rbArea1:
                AppLog.log("rbArea1");
                result = ConstanArea.HOKKAIDO;
                break;
            case R.id.rbArea2:
                AppLog.log("rbArea2");
                result = ConstanArea.TOHOKU;
                break;
            case R.id.rbArea3:
                AppLog.log("rbArea3");
                result = ConstanArea.KANTO;
                break;
            case R.id.rbArea4:
                AppLog.log("rbArea4");
                result = ConstanArea.KOUSHINETSU;
                break;
            case R.id.rbArea5:
                result = ConstanArea.HOKURIKUTOKAI;
                AppLog.log("rbArea5");
                break;
            case R.id.rbArea6:
                AppLog.log("rbArea6");
                result = ConstanArea.KINKI;
                break;
            case R.id.rbArea7:
                AppLog.log("rbArea7");
                result = ConstanArea.CYUUGOKUSHIKOKU;
                break;
            case R.id.rbArea8:
                AppLog.log("rbArea8");
                result = ConstanArea.KYUSHU;
                break;
            case R.id.rbArea9:
                AppLog.log("rbArea9");
                result = ConstanArea.OKINAWA;
                break;
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        areaName = getAreaName();
        if(rgArea.getCheckedRadioButtonId()==-1){
            if(categoryList==null || categoryList.isEmpty()){
                mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_CATEGORY);
            }else{
                mHandler.sendEmptyMessage(CouponListFragment.MSG_SET_CATEGORY);
            }
        }else{
            if(listCoupon!= null && listCoupon.size()>0){
                if(categoryList==null || categoryList.isEmpty()){
                    mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_CATEGORY);
                }else{
                    mHandler.sendEmptyMessage(CouponListFragment.MSG_SET_CATEGORY);
                }
                mHandler.sendEmptyMessage(CouponListFragment.MSG_UPDATE_ADAPTER);
            }else{
                mHandler.sendEmptyMessage(CouponListFragment.MSG_CHECK_UPDATE);
            }
        }
        rbArea1.setChecked(true);
        setChangeSelectArea();

    }

    private void setChangeSelectArea(){
        rgArea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rbArea1:
                        AppLog.log("rbArea1");
                        areaName = ConstanArea.HOKKAIDO;
                        break;
                    case R.id.rbArea2:
                        AppLog.log("rbArea2");
                        areaName = ConstanArea.TOHOKU;
                        break;
                    case R.id.rbArea3:
                        AppLog.log("rbArea3");
                        areaName = ConstanArea.KANTO;
                        break;
                    case R.id.rbArea4:
                        AppLog.log("rbArea4");
                        areaName = ConstanArea.KOUSHINETSU;
                        break;
                    case R.id.rbArea5:
                        areaName = ConstanArea.HOKURIKUTOKAI;
                        AppLog.log("rbArea5");
                        break;
                    case R.id.rbArea6:
                        AppLog.log("rbArea6");
                        areaName = ConstanArea.KINKI;
                        break;
                    case R.id.rbArea7:
                        AppLog.log("rbArea7");
                        areaName = ConstanArea.CYUUGOKUSHIKOKU;
                        break;
                    case R.id.rbArea8:
                        AppLog.log("rbArea8");
                        areaName = ConstanArea.KYUSHU;
                        break;
                    case R.id.rbArea9:
                        AppLog.log("rbArea9");
                        areaName = ConstanArea.OKINAWA;
                        break;
                }
                //mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_DATA);
                mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_CATEGORY);
            }
        });
    }
}
