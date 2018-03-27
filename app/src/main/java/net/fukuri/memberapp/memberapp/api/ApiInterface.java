package net.fukuri.memberapp.memberapp.api;


import net.fukuri.memberapp.memberapp.model.BaseReponse;
import net.fukuri.memberapp.memberapp.model.ForceupdateApp;
import net.fukuri.memberapp.memberapp.model.LoginReponse;
import net.fukuri.memberapp.memberapp.model.VersionReponse;
import net.fukuri.memberapp.memberapp.util.Constant;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by tonkhanh on 5/23/17.
 */

public interface ApiInterface {
    @GET(Constant.URL_FORCEUPDATE_FULL)
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


    @FormUrlEncoded
    @POST("/log/create-logs.php")
    Call<ResponseBody> writeLog(@Field("request_no") String request_no, @Field("member_id") String member_id);

    @GET
    Call<ForceupdateApp> checkForceupdateApp(@Url String url);


}
