package jp.relo.cluboff.model;

import jp.relo.cluboff.util.ase.AESHelper;
import jp.relo.cluboff.util.ase.BackAES;

/**
 * Created by PhatVan on 8/4/17.
 */

public class UserProfile {

    //FIRST LOGIN : none encry
    public static String user_id_first_login_plaintext;
    public static String brand_id_first_login_plaintext;
    public static String email_first_login_plaintext;

    // have encry
    public static String user_id_responsed_encry ;
    public static String brand_id_responsed_encry;
    public static String url_responsed_encry;

    // RESPONSE none encry
    public static String user_id_responsed_plaintext ;
    public static String brand_id_responsed_plaintext;
    public static String url_responsed_none_plaintext;


    public static void set_data_first_login(String _user_id, String _email, String _brand_id ){
        user_id_first_login_plaintext = _user_id;
        brand_id_first_login_plaintext = _brand_id;
        email_first_login_plaintext = _email;
    }

    public static void set_data_response_login(String _user_id, String _url, String _brand_id ) {

        user_id_responsed_encry = _user_id;
        brand_id_responsed_encry = _brand_id;
        url_responsed_encry = _url;

        try {
            //1. get data from
            user_id_responsed_plaintext = new String(BackAES.decrypt(_user_id, AESHelper.password, AESHelper.type));
            brand_id_responsed_plaintext = new String(BackAES.decrypt(_user_id, AESHelper.password, AESHelper.type));
            url_responsed_none_plaintext = new String(BackAES.decrypt(_user_id, AESHelper.password, AESHelper.type));

            //2. remove ToString text
            //....
            //....

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
