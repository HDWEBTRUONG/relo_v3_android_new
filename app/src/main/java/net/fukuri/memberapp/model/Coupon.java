package net.fukuri.memberapp.model;

/**
 * Created by quynguyen on 3/24/17.
 */

public class Coupon {

    public String image;
    public String categoryName;
    public String companyName;
    public String durationCoupon;

    public String getImage() {
        return image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDurationCoupon() {
        return durationCoupon;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDurationCoupon(String durationCoupon) {
        this.durationCoupon = durationCoupon;
    }
}
