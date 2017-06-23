package main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 6/23/17.
 */

public class BaseReponse {
    @SerializedName("status")
    protected int status;
    @SerializedName("detail")
    protected String detail;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
