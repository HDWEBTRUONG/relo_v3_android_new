package jp.relo.cluboff.util;

import jp.relo.cluboff.BuildConfig;

/**
 * Created by HuyTran on 3/21/17.
 */

public class Constant {
    //link server coupon
    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String BASE_URL_JP = BuildConfig.BASE_URL_JP;
    //public static final String BASE_URL_UPDATE = "http://54.202.202.214/relo/relo_data.xml";
    public static final String BASE_URL_UPDATE = BuildConfig.BASE_URL_UPDATE;


    //Google Analytics
    public static final String GA_POPULAR_COUPON_SCREEN = "人気クーポン";

    public static final String KEY_ALIAS_APP = "reloalias";
    public static final String KEY_LOGIN_URL = "loginUrl";
    public static final String KEY_CHECK_WEBVIEW = "checkWebview";

    // Some URL of webview
    public static final String WEBVIEW_URL_FORGET_LOGIN = BuildConfig.WEBVIEW_URL_FORGET_LOGIN;
    public static final String WEBVIEW_URL_CAN_NOT_LOGIN = BuildConfig.WEBVIEW_URL_CAN_NOT_LOGIN;
    public static final String WEBVIEW_URL_AREA_COUPON = BuildConfig.WEBVIEW_URL_AREA_COUPON;
    public static final String WEBVIEW_URL_MEMBER_COUPON = BuildConfig.WEBVIEW_URL_MEMBER_COUPON;

    //Key username
    public static final String ACC_LOGIN_DEMO_USERNAME ="test";
    public static final String ACC_LOGIN_DEMO_PASS = "123";
    public static final String ACC_USER_MAIL = "admin@gmail.com";

    /** GA LOGIN**/
    public static final String GA_LOGIN_SCREEN = "ログイン";
    public static final String GA_LOGIN_SCREEN_ACTION = "タップ";

    /** GA FORGET ID/PASSWORD **/
    public static final String GA_LOGIN_SCREEN_FORGET_LABEL = "IDPASSチェック";
    public static final long GA_LOGIN_FORGET_VALUE = 1;

    /** GA You can not login **/
    public static final String GA_LOGIN_SCREEN_CAN_NOT_LOGIN_LABEL = "ログイン確認";
    public static final long GA_LOGIN_SCREEN_CAN_NOT_LOGIN_VALUE = 1;


    // Const for Weview throught login page
    public static final int FORGET_PASSWORD = 1;
    public static final int CAN_NOT_LOGIN =  FORGET_PASSWORD + 1;
    public static final int AREA_COUPON =  CAN_NOT_LOGIN + 1;
    public static final int MEMBER_COUPON =  AREA_COUPON + 1;
    public static final int DETAIL_COUPON =  MEMBER_COUPON + 1;

    public static final int HTTPOK = 200;
    public static final String HTTPOKSTR = "200";

    public static final String PUSH_ACTION = "jp.relo.cluboff.pushing";
    //Push GCM
    public static final String GCM_SENDER_ID = BuildConfig.GCM_SENDER_ID;

    public static final String GA_CATALOGY = "click";
    public static final String GA_ACTION = "login";
    public static final String GA_DIMENSION_VALUE = "success";

    public static final String TEST_GA_LOGIN_ANALYTICS = "LoginSrceen";


}
