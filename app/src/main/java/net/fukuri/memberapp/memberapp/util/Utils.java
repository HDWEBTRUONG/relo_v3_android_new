package net.fukuri.memberapp.memberapp.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;

import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.ReloApp;
import net.fukuri.memberapp.memberapp.ui.activity.LoginActivity;
import net.fukuri.memberapp.memberapp.views.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by quynguyen on 3/22/17.
 */

public class Utils {

    public static  void setCookie(String domain, String sessionCookie) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        if(!StringUtil.isEmpty(sessionCookie)) {
            String[] temp = sessionCookie.split(";");
            for (String ar1 : temp) {
                cookieManager.setCookie(domain, ar1);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }else{
            CookieSyncManager.getInstance().sync();
        }
    }

    public static String getAllCokkie(String domain){
        CookieManager cookieManager = CookieManager.getInstance();
        return cookieManager.getCookie(domain);
    }

    public static String setCookieIconPage(Context context, String domain, String newCookie){
        //String appCookie = getAllCokkie( domain);
        if(!StringUtil.isEmpty(newCookie)) {
            setCookie(domain,newCookie);
            /*String[] temp = appCookie.split(";");
            for (String ar1 : temp) {
                if (ar1.contains(Constant.WEB_COOKIE)) {
                    appCookie = appCookie.replace(ar1, Constant.WEB_COOKIE+"="+newCookie);
                    setCookie(domain,appCookie);
                }
            }*/
        }
        return "";
    }



    public static void getCookie(Context context, String siteName){
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        AppLog.log("All Cookie: "+cookies);
        LoginSharedPreference.getInstance(context).setCookie(cookies);
        /*if(cookies!=null){
            String[] temp=cookies.split(";");
            for (String ar1 : temp ){
                AppLog.log("Cookie: "+ ar1);

                if(ar1.contains(Constant.WEB_COOKIE) && ar1.contains("=")){
                    String cookie = ar1.substring(ar1.indexOf("=")+1,ar1.length());
                    LoginSharedPreference.getInstance(context).setCookie(cookie);
                }


            }
        }*/
    }
    public static void getLogCookie(Context context, String siteName){
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        AppLog.log("All Cookie log: "+cookies);
    }

    public static  boolean isAuthSuccess(Context context, Document document){
        boolean isSuccess = true;
        Element divChildren = document.select("html").first();
        for (int i = 0; i < divChildren.childNodes().size(); i++) {
            Node child = divChildren.childNode(i);
            if (child.nodeName().equals("#comment")) {
                String valueAuth = child.toString();
                int valueHandleLogin = 0;
                if(Utils.parserInt(valueAuth.substring(valueAuth.indexOf("<STS>")+5,valueAuth.indexOf("</STS>")))!=valueHandleLogin){
                    LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(context);
                    loginSharedPreference.setKEY_APPU(valueAuth.substring(valueAuth.indexOf("<APPU>")+6,valueAuth.indexOf("</APPU>")));
                    loginSharedPreference.setKEY_APPP(valueAuth.substring(valueAuth.indexOf("<APPP>")+6,valueAuth.indexOf("</APPP>")));
                }else{
                    isSuccess = false;
                }
            }
        }
        ReloApp.setBlockAuth(!isSuccess);
        return  isSuccess;
    }

    public static String getDefaultUserAgent() {
        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version")); // such as 1.1.0
        result.append(" (Linux; U; Android ");

        String version = Build.VERSION.RELEASE; // "1.0" or "3.4b5"
        result.append(version.length() > 0 ? version : "1.0");

        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        String id = Build.ID; // "MASTER" or "M4-rc20"
        if (id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }
        result.append(")");
        return result.toString();
    }

    public static String addTagRedBenefit(String txt){
        if(txt!=null){
            return txt.replaceAll("<strong>","<font color='red'>").replaceAll("</strong>","</font>");
        }else{
            return "";
        }
    }

    public static void forceLogout(Activity context){
        LoginSharedPreference.getInstance(context).forceLogout();
        context.startActivity(new Intent(context, LoginActivity.class));
        context.finish();
    }

    public static String loadSharedPrefs(Context context, String ... prefs) {
        String result ="";

        // Define default return values. These should not display, but are needed
        final String STRING_ERROR = "error!";
        final Integer INT_ERROR = -1;
        // ...
        final Set<String> SET_ERROR = new HashSet<>(1);

        // Add an item to the set
        SET_ERROR.add("Set Error!");
        for (String pref_name: prefs) {

            SharedPreferences preference = context.getSharedPreferences(pref_name, MODE_PRIVATE);
            Map<String, ?> prefMap = preference.getAll();

            Object prefObj;
            Object prefValue = null;

            for (String key : prefMap.keySet()) {

                prefObj = prefMap.get(key);

                if (prefObj instanceof String) prefValue = preference.getString(key, STRING_ERROR);
                if (prefObj instanceof Integer) prefValue = preference.getInt(key, INT_ERROR);
                // ...
                if (prefObj instanceof Set) prefValue = preference.getStringSet(key, SET_ERROR);
                if(key!=null && prefObj.toString().contains("member_id")){
                    Log.i(String.format("Shared Preference : %s - %s", pref_name, key),
                            result =String.valueOf(prefValue));
                }

            }
        }
        return result;
    }

    public static final String TEMP_TOSTRING = "ToString(";

    public static void setupWebView(WebView mWebView) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //Disable cache Webview
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }


    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void createNewKeys(Context mContext,KeyStore keyStore) {
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(Constant.KEY_ALIAS_APP)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(mContext)
                        .setAlias(Constant.KEY_ALIAS_APP)
                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);

                KeyPair keyPair = generator.generateKeyPair();
            }
        } catch (Exception e) {
            AppLog.log(Log.getStackTraceString(e));
        }
    }

    public static String encryptString(KeyStore keyStore,String input) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(Constant.KEY_ALIAS_APP, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            if(input.isEmpty()) {
                return "";
            }
            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(input.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte [] vals = outputStream.toByteArray();
            return Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("KTS", Log.getStackTraceString(e));
        }
        return "";
    }

    public static String decryptString(KeyStore keyStore,String input) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(Constant.KEY_ALIAS_APP, null);
            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(input, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            return finalText;

        } catch (Exception e) {
            AppLog.log(Log.getStackTraceString(e));
        }
        return "";
    }
    public static int convertInt(String value){
        if(value!=null&&!"".equals(value)){
            try {
                return Integer.valueOf(value);
            }catch (Exception ex){
                return 0;
            }
        }
        return 0;
    }
    public static long convertLong(String value){
        if(value!=null&&!"".equals(value)){
            try {
                return Long.valueOf(value);
            }catch (Exception ex){
                return 0;
            }
        }
        return 0;
    }
    public static int convertIntVersion(String value){
        if(value!=null&&!"".equals(value)){
            try {
                return Integer.valueOf(value.replace(".","").replace(",",""));
            }catch (Exception ex){
                return 0;
            }
        }
        return 0;
    }

    public static void showDialog(Context context,int title, int messagee){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(title));
        alertDialog.setMessage(context.getResources().getString(messagee));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getResources().getString(R.string.popup_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public static void showDialog(Context context, int messagee){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(context.getResources().getString(messagee));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getResources().getString(R.string.popup_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void showDialogLIB(Context context, final int messagee){
        new SweetAlertDialog(context)
                .setTitleText(context.getString(R.string.title_dialog_error))
                .setContentText(context.getResources().getString(messagee))
                .setConfirmText(context.getResources().getString(R.string.popup_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
    public static void showDialogAPI(Context context, final int messagee){
        new SweetAlertDialog(context)
                .setTitleText(context.getString(R.string.title_dialog_error))
                .setContentText(context.getResources().getString(R.string.err_api)+context.getResources().getString(messagee))
                .setConfirmText(context.getResources().getString(R.string.popup_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public static void showDialogLIB(Context context, int title, int messagee, int btn, final iUpdateIU miUpdateIU){
        new SweetAlertDialog(context)
                .setTitleText(context.getResources().getString(title))
                .setContentText(context.getResources().getString(messagee))
                .setConfirmText(context.getResources().getString(btn ))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        if(miUpdateIU!=null){
                            miUpdateIU.updateError(0);
                        }
                    }
                })
                .show();
    }

    public static String PLAY_STORE_LINK ="http://play.google.com/store/apps/details?id=%s";
    public static void showDialogLIBForceUpdate(final Context context, final String messagee){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog
                .setTitleText(context.getString(R.string.force_update_title))
                .setContentText(messagee)
                .setConfirmText(context.getResources().getString(R.string.popup_update))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //sDialog.dismissWithAnimation();
                        AppLog.log("Link: "+String.format(PLAY_STORE_LINK, context.getPackageName()));
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(PLAY_STORE_LINK, context.getPackageName()))));
                        System.exit(0);
                    }
                })
                .show();
    }

    public static void showDialogUpdate(Context context, int title, String messagee, final DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(messagee);
        builder.setPositiveButton(R.string.btn_update, onClickListener);
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.show();
    }

    public static void showDialogBrowser(Context context, final DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_browser);
        builder.setMessage(R.string.content_browser);
        builder.setPositiveButton(R.string.btn_browser, onClickListener);
        builder.setNegativeButton(R.string.btn_browser_cacel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public static String getYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return ""+ year;
    }

    public static String convertDateShort(String dateString){
        if(dateString==null){
            return "";
        }else{
            if(dateString.length()>8){
                dateString = dateString.substring(8) ;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = format.parse(dateString);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String valueNowTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String fomatDate = dateFormat.format(date);
        return fomatDate;
    }
    public static boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }
    /**
     * Darkens a color by a given factor.
     *
     * @param color
     *     the color to darken
     * @param factor
     *     The factor to darken the color.
     * @return darker version of specified color.
     */
    public static int darker(int color, float factor) {
        return Color.argb(Color.alpha(color), Math.max((int) (Color.red(color) * factor), 0),
                Math.max((int) (Color.green(color) * factor), 0),
                Math.max((int) (Color.blue(color) * factor), 0));
    }

    /**
     * Lightens a color by a given factor.
     *
     * @param color
     *     The color to lighten
     * @param factor
     *     The factor to lighten the color. 0 will make the color unchanged. 1 will make the
     *     color white.
     * @return lighter version of the specified color.
     */
    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    /**
     * Check if layout direction is RTL
     *
     * @param context
     *     the current context
     * @return {@code true} if the layout direction is right-to-left
     */
    static boolean isRtl(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * Return a drawable object associated with a particular resource ID.
     *
     * <p>Starting in {@link android.os.Build.VERSION_CODES#LOLLIPOP}, the returned drawable will be styled for the
     * specified Context's theme.</p>
     *
     * @param id
     *     The desired resource identifier, as generated by the aapt tool.
     *     This integer encodes the package, type, and resource entry.
     *     The value 0 is an invalid identifier.
     * @return Drawable An object that can be used to draw this resource.
     */
    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        }
        return ContextCompat.getDrawable(context,id);
    }

    public static long dateTimeValue()
    {
        return new Date().getTime();
    }
    public static String long2Time(long time){
        Date dateObj = new Date(time);
        SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyy/MM/dd");
        StringBuilder strTime = new StringBuilder( dateformatYYYYMMDD.format( dateObj ) );
        return strTime.toString();
    }
    public static int parserInt(String num){
        if(StringUtil.isEmpty(num)){
            return 0;
        }else{
            try{
                return Integer.parseInt(num);
            }catch (Exception ex){
                AppLog.log(ex.toString());
                return 0;
            }
        }
    }
    public static long parserLong(String num){
        if(StringUtil.isEmpty(num)){
            return 0;
        }else{
            try{
                return Long.parseLong(num);
            }catch (Exception ex){
                AppLog.log(ex.toString());
                return 0;
            }
        }
    }

    public static String removeString(String txt){

        if(txt!=null && txt.startsWith(TEMP_TOSTRING)){
            return txt.substring(TEMP_TOSTRING.length(),txt.length()-1);
        }
        return txt;
    }
    public static boolean isMyServiceRunning(Class<?> serviceClass, Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public static long getValueDateTime(){
        return System.currentTimeMillis();
    }
}
