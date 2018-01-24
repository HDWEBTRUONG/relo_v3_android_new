package net.fukuri.memberapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 12/21/17.
 */

public class ValueLoginOld {
    @SerializedName("member_id")
    String member_id;
    @SerializedName("password")
    String password;
    @SerializedName("logined_year")
    String logined_year;
    @SerializedName("logined_month")
    String logined_month;
    @SerializedName("id")
    String id;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
