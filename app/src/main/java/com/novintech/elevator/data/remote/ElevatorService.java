package com.novintech.elevator.data.remote;

import com.novintech.elevator.data.model.response.Checklist;
import com.novintech.elevator.data.model.response.CompanyInfo;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.FactorItem;
import com.novintech.elevator.data.model.response.SaveReport;
import com.novintech.elevator.data.model.response.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ElevatorService {

    @POST("AppUsers/loginAppUser")
    Single<User> login(@Body User user);

    @POST("AppUsers/checkVerification")
    Single<User> verify(@Body User user);

    @GET("Damages/serviceDamages")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<List<Damage>> serviceDamages(@Query("access_token") String accessToken,
                                        @Query("filter") String serviceDamageFilter);


    @GET("AppUsers/buildings")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<List<User>> buildings(@Query("access_token") String accessToken);

    @GET("Damages")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<List<Damage>> dailySchedule(@Query("access_token") String accessToken,
                                       @Query("filter") String filter);


    @POST("Damages/saveReportFactor")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<List<FactorItem>> saveReportFactor(@Query("access_token") String accessToken,
                                              @Body SaveReport damageObject);



    @GET("Damages/customerDamages")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<List<Damage>> customerDamages(@Query("access_token") String accessToken);

    @POST("Damages")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<Damage> addDamage(@Query("access_token") String accessToken,
                                        @Body Damage damage);

    @POST("Damages/GetDamage")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<Damage> getDamage(@Query("access_token") String accessToken,
                             @Body Damage damage);

    @GET("AppUsers/customerFactorsToPay")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<List<Damage>> customerFactorsToPay(@Query("access_token") String accessToken);

    @GET("CheckLists/ShowCheckLists")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<List<Checklist>> getChecklists(@Query("access_token") String accessToken);

    @GET("Settings/GetMyCompanyInfo")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Single<CompanyInfo> getCompanyInfo(@Query("access_token") String accessToken);
}

