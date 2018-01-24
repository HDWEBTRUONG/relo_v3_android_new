package net.fukuri.memberapp.memberapp.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by tonkhanh on 7/19/17.
 */

public class PostDetailType1 {
    private String kid = "";
    private String brndid = "";
    String shgrid = "";


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

    public String getShgrid() {
        return shgrid;
    }

    public void setShgrid(String shgrid) {
        this.shgrid = shgrid;
    }

    @Override
    public String toString() {
        try {
            return "kid=" + URLEncoder.encode(kid, "UTF-8") +
                    "&shgrid=" + URLEncoder.encode(shgrid, "UTF-8")+
                    "&brndid=" + URLEncoder.encode(brndid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
