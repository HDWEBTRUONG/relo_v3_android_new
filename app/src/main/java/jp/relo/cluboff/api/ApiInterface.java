package jp.relo.cluboff.api;


import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.model.VersionReponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tonkhanh on 5/23/17.
 */

public interface ApiInterface {
    @GET("coa/xml_version.json")
    Observable<VersionReponse> checkVersion();

    @FormUrlEncoded
    @POST("coa/coa_login.cfm")
    Observable<LoginReponse> logon(@Field("kaiinno") String kaiinno,@Field("emailad") String emailad,@Field("brandid") String brandid);

}
