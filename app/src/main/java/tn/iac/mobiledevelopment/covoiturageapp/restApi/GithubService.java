package tn.iac.mobiledevelopment.covoiturageapp.restApi;

/**
 * Created by S4M37 on 19/01/2016.
 */

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;
import tn.iac.mobiledevelopment.covoiturageapp.connectivity.model.User;

public interface GithubService {

    public static final String ENDPOINT = "http://192.168.1.9/CovoiturageWebApp/public/";

    @FormUrlEncoded
    @POST("/user/add")
    int storeNote(@Header("Authorization") String authorization, @Field("id_Etudiant") String id_Etudiant, @Field("id_Station") int id_Station, @Field("inputs[]") int[] notes) throws RuntimeException;

    @FormUrlEncoded
    @POST("/mobile/signin")
    void signin(@Field("login") String login, @Field("password") String password, Callback<Response> cb);
}