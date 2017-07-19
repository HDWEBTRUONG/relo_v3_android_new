package jp.relo.cluboff.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by tonkhanh on 7/19/17.
 */

public class PostDetail {
    String userid;
    String requestno;
    String senicode;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    public String getSenicode() {
        return senicode;
    }

    public void setSenicode(String senicode) {
        this.senicode = senicode;
    }
    @Override
    public String toString() {
        try {
            return "userid=" + URLEncoder.encode(userid, "UTF-8") +
                    "&requestno=" + URLEncoder.encode(requestno, "UTF-8")+
                    "&senicode=" + URLEncoder.encode(senicode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
