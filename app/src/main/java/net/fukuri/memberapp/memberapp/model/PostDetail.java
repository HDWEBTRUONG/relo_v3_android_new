package net.fukuri.memberapp.memberapp.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by tonkhanh on 7/19/17.
 */

public class PostDetail {
    String kaiinno;
    String requestno;
    String senicode;

    public PostDetail() {
        this.kaiinno = "";
        this.requestno = "";
        this.senicode = "";
    }

    public String getKaiinno() {
        return kaiinno;
    }

    public void setKaiinno(String userid) {
        this.kaiinno = userid;
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
            return "kaiinno=" + URLEncoder.encode(kaiinno, "UTF-8") +
                    "&requestno=" + URLEncoder.encode(requestno, "UTF-8")+
                    "&senicode=" + URLEncoder.encode(senicode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
