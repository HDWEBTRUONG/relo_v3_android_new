package net.fukuri.memberapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 7/5/17.
 */

public class Header {
    @SerializedName("status")
    private String status;
    @SerializedName("detail")
    private String detail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
