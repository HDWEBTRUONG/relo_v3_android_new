package jp.relo.cluboff.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import framework.phvtFragment.BaseFragment;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.adapter.CouponListAdapter;
import jp.relo.cluboff.api.MyCallBack;
import jp.relo.cluboff.database.ConstansDB;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.model.Info;
import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.model.LoginRequest;
import jp.relo.cluboff.model.VersionReponse;
import jp.relo.cluboff.ui.activity.LoginActivity;
import jp.relo.cluboff.util.ConstansSharedPerence;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;
import jp.relo.cluboff.views.MyMaterialSpinner;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponListFragment extends BaseFragment implements View.OnClickListener,CouponListAdapter.iClickButton{

    LinearLayout lnCatalory;
    ListView lvCategoryMenu;
    TextView tvCategory;
    MyMaterialSpinner spinner;
    MyDatabaseHelper myDatabaseHelper;
    CouponListAdapter adapter;
    ArrayList<CouponDTO> listCoupon=new ArrayList<>();
    public static String WILL_NET_SERVER="1";
    public switchFragment iSwitchFragment;
    private Handler mHandler;

    public static final int MSG_LOAD_CATEGORY = 0;
    public static final int MSG_LOAD_DATA = 1;
    public static final int MSG_CREATE_ADAPTER = 2;
    public static final int MSG_UPDATE_ADAPTER = 3;
    public static final int MSG_SET_CATEGORY = 4;
    public static final int MSG_CHECK_UPDATE = 5;
    ArrayList<CatagoryDTO> categoryList = new ArrayList<>();
    ArrayList<CouponDTO> listResult = new ArrayList<>();

    MyDatabaseHelper sqLiteOpenHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteOpenHelper = new MyDatabaseHelper(getActivity());
    }
    public void setiSwitchFragment(switchFragment iSwitchFragment){
      this.iSwitchFragment = iSwitchFragment;
    }

    private void init(View view) {
        lnCatalory = (LinearLayout) view.findViewById(R.id.lnCatalory);
        lvCategoryMenu = (ListView) view.findViewById(R.id.list_category_listview);
        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        spinner = (MyMaterialSpinner) view.findViewById(R.id.spinnerCategory);
    }

    private void loadCategory() {
        myDatabaseHelper.getCatagorysRX(ConstansDB.COUPON_ALL).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CatagoryDTO>>() {
                    @Override
                    public void call(List<CatagoryDTO> catagoryDTOs) {
                        categoryList.clear();
                        categoryList.addAll(catagoryDTOs);
                        mHandler.sendEmptyMessage(CouponListFragment.MSG_SET_CATEGORY);
                        mHandler.sendEmptyMessage(CouponListFragment.MSG_LOAD_DATA);
                    }
                });


    }
    public void setCategory(){
        categoryList.add(0,new CatagoryDTO(ConstansDB.COUPON_ALL,getString(R.string.catalogy)));
        categoryList.add(1,new CatagoryDTO(ConstansDB.COUPON_FAV,getString(R.string.catalogy_fav)));
        spinner.setItems(categoryList);
        spinner.setOnItemSelectedListener(new MyMaterialSpinner.OnItemSelectedListener<CatagoryDTO>() {

            @Override
            public void onItemSelected(MyMaterialSpinner view, int position, long id, CatagoryDTO item) {
                getListDataCategoryID(item.getCatagoryID());
                tvCategory.setText(item.getGetCatagoryName());
            }
        });
        lnCatalory.setOnClickListener(this);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myDatabaseHelper=new MyDatabaseHelper(getActivity());
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case CouponListFragment.MSG_LOAD_CATEGORY:
                        loadCategory();
                        break;
                    case CouponListFragment.MSG_LOAD_DATA:
                        getListDataCategoryID(ConstansDB.COUPON_ALL);
                        break;
                    case CouponListFragment.MSG_CREATE_ADAPTER:
                        setAdapter(true);
                        break;
                    case CouponListFragment.MSG_UPDATE_ADAPTER:
                        setAdapter(false);
                        break;
                    case CouponListFragment.MSG_SET_CATEGORY:
                        setCategory();
                        break;
                    case CouponListFragment.MSG_CHECK_UPDATE:
                        updateData();
                        break;
                }
            }
        };
        mHandler.sendEmptyMessage(CouponListFragment.MSG_CHECK_UPDATE);
    }


    private void getListDataCategoryID(String categoryID) {
        listCoupon.clear();
        myDatabaseHelper.getCouponWithDateCategoryIDRX(categoryID).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CouponDTO>>() {
                    @Override
                    public void call(List<CouponDTO> couponDTOs) {
                        listCoupon.addAll(couponDTOs);
                        mHandler.sendEmptyMessage(CouponListFragment.MSG_CREATE_ADAPTER);
                    }
                });

    }
    private void setAdapter(boolean isReload){
        if(adapter==null){
            adapter =new CouponListAdapter(getContext(), listCoupon,this);
            lvCategoryMenu.setAdapter(adapter);
        }else{
            adapter.setDataChange(listCoupon);
            if(isReload){
                lvCategoryMenu.setAdapter(adapter);
            }
        }
        hideLoading();
    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.lnCatalory){
            //clickCategoryMenu();
            spinner.expand();
        }
    }
    @Override
    public void callback(CouponDTO data) {
        String url="";
        Info info = LoginSharedPreference.getInstance(getActivity()).get(ConstansSharedPerence.TAG_LOGIN_SAVE, Info.class);
        Bundle bundle = new Bundle();
        if(data.getCoupon_type().equals(WILL_NET_SERVER)){
            url =data.getLink_path();
            String mBrndid = "";
            if(info!=null) {
                try {
                    mBrndid = BackAES.decrypt(info.getBrandid(), AESHelper.password, AESHelper.type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            bundle.putString(Constant.TAG_BRNDID, mBrndid);

        }else{
            bundle.putString(Constant.TAG_SENICODE, "1");
            url = "";
            if(info!=null){
                try {
                    url = MessageFormat.format(Constant.TEMPLATE_URL_COUPON,BackAES.decrypt(info.getUrl(), AESHelper.password, AESHelper.type));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        String userID = "";
        LoginRequest loginRequest = LoginSharedPreference.getInstance(getActivity()).get(ConstansSharedPerence.TAG_LOGIN_INPUT,LoginRequest.class);
        if(loginRequest!=null){
            userID = loginRequest.getKaiinno();
        }
        bundle.putString(Constant.TAG_USER_ID, userID);
        bundle.putString(Constant.KEY_LOGIN_URL, url);
        bundle.putString(Constant.KEY_URL_TYPE, data.getCoupon_type());

        bundle.putString(Constant.TAG_REQUESTNO, data.getShgrid());
        iSwitchFragment.callSwitchFragment(bundle);
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
        mHandler.sendEmptyMessage(CouponListFragment.MSG_UPDATE_ADAPTER);
    }

    interface switchFragment{
        void callSwitchFragment(Bundle bundle);
    }

    private void updateData(){
        showLoading(getActivity());
        addSubscription(apiInterface.checkVersion(),new MyCallBack<VersionReponse>() {
            @Override
            public void onSuccess(VersionReponse model) {
                if(Utils.convertIntVersion(model.getVersion())> LoginSharedPreference.getInstance(getActivity().getApplicationContext()).getVersion()){
                    new CouponListFragment.UpdateTask().execute(model.getVersion());
                }else{
                    mHandler.sendEmptyMessage(MSG_LOAD_CATEGORY);
                }
            }

            @Override
            public void onFailure(int msg) {
                AppLog.log(""+msg);
                mHandler.sendEmptyMessage(MSG_LOAD_CATEGORY);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    class UpdateTask extends AsyncTask<String, Void, Void> {
        URL url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... arg0) {
            CouponDTO item= new CouponDTO();
            boolean isOpened= false;
            try {
                url = new URL(Constant.BASE_URL_UPDATE);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                if ( factory == null){
                    hideLoading();
                    LoginSharedPreference.getInstance(getActivity()).setVersion(0);
                    mHandler.sendEmptyMessage(MSG_LOAD_CATEGORY);
                    return null;
                }

                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(getInputStream(url), "UTF_8");

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
                                    item.setExpiration_from(xpp.nextText());
                                    break;
                                case CouponDTO.TAG_EXPIRATION_TO:
                                    item.setExpiration_to(xpp.nextText());
                                    break;
                                case CouponDTO.TAG_PRIORITY:
                                    item.setPriority(Utils.parserInt(xpp.nextText()));
                                    break;
                                case CouponDTO.TAG_ADD_BLAND:
                                    item.setAdd_bland(xpp.nextText());
                                    break;
                                case CouponDTO.TAG_MEMO:
                                    item.setMemo(xpp.nextText());
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
            saveData(listResult);
            // save version number get from api
            if(arg0[0]!=null)
                LoginSharedPreference.getInstance(getActivity()).setVersion(Utils.convertIntVersion(arg0[0]));
            return null;
        }

        protected void onPostExecute(Void result) {
            hideLoading();
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
    public void saveData(ArrayList<CouponDTO> datas){
        if(datas!=null){
            sqLiteOpenHelper.clearData();
            sqLiteOpenHelper.saveCouponList(datas);
        }
    }
}