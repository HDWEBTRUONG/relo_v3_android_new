package jp.relo.cluboff.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 6/23/17.
 */

public class LoginRequest {
    @SerializedName("kaiinno")
    private String kaiinno;
    @SerializedName("emailad")
    private String emailad;
   // @SerializedName("pass")
    //private String pass;
    @SerializedName("brandid")
    private String brandid;

    public LoginRequest(String kaiinno, String emailad, String pass) {
        this.kaiinno = kaiinno;
        this.emailad = emailad;
        //this.pass = pass;
        this.brandid = pass;
    }

    public String getKaiinno() {
        return kaiinno;
    }

    public void setKaiinno(String kaiinno) {
        this.kaiinno = kaiinno;
    }

    public String getEmailad() {
        return emailad;
    }

    public void setEmailad(String emailad) {
        this.emailad = emailad;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }
}
