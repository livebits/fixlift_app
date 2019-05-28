package com.novintech.elevator.features.serviceMain.serviceDamageDetail.Checklist;

import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.model.response.Checklist;
import com.novintech.elevator.data.model.response.SaveReport;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.features.serviceMain.serviceDamageDetail.DamageDetailView;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;

import java.util.List;


public class ChecklistPresenter extends BasePresenter<ChecklistView> {

    private final DataManager dataManager;

    public User user;

    public ChecklistPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void getChecklistItems(){

        checkViewAttached();

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        getView().showProgress(true);
        dataManager
                .getChecklistItems()
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        checklists -> {
                            getView().showProgress(false);
                            ElevatorApplication.checklists = checklists;
                            getView().showCheckLists(checklists);
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError(throwable.getMessage());
                        });
    }
}
