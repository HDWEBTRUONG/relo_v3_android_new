package jp.relo.cluboff.model;

import android.content.Context;

import jp.relo.cluboff.util.ConstansSharedPerence;
import jp.relo.cluboff.util.LoginSharedPreference;

/**
 * Created by tonkhanh on 8/7/17.
 */

public class SaveLogin {
    private static SaveLogin saveLogin;
    private static Info info;
    private static LoginRequest loginRequest;

    public static SaveLogin getInstance(Context mContext){
        if(saveLogin==null){
            loginRequest = LoginSharedPreference.getInstance(mContext).get(ConstansSharedPerence.TAG_LOGIN_INPUT, LoginRequest.class);
            info = LoginSharedPreference.getInstance(mContext).get(ConstansSharedPerence.TAG_LOGIN_SAVE, Info.class);
            saveLogin = new SaveLogin(info,loginRequest) ;

        }
        return saveLogin;
    }

    public SaveLogin(Info _info,LoginRequest _loginRequest) {
        info = _info;
        loginRequest = _loginRequest;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        SaveLogin.info = info;
    }

    public LoginRequest getLoginRequest() {
        return loginRequest;
    }

    public void setLoginRequest(LoginRequest loginRequest) {
        SaveLogin.loginRequest = loginRequest;
    }

    public String getUserName() {
        if(loginRequest==null) return "";
        return loginRequest.getLOGINID();
    }


    public String getPass() {
        return loginRequest.getPASSWORD();
    }


    public String getBrandidEncrypt() {
        return info.getBrandid();
    }


    public String getUseridEncrypt() {
        if(info==null) return "";
        return info.getUserid();
    }

    public String getUrlEncrypt() {
        return info.getUrl();
    }

}
