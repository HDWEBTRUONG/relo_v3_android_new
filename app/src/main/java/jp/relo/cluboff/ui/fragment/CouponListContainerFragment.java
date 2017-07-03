package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import jp.relo.cluboff.R;
import jp.relo.cluboff.model.ControlWebEventBus;
import jp.relo.cluboff.ui.BaseFragmentBottombar;
import jp.relo.cluboff.ui.activity.MainTabActivity;
import jp.relo.cluboff.util.IControlBottom;

/**
 * Created by tonkhanh on 7/3/17.
 */

public class CouponListContainerFragment extends BaseFragmentBottombar implements CouponListFragment.switchFragment, IControlBottom {
    @Override
    public void onResume() {
        super.onResume();
        switchFragmentCoupon(false,null);
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
        imvReloadBottomBar.setVisibility(View.VISIBLE);
        setBottomListener();
        disbleBottom();
    }

    private void setBottomListener() {
        imvBackBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ControlWebEventBus(true,false,false));
            }
        });
        imvForwardBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ControlWebEventBus(false,true,false));
            }
        });
        imvReloadBottomBar.setOnClickListener(new View.OnClickListener() {
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
    }
    public  void switchFragmentCoupon(boolean isDetail,Bundle bundle){
        if(isDetail){
            WebViewFragment webViewFragment = new WebViewFragment();
            webViewFragment.setControlBottom(this);
            switchFragment(webViewFragment,((MainTabActivity)getActivity()),bundle);
        }else{
            disbleBottom();
            CouponListFragment couponListFragment = new CouponListFragment();
            couponListFragment.setiSwitchFragment(this);
            switchFragment(couponListFragment,((MainTabActivity)getActivity()),null);
        }
    }

    @Override
    public void callSwitchFragment(Bundle bundle) {
        switchFragmentCoupon(true, bundle);
    }

    @Override
    public void canBack(boolean isCanBack) {
        imvBackBottomBar.setEnabled(isCanBack);
    }

    @Override
    public void canForward(boolean isCanForward) {
        imvForwardBottomBar.setEnabled(isCanForward);
    }

    @Override
    public void canReload(boolean isCanReload) {
        imvReloadBottomBar.setEnabled(isCanReload);
    }

    @Override
    public void disableAll() {
        disbleBottom();
    }
}
