package com.novintech.elevator.features.auth.login;

import android.util.Log;

import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;

import retrofit2.HttpException;


public class LoginPresenter extends BasePresenter<LoginView> {

    private final DataManager dataManager;

    public User user;

    public LoginPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(LoginView mvpView) {
        super.attachView(mvpView);
    }

    public void login(String phoneNumber) {
        checkViewAttached();

        if(phoneNumber.equals("")) {
            getView().showError("لطفا شماره موبایل را وارد کنید");
            return;
        }

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        user = new User();
        user.mobile = phoneNumber;

        getView().showProgress(true);

        dataManager
                .login(user)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        user -> {

                            dataManager.saveUserToPref(user);

                            getView().showProgress(false);
                            getView().showVerifyPage(user);
                        },
                        throwable -> {
                            Gson g = new Gson();
                            HttpException error = (HttpException)throwable;
                            String errorBody = error.response().errorBody().string();
                            Log.e("xxx", errorBody);
                            getView().showProgress(false);
                            getView().showError("شماره وارد شده پیدا نشد");
                        });
    }
}
