package net.fukuri.memberapp.model;

import framework.phvtUtils.AppLog;

/**
 * Created by tonkhanh on 11/1/17.
 */

public class XMLUpdate {
    private String url;
    private String area;

    public XMLUpdate(String url, String area) {
        this.url = url;
        this.area = area;


        // debug
        AppLog.log_url("url: "+url+"  area: "+ area);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
