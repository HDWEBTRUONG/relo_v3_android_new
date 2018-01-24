package net.fukuri.memberapp.model;

/**
 * Created by tonkhanh on 7/6/17.
 */

public class AreaCouponPost {
    private String p_s1;
    private String p_s6;
    private String p_s7;
    private String p_s22;
    private String p_s25;
    private String p_s29;
    private String p_s34;
    private String p_s36;

    public AreaCouponPost() {
        this.p_s1 = "0";
        this.p_s6 = "1";
        this.p_s7 = "";
        this.p_s22 = "1";
        this.p_s25 = "90";
        this.p_s29 = "1";
        this.p_s34 = "1";
        this.p_s36 = "205,270,330,331,380,406,407,408,409,410,411,412,413,414,415,416,417,418,420,422,424,425,427,428,430,431,432,433,434,435,436,437,440,441,444,445,446,447,448,449,450,451,459,463,464,475,200,201,202,203,208,210,211,212,214,215,216,217,218,219,220,221,222,223,224,225,226,228,230,231,232,233,234,235,236,237,241,245,248,250,251,253,260,262,263,265,266,269,271,272,276,278,279,280,281,282,285,286,288,289,290,292,293,294,295,296,298,299,300,301,303,308,309,310,311,312,314,317,319,320,321,322,324,326,327,328,329,333,334,335,338,341,342,344,345,346,349";
    }

    public String getP_s1() {
        return p_s1;
    }

    public void setP_s1(String p_s1) {
        this.p_s1 = p_s1;
    }

    public String getP_s6() {
        return p_s6;
    }

    public void setP_s6(String p_s6) {
        this.p_s6 = p_s6;
    }

    public String getP_s7() {
        return p_s7;
    }

    public void setP_s7(String p_s7) {
        this.p_s7 = p_s7;
    }

    public String getP_s22() {
        return p_s22;
    }

    public void setP_s22(String p_s22) {
        this.p_s22 = p_s22;
    }

    public String getP_s25() {
        return p_s25;
    }

    public void setP_s25(String p_s25) {
        this.p_s25 = p_s25;
    }

    public String getP_s29() {
        return p_s29;
    }

    public void setP_s29(String p_s29) {
        this.p_s29 = p_s29;
    }

    public String getP_s34() {
        return p_s34;
    }

    public void setP_s34(String p_s34) {
        this.p_s34 = p_s34;
    }

    public String getP_s36() {
        return p_s36;
    }

    public void setP_s36(String p_s36) {
        this.p_s36 = p_s36;
    }

    @Override
    public String toString() {
        try {
            return "p_s1=" + p_s1 +
                    "&p_s6=" + p_s6 +
                    "&p_s7=" + p_s7+
                    "&p_s22=" + p_s22 +
                    "&p_s25=" + p_s25 +
                    "&p_s29=" + p_s29 +
                    "&p_s34=" + p_s34 +
                    "&p_s36=" + p_s36;
        } catch (Exception e) {
            return e.toString();
        }
    }
}
