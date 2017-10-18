package jp.relo.cluboff.api;


import jp.relo.cluboff.model.LoginReponse;
import jp.relo.cluboff.model.VersionReponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
    @POST("/fkr/apps/oil/ffoil_main.cfm")
    Observable<LoginReponse> logon(@Field("LOGINID") String LOGINID,@Field("PASSWORD") String PASSWORD);

    @FormUrlEncoded
    @POST("/fkr/apps/oil/ffoil_main.cfm")
    Call<ResponseBody> logonHTML(@Field("LOGINID") String LOGINID, @Field("PASSWORD") String PASSWORD);

    @FormUrlEncoded
    @POST("/reloclub/re_post_app.cfm")
    Call<ResponseBody> memberAuthHTML(@Field("APPU") String LOGINID, @Field("APPP") String PASSWORD);

}
