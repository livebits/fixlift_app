package com.novintech.elevator.features.main;

import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;


public class MainPresenter extends BasePresenter<MainView> {

    private final DataManager dataManager;

    public MainPresenter(DataManager dataManager) {

        this.dataManager = dataManager;
    }

    @Override
    public void attachView(MainView mvpView) {
        super.attachView(mvpView);
    }


    public void getCompanyInfo() {
        checkViewAttached();

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        getView().showProgress(true);
        dataManager
                .getCompanyInfo()
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        companyInfo -> {
                            getView().showProgress(false);
                            getView().showCompanyInfo(companyInfo);
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError("خطای سرور");
                        });

    }
}
