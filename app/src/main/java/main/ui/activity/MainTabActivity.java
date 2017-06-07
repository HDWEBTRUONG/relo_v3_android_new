package main.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import framework.phvtActivity.BaseActivity;
import main.R;
import main.ui.adapter.ViewPagerAdapter;
import main.ui.fragment.CouponAreaFragment;
import main.ui.fragment.CouponListFragment;
import main.ui.fragment.MembershipFragment;
import main.util.Constant;

public class MainTabActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main_tab;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        CouponAreaFragment couponAreaFragment = new CouponAreaFragment();
        couponAreaFragment.setArguments(createBundleFragment(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_AREA_COUPON, Constant.AREA_COUPON));
        adapter.addFragment(couponAreaFragment, getString(R.string.title_coupon_area));


        adapter.addFragment(new CouponListFragment(), getString(R.string.title_coupon_list));


        adapter.addFragment(new MembershipFragment(), getString(R.string.title_membership));
        viewPager.setAdapter(adapter);
    }

    public Bundle createBundleFragment(String key, String url, int keyCheckWebview){
        Bundle bundle  = new Bundle();
        bundle.putString(key,url);
        bundle.putInt(Constant.KEY_CHECK_WEBVIEW, keyCheckWebview);
        return bundle;
    }
}
