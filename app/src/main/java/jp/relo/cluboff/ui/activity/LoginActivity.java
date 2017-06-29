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

import com.google.gson.Gson;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import jp.relo.cluboff.R;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.api.MyCallBack;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.DataReponse;
import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.model.LoginRequest;
import jp.relo.cluboff.model.VersionReponse;
import jp.relo.cluboff.ui.BaseActivityToolbar;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.EASHelper;
import jp.relo.cluboff.util.LoginSharedPreference;
import jp.relo.cluboff.util.Utils;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteOpenHelper = new MyDatabaseHelper(this);
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

        if(jp.relo.cluboff.BuildConfig.DEBUG){
            img_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edtLoginUsername.getText().toString().equals(Constant.ACC_LOGIN_DEMO_USERNAME)){
                        edtLoginUsername.setText("70000000513");
                        edtPassword.setText("7619");
                    }else{
                        edtLoginUsername.setText(Constant.ACC_LOGIN_DEMO_USERNAME);
                        edtPassword.setText(Constant.ACC_LOGIN_DEMO_PASS);
                    }
                }
            });
        }
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
            String usernameTemp="";
            try {
                usernameTemp = AESCrypt.encrypt(EASHelper.password, edtLoginUsername.getText().toString());
            }catch (GeneralSecurityException e){
                //handle error
            }
            final String username=usernameTemp;
            final String userMail = edtMail.getText().toString().trim();
            final String password = edtPassword.getText().toString();

            //hard code login test
            if(edtLoginUsername.getText().toString().equals(Constant.ACC_LOGIN_DEMO_USERNAME)&&password.equals(Constant.ACC_LOGIN_DEMO_PASS)){
                updateData();
                return;
            }
            //end test

            if (!StringUtil.isEmpty(username)&& !StringUtil.isEmpty(password)&& !StringUtil.isEmpty(userMail)) {
                showLoading(this);
                LoginRequest loginRequest = new LoginRequest(username,userMail,password);
                addSubscription(apiInterfaceJP.logon(loginRequest), new MyCallBack<LoginReponse>() {
                    @Override
                    public void onSuccess(LoginReponse model) {
                        if(model.getStatus()==Constant.HTTPOK){
                            updateData();
                            //save user and password encrypt KeyStore
                            LoginSharedPreference.getInstance(LoginActivity.this).setLogin(username, userMail,password);
                        }else{
                            txt_show_error.setText(getResources().getString(R.string.error_blank_id_password));
                            txt_show_error.setVisibility(View.VISIBLE);
                            btnLogin.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
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

    public void clickForget(){
        trackingAnalytics(false,Constant.GA_LOGIN_SCREEN,Constant.GA_LOGIN_SCREEN_ACTION,
                Constant.GA_LOGIN_SCREEN_FORGET_LABEL, Constant.GA_LOGIN_SCREEN_CAN_NOT_LOGIN_VALUE);
        // Go to webview
        goNextWebview(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_FORGET_LOGIN, Constant.FORGET_PASSWORD);
    }

    public void clickLinkNotLogin(){
        trackingAnalytics(false,Constant.GA_LOGIN_SCREEN, Constant.GA_LOGIN_SCREEN_ACTION,
                Constant.GA_LOGIN_SCREEN_CAN_NOT_LOGIN_LABEL, Constant.GA_LOGIN_SCREEN_CAN_NOT_LOGIN_VALUE);
        // Go to webview
        goNextWebview(Constant.KEY_LOGIN_URL, Constant.WEBVIEW_URL_CAN_NOT_LOGIN, Constant.CAN_NOT_LOGIN);
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

    /**
     * Tracking Google Analytic Login Screen
     * @param isOnlyScreen
     * @param category
     * @param action
     * @param label
     * @param value
     */
    public void trackingAnalytics(Boolean isOnlyScreen, String category, String action, String label, long value){
        ReloApp application = (ReloApp) getApplication();
        if(isOnlyScreen){
            application.trackingAnalyticByScreen(Constant.GA_LOGIN_SCREEN);
        }else {
            application.trackingWithAnalyticGoogleServices(category, action, label, value);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*try {
            if(LoginSharedPreference.getInstance(this)!=null&&LoginSharedPreference.getInstance(this).getUserID()!=null){
                String userLoca = LoginSharedPreference.getInstance(this).getUserID();
                if(userLoca!=null&&userLoca.length()>0)
                edtLoginUsername.setText(AESCrypt.decrypt(EASHelper.password,userLoca));
            }
            edtPassword.setText(LoginSharedPreference.getInstance(this).getAppID());
            edtMail.setText(LoginSharedPreference.getInstance(this).getUserMail());
        }catch (GeneralSecurityException e){
            //handle error
        }*/
        link_webview_not_login.setPaintFlags(link_webview_not_login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        hideSoftKeyboard();
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
                AppLog.log("Complate");
                btnLogin.setEnabled(true);
            }
        });
    }
    private void gotoMain(){
        Intent mainActivity = new Intent(this, MainTabActivity.class);
                        startActivity(mainActivity);
                        finish();
    }

    class UpdateTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingData(LoginActivity.this);
        }

        protected Integer doInBackground(String... arg0) {
            //Your implementation
            Document doc = null;
            URL xmlURL = null;
            InputStream xml = null;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                xmlURL = new URL(Constant.BASE_URL_UPDATE);
                xml = xmlURL.openStream();
                db = dbf.newDocumentBuilder();
                doc = db.parse(xml);
                xml.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }catch (ParserConfigurationException e) {
                e.printStackTrace();
            }catch (SAXException e) {
                e.printStackTrace();
            }
            saveData(doc);
            return Utils.convertIntVersion(arg0[0]);
        }

        protected void onPostExecute(Integer result) {
            // TODO: do something with the feed
            hideLoading();
            LoginSharedPreference.getInstance(LoginActivity.this).setVersion(result);
            gotoMain();
        }
    }

    public void saveData(Document doc){
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }catch (TransformerException e) {
            e.printStackTrace();
        }
        try {
            DataReponse dataReponse=new Gson().fromJson(XML.toJSONObject(writer.toString()).toString(), DataReponse.class);
            if(dataReponse!=null){
                sqLiteOpenHelper.clearData();
                sqLiteOpenHelper.saveCouponList(dataReponse.getData().getCoupon());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
