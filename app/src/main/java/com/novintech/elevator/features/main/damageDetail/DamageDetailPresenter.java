package com.novintech.elevator.features.main.damageDetail;

import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;

import java.util.ArrayList;
import java.util.List;


public class DamageDetailPresenter extends BasePresenter<DamageDetailView> {

    private final DataManager dataManager;

    public User user;

    public DamageDetailPresenter(DataManager dataManager) {

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
//                            Gson g = new Gson();
                            ElevatorApplication.damage = damageDetail;
                            getView().showDamageDetail();
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError("خطای سرور");
                        });
    }
}
