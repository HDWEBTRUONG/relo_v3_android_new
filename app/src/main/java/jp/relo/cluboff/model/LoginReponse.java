package jp.relo.cluboff.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 6/23/17.
 */

public class LoginReponse {
    @SerializedName("Header")
    private Header header;
    @SerializedName("Info")
    private Info info;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

}
