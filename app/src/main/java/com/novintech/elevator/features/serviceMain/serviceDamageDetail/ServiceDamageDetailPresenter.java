package com.novintech.elevator.features.serviceMain.serviceDamageDetail;

import android.util.Log;

import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.SaveReport;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;


public class ServiceDamageDetailPresenter extends BasePresenter<DamageDetailView> {

    private final DataManager dataManager;

    public User user;

    public ServiceDamageDetailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(DamageDetailView mvpView) {
        super.attachView(mvpView);
    }

    public void getDamage(String damageId) {

        checkViewAttached();

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        Damage damage = new Damage();
        damage.id = damageId;

        getView().showProgress(true);
        dataManager
                .getDamage(damage)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        damageDetail -> {
                            getView().showProgress(false);
                            ElevatorApplication.damage = damageDetail;
                            getView().showDamageDetail(damageDetail);
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError("خطای سرور");
                        });
    }

    public void saveFactor(SaveReport saveReport) {

        checkViewAttached();

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        getView().showProgress(true);
        dataManager
                .saveDamageFactor(saveReport)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        damage -> {
                            getView().showProgress(false);
                            getView().showMessage("گزارش خرابی با موفقیت ثبت شد.");
                            getView().goBack();
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError(throwable.getMessage());
                        });
    }
}
