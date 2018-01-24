package net.fukuri.memberapp.memberapp.model;

/**
 * Created by tonkhanh on 8/15/17.
 */

public class ReloadEvent {
    boolean isReload;

    public ReloadEvent(boolean isReload) {
        this.isReload = isReload;
    }

    public boolean isReload() {
        return isReload;
    }

    public void setReload(boolean reload) {
        isReload = reload;
    }
}
