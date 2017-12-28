package jp.relo.cluboff.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.MessageFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.vansuita.library.CheckNewAppVersion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import biz.appvisor.push.android.sdk.AppVisorPush;
import framework.phvtCommon.FragmentTransitionInfo;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.adapter.MenuListAdapter;
import jp.relo.cluboff.api.ApiClient;
import jp.relo.cluboff.api.ApiInterface;
import jp.relo.cluboff.api.MyCallBack;
import jp.relo.cluboff.model.BlockEvent;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.model.ReloadEvent;
import jp.relo.cluboff.model.SaveLogin;
import jp.relo.cluboff.model.VersionReponse;
import jp.relo.cluboff.ui.BaseActivityToolbar;
import jp.relo.cluboff.ui.fragment.CouponListAreaFragment;
import jp.relo.cluboff.ui.fragment.CouponListFragment;
import jp.relo.cluboff.ui.fragment.HowToDialogFragment;
import jp.relo.cluboff.ui.fragment.MemberAuthFragment;
import jp.relo.cluboff.ui.fragment.PostAreaWebViewFragment;
import jp.relo.cluboff.ui.fragment.PostAreaWebViewFragment2;
import jp.relo.cluboff.ui.fragment.PostMemberFragment;
import jp.relo.cluboff.ui.fragment.PostMemberWebViewFragment;
import jp.relo.cluboff.ui.fragment.WebViewDialogFragment;
import jp.relo.cluboff.util.AnimationUtil;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;

public class MainTabActivity extends BaseActivityToolbar {
    MenuListAdapter mMenuAdapter;
    String[] titleMenu;
    DrawerLayout mDrawerLayoutMenu;
    ListView mDrawerListMenu;
    //main AppVisor processor
    private AppVisorPush appVisorPush;
    private FrameLayout memberSiteFragmentContainer;
    private FrameLayout mapSiteFragmentContainer;

    //Handler handler;
    public static final int INDEX_AREA=0;
    public static final int INDEX_TOP=1;
    public static final int INDEX_MEMBER=2;
    long lateResume;
    FragmentTabHost mTabHost;
    View llMember;
    View llTab;
    View llMain;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        LoginSharedPreference.getInstance(this).setValueStop(Utils.dateTimeValue());
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEvent(MessageEvent event) {
        if(Constant.TOP_COUPON.equals(event.getMessage())){
            selectPage(INDEX_TOP);
        }
    }

    @Subscribe
    public void onEvent(BlockEvent event) {
        ivMenuRight.setEnabled(false);
        llTab.setVisibility(View.GONE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pushProcess();
        setupView();
        replaceFragment(PostMemberFragment.newInstance(Constant.KEY_LOGIN_URL, "", Constant.MEMBER_COUPON),R.id.container_member_fragment,"MEMBER_FRAGMENT", new FragmentTransitionInfo());
        replaceFragment(PostAreaWebViewFragment2.newInstance(),R.id.container_map_fragment,"MAP_AREA_FRAGMENT", new FragmentTransitionInfo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        long valueTime = Utils.dateTimeValue();
        lateResume = LoginSharedPreference.getInstance(this).getValueStop();
        tvMenuTitle.setText(R.string.title_area);
        tvMenuSubTitle.setText(R.string.title_coupon_area);
        //loadCountPush();
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            String target = bundle.getString(Constant.TARGET_PUSH);
            if(Constant.TARGET_PUSH_SCREEN_AREA.equalsIgnoreCase(target)){
                selectPage(INDEX_AREA);
            }else if(Constant.TARGET_PUSH_SCREEN_SITE.equalsIgnoreCase(target)){
                selectPage(INDEX_MEMBER);
            }else if(Constant.TARGET_PUSH_SCREEN_LIST.equalsIgnoreCase(target)){
                selectPage(INDEX_TOP);
            }else{
                selectPage(INDEX_TOP);
            }
        }else{
            if(valueTime - lateResume > Constant.LIMIT_ON_BACKGROUND){
                EventBus.getDefault().post(new ReloadEvent(true));
            }
        }
    }
    void selectPage(int pageIndex){
        mTabHost.setCurrentTab(pageIndex);
    }

    @Override
    public void setupToolbar() {
        ivMenuRight.setVisibility(View.VISIBLE);
        ivMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDrawerLayoutMenu!=null){
                    if (mDrawerLayoutMenu.isDrawerOpen(mDrawerListMenu)) {
                        mDrawerLayoutMenu.closeDrawer(mDrawerListMenu);
                    } else {
                        mDrawerLayoutMenu.openDrawer(mDrawerListMenu);
                    }
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
        mDrawerLayoutMenu = (DrawerLayout) findViewById(R.id.drawerMenuRight);
        mTabHost = (FragmentTabHost) findViewById(R.id.fragment_tab_host);
        llMember = findViewById(R.id.llMember);
        llMain = findViewById(R.id.llMain);
        llTab = findViewById(R.id.llTab);
        memberSiteFragmentContainer = (FrameLayout) findViewById(R.id.container_member_fragment);
        mapSiteFragmentContainer = (FrameLayout) findViewById(R.id.container_map_fragment);

        // Locate ListView in drawer_main.xml
        mDrawerListMenu = (ListView) findViewById(R.id.left_drawer);
    }

    private void setupView(){
        // Locate DrawerLayout in drawer_main.xml
        titleMenu = new String[] { getString(R.string.menu_top), getString(R.string.menu_FAQ)};
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_listview_menu, null, false);
        mDrawerListMenu.addHeaderView(header);
        // Pass results to MenuListAdapter Class
        mMenuAdapter = new MenuListAdapter(this, titleMenu);
        // opens
        mDrawerLayoutMenu.setDrawerShadow(R.drawable.bg_catelogy_coupon,
                GravityCompat.START);

        mDrawerLayoutMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // Set the MenuListAdapter to the ListView
        mDrawerListMenu.setAdapter(mMenuAdapter);
        mDrawerListMenu.setOnItemClickListener(new DrawerItemClickListener());
        initTabHost();
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
        switch (position) {
            case 1:
                selectPage(INDEX_TOP);
                break;
            case 2:
                openDialogFragment(WebViewDialogFragment.newInstance(Constant.URL_FAQ,getString(R.string.menu_FAQ),""));
                break;
        }
        mDrawerListMenu.setItemChecked(position, true);
        // Close drawer
        mDrawerLayoutMenu.closeDrawer(mDrawerListMenu);
    }

    @Override
    protected void registerEventHandlers() {

    }

    private void initTabHost() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.container_fragment);

        mTabHost.addTab(setIndicator(mTabHost.newTabSpec(MemberAuthFragment.class.getSimpleName()),
                R.drawable.tab_area, getString(R.string.title_coupon_area)), MemberAuthFragment.class, null);

        Bundle bundlePupolar = new Bundle();
        bundlePupolar.putBoolean(Constant.DATA_COUPON_URL,false);
        mTabHost.addTab(setIndicator(mTabHost.newTabSpec(CouponListFragment.class.getSimpleName()),
                R.drawable.tab_coupon_all, getString(R.string.title_coupon_list)), CouponListFragment.class, bundlePupolar);

        Bundle bundleArea = new Bundle();
        bundleArea.putBoolean(Constant.DATA_COUPON_URL,true);
        mTabHost.addTab(setIndicator(mTabHost.newTabSpec(CouponListAreaFragment.class.getSimpleName()),
                R.drawable.tab_coupon_area, getString(R.string.title_coupon_list_area)), CouponListAreaFragment.class, bundleArea);

        llMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PostMemberWebViewFragment postMemberWebViewFragment = PostMemberWebViewFragment.newInstance();
//                Bundle bundle = createBundleFragment(Constant.KEY_LOGIN_URL, "", Constant.MEMBER_COUPON);
//                postMemberWebViewFragment.setArguments(bundle);
//                openDialogFragment(postMemberWebViewFragment);
                memberSiteFragmentContainer.setVisibility(View.VISIBLE);
//                AnimationUtil.slideToTop(memberSiteFragmentContainer);

            }
        });

        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(MemberAuthFragment.TAG.equalsIgnoreCase(tabId)){
                    tvMenuTitle.setText(R.string.title_area);
                    tvMenuSubTitle.setText(R.string.title_coupon_area);
                }else if(CouponListFragment.TAG.equalsIgnoreCase(tabId)){
                    tvMenuTitle.setText(R.string.title_popular_coupon);
                    tvMenuSubTitle.setText(R.string.title_coupon_list);
                }else{
                    tvMenuTitle.setText(R.string.title_area_coupon);
                    tvMenuSubTitle.setText(R.string.title_coupon_list_area);
                }
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

    //handle message from PushVisor
    public void pushProcess() {
        this.appVisorPush = AppVisorPush.sharedInstance();
        SaveLogin saveLogin = SaveLogin.getInstance(this);

        this.appVisorPush.setAppInfor(getApplicationContext(), Constant.APPVISOR_ID);

        // プッシュ通知の関連設定(GCM_SENDER_ID、アイコン、ステータスバーアイコン、プッシュ通知で起動するクラス名、タイトル)
        this.appVisorPush.startPush(Constant.GCM_SENDER_ID, R.mipmap.ic_launcher, R.mipmap.ic_launcher, PushvisorHandlerActivity.class, getString(R.string.app_name));
        // プッシュ通知の反応率を測定(必須)
        this.appVisorPush.trackPushWithActivity(this);
        // BRANDID  of userPropertyGroup 1 （UserPropertyGroup1〜UserPropertyGroup5）
        this.appVisorPush.setUserPropertyWithGroup(saveLogin.getUserName(),AppVisorPush.UserPropertyGroup1);
        appVisorPush.synchronizeUserProperties();

        String mDevice_Token_Pushnotification = this.appVisorPush.getDeviceID();
        AppLog.log("###################################");
        AppLog.log("####### [ Appvisor uuid ]=", mDevice_Token_Pushnotification);
        AppLog.log("###################################");
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //process for PushIntent owner
        setIntent(intent);
    }
    public void showFragmentMapSite(){
        mapSiteFragmentContainer.setVisibility(View.VISIBLE);
    }
}
