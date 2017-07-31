package jp.relo.cluboff.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 6/23/17.
 */

public class BaseReponse {
    @SerializedName("Header")
    private Header header;
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
