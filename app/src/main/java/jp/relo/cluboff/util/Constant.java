package jp.relo.cluboff.util;

import java.util.ArrayList;

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
    public static final String WEBVIEW_URL_AREA_COUPON = BuildConfig.WEBVIEW_URL_AREA_COUPON;
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
    public static final int FAQ =  TUTURIAL_APP + 1;

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

    //------------------Google Analytic Login----------------//
    public static final String GA_LOGIN_SCREEN = "ログイン画面";
    public static final String GA_CATALOGY_LOGIN = "ログイン";
    public static final String GA_ACTION_LOGIN = "タップ";
    public static final String GA_LABLE_LOGIN = "IDPASSチェック";

    //------------------Google Analytic Detail Coupon----------------//
    public static final String GA_DETAIL_SCREEN = "画面提示クーポン画面";
    public static final String GA_CATALOGY_DETAIL = "人気クーポン ";
    public static final String GA_ACTION_DETAIL = "タップ";
    public static final String GA_VALUE_DETAIL = ""; //ToDO Change value

    //------------------Google Analytic Like Coupon----------------//
    public static final String GA_LIKE_CATEGORY = "人気クーポン お気に入り";
    public static final String GA_ACTION_LIKE = "タップ";
    public static final String GA_VALUE_LIKE = ""; //ToDO Change value


    //------------------Google Analytic Area Coupon----------------//
    public static final String GA_AREA_SCREEN = "エリアクーポン";

    //------------------Google Analytic List Coupon----------------//
    public static final String GA_LIST_COUPON_SCREEN = "人気クーポン";

    //------------------Google Analytic Member Coupon----------------//
    public static final String GA_MEMBER_SCREEN = "会員サイト";

    //------------------Google Analytic HistoryPush----------------//
    public static final String GA_HISTORYPUSH_SCREEN = "お知らせ";
    public static final String GA_HISTORYPUSH_CATEGORY = "人気クーポン お気に入り";
    public static final String GA_HISTORYPUSH_ACTION = "タップ";
    public static final String GA_HISTORYPUSH_VALUE = ""; //ToDO Change value

    //------------------Google Analytic How to----------------//
    public static final String GA_HOWTO_SCREEN = "アプリの使い方";


    //------------------Google Analytic FAQ----------------//
    public static final String GA_FAQ_SCREEN = "FAQ";


    //Push GCM
    public static final String GCM_SENDER_ID = BuildConfig.GCM_SENDER_ID;

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

    public static final String[] listCategory = {"25","27","32","126"};
    public static final String[] listCategoryName = {"グルメ","カラオケ","レジャー","日帰り湯","その他"};

    public static final String TOP_COUPON= "TOP_COUPON";
    public static final String USERID= "USERID";
    public static final int LIMIT_ON_BACKGROUND= 10800000;
}
