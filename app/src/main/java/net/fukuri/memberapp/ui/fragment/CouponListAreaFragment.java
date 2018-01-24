package net.fukuri.memberapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import framework.phvtUtils.AppLog;
import net.fukuri.memberapp.R;
import net.fukuri.memberapp.model.CatagoryDTO;
import net.fukuri.memberapp.ui.activity.MainTabActivity;
import net.fukuri.memberapp.util.ConstanArea;
import net.fukuri.memberapp.util.LoginSharedPreference;
import net.fukuri.memberapp.views.MyMaterialSpinner;

/**
 * Created by tonkhanh on 10/16/17.
 */

public class CouponListAreaFragment extends CouponListFragment {
    HorizontalScrollView svMenu;
    RadioGroup rgArea;
    RadioButton rbArea1,rbArea2,rbArea3,rbArea4,rbArea5,rbArea6,rbArea7,rbArea8,rbArea9;
    View rlViewAreaMap;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideLoading();
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
        rbArea2 =(RadioButton) view.findViewById(R.id.rbArea2);
        rbArea3 =(RadioButton) view.findViewById(R.id.rbArea3);
        rbArea4 =(RadioButton) view.findViewById(R.id.rbArea4);
        rbArea5 =(RadioButton) view.findViewById(R.id.rbArea5);
        rbArea6 =(RadioButton) view.findViewById(R.id.rbArea6);
        rbArea7 =(RadioButton) view.findViewById(R.id.rbArea7);
        rbArea8 =(RadioButton) view.findViewById(R.id.rbArea8);
        rbArea9 =(RadioButton) view.findViewById(R.id.rbArea9);

    }

    @Override
    protected void setEventCategory() {
        spinner.setOnItemSelectedListener(new MyMaterialSpinner.OnItemSelectedListener<CatagoryDTO>() {
            @Override
            public void onItemSelected(MyMaterialSpinner view, int position, long id, CatagoryDTO item) {
                areaName = getAreaName();
                positionView = 0;
                categoryID = item.getCatagoryID();
                isSelected = true;
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
        if(!LoginSharedPreference.getInstance(getActivity()).checkDownloadDone()){
            myDatabaseHelper.clearData();
            return;
        }
        if(rgArea.getCheckedRadioButtonId()==-1){
            if(categoryList==null || categoryList.isEmpty()){
                mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_CATEGORY);
            }else{
                mHandler.sendEmptyMessage(CouponListFragment.MSG_SET_CATEGORY);
            }
        }else{
            if(listCoupon!= null && listCoupon.size()>0){
                if(categoryList==null || categoryList.isEmpty() && categoryList.size()<=1){
                    mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_CATEGORY);
                }else{
                    mHandler.sendEmptyMessage(CouponListFragment.MSG_SET_CATEGORY);
                }
                mHandler.sendEmptyMessage(CouponListFragment.MSG_UPDATE_ADAPTER);
            }else{
                mHandler.sendEmptyMessage(CouponListFragment.MSG_CHECK_UPDATE);
            }
        }

        setChangeSelectArea();
        int tabSave = LoginSharedPreference.getInstance(getActivity()).getTabSave();
        switch (tabSave){
            case 0:
                rbArea1.setChecked(true);
                break;
            case 1:
                rbArea2.setChecked(true);
                break;
            case 2:
                rbArea3.setChecked(true);
                break;
            case 3:
                rbArea4.setChecked(true);
                break;
            case 4:
                rbArea5.setChecked(true);
                break;
            case 5:
                rbArea6.setChecked(true);
                break;
            case 6:
                rbArea7.setChecked(true);
                break;
            case 7:
                rbArea8.setChecked(true);
                break;
            case 8:
                rbArea9.setChecked(true);
                break;
        }

    }

    private void setChangeSelectArea(){
        rgArea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
                switch (checkedId){
                    case R.id.rbArea1:
                        AppLog.log("rbArea1");
                        loginSharedPreference.setTabSave(0);
                        areaName = ConstanArea.HOKKAIDO;
                        break;
                    case R.id.rbArea2:
                        AppLog.log("rbArea2");
                        loginSharedPreference.setTabSave(1);
                        areaName = ConstanArea.TOHOKU;
                        break;
                    case R.id.rbArea3:
                        AppLog.log("rbArea3");
                        loginSharedPreference.setTabSave(2);
                        areaName = ConstanArea.KANTO;
                        break;
                    case R.id.rbArea4:
                        AppLog.log("rbArea4");
                        loginSharedPreference.setTabSave(3);
                        areaName = ConstanArea.KOUSHINETSU;
                        break;
                    case R.id.rbArea5:
                        AppLog.log("rbArea5");
                        loginSharedPreference.setTabSave(4);
                        areaName = ConstanArea.HOKURIKUTOKAI;
                        break;
                    case R.id.rbArea6:
                        AppLog.log("rbArea6");
                        loginSharedPreference.setTabSave(5);
                        areaName = ConstanArea.KINKI;
                        break;
                    case R.id.rbArea7:
                        AppLog.log("rbArea7");
                        loginSharedPreference.setTabSave(6);
                        areaName = ConstanArea.CYUUGOKUSHIKOKU;
                        break;
                    case R.id.rbArea8:
                        AppLog.log("rbArea8");
                        loginSharedPreference.setTabSave(7);
                        areaName = ConstanArea.KYUSHU;
                        break;
                    case R.id.rbArea9:
                        AppLog.log("rbArea9");
                        loginSharedPreference.setTabSave(8);
                        areaName = ConstanArea.OKINAWA;
                        break;
                }
                //mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_DATA);
                mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_CATEGORY);
            }
        });
    }
}
