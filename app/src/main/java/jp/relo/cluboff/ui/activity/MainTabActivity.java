package jp.relo.cluboff.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import biz.appvisor.push.android.sdk.AppVisorPush;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.adapter.HistoryPushAdapter;
import jp.relo.cluboff.adapter.MenuListAdapter;
import jp.relo.cluboff.adapter.ViewPagerAdapter;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.MessageEvent;
import jp.relo.cluboff.model.ReloadEvent;
import jp.relo.cluboff.model.SaveLogin;
import jp.relo.cluboff.services.MyAppVisorPushIntentService;
import jp.relo.cluboff.ui.BaseActivityToolbar;
import jp.relo.cluboff.ui.fragment.CouponListContainerFragment;
import jp.relo.cluboff.ui.fragment.HistoryPushDialogFragment;
import jp.relo.cluboff.ui.fragment.HowToDialogFragment;
import jp.relo.cluboff.ui.fragment.PostAreaWebViewFragment;
import jp.relo.cluboff.ui.fragment.PostMemberWebViewFragment;
import jp.relo.cluboff.ui.fragment.FAQDialogFragment;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;

public class MainTabActivity extends BaseActivityToolbar {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    MenuListAdapter mMenuAdapter;
    String[] titleMenu;
    DrawerLayout mDrawerLayoutMenu;
    ListView mDrawerListMenu;
    //main AppVisor processor
    private AppVisorPush appVisorPush;
    private HistoryPushDialogFragment historyPushDialogFragment;
    private FAQDialogFragment faqDialogFragment;
    long countPush=0;

    Handler handler;
    public static final int INDEX_AREA=0;
    public static final int INDEX_TOP=1;
    public static final int INDEX_MEMBER=2;
    public static final int UPDATE_COUNT=4;
    public static final int EMPTY_W_PUSH=5;
    int indexTab = 0;
    MyDatabaseHelper myDatabaseHelper;
    long lateResume;

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
        if(event.getMessage().equals(MyAppVisorPushIntentService.class.getSimpleName())||
                event.getMessage().equals(HistoryPushDialogFragment.class.getSimpleName())){
            loadCountPush();
        }else if(Constant.TOP_COUPON.equals(event.getMessage())){
            selectPage(INDEX_TOP);
        }
        else{
            loadCountPush();
            openHistoryPush();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDatabaseHelper = new MyDatabaseHelper(this);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.what== UPDATE_COUNT){
                    if(countPush==0){
                        tvCount.setVisibility(View.GONE);
                    }else{
                        tvCount.setVisibility(View.VISIBLE);
                        tvCount.setText(""+countPush);
                    }
                }

            }
        };
        // Generate title
        pushProcess();
    }

    @Override
    protected void onResume() {
        super.onResume();
        long valueTime = Utils.dateTimeValue();
        lateResume = LoginSharedPreference.getInstance(this).getValueStop();

        loadCountPush();
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            String target = bundle.getString(Constant.TARGET_PUSH);
            if(Constant.TARGET_PUSH_SCREEN_AREA.equalsIgnoreCase(target)){
                selectPage(INDEX_AREA);
            }else if(Constant.TARGET_PUSH_SCREEN_SITE.equalsIgnoreCase(target)){
                selectPage(INDEX_MEMBER);
            }else if(Constant.TARGET_PUSH_SCREEN_LIST.equalsIgnoreCase(target)){
                selectPage(INDEX_TOP);
                openHistoryPush();
            }else{
                selectPage(INDEX_TOP);
            }
        }else{
            if(indexTab < -1 || indexTab > 1){
                indexTab = 0;
            }
            if(valueTime - lateResume > Constant.LIMIT_ON_BACKGROUND){
                indexTab = 0;
                EventBus.getDefault().post(new ReloadEvent(true));

            }
            selectPage(indexTab+1);
        }
    }
    void selectPage(int pageIndex){
        if(tabLayout!=null&&viewPager!=null){
            tabLayout.setScrollPosition(pageIndex,0f,true);
            viewPager.setCurrentItem(pageIndex);
        }
    }


    public void loadCountPush(){
        countPush = myDatabaseHelper.countPushUnread();
        handler.sendEmptyMessage(UPDATE_COUNT);
    }
    public void openHistoryPush(){
        if(historyPushDialogFragment==null){
            historyPushDialogFragment = new HistoryPushDialogFragment();
        }
        historyPushDialogFragment.setICallDetailCoupon(new HistoryPushAdapter.iCallDetailCoupon() {
            @Override
            public void callbackDetail(String actionPush, int tabIndex) {
                ReloApp reloApp = (ReloApp) getApplication();
                reloApp.trackingWithAnalyticGoogleServices(Constant.GA_HISTORYPUSH_CATEGORY,Constant.GA_HISTORYPUSH_ACTION,actionPush, Utils.convertLong(Constant.GA_HISTORYPUSH_VALUE));
                selectPage(tabIndex);
            }
        });
        openDialogFragment(historyPushDialogFragment);
    }

    @Override
    public void setupToolbar() {
        lnToolbar.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        imvMenu.setVisibility(View.VISIBLE);
        imvInfo.setVisibility(View.VISIBLE);
        tvCount.setVisibility(View.VISIBLE);


        rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayoutMenu.isDrawerOpen(mDrawerListMenu)) {
                    mDrawerLayoutMenu.closeDrawer(mDrawerListMenu);
                } else {
                    mDrawerLayoutMenu.openDrawer(mDrawerListMenu);
                }
            }
        });
        flInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistoryPush();
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
        titleMenu = new String[] { getString(R.string.menu_top), getString(R.string.menu_tutorial), getString(R.string.menu_FAQ)};
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
        switch (position) {
            case 1:
                selectPage(INDEX_TOP);
                break;
            case 2:
                openDialogFragment(new HowToDialogFragment());
                break;
            case 3:
                if(faqDialogFragment==null){
                    faqDialogFragment = new FAQDialogFragment();
                }
                openDialogFragment(faqDialogFragment);
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

        PostAreaWebViewFragment couponAreaFragment = new PostAreaWebViewFragment();
        couponAreaFragment.setArguments(createBundleFragment(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_AREA_COUPON, Constant.AREA_COUPON));
        adapter.addFragment(couponAreaFragment, getString(R.string.title_coupon_area));

        adapter.addFragment(new CouponListContainerFragment(), getString(R.string.title_coupon_list));

        PostMemberWebViewFragment membershipFragment = new PostMemberWebViewFragment();
        membershipFragment.setArguments(createBundleFragment(Constant.KEY_LOGIN_URL, "", Constant.MEMBER_COUPON));
        adapter.addFragment(membershipFragment, getString(R.string.title_membership));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indexTab = position - 1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(adapter);
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
        this.appVisorPush.setUserPropertyWithGroup(saveLogin.getBrandid(),AppVisorPush.UserPropertyGroup1);
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
}
