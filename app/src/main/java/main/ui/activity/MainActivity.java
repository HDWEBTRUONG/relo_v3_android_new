package main.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.io.File;

import framework.phvtActivity.BaseActivity;
import main.R;
import main.ui.fragment.CouponAreaFragment;
import main.ui.fragment.CouponListFragment;
import main.ui.fragment.MembershipFragment;
import main.util.Constant;

public class MainActivity extends BaseActivity {
    FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTabHost();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        mTabHost = (FragmentTabHost) findViewById(R.id.fragment_tab_host);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void registerEventHandlers() {

    }

    private void initTabHost() {
        mTabHost = (FragmentTabHost) findViewById(R.id.fragment_tab_host);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.content);
        //ic_add tab host
        mTabHost.addTab(setIndicator(mTabHost.newTabSpec(CouponListFragment.class.getSimpleName()),
                R.drawable.ic_home_black_24dp, getString(R.string.title_coupon_list)), CouponListFragment.class, null);

        mTabHost.addTab(setIndicator(mTabHost.newTabSpec(CouponAreaFragment.class.getSimpleName()),
                R.drawable.ic_dashboard_black_24dp, getString(R.string.title_coupon_area)), CouponAreaFragment.class,
                createBundleFragment(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_AREA_COUPON, Constant.AREA_COUPON));

        mTabHost.addTab(setIndicator(mTabHost.newTabSpec(MembershipFragment.class.getSimpleName()),
                R.drawable.ic_notifications_black_24dp, getString(R.string.title_membership)), MembershipFragment.class,
                createBundleFragment(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_MEMBER_COUPON, Constant.MEMBER_COUPON));

        /*mTabHost.getTabWidget().setDividerDrawable(R.drawable.bg_driver_bottom_bar);
        mTabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);*/
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //MyUltils.hideKeyboard(MainActivity.this);
            }
        });
    }
    private TabHost.TabSpec setIndicator(TabHost.TabSpec spec, int resourceIcon, String lable) {
        View v = LayoutInflater.from(this).inflate(R.layout.view_tab_item, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.image_view);
        imageView.setImageResource(resourceIcon);
        TextView tvLable = (TextView) v.findViewById(R.id.tvLableTab);
        tvLable.setText(lable);
        return spec.setIndicator(v);
    }

    public Bundle createBundleFragment(String key, String url, int keyCheckWebview){
        Bundle bundle  = new Bundle();
        bundle.putString(key,url);
        bundle.putInt(Constant.KEY_CHECK_WEBVIEW, keyCheckWebview);
        return bundle;
    }
    @Override
    public File getCacheDir() {
        return getApplicationContext().getCacheDir();
    }
}
