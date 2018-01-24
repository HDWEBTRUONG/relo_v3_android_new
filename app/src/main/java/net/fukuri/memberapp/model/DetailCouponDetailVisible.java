package net.fukuri.memberapp.model;

/**
 * Created by tonkhanh on 7/27/17.
 */

public class DetailCouponDetailVisible {
    boolean isVisible;

    public DetailCouponDetailVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
