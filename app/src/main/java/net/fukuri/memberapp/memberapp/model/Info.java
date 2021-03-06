package net.fukuri.memberapp.memberapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 7/5/17.
 */

public class Info {
    @SerializedName("brandid")
    private String brandid;
    @SerializedName("userid")
    private String userid;
    @SerializedName("url")
    private String url;

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
