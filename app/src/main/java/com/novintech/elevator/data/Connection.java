package com.novintech.elevator.data;
import com.novintech.elevator.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connection {
    private static Retrofit retrofit = null;

//    public static Retrofit googleAPIs() {
//            retrofit = new Retrofit.Builder()
//              .baseUrl(Constants.GOOGLE_API_URL)
//              .addConverterFactory(GsonConverterFactory.create())
//              .build();
//        return retrofit;
//    }

    public static Retrofit loopbackAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
}