package com.novintech.elevator.features.serviceMain.dailySchedule;

import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.MvpView;

import java.util.List;

public interface DailyView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void showMessage(String message);

    void showDailyList(List<Damage> damages);
}
