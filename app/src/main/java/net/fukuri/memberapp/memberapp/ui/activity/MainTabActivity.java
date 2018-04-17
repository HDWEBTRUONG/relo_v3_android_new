package net.fukuri.memberapp.memberapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;

import biz.appvisor.push.android.sdk.AppVisorPush;
import framework.phvtCommon.FragmentTransitionInfo;
import framework.phvtUtils.AppLog;

import net.fukuri.memberapp.memberapp.BuildConfig;
import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.ReloApp;
import net.fukuri.memberapp.memberapp.adapter.MenuListAdapter;
import net.fukuri.memberapp.memberapp.api.ApiClientJP;
import net.fukuri.memberapp.memberapp.api.ApiInterface;
import net.fukuri.memberapp.memberapp.model.BlockEvent;
import net.fukuri.memberapp.memberapp.model.ForceupdateApp;
import net.fukuri.memberapp.memberapp.model.MessageEvent;
import net.fukuri.memberapp.memberapp.model.OpenMemberSiteEvent;
import net.fukuri.memberapp.memberapp.model.ReloadEvent;
import net.fukuri.memberapp.memberapp.ui.BaseActivityToolbar;
import net.fukuri.memberapp.memberapp.ui.fragment.CouponListAreaFragment;
import net.fukuri.memberapp.memberapp.ui.fragment.CouponListFragment;
import net.fukuri.memberapp.memberapp.ui.fragment.MemberAuthFragment;
import net.fukuri.memberapp.memberapp.ui.fragment.PostAreaWebViewFragment2;
import net.fukuri.memberapp.memberapp.ui.fragment.PostMemberFragment;
import net.fukuri.memberapp.memberapp.ui.fragment.WebViewDialogFragment;
import net.fukuri.memberapp.memberapp.util.Constant;
import net.fukuri.memberapp.memberapp.util.LoginSharedPreference;
import net.fukuri.memberapp.memberapp.util.Utils;
import net.fukuri.memberapp.memberapp.util.ase.EvenBusLoadWebMembersite;
import net.fukuri.memberapp.memberapp.util.ase.EventBusPermission;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public static final int INDEX_AREA = 0;
    public static final int INDEX_TOP = 1;
    public static final int INDEX_MEMBER = 2;
    long lateResume;
    FragmentTabHost mTabHost;
    View llMember;
    View llTab;
    View llMain;
    View svError;

    String userID = "";
    String pass = "";
    TextView tvPhone;

    Handler handler;
    public static final int UPDATE_LAYOUT_ERROR = 2;

    protected static boolean isVisible = false;

    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.CALL_PHONE};


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        setIsVisible(false);
        LoginSharedPreference.getInstance(this).setValueStop(Utils.dateTimeValue());
        EventBus.getDefault().unregister(this);
        handler.removeCallbacksAndMessages(null);
    }

    @Subscribe
    public void onEvent(MessageEvent event) {
        if (Constant.TOP_COUPON.equals(event.getMessage())) {
            selectPage(INDEX_TOP);
        }
    }

    @Subscribe
    public void onEvent(OpenMemberSiteEvent event) {
        if(getSupportFragmentManager().findFragmentByTag(PostMemberFragment.class.getSimpleName())==null){
            replaceFragment(PostMemberFragment.newInstance(Constant.KEY_LOGIN_URL, "", Constant.MEMBER_COUPON), R.id.container_member_fragment, PostMemberFragment.class.getSimpleName(), new FragmentTransitionInfo());
        }
        //replaceFragment(PostAreaWebViewFragment2.newInstance(), R.id.container_map_fragment, "MAP_AREA_FRAGMENT", new FragmentTransitionInfo());
    }

    @Subscribe
    public void onEvent(BlockEvent event) {
        ivMenuRight.setEnabled(false);
        llTab.setVisibility(View.GONE);

    }

    public static boolean isIsVisible() {
        return isVisible;
    }

    public static void setIsVisible(boolean isVisible) {
        MainTabActivity.isVisible = isVisible;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pushProcess();
        setupView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_LAYOUT_ERROR:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //svError.setVisibility(View.VISIBLE);
                                    EventBus.getDefault().post(new BlockEvent());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        break;
                }
                return false;
            }
        });
        LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(this);

        if (loginSharedPreference != null) {
            userID = loginSharedPreference.getUserName();
            pass = loginSharedPreference.getPass();
        }
        /*if (ReloApp.isBlockAuth()) {
            handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
        } else {
            checkAuthMember(userID, pass);
        }*/

        setIsVisible(true);
        long valueTime = Utils.dateTimeValue();
        lateResume = LoginSharedPreference.getInstance(this).getValueStop();


        if (mTabHost.getCurrentTab() == 0) {
            tvMenuTitle.setText(R.string.title_area);
            tvMenuSubTitle.setText(R.string.title_coupon_area);
        } else if (mTabHost.getCurrentTab() == 1) {
            tvMenuTitle.setText(R.string.title_popular_coupon);
            tvMenuSubTitle.setText(R.string.title_coupon_list);
        } else {
            tvMenuTitle.setText(R.string.title_area_coupon);
            tvMenuSubTitle.setText(R.string.title_coupon_list_area);
        }
        mTabHost.setCurrentTab(1);
        //loadCountPush();
        /*Bundle bundle = this.getIntent().getExtras();
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
            AppLog.log("value time: "+(valueTime - lateResume));
            if(valueTime - lateResume > Constant.LIMIT_ON_BACKGROUND){
                EventBus.getDefault().post(new ReloadEvent(true));
            }
            if(valueTime - lateResume > Constant.LIMIT_ON_BACKGROUND_MEMBERSITE){
                EventBus.getDefault().post(new EvenBusLoadWebMembersite());
            }
        }*/
        AppLog.log("value time: " + (valueTime - lateResume));
        if (valueTime - lateResume > Constant.LIMIT_ON_BACKGROUND) {
            EventBus.getDefault().post(new ReloadEvent(true));
        }
        if ((valueTime - lateResume > Constant.LIMIT_ON_BACKGROUND_MEMBERSITE) || PostMemberFragment.isCallbackFromBrowser) {
            AppLog.log_url(" start reload member_site ............. after timer minutes OR CallBackfrom EXT Browser");
            EventBus.getDefault().post(new EvenBusLoadWebMembersite());
        }
        checkForceUpdateApp();
    }

    void selectPage(int pageIndex) {
        mTabHost.setCurrentTab(pageIndex);
    }


    @Override
    public void setupToolbar() {
        ivMenuRight.setVisibility(View.VISIBLE);
        ivMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayoutMenu != null) {
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
        svError = findViewById(R.id.svError);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        memberSiteFragmentContainer = (FrameLayout) findViewById(R.id.container_member_fragment);
        mapSiteFragmentContainer = (FrameLayout) findViewById(R.id.container_map_fragment);

        // Locate ListView in drawer_main.xml
        mDrawerListMenu = (ListView) findViewById(R.id.left_drawer);

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber();
            }
        });
    }

    public void callPhoneNumber() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvPhone.getText().toString()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermission();
            return;
        }
        startActivity(intent);
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
                if(getSupportFragmentManager().findFragmentByTag(PostMemberFragment.class.getSimpleName())==null){
                    replaceFragment(PostMemberFragment.newInstance(Constant.KEY_LOGIN_URL, "", Constant.MEMBER_COUPON), R.id.container_member_fragment, PostMemberFragment.class.getSimpleName(), new FragmentTransitionInfo());
                }
                EventBus.getDefault().postSticky(new EventBusPermission());
                  memberSiteFragmentContainer.setVisibility(View.VISIBLE);
                ((ReloApp)getApplication()).trackingAnalytics(Constant.GA_MEMBER_SCREEN);
//                AnimationUtil.slideToTop(memberSiteFragmentContainer);
               // updateAuth();
            }
        });

        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                hideLoading();
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

    public void updateAuth(){
        showLoading();
        String  userID = "";
        String  pass = "";
        ApiInterface apiInterface = ApiClientJP.getClient().create(ApiInterface.class);
        LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(this);
        userID = loginSharedPreference.getUserName();
        pass = loginSharedPreference.getPass();
        apiInterface.memberAuthHTML(userID, pass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();
                try {
                    Document document = Jsoup.parse(response.body().string());
                    Element divChildren = document.select("html").first();
                    for (int i = 0; i < divChildren.childNodes().size(); i++) {
                        Node child = divChildren.childNode(i);
                        if (child.nodeName().equals("#comment")) {
                            String valueAuth = child.toString();
                            int valueHandleLogin = BuildConfig.DEBUG? 0:1;
                            if(Utils.parserInt(valueAuth.substring(valueAuth.indexOf("<STS>")+5,valueAuth.indexOf("</STS>")))==valueHandleLogin){
                                LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(MainTabActivity.this);
                                loginSharedPreference.setKEY_APPU(valueAuth.substring(valueAuth.indexOf("<APPU>")+6,valueAuth.indexOf("</APPU>")));
                                loginSharedPreference.setKEY_APPP(valueAuth.substring(valueAuth.indexOf("<APPP>")+6,valueAuth.indexOf("</APPP>")));
                            }
                        }
                    }

                    openMenberSiteBrowser();

                } catch (IOException e) {
                    e.printStackTrace();
                    openMenberSiteBrowser();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppLog.log("Err: "+t.toString());
                hideLoading();
                openMenberSiteBrowser();
            }
        });
    }

    public void openMenberSiteBrowser(){
        final LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(this);
        if(loginSharedPreference!=null){
            try {
                Intent internetIntent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(Constant.URL_MEMBER_BROWSER)
                        .buildUpon()
                        .appendQueryParameter("APPU", loginSharedPreference.getKEY_APPU())
                        .appendQueryParameter("APPP", loginSharedPreference.getKEY_APPP())
                        .build();
                internetIntent.setData(uri);
                startActivity(internetIntent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

        this.appVisorPush.setAppInfor(getApplicationContext(), Constant.APPVISOR_ID);

        // プッシュ通知の反応率を測定(必須)
        this.appVisorPush.trackPushWithActivity(this);

        // プッシュ通知の関連設定(GCM_SENDER_ID、アイコン、ステータスバーアイコン、プッシュ通知で起動するクラス名、タイトル)
        this.appVisorPush.startPush(Constant.GCM_SENDER_ID, R.mipmap.ic_launcher, R.mipmap.ic_launcher, PushvisorHandlerActivity.class, getString(R.string.app_name));

        // BRANDID  of userPropertyGroup 1 （UserPropertyGroup1〜UserPropertyGroup5）
        try{
            this.appVisorPush.setUserPropertyWithGroup(LoginSharedPreference.getInstance(this).getUserName(),AppVisorPush.UserPropertyGroup1);
            appVisorPush.synchronizeUserProperties();
        }catch (Exception ex){
            Log.e("BSV", ex.toString());
        }

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
    public void resetCurrentTab(){
        mTabHost.setCurrentTab(0);
    }

    private void checkForceUpdateApp(){
        apiInterface.checkForceupdateApp(Constant.FORCEUPDATE_APP).enqueue(new Callback<ForceupdateApp>() {
            @Override
            public void onResponse(Call<ForceupdateApp> call, Response<ForceupdateApp> response) {
                if(response.isSuccessful()){
                    if(Utils.convertIntVersion(response.body().getAndroid().getVersion())> Utils.convertIntVersion((BuildConfig.VERSION_NAME))){
                        Utils.showDialogLIBForceUpdate(MainTabActivity.this, response.body().getUp_comment());
                    }
                }
            }

            @Override
            public void onFailure(Call<ForceupdateApp> call, Throwable t) {
                AppLog.log(""+t.toString());
            }
        });
    }

    private void checkAuthMember(String userID, String pass){
        ApiInterface apiInterface = ApiClientJP.getClient().create(ApiInterface.class);
        apiInterface.memberAuthHTML(userID, pass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();
                try {
                    if(response!=null && response.body()!=null){
                        Document document = Jsoup.parse(response.body().string());
                        if(!Utils.isAuthSuccess(MainTabActivity.this,document)){
                            handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    //handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                //handler.sendEmptyMessage(UPDATE_LAYOUT_ERROR);
            }
        });
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean callPhone = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (callPhone) {
                        Toast.makeText(this, R.string.premission_accepted, Toast.LENGTH_SHORT).show();

                    }/*else{
                        Toast.makeText(getActivity(), R.string.premissionaccepted_no_accepted, Toast.LENGTH_SHORT).show();

                    }*/
                } /*else {
                    Toast.makeText(getActivity(), R.string.premission_error, Toast.LENGTH_SHORT).show();
                }*/
                callPhoneNumber();
                break;

        }
    }
}
