package main.api;


import main.model.VersionReponse;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by tonkhanh on 5/23/17.
 */

public interface ApiInterface {
    @GET("relo_ver.json")
    Observable<VersionReponse> checkVersion();
}
