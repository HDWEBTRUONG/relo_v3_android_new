package net.fukuri.memberapp.model;

/**
 * Created by tonkhanh on 6/8/17.
 */

public class HistoryPushDTO {
    private int idHis;
    private String titlePush;
    private long timeHis;
    private String contentHis;
    private String wHis;
    private String xHis;
    private String yHis;
    private String zHis;
    private int isReaded;

    public HistoryPushDTO() {
    }

    public String getTitlePush() {
        return titlePush;
    }

    public void setTitlePush(String titlePush) {
        this.titlePush = titlePush;
    }

    public long getTimeHis() {
        return timeHis;
    }

    public void setTimeHis(long timeHis) {
        this.timeHis = timeHis;
    }

    public String getContentHis() {
        return contentHis;
    }

    public void setContentHis(String contentHis) {
        this.contentHis = contentHis;
    }

    public int getIdHis() {
        return idHis;
    }

    public void setIdHis(int idHis) {
        this.idHis = idHis;
    }

    public String getwHis() {
        return wHis;
    }

    public void setwHis(String wHis) {
        this.wHis = wHis;
    }

    public String getxHis() {
        return xHis;
    }

    public void setxHis(String xHis) {
        this.xHis = xHis;
    }

    public String getyHis() {
        return yHis;
    }

    public void setyHis(String yHis) {
        this.yHis = yHis;
    }

    public String getzHis() {
        return zHis;
    }

    public void setzHis(String zrlHis) {
        this.zHis = zrlHis;
    }

    public int getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(int isReaded) {
        this.isReaded = isReaded;
    }
}
