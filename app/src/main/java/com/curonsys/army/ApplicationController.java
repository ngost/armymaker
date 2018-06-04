package com.curonsys.army;

import android.app.Application;
import android.util.Log;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApplicationController extends  Application {
    public final static String TAG = "KJH";
    private static ApplicationController instance;
    public static ApplicationController getInstance(){return instance;}

    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
    }

    //클래스구현이 아직 안되었기 때문
    private NetworkService networkService;
    public NetworkService getNetworkService() {return networkService;}

    private String baseUrl;

    public ApplicationController() {
        super();
    }

    public void buildNetworkService (String ip, int port){
        synchronized (ApplicationController.class){
            if(networkService == null){
                baseUrl = String.format("http://%s:%d/", ip, port);
                Log.i(TAG, baseUrl);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                networkService = retrofit.create(NetworkService.class);
            }
        }
    }

    public void buildNetworkService(String ip){
        synchronized (ApplicationController.class){
            if (networkService == null){
                baseUrl = String.format("http://%s/", ip);
                Log.i(TAG, baseUrl);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                networkService = retrofit.create(NetworkService.class);
            }
        }
    }

}


//출처: http://duzi077.tistory.com/129?category=703147 [개발하는 두더지]*/