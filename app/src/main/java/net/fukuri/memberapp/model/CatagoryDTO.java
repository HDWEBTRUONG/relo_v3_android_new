package net.fukuri.memberapp.model;

/**
 * Created by tonkhanh on 7/11/17.
 */

public class CatagoryDTO {
    String catagoryID;
    String getCatagoryName;

    public CatagoryDTO() {
    }

    public CatagoryDTO(String catagoryID, String getCatagoryName) {
        this.catagoryID = catagoryID;
        this.getCatagoryName = getCatagoryName;
    }

    public String getCatagoryID() {
        return catagoryID;
    }

    public void setCatagoryID(String catagoryID) {
        this.catagoryID = catagoryID;
    }

    public String getGetCatagoryName() {
        return getCatagoryName;
    }

    public void setGetCatagoryName(String getCatagoryName) {
        this.getCatagoryName = getCatagoryName;
    }

    @Override
    public String toString() {
        return getCatagoryName ;
    }
}
