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
    public static final String WEBVIEW_URL_CAN_NOT_LOGIN = BuildConfig.WEBVIEW_URL_CAN_NOT_LOGIN;
    public static final String WEBVIEW_URL_AREA_COUPON = BuildConfig.WEBVIEW_URL_AREA_COUPON;
    public static final String WEBVIEW_URL_TUTORIAL = BuildConfig.WEBVIEW_URL_TUTORIAL;
    public static final String WEBVIEW_URL_FAQ = BuildConfig.WEBVIEW_URL_FAQ;

    //Template url
    public static final String TEMPLATE_URL_MEMBER = BuildConfig.TEMPLATE_URL_MEMBER;
    public static final String TEMPLATE_URL_COUPON = BuildConfig.TEMPLATE_URL_COUPON;
    public static final String TEMPLATE_ARG = BuildConfig.TEMPLATE_ARG;

    //Appvisor id
    public static final String APPVISOR_ID = BuildConfig.APPVISOR_ID;

    public static final String KEY_ALIAS_APP = "reloalias";
    public static final String KEY_LOGIN_URL = "loginUrl";
    public static final String KEY_URL_TYPE= "urlType";
    public static final String KEY_CHECK_WEBVIEW = "checkWebview";


    // Const for Weview throught login page
    public static final int FORGET_PASSWORD = 1;
    public static final int CAN_NOT_LOGIN =  FORGET_PASSWORD + 1;
    public static final int AREA_COUPON =  CAN_NOT_LOGIN + 1;
    public static final int MEMBER_COUPON =  AREA_COUPON + 1;
    public static final int DETAIL_COUPON =  MEMBER_COUPON + 1;
    public static final int TUTURIAL_APP =  DETAIL_COUPON + 1;

    public static final String HTTPOKJP = "00";
    public static final String HTTP0000 = "0000";
    public static final String HTTP0001 = "0001";
    public static final String HTTP0002 = "0002";
    public static final String HTTP0005 = "0005";
    public static final String HTTP0006 = "0006";
    public static final String HTTP0007 = "0007";
    public static final String HTTP0010 = "0010";
    public static final String HTTP0021 = "0021";
    public static final String HTTP0022 = "0022";
    public static final String HTTP0023 = "0023";
    public static final String HTTP0099 = "0099";


    //****************TEXT TEMP******************//
    public static final String GA_CATALOGY = "click";
    public static final String GA_ACTION = "login";
    public static final String GA_DIMENSION_VALUE = "success";
    public static final String TEST_GA_LOGIN_ANALYTICS = "LoginSrceen";
    //Push GCM
    public static final String GCM_SENDER_ID = BuildConfig.GCM_SENDER_ID;
    //Key username
    public static final String ACC_TEST_ID_LOGIN = "00008440";
    public static final String ACC_TEST_ID_LOGIN_ENCRY = "f4od/GCIvlp402l4ZOkYzg";
    public static final String ACC_TEST_URL_LOGIN = "sptest.club-off.com/relo";


    public static final String TAG_USER_ID= "userid";
    public static final String TAG_SHGRID = "shgrid";
    public static final String TAG_SENICODE= "senicode";
    public static final String TAG_KAIINNO= "kaiinno";
    public static final String TAG_BRNDID = "brndid";
        public static final String TAG_IS_FIRST = "isFirst";

        //****************CATEGORY ID******************//
    public static final String VALUE_CATALOGY_25 = "25";
    public static final String VALUE_CATALOGY_27 = "27";
    public static final String VALUE_CATALOGY_32 = "32";
    public static final String VALUE_CATALOGY_126 = "126";
    public static final String VALUE_CATALOGY_999 = "999";

    //****************TARGET PUSH******************//
    public static final String TARGET_PUSH = "target_push";
    public static final String TARGET_PUSH_SCREEN_AREA = "area";
    public static final String TARGET_PUSH_SCREEN_COUPON = "coupon";
    public static final String TARGET_PUSH_SCREEN_SITE= "site";
    public static final String TARGET_PUSH_SCREEN_LIST = "list";
}
