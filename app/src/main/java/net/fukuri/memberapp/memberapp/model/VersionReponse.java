package net.fukuri.memberapp.memberapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 5/23/17.
 */

public class VersionReponse{
    @SerializedName("version")
    String version;
    public String getVersion() {
        return version;
    }
}
