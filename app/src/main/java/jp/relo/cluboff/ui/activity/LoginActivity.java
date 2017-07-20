package jp.relo.cluboff.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.api.MyCallBack;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.model.LoginRequest;
import jp.relo.cluboff.model.VersionReponse;
import jp.relo.cluboff.ui.BaseActivityToolbar;
import jp.relo.cluboff.util.ConstansSharedPerence;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;
import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;

/**
 * Created by quynguyen on 3/22/17.
 */

public class LoginActivity extends BaseActivityToolbar implements View.OnClickListener{

    ImageView img_logo;
    TextView link_webview_not_login;
    TextView txt_show_error;
    Button btnLogin;
    MyDatabaseHelper sqLiteOpenHelper;
    EditText edtLoginUsername,edtPassword,edtMail;
    ArrayList<CouponDTO> listResult = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteOpenHelper = new MyDatabaseHelper(this);
        ((ReloApp)getApplication()).trackingAnalytics(Constant.TEST_GA_LOGIN_ANALYTICS);
        init();
    }

    private void init() {
        img_logo = (ImageView) findViewById(R.id.img_logo);
        link_webview_not_login = (TextView) findViewById(R.id.link_webview_not_login);
        txt_show_error = (TextView) findViewById(R.id.txt_show_error);
        edtLoginUsername = (EditText) findViewById(R.id.edtLoginUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtMail = (EditText) findViewById(R.id.edtMail);

        btnLogin = (Button) findViewById(R.id.bt_login);
        btnLogin.setOnClickListener(this);
        link_webview_not_login.setOnClickListener(this);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
    }

    @Override
    protected void registerEventHandlers() {

    }
    public void clickLogin(View view){
        boolean isNetworkAvailable = Utils.isNetworkAvailable(this);
        if(isNetworkAvailable) {
            final String username=edtLoginUsername.getText().toString();
            final String userMail = edtMail.getText().toString().trim();
            final String password = edtPassword.getText().toString();

            if (!StringUtil.isEmpty(username)&& !StringUtil.isEmpty(password)&& !StringUtil.isEmpty(userMail)) {
                showLoading(this);
                addSubscription(apiInterfaceJP.logon(username,userMail,password), new MyCallBack<LoginReponse>() {
                    @Override
                    public void onSuccess(LoginReponse model) {
                        if(model!=null){
                            if(Constant.HTTPOKJP.equals((model.getHeader().getStatus()))){
                                int brandid=0;
                                try {
                                    brandid = Utils.convertInt(BackAES.decrypt(model.getInfo().getBrandid(), AESHelper.password, AESHelper.type));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //Save reponse value
                                LoginSharedPreference.getInstance(LoginActivity.this).put(ConstansSharedPerence.TAG_LOGIN_SAVE,model.getInfo());
                                //save value input
                                LoginSharedPreference.getInstance(LoginActivity.this).put(ConstansSharedPerence.TAG_LOGIN_INPUT,
                                        new LoginRequest(username,userMail,password));
                                updateData();
                                setGoogleAnalytic(brandid);
                                //save user and password encrypt KeyStore

                            }else{
                                txt_show_error.setText(getResources().getString(R.string.error_blank_id_password));
                                txt_show_error.setVisibility(View.VISIBLE);
                                btnLogin.setEnabled(true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        if(msg==null){
                            msg="";
                        }
                        AppLog.log(msg);

                        //TODO login failure
                        txt_show_error.setText(msg);
                        txt_show_error.setVisibility(View.VISIBLE);
                        btnLogin.setEnabled(true);

                    }

                    @Override
                    public void onFinish() {
                        hideLoading();
                    }
                });

            }else{
                txt_show_error.setText(getResources().getString(R.string.error_login_wrong_id_password));
                txt_show_error.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(true);
            }

        }else{
            btnLogin.setEnabled(true);
            Utils.showDialog(this,R.string.popup_title_login,R.string.error_connect_network);
        }
    }

    public void setGoogleAnalytic(int brandid){
        ReloApp reloApp = (ReloApp) getApplication();
        reloApp.trackingWithAnalyticGoogleServices(Constant.GA_CATALOGY,Constant.GA_ACTION,Constant.GA_DIMENSION_VALUE,brandid);
    }



    /**
     *
     * Show webview with current url
     * @param key a key of url
     * @param url a url address
     * @param keyCheckWebview change title webview
     * <p>Note:
     *       <b>1 - Forget ID/Password</b>
     *       <b>2 - You can not login</b>
     * </p>
     */
    //TODO make redirection after splash screen gone
    private void goNextWebview(String key, String url, int keyCheckWebview) {
        Bundle bundle = new Bundle();
        bundle.putString(key,url);
        bundle.putInt(Constant.KEY_CHECK_WEBVIEW, keyCheckWebview);
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        link_webview_not_login.setPaintFlags(link_webview_not_login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        hideSoftKeyboard();
        LoginRequest loginRequest = LoginSharedPreference.getInstance(this).get(ConstansSharedPerence.TAG_LOGIN_INPUT, LoginRequest.class);
        if(loginRequest!=null){
            edtLoginUsername.setText(loginRequest.getKaiinno());
            edtPassword.setText(loginRequest.getBrandid());
            edtMail.setText(loginRequest.getEmailad());
        }
    }

    @Override
    public void setupToolbar() {
        lnToolbar.setVisibility(View.VISIBLE);
        title_toolbar.setVisibility(View.VISIBLE);
        title_toolbar.setText(getString(R.string.txt_title_login));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                btnLogin.setEnabled(false);
                clickLogin(v);
                break;
            /*case R.id.link_webview_forget_id:
                clickForget();
                break;*/
            case R.id.link_webview_not_login:
                clickLinkNotLogin();
                break;
        }
    }

    private void updateData(){
        addSubscription(apiInterface.checkVersion(),new MyCallBack<VersionReponse>() {
            @Override
            public void onSuccess(VersionReponse model) {
                if(Utils.convertIntVersion(model.getVersion())>((ReloApp)getApplication()).getVersion()){
                    new UpdateTask().execute(model.getVersion());
                }else{
                    gotoMain();
                }
            }

            @Override
            public void onFailure(String msg) {
                AppLog.log(msg);
                gotoMain();
            }

            @Override
            public void onFinish() {
                btnLogin.setEnabled(true);
            }
        });
    }
    private void gotoMain(){
        Intent mainActivity = new Intent(this, MainTabActivity.class);
                        startActivity(mainActivity);
                        finish();
    }
    public void clickLinkNotLogin(){
        goNextWebview(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_CAN_NOT_LOGIN, Constant.CAN_NOT_LOGIN);
    }

    public void saveData(ArrayList<CouponDTO> datas){
        if(datas!=null){
            sqLiteOpenHelper.clearData();
            sqLiteOpenHelper.saveCouponList(datas);
        }
    }
    class UpdateTask extends AsyncTask<String, Void, Void> {
        URL url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingData(LoginActivity.this);
        }

        protected Void doInBackground(String... arg0) {
            CouponDTO item= new CouponDTO();
            boolean isOpened= false;
            try {
                url = new URL(Constant.BASE_URL_UPDATE);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                if ( factory == null){
                    hideLoading();
                    LoginSharedPreference.getInstance(LoginActivity.this).setVersion(0);
                    gotoMain();
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
            LoginSharedPreference.getInstance(LoginActivity.this).setVersion(Utils.convertIntVersion(arg0[0]));
            return null;
        }

        protected void onPostExecute(Void result) {
            hideLoading();
            gotoMain();
        }
    }
    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
}
