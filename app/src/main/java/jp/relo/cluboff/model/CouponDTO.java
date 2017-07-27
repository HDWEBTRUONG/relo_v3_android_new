package jp.relo.cluboff.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 5/23/17.
 */
public class CouponDTO{
    public static final String TAG_COUPON = "COUPON";
    public static final String TAG_ITEM = "ITEM";
    public static final String TAG_SHGRID = "SHGRID";
    public static final String TAG_CATEGORY_ID = "CATEGORY_ID";
    public static final String TAG_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String TAG_COUPON_NAME = "COUPON_NAME";
    public static final String TAG_COUPON_IMAGE_PATH = "COUPON_IMAGE_PATH";
    public static final String TAG_COUPON_TYPE = "COUPON_TYPE";
    public static final String TAG_LINK_PATH = "LINK_PATH";
    public static final String TAG_EXPIRATION_FROM = "EXPIRATION_FROM";
    public static final String TAG_EXPIRATION_TO = "EXPIRATION_TO";
    public static final String TAG_PRIORITY = "PRIORITY";
    public static final String TAG_ADD_BLAND = "ADD_BLAND";
    public static final String TAG_MEMO = "MEMO";


    @SerializedName("shgrid")
    private String shgrid;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("coupon_name")
    private String coupon_name;

    @SerializedName("coupon_image_path")
    private String coupon_image_path;

    @SerializedName("coupon_type")
    private String coupon_type;

    @SerializedName("link_path")
    private String link_path;

    @SerializedName("expiration_from")
    private String expiration_from;

    @SerializedName("expiration_to")
    private String expiration_to;

    @SerializedName("priority")
    private int priority;

    @Expose
    @SerializedName("memo")
    private String memo;

    @Expose
    @SerializedName("add_bland")
    private String add_bland;

    private int liked;

    public String getShgrid() {
        return shgrid;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public String getCoupon_image_path() {
        return coupon_image_path;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public String getLink_path() {
        return link_path;
    }

    public String getExpiration_from() {
        return expiration_from;
    }

    public String getExpiration_to() {
        return expiration_to;
    }

    public int getPriority() {
        return priority;
    }

    public String getMemo() {
        return memo;
    }

    public String getAdd_bland() {
        return add_bland;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public void setShgrid(String shgrid) {
        this.shgrid = shgrid;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public void setCoupon_image_path(String coupon_image_path) {
        this.coupon_image_path = coupon_image_path;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public void setLink_path(String link_path) {
        this.link_path = link_path;
    }

    public void setExpiration_from(String expiration_from) {
        this.expiration_from = expiration_from;
    }

    public void setExpiration_to(String expiration_to) {
        this.expiration_to = expiration_to;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setAdd_bland(String add_bland) {
        this.add_bland = add_bland;
    }
}
