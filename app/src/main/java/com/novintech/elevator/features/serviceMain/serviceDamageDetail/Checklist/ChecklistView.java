package com.novintech.elevator.features.serviceMain.serviceDamageDetail.Checklist;

import com.novintech.elevator.data.model.response.Checklist;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.MvpView;

import java.util.List;

public interface ChecklistView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void showMessage(String message);

    void showCheckLists(List<Checklist> checkLists);

    void goBack();
}
