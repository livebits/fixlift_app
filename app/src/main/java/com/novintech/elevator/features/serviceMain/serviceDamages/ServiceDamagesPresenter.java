package com.novintech.elevator.features.serviceMain.serviceDamages;

import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.Filter;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;


public class ServiceDamagesPresenter extends BasePresenter<ServiceDamageView> {

//    private final DataManager dataManager;

    private final DataManager dataManager;
    public User user;

    public ServiceDamagesPresenter(DataManager dataManager) {

        this.dataManager = dataManager;
    }

    @Override
    public void attachView(ServiceDamageView mvpView) {
        super.attachView(mvpView);
    }

    public void getServiceDamages(Filter serviceDamagesFilter) {

        checkViewAttached();

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        getView().showProgress(true);
        dataManager
                .serviceDamages(serviceDamagesFilter)
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

    public void getBuildings() {

        checkViewAttached();

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        dataManager
                .getBuildings()
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        buildings -> {
                            Gson g = new Gson();
                            PrefUtil.putString(ElevatorApplication.ApplicationContext,
                                    "buildings", g.toJson(buildings));
                            getView().showBuilding(buildings);
                        },
                        throwable -> {
                            getView().showError(throwable.getMessage());
                        });
    }

}
