package com.novintech.elevator.features.main.newDamage;

import android.util.Log;

import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;


public class NewDamagePresenter extends BasePresenter<NewDamageView> {

    private final DataManager dataManager;

    public NewDamagePresenter(DataManager dataManager) {

        this.dataManager = dataManager;
    }

    @Override
    public void attachView(NewDamageView mvpView) {
        super.attachView(mvpView);
    }

    public void sendDamage(String description, String address) {

        checkViewAttached();
        if(description.equals("")) {
            getView().showError("لطفا شرح خرابی را وارد کنید");
            return;
        }

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        Damage damage = new Damage();
        damage.description = description;
        damage.appUserId = ElevatorApplication.AppUser.id + "";
        getView().showProgress(true);

        dataManager
                .addDamage(damage)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        damage1 -> {
                            getView().showProgress(false);
                            getView().showDamagesList();
                            getView().dismiss();
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError("خطای سرور");
                        });
    }
}
