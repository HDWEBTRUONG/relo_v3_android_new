package main.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import framework.phvtActivity.BaseActivity;
import framework.phvtUtils.AppLog;
import main.R;
import main.ReloApp;
import main.api.ApiClient;
import main.api.ApiInterface;
import main.api.MyCallBack;
import main.database.MyDatabaseHelper;
import main.model.DataReponse;
import main.model.VersionReponse;
import main.ui.BaseActivityToolbar;
import main.util.Constant;
import main.util.LoginSharedPreference;
import main.util.Utils;
import rx.Subscriber;

/**
 * Created by quynguyen on 3/22/17.
 */

public class LoginActivity extends BaseActivityToolbar implements View.OnClickListener{
    EditText editUsername;
    EditText editPassword;
    TextView txtShowError;
    TextView link_webview_forget_id;
    TextView link_webview_not_login;
    Button btnLogin;
    MyDatabaseHelper sqLiteOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteOpenHelper = new MyDatabaseHelper(this);
        init();
    }

    private void init() {
        editUsername = (EditText) findViewById(R.id.edit_login_username);
        editPassword = (EditText) findViewById(R.id.edit_login_password);
        txtShowError = (TextView) findViewById(R.id.txt_show_error);
        link_webview_forget_id = (TextView) findViewById(R.id.link_webview_forget_id);
        link_webview_not_login = (TextView) findViewById(R.id.link_webview_not_login);
        btnLogin = (Button) findViewById(R.id.bt_login);
        btnLogin.setOnClickListener(this);
        link_webview_forget_id.setOnClickListener(this);
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
            // Get Data From UI
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            try {
                if ((username != null && !username.isEmpty()) && (password != null && !password.isEmpty())) {
                    if(username.equals(Constant.ACC_LOGIN_DEMO_USERNAME) && password.equals(Constant.ACC_LOGIN_DEMO_PASSWORD)) {
                        //save user and password encrypt KeyStore
                        LoginSharedPreference.getInstance(this).setLogin(encryptKeyStore(username),encryptKeyStore(password));
                        updateData();
                    }else{
                        txtShowError.setText(getResources().getString(R.string.error_login_wrong_id_password));
                        txtShowError.setVisibility(View.VISIBLE);
                        btnLogin.setEnabled(true);
                    }
                }else{
                    Utils.showDialog(this,R.string.popup_title_login,R.string.error_blank_id_password);
                    btnLogin.setEnabled(true);
                }
            }catch (Exception e) {
                e.printStackTrace();
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
        editUsername.setText(decryptKeyStore(LoginSharedPreference.getInstance(this).getUserID()));
        editPassword.setText(decryptKeyStore(LoginSharedPreference.getInstance(this).getPassword()));
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
            case R.id.link_webview_forget_id:
                clickForget();
                break;
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
            showLoading(LoginActivity.this);
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
