package net.fukuri.memberapp.memberapp.model;

public class ForceupdateApp implements java.io.Serializable {
    private static final long serialVersionUID = -9216844344032583277L;
    private String app_name;
    private ForceupdateAppAndroid android;
    private ForceupdateAppIos ios;
    private String up_comment;

    public String getApp_name() {
        return this.app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public ForceupdateAppAndroid getAndroid() {
        return this.android;
    }

    public void setAndroid(ForceupdateAppAndroid android) {
        this.android = android;
    }

    public ForceupdateAppIos getIos() {
        return this.ios;
    }

    public void setIos(ForceupdateAppIos ios) {
        this.ios = ios;
    }

    public String getUp_comment() {
        return this.up_comment;
    }

    public void setUp_comment(String up_comment) {
        this.up_comment = up_comment;
    }
}
