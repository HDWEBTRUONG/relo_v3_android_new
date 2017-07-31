package jp.relo.cluboff.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by tonkhanh on 7/19/17.
 */

public class PostDetailType1 {
    private String kid = "";
    private String brndid = "";
    String requestno = "";


    public PostDetailType1() {
        this.kid = "";
        this.brndid = "";
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getBrndid() {
        return brndid;
    }

    public void setBrndid(String brndid) {
        this.brndid = brndid;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    @Override
    public String toString() {
        try {
            return "kid=" + URLEncoder.encode(kid, "UTF-8") +
                    "&requestno=" + URLEncoder.encode(requestno, "UTF-8")+
                    "&brndid=" + URLEncoder.encode(brndid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
