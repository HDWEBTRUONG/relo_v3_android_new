package jp.relo.cluboff.util;

import jp.relo.cluboff.BuildConfig;

/**
 * Created by HuyTran on 3/21/17.
 */

public class Constant {
    //link server coupon
    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String BASE_URL_JP = BuildConfig.BASE_URL_JP;
    public static final String BASE_URL_UPDATE = BuildConfig.BASE_URL_UPDATE;

    // Some URL of webview
    public static final String WEBVIEW_URL_FORGET_LOGIN = BuildConfig.WEBVIEW_URL_FORGET_LOGIN;
    public static final String WEBVIEW_URL_CAN_NOT_LOGIN = BuildConfig.WEBVIEW_URL_CAN_NOT_LOGIN;
    public static final String WEBVIEW_URL_AREA_COUPON = BuildConfig.WEBVIEW_URL_AREA_COUPON;
    public static final String WEBVIEW_URL_MEMBER_COUPON = BuildConfig.WEBVIEW_URL_MEMBER_COUPON;

    public static final String KEY_ALIAS_APP = "reloalias";
    public static final String KEY_LOGIN_URL = "loginUrl";
    public static final String KEY_CHECK_WEBVIEW = "checkWebview";
    public static final String KEY_POST_WEBVIEW = "postDataWebview";


    // Const for Weview throught login page
    public static final int FORGET_PASSWORD = 1;
    public static final int CAN_NOT_LOGIN =  FORGET_PASSWORD + 1;
    public static final int AREA_COUPON =  CAN_NOT_LOGIN + 1;
    public static final int MEMBER_COUPON =  AREA_COUPON + 1;
    public static final int DETAIL_COUPON =  MEMBER_COUPON + 1;

    public static final int HTTPOK = 200;
    public static final String HTTPOKSTR = "200";
    public static final String HTTPOKJP = "00";


    //****************TEXT TEMP******************//
    public static final String GA_CATALOGY = "click";
    public static final String GA_ACTION = "login";
    public static final String GA_DIMENSION_VALUE = "success";
    public static final String TEST_GA_LOGIN_ANALYTICS = "LoginSrceen";
    //Push GCM
    public static final String GCM_SENDER_ID = BuildConfig.GCM_SENDER_ID;

    //****************TEXT DEMO******************//
    public static final String TEST_MENU_TOP = "TOPへ戻る";
    public static final String TEST_MENU_TUTORIAL = "アプリの使い方";
    public static final String TEST_LINK_COUPON = "https://sptest.club-off.com/relo";
    //Key username
    public static final String ACC_LOGIN_DEMO_USERNAME ="test";
    public static final String ACC_LOGIN_DEMO_PASS = "123";
    public static final String ACC_USER_MAIL = "admin@gmail.com";
    public static final String ACC_TEST_ID_LOGIN = "00008440";
    public static final String ACC_TEST_ID_LOGIN_ENCRY = "f4od/GCIvlp402l4ZOkYzg";
    public static final String ACC_TEST_URL_LOGIN = "sptest.club-off.com/relo";

}
