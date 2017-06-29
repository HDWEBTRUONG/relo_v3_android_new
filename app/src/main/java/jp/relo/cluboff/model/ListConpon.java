package jp.relo.cluboff.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tonkhanh on 5/23/17.
 */

public class ListConpon {
    @SerializedName("item")
    @Expose
    private List<CouponDTO> coupon = null;

    public List<CouponDTO> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<CouponDTO> coupon) {
        this.coupon = coupon;
    }
}
