package main.util;

/**
 * Created by HuyTran on 3/21/17.
 */

public class Constant {
    //link server coupon
    public static final String BASE_URL = "http://54.202.202.214/relo/";
    public static final String BASE_URL_UPDATE = "http://54.202.202.214/relo/relo_data.xml";


    //Google Analytics
    public static final String GA_POPULAR_COUPON_SCREEN = "人気クーポン";

    public static final String KEY_ALIAS_APP = "reloalias";
    public static final String KEY_LOGIN_URL = "loginUrl";
    public static final String KEY_CHECK_WEBVIEW = "checkWebview";

    // Some URL of webview
    public static final String WEBVIEW_URL_FORGET_LOGIN = "https://sp.fukuri.jp/fkr/contents/files/fukuri/no_login_app.html";
    public static final String WEBVIEW_URL_CAN_NOT_LOGIN = "https://sp.fukuri.jp/fkr/contents/files/fukuri/first_times_01_1.html";
    public static final String WEBVIEW_URL_AREA_COUPON = "https://www.google.com/maps/@10.764855,106.6468148,15z";
    public static final String WEBVIEW_URL_MEMBER_COUPON = "http://devpro.edu.vn";

    //Key username
    public static final String ACC_LOGIN_DEMO_USERNAME ="admin";
    public static final String ACC_LOGIN_DEMO_PASSWORD = "123456";

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

}
