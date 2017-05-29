package main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 5/23/17.
 */
public class CouponDTO{

    @SerializedName("category")
    private String category;
    @SerializedName("name")
    private String name;
    @SerializedName("limit")
    private String limit;
    @SerializedName("p_url")
    private String p_url;
    @SerializedName("c_url")
    private String c_url;
    @SerializedName("like")
    private int like;
    @SerializedName("id")
    private int id;



    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getLimit() {
        return limit;
    }

    public String getP_url() {
        return p_url;
    }

    public String getC_url() {
        return c_url;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public void setP_url(String p_url) {
        this.p_url = p_url;
    }

    public void setC_url(String c_url) {
        this.c_url = c_url;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
