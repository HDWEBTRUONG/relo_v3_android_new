package jp.relo.cluboff.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ui.BaseActivityToolbar;
import jp.relo.cluboff.ui.adapter.MenuListAdapter;
import jp.relo.cluboff.ui.adapter.ViewPagerAdapter;
import jp.relo.cluboff.ui.fragment.CouponAreaFragment;
import jp.relo.cluboff.ui.fragment.CouponListFragment;
import jp.relo.cluboff.ui.fragment.HistoryPushDialogFragment;
import jp.relo.cluboff.ui.fragment.MembershipFragment;
import jp.relo.cluboff.util.Constant;

import static jp.relo.cluboff.R.id.lnToolbar;

public class MainTabActivity extends BaseActivityToolbar {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    MenuListAdapter mMenuAdapter;
    String[] titleMenu;
    DrawerLayout mDrawerLayoutMenu;
    ListView mDrawerListMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Generate title
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setupToolbar() {
        lnToolbar.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        imvMenu.setVisibility(View.VISIBLE);
        imvInfo.setVisibility(View.VISIBLE);
        tvCount.setVisibility(View.VISIBLE);
        //toolbar.setTitle("Abcd");

        //event
        imvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFragment(new HistoryPushDialogFragment());
            }
        });
        imvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayoutMenu.isDrawerOpen(mDrawerListMenu)) {
                    mDrawerLayoutMenu.closeDrawer(mDrawerListMenu);
                } else {
                    mDrawerLayoutMenu.openDrawer(mDrawerListMenu);
                }
            }
        });
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main_tab;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        titleMenu = new String[] { "TOPへ戻る", "アプリの使い方"};

        // Locate DrawerLayout in drawer_main.xml
        mDrawerLayoutMenu = (DrawerLayout) findViewById(R.id.drawerMenuRight);

        // Locate ListView in drawer_main.xml
        mDrawerListMenu = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_listview_menu, null, false);
        mDrawerListMenu.addHeaderView(header);

        // Set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayoutMenu.setDrawerShadow(R.drawable.bg_catelogy_coupon,
                GravityCompat.START);

        mDrawerLayoutMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Pass results to MenuListAdapter Class
        mMenuAdapter = new MenuListAdapter(this, titleMenu);

        // Set the MenuListAdapter to the ListView
        mDrawerListMenu.setAdapter(mMenuAdapter);
        mDrawerListMenu.setOnItemClickListener(new DrawerItemClickListener());
    }
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {
        // Locate Position
        AppLog.log("Menu: "+position);
        switch (position) {
            case 0:

                break;
            case 1:
                break;
        }
        mDrawerListMenu.setItemChecked(position, true);
        // Close drawer
        mDrawerLayoutMenu.closeDrawer(mDrawerListMenu);
    }

    @Override
    protected void registerEventHandlers() {

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CouponAreaFragment couponAreaFragment = new CouponAreaFragment();
        couponAreaFragment.setArguments(createBundleFragment(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_AREA_COUPON, Constant.AREA_COUPON));
        adapter.addFragment(couponAreaFragment, getString(R.string.title_coupon_area));

        adapter.addFragment(new CouponListFragment(), getString(R.string.title_coupon_list));

        MembershipFragment membershipFragment = new MembershipFragment();
        membershipFragment.setArguments(createBundleFragment(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_MEMBER_COUPON, Constant.MEMBER_COUPON));
        adapter.addFragment(membershipFragment, getString(R.string.title_membership));

        viewPager.setAdapter(adapter);
    }

    public Bundle createBundleFragment(String key, String url, int keyCheckWebview){
        Bundle bundle  = new Bundle();
        bundle.putString(key,url);
        bundle.putInt(Constant.KEY_CHECK_WEBVIEW, keyCheckWebview);
        return bundle;
    }

}
