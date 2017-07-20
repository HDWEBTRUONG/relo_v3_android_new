package jp.relo.cluboff.util.ase;


import framework.phvtUtils.AppLog;
import jp.relo.cluboff.util.ase.BackAES;

/**
 * Created by tonkhanh on 6/16/17.
 */

public class AESHelper {
    public static String password="cluboffapp0244az";
    public static int type = 0;

    public static void Test(){
        try {
            String str="";
            String result="";
            result = new String(BackAES.encrypt("sptest.club-off.com/relo/", AESHelper.password, AESHelper.type));
            AppLog.log("----------------"+result);
            str = BackAES.decrypt(result, AESHelper.password, AESHelper.type);
            AppLog.log("----------------"+str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
