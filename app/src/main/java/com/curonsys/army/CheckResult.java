package com.curonsys.army;

/**
 * Created by Leejuhwan on 2018-05-27.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import junit.runner.Version;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckResult extends AppCompatActivity {

    //public final String TAG = "KJH";
    private NetworkService networkService;
    @BindView(R.id.tv1) TextView tv1;
    @BindView(R.id.tv2) TextView tv2;
    @BindView(R.id.tv3) TextView tv3;
    @BindView(R.id.tv4) TextView tv4;
    @BindView(R.id.tv5) TextView tv5;
    @BindView(R.id.tv6) TextView tv6;
    @BindView(R.id.tv7) TextView tv7;
    @BindView(R.id.tv8) TextView tv8;
    @BindView(R.id.tv9) TextView tv9;

    @OnClick(R.id.bt1)
    public void bt1_Click()
    {
        //GET
        Call<List<Version>> versionCall = networkService.get_version();
        versionCall.enqueue(new Callback<List<Version>>() {
            @Override
            public void onResponse(Call<List<Version>> call, Response<List<Version>> response) {
                if(response.isSuccessful()) {
                    List<Version> versionList = response.body();

                    String version_txt = "";
                    for(Version version : versionList){
                        version_txt += version.getVersion() + "\n";
                    }

                    tv1.setText(version_txt);
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "Status Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<List<Version>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.bt2)
    public void bt2_Click(){
        //POST
        Version version = new Version();
        version.setVersion("1.0.0.1");
        Call<Version> postCall = networkService.post_version(version);
        postCall.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                if( response.isSuccessful()) {
                    tv2.setText("등록");
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "Status Code : " + StatusCode);
                }

            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.bt3)
    public void bt3_click(){
        //PATCH
        Version version = new Version();
        version.setVersion("1.0.0.0.1");
        Call<Version> patchCall = networkService.patch_version(1, version);
        patchCall.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                if( response.isSuccessful()) {
                    tv3.setText("업데이트");
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "Status Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.bt4)
    public void bt4_click(){
        //DELETE
        Call<Version> deleteCall = networkService.delete_version(1);
        deleteCall.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                if( response.isSuccessful()) {
                    tv4.setText("삭제");
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "Status Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }

    /*@OnClick(R.id.bt5)
    public void bt5_click(){
        //Restaurant GET
        Call<List<Restaurant>> getCall = networkService.get_restaruant();
        getCall.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if( response.isSuccessful()) {
                    List<Restaurant> restaurantList = response.body();

                    String restaurant_txt = "";
                    for(Restaurant restaurant : restaurantList){
                        restaurant_txt += restaurant.getName() +
                                restaurant.getAddress() +
                                restaurant.getCategory() +
                                restaurant.getWeather() +
                                restaurant.getDistance() +
                                restaurant.getDescription() +
                                "\n";
                    }

                    tv5.setText(restaurant_txt);
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "Status Code : " + StatusCode + " Error Message : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.bt6)
    public void bt6_click(){
        //Restaurant POST
        Restaurant restaurant = new Restaurant();
        restaurant.setName("음식점1");
        restaurant.setAddress("장소1");
        restaurant.setCategory(3);
        restaurant.setWeather(3);
        restaurant.setDistance(3);
        restaurant.setDescription("설명1");

        Call<Restaurant> postCall = networkService.post_restaruant(restaurant);
        postCall.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if( response.isSuccessful()) {
                    tv6.setText("등록");
                } else {
                    int StatusCode = response.code();
                    try {
                        Log.i(ApplicationController.TAG, "Status Code : " + StatusCode + " Error Message : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.bt7)
    public void bt7_click(){
        //Restaurant PATCH
        //Full or partial patch available
        Restaurant restaurant = new Restaurant();
        restaurant.setName("이름22");
        restaurant.setAddress("장소22");
        restaurant.setCategory(3);
        restaurant.setWeather(1);
        restaurant.setDistance(2);
        restaurant.setDescription("장소22");

        Call<Restaurant> patchCall = networkService.patch_restaruant(1, restaurant);
        patchCall.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if( response.isSuccessful()) {
                    tv7.setText("업데이트");
                } else {
                    int StatusCode = response.code();
                    try {
                        Log.i(ApplicationController.TAG, "Status Code : " + StatusCode + " Error Message : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.bt8)
    public void bt8_click(){
        //Restaurant DELETE
        Call<Restaurant> deleteCall = networkService.delete_restaruant(2);
        deleteCall.enqueue(new Callback<Restaurant>() {

            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if ( response.isSuccessful()) {
                    tv8.setText("삭제");
                } else {
                    int StatusCode = response.code();
                    try {
                        Log.i(ApplicationController.TAG, "Status Code : " + StatusCode + " Error Message : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.bt9)
    public void bt9_click(){
        Call<List<Restaurant>> get_weather_pk_Call = networkService.get_weather_pk_restaruant(1);
        get_weather_pk_Call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if( response.isSuccessful()) {
                    List<Restaurant> restaurantList = response.body();

                    String restaurant_txt = "";
                    for(Restaurant restaurant : restaurantList){
                        restaurant_txt += restaurant.getName() +
                                restaurant.getAddress() +
                                restaurant.getCategory() +
                                restaurant.getWeather() +
                                restaurant.getDistance() +
                                restaurant.getDescription() +
                                "\n";
                    }

                    tv9.setText(restaurant_txt);
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "Status Code : " + StatusCode + " Error Message : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("61fa624f.ngrok.io");
        //application.buildNetworkService("자신의 ip", 8000);
        networkService = ApplicationController.getInstance().getNetworkService();
    }
}


//출처: http://duzi077.tistory.com/129?category=703147 [개발하는 두더지]
