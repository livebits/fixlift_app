package com.novintech.elevator.features.serviceMain.dailySchedule;

import android.util.Log;

import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.HttpException;


public class DailyPresenter extends BasePresenter<DailyView> {

    private final DataManager dataManager;

    public User user;

    public DailyPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(DailyView mvpView) {
        super.attachView(mvpView);
    }

    public void getDamages(String date) {

        checkViewAttached();
        Log.e("xxx", date);

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        getView().showProgress(true);

        dataManager
                .dailySchedule(date)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        damages -> {
                            getView().showProgress(false);
                            getView().showDailyList(damages);
                        },
                        throwable -> {

                            Gson g = new Gson();
                            HttpException error = (HttpException)throwable;
                            String errorBody = error.response().errorBody().string();
                            Log.e("xxx", errorBody);
                            getView().showProgress(false);
                            getView().showError(throwable.getMessage());
                        });
    }
}
