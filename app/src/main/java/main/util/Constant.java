package main.util;

/**
 * Created by HuyTran on 3/21/17.
 */

public class Constant {

    //Google Analytics
    public static final String GA_POPULAR_COUPON_SCREEN = "人気クーポン";

    public static final String KEY_URL_FORGET_LOGIN = "fogetLoginUrl";
    public static final String KEY_CHECK_WEBVIEW = "checkWebview";

    // Some URL of webview
    public static final String WEBVIEW_URL_FORGET_LOGIN = "https://google.com";
    public static final String WEBVIEW_URL_CAN_NOT_LOGIN = "https://google.com";

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

}
