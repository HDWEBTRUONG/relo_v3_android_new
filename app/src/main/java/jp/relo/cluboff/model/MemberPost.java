package jp.relo.cluboff.model;

import com.scottyab.aescrypt.AESCrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.EASHelper;

/**
 * Created by tonkhanh on 7/6/17.
 */

public class MemberPost {
    private String u;
    private String COA_APP;

    public MemberPost() {
        this.u = Constant.ACC_TEST_ID_LOGIN_ENCRY;
        this.COA_APP = "1";
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getCOA_APP() {
        return COA_APP;
    }

    public void setCOA_APP(String COA_APP) {
        this.COA_APP = COA_APP;
    }

    @Override
    public String toString() {
        try {
            return "u=" + URLEncoder.encode(u, "UTF-8") +
                    "&COA_APP=" + URLEncoder.encode(COA_APP, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
