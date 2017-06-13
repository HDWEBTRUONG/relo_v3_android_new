package main.model;

/**
 * Created by tonkhanh on 6/8/17.
 */

public class HistoryPushDTO {
    private String timeHis;
    private String contentHis;
    private String urlHis;

    public HistoryPushDTO() {
    }

    public HistoryPushDTO(String timeHis, String contentHis, String urlHis) {
        this.timeHis = timeHis;
        this.contentHis = contentHis;
        this.urlHis = urlHis;
    }

    public String getTimeHis() {
        return timeHis;
    }

    public void setTimeHis(String timeHis) {
        this.timeHis = timeHis;
    }

    public String getContentHis() {
        return contentHis;
    }

    public void setContentHis(String contentHis) {
        this.contentHis = contentHis;
    }

    public String getUrlHis() {
        return urlHis;
    }

    public void setUrlHis(String urlHis) {
        this.urlHis = urlHis;
    }
}
