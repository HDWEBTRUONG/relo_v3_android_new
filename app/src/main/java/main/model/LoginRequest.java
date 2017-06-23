package main.model;

/**
 * Created by tonkhanh on 6/23/17.
 */

public class LoginRequest {
    private String kaiinno;
    private String emailad;
    private String pass;

    public LoginRequest(String kaiinno, String emailad, String pass) {
        this.kaiinno = kaiinno;
        this.emailad = emailad;
        this.pass = pass;
    }

    public String getKaiinno() {
        return kaiinno;
    }

    public void setKaiinno(String kaiinno) {
        this.kaiinno = kaiinno;
    }

    public String getEmailad() {
        return emailad;
    }

    public void setEmailad(String emailad) {
        this.emailad = emailad;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
