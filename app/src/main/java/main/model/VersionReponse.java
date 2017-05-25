package main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 5/23/17.
 */

public class VersionReponse {
    @SerializedName("project")
    String project;
    @SerializedName("version")
    String version;

    public String getProject() {
        return project;
    }

    public String getVersion() {
        return version;
    }
}
