package net.fukuri.memberapp.memberapp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import framework.phvtFragment.BaseFragment;
import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.ReloApp;
import net.fukuri.memberapp.memberapp.adapter.CouponListAdapter;
import net.fukuri.memberapp.memberapp.api.MyCallBack;
import net.fukuri.memberapp.memberapp.database.ConstansDB;
import net.fukuri.memberapp.memberapp.database.MyDatabaseHelper;
import net.fukuri.memberapp.memberapp.model.CatagoryDTO;
import net.fukuri.memberapp.memberapp.model.CouponDTO;
import net.fukuri.memberapp.memberapp.model.VersionReponse;
import net.fukuri.memberapp.memberapp.model.XMLUpdate;
import net.fukuri.memberapp.memberapp.ui.activity.MainTabActivity;
import net.fukuri.memberapp.memberapp.util.ConstanArea;
import net.fukuri.memberapp.memberapp.util.Constant;
import net.fukuri.memberapp.memberapp.util.LoginSharedPreference;
import net.fukuri.memberapp.memberapp.util.Utils;
import net.fukuri.memberapp.memberapp.views.MyMaterialSpinner;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponListFragment extends BaseFragment implements View.OnClickListener,CouponListAdapter.iClickButton{

    LinearLayout lnCatalory;
    ListView lvCoupon;
    TextView tvCategory;
    View svMenu;
    View viewLoading;
    protected MyMaterialSpinner spinner;
    MyDatabaseHelper myDatabaseHelper;
    CouponListAdapter adapter;
    ArrayList<CouponDTO> listCoupon=new ArrayList<>();
    public static String WILL_NET_SERVER="1";
    public Handler mHandler;

    public static final int MSG_LOAD_CATEGORY = 0;
    public static final int MSG_LOAD_DATA = 1;
    public static final int MSG_CREATE_ADAPTER = 2;
    public static final int MSG_UPDATE_ADAPTER = 3;
    public static final int MSG_SET_CATEGORY = 4;
    public static final int MSG_CHECK_UPDATE = 5;
    ArrayList<CatagoryDTO> categoryList = new ArrayList<>();
    int positionView = 0;
    int countDownloaded =0;

    private boolean isArea;
    public static final String TAG = CouponListFragment.class.getSimpleName();

    public String areaName= ConstanArea.WHOLEJAPAN;
    public String categoryID= "";
    public boolean isSelected;

    public List<XMLUpdate> xmlUpdatesList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDatabaseHelper= MyDatabaseHelper.getInstance(getActivity());
    }

    private void init(View view) {
        svMenu = view.findViewById(R.id.svMenu);
        lnCatalory = (LinearLayout) view.findViewById(R.id.lnCatalory);
        lvCoupon = (ListView) view.findViewById(R.id.list_category_listview);
        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        spinner = (MyMaterialSpinner) view.findViewById(R.id.spinnerCategory);
        viewLoading = view.findViewById(R.id.viewLoading);
    }

    private void loadCategory() {
        if(MainTabActivity.isIsVisible()){
            categoryList.clear();
            categoryList.addAll(myDatabaseHelper.getCatagorys(areaName));
            categoryList.add(0,new CatagoryDTO(ConstansDB.COUPON_ALL,"すべて"));
            mHandler.sendEmptyMessage(CouponListFragment.MSG_SET_CATEGORY);
            mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_DATA);
        }


    }
    private void setCategory(){
        setEventCategory();
        spinner.setItems(categoryList);
        if(categoryList!=null && categoryList.size()>0){
            if(spinner.getSelectedIndex()>0){
                tvCategory.setText(categoryList.get(spinner.getSelectedIndex()).getGetCatagoryName());
            }else{
                spinner.setSelectedIndex(0);
                categoryID = categoryList.get(spinner.getSelectedIndex()).getCatagoryID();
                if(isSelected){
                    tvCategory.setText(categoryList.get(spinner.getSelectedIndex()).getGetCatagoryName());
                }else{
                    if(isAdded())
                    tvCategory.setText(getString(R.string.catalogy_selecte));
                }
            }
        }
        lnCatalory.setOnClickListener(this);
    }

    protected void setEventCategory(){
        spinner.setOnItemSelectedListener(new MyMaterialSpinner.OnItemSelectedListener<CatagoryDTO>() {

            @Override
            public void onItemSelected(MyMaterialSpinner view, int position, long id, CatagoryDTO item) {
                positionView = 0;
                areaName = ConstanArea.WHOLEJAPAN;
                categoryID = item.getCatagoryID();
                isSelected =true;
                getListDataCategoryID(categoryID);
                tvCategory.setText(item.getGetCatagoryName());
            }
        });
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_coupon_list;
    }
    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        init(root);

    }

    @Override
    public void onPause() {
        super.onPause();
        AppLog.log("-------onPause");

    }

    @Override
    public void onStop() {
        AppLog.log("-------onStop");
        mainTabActivity.hideLoading();
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        isArea = bundle.getBoolean(Constant.DATA_COUPON_URL);
        if(!isArea){
            ((ReloApp)getActivity().getApplication()).trackingAnalytics(Constant.GA_LIST_COUPON_SCREEN);
            areaName = ConstanArea.WHOLEJAPAN;
            //check update xml data
            if(!LoginSharedPreference.getInstance(getActivity()).checkDownloadDone()){
                myDatabaseHelper.clearData();
            }
            mainTabActivity.showLoading();
            addSubscription(apiInterfaceForceUpdate.checkVersion(),new MyCallBack<VersionReponse>(){
                        @Override
                        public void onSuccess(VersionReponse model) {
                            boolean isUpdate = Utils.convertIntVersion(model.getVersion())> LoginSharedPreference.getInstance(getActivity()).getVersion();
                            if(isUpdate){
                                ReloApp.setVersionApp(Utils.convertIntVersion(model.getVersion()));
                                ReloApp.setIsUpdateData(isUpdate);
                            }
                        }

                        @Override
                        public void onFailure(int msg) {
                            AppLog.log("Error: "+msg);
                        }

                        @Override
                        public void onFinish() {
                            mainTabActivity.hideLoading();
                            if(ReloApp.isUpdateData()){
                                mHandler.sendEmptyMessage(CouponListFragment.MSG_CHECK_UPDATE);
                            }else{
                                if(categoryList==null || categoryList.isEmpty()){
                                    mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_CATEGORY);
                                }else{
                                    mHandler.sendEmptyMessage(CouponListFragment.MSG_SET_CATEGORY);
                                }
                                mHandler.sendEmptyMessage(CouponListFragment.MSG_UPDATE_ADAPTER);
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLog.log("-------onViewCreated");
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_UP){
                    getActivity().finish();
                    return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.log("-------onResume");
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case CouponListFragment.MSG_LOAD_CATEGORY:
                        loadCategory();
                        break;
                    case CouponListFragment.MSG_LOAD_DATA:
                        if(StringUtil.isEmpty(categoryID)){
                            getListDataCategoryID(ConstansDB.COUPON_ALL);
                        }else{
                            getListDataCategoryID(categoryID);
                        }
                        break;
                    case CouponListFragment.MSG_CREATE_ADAPTER:
                        setAdapter();
                        break;
                    case CouponListFragment.MSG_UPDATE_ADAPTER:
                        setAdapter();
                        break;
                    case CouponListFragment.MSG_SET_CATEGORY:
                        if(MainTabActivity.isIsVisible()){
                        setCategory();
                    }
                        break;
                    case CouponListFragment.MSG_CHECK_UPDATE:
                        updateData();
                        break;
                }
            }
        };

        if(LoginSharedPreference.getInstance(getActivity()).checkDownloadDone() && (categoryList==null || categoryList.isEmpty())){
            mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_CATEGORY);
        }
    }

    protected void getListDataCategoryID(String categoryID) {
        mainTabActivity.showLoading();
        listCoupon.clear();
        listCoupon.addAll(myDatabaseHelper.getCouponWithDateCategoryIDs(categoryID, areaName));
        mHandler.sendEmptyMessage(CouponListFragment.MSG_CREATE_ADAPTER);
        mainTabActivity.hideLoading();

    }
    private void setAdapter(){
        if(adapter==null){
            adapter =new CouponListAdapter(getContext(), listCoupon,this);
            lvCoupon.setAdapter(adapter);
        }else{
            adapter.setDataChange(listCoupon);
            lvCoupon.setAdapter(adapter);
        }
    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.lnCatalory){
            spinner.expand();
        }
    }
    @Override
    public void callback(CouponDTO data) {
        String url="";
        String kaiinno = "";
        String requestno = "";
        LoginSharedPreference saveLogin = LoginSharedPreference.getInstance(getActivity());
        Bundle bundle = new Bundle();
        if(saveLogin!=null){
            kaiinno = saveLogin.getUserName();
            requestno = data.getShgrid();

            //version 2
            if(WILL_NET_SERVER.equals(data.getCoupon_type())){
                url =data.getLink_path();
                bundle.putString(Constant.KEY_LOGIN_URL, url);
                bundle.putString(Constant.TAG_KAIINNO, kaiinno);
                bundle.putString(Constant.KEY_URL_TYPE, data.getCoupon_type());
                bundle.putString(Constant.TAG_SENICODE, "1");
                bundle.putString(Constant.TAG_SHGRID, requestno);

                WebViewDetailCouponFragment webViewFragment = new WebViewDetailCouponFragment();
                webViewFragment.setArguments(bundle);
                if(getActivity() instanceof MainTabActivity){
                    ((MainTabActivity) getActivity()).openDialogFragment(webViewFragment);
                }
            }else{
                //type 0
                if(getActivity() instanceof MainTabActivity){
                    DetailCouponOfflineDialogFragment detailCouponOfflineDialogFragment = DetailCouponOfflineDialogFragment.newInstance(requestno,areaName);
                    ((MainTabActivity) getActivity()).openDialogFragment(detailCouponOfflineDialogFragment);
                }

            }

        }
    }

    @Override
    public void like(String id, int isLiked) {
        int newLikeValue = isLiked == 0?1:0;
        myDatabaseHelper.likeCoupon(id,newLikeValue);
        for(int i =0; i < listCoupon.size();i++){
            if(listCoupon.get(i).getShgrid().equals(id)){
                listCoupon.get(i).setLiked(newLikeValue);
                break;
            }
        }
        setGoogleAnalyticDetailCoupon(Constant.GA_LIKE_CATEGORY,Constant.GA_ACTION_LIKE,id,Constant.GA_VALUE_LIKE);
        mHandler.sendEmptyMessage(CouponListFragment.MSG_UPDATE_ADAPTER);
    }

    @Override
    public void positionClick(int position) {
        this.positionView = position;
    }

    private void updateData(){
        if(ReloApp.isUpdateData()) {
            mainTabActivity.showLoading();
            LoginSharedPreference.getInstance(getActivity()).setDownloadDone(false);
            countDownloaded =0;
            myDatabaseHelper.clearData();
            listCoupon.clear();
            xmlUpdatesList = new ArrayList<>();
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_WHOLEJAPAN, ConstanArea.WHOLEJAPAN));
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_HOKKAIDO, ConstanArea.HOKKAIDO));
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_TOHOKU, ConstanArea.TOHOKU));
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_KANTO, ConstanArea.KANTO));
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_KOUSHINETSU, ConstanArea.KOUSHINETSU));
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_HOKURIKUTOKAI, ConstanArea.HOKURIKUTOKAI));
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_KINKI, ConstanArea.KINKI));
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_CYUUGOKUSHIKOKU, ConstanArea.CYUUGOKUSHIKOKU));
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_KYUSHU, ConstanArea.KYUSHU));
            xmlUpdatesList.add(new XMLUpdate(Constant.XML_OKINAWA, ConstanArea.OKINAWA));
            Observable.from(xmlUpdatesList)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<XMLUpdate>() {
                @Override
                public void onCompleted() {
                    if(countDownloaded ==xmlUpdatesList.size()){
                        mainTabActivity.hideLoading();
                        LoginSharedPreference.getInstance(getActivity()).setVersion(ReloApp.getVersionApp());
                        ReloApp.setIsUpdateData(false);
                        LoginSharedPreference.getInstance(getActivity()).setDownloadDone(true);
                        if(MainTabActivity.isIsVisible()){
                            mHandler.sendEmptyMessage(MSG_LOAD_CATEGORY);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    LoginSharedPreference.getInstance(getActivity()).setDownloadDone(false);
                }

                @Override
                public void onNext(XMLUpdate xmlUpdates) {
                    //new CouponListFragment.UpdateTask().execute(xmlUpdates.getUrl(), xmlUpdates.getArea());
                    loadDataXML(xmlUpdates.getUrl(), xmlUpdates.getArea());
                }
            });
        }else{
            mHandler.sendEmptyMessage(MSG_LOAD_CATEGORY);
        }
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
    public void saveData(ArrayList<CouponDTO> datas, String area){
        if(datas!=null){
            myDatabaseHelper.saveCouponList(datas,area);
            countDownloaded++;
        }
    }

    public void loadDataXML(String url, String area){
        ArrayList<CouponDTO> listResult = new ArrayList<>();
        CouponDTO item= new CouponDTO();
        boolean isOpened= false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            if ( factory == null){
                LoginSharedPreference.getInstance(getActivity()).setVersion(0);
                mHandler.sendEmptyMessage(MSG_LOAD_CATEGORY);
                return;
            }

            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            // We will get the XML from an input stream
            xpp.setInput(getInputStream(new URL(url)), "UTF_8");

            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                String name = "";
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = xpp.getName().toUpperCase();
                        switch (name){
                            case CouponDTO.TAG_COUPON:
                                listResult = new ArrayList<>();
                                break;
                            case CouponDTO.TAG_ITEM:
                                isOpened = true;
                                item = new CouponDTO();
                                break;
                            case CouponDTO.TAG_SHGRID:
                                item.setShgrid(xpp.nextText());
                                break;
                            case CouponDTO.TAG_CATEGORY_ID:
                                item.setCategory_id(xpp.nextText());
                                break;
                            case CouponDTO.TAG_CATEGORY_NAME:
                                item.setCategory_name(xpp.nextText());
                                break;
                            case CouponDTO.TAG_COUPON_NAME:
                                item.setCoupon_name(xpp.nextText());
                                break;
                            case CouponDTO.TAG_COUPON_NAME_EN:
                                item.setCoupon_name_en(xpp.nextText());
                                break;
                            case CouponDTO.TAG_COUPON_IMAGE_PATH:
                                item.setCoupon_image_path(xpp.nextText());
                                break;
                            case CouponDTO.TAG_COUPON_TYPE:
                                item.setCoupon_type(xpp.nextText());
                                break;
                            case CouponDTO.TAG_LINK_PATH:
                                item.setLink_path(xpp.nextText());
                                break;
                            case CouponDTO.TAG_EXPIRATION_FROM:
                                String value = xpp.nextText();
                                if(value!=null && value.length()>8){
                                    value = value.substring(0,8);
                                }
                                item.setExpiration_from(value);
                                break;
                            case CouponDTO.TAG_EXPIRATION_TO:
                                String valueTo = xpp.nextText();
                                if(valueTo!=null && valueTo.length()>8){
                                    valueTo = valueTo.substring(0,8);
                                }
                                item.setExpiration_to(valueTo);
                                break;
                            case CouponDTO.TAG_PRIORITY:
                                item.setPriority(Utils.parserInt(xpp.nextText()));
                                break;
                            case CouponDTO.TAG_MEMO:
                                item.setMemo(xpp.nextText());
                                break;
                            case CouponDTO.TAG_BENEFIT:
                                item.setBenefit(xpp.nextText());
                                break;
                            case CouponDTO.TAG_BENEFIT_NOTES:
                                item.setBenefit_notes(xpp.nextText());
                                break;
                        }
                    case XmlPullParser.END_TAG:
                        name = xpp.getName().toUpperCase();
                        if (name.equalsIgnoreCase(CouponDTO.TAG_ITEM) && item != null && isOpened){
                            listResult.add(item);
                            isOpened = false;
                        }
                }
                eventType = xpp.next();
            }
        } catch (Exception e){

        }
        saveData(listResult,area);
    }
}