package com.novintech.elevator.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Date;
import java.util.List;


import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.Checklist;
import com.novintech.elevator.data.model.response.CompanyInfo;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.FactorItem;
import com.novintech.elevator.data.model.response.Filter;
import com.novintech.elevator.data.model.response.SaveReport;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.data.remote.ElevatorService;

import io.reactivex.Single;

public class DataManager {

    private ElevatorService elevatorService;
    Gson g = new Gson();

    public DataManager(ElevatorService elevatorService) {

        this.elevatorService = elevatorService;
    }

    public Single<User> login(User user) {

        return elevatorService.login(user);
    }

    public Single<User> verify(User user) {

        return elevatorService.verify(user);
    }

    public Single<List<Damage>> customerDamages() {

        return elevatorService.customerDamages(ElevatorApplication.Token);
    }

    public Single<Damage> getDamage(Damage damage) {

        return elevatorService.getDamage(ElevatorApplication.Token, damage);
    }

    public  Single<List<Damage>> serviceDamages(Filter filter) {


        return elevatorService.serviceDamages(ElevatorApplication.Token, g.toJson(filter));
    }

    public  Single<List<User>> getBuildings() {


        return elevatorService.buildings(ElevatorApplication.Token);
    }

    public  Single<List<Damage>> dailySchedule(String date) {

        String FilterContainer  = "{\n" +
                "  \"where\": {\n" +
                "    \"and\": [\n" +
                "      {\n" +
                "        \"serviceId\": " + ElevatorApplication.AppUser.id + "\n" +
                "      },\n" +
                "      {\n" +
                "        \"visitDate\": {\n" +
                "          \"between\": [\n" +
                "            \"" + date + " 00:00:00\",\n" +
                "            \"" + date + " 23:59:59\"\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"include\": [\"appUser\",\n" +
                "                \"serviceUser\",\n" +
                "                \"reports\",\n" +
                "                {\n" +
                "                    \"relation\": \"factors\",\n" +
                "                    \"scope\":{\n" +
                "                        \"include\": [\"payments\", \"factorItems\"]\n" +
                "                    }\n" +
                "                }],\"order\":\"id desc\"\n" +
                "}";

        return elevatorService.dailySchedule(ElevatorApplication.Token, FilterContainer);
    }

    public Single<Damage> addDamage(Damage damage){

        return  elevatorService.addDamage(ElevatorApplication.Token, damage);
    }

    public Single<List<FactorItem>> saveDamageFactor(SaveReport saveReport){

        return  elevatorService.saveReportFactor(ElevatorApplication.Token, saveReport);
    }

    public Single<List<Damage>> customerFactorsToPay(){

        return  elevatorService.customerFactorsToPay(ElevatorApplication.Token);
    }

    public Single<List<Checklist>> getChecklistItems(){

        return  elevatorService.getChecklists(ElevatorApplication.Token);
    }

    public Single<CompanyInfo> getCompanyInfo(){

        return  elevatorService.getCompanyInfo(ElevatorApplication.Token);
    }

    public void saveUserToPref(User user) {

        Gson gson = new Gson();
        PrefUtil.putString(ElevatorApplication.ApplicationContext, "user", gson.toJson(user));
    }

    public User getUserFromPref() {

        Gson gson = new Gson();
        return  gson.fromJson(PrefUtil.getString(ElevatorApplication.ApplicationContext, "user"), User.class);
    }

}
