package main.api;


import main.model.LoginReponse;
import main.model.LoginRequest;
import main.model.VersionReponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tonkhanh on 5/23/17.
 */

public interface ApiInterface {
    @GET("relo_ver.json")
    Observable<VersionReponse> checkVersion();


    @POST("coa/coa_login.cfm")
    Observable<LoginReponse> logon(@Body LoginRequest login);

}
