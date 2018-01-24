package net.fukuri.memberapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 6/23/17.
 */

public class LoginRequest {
    @SerializedName("LOGINID")
    private String LOGINID;
    @SerializedName("PASSWORD")
    private String PASSWORD;

    public LoginRequest(String LOGINID, String PASSWORD) {
        this.LOGINID = LOGINID;
        this.PASSWORD = PASSWORD;
    }

    public String getLOGINID() {
        return LOGINID;
    }

    public void setLOGINID(String LOGINID) {
        this.LOGINID = LOGINID;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}
