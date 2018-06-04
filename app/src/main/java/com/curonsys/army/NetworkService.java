
package com.curonsys.army;


/**
 * Created by Leejuhwan on 2018-05-27.
 */


import junit.runner.Version;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface NetworkService {
    @POST("/api/versions/")
    Call<Version> post_version(@Body Version version);

    @PATCH("/api/version/{pk}/")
    Call<Version> patch_version(@Path("pk") int pk, @Body Version version);

    @DELETE("/api/version/{pk}/")
    Call<Version> patch_version(@Path("pk") int pk);

    @GET("/api/versions/")
    Call<List<Version>> get_version();

    @GET("/api/versions/{pk}/")
    Call<Version> get_pk_version(@Path("pk") int pk);


   /* @POST("/api/restaurants/")
    Call<Restaurant> post_restaruant(@Body Restaurant restaruant);

    @PATCH("/api/restaurants/{pk}/")
    Call<Restaurant> patch_restaruant(@Path("pk") int pk, @Body Restaurant restaruant);

    @DELETE("/api/restaurants/{pk}/")
    Call<Restaurant> delete_restaruant(@Path("pk") int pk);

    @GET("/api/restaurants/")
    Call<List<Restaurant>> get_restaruant();

    @GET("/api/restaurants/{pk}/")
    Call<Restaurant> get_pk_restaruant(@Path("pk") int pk);

    @GET("/api/weathers/{pk}/restaurant_list/")
    Call<List<Restaurant>> get_weather_pk_restaruant(@Path("pk") int pk);*/


    //출처: http://duzi077.tistory.com/129?category=703147 [개발하는 두더지]

}