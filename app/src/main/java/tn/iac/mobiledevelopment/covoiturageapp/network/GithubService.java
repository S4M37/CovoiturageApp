package tn.iac.mobiledevelopment.covoiturageapp.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by S4M37 on 19/01/2016.
 */


public interface GitHubService {
    public static final String baseUrl = "http://192.168.1.7/CovoiturageWebApp/public/";

    @FormUrlEncoded
    @POST("mobile/signup")
    Call<ResponseBody> storeUser(@Field("nom") String nom, @Field("prenom") String prenom, @Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("mobile/signin")
    Call<ResponseBody> signin(@Field("login") String login, @Field("password") String password);
}