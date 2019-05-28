package com.novintech.elevator.features.serviceMain.serviceDamageDetail;

import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.MvpView;

public interface DamageDetailView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void showMessage(String message);

    void showDamageDetail(Damage damage);

    void goBack();
}
