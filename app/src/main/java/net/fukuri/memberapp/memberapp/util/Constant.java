package net.fukuri.memberapp.memberapp.util;

import net.fukuri.memberapp.memberapp.BuildConfig;

/**
 * Created by HuyTran on 3/21/17.
 */

public class Constant {
    //link server coupon
    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String BASE_URL_JP = BuildConfig.BASE_URL_JP;
    public static final String BASE_URL_LOG = BuildConfig.BASE_URL_LOG;
    public static final String BASE_URL_FORCE_UPDATE = BuildConfig.URL_FORCEUPDATE;
    public static final String URL_FORCEUPDATE_FULL = BuildConfig.URL_FORCEUPDATE_FULL;

    // Some URL of webview
    public static final String WEBVIEW_URL_AREA_COUPON = BuildConfig.WEBVIEW_URL_AREA_COUPON;
    public static final String WEBVIEW_CAN_NOT_LOGIN = BuildConfig.WEBVIEW_CAN_NOT_LOGIN;
    public static final String WEBVIEW_FORGET_ID = BuildConfig.WEBVIEW_FORGET_ID;

    //Template url
    public static final String TEMPLATE_URL_MEMBER = BuildConfig.TEMPLATE_URL_MEMBER;
    public static final String TEMPLATE_ARG = BuildConfig.TEMPLATE_ARG;

    public static final String URL_FAQ = BuildConfig.URL_FAQ;

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
    public static final int FORGET_ID =  FAQ + 1;

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

    //------------------Google Analytic How to----------------//
    public static final String GA_HOWTO_SCREEN = "アプリの使い方";


    //------------------Google Analytic FAQ----------------//
    public static final String GA_FAQ_SCREEN = "FAQ";


    //Push GCM
    public static final String GCM_SENDER_ID = BuildConfig.GCM_SENDER_ID;

    public static final String TAG_SHGRID = "shgrid";
    public static final String TAG_SENICODE= "senicode";
    public static final String TAG_KAIINNO= "kaiinno";
        public static final String TAG_IS_FIRST = "isFirst";

        //****************CATEGORY ID******************//
    public static final String VALUE_CATALOGY_12 = "012";
    public static final String VALUE_CATALOGY_09 = "009";
    public static final String VALUE_CATALOGY_15 = "015";
    public static final String VALUE_CATALOGY_21 = "021";
    public static final String VALUE_CATALOGY_18 = "018";
    public static final String VALUE_CATALOGY_999 = "999";

    //****************TARGET PUSH******************//
    public static final String TARGET_PUSH = "target_push";
    public static final String TARGET_PUSH_SCREEN_AREA = "area";
    public static final String TARGET_PUSH_SCREEN_COUPON = "coupon";
    public static final String TARGET_PUSH_SCREEN_SITE= "site";
    public static final String TARGET_PUSH_SCREEN_LIST = "list";

    public static final String[] listCategoryName = {"グルメ","カラオケ","スーツ・メガネ","その他"};
    public static final String[] listCategoryNameArea = {"レジャー","日帰り湯","グルメ","スーツ・メガネ","その他"};

    public static final String TOP_COUPON= "TOP_COUPON";
    public static final int LIMIT_ON_BACKGROUND= 10800000;
    public static final int LIMIT_ON_BACKGROUND_MEMBERSITE= 1800000;

    public static final String DATA_COUPON_URL= "DATA_COUPON_URL";
    public static final String URL_MEMBER_BROWSER= BuildConfig.URL_MEMBER_BROWSER;


    public static final String BUNDER_URL= "BUNDER_URL";
    public static final String BUNDER_TITLE= "BUNDER_TITLE";
    public static final String BUNDER_SUBTITLE= "BUNDER_SUBTITLE";

    public static final String XML_WHOLEJAPAN = BuildConfig.XML_WHOLEJAPAN;
    public static final String XML_HOKKAIDO = BuildConfig.XML_HOKKAIDO;
    public static final String XML_TOHOKU = BuildConfig.XML_TOHOKU;
    public static final String XML_KANTO = BuildConfig.XML_KANTO;
    public static final String XML_KOUSHINETSU = BuildConfig.XML_KOUSHINETSU;
    public static final String XML_HOKURIKUTOKAI = BuildConfig.XML_HOKURIKUTOKAI;
    public static final String XML_KINKI = BuildConfig.XML_KINKI;
    public static final String XML_CYUUGOKUSHIKOKU = BuildConfig.XML_CYUUGOKUSHIKOKU;
    public static final String XML_KYUSHU = BuildConfig.XML_KYUSHU;
    public static final String XML_OKINAWA = BuildConfig.XML_OKINAWA;

    public static final String TITLE_LOGOUT = "認証エラー";

    public static final String URL_READ_FDF = "http://docs.google.com/gview?embedded=true&url=";
    public static final String URLS_READ_FDF = "https://docs.google.com/gview?embedded=true&url=";

}
