package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.model.ControlWebEventBus;
import jp.relo.cluboff.model.DetailCouponDetailVisible;
import jp.relo.cluboff.ui.BaseFragmentBottombar;
import jp.relo.cluboff.ui.activity.MainTabActivity;
import jp.relo.cluboff.util.IControlBottom;

/**
 * Created by tonkhanh on 7/3/17.
 */

public class CouponListContainerFragment extends BaseFragmentBottombar implements CouponListFragment.switchFragment, IControlBottom {
    MainTabActivity mainTabActivity;
    @Override
    public void onResume() {
        super.onResume();
        mainTabActivity = (MainTabActivity)getActivity();
        if(isAdded()){
            switchFragmentCoupon(false,null);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.coupon_list_container_layout;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void setupBottombar() {
        lnBottom.setVisibility(View.VISIBLE);
        imvBackBottomBar.setVisibility(View.VISIBLE);
        imvForwardBottomBar.setVisibility(View.VISIBLE);
        imvBrowserBottomBar.setVisibility(View.GONE);
        llBrowser.setVisibility(View.INVISIBLE);
        imvReloadBottomBar.setVisibility(View.VISIBLE);
        setBottomListener();
        disbleBottom();
    }

    private void setBottomListener() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ControlWebEventBus(true,false,false));
            }
        });
        llForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ControlWebEventBus(false,true,false));
            }
        });
        llReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ControlWebEventBus(false,false,true));
            }
        });
    }

    public void disbleBottom(){
        imvBackBottomBar.setEnabled(false);
        imvForwardBottomBar.setEnabled(false);
        imvReloadBottomBar.setEnabled(false);
        llBack.setEnabled(false);
        llForward.setEnabled(false);
        llReload.setEnabled(false);
    }
    public  void switchFragmentCoupon(boolean isDetail,Bundle bundle){
        if(isDetail){
            WebViewDetailCouponFragment webViewFragment = new WebViewDetailCouponFragment();
            webViewFragment.setControlBottom(this);
            switchFragment(webViewFragment,mainTabActivity,bundle);
        }else{
            disbleBottom();
            CouponListFragment couponListFragment = new CouponListFragment();
            couponListFragment.setiSwitchFragment(this);
            switchFragment(couponListFragment,mainTabActivity,null);
        }
    }

    @Override
    public void callSwitchFragment(Bundle bundle) {
        switchFragmentCoupon(true, bundle);
    }

    @Override
    public void canBack(boolean isCanBack) {
        imvBackBottomBar.setEnabled(isCanBack);
        llBack.setEnabled(isCanBack);
    }

    @Override
    public void canForward(boolean isCanForward) {
        imvForwardBottomBar.setEnabled(isCanForward);
        llForward.setEnabled(isCanForward);
    }

    @Override
    public void canReload(boolean isCanReload) {
        imvReloadBottomBar.setEnabled(isCanReload);
        llReload.setEnabled(isCanReload);
    }

    @Override
    public void disableAll() {
        disbleBottom();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        EventBus.getDefault().post(new DetailCouponDetailVisible(isVisibleToUser));
        if(isAdded()&&isVisibleToUser){
            if (getActivity().getSupportFragmentManager().findFragmentById(R.id.flContainerCouponList) == null) {
                switchFragmentCoupon(false,null);
            }
        }
    }
}
