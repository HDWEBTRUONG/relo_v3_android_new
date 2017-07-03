package jp.relo.cluboff.model;

/**
 * Created by tonkhanh on 7/3/17.
 */

public class ControlWebEventBus {
    boolean callBack;
    boolean callForward;
    boolean callReload;

    public ControlWebEventBus(boolean callBack, boolean callForward, boolean callReload) {
        this.callBack = callBack;
        this.callForward = callForward;
        this.callReload = callReload;
    }

    public ControlWebEventBus() {
        this.callBack = false;
        this.callForward = false;
        this.callReload = false;
    }

    public boolean isCallBack() {
        return callBack;
    }

    public void setCallBack(boolean callBack) {
        this.callBack = callBack;
    }

    public boolean isCallForward() {
        return callForward;
    }

    public void setCallForward(boolean callForward) {
        this.callForward = callForward;
    }

    public boolean isCallReload() {
        return callReload;
    }

    public void setCallReload(boolean callReload) {
        this.callReload = callReload;
    }

    @Override
    public String toString() {
        return "ControlWebEventBus{" +
                "callBack=" + callBack +
                ", callForward=" + callForward +
                ", callReload=" + callReload +
                '}';
    }
}
