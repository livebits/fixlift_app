package com.novintech.elevator.features.auth.verify;

import android.util.Log;

import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;


public class VerifyPresenter extends BasePresenter<VerifyView> {

    private final DataManager dataManager;

    public User user;

    public VerifyPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(VerifyView mvpView) {
        super.attachView(mvpView);
    }

    public void verify(String code, String fcmToken) {
        checkViewAttached();

        if(code.equals("")) {
            getView().showError("لطفا کد ارسالی را وارد کنید");
            return;
        }

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        getView().showProgress(true);

        User user = dataManager.getUserFromPref();
        user.code = code;
        user.fcmToken = fcmToken;

        dataManager
                .verify(user)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        userRX -> {
                            dataManager.saveUserToPref(userRX);
                            ElevatorApplication.Token = userRX.token.id;
                            ElevatorApplication.AppUser = userRX;

                            getView().showProgress(false);
                            if(userRX.role.equals("customer")){

                                getView().showMainActivity();
                            }
                            else if(userRX.role.equals("service")){

                                getView().showServiceMainActivity();
                            }

                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError("کد وارد شده اشتباه است.");
                        });
    }
}
