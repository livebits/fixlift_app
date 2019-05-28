package com.novintech.elevator.features.main.damages;

import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;


public class DamagesPresenter extends BasePresenter<DamageView> {

//    private final DataManager dataManager;

    private final DataManager dataManager;
    public User user;

    public DamagesPresenter(DataManager dataManager) {

        this.dataManager = dataManager;
    }

    @Override
    public void attachView(DamageView mvpView) {
        super.attachView(mvpView);
    }

    public void getCustomerDamages() {

        checkViewAttached();

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        getView().showProgress(true);
        dataManager
                .customerDamages()
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        damageList -> {
                            getView().showProgress(false);
                            Gson g = new Gson();
                            PrefUtil.putString(ElevatorApplication.ApplicationContext,
                                    "damages", g.toJson(damageList));
                            getView().showDamagesList(damageList);
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError("خطای سرور");
                        });
    }

}
